package com.PIDEV.Course_Service.Services;

import com.PIDEV.Course_Service.Entities.User;
import com.PIDEV.Course_Service.Entities.UserCourse;
import java.util.List;
import java.util.Optional;


public interface UserCourseService {
    UserCourse enrollUserInCourse(Long userId, Long courseId); // Inscrire un utilisateur à un cours
    UserCourse completeCourse(Long userId, Long courseId); // Marquer un cours comme terminé pour un utilisateur
    List<UserCourse> getUserCourses(Long userId); // Récupérer tous les cours suivis par un utilisateur List<User> getEnrolledStudents(Long courseId);

    List<User> getEnrolledStudents(Long courseId);

    // UserCourseServiceImpl.java
//    boolean hasUserCompletedAllModules(Long userId, Long courseId);
//
//    void markModuleAsCompleted(Long userId, Long moduleId);
}