package tn.esprit.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Support;
import tn.esprit.spring.entities.TypeCourse;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.services.CourseServicesImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CourseServicesImplTest {

    @InjectMocks
    private CourseServicesImpl courseServices;

    @Mock
    private ICourseRepository courseRepository;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    // 1. Test retrieveAllCourses()
    @Test
    void testRetrieveAllCourses() {
        List<Course> mockCourses = Arrays.asList(new Course(), new Course());
        when(courseRepository.findAll()).thenReturn(mockCourses);

        List<Course> result = courseServices.retrieveAllCourses();

        assertEquals(2, result.size());
        verify(courseRepository, times(1)).findAll();
    }

    // 2. Test addCourse()
    @Test
    void testAddCourse() {
        Course course = new Course();
        course.setLevel(1);
        course.setSupport(Support.SKI);
        course.setTypeCourse(TypeCourse.INDIVIDUAL);
        course.setPrice(120f);
        course.setTimeSlot(2);

        when(courseRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Course saved = courseServices.addCourse(course);

        assertNotNull(saved);
        assertEquals(1, saved.getLevel());
        assertEquals(Support.SKI, saved.getSupport());
        assertEquals(TypeCourse.INDIVIDUAL, saved.getTypeCourse());
        assertEquals(120f, saved.getPrice());
        assertEquals(2, saved.getTimeSlot());
    }

    // 3. Test updateCourse()
    @Test
    void testUpdateCourse() {
        Course course = new Course();
        course.setNumCourse(1L);
        course.setLevel(2);

        when(courseRepository.save(any())).thenReturn(course);

        Course updated = courseServices.updateCourse(course);

        assertNotNull(updated);
        assertEquals(1L, updated.getNumCourse());
        assertEquals(2, updated.getLevel());
        verify(courseRepository).save(course);
    }

    // 4. Test retrieveCourse() - existing
    @Test
    void testRetrieveCourseExists() {
        Course course = new Course();
        course.setNumCourse(10L);
        course.setLevel(3);

        when(courseRepository.findById(10L)).thenReturn(Optional.of(course));

        Course found = courseServices.retrieveCourse(10L);

        assertNotNull(found);
        assertEquals(10L, found.getNumCourse());
        assertEquals(3, found.getLevel());
    }

    // 5. Test retrieveCourse() - not found
    @Test
    void testRetrieveCourseNotFound() {
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        Course found = courseServices.retrieveCourse(99L);

        assertNull(found);
    }
}
