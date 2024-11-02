package ru.practicum.explorewithme.server;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.practicum.explorewithme.server.event.dto.UpdateEventAdminRequest;

import java.time.LocalDateTime;

public class CheckEventDateValidator implements ConstraintValidator<CheckEventDate, UpdateEventAdminRequest> {
    @Override
    public boolean isValid(UpdateEventAdminRequest updateEventAdminRequest, ConstraintValidatorContext constraintValidatorContext) {
        if (updateEventAdminRequest.getEventDate() == null) {
            return true;
        }
        return !updateEventAdminRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(1));
    }
}


