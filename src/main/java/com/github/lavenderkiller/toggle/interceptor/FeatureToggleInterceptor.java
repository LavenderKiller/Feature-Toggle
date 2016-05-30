package com.github.lavenderkiller.toggle.interceptor;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.github.lavenderkiller.toggle.annotation.SupportFeatureToggle;
import com.github.lavenderkiller.toggle.feature.reader.util.ConsulReader;

@Component
public class FeatureToggleInterceptor extends HandlerInterceptorAdapter {
    
    @Autowired private ConsulReader consulReader;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
	HandlerMethod handlerMethod = (HandlerMethod) handler;
	Method method = handlerMethod.getMethod();

	if (method.isAnnotationPresent(SupportFeatureToggle.class)) {
	    String name = method.getAnnotation(SupportFeatureToggle.class).name();
	    if ("off".equalsIgnoreCase(consulReader.get(name) )) {
		response.setStatus(HttpStatus.NOT_FOUND.value());
		return false;
	    }
	}
	return true;

    }

}
