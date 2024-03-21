package com.fortinet.auditservice;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AuditServiceApplicationTests {
	private static final Logger logger = LogManager.getLogger(AuditServiceApplicationTests.class);
	private static final Logger auditLogger = LogManager.getLogger("auditLogger");
	@Test
	void contextLoads() {
		logger.debug("Debug log message");
		logger.info("Info log message");
		logger.error("Error log message");
		logger.warn("Warn log message");
		logger.trace("Trace log message");
		auditLogger.debug("Debug log message");
		auditLogger.info("Info log message");
		auditLogger.error("Error log message");
		auditLogger.warn("Warn log message");
		auditLogger.trace("Trace log message");
	}
}
