package com.PIDEV.Course_Service.Services;
import com.PIDEV.Course_Service.Entities.Course;
import java.util.List;
import java.util.Map;
import java.util.Optional;
public interface CourseService {
    Course createCourse(Course course); // Créer un cours
    Course updateCourse(Long id, Course course); // Mettre à jour un cours
    void deleteCourse(Long id); // Supprimer un cours
    List<Course> getAllPublishedCourses(); // Récupérer tous les cours publiés
    Optional<Course> getCourseById(Long id); // Récupérer un cours par son ID

    List<Course> getAllCourses();


    // Statistique 1 : Nombre de cours publiés vs non publiés
    Map<String, Long> getPublishedVsUnpublishedStats();

    // Statistique 2 : Nombre de modules par cours
    Map<Long, Long> getModuleCountPerCourse();

    // Statistique 3 : Nombre d'étudiants inscrits par cours
    Map<Long, Long> getEnrolledStudentsPerCourse();

    // Statistique 4 : Cours les plus longs et les plus courts
    Map<String, Course> getLongestAndShortestCourses();

    // Statistique 5 : Répartition des cours par instructeur
    Map<String, Long> getCourseCountByInstructor();
}