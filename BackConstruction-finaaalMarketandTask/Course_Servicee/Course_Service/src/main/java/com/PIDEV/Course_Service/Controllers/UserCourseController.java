package com.PIDEV.Course_Service.Controllers;

import com.PIDEV.Course_Service.Entities.User;
import com.PIDEV.Course_Service.Entities.UserCourse;
import com.PIDEV.Course_Service.Services.EmailService;
import com.PIDEV.Course_Service.Services.UserCourseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200") // Autorise les requêtes depuis Angular
@RequestMapping("/api/user-courses") // Base URL pour ce contrôleur
public class UserCourseController {

    @Autowired
    private UserCourseServiceImpl userCourseService;

    @Autowired
    private EmailService emailService;

    // Endpoint pour inscrire un utilisateur à un cours
    @PostMapping("/enroll")
    public ResponseEntity<UserCourse> enrollUserInCourse(
            @RequestParam Long userId,
            @RequestParam Long courseId
    ) {
        UserCourse userCourse = userCourseService.enrollUserInCourse(userId, courseId);

        // Envoyer un email de confirmation
        String userEmail = "ahmeddslama2002@gamil.com"; // Récupérez l'email de l'utilisateur
        String subject = "Confirmation d'inscription au cours";
        String body = "Vous avez été inscrit avec succès au cours.";
        emailService.sendSimpleEmail(userEmail, subject, body);

        return new ResponseEntity<>(userCourse, HttpStatus.CREATED);
    }

    // Endpoint pour marquer un cours comme terminé
    @PutMapping("/complete")
    public ResponseEntity<UserCourse> completeCourse(
            @RequestParam Long userId, // ID de l'utilisateur
            @RequestParam Long courseId // ID du cours
    ) {
        UserCourse userCourse = userCourseService.completeCourse(userId, courseId);
        return new ResponseEntity<>(userCourse, HttpStatus.OK); // Retourne 200 (OK)
    }

    // Endpoint pour récupérer tous les cours d'un utilisateur
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserCourse>> getUserCourses(
            @PathVariable Long userId // ID de l'utilisateur
    ) {
        List<UserCourse> userCourses = userCourseService.getUserCourses(userId);
        return new ResponseEntity<>(userCourses, HttpStatus.OK); // Retourne 200 (OK)
    }



    // Endpoint pour récupérer les étudiants inscrits à un cours
    @GetMapping("/course/{courseId}/students")
    public ResponseEntity<List<User>> getEnrolledStudents(
            @PathVariable Long courseId // ID du cours
    ) {
        List<User> enrolledStudents = userCourseService.getEnrolledStudents(courseId);
        return new ResponseEntity<>(enrolledStudents, HttpStatus.OK); // Retourne 200 (OK)
    }
}