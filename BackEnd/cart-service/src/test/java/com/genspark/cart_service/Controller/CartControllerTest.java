//package com.genspark.cart_service.Controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.genspark.cart_service.controller.CartController;
//import com.genspark.cart_service.model.Cart;
//import com.genspark.cart_service.services.CartServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.RepeatedTest;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.util.ArrayList;
//import java.util.List;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@ExtendWith(MockitoExtension.class)
//public class CartControllerTest {
//
//    @Mock
//    private CartServiceImpl service;
//
//    @InjectMocks
//    private CartController controller;
//
//    private MockMvc mockMvc;
//
//    @BeforeEach
//    public void setUp(){
//        MockitoAnnotations.openMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
//    }
//
//    @Test
//    void getCartByID() throws Exception{
//        // Create a sample Cart object for the test
//        List<Cart> cart = new ArrayList<>();
//        cart.add(new Cart("1","1", List.of()));
//        cart.add(new Cart("2","2", List.of()));
//        // Mock service behavior to return cart
//        Mockito.when(service.getCartByCartId("1")).thenReturn(cart.get(0));
//        Mockito.when(service.getCartByCartId("2")).thenReturn(cart.get(1));
//        mockMvc.perform(get("/cart/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value("1"))
//                .andExpect(jsonPath("$.userId").value("1"));
//
//        // Verify that service.getCartByCartId was called once with the id 1
//        verify(service, times(1)).getCartByCartId("1");
//
//        // Perform GET request and verify response
//        mockMvc.perform(get("/cart/2"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value("2"))
//                .andExpect(jsonPath("$.userId").value("2"));
//
//        // Verify that service.getCartByCartId was called once with the id 2
//        verify(service, times(1)).getCartByCartId("2");
//    }
//
//
//    @Test
//    void getCartByID_Non_Exist_ID() throws Exception {
//        // Mocking the service to return null when an ID that doesn't exist is requested
//        Mockito.when(service.getCartByCartId("non_existing_id")).thenReturn(null);
//
//        // Perform GET request and verify response to be 404
//        mockMvc.perform(get("/cart/non_existing_id"))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    void addCart() throws Exception {
//        // Create a sample Cart object for the test
//        Cart cart = new Cart("1", "1", List.of());
//        // Convert Cart object to JSON string
//        ObjectMapper mapper = new ObjectMapper();
//        String cartJson = mapper.writeValueAsString(cart);
//        // Mock service behavior
//        Mockito.when(service.addCart(any(Cart.class))).thenReturn(cart);
//        // Perform POST request and verify response
//        mockMvc.perform(post("/cart")
//                        .contentType("application/json")
//                        .content(cartJson))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value("1"))
//                .andExpect(jsonPath("$.userId").value("1"));
//
//        // Verify that service.addCart was called once with the Cart object
//        verify(service, times(1)).addCart(any(Cart.class));
//    }
//
//    @Test
//    void addCart_ExceptionHandling() throws Exception {
//        // Mock service to throw an exception when adding a Cart
//        Mockito.when(service.addCart(any(Cart.class))).thenThrow(new RuntimeException("Database not available"));
//
//        // Create a sample Cart object for the test
//        Cart cart = new Cart("1", "user1", List.of());
//        String cartJson = new ObjectMapper().writeValueAsString(cart);
//
//        mockMvc.perform(post("/cart")
//                        .contentType("application/json")
//                        .content(cartJson))
//                .andExpect(status().isInternalServerError()); // Expecting a 500 Internal Server Error due to exception handling
//    }
//
//    @RepeatedTest(10) // Run multiple times to simulate concurrent requests
//    void addCart_ConcurrentAccess() throws Exception {
//        Cart cart = new Cart("1", "user1", List.of());
//        String cartJson = new ObjectMapper().writeValueAsString(cart);
//
//        mockMvc.perform(post("/cart")
//                        .contentType("application/json")
//                        .content(cartJson))
//                .andExpect(status().isOk());
//
//        verify(service, times(1)).addCart(any(Cart.class));
//    }
//
//    @Test
//    void deleteCartById() throws Exception {
//        // Define the ID to be deleted
//        String cartIdToDelete = "1";
//
//        // Mock the service method to return a success message or code
//        Mockito.when(service.deleteCart(anyString())).thenReturn("Deleted cart with ID " + cartIdToDelete);
//
//        // Perform the DELETE request and verify the status
//        mockMvc.perform(delete("/cart/{id}", cartIdToDelete))
//                .andExpect(status().isOk()); // Expecting HTTP 200 OK status
//
//        // Verify that the service method was called once with the correct ID
//        verify(service, times(1)).deleteCart(cartIdToDelete);
//    }
//
//    @Test
//    void deleteCartById_Success() throws Exception {
//        // Define the ID to be deleted
//        String cartIdToDelete = "1";
//
//        // Mock the service method to return a success message or code
//        Mockito.when(service.deleteCart(anyString())).thenReturn("Deleted cart with ID " + cartIdToDelete);
//
//        // Perform the DELETE request and verify the status and content
//        mockMvc.perform(delete("/cart/{id}", cartIdToDelete))
//                .andExpect(status().isOk()) // Expecting HTTP 200 OK status
//                .andExpect(content().string("Deleted cart with ID " + cartIdToDelete)); // Expecting specific response content
//
//        // Verify that the service method was called once with the correct ID
//        verify(service, times(1)).deleteCart(cartIdToDelete);
//    }
//
//    @Test
//    void deleteCartById_NotFound() throws Exception {
//        // Define a non-existent ID
//        String nonExistentCartId = "999";
//
//        // Mock the service method to return null, simulating not found scenario
//        Mockito.when(service.deleteCart(anyString())).thenReturn(null);
//
//        // Perform the DELETE request and verify the status
//        mockMvc.perform(delete("/cart/{id}", nonExistentCartId))
//                .andExpect(status().isNotFound()); // Expecting HTTP 404 Not Found status
//
//        // Verify that the service method was called once with the correct ID
//        verify(service, times(1)).deleteCart(nonExistentCartId);
//    }
//
//    @Test
//    void deleteCartById_InternalServerError() throws Exception {
//        // Define an ID that might cause an internal server error (e.g., invalid format)
//        String invalidCartId = "abc";
//
//        // Mock the service method to throw an exception or return an error message
//        Mockito.when(service.deleteCart(anyString())).thenThrow(new RuntimeException("Internal Server Error"));
//
//        // Perform the DELETE request and verify the status
//        mockMvc.perform(delete("/cart/{id}", invalidCartId))
//                .andExpect(status().isInternalServerError()); // Expecting HTTP 500 Internal Server Error status
//
//        // Verify that the service method was called once with the correct ID
//        verify(service, times(1)).deleteCart(invalidCartId);
//    }
//
//}
