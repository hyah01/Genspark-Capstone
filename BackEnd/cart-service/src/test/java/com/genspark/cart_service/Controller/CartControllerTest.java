package com.genspark.cart_service.Controller;

import com.genspark.cart_service.controller.CartController;
import com.genspark.cart_service.dto.CartReqRes;
import com.genspark.cart_service.model.Cart;
import com.genspark.cart_service.services.CartService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartControllerTest {

    @InjectMocks
    private CartController cartController;

    @Mock
    private CartService service;


    // Successfully add a new cart and return the appropriate status code
    @Test
    public void test_add_cart_success() {
        CartReqRes request = new CartReqRes();
        request.setEmail("test@example.com");
        CartReqRes response = new CartReqRes();
        response.setStatusCode(201);
        response.setMessage("Cart added successfully");

        when(service.addCart(request)).thenReturn(response);

        ResponseEntity<CartReqRes> result = cartController.add(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(service).addCart(request);
    }

    // Add cart with invalid or missing request body data
    @Test
    public void test_add_cart_invalid_request() {
        CartReqRes request = new CartReqRes();

        CartReqRes response = new CartReqRes();
        response.setStatusCode(400);
        response.setMessage("Invalid request data");

        when(service.addCart(any(CartReqRes.class))).thenReturn(response);

        ResponseEntity<CartReqRes> result = cartController.add(request);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(service).addCart(request);
    }

    // Successfully check cart existence by email
    @Test
    public void test_has_cart_by_email_success() {
        String email = "test@example.com";
        CartReqRes response = new CartReqRes();
        response.setStatusCode(200);
        response.setMessage("Cart exists");

        when(service.hasCart(email)).thenReturn(response);

        ResponseEntity<CartReqRes> result = cartController.hasCart(email);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(service).hasCart(email);
    }

    // Successfully retrieve cart by ID with valid cart ID
    @Test
    public void test_get_cart_by_id_success() {
        String cartId = "validCartId";
        CartReqRes response = new CartReqRes();
        response.setStatusCode(200);
        response.setMessage("Cart retrieved successfully");

        when(service.getCartByCartId(cartId)).thenReturn(response);

        ResponseEntity<CartReqRes> result = cartController.getCartByID(cartId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(service).getCartByCartId(cartId);
    }

    // Successfully delete cart by ID
    @Test
    public void test_delete_cart_by_id_success() {
        String cartId = "12345";
        CartReqRes response = new CartReqRes();
        response.setStatusCode(200);
        response.setMessage("Cart deleted successfully");

        when(service.deleteCart(cartId)).thenReturn(response);

        ResponseEntity<CartReqRes> result = cartController.deleteById(cartId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(service).deleteCart(cartId);
    }

    // Delete non-existent cart ID
    @Test
    public void test_delete_non_existent_cart_id() {
        String nonExistentCartId = "nonExistentId";
        CartReqRes response = new CartReqRes();
        response.setStatusCode(404);
        response.setMessage("Cart not found");

        when(service.deleteCart(nonExistentCartId)).thenReturn(response);

        ResponseEntity<CartReqRes> result = cartController.deleteById(nonExistentCartId);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(service).deleteCart(nonExistentCartId);
    }

    // Check cart existence with non-existent email
    @Test
    public void test_has_cart_with_non_existent_email() {
        String nonExistentEmail = "nonexistent@example.com";
        CartReqRes response = new CartReqRes();
        response.setStatusCode(404);
        response.setMessage("Cart not found");

        when(service.hasCart(nonExistentEmail)).thenReturn(response);

        ResponseEntity<CartReqRes> result = cartController.hasCart(nonExistentEmail);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(service).hasCart(nonExistentEmail);
    }

    // Get cart with non-existent ID
    @Test
    public void test_get_cart_with_non_existent_id() {
        String nonExistentId = "nonExistentId";
        CartReqRes response = new CartReqRes();
        response.setStatusCode(404);
        response.setMessage("Cart not found");

        when(service.getCartByCartId(nonExistentId)).thenReturn(response);

        ResponseEntity<CartReqRes> result = cartController.getCartByID(nonExistentId);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(service).getCartByCartId(nonExistentId);
    }

    // Get cart with invalid/expired/malformed JWT token
    @Test
    public void test_get_my_cart_with_invalid_token() throws Exception {
        String invalidToken = "invalid.jwt.token";

        when(service.validateAndExtractUsername(invalidToken)).thenThrow(new IllegalArgumentException());

        ResponseEntity<CartReqRes> result = cartController.getMyCart(invalidToken);

        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        verify(service).validateAndExtractUsername(invalidToken);
    }

    // Handle null values in CartReqRes fields
    @Test
    public void test_add_cart_with_null_values() {
        CartReqRes request = new CartReqRes();
        // Intentionally leaving fields null to test null handling
        CartReqRes response = new CartReqRes();
        response.setStatusCode(400);
        response.setMessage("Invalid input");

        when(service.addCart(request)).thenReturn(response);

        ResponseEntity<CartReqRes> result = cartController.add(request);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(service).addCart(request);
    }

    // Check proper exception handling and error message format
    @Test
    public void test_get_my_cart_exception_handling() throws Exception{
        String invalidToken = "invalidToken";
        when(service.validateAndExtractUsername(invalidToken)).thenThrow(new IllegalArgumentException("Invalid token"));

        ResponseEntity<CartReqRes> response = cartController.getMyCart(invalidToken);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(service).validateAndExtractUsername(invalidToken);
    }

    // Successfully retrieve cart using valid JWT token
    @Test
    public void test_get_my_cart_with_valid_jwt() throws Exception{
        String token = "valid.jwt.token";
        String username = "test@example.com";
        CartReqRes response = new CartReqRes();
        response.setStatusCode(200);
        response.setEmail(username);

        when(service.validateAndExtractUsername(token)).thenReturn(username);
        when(service.getCartByEmail(username)).thenReturn(response);

        ResponseEntity<CartReqRes> result = cartController.getMyCart(token);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(service).validateAndExtractUsername(token);
        verify(service).getCartByEmail(username);
    }

    // Verify correct error status codes (401, 403, 500) for token validation failures
    @Test
    public void test_get_my_cart_token_validation_failures() throws Exception{
        String invalidToken = "invalidToken";

        // Mock the service to throw exceptions for different scenarios
        doThrow(new IllegalArgumentException()).when(service).validateAndExtractUsername(invalidToken);
        ResponseEntity<CartReqRes> response = cartController.getMyCart(invalidToken);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

        doThrow(new SecurityException()).when(service).validateAndExtractUsername(invalidToken);
        response = cartController.getMyCart(invalidToken);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

        doThrow(new Exception()).when(service).validateAndExtractUsername(invalidToken);
        response = cartController.getMyCart(invalidToken);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        verify(service, times(3)).validateAndExtractUsername(invalidToken);
    }

    // Validate response body structure matches CartReqRes format
    @Test
    public void test_get_cart_by_id_response_structure() {
        String cartId = "123";
        CartReqRes expectedResponse = new CartReqRes();
        expectedResponse.setStatusCode(200);
        expectedResponse.setMessage("Cart retrieved successfully");
        expectedResponse.setEmail("user@example.com");
        expectedResponse.setCart(new Cart());

        when(service.getCartByCartId(cartId)).thenReturn(expectedResponse);

        ResponseEntity<CartReqRes> result = cartController.getCartByID(cartId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedResponse, result.getBody());
        verify(service).getCartByCartId(cartId);
    }

    // Handle empty strings for ID/email parameters
    @Test
    public void test_handle_empty_string_for_id_and_email() {
        CartReqRes emptyIdResponse = new CartReqRes();
        emptyIdResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        emptyIdResponse.setMessage("Invalid ID");

        CartReqRes emptyEmailResponse = new CartReqRes();
        emptyEmailResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        emptyEmailResponse.setMessage("Invalid email");

        when(service.getCartByCartId("")).thenReturn(emptyIdResponse);
        when(service.hasCart("")).thenReturn(emptyEmailResponse);

        ResponseEntity<CartReqRes> idResult = cartController.getCartByID("");
        ResponseEntity<CartReqRes> emailResult = cartController.hasCart("");

        assertEquals(HttpStatus.BAD_REQUEST, idResult.getStatusCode());
        assertEquals(emptyIdResponse, idResult.getBody());

        assertEquals(HttpStatus.BAD_REQUEST, emailResult.getStatusCode());
        assertEquals(emptyEmailResponse, emailResult.getBody());

        verify(service).getCartByCartId("");
        verify(service).hasCart("");
    }

    // Verify idempotent behavior of delete operation
    @Test
    public void test_delete_cart_idempotency() {
        String cartId = "12345";
        CartReqRes response = new CartReqRes();
        response.setStatusCode(200);
        response.setMessage("Cart deleted successfully");

        when(service.deleteCart(cartId)).thenReturn(response);

        // First deletion
        ResponseEntity<CartReqRes> firstResult = cartController.deleteById(cartId);
        assertEquals(HttpStatus.OK, firstResult.getStatusCode());
        assertEquals(response, firstResult.getBody());

        // Second deletion (idempotent behavior)
        ResponseEntity<CartReqRes> secondResult = cartController.deleteById(cartId);
        assertEquals(HttpStatus.OK, secondResult.getStatusCode());
        assertEquals(response, secondResult.getBody());

        verify(service, times(2)).deleteCart(cartId);
    }

    // Ensure proper token extraction and validation flow
    @Test
    public void test_get_my_cart_with_valid_token() throws Exception{
        String token = "validToken";
        String username = "testUser";
        CartReqRes response = new CartReqRes();
        response.setStatusCode(200);
        response.setEmail(username);

        when(service.validateAndExtractUsername(token)).thenReturn(username);
        when(service.getCartByEmail(username)).thenReturn(response);

        ResponseEntity<CartReqRes> result = cartController.getMyCart(token);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
        verify(service).validateAndExtractUsername(token);
        verify(service).getCartByEmail(username);
    }
}


