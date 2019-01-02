package com.web.multifactor.config;

import java.util.List;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;

import com.web.multifactor.oauth.UserArgumentResolver;
import com.web.multifactor.repository.jpa.UserRepository;

@Configuration
@MapperScan(basePackages = "com.web.multifactor.repository.mybatis")
public class SampleConfig implements WebMvcConfigurer  {
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new UserArgumentResolver(userRepository));
	}
}
