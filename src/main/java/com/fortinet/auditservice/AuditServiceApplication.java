package com.fortinet.auditservice;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuditServiceApplication {

	private static final Logger logger = LogManager.getLogger(AuditServiceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(AuditServiceApplication.class, args);
	}

}
