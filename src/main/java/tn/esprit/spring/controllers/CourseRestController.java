package tn.esprit.spring.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus; // Ajoute cet import
import org.springframework.http.ResponseEntity; // Ajoute cet import
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.services.ICourseServices;

import java.util.List;

@Tag(name = "\uD83D\uDCDA Course Management")
@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
public class CourseRestController {
    
    private final ICourseServices courseServices;

  @Operation(description = "Add Course")
    @PostMapping("/add")
    public ResponseEntity<Course> addCourse(@RequestBody Course course) {
    
        Course created = courseServices.addCourse(course);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @Operation(description = "Retrieve all Courses")
    @GetMapping("/all")
    public ResponseEntity<List<Course>> getAllCourses() {
     
        List<Course> courses = courseServices.retrieveAllCourses();
        return ResponseEntity.ok(courses);
    }

    @Operation(description = "Update Course ")
    @PutMapping("/update")
    public Course updateCourse(@RequestBody Course course){
        return  courseServices.updateCourse(course);
    }

    @Operation(description = "Retrieve Course by Id")
    @GetMapping("/get/{id-course}")
    public Course getById(@PathVariable("id-course") Long numCourse){
        return courseServices.retrieveCourse(numCourse);
    }

}
