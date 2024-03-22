package com.fortinet.auditservice.service;

import com.fortinet.auditservice.util.EncryptionUtil;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.beans.factory.annotation.Autowired;

@Converter
public class StringEncrypt implements AttributeConverter<String,String> {
    @Autowired
    EncryptionUtil encryptionUtil;

    @Override
    public String convertToDatabaseColumn(String s) {
        return encryptionUtil.encrypt(s);
    }

    @Override
    public String convertToEntityAttribute(String s) {
        return encryptionUtil.decrypt(s);
    }
}
