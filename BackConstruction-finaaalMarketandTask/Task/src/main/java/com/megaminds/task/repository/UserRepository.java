package com.megaminds.task.repository;

import com.megaminds.task.entity.Role;
import com.megaminds.task.entity.RoleType;
import com.megaminds.task.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByRolesContaining(Role role);
    List<User> findByRoles_Name(RoleType name);


}
