package com.toggle.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ecwid.consul.v1.ConsulClient;

@Configuration
public class ApplicationConfig {

    @Bean
    public ConsulClient consulClient(ConsulProperties consulconfiguration) {
	return new ConsulClient(consulconfiguration.getHost(), consulconfiguration.getPort());
    }
    
}
