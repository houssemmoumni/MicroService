package com.megaminds.task.repository;

import com.megaminds.task.entity.Task;
import com.megaminds.task.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TaskRepository extends JpaRepository<Task,Integer> {
    List<Task> findByAssignedToId(Integer workerId);
    List<Task> findByAssignedToId(int id);
    long countByStatus(TaskStatus status);


}
