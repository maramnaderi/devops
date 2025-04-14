

package tn.esprit.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.repositories.*;
import tn.esprit.spring.services.InstructorServicesImp;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InstructorServicesImpl {

    @InjectMocks
    private InstructorServicesImp InstructorServices;

    @Mock
    private IInstructorRepository instructorRepository;

    @Mock
    private ISubscriptionRepository subscriptionRepository;

    @Mock
    private IPisteRepository pisteRepository;

    @Mock
    private ICourseRepository courseRepository;

    @Mock
    private IRegistrationRepository registrationRepository;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    // 1. Test addSkier()
    @Test
    void testAddinstructorWithAnnualSubscription() {
        Subscription sub = new Subscription();
        sub.setStartDate(LocalDate.now());
        sub.setTypeSub(TypeSubscription.ANNUAL);

        Skier skier = new Skier();
        skier.setSubscription(sub);

        when(skierRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Skier saved = skierServices.addSkier(skier);

        assertNotNull(saved.getSubscription().getEndDate());
        assertEquals(sub.getStartDate().plusYears(1), saved.getSubscription().getEndDate());
    }

    // 2. Test assignSkierToSubscription()
    @Test
    void testAssignSkierToSubscription() {
        Skier skier = new Skier();
        skier.setNumSkier(1L);

        Subscription sub = new Subscription();
        sub.setNumSub(2L);

        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));
        when(subscriptionRepository.findById(2L)).thenReturn(Optional.of(sub));
        when(skierRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Skier updated = skierServices.assignSkierToSubscription(1L, 2L);

        assertEquals(sub, updated.getSubscription());
    }

    // 3. Test assignSkierToPiste()
    @Test
    void testAssignSkierToPiste() {
        Skier skier = new Skier();
        skier.setNumSkier(1L);
        skier.setPistes(new HashSet<>());

        Piste piste = new Piste();
        piste.setNumPiste(100L);

        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));
        when(pisteRepository.findById(100L)).thenReturn(Optional.of(piste));
        when(skierRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        Skier updated = skierServices.assignSkierToPiste(1L, 100L);

        assertTrue(updated.getPistes().contains(piste));
    }

    // 4. Test retrieveSkiersBySubscriptionType()
    @Test
    void testRetrieveSkiersBySubscriptionType() {
        List<Skier> expected = Arrays.asList(new Skier(), new Skier());
        when(skierRepository.findBySubscription_TypeSub(TypeSubscription.MONTHLY)).thenReturn(expected);

        List<Skier> result = skierServices.retrieveSkiersBySubscriptionType(TypeSubscription.MONTHLY);

        assertEquals(2, result.size());
    }

    // 5. Test removeSkier()
    @Test
    void testRemoveSkier() {
        Long skierId = 5L;
        skierServices.removeSkier(skierId);
        verify(skierRepository, times(1)).deleteById(skierId);
    }
}
