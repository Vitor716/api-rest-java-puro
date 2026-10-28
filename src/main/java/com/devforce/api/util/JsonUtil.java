package com.devforce.api.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JsonUtil {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private JsonUtil() {}

    public static String toJson(Object objeto) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(objeto);
    }

    public static <T> T fromJson(String json, Class<T> tipo) throws JsonProcessingException {
        return OBJECT_MAPPER.readValue(json, tipo);
    }
}
