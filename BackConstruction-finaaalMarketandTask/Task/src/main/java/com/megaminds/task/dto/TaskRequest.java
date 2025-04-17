package com.megaminds.task.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record TaskRequest(
    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must be less than 100 characters")
    String title,

    @NotBlank(message = "Description is required")
    @Size(max = 500, message = "Description must be less than 500 characters")
    String description,

    @NotNull(message = "Status is required")
    String status, // Add this field

    @NotNull(message = "Due date is required")
    @Future(message = "Due date must be in the future")
    LocalDate dueDate,

    @NotNull(message = "Assigned by ID is required")
    Integer assignedById,

    @NotNull(message = "Assigned to ID is required")
    Integer assignedToId
) {}
