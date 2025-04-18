package com.PIDEV.Course_Service.Repositories;
import com.PIDEV.Course_Service.Entities.Course;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByIsPublished(String isPublished);

    long countByIsPublished(String aTrue);
}