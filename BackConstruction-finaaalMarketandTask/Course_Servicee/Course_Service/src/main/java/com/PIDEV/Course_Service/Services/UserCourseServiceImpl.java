package com.PIDEV.Course_Service.Services;

import com.PIDEV.Course_Service.Entities.*;
import com.PIDEV.Course_Service.Repositories.UserRepository;
import com.PIDEV.Course_Service.Repositories.UserCourseRepository;
import com.PIDEV.Course_Service.Repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserCourseServiceImpl implements UserCourseService {
    @Autowired
    private UserCourseRepository userCourseRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CourseRepository courseRepository;


    @Override
    public UserCourse enrollUserInCourse(Long userId, Long courseId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        UserCourse userCourse = new UserCourse();
        userCourse.setUser(user);
        userCourse.setCourse(course);
        userCourse.setCompleted(false);
        return userCourseRepository.save(userCourse);
    }

    @Override
    public UserCourse completeCourse(Long userId, Long courseId) {
        // Récupérer un seul UserCourse
        UserCourse userCourse = userCourseRepository.findByUserIdAndCourseId(userId, courseId)
                .orElseThrow(() -> new RuntimeException("UserCourse not found"));

        // Marquer le cours comme terminé
        userCourse.setCompleted(true);
        userCourse.setCompletionDate(LocalDate.now());

        // Enregistrer les modifications
        return userCourseRepository.save(userCourse);
    }

    @Override
    public List<UserCourse> getUserCourses(Long userId) {
        // Utiliser la nouvelle méthode pour récupérer les UserCourse avec les données de Course
        return userCourseRepository.findByUserIdWithCourse(userId);
    }


    @Override
    public List<User> getEnrolledStudents(Long courseId) {
        // Récupérer tous les UserCourse pour le cours donné
        List<UserCourse> userCourses = userCourseRepository.findByCourseId(courseId);

        // Extraire les utilisateurs (étudiants) de ces UserCourse
        List<User> enrolledStudents = userCourses.stream()
                .map(UserCourse::getUser)
                .collect(Collectors.toList());

        return enrolledStudents;
    }

}