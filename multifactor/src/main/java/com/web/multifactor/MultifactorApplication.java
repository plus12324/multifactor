package com.web.multifactor;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.web.multifactor.oauth.UserArgumentResolver;

@SpringBootApplication
public class MultifactorApplication {
//	@Autowired(required=true)
//	private UserArgumentResolver userArgumentResolver;

	public static void main(String[] args) throws UnsupportedEncodingException {
		System.out.println("■ " + URLDecoder.decode("http%3A%2F%2Flocalhost%3A8080%2Flogin%2Foauth2%2Fcode%2Fkakao","utf-8"));
		SpringApplication.run(MultifactorApplication.class, args);
	}
	
//	@Override
//	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
//		argumentResolvers.add(userArgumentResolver);
//	}
}

