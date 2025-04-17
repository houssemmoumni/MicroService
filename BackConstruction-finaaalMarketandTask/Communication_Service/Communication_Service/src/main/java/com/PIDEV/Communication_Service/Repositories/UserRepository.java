package com.PIDEV.Communication_Service.Repositories;

import com.PIDEV.Communication_Service.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Trouver un utilisateur par son username
    Optional<User> findByUsername(String username);
}




