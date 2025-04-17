package com.PIDEV.Blog_Formation_Service.Repositories;

import com.PIDEV.Blog_Formation_Service.Entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface RoleRepository extends JpaRepository<Role, Long> {
}