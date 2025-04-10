package com.megaminds.task.dto;

public record TaskAssignmentResponse(
                                      Integer taskId,

                                      String assignedTo,

                                      String message) {
}
