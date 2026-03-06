package com.product.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.util.List;
import java.util.Map;

public final class JsonUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.findAndRegisterModules();
        OBJECT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    private JsonUtil() {}

    /* =========================
       Object → JSON
       ========================= */

    public static String toJson(Object object) {
        if (object == null) return null;
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting object to JSON", e);
        }
    }

    /* =========================
       Object → Pretty JSON
       ========================= */

    public static String toPrettyJson(Object object) {
        if (object == null) return null;
        try {
            return OBJECT_MAPPER
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting object to pretty JSON", e);
        }
    }

    /* =========================
       JSON → Object
       ========================= */

    public static <T> T fromJson(String json, Class<T> clazz) {
        if (json == null || json.isEmpty()) return null;

        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Error converting JSON to Object", e);
        }
    }

    /* =========================
       JSON → List
       ========================= */

    public static <T> List<T> fromJsonToList(String json, Class<T> clazz) {
        if (json == null || json.isEmpty()) return null;

        try {
            return OBJECT_MAPPER.readValue(
                    json,
                    OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, clazz)
            );
        } catch (Exception e) {
            throw new RuntimeException("Error converting JSON to List", e);
        }
    }

    /* =========================
       JSON → Map
       ========================= */

    public static Map<String, Object> fromJsonToMap(String json) {
        if (json == null || json.isEmpty()) return null;

        try {
            return OBJECT_MAPPER.readValue(json, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Error converting JSON to Map", e);
        }
    }

    /* =========================
       Object → Map
       ========================= */

    public static Map<String, Object> objectToMap(Object object) {
        if (object == null) return null;
        return OBJECT_MAPPER.convertValue(object, new TypeReference<Map<String, Object>>() {});
    }

    /* =========================
       Map → Object
       ========================= */

    public static <T> T mapToObject(Map<String, Object> map, Class<T> clazz) {
        if (map == null) return null;
        return OBJECT_MAPPER.convertValue(map, clazz);
    }

}
