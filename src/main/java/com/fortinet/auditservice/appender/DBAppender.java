/**
 * 
 */
package com.fortinet.auditservice.appender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fortinet.auditservice.model.AuditLog;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

@Plugin(name = "DBAppender", category = Core.CATEGORY_NAME, elementType = Appender.ELEMENT_TYPE)
public class DBAppender extends AbstractAppender {

    private static final Logger logger = LogManager.getLogger(DBAppender.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    protected DBAppender(String name, Filter filter) {
        super(name, filter, null);
    }

    @PluginFactory
    public static DBAppender createAppender(@PluginAttribute("name") String name, @PluginElement("Filter") final Filter filter) {
        return new DBAppender(name, filter);
    }

    @Override
    public void append(LogEvent event) {
        AuditLog auditLog = null;
        String message = event.getMessage().getFormattedMessage();
        try {
            auditLog = objectMapper.readValue(message, AuditLog.class);
        } catch (JsonProcessingException e) {
            logger.error("Json Processing Exception while converting json string to audit log", e);
        }
        if(auditLog == null) {
            logger.error("AuditLog is null");
            return;
        }
        //auditLogService.save(auditLog);
    }

}
