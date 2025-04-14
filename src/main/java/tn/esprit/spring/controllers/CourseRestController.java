package tn.esprit.spring.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.dto.CourseDTO;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.services.ICourseServices;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "\uD83D\uDCDA Course Management")
@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
public class CourseRestController {

    private final ICourseServices courseServices;

    @Operation(description = "Add Course")
    @PostMapping("/add")
    public CourseDTO addCourse(@RequestBody CourseDTO courseDTO) {
        Course saved = courseServices.addCourse(dtoToEntity(courseDTO));
        return entityToDto(saved);
    }

    @Operation(description = "Retrieve all Courses")
    @GetMapping("/all")
    public List<CourseDTO> getAllCourses() {
        return courseServices.retrieveAllCourses()
                .stream()
                .map(this::entityToDto)
                .collect(Collectors.toList());
    }

    @Operation(description = "Update Course")
    @PutMapping("/update")
    public CourseDTO updateCourse(@RequestBody CourseDTO courseDTO) {
        Course updated = courseServices.updateCourse(dtoToEntity(courseDTO));
        return entityToDto(updated);
    }

    @Operation(description = "Retrieve Course by Id")
    @GetMapping("/get/{id-course}")
    public CourseDTO getById(@PathVariable("id-course") Long numCourse) {
        Course course = courseServices.retrieveCourse(numCourse);
        return entityToDto(course);
    }

    // ✅ Mappers
    private CourseDTO entityToDto(Course course) {
        CourseDTO dto = new CourseDTO();
        dto.setNumCourse(course.getNumCourse());
        dto.setLevel(course.getLevel());
        dto.setTypeCourse(course.getTypeCourse());
        dto.setSupport(course.getSupport());
        dto.setPrice(course.getPrice());
        dto.setTimeSlot(course.getTimeSlot());
        return dto;
    }

    private Course dtoToEntity(CourseDTO dto) {
        Course course = new Course();
        course.setNumCourse(dto.getNumCourse());
        course.setLevel(dto.getLevel());
        course.setTypeCourse(dto.getTypeCourse());
        course.setSupport(dto.getSupport());
        course.setPrice(dto.getPrice());
        course.setTimeSlot(dto.getTimeSlot());
        return course;
    }
}
