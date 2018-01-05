package com.menlo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@ComponentScan
@EnableAutoConfiguration
@ImportResource("classpath:data-context.xml")
@SpringBootApplication
public class SpringbootFtpToAwsApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootFtpToAwsApplication.class, args);
	}
}
