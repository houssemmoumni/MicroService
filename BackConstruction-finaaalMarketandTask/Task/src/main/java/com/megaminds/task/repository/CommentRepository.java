package com.megaminds.task.repository;

import com.megaminds.task.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByTaskIdOrderByCreatedAtAsc(Integer taskId);

    @Modifying
    @Query("DELETE FROM Comment c WHERE c.task.id = :taskId")
    void deleteByTaskId(@Param("taskId") Integer taskId);
}
