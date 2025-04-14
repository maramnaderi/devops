package tn.esprit.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import tn.esprit.spring.entities.Skier;
import tn.esprit.spring.entities.Subscription;
import tn.esprit.spring.entities.TypeSubscription;
import tn.esprit.spring.repositories.ISkierRepository;
import tn.esprit.spring.repositories.ISubscriptionRepository;
import tn.esprit.spring.services.SubscriptionServicesImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubscriptionServicesImplTest {

    @InjectMocks
    private SubscriptionServicesImpl subscriptionServices;

    @Mock
    private ISubscriptionRepository subscriptionRepository;

    @Mock
    private ISkierRepository skierRepository;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    // 1. Test addSubscription() for ANNUAL type
    @Test
    void testAddSubscriptionAnnual() {
        // Arrange
        Subscription subscription = new Subscription();
        subscription.setTypeSub(TypeSubscription.ANNUAL);
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        subscription.setStartDate(startDate);
        
        when(subscriptionRepository.save(any(Subscription.class))).thenAnswer(i -> i.getArgument(0));
        
        // Act
        Subscription result = subscriptionServices.addSubscription(subscription);
        
        // Assert
        assertEquals(startDate.plusYears(1), result.getEndDate());
        verify(subscriptionRepository, times(1)).save(subscription);
    }
    
    // 2. Test addSubscription() for SEMESTRIEL type
    @Test
    void testAddSubscriptionSemestriel() {
        // Arrange
        Subscription subscription = new Subscription();
        subscription.setTypeSub(TypeSubscription.SEMESTRIEL);
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        subscription.setStartDate(startDate);
        
        when(subscriptionRepository.save(any(Subscription.class))).thenAnswer(i -> i.getArgument(0));
        
        // Act
        Subscription result = subscriptionServices.addSubscription(subscription);
        
        // Assert
        assertEquals(startDate.plusMonths(6), result.getEndDate());
        verify(subscriptionRepository, times(1)).save(subscription);
    }
    
    // 3. Test addSubscription() for MONTHLY type
    @Test
    void testAddSubscriptionMonthly() {
        // Arrange
        Subscription subscription = new Subscription();
        subscription.setTypeSub(TypeSubscription.MONTHLY);
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        subscription.setStartDate(startDate);
        
        when(subscriptionRepository.save(any(Subscription.class))).thenAnswer(i -> i.getArgument(0));
        
        // Act
        Subscription result = subscriptionServices.addSubscription(subscription);
        
        // Assert
        assertEquals(startDate.plusMonths(1), result.getEndDate());
        verify(subscriptionRepository, times(1)).save(subscription);
    }

    // 4. Test updateSubscription()
    @Test
    void testUpdateSubscription() {
        // Arrange
        Subscription subscription = new Subscription();
        subscription.setNumSub(1L);
        subscription.setTypeSub(TypeSubscription.MONTHLY);
        
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(subscription);
        
        // Act
        Subscription result = subscriptionServices.updateSubscription(subscription);
        
        // Assert
        assertNotNull(result);
        assertEquals(subscription.getNumSub(), result.getNumSub());
        verify(subscriptionRepository, times(1)).save(subscription);
    }

    // 5. Test retrieveSubscriptionById()
    @Test
    void testRetrieveSubscriptionById() {
        // Arrange
        Long subscriptionId = 1L;
        Subscription expected = new Subscription();
        expected.setNumSub(subscriptionId);
        
        when(subscriptionRepository.findById(subscriptionId)).thenReturn(Optional.of(expected));
        
        // Act
        Subscription result = subscriptionServices.retrieveSubscriptionById(subscriptionId);
        
        // Assert
        assertNotNull(result);
        assertEquals(subscriptionId, result.getNumSub());
    }
    
    // 6. Test retrieveSubscriptionById() with non-existent ID
    @Test
    void testRetrieveSubscriptionByIdNotFound() {
        // Arrange
        Long subscriptionId = 999L;
        
        when(subscriptionRepository.findById(subscriptionId)).thenReturn(Optional.empty());
        
        // Act
        Subscription result = subscriptionServices.retrieveSubscriptionById(subscriptionId);
        
        // Assert
        assertNull(result);
    }

    // 7. Test getSubscriptionByType()
    @Test
    void testGetSubscriptionByType() {
        // Arrange
        TypeSubscription type = TypeSubscription.ANNUAL;
        Set<Subscription> expected = new HashSet<>();
        Subscription sub1 = new Subscription();
        sub1.setNumSub(1L);
        Subscription sub2 = new Subscription();
        sub2.setNumSub(2L);
        expected.add(sub1);
        expected.add(sub2);
        
        when(subscriptionRepository.findByTypeSubOrderByStartDateAsc(type)).thenReturn(expected);
        
        // Act
        Set<Subscription> result = subscriptionServices.getSubscriptionByType(type);
        
        // Assert
        assertEquals(2, result.size());
        verify(subscriptionRepository, times(1)).findByTypeSubOrderByStartDateAsc(type);
    }

    // 8. Test retrieveSubscriptionsByDates()
    @Test
    void testRetrieveSubscriptionsByDates() {
        // Arrange
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 12, 31);
        List<Subscription> expected = Arrays.asList(new Subscription(), new Subscription());
        
        when(subscriptionRepository.getSubscriptionsByStartDateBetween(startDate, endDate)).thenReturn(expected);
        
        // Act
        List<Subscription> result = subscriptionServices.retrieveSubscriptionsByDates(startDate, endDate);
        
        // Assert
        assertEquals(2, result.size());
        verify(subscriptionRepository, times(1)).getSubscriptionsByStartDateBetween(startDate, endDate);
    }

    // 9. Test retrieveSubscriptions() scheduled method
    @Test
    void testRetrieveSubscriptions() {
        // Arrange
        List<Subscription> subscriptions = new ArrayList<>();
        Subscription sub1 = new Subscription();
        sub1.setNumSub(1L);
        sub1.setEndDate(LocalDate.now().plusMonths(1));
        subscriptions.add(sub1);
        
        Skier skier1 = new Skier();
        skier1.setFirstName("John");
        skier1.setLastName("Doe");
        
        when(subscriptionRepository.findDistinctOrderByEndDateAsc()).thenReturn(subscriptions);
        when(skierRepository.findBySubscription(sub1)).thenReturn(skier1);
        
        // Act - This is just to check if the method runs without exceptions
        subscriptionServices.retrieveSubscriptions();
        
        // Assert
        verify(subscriptionRepository, times(1)).findDistinctOrderByEndDateAsc();
        verify(skierRepository, times(1)).findBySubscription(sub1);
    }

    // 10. Test showMonthlyRecurringRevenue() scheduled method
    @Test
    void testShowMonthlyRecurringRevenue() {
        // Arrange
        when(subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.MONTHLY)).thenReturn(100.0f);
        when(subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.SEMESTRIEL)).thenReturn(600.0f);
        when(subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.ANNUAL)).thenReturn(1200.0f);
        
        // Act - This is just to check if the method runs without exceptions
        subscriptionServices.showMonthlyRecurringRevenue();
        
        // Assert
        verify(subscriptionRepository, times(1)).recurringRevenueByTypeSubEquals(TypeSubscription.MONTHLY);
        verify(subscriptionRepository, times(1)).recurringRevenueByTypeSubEquals(TypeSubscription.SEMESTRIEL);
        verify(subscriptionRepository, times(1)).recurringRevenueByTypeSubEquals(TypeSubscription.ANNUAL);
    }
}
