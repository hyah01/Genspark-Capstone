package com.genspark.cart_service.Controller;

import com.genspark.cart_service.controller.CartItemsController;

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
public class CartItemsControllerTest {

    @InjectMocks
    private CartItemsController controller;

    @Mock
    private CartService cartService;

    @Mock
    private CartItemService service;


    // Successfully add new item to cart with valid token and cart item data
    @Test
    public void test_add_item_success() throws Exception {
        // Arrange
        String validToken = "Bearer valid.jwt.token";
        String username = "testuser@email.com";
        String cartItemId = "cart123";
        CartItem cartItem = new CartItem("prod1", 2);
        CartItemReqRes expectedResponse = new CartItemReqRes();
        expectedResponse.setStatusCode(200);
    
        when(cartService.validateAndExtractUsername(validToken)).thenReturn(username);
    
        CartReqRes cartReqRes = new CartReqRes();
        cartReqRes.setCartItemsId(cartItemId);
        when(cartService.getCartByEmail(username)).thenReturn(cartReqRes);
    
        when(service.addItem(cartItemId, cartItem)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<CartItemReqRes> response = controller.addItem(validToken, cartItem);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());
    
        verify(cartService).validateAndExtractUsername(validToken);
        verify(cartService).getCartByEmail(username);
        verify(service).addItem(cartItemId, cartItem);
    }

    // Handle invalid/expired JWT tokens
    @Test
    public void test_add_item_invalid_token() throws Exception{
        // Arrange
        String invalidToken = "Bearer invalid.jwt.token";
        CartItem cartItem = new CartItem("prod1", 2);
    
        when(cartService.validateAndExtractUsername(invalidToken))
            .thenThrow(new IllegalArgumentException("Invalid token"));

        // Act
        ResponseEntity<CartItemReqRes> response = controller.addItem(invalidToken, cartItem);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());
    
        verify(cartService).validateAndExtractUsername(invalidToken);
        verifyNoMoreInteractions(service);
    }

    // Successfully delete all items from cart with valid token
    @Test
    public void test_delete_all_items_success() throws Exception{
        // Arrange
        String validToken = "Bearer valid.jwt.token";
        String username = "testuser@email.com";
        CartItemReqRes expectedResponse = new CartItemReqRes();
        expectedResponse.setStatusCode(200);

        when(cartService.validateAndExtractUsername(validToken)).thenReturn(username);
        when(service.deleteAllItem(username)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<CartItemReqRes> response = controller.deleteAllItems(validToken);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());

        verify(cartService).validateAndExtractUsername(validToken);
        verify(service).deleteAllItem(username);
    }

    // Process requests with missing authorization header
    @Test
    public void test_add_item_missing_authorization_header() {
        // Arrange
        CartItem cartItem = new CartItem("prod1", 2);
        CartItemsController controller = new CartItemsController(service, cartService);

        // Act
        ResponseEntity<CartItemReqRes> response = controller.addItem(null, cartItem);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    // Successfully retrieve cart items with valid token
    @Test
    public void test_get_item_success() throws Exception{
        // Arrange
        String validToken = "Bearer valid.jwt.token";
        String username = "testuser@email.com";
        String cartItemId = "cart123";
        CartItemReqRes expectedResponse = new CartItemReqRes();
        expectedResponse.setStatusCode(200);

        when(cartService.validateAndExtractUsername(validToken)).thenReturn(username);

        CartReqRes cartReqRes = new CartReqRes();
        cartReqRes.setCartItemsId(cartItemId);
        when(cartService.getCartByEmail(username)).thenReturn(cartReqRes);

        when(service.getCartItemById(cartItemId)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<CartItemReqRes> response = controller.addItem(validToken);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());

        verify(cartService).validateAndExtractUsername(validToken);
        verify(cartService).getCartByEmail(username);
        verify(service).getCartItemById(cartItemId);
    }

    // Return correct HTTP status codes for successful operations
    @Test
    public void test_update_item_success() throws Exception{
        // Arrange
        String validToken = "Bearer valid.jwt.token";
        String username = "testuser@email.com";
        String cartItemId = "cart123";
        CartItem cartItem = new CartItem("prod1", 3);
        CartItemReqRes expectedResponse = new CartItemReqRes();
        expectedResponse.setStatusCode(200);

        when(cartService.validateAndExtractUsername(validToken)).thenReturn(username);

        CartReqRes cartReqRes = new CartReqRes();
        cartReqRes.setCartItemsId(cartItemId);
        when(cartService.getCartByEmail(username)).thenReturn(cartReqRes);

        when(service.updateItem(cartItemId, cartItem)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<CartItemReqRes> response = controller.updateItem(validToken, cartItem);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());

        verify(cartService).validateAndExtractUsername(validToken);
        verify(cartService).getCartByEmail(username);
        verify(service).updateItem(cartItemId, cartItem);
    }

    // Successfully process checkout with valid token
    @Test
    public void test_checkout_success() throws Exception{
        // Arrange
        String validToken = "Bearer valid.jwt.token";
        String username = "testuser@email.com";
        String cartItemId = "cart123";
        CartItemReqRes expectedResponse = new CartItemReqRes();
        expectedResponse.setStatusCode(200);

        when(cartService.validateAndExtractUsername(validToken)).thenReturn(username);

        CartReqRes cartReqRes = new CartReqRes();
        cartReqRes.setCartItemsId(cartItemId);
        when(cartService.getCartByEmail(username)).thenReturn(cartReqRes);

        when(service.deleteAllItem(cartItemId)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<CartItemReqRes> response = controller.checkout(validToken);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());

        verify(cartService).validateAndExtractUsername(validToken);
        verify(cartService).getCartByEmail(username);
        verify(service).deleteAllItem(cartItemId);
    }


    // Handle non-existent cart items during update
    @Test
    public void test_update_non_existent_cart_item() throws Exception{
        // Arrange
        String validToken = "Bearer valid.jwt.token";
        String username = "testuser@email.com";
        String cartItemId = "cart123";
        CartItem cartItem = new CartItem("prod1", 2);
        CartItemReqRes expectedResponse = new CartItemReqRes();
        expectedResponse.setStatusCode(404);
        expectedResponse.setMessage("Cart item not found");

        when(cartService.validateAndExtractUsername(validToken)).thenReturn(username);

        CartReqRes cartReqRes = new CartReqRes();
        cartReqRes.setCartItemsId(cartItemId);
        when(cartService.getCartByEmail(username)).thenReturn(cartReqRes);

        when(service.updateItem(cartItemId, cartItem)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<CartItemReqRes> response = controller.updateItem(validToken, cartItem);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());

        verify(cartService).validateAndExtractUsername(validToken);
        verify(cartService).getCartByEmail(username);
        verify(service).updateItem(cartItemId, cartItem);
    }

    // Handle empty cart during checkout
    @Test
    public void test_checkout_empty_cart() throws Exception{
        // Arrange
        String validToken = "Bearer valid.jwt.token";
        String username = "testuser@email.com";
        String cartItemId = "cart123";
        CartItemReqRes expectedResponse = new CartItemReqRes();
        expectedResponse.setStatusCode(200);
        expectedResponse.setMessage("Cart is already empty");

        when(cartService.validateAndExtractUsername(validToken)).thenReturn(username);

        CartReqRes cartReqRes = new CartReqRes();
        cartReqRes.setCartItemsId(cartItemId);
        when(cartService.getCartByEmail(username)).thenReturn(cartReqRes);

        when(service.deleteAllItem(cartItemId)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<CartItemReqRes> response = controller.checkout(validToken);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());

        verify(cartService).validateAndExtractUsername(validToken);
        verify(cartService).getCartByEmail(username);
        verify(service).deleteAllItem(cartItemId);
    }

    // Handle invalid cart item data format
    @Test
    public void test_add_item_invalid_data_format() throws Exception{
        // Arrange
        String validToken = "Bearer valid.jwt.token";
        String username = "testuser@email.com";
        String cartItemId = "cart123";
        CartItem invalidCartItem = new CartItem(null, -1); // Invalid data format
        CartItemReqRes expectedResponse = new CartItemReqRes();
        expectedResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());

        when(cartService.validateAndExtractUsername(validToken)).thenReturn(username);

        CartReqRes cartReqRes = new CartReqRes();
        cartReqRes.setCartItemsId(cartItemId);
        when(cartService.getCartByEmail(username)).thenReturn(cartReqRes);

        when(service.addItem(cartItemId, invalidCartItem)).thenThrow(new IllegalArgumentException("Invalid cart item data format"));

        // Act
        ResponseEntity<CartItemReqRes> response = controller.addItem(validToken, invalidCartItem);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        verify(cartService).validateAndExtractUsername(validToken);
        verify(cartService).getCartByEmail(username);
        verify(service).addItem(cartItemId, invalidCartItem);
    }

    // Check authorization matches cart owner
    @Test
    public void test_authorization_matches_cart_owner() throws Exception{
        // Arrange
        String validToken = "Bearer valid.jwt.token";
        String username = "testuser@email.com";
        String cartItemId = "cart123";
        CartItem cartItem = new CartItem("prod1", 2);
        CartItemReqRes expectedResponse = new CartItemReqRes();
        expectedResponse.setStatusCode(200);

        when(cartService.validateAndExtractUsername(validToken)).thenReturn(username);

        CartReqRes cartReqRes = new CartReqRes();
        cartReqRes.setCartItemsId(cartItemId);
        when(cartService.getCartByEmail(username)).thenReturn(cartReqRes);

        when(service.addItem(cartItemId, cartItem)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<CartItemReqRes> response = controller.addItem(validToken, cartItem);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());

        verify(cartService).validateAndExtractUsername(validToken);
        verify(cartService).getCartByEmail(username);
        verify(service).addItem(cartItemId, cartItem);
    }

    // Handle requests with malformed JSON body
    @Test
    public void test_add_item_malformed_json() throws Exception{
        // Arrange
        String validToken = "Bearer valid.jwt.token";
        String username = "testuser@email.com";
        String cartItemId = "cart123";
        CartItem malformedCartItem = null; // Simulating malformed JSON by passing null
        CartItemReqRes expectedResponse = new CartItemReqRes();
        expectedResponse.setStatusCode(HttpStatus.UNAUTHORIZED.value());

        when(cartService.validateAndExtractUsername(validToken)).thenReturn(username);

        CartReqRes cartReqRes = new CartReqRes();
        cartReqRes.setCartItemsId(cartItemId);
        when(cartService.getCartByEmail(username)).thenReturn(cartReqRes);

        when(service.addItem(cartItemId, malformedCartItem)).thenThrow(new IllegalArgumentException("Malformed JSON"));

        // Act
        ResponseEntity<CartItemReqRes> response = controller.addItem(validToken, malformedCartItem);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        verify(cartService).validateAndExtractUsername(validToken);
        verify(cartService).getCartByEmail(username);
        verify(service).addItem(cartItemId, malformedCartItem);
    }

    // Process concurrent modifications to same cart items
    @Test
    public void test_concurrent_modifications_to_cart_items() throws Exception{
        // Arrange
        String validToken = "Bearer valid.jwt.token";
        String username = "testuser@email.com";
        String cartItemId = "cart123";
        CartItem cartItem1 = new CartItem("prod1", 2);
        CartItem cartItem2 = new CartItem("prod1", 3);
        CartItemReqRes expectedResponse1 = new CartItemReqRes();
        expectedResponse1.setStatusCode(200);
        CartItemReqRes expectedResponse2 = new CartItemReqRes();
        expectedResponse2.setStatusCode(200);

        when(cartService.validateAndExtractUsername(validToken)).thenReturn(username);

        CartReqRes cartReqRes = new CartReqRes();
        cartReqRes.setCartItemsId(cartItemId);
        when(cartService.getCartByEmail(username)).thenReturn(cartReqRes);

        when(service.updateItem(cartItemId, cartItem1)).thenReturn(expectedResponse1);
        when(service.updateItem(cartItemId, cartItem2)).thenReturn(expectedResponse2);

        // Act
        ResponseEntity<CartItemReqRes> response1 = controller.updateItem(validToken, cartItem1);
        ResponseEntity<CartItemReqRes> response2 = controller.updateItem(validToken, cartItem2);

        // Assert
        assertEquals(HttpStatus.OK, response1.getStatusCode());
        assertEquals(expectedResponse1, response1.getBody());
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        assertEquals(expectedResponse2, response2.getBody());

        verify(cartService, times(2)).validateAndExtractUsername(validToken);
        verify(cartService, times(2)).getCartByEmail(username);
        verify(service).updateItem(cartItemId, cartItem1);
        verify(service).updateItem(cartItemId, cartItem2);
    }

    // Verify idempotency of delete operations
    @Test
    public void test_delete_all_items_idempotency() throws Exception{
        // Arrange
        String validToken = "Bearer valid.jwt.token";
        String username = "testuser@email.com";
        CartItemReqRes expectedResponse = new CartItemReqRes();
        expectedResponse.setStatusCode(200);
    
        when(cartService.validateAndExtractUsername(validToken)).thenReturn(username);
        when(service.deleteAllItem(username)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<CartItemReqRes> firstResponse = controller.deleteAllItems(validToken);
        ResponseEntity<CartItemReqRes> secondResponse = controller.deleteAllItems(validToken);

        // Assert
        assertEquals(HttpStatus.OK, firstResponse.getStatusCode());
        assertEquals(expectedResponse, firstResponse.getBody());
        assertEquals(HttpStatus.OK, secondResponse.getStatusCode());
        assertEquals(expectedResponse, secondResponse.getBody());

        verify(cartService, times(2)).validateAndExtractUsername(validToken);
        verify(service, times(2)).deleteAllItem(username);
    }

    // Ensure proper error message propagation
    @Test
    public void test_add_item_unauthorized_error() throws Exception{
        // Arrange
        String invalidToken = "Bearer invalid.jwt.token";
        CartItem cartItem = new CartItem("prod1", 2);
    
        when(cartService.validateAndExtractUsername(invalidToken)).thenThrow(new IllegalArgumentException("Invalid token"));

        // Act
        ResponseEntity<CartItemReqRes> response = controller.addItem(invalidToken, cartItem);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());

        verify(cartService).validateAndExtractUsername(invalidToken);
        verifyNoMoreInteractions(cartService, service);
    }

    // Validate cart item quantity is positive
    @Test
    public void test_add_item_with_negative_quantity() throws Exception{
        // Arrange
        String validToken = "Bearer valid.jwt.token";
        String username = "testuser@email.com";
        String cartItemId = "cart123";
        CartItem cartItem = new CartItem("prod1", -1); // Negative quantity
        CartItemReqRes expectedResponse = new CartItemReqRes();
        expectedResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        expectedResponse.setMessage("Quantity must be positive");

        when(cartService.validateAndExtractUsername(validToken)).thenReturn(username);

        CartReqRes cartReqRes = new CartReqRes();
        cartReqRes.setCartItemsId(cartItemId);
        when(cartService.getCartByEmail(username)).thenReturn(cartReqRes);

        when(service.addItem(cartItemId, cartItem)).thenReturn(expectedResponse);

        // Act
        ResponseEntity<CartItemReqRes> response = controller.addItem(validToken, cartItem);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(expectedResponse, response.getBody());

        verify(cartService).validateAndExtractUsername(validToken);
        verify(cartService).getCartByEmail(username);
        verify(service).addItem(cartItemId, cartItem);
    }
}