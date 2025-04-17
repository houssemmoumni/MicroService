package com.PIDEV.Blog_Formation_Service.Services;

import com.PIDEV.Blog_Formation_Service.Entities.Blog;
import com.PIDEV.Blog_Formation_Service.Entities.Comment;
import com.PIDEV.Blog_Formation_Service.Entities.User;
import com.PIDEV.Blog_Formation_Service.Repositories.BlogRepository;
import com.PIDEV.Blog_Formation_Service.Repositories.CommentRepository;
import com.PIDEV.Blog_Formation_Service.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
class CommentService implements ICommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Comment addComment(Comment comment, Long blogId, Long userId) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new RuntimeException("Blog non trouvé"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        comment.setBlog(blog);
        comment.setAuthor(user);
        comment.setDate(LocalDate.now());
        return commentRepository.save(comment);
    }

    @Override
    public Comment updateComment(Comment comment) {
        Comment existingComment = commentRepository.findById(comment.getId())
                .orElseThrow(() -> new RuntimeException("Commentaire non trouvé"));
        existingComment.setContent(comment.getContent());
        existingComment.setDate(LocalDate.now());
        return commentRepository.save(existingComment);
    }

    @Override
    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commentaire non trouvé"));
        commentRepository.delete(comment);
    }

    @Override
    public List<Comment> retrieveAllComments() {
        return commentRepository.findAll();
    }

    @Override
    public Comment retrieveCommentById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commentaire non trouvé"));
    }

    @Override
    public List<Comment> retrieveCommentsByBlog(Long blogId) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new RuntimeException("Blog non trouvé"));
        return commentRepository.findByBlog(blog);
    }
}
