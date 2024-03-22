package com.fortinet.auditservice.service;

import com.fortinet.auditservice.util.EncryptionUtil;
import jakarta.persistence.AttributeConverter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class StringStringMapEncrypt implements AttributeConverter<Map<String, String>,String> {
    @Autowired
    EncryptionUtil encryptionUtil;

    private String convertMapToString(Map<String, String> map) {
        return map.keySet().stream().filter(Objects::nonNull).filter(key -> map.get(key) != null)
                .map(key -> key + "=" + map.get(key))
                .collect(Collectors.joining(", ", "{", "}"));
    }

    private Map<String, String> convertStringToMap(String mapAsString) {
        return Arrays.stream(mapAsString.split(","))
                .map(entry -> entry.split("="))
                .collect(Collectors.toMap(entry -> entry[0], entry -> entry[1]));
    }

    @Override
    public String convertToDatabaseColumn(Map<String, String> stringStringMap) {
        if (stringStringMap == null)
            return null;
        if(stringStringMap.isEmpty())
            return "";
        return convertMapToString(stringStringMap);
    }

    @Override
    public Map<String, String> convertToEntityAttribute(String s) {
        if(s == null)
            return null;
        if(s.isEmpty())
            return Map.of();
        return convertStringToMap(s);
    }
}
