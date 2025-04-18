package com.PIDEV.Course_Service.Services;

import com.PIDEV.Course_Service.DTO.UserDTO;
import com.PIDEV.Course_Service.Entities.UserCourse;
import java.util.List;

public interface UserCourseService {
    UserCourse enrollUserInCourse(Long userId, Long courseId);
    UserCourse completeCourse(Long userId, Long courseId);
    List<UserCourse> getUserCourses(Long userId);
    List<UserDTO> getEnrolledStudents(Long courseId); // Changé de List<User> à List<UserDTO>
}