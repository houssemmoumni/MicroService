package com.megaminds.task.dto;

import jakarta.validation.constraints.NotNull;

public record TaskAssignmentRequest(
                                     @NotNull(message = "Task ID is required")
                                     Integer taskId,

                                     @NotNull(message = "Assigned to ID is required")
                                     Integer assignedToId) {
}
