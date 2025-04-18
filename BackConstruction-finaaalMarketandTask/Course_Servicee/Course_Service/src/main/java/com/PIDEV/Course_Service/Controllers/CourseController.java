package com.PIDEV.Course_Service.Controllers;

//import com.PIDEV.Blog_Formation_Service.Entities.Blog;
//import com.PIDEV.Blog_Formation_Service.Repositories.UserRepository;
//import com.PIDEV.Blog_Formation_Service.Services.CloudinaryService;
import com.PIDEV.Course_Service.Entities.Course;
import com.PIDEV.Course_Service.Services.CourseService;
import com.PIDEV.Course_Service.Services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    // Créer un cours
    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        Course createdCourse = courseService.createCourse(course);
        return new ResponseEntity<>(createdCourse, HttpStatus.CREATED);
    }

    // Récupérer un cours par son ID
    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        Course course = courseService.getCourseById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return new ResponseEntity<>(course, HttpStatus.OK);
    }

    // Mettre à jour un cours
    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody Course course) {
        Course updatedCourse = courseService.updateCourse(id, course);
        return new ResponseEntity<>(updatedCourse, HttpStatus.OK);
    }

    // Supprimer un cours
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Récupérer tous les cours publiés
    @GetMapping("/published")
    public ResponseEntity<List<Course>> getAllPublishedCourses() {
        List<Course> courses = courseService.getAllPublishedCourses();
        return new ResponseEntity<>(courses, HttpStatus.OK);
    }


    @GetMapping("/all")
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }


    // Statistique 1 : Nombre de cours publiés vs non publiés
    @GetMapping("/stats/published-vs-unpublished")
    public ResponseEntity<Map<String, Long>> getPublishedVsUnpublishedStats() {
        Map<String, Long> stats = courseService.getPublishedVsUnpublishedStats();
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }

    // Statistique 2 : Nombre de modules par cours
    @GetMapping("/stats/module-count")
    public ResponseEntity<Map<Long, Long>> getModuleCountPerCourse() {
        Map<Long, Long> stats = courseService.getModuleCountPerCourse();
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }

    // Statistique 3 : Nombre d'étudiants inscrits par cours
    @GetMapping("/stats/enrolled-students")
    public ResponseEntity<Map<Long, Long>> getEnrolledStudentsPerCourse() {
        Map<Long, Long> stats = courseService.getEnrolledStudentsPerCourse();
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }

    // Statistique 4 : Cours les plus longs et les plus courts
    @GetMapping("/stats/longest-shortest")
    public ResponseEntity<Map<String, Course>> getLongestAndShortestCourses() {
        Map<String, Course> stats = courseService.getLongestAndShortestCourses();
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }

    // Statistique 5 : Répartition des cours par instructeur
    @GetMapping("/stats/course-count-by-instructor")
    public ResponseEntity<Map<String, Long>> getCourseCountByInstructor() {
        Map<String, Long> stats = courseService.getCourseCountByInstructor();
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }


}