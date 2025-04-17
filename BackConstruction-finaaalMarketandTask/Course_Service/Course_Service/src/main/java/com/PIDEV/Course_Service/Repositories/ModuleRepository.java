package com.PIDEV.Course_Service.Repositories;

import com.PIDEV.Course_Service.Entities.Module;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface ModuleRepository extends JpaRepository<Module, Long> {
   List<Module> findByCourseId(Long courseId);
}
