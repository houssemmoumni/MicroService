package com.megaminds.task.dto;

import com.megaminds.task.entity.Comment;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentResponse {
    private Long id;
    private String text;
    private LocalDateTime createdAt;
    private Integer authorId;
    private String authorName;
    private Integer taskId;

    public static CommentResponse fromEntity(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .text(comment.getText())
                .createdAt(comment.getCreatedAt())
                .authorId(comment.getAuthor().getId())
                .authorName(comment.getAuthor().getUsername())
                .taskId(comment.getTask().getId())
                .build();
    }
}
