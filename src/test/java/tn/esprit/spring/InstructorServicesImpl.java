package tn.esprit.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.repositories.*;
import tn.esprit.spring.services.InstructorServicesImpl;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InstructorServicesImplTest {

    @InjectMocks
    private InstructorServicesImpl instructorServices;

    @Mock
    private IInstructorRepository instructorRepository;

    @Mock
    private ICourseRepository courseRepository;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    // 1. Test addInstructorAndAssignToCourse()
    @Test
    void testAddInstructorAndAssignToCourse() {
        // Création des objets Instructor et Course
        Instructor instructor = new Instructor();
        Course course = new Course();
        course.setNumCourse(1L);

        // Comportement du mock
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(instructorRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Instructor savedInstructor = instructorServices.addInstructorAndAssignToCourse(instructor, 1L);

        // Assertions pour vérifier que l'instructeur a bien été ajouté et assigné au cours
        assertNotNull(savedInstructor);
        assertTrue(savedInstructor.getCourses().contains(course));
    }

    // 2. Test retrieveInstructor()
    @Test
    void testRetrieveInstructor() {
        Instructor instructor = new Instructor();
        instructor.setNumInstructor(1L);

        when(instructorRepository.findById(1L)).thenReturn(Optional.of(instructor));

        Instructor retrievedInstructor = instructorServices.retrieveInstructor(1L);

        assertNotNull(retrievedInstructor);
        assertEquals(1L, retrievedInstructor.getNumInstructor());
    }

    // 3. Test updateInstructor()
    @Test
    void testUpdateInstructor() {
        Instructor instructor = new Instructor();
        instructor.setNumInstructor(1L);

        when(instructorRepository.save(any())).thenReturn(instructor);

        Instructor updatedInstructor = instructorServices.updateInstructor(instructor);

        assertNotNull(updatedInstructor);
        assertEquals(1L, updatedInstructor.getNumInstructor());
    }

    // 4. Test retrieveAllInstructors()
    @Test
    void testRetrieveAllInstructors() {
        List<Instructor> expectedInstructors = Arrays.asList(new Instructor(), new Instructor());
        when(instructorRepository.findAll()).thenReturn(expectedInstructors);

        List<Instructor> allInstructors = instructorServices.retrieveAllInstructors();

        assertEquals(2, allInstructors.size());
    }

    // 5. Test addInstructor()
    @Test
    void testAddInstructor() {
        Instructor instructor = new Instructor();
        when(instructorRepository.save(any())).thenReturn(instructor);

        Instructor savedInstructor = instructorServices.addInstructor(instructor);

        assertNotNull(savedInstructor);
    }
}
