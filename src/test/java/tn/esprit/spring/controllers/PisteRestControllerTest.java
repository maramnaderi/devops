package tn.esprit.spring.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import tn.esprit.spring.entities.Color;
import tn.esprit.spring.entities.Piste;
import tn.esprit.spring.services.IPisteServices;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@WebMvcTest(PisteRestController.class)
@ActiveProfiles("test")
public class PisteRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IPisteServices pisteServices;

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
    public void testGetAllPistes() throws Exception {
        List<Piste> pistes = Arrays.asList(piste1, piste2);
        when(pisteServices.retrieveAllPistes()).thenReturn(pistes);

        mockMvc.perform(MockMvcRequestBuilders.get("/piste/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].namePiste").value("Test Piste 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].namePiste").value("Test Piste 2"));
    }

    @Test
    public void testGetById() throws Exception {
        when(pisteServices.retrievePiste(1L)).thenReturn(piste1);

        mockMvc.perform(MockMvcRequestBuilders.get("/piste/get/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.namePiste").value("Test Piste 1"));
    }

    @Test
    public void testAddPiste() throws Exception {
        when(pisteServices.addPiste(any(Piste.class))).thenReturn(piste1);

        String jsonPiste = "{\"namePiste\":\"Test Piste 1\",\"color\":\"RED\",\"length\":1000,\"slope\":30}";

        mockMvc.perform(MockMvcRequestBuilders.post("/piste/add")
                .content(jsonPiste)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.namePiste").value("Test Piste 1"));
    }

    @Test
    public void testDeleteById() throws Exception {
        doNothing().when(pisteServices).removePiste(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/piste/delete/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
