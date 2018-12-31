package com.web.multifactor.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "com.web.multifactor.repository.mybatis")
public class SampleConfig {

}
