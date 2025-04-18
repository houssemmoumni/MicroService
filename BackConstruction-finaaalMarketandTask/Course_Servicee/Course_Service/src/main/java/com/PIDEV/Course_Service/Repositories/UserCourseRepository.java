package com.PIDEV.Course_Service.Repositories;

import com.PIDEV.Course_Service.Entities.UserCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserCourseRepository extends JpaRepository<UserCourse, Long> {
    Optional<UserCourse> findByUserIdAndCourseId(Long userId, Long courseId);

    // Récupérer les UserCourse avec les données de Course
    @Query("SELECT uc FROM UserCourse uc JOIN FETCH uc.course WHERE uc.user.id = :userId")
    List<UserCourse> findByUserIdWithCourse(@Param("userId") Long userId);

    List<UserCourse> findByCourseId(Long courseId);
}