package com.PIDEV.Blog_Formation_Service.Repositories;


import com.PIDEV.Blog_Formation_Service.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
