//package com.genspark.cart_service.Controller;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.genspark.cart_service.controller.CartItemsController;
//import com.genspark.cart_service.model.CartItem;
//import com.genspark.cart_service.services.CartItemImpl;
//import org.junit.jupiter.api.BeforeEach;
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
//import java.util.List;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//@ExtendWith(MockitoExtension.class)
//public class CartItemsControllerTest {
//
//    @Mock
//    private CartItemImpl service;
//
//    @InjectMocks
//    private CartItemsController controller;
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
//    void testAddCartOrder() throws Exception {
//        // Create a sample Cart Order object for the test
//        CartItem cartOrder = new CartItem("1", "1", "1", 1);
//        // Convert Cart Order object to JSON string
//        ObjectMapper mapper = new ObjectMapper();
//        String cartJson = mapper.writeValueAsString(cartOrder);
//        // Mock service behavior
//        Mockito.when(service.addCartOrder(Mockito.any(CartItem.class))).thenReturn(cartOrder);
//        // Perform POST request and verify response
//        mockMvc.perform(post("/cartorder")
//                        .contentType("application/json")
//                        .content(cartJson))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value("1"))
//                .andExpect(jsonPath("$.cartId").value("1"))
//                .andExpect(jsonPath("$.productId").value("1"))
//                .andExpect(jsonPath("$.quantity").value(1));
//        // Verify that service.addCartOrder was called once with the Cart Order object
//        verify(service, times(1)).addCartOrder(Mockito.any(CartItem.class));
//    }
//
//    @Test
//    void testUpdateCartOrder() throws Exception {
//        CartItem cartOrder = new CartItem("1", "1", "1", 1);
//        CartItem updatedCartOrder = new CartItem("1", "1", "1", 2);
//        // Convert Cart Order object to JSON string
//        ObjectMapper mapper = new ObjectMapper();
//        String cartJson = mapper.writeValueAsString(updatedCartOrder);
//
//        Mockito.when(service.getCartOrderById("1")).thenReturn(cartOrder);
//        Mockito.when(service.updateCartOrder(Mockito.any(CartItem.class))).thenReturn(updatedCartOrder);
//
//        mockMvc.perform(put("/cartorder")
//                        .contentType("application/json")
//                        .content(cartJson))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value("1"))
//                .andExpect(jsonPath("$.quantity").value(2));
//
//        verify(service, times(1)).getCartOrderById("1");
//        verify(service, times(1)).updateCartOrder(Mockito.any(CartItem.class));
//    }
//
//    @Test
//    void testDeleteCartOrder() throws Exception {
//        // Test Delete on a valid ID
//        Mockito.when(service.deleteCartOrder("1")).thenReturn("Deleted CartOrder");
//
//        mockMvc.perform(delete("/cartorder/byId/1"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Deleted CartOrder"));
//
//        verify(service, times(1)).deleteCartOrder("1");
//    }
//
//    @Test
//    void testDeleteCartOrder_NotFound() throws Exception {
//        // Test Delete on a valid ID
//        Mockito.when(service.deleteCartOrder("1")).thenReturn(null);
//
//        mockMvc.perform(delete("/cartorder/byId/1"))
//                .andExpect(status().isNotFound()); // Expecting HTTP 404 Not Found status
//
//        verify(service, times(1)).deleteCartOrder("1");
//    }
//
//    @Test
//    void testDeleteAllCartOrdersByCartId() throws Exception {
//        Mockito.when(service.deleteAllByCartId("1")).thenReturn("Deleted All CartOrders");
//
//        mockMvc.perform(delete("/cartorder/1"))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Deleted All CartOrders"));
//
//        verify(service, times(1)).deleteAllByCartId("1");
//    }
//
//    @Test
//    void testFindAllCartOrders() throws Exception {
//        List<CartItem> cartOrders = List.of(new CartItem("1", "1", "1", 1),
//                new CartItem("2", "1", "2", 2));
//        Mockito.when(service.getAllCartOrder()).thenReturn(cartOrders);
//
//        mockMvc.perform(get("/cartorder"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].id").value("1"))
//                .andExpect(jsonPath("$[1].id").value("2"));
//
//        verify(service, times(1)).getAllCartOrder();
//    }
//
//    @Test
//    void testGetCartOrderById() throws Exception {
//        CartItem cartOrder = new CartItem("1", "1", "1", 1);
//        Mockito.when(service.getCartOrderById("1")).thenReturn(cartOrder);
//
//        mockMvc.perform(get("/cartorder/byId/1"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value("1"))
//                .andExpect(jsonPath("$.cartId").value("1"))
//                .andExpect(jsonPath("$.quantity").value(1));
//
//        verify(service, times(1)).getCartOrderById("1");
//    }
//
//    @Test
//    void testExceptionHandling() throws Exception {
//        Mockito.when(service.getCartOrderById("1")).thenThrow(new RuntimeException("Unexpected error"));
//
//        mockMvc.perform(get("/cartorder/byId/1"))
//                .andExpect(status().isInternalServerError())
//                .andExpect(content().string("An error occurred: Unexpected error"));
//    }
//
//    @Test
//    void testGetCartOrderById_InvalidIdFormat() throws Exception {
//        mockMvc.perform(get("/cartorder/byId/invalid-id"))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    void testGetCartOrderById_NonExistentId() throws Exception {
//        Mockito.when(service.getCartOrderById("999")).thenReturn(null);
//
//        mockMvc.perform(get("/cartorder/byId/999"))
//                .andExpect(status().isNotFound());
//
//        verify(service, times(1)).getCartOrderById("999");
//    }
//
//
//    @Test
//    void testGetCartOrderById_NegativeId() throws Exception {
//        mockMvc.perform(get("/cartorder/byId/-1"))
//                .andExpect(status().isNotFound());
//    }
//
//    @Test
//    void testGetCartOrderById_ServiceThrowsException() throws Exception {
//        Mockito.when(service.getCartOrderById("1")).thenThrow(new RuntimeException("Service exception"));
//
//        mockMvc.perform(get("/cartorder/byId/1"))
//                .andExpect(status().isInternalServerError());
//
//        verify(service, times(1)).getCartOrderById("1");
//    }
//}
