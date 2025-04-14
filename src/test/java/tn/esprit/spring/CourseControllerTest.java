package tn.esprit.spring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.spring.controllers.CourseRestController;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Support;
import tn.esprit.spring.entities.TypeCourse;
import tn.esprit.spring.services.ICourseServices;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourseRestController.class)
public class CourseControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ICourseServices courseServices;

    @Test
    public void testAddCourse() throws Exception {
        // Create test course
        Course course = new Course();
        course.setNumCourse(1L);
        course.setLevel(1);
        course.setSupport(Support.SKI);
        course.setTypeCourse(TypeCourse.INDIVIDUAL);

        // Mock the service method
        when(courseServices.addCourse(any(Course.class))).thenReturn(course);

        // Test the controller endpoint
        mockMvc.perform(post("/course/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"level\":1,\"support\":\"SKI\",\"typeCourse\":\"INDIVIDUAL\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAllCourses() throws Exception {
        // Create test courses using setters
        Course course1 = new Course();
        course1.setNumCourse(1L);
        course1.setLevel(1);
        course1.setSupport(Support.SKI);
        course1.setTypeCourse(TypeCourse.INDIVIDUAL);
        course1.setPrice(100f);
        course1.setTimeSlot(2);

        Course course2 = new Course();
        course2.setNumCourse(2L);
        course2.setLevel(2);
        course2.setSupport(Support.SNOWBOARD);
        course2.setTypeCourse(TypeCourse.COLLECTIVE_ADULT);
        course2.setPrice(80f);
        course2.setTimeSlot(1);

        List<Course> courses = Arrays.asList(course1, course2);

        // Mock the service method
        when(courseServices.retrieveAllCourses()).thenReturn(courses);

        // Test the controller endpoint
        mockMvc.perform(get("/course/all"))
                .andExpect(status().isOk());
    }
    @Test
    public void testUpdateCourse() throws Exception {
        // Create test course
        Course course = new Course();
        course.setNumCourse(1L);
        course.setLevel(2);
        course.setSupport(Support.SNOWBOARD);

        // Mock the service method
        when(courseServices.updateCourse(any(Course.class))).thenReturn(course);

        // Test the controller endpoint
        mockMvc.perform(put("/course/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"numCourse\":1,\"level\":2,\"support\":\"SNOWBOARD\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetCourseById() throws Exception {
        // Create test course
        Course course = new Course();
        course.setNumCourse(1L);
        course.setLevel(3);
        course.setSupport(Support.SKI);

        // Mock the service method
        when(courseServices.retrieveCourse(1L)).thenReturn(course);

        // Test the controller endpoint
        mockMvc.perform(get("/course/get/1"))
                .andExpect(status().isOk());
    }
}
