package com.lgdx.web;

import java.io.ObjectInputFilter.Config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


// @SpringBootApplication : 해당 클래스의 위치를 기준으로 하위에 있는 파일을 읽어서 Spring Boot의 설정을 자동적으로 진행하는 클래스!

// @ComponentScan : @Controller, @RestController, @Services, @Repository

@SpringBootApplication
public class SpringBoot1Application extends SpringBootServletInitializer{
	

	// 내장 톰캣이 아닌 외부 톰캣을 사용하기 위한 설정 (.jsp)
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		// TODO Auto-generated method stub
		return builder.sources(SpringBoot1Application.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringBoot1Application.class, args);
	}

}
