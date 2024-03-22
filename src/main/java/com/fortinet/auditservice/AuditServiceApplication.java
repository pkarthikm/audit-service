package com.fortinet.auditservice;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.fortinet.auditservice")
public class AuditServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(AuditServiceApplication.class, args);
	}

}
