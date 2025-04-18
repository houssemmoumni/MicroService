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

    // Modification ici: utilisez userId directement
    Optional<UserCourse> findByUserIdAndCourseId(Long userId, Long courseId);

    // Modifiez cette requête pour utiliser userId au lieu de user.id
    @Query("SELECT uc FROM UserCourse uc JOIN FETCH uc.course WHERE uc.userId = :userId")
    List<UserCourse> findByUserIdWithCourse(@Param("userId") Long userId);

    // Cette méthode reste inchangée
    List<UserCourse> findByCourseId(Long courseId);

    // Ajoutez cette nouvelle méthode si nécessaire
    List<UserCourse> findByUserId(Long userId);
}