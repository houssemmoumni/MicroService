package com.PIDEV.Blog_Formation_Service.Services;

import com.PIDEV.Blog_Formation_Service.Entities.Comment;
import java.util.List;

public interface ICommentService {
    Comment addComment(Comment comment, Long blogId, Long userId);
    Comment updateComment(Comment comment);
    void deleteComment(Long id);
    List<Comment> retrieveAllComments();
    Comment retrieveCommentById(Long id);
    List<Comment> retrieveCommentsByBlog(Long blogId);
}
