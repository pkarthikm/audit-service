package com.fortinet.auditservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.fortinet.auditservice")
public class AuditServiceApplication {
	public static void main(String[] args) throws Exception {
		SpringApplication.run(AuditServiceApplication.class, args);
	}
}
