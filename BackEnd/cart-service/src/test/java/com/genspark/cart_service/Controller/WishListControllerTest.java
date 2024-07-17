package com.genspark.cart_service.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.genspark.cart_service.controller.WishListController;
import com.genspark.cart_service.model.WishList;
import com.genspark.cart_service.services.WishListService;
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
public class WishListControllerTest {

    @Mock
    private WishListService service;

    @InjectMocks
    private WishListController controller;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testAddWishList() throws Exception {
        WishList wishList = new WishList("1", "1", "1", "1");
        ObjectMapper mapper = new ObjectMapper();
        String wishListJson = mapper.writeValueAsString(wishList);

        Mockito.when(service.addToWishList(Mockito.any(WishList.class))).thenReturn(wishList);

        mockMvc.perform(post("/wishlist")
                        .contentType("application/json")
                        .content(wishListJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.cartId").value("1"))
                .andExpect(jsonPath("$.userId").value("1"))
                .andExpect(jsonPath("$.productId").value("1"));

        verify(service, times(1)).addToWishList(Mockito.any(WishList.class));
    }

    @Test
    void testUpdateWishList() throws Exception {
        WishList wishList = new WishList("1", "1", "1", "1");
        WishList updatedWishList = new WishList("1", "1", "1", "2");
        ObjectMapper mapper = new ObjectMapper();
        String wishListJson = mapper.writeValueAsString(updatedWishList);

        Mockito.when(service.getById("1")).thenReturn(wishList);
        Mockito.when(service.updateWishList(Mockito.any(WishList.class))).thenReturn(updatedWishList);

        mockMvc.perform(put("/wishlist")
                        .contentType("application/json")
                        .content(wishListJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.productId").value("2"));

        verify(service, times(1)).getById("1");
        verify(service, times(1)).updateWishList(Mockito.any(WishList.class));
    }

    @Test
    void testDeleteWishList() throws Exception {
        Mockito.when(service.deleteFromWishList("1")).thenReturn("Deleted WishList");

        mockMvc.perform(delete("/wishlist/byId/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Deleted WishList"));

        verify(service, times(1)).deleteFromWishList("1");
    }

    @Test
    void testDeleteWishList_NotFound() throws Exception {
        Mockito.when(service.deleteFromWishList("1")).thenReturn(null);

        mockMvc.perform(delete("/wishlist/byId/1"))
                .andExpect(status().isNotFound());

        verify(service, times(1)).deleteFromWishList("1");
    }

    @Test
    void testFindAllWishLists() throws Exception {
        List<WishList> wishLists = List.of(new WishList("1", "1", "1", "Item 1"),
                new WishList("2", "1", "2", "Item 2"));
        Mockito.when(service.getAllWishList()).thenReturn(wishLists);

        mockMvc.perform(get("/wishlist"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[1].id").value("2"));

        verify(service, times(1)).getAllWishList();
    }

    @Test
    void testGetWishListById() throws Exception {
        WishList wishList = new WishList("1", "1", "1", "1");
        Mockito.when(service.getById("1")).thenReturn(wishList);

        mockMvc.perform(get("/wishlist/byId/1"))
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

        mockMvc.perform(get("/wishlist/byId/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An error occurred: Unexpected error"));
    }

    @Test
    void testGetWishListById_InvalidIdFormat() throws Exception {
        mockMvc.perform(get("/wishlist/byId/invalid-id"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetWishListById_NonExistentId() throws Exception {
        Mockito.when(service.getById("999")).thenReturn(null);

        mockMvc.perform(get("/wishlist/byId/999"))
                .andExpect(status().isNotFound());

        verify(service, times(1)).getById("999");
    }

    @Test
    void testGetWishListById_NegativeId() throws Exception {
        mockMvc.perform(get("/wishlist/byId/-1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetWishListById_ServiceThrowsException() throws Exception {
        Mockito.when(service.getById("1")).thenThrow(new RuntimeException("Service exception"));

        mockMvc.perform(get("/wishlist/byId/1"))
                .andExpect(status().isInternalServerError());

        verify(service, times(1)).getById("1");
    }
}