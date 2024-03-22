package com.fortinet.auditservice.interfaces;

import java.util.Map;

public interface ConversionService {
    String convertStringStringMapToString(Map<String, String> stringStringMap);
    Map<String, String> convertStringToStringStringMap(String s);
}
