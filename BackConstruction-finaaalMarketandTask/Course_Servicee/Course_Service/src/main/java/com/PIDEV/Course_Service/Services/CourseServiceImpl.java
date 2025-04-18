package com.PIDEV.Course_Service.Services;

import com.PIDEV.Course_Service.Entities.*;

;
import com.PIDEV.Course_Service.Repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseRepository courseRepository;

    @Override
    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public Course updateCourse(Long id, Course course) {
        course.setId(id); // S'assurer que l'ID est correct
        return courseRepository.save(course);
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll(); // Renvoie tous les cours
    }

    @Override
    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    @Override
    public List<Course> getAllPublishedCourses() {
        return courseRepository.findByIsPublished("true");
    }

    @Override
    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    @Override
    public Map<String, Long> getPublishedVsUnpublishedStats() {
        long publishedCount = courseRepository.countByIsPublished("true");
        long unpublishedCount = courseRepository.countByIsPublished("false");
        return Map.of("published", publishedCount, "unpublished", unpublishedCount);
    }

    @Override
    public Map<Long, Long> getModuleCountPerCourse() {
        List<Course> courses = courseRepository.findAll();
        return courses.stream()
                .collect(Collectors.toMap(Course::getId, course -> (long) course.getModules().size()));
    }

    @Override
    public Map<Long, Long> getEnrolledStudentsPerCourse() {
        List<Course> courses = courseRepository.findAll();
        return courses.stream()
                .collect(Collectors.toMap(Course::getId, course -> (long) course.getUserCourses().size()));
    }

    @Override
    public Map<String, Course> getLongestAndShortestCourses() {
        List<Course> courses = courseRepository.findAll();
        Course longestCourse = courses.stream()
                .max(Comparator.comparingInt(Course::getDuration))
                .orElseThrow(() -> new RuntimeException("No courses found"));
        Course shortestCourse = courses.stream()
                .min(Comparator.comparingInt(Course::getDuration))
                .orElseThrow(() -> new RuntimeException("No courses found"));
        return Map.of("longest", longestCourse, "shortest", shortestCourse);
    }

    @Override
    public Map<String, Long> getCourseCountByInstructor() {
        List<Course> courses = courseRepository.findAll();
        return courses.stream()
                .collect(Collectors.groupingBy(Course::getInstructor, Collectors.counting()));
    }
}