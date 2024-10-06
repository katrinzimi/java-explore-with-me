package ru.practicum.explorewithme.server.dto.event;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jdk.jshell.Snippet;
import ru.practicum.explorewithme.server.model.enums.EventRequestStatus;

import java.util.List;

public class EventRequestStatusUpdateRequest {
    @NotNull
    private List<Long> requestIds;

    @NotBlank
    private EventRequestStatus status;
}
