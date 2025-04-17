package com.PIDEV.Blog_Formation_Service.Repositories;

import com.PIDEV.Blog_Formation_Service.Entities.Blog;
import com.PIDEV.Blog_Formation_Service.Entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByBlog(Blog blog);
}
