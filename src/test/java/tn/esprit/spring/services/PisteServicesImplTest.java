package tn.esprit.spring.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import tn.esprit.spring.entities.Color;
import tn.esprit.spring.entities.Piste;
import tn.esprit.spring.repositories.IPisteRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class PisteServicesImplTest {

    @MockBean
    private IPisteRepository pisteRepository;

    @SpyBean
    private PisteServicesImpl pisteServices;

    private Piste piste1;
    private Piste piste2;

    @BeforeEach
    public void setUp() {
        // Create test pistes
        piste1 = new Piste();
        piste1.setNumPiste(1L);
        piste1.setNamePiste("Test Piste 1");
        piste1.setColor(Color.RED);
        piste1.setLength(1000);
        piste1.setSlope(30);

        piste2 = new Piste();
        piste2.setNumPiste(2L);
        piste2.setNamePiste("Test Piste 2");
        piste2.setColor(Color.BLUE);
        piste2.setLength(800);
        piste2.setSlope(20);
    }

    @Test
    public void testRetrieveAllPistes() {
        // Arrange
        List<Piste> pistes = Arrays.asList(piste1, piste2);
        when(pisteRepository.findAll()).thenReturn(pistes);

        // Act
        List<Piste> retrievedPistes = pisteServices.retrieveAllPistes();

        // Assert
        assertEquals(2, retrievedPistes.size());
        assertEquals("Test Piste 1", retrievedPistes.get(0).getNamePiste());
        assertEquals("Test Piste 2", retrievedPistes.get(1).getNamePiste());
        verify(pisteRepository).findAll();
    }

    @Test
    public void testAddPiste() {
        // Arrange
        when(pisteRepository.save(any(Piste.class))).thenReturn(piste1);

        // Act
        Piste savedPiste = pisteServices.addPiste(piste1);

        // Assert
        assertNotNull(savedPiste);
        assertEquals("Test Piste 1", savedPiste.getNamePiste());
        assertEquals(Color.RED, savedPiste.getColor());
        verify(pisteRepository).save(piste1);
    }

    @Test
    public void testRetrievePiste() {
        // Arrange
        when(pisteRepository.findById(1L)).thenReturn(Optional.of(piste1));

        // Act
        Piste retrievedPiste = pisteServices.retrievePiste(1L);

        // Assert
        assertNotNull(retrievedPiste);
        assertEquals(1L, retrievedPiste.getNumPiste());
        assertEquals("Test Piste 1", retrievedPiste.getNamePiste());
        verify(pisteRepository).findById(1L);
    }

    @Test
    public void testRemovePiste() {
        // Act
        pisteServices.removePiste(1L);

        // Assert
        verify(pisteRepository).deleteById(1L);
    }
}
