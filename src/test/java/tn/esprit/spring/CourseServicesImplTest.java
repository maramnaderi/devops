package tn.esprit.spring;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import tn.esprit.spring.entities.Course;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.services.CourseServicesImpl;

class CourseServicesImplTest {

    @InjectMocks
    private CourseServicesImpl courseServices;

    @Mock
    private ICourseRepository courseRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddCourse() {
        Course course = new Course();
        course.setPrice(100.0f);
        when(courseRepository.save(course)).thenReturn(course);
        Course result = courseServices.addCourse(course);
        assertEquals(100.0f, result.getPrice());
    }

    @Test
    void testUpdateCourse() {
        Course course = new Course();
        course.setNumCourse(1L);
        course.setLevel(3);

        when(courseRepository.save(course)).thenReturn(course);

        Course updated = courseServices.updateCourse(course);
        assertEquals(3, updated.getLevel());
    }

    @Test
    void testRetrieveAllCourses() {
        Course c1 = new Course();
        Course c2 = new Course();

        when(courseRepository.findAll()).thenReturn(Arrays.asList(c1, c2));

        List<Course> list = courseServices.retrieveAllCourses();
        assertEquals(2, list.size());
    }

    @Test
    void testRetrieveCourse() {
        Course course = new Course();
        course.setNumCourse(10L);
        course.setLevel(2);

        when(courseRepository.findById(10L)).thenReturn(Optional.of(course));

        Course found = courseServices.retrieveCourse(10L);
        assertNotNull(found);
        assertEquals(2, found.getLevel());
    }

    @Test
    void testRetrieveCourse_NotFound() {
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        Course found = courseServices.retrieveCourse(99L);
        assertNull(found);
    }
}
