package com.toggle.config;

import com.toggle.interceptor.FeatureToggleInterceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebApplicationConfiguration extends WebMvcConfigurerAdapter {
    
    @Autowired
    private FeatureToggleInterceptor featureToggleInterceptor;
    
    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
	registry.addInterceptor(featureToggleInterceptor).addPathPatterns("/**");
    }

}