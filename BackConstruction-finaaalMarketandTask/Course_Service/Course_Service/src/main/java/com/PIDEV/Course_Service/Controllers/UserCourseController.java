package com.PIDEV.Course_Service.Controllers;

import com.PIDEV.Course_Service.DTO.UserDTO;
import com.PIDEV.Course_Service.Entities.UserCourse;
import com.PIDEV.Course_Service.Services.EmailService;
import com.PIDEV.Course_Service.Services.UserCourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/user-courses")
@RequiredArgsConstructor
public class UserCourseController {

    private final UserCourseService userCourseService;
    private final EmailService emailService;

    @PostMapping("/enroll")
    public ResponseEntity<UserCourse> enrollUserInCourse(
            @RequestParam Long userId,
            @RequestParam Long courseId) {

        UserCourse userCourse = userCourseService.enrollUserInCourse(userId, courseId);

        // Envoyer un email de confirmation
        String userEmail = "ahmeddslama2002@gmail.com"; // Devrait être récupéré via user-service
        String subject = "Confirmation d'inscription au cours";
        String body = "Vous avez été inscrit avec succès au cours.";
        emailService.sendSimpleEmail(userEmail, subject, body);

        return new ResponseEntity<>(userCourse, HttpStatus.CREATED);
    }

    @PutMapping("/complete")
    public ResponseEntity<UserCourse> completeCourse(
            @RequestParam Long userId,
            @RequestParam Long courseId) {
        UserCourse userCourse = userCourseService.completeCourse(userId, courseId);
        return new ResponseEntity<>(userCourse, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserCourse>> getUserCourses(
            @PathVariable Long userId) {
        List<UserCourse> userCourses = userCourseService.getUserCourses(userId);
        return new ResponseEntity<>(userCourses, HttpStatus.OK);
    }

    @GetMapping("/course/{courseId}/students")
    public ResponseEntity<List<UserDTO>> getEnrolledStudents(
            @PathVariable Long courseId) {
        List<UserDTO> enrolledStudents = userCourseService.getEnrolledStudents(courseId);
        return new ResponseEntity<>(enrolledStudents, HttpStatus.OK);
    }
}