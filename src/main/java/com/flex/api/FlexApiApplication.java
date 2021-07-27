package com.flex.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaAuditing
@EnableAspectJAutoProxy
@SpringBootApplication
@EntityScan(basePackages = { "com.flex.api" })
@ComponentScan(basePackages = { "com.flex.api" })
@EnableJpaRepositories(basePackages = { "com.flex.api" })
public class FlexApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlexApiApplication.class, args);
	}

}
