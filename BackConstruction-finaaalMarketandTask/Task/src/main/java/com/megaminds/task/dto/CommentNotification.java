package com.megaminds.task.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentNotification {
    private Integer taskId;
    private CommentResponse comment;
}
