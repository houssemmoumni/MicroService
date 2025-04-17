package com.megaminds.incident.repository;

import com.megaminds.incident.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByPublishedTrue();
    @Query("SELECT p FROM Project p WHERE p.published = true")
    List<Project> findPublishedProjects();
}