package com.phhc.sso;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * springboot 启动类
 * @author
 */
@SpringBootApplication
@MapperScan("com.phhc.sso.mapper")
@ComponentScan(basePackages = {
		"com.phhc.sso.config",
		"com.phhc.sso.controller",
		"com.phhc.sso.service"})
public class SsoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SsoApplication.class, args);
	}

}
