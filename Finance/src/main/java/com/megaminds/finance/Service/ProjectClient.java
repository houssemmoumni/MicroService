package com.megaminds.finance.Service;

import com.megaminds.finance.Entity.ProjectDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "PROJECT") // Ce nom doit être identique au nom dans Eureka
public interface ProjectClient {

    @GetMapping("/pro/get/projects")
    List<ProjectDTO> getAllProjects();

    @GetMapping("/pro/get/projects/{id}")
    ProjectDTO getProjectById(@PathVariable("id") Long id);
}
