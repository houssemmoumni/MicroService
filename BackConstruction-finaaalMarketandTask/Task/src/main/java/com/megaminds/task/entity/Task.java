package com.megaminds.task.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Getter
@Setter
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status; // Enum for task status (e.g., PENDING, IN_PROGRESS, COMPLETED)

    private LocalDate dueDate;

    @ManyToOne
    @JoinColumn(name = "assigned_by_id", nullable = false)
    private User assignedBy; // Project Manager who assigned the task

    @ManyToOne

    @JoinColumn(name = "assigned_to_id", nullable = false)
    private User assignedTo; // Ouvrier who is assigned the task

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();
}

