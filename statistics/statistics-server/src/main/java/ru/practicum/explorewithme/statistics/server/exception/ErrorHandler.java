package ru.practicum.explorewithme.statistics.server.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleError(final Exception e) {
        log.error("Ошибка 500 при обработке запроса", e);
        return new ApiError(
                "Внутренняя ошибка сервера", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handle(final BindException e) {
        log.info("Ошибка 400 при обработке запроса");
        return new ApiError(
                "Ошибка с параметром count.", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handle(final ValidationException e) {
        log.info("Ошибка 400 при обработке запроса");
        return new ApiError(
                "Ошибка с параметром count.", e.getMessage()
        );
    }

}
