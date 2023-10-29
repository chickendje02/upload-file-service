package com.example.uploadfileservice.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JsonHandler {

    private static ObjectMapper objectMapper;

    public static <T> T deepCloneObject(T data, TypeReference<T> typeReference) {
        try {
            String jsonString = getObjectMapper().writeValueAsString(data);

            return getObjectMapper().readValue(jsonString, typeReference);
        } catch (Exception e) {
            throw new RuntimeException("Failed to clone object");
        }
    }

    private static ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
        }
        return objectMapper;
    }
}
