package com.megaminds.task.dto;

import java.time.LocalDate;

public record TaskResponse(
                            Integer id,

                            String title,

                            String description,
                            String status,


                            LocalDate dueDate,

                            String assignedBy,

                            String assignedTo) {
}
