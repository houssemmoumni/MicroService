package com.megaminds.task.controller;

import com.megaminds.task.dto.CommentRequest;
import com.megaminds.task.dto.CommentResponse;
import com.megaminds.task.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(
            @Valid @RequestBody CommentRequest request,
            @RequestParam Integer authorId) {  // Now accepting authorId as parameter

        CommentResponse response = commentService.createComment(request, authorId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<CommentResponse>> getCommentsForTask(
            @PathVariable Integer taskId) {

        List<CommentResponse> comments = commentService.getCommentsForTask(taskId);
        return ResponseEntity.ok(comments);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentRequest request) {

        CommentResponse updated = commentService.updateComment(commentId, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}