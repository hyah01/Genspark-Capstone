package com.genspark.cart_service.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.genspark.cart_service.controller.SaveForLaterController;
import com.genspark.cart_service.model.SaveForLater;
import com.genspark.cart_service.services.SaveForLaterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class SaveForLaterControllerTest {

    @Mock
    private SaveForLaterService service;

    @InjectMocks
    private SaveForLaterController controller;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testAddSaveForLater() throws Exception {
        SaveForLater saveForLater = new SaveForLater("1", "1", "1", "1");
        ObjectMapper mapper = new ObjectMapper();
        String saveForLaterJson = mapper.writeValueAsString(saveForLater);

        Mockito.when(service.addSFLList(Mockito.any(SaveForLater.class))).thenReturn(saveForLater);

        mockMvc.perform(post("/saveforlater")
                        .contentType("application/json")
                        .content(saveForLaterJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.cartId").value("1"))
                .andExpect(jsonPath("$.userId").value("1"))
                .andExpect(jsonPath("$.productId").value("1"));

        verify(service, times(1)).addSFLList(Mockito.any(SaveForLater.class));
    }

    @Test
    void testUpdateSaveForLater() throws Exception {
        SaveForLater saveForLater = new SaveForLater("1", "1", "1", "1");
        SaveForLater updatedSaveForLater = new SaveForLater("1", "1", "1", "2");
        ObjectMapper mapper = new ObjectMapper();
        String saveForLaterJson = mapper.writeValueAsString(updatedSaveForLater);

        Mockito.when(service.getById("1")).thenReturn(saveForLater);
        Mockito.when(service.updateSFLList(Mockito.any(SaveForLater.class))).thenReturn(updatedSaveForLater);

        mockMvc.perform(put("/saveforlater")
                        .contentType("application/json")
                        .content(saveForLaterJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.productId").value("2"));

        verify(service, times(1)).getById("1");
        verify(service, times(1)).updateSFLList(Mockito.any(SaveForLater.class));
    }

    @Test
    void testDeleteSaveForLater() throws Exception {
        Mockito.when(service.deleteFromSFLList("1")).thenReturn("Deleted SaveForLater");

        mockMvc.perform(delete("/saveforlater/byId/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted SaveForLater"));

        verify(service, times(1)).deleteFromSFLList("1");
    }

    @Test
    void testDeleteSaveForLater_NotFound() throws Exception {
        Mockito.when(service.deleteFromSFLList("1")).thenReturn(null);

        mockMvc.perform(delete("/saveforlater/byId/1"))
                .andExpect(status().isNotFound());

        verify(service, times(1)).deleteFromSFLList("1");
    }

    @Test
    void testFindAllSaveForLaterItems() throws Exception {
        List<SaveForLater> saveForLaterItems = List.of(new SaveForLater("1", "1", "1", "Item 1"),
                new SaveForLater("2", "1", "2", "Item 2"));
        Mockito.when(service.getSaveForLaterItems()).thenReturn(saveForLaterItems);

        mockMvc.perform(get("/saveforlater"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[1].id").value("2"));

        verify(service, times(1)).getSaveForLaterItems();
    }

    @Test
    void testGetSaveForLaterById() throws Exception {
        SaveForLater saveForLater = new SaveForLater("1", "1", "1", "1");
        Mockito.when(service.getById("1")).thenReturn(saveForLater);

        mockMvc.perform(get("/saveforlater/byId/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.cartId").value("1"))
                .andExpect(jsonPath("$.userId").value("1"))
                .andExpect(jsonPath("$.productId").value("1"));

        verify(service, times(1)).getById("1");
    }

    @Test
    void testExceptionHandling() throws Exception {
        Mockito.when(service.getById("1")).thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get("/saveforlater/byId/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An error occurred: Unexpected error"));
    }

    @Test
    void testGetSaveForLaterById_InvalidIdFormat() throws Exception {
        mockMvc.perform(get("/saveforlater/byId/invalid-id"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetSaveForLaterById_NonExistentId() throws Exception {
        Mockito.when(service.getById("999")).thenReturn(null);

        mockMvc.perform(get("/saveforlater/byId/999"))
                .andExpect(status().isNotFound());

        verify(service, times(1)).getById("999");
    }

    @Test
    void testGetSaveForLaterById_NegativeId() throws Exception {
        mockMvc.perform(get("/saveforlater/byId/-1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetSaveForLaterById_ServiceThrowsException() throws Exception {
        Mockito.when(service.getById("1")).thenThrow(new RuntimeException("Service exception"));

        mockMvc.perform(get("/saveforlater/byId/1"))
                .andExpect(status().isInternalServerError());

        verify(service, times(1)).getById("1");
    }
}