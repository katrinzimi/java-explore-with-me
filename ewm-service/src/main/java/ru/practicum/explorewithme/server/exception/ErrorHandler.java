package ru.practicum.explorewithme.server.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.explorewithme.server.adminAPI.exception.NotFoundException;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleError(final Exception e) {
        log.error("Ошибка 500 при обработке запроса", e);
        return new ApiError("Ошибка 500 при обработке запроса", e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                LocalDateTime.now().toString(),
                ""
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handle(final BindException e) {
        log.info("Ошибка 400 при обработке запроса");
        return new ApiError(
                "Ошибка 400 при обработке запроса", e.getMessage(),
                HttpStatus.BAD_REQUEST.toString(),
                LocalDateTime.now().toString(),
                "Для запрошенной операции условия не выполнены"
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handle(final ValidationException e) {
        log.info("Ошибка 400 при обработке запроса");
        return new ApiError("Ошибка 400 при обработке запроса", e.getMessage(),
                HttpStatus.BAD_REQUEST.toString(),
                LocalDateTime.now().toString(),
                "Для запрошенной операции условия не выполнены");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handle(final ConflictException e) {
        log.info("Ошибка 409 при обработке запроса");
        return new ApiError("Ошибка 409 при обработке запроса", e.getMessage(),
                HttpStatus.CONFLICT.toString(),
                LocalDateTime.now().toString(),
                "Для запрошенной операции условия не выполнены");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFound(final NotFoundException e) {
        log.info("Ошибка 404 при обработке запроса");
        return new ApiError("Ошибка 404 при обработке запроса", e.getMessage(),
                HttpStatus.NOT_FOUND.toString(),
                LocalDateTime.now().toString(),
                "Для запрошенной операции условия не выполнены");
    }

}
