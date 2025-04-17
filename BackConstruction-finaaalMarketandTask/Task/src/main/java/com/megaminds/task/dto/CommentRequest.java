package com.megaminds.task.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentRequest {
    @NotNull(message = "Task ID is required")
    private Integer taskId;

    @NotBlank(message = "Comment text cannot be empty")
    private String text;
}
