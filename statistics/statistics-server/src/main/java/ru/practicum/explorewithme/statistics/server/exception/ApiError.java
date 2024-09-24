package ru.practicum.explorewithme.statistics.server.exception;

public class ApiError {
    // название ошибки
    String error;
    // подробное описание
    String description;

    public ApiError(String error, String description) {
        this.error = error;
        this.description = description;
    }

    // геттеры необходимы, чтобы Spring Boot мог получить значения полей
    public String getError() {
        return error;
    }

    public String getDescription() {
        return description;
    }
}