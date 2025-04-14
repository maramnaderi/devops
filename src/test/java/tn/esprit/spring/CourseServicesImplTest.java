package tn.esprit.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.spring.controllers.CourseRestController;
import tn.esprit.spring.dto.CourseDTO;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.TypeCourse;
import tn.esprit.spring.entities.Support;
import tn.esprit.spring.services.ICourseServices;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourseRestController.class)
public class CourseServicesImplTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ICourseServices courseServices;

    @InjectMocks
    private CourseRestController courseRestController;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private TypeCourse typeCourse;  // Mock TypeCourse

    @Mock
    private Support support;  // Mock Support

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddCourse() throws Exception {
        // Arrange
        CourseDTO courseDTO = new CourseDTO(1L, 1, null, null, 100.0f, 10);
        Course course = new Course(1L, 1, typeCourse, support, 100.0f, 10);

        when(courseServices.addCourse(any(Course.class))).thenReturn(course);

        // Act & Assert
        mockMvc.perform(post("/course/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courseDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numCourse").value(1))
                .andExpect(jsonPath("$.level").value(1));
    }

    @Test
    public void testGetAllCourses() throws Exception {
        // Arrange
        CourseDTO courseDTO = new CourseDTO(1L, 1, null, null, 100.0f, 10);
        when(courseServices.retrieveAllCourses()).thenReturn(List.of(new Course(1L, 1, typeCourse, support, 100.0f, 10)));

        // Act & Assert
        mockMvc.perform(get("/course/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].numCourse").value(1))
                .andExpect(jsonPath("$[0].level").value(1));
    }

    @Test
    public void testUpdateCourse() throws Exception {
        // Arrange
        CourseDTO courseDTO = new CourseDTO(1L, 1, null, null, 200.0f, 15);
        Course updatedCourse = new Course(1L, 1, typeCourse, support, 200.0f, 15);

        when(courseServices.updateCourse(any(Course.class))).thenReturn(updatedCourse);

        // Act & Assert
        mockMvc.perform(put("/course/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courseDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numCourse").value(1))
                .andExpect(jsonPath("$.price").value(200.0f));
    }

    @Test
    public void testGetById() throws Exception {
        // Arrange
        CourseDTO courseDTO = new CourseDTO(1L, 1, null, null, 100.0f, 10);
        when(courseServices.retrieveCourse(1L)).thenReturn(new Course(1L, 1, typeCourse, support, 100.0f, 10));

        // Act & Assert
        mockMvc.perform(get("/course/get/1-course"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numCourse").value(1))
                .andExpect(jsonPath("$.level").value(1));
    }
}
