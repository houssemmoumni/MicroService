package com.megaminds.incident.repository;

import com.megaminds.incident.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}