package com.fortinet.auditservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fortinet.auditservice.interfaces.ConversionService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ConversionServiceImpl implements ConversionService {

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
    public String convertStringStringMapToString(Map<String, String> stringStringMap) {
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
    public Map<String, String> convertStringToStringStringMap(String s) {
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
