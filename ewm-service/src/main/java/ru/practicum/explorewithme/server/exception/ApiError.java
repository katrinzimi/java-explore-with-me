package ru.practicum.explorewithme.server.exception;

import lombok.Getter;

@Getter
public class ApiError {
    private final String error;
    private final String message;
    private final String status;
    private final String timestamp;
    private final String reason;

    public ApiError(String error, String message, String status, String timestamp, String reason) {
        this.error = error;
        this.message = message;
        this.status = status;
        this.timestamp = timestamp;
        this.reason = reason;
    }
}