package ru.practicum.explorewithme.server.exception;

import lombok.Getter;

@Getter
public class ApiError {
    private String error;
    private String message;
    private String status;
    private String timestamp;
    private String reason;

    public ApiError(String error, String message, String status, String timestamp, String reason) {
        this.error = error;
        this.message = message;
        this.status = status;
        this.timestamp = timestamp;
        this.reason = reason;
    }
}