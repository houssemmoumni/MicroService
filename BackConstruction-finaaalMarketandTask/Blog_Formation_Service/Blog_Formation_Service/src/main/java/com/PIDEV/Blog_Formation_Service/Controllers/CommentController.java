package com.PIDEV.Blog_Formation_Service.Controllers;

import com.PIDEV.Blog_Formation_Service.Entities.Comment;
import com.PIDEV.Blog_Formation_Service.Services.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private ICommentService commentService;

    @PostMapping("/{blogId}/{userId}")
    public Comment addComment(@RequestBody Comment comment, @PathVariable Long blogId, @PathVariable Long userId) {
        return commentService.addComment(comment, blogId, userId);
    }

    @PutMapping
    public Comment updateComment(@RequestBody Comment comment) {
        return commentService.updateComment(comment);
    }

    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
    }

    @GetMapping
    public List<Comment> retrieveAllComments() {
        return commentService.retrieveAllComments();
    }

    @GetMapping("/{id}")
    public Comment retrieveCommentById(@PathVariable Long id) {
        return commentService.retrieveCommentById(id);
    }

    @GetMapping("/blog/{blogId}")
    public List<Comment> retrieveCommentsByBlog(@PathVariable Long blogId) {
        return commentService.retrieveCommentsByBlog(blogId);
    }
}
