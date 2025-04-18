package com.PIDEV.Course_Service.Services;

import com.PIDEV.Course_Service.DTO.UserDTO;
import com.PIDEV.Course_Service.Entities.*;
import com.PIDEV.Course_Service.Feign.UserClient;
import com.PIDEV.Course_Service.Repositories.UserCourseRepository;
import com.PIDEV.Course_Service.Repositories.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserCourseServiceImpl implements UserCourseService {

    private final UserCourseRepository userCourseRepository;
    private final CourseRepository courseRepository;
    private final UserClient userClient;

    @Override
    public UserCourse enrollUserInCourse(Long userId, Long courseId) {
        // Récupérer le token JWT
        String token = getJwtToken();

        // 1. Vérifier que l'utilisateur existe via Feign Client
        try {
            UserDTO user = userClient.getUserById(userId, token);
            if (user == null) {
                throw new RuntimeException("User not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while verifying user existence: " + e.getMessage());
        }

        // 2. Vérifier que le cours existe
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // 3. Créer le UserCourse
        UserCourse userCourse = new UserCourse();
        userCourse.setUserId(userId);
        userCourse.setCourse(course);
        userCourse.setCompleted(false);

        return userCourseRepository.save(userCourse);
    }

    @Override
    public UserCourse completeCourse(Long userId, Long courseId) {
        UserCourse userCourse = userCourseRepository.findByUserIdAndCourseId(userId, courseId)
                .orElseThrow(() -> new RuntimeException("UserCourse not found"));

        userCourse.setCompleted(true);
        userCourse.setCompletionDate(LocalDate.now());

        return userCourseRepository.save(userCourse);
    }

    @Override
    public List<UserCourse> getUserCourses(Long userId) {
        return userCourseRepository.findByUserIdWithCourse(userId);
    }

    @Override
    public List<UserDTO> getEnrolledStudents(Long courseId) {
        String token = getJwtToken();

        return userCourseRepository.findByCourseId(courseId).stream()
                .map(UserCourse::getUserId)
                .map(userId -> {
                    try {
                        return userClient.getUserById(userId, token);
                    } catch (Exception e) {
                        return UserDTO.builder()
                                .id_User(userId)
                                .login("Unavailable user data")
                                .email("")
                                .firstName("")
                                .lastName("")
                                .build();
                    }
                })
                .collect(Collectors.toList());
    }

    private String getJwtToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            return "Bearer " + jwt.getTokenValue();
        }
        throw new RuntimeException("No JWT token found in security context");
    }
}