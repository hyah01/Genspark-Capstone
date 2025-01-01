package com.genspark.cart_service.Controller;

import com.genspark.cart_service.controller.CartItemsController;
import com.genspark.cart_service.controller.SaveForLaterController;

import com.genspark.cart_service.dto.CartReqRes;
import com.genspark.cart_service.dto.SFLReqRes;
import com.genspark.cart_service.model.SaveForLaterItem;
import com.genspark.cart_service.services.CartService;
import com.genspark.cart_service.services.SaveForLaterService;
import com.genspark.cart_service.util.CartJwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.genspark.cart_service.dto.CartItemReqRes;
import com.genspark.cart_service.dto.CartReqRes;
import com.genspark.cart_service.model.CartItem;
import com.genspark.cart_service.services.CartItemService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.mockito.junit.jupiter.MockitoExtension;
import com.genspark.cart_service.services.CartService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SaveForLaterControllerTest {

    @InjectMocks
    private SaveForLaterController controller;

    @Mock
    private CartService mockCartService;

    @Mock
    private SaveForLaterService mockService;


    // Successfully add new item to save-for-later list with valid token and item data
    @Test
    public void test_add_item_success() throws Exception{
        // Arrange
    
        String token = "Bearer valid_token";
        SaveForLaterItem item = new SaveForLaterItem("123", 1);
    
        SFLReqRes expectedResponse = new SFLReqRes();
        expectedResponse.setStatusCode(200);
    
        when(mockCartService.validateAndExtractUsername(token)).thenReturn("testuser");
    
        CartReqRes cartReqRes = new CartReqRes();
        cartReqRes.setSaveForLaterId("cart123");
        when(mockCartService.getCartByEmail("testuser")).thenReturn(cartReqRes);
    
        when(mockService.addItem("cart123", item)).thenReturn(expectedResponse);
    
        // Act
        ResponseEntity<SFLReqRes> response = controller.addItem(token, item);
    
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    
        verify(mockCartService).validateAndExtractUsername(token);
        verify(mockCartService).getCartByEmail("testuser");
        verify(mockService).addItem("cart123", item);
    }

    // Handle invalid/expired JWT tokens
    @Test
    public void test_add_item_invalid_token() throws Exception{
        // Arrange
    
        String invalidToken = "Bearer invalid_token";
        SaveForLaterItem item = new SaveForLaterItem("123", 1);
    
        when(mockCartService.validateAndExtractUsername(invalidToken))
            .thenThrow(new IllegalArgumentException("Invalid token"));
    
        // Act
        ResponseEntity<SFLReqRes> response = controller.addItem(invalidToken, item);
    
        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    
        verify(mockCartService).validateAndExtractUsername(invalidToken);
        verifyNoInteractions(mockService);
    }

    // Handle missing authorization header
    @Test
    public void test_missing_authorization_header() {
        // Arrange

        SaveForLaterItem item = new SaveForLaterItem("123", 1);

        // Act
        ResponseEntity<SFLReqRes> response = controller.addItem(null, item);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    // Successfully update existing save-for-later item with valid token and item data
    @Test
    public void test_update_item_success() throws Exception{
        // Arrange

        String token = "Bearer valid_token";
        SaveForLaterItem item = new SaveForLaterItem("123", 2);

        SFLReqRes expectedResponse = new SFLReqRes();
        expectedResponse.setStatusCode(200);

        when(mockCartService.validateAndExtractUsername(token)).thenReturn("testuser");

        CartReqRes cartReqRes = new CartReqRes();
        cartReqRes.setSaveForLaterId("cart123");
        when(mockCartService.getCartByEmail("testuser")).thenReturn(cartReqRes);

        when(mockService.updateItem("cart123", item)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<SFLReqRes> response = controller.updateItem(token, item);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());

        verify(mockCartService).validateAndExtractUsername(token);
        verify(mockCartService).getCartByEmail("testuser");
        verify(mockService).updateItem("cart123", item);
    }

    // Successfully delete all items from save-for-later list with valid token
    @Test
    public void test_delete_all_items_success() throws Exception{
        // Arrange


        String token = "Bearer valid_token";
        SaveForLaterItem item = new SaveForLaterItem("123", 1);

        SFLReqRes expectedResponse = new SFLReqRes();
        expectedResponse.setStatusCode(200);

        when(mockCartService.validateAndExtractUsername(token)).thenReturn("testuser");

        CartReqRes cartReqRes = new CartReqRes();
        cartReqRes.setSaveForLaterId("cart123");
        when(mockCartService.getCartByEmail("testuser")).thenReturn(cartReqRes);

        when(mockService.deleteAllItem("testuser", item)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<SFLReqRes> response = controller.deleteAllItems(token, item);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());

        verify(mockCartService).validateAndExtractUsername(token);
        verify(mockCartService).getCartByEmail("testuser");
        verify(mockService).deleteAllItem("testuser", item);
    }



    // Successfully retrieve save-for-later items with valid token
    @Test
    public void test_get_item_success() throws Exception{
        // Arrange

        String token = "Bearer valid_token";

        SFLReqRes expectedResponse = new SFLReqRes();
        expectedResponse.setStatusCode(200);

        when(mockCartService.validateAndExtractUsername(token)).thenReturn("testuser");

        CartReqRes cartReqRes = new CartReqRes();
        cartReqRes.setSaveForLaterId("cart123");
        when(mockCartService.getCartByEmail("testuser")).thenReturn(cartReqRes);

        when(mockService.getSFLItemById("cart123")).thenReturn(expectedResponse);

        // Act
        ResponseEntity<SFLReqRes> response = controller.addItem(token);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());

        verify(mockCartService).validateAndExtractUsername(token);
        verify(mockCartService).getCartByEmail("testuser");
        verify(mockService).getSFLItemById("cart123");
    }

    // Handle malformed request bodies
    @Test
    public void test_add_item_with_malformed_request_body() throws Exception{
        // Arrange

        String token = "Bearer valid_token";
        SaveForLaterItem malformedItem = null; // Simulating a malformed request body

        when(mockCartService.validateAndExtractUsername(token)).thenReturn("testuser");

        CartReqRes cartReqRes = new CartReqRes();
        cartReqRes.setSaveForLaterId("cart123");
        when(mockCartService.getCartByEmail("testuser")).thenReturn(cartReqRes);

        // Act
        ResponseEntity<SFLReqRes> response = controller.addItem(token, malformedItem);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        verify(mockCartService).validateAndExtractUsername(token);
        verify(mockCartService).getCartByEmail("testuser");
    }

    // Handle non-existent cart IDs
    @Test
    public void test_get_item_non_existent_cart_id() throws Exception{
        // Arrange

        String token = "Bearer valid_token";

        when(mockCartService.validateAndExtractUsername(token)).thenReturn("testuser");

        CartReqRes cartReqRes = new CartReqRes();
        cartReqRes.setSaveForLaterId(null); // Simulate non-existent cart ID
        when(mockCartService.getCartByEmail("testuser")).thenReturn(cartReqRes);

        SFLReqRes expectedResponse = new SFLReqRes();
        expectedResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
        when(mockService.getSFLItemById(null)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<SFLReqRes> response = controller.addItem(token);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());

        verify(mockCartService).validateAndExtractUsername(token);
        verify(mockCartService).getCartByEmail("testuser");
        verify(mockService).getSFLItemById(null);
    }
}