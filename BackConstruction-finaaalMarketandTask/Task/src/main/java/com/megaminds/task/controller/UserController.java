package com.megaminds.task.controller;

import com.megaminds.task.entity.RoleType;
import com.megaminds.task.entity.User;
import com.megaminds.task.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/ouvriers")
    public ResponseEntity<List<User>> getOuvriers() {
        return ResponseEntity.ok(userRepository.findByRoles_Name(RoleType.OUVRIER));
    }
}
