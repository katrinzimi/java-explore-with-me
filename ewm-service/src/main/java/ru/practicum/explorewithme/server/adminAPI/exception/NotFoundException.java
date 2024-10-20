package ru.practicum.explorewithme.server.adminAPI.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
