package com.fortinet.auditservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fortinet.auditservice.model.AuditLog;
import com.fortinet.auditservice.util.EncryptionUtil;
import jakarta.persistence.AttributeConverter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class StringStringMapEncrypt implements AttributeConverter<Map<String, String>,String> {
    @Autowired
    EncryptionUtil encryptionUtil;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private String convertMapToString(Map<String, String> map) throws JsonProcessingException {
        if(map == null)
            return null;
        if(map.isEmpty())
            return "";
        return objectMapper.writeValueAsString(map);
    }

    private Map<String, String> convertStringToMap(String mapAsString) throws JsonProcessingException {
        if(mapAsString == null)
            return null;
        if(mapAsString.isEmpty())
            return Map.of();
        Map<String, String> map = objectMapper.readValue(mapAsString, Map.class);
        return map;
    }

    @Override
    public String convertToDatabaseColumn(Map<String, String> stringStringMap) {
        if (stringStringMap == null)
            return null;
        if(stringStringMap.isEmpty())
            return "";
        try {
            return convertMapToString(stringStringMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, String> convertToEntityAttribute(String s) {
        if(s == null)
            return null;
        if(s.isEmpty())
            return Map.of();
        try {
            return convertStringToMap(s);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
