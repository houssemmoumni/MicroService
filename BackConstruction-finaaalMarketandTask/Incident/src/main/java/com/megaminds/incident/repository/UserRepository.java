package com.megaminds.incident.repository;

import com.megaminds.incident.entity.User;
import com.megaminds.incident.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.management.relation.Role;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByRole(UserRole role);
}