//package ru.practicum.explorewithme.server;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//
//public class JsonUtil {
//    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
//
//    public static <T> String toJson(T entity) {
//        try {
//            return objectMapper.writeValueAsString(entity);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
