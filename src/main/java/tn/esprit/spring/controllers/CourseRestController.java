package tn.esprit.spring.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.services.ICourseServices;

import java.util.List;

@Tag(name = "📚 Course Management")
@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
public class CourseRestController {

    private final ICourseServices courseServices;

    @Operation(description = "Add Course")
    @PostMapping("/add")
    public ResponseEntity<Course> addCourse(@Valid @RequestBody Course course) {
        Course created = courseServices.addCourse(course);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @Operation(description = "Retrieve all Courses")
    @GetMapping("/all")
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseServices.retrieveAllCourses();
        return ResponseEntity.ok(courses);
    }

    @Operation(description = "Update Course")
    @PutMapping("/update")
    public ResponseEntity<Course> updateCourse(@Valid @RequestBody Course course) {
        Course updated = courseServices.updateCourse(course);
        return ResponseEntity.ok(updated);
    }

    @Operation(description = "Retrieve Course by Id")
    @GetMapping("/get/{id-course}")
    public ResponseEntity<Course> getById(@PathVariable("id-course") Long numCourse) {
        Course course = courseServices.retrieveCourse(numCourse);
        return (course != null) ? ResponseEntity.ok(course) : ResponseEntity.notFound().build();
    }

    @Operation(description = "Delete Course by Id")
    @DeleteMapping("/delete/{id-course}")
    public ResponseEntity<Void> deleteCourse(@PathVariable("id-course") Long numCourse) {
        courseServices.removeCourse(numCourse);
        return ResponseEntity.noContent().build();
    }
}
