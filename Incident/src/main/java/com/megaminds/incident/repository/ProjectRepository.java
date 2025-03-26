// src/main/java/com/megaminds/incident/repository/ProjectRepository.java
package com.megaminds.incident.repository;

import com.megaminds.incident.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByPublishedTrue();
}