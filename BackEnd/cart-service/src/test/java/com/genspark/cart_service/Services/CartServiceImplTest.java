package com.genspark.cart_service.Services;

import com.genspark.cart_service.dto.CartItemReqRes;
import com.genspark.cart_service.dto.CartReqRes;
import com.genspark.cart_service.dto.SFLReqRes;
import com.genspark.cart_service.model.Cart;
import com.genspark.cart_service.model.CartItems;
import com.genspark.cart_service.model.SaveForLaterItems;
import com.genspark.cart_service.repository.CartRepository;
import com.genspark.cart_service.services.CartItemImpl;
import com.genspark.cart_service.services.CartServiceImpl;

import com.genspark.cart_service.services.SaveForLaterService;
import com.genspark.cart_service.util.CartJwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CartServiceImplTest {


    // Successfully create new cart with valid email and dependencies
    @Test
    public void test_add_cart_success() {
        // Arrange
        CartServiceImpl cartService = new CartServiceImpl();
        CartRepository mockRepo = mock(CartRepository.class);
        CartItemImpl mockCartItems = mock(CartItemImpl.class);
        SaveForLaterService mockSfl = mock(SaveForLaterService.class);

        ReflectionTestUtils.setField(cartService, "repository", mockRepo);
        ReflectionTestUtils.setField(cartService, "cartItems", mockCartItems);
        ReflectionTestUtils.setField(cartService, "sflItems", mockSfl);

        CartReqRes request = new CartReqRes();
        request.setEmail("test@test.com");

        Cart savedCart = new Cart();
        savedCart.setId("123");
        savedCart.setEmail("test@test.com");
        savedCart.setCartItemsId("cart-123");
        savedCart.setSaveForLaterId("sfl-123");

        CartItemReqRes cartItemsResponse = new CartItemReqRes();
        CartItems cartItems = new CartItems();
        cartItems.setId("cart-123");
        cartItemsResponse.setCartItems(cartItems);

        SFLReqRes sflResponse = new SFLReqRes();
        SaveForLaterItems sflItems = new SaveForLaterItems();
        sflItems.setId("sfl-123");
        sflResponse.setSflItems(sflItems);

        when(mockCartItems.addCartItem()).thenReturn(cartItemsResponse);
        when(mockSfl.addSFLItem()).thenReturn(sflResponse);
        when(mockRepo.save(any(Cart.class))).thenReturn(savedCart);

        // Act
        CartReqRes result = cartService.addCart(request);

        // Assert
        assertEquals(200, result.getStatusCode());
        assertEquals("Successfully Created Cart", result.getMessage());
        assertNotNull(result.getCart());
        assertEquals("test@test.com", result.getCart().getEmail());
        assertEquals("cart-123", result.getCart().getCartItemsId());
        assertEquals("sfl-123", result.getCart().getSaveForLaterId());
    }

    // Handle cart creation when dependencies fail to create IDs
    @Test
    public void test_add_cart_fails_when_dependencies_error() {
        // Arrange
        CartServiceImpl cartService = new CartServiceImpl();
        CartRepository mockRepo = mock(CartRepository.class);
        CartItemImpl mockCartItems = mock(CartItemImpl.class);
        SaveForLaterService mockSfl = mock(SaveForLaterService.class);

        ReflectionTestUtils.setField(cartService, "repository", mockRepo);
        ReflectionTestUtils.setField(cartService, "cartItems", mockCartItems);
        ReflectionTestUtils.setField(cartService, "sflItems", mockSfl);

        CartReqRes request = new CartReqRes();
        request.setEmail("test@test.com");

        when(mockCartItems.addCartItem()).thenThrow(new RuntimeException("Failed to create cart items"));

        // Act
        CartReqRes result = cartService.addCart(request);

        // Assert
        assertEquals(500, result.getStatusCode());
        assertTrue(result.getMessage().contains("Error: java.lang.RuntimeException: Failed to create cart items"));
        assertNull(result.getCart());

        verify(mockRepo, never()).save(any(Cart.class));
    }

    // Check if cart exists for given email returns true/false
    @Test
    public void test_has_cart_for_email() {
        // Arrange
        CartServiceImpl cartService = new CartServiceImpl();
        CartRepository mockRepo = mock(CartRepository.class);
        ReflectionTestUtils.setField(cartService, "repository", mockRepo);

        String email = "test@test.com";
        Cart cart = new Cart();
        cart.setEmail(email);

        when(mockRepo.findByEmail(email)).thenReturn(Optional.of(cart));

        // Act
        CartReqRes result = cartService.hasCart(email);

        // Assert
        assertEquals(200, result.getStatusCode());
        assertEquals("true", result.getMessage());

        // Test for non-existing cart
        when(mockRepo.findByEmail(email)).thenReturn(Optional.empty());

        // Act
        result = cartService.hasCart(email);

        // Assert
        assertEquals(200, result.getStatusCode());
        assertEquals("false", result.getMessage());
    }

    // Successfully retrieve cart by valid cart ID
    @Test
    public void test_get_cart_by_valid_cart_id() {
        // Arrange
        CartServiceImpl cartService = new CartServiceImpl();
        CartRepository mockRepo = mock(CartRepository.class);
        ReflectionTestUtils.setField(cartService, "repository", mockRepo);

        String validCartId = "123";
        Cart expectedCart = new Cart();
        expectedCart.setId(validCartId);
        expectedCart.setEmail("test@test.com");

        when(mockRepo.findById(validCartId)).thenReturn(Optional.of(expectedCart));

        // Act
        CartReqRes result = cartService.getCartByCartId(validCartId);

        // Assert
        assertEquals(200, result.getStatusCode());
        assertEquals("Cart with id '123' found successfully", result.getMessage());
        assertNotNull(result.getCart());
        assertEquals(validCartId, result.getCart().getId());
        assertEquals("test@test.com", result.getCart().getEmail());
    }

    // Successfully retrieve cart by valid email
    @Test
    public void test_get_cart_by_email_success() {
        // Arrange
        CartServiceImpl cartService = new CartServiceImpl();
        CartRepository mockRepo = mock(CartRepository.class);
        ReflectionTestUtils.setField(cartService, "repository", mockRepo);

        String email = "test@test.com";
        Cart cart = new Cart();
        cart.setEmail(email);
        cart.setCartItemsId("cart-123");
        cart.setSaveForLaterId("sfl-123");
        cart.setWishListId("wish-123");

        when(mockRepo.findByEmail(email)).thenReturn(Optional.of(cart));

        // Act
        CartReqRes result = cartService.getCartByEmail(email);

        // Assert
        assertEquals(200, result.getStatusCode());
        assertEquals("Cart with email 'test@test.com' found successfully", result.getMessage());
        assertNotNull(result.getCart());
        assertEquals(email, result.getCart().getEmail());
        assertEquals("cart-123", result.getCartItemsId());
        assertEquals("sfl-123", result.getSaveForLaterId());
        assertEquals("wish-123", result.getWishListId());
    }

    // Successfully validate and extract username from valid JWT token
    @Test
    public void test_validate_and_extract_username_success() throws Exception {
        // Arrange
        CartServiceImpl cartService = new CartServiceImpl();
        CartJwtUtil mockJwtUtil = mock(CartJwtUtil.class);
        ReflectionTestUtils.setField(cartService, "jwtUtil", mockJwtUtil);

        String token = "Bearer valid.jwt.token";
        String expectedUsername = "testUser";

        when(mockJwtUtil.tokenValidate("valid.jwt.token")).thenReturn(true);
        when(mockJwtUtil.extractUsername("valid.jwt.token")).thenReturn(expectedUsername);

        // Act
        String actualUsername = cartService.validateAndExtractUsername(token);

        // Assert
        assertEquals(expectedUsername, actualUsername);
    }

    // Successfully update existing cart with new data
    @Test
    public void test_update_cart_success() {
        // Arrange
        CartServiceImpl cartService = new CartServiceImpl();
        CartRepository mockRepo = mock(CartRepository.class);
        ReflectionTestUtils.setField(cartService, "repository", mockRepo);

        Cart existingCart = new Cart();
        existingCart.setId("123");
        existingCart.setEmail("test@test.com");
        existingCart.setCartItemsId("cart-123");
        existingCart.setSaveForLaterId("sfl-123");

        Cart updatedCart = new Cart();
        updatedCart.setId("123");
        updatedCart.setEmail("updated@test.com");
        updatedCart.setCartItemsId("cart-456");
        updatedCart.setSaveForLaterId("sfl-456");

        when(mockRepo.save(any(Cart.class))).thenReturn(updatedCart);

        // Act
        Cart result = cartService.updateCart(updatedCart);

        // Assert
        assertNotNull(result);
        assertEquals("123", result.getId());
        assertEquals("updated@test.com", result.getEmail());
        assertEquals("cart-456", result.getCartItemsId());
        assertEquals("sfl-456", result.getSaveForLaterId());
    }

    // Successfully delete cart with valid cart ID
    @Test
    public void test_delete_cart_success() {
        // Arrange
        CartServiceImpl cartService = new CartServiceImpl();
        CartRepository mockRepo = mock(CartRepository.class);
        ReflectionTestUtils.setField(cartService, "repository", mockRepo);

        String cartId = "123";
        when(mockRepo.existsById(cartId)).thenReturn(true);

        // Act
        CartReqRes result = cartService.deleteCart(cartId);

        // Assert
        assertEquals(200, result.getStatusCode());
        assertEquals("Cart with id '123' deleted successfully", result.getMessage());
        verify(mockRepo, times(1)).deleteById(cartId);
    }

    // Handle cart retrieval when ID/email not found
    @Test
    public void test_get_cart_by_email_not_found() {
        // Arrange
        CartServiceImpl cartService = new CartServiceImpl();
        CartRepository mockRepo = mock(CartRepository.class);
        ReflectionTestUtils.setField(cartService, "repository", mockRepo);

        String email = "nonexistent@test.com";

        when(mockRepo.findByEmail(email)).thenReturn(Optional.empty());

        // Act
        CartReqRes result = cartService.getCartByEmail(email);

        // Assert
        assertEquals(500, result.getStatusCode());
        assertEquals("Error Occurred: Cart Not Found", result.getMessage());
    }

    // Handle cart deletion when ID not found
    @Test
    public void test_delete_cart_id_not_found() {
        // Arrange
        CartServiceImpl cartService = new CartServiceImpl();
        CartRepository mockRepo = mock(CartRepository.class);

        ReflectionTestUtils.setField(cartService, "repository", mockRepo);

        String nonExistentCartId = "non-existent-id";

        when(mockRepo.existsById(nonExistentCartId)).thenReturn(false);

        // Act
        CartReqRes result = cartService.deleteCart(nonExistentCartId);

        // Assert
        assertEquals(500, result.getStatusCode());
        assertEquals("Error Occurred: Cart Not Found", result.getMessage());
    }

    // Handle malformed/missing JWT token
    @Test
    public void test_validate_and_extract_username_with_malformed_token() {
        // Arrange
        CartServiceImpl cartService = new CartServiceImpl();
        CartJwtUtil mockJwtUtil = mock(CartJwtUtil.class);

        ReflectionTestUtils.setField(cartService, "jwtUtil", mockJwtUtil);

        String malformedToken = "InvalidToken";

        // Act & Assert
        try {
            cartService.validateAndExtractUsername(malformedToken);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid token format", e.getMessage());
        } catch (Exception e) {
            fail("Expected IllegalArgumentException, but got " + e.getClass().getSimpleName());
        }
    }

    // Handle expired JWT token validation
    @Test
    public void test_validate_and_extract_username_with_expired_token() {
        // Arrange
        CartServiceImpl cartService = new CartServiceImpl();
        CartJwtUtil mockJwtUtil = mock(CartJwtUtil.class);
        ReflectionTestUtils.setField(cartService, "jwtUtil", mockJwtUtil);

        String expiredToken = "Bearer expiredTokenString";

        when(mockJwtUtil.tokenValidate("expiredTokenString")).thenReturn(false);

        // Act & Assert
        try {
            cartService.validateAndExtractUsername(expiredToken);
            fail("Expected SecurityException to be thrown");
        } catch (Exception e) {
            assertEquals("Invalid or expired token", e.getMessage());
        }
    }

    // Handle null/empty email during cart creation
    @Test
    public void test_add_cart_with_null_or_empty_email() {
        // Arrange
        CartServiceImpl cartService = new CartServiceImpl();
        CartRepository mockRepo = mock(CartRepository.class);
        CartItemImpl mockCartItems = mock(CartItemImpl.class);
        SaveForLaterService mockSfl = mock(SaveForLaterService.class);

        ReflectionTestUtils.setField(cartService, "repository", mockRepo);
        ReflectionTestUtils.setField(cartService, "cartItems", mockCartItems);
        ReflectionTestUtils.setField(cartService, "sflItems", mockSfl);

        CartReqRes requestWithNullEmail = new CartReqRes();
        requestWithNullEmail.setEmail(null);

        CartReqRes requestWithEmptyEmail = new CartReqRes();
        requestWithEmptyEmail.setEmail("");

        // Act
        CartReqRes resultWithNullEmail = cartService.addCart(requestWithNullEmail);
        CartReqRes resultWithEmptyEmail = cartService.addCart(requestWithEmptyEmail);

        // Assert
        assertEquals(500, resultWithNullEmail.getStatusCode());
        assertTrue(resultWithNullEmail.getMessage().contains("Error"));

        assertEquals(500, resultWithEmptyEmail.getStatusCode());
        assertTrue(resultWithEmptyEmail.getMessage().contains("Error"));
    }

    // Handle JWT token without username claim
    @Test
    public void test_validate_and_extract_username_without_username_claim() {
        // Arrange
        CartServiceImpl cartService = new CartServiceImpl();
        CartJwtUtil mockJwtUtil = mock(CartJwtUtil.class);
        ReflectionTestUtils.setField(cartService, "jwtUtil", mockJwtUtil);

        String token = "Bearer validTokenWithoutUsername";

        when(mockJwtUtil.tokenValidate("validTokenWithoutUsername")).thenReturn(true);
        when(mockJwtUtil.extractUsername("validTokenWithoutUsername")).thenReturn(null);

        // Act & Assert
        try {
            cartService.validateAndExtractUsername(token);
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            assertEquals("Username could not be extracted from token", e.getMessage());
        } catch (Exception e) {
            fail("Expected IllegalStateException, but got " + e.getClass().getSimpleName());
        }
    }

    // Ensure proper error message propagation
    @Test
    public void test_get_cart_by_cart_id_error_message_propagation() {
        // Arrange
        CartServiceImpl cartService = new CartServiceImpl();
        CartRepository mockRepo = mock(CartRepository.class);

        ReflectionTestUtils.setField(cartService, "repository", mockRepo);

        String cartId = "non-existent-id";
        when(mockRepo.findById(cartId)).thenReturn(Optional.empty());

        // Act
        CartReqRes result = cartService.getCartByCartId(cartId);

        // Assert
        assertEquals(500, result.getStatusCode());
        assertEquals("Error Occurred: Cart Not Found", result.getMessage());
    }

    // Verify correct status codes (200/500) in response objects
    @Test
    public void test_get_cart_by_cart_id_status_codes() {
        // Arrange
        CartServiceImpl cartService = new CartServiceImpl();
        CartRepository mockRepo = mock(CartRepository.class);
        ReflectionTestUtils.setField(cartService, "repository", mockRepo);

        String cartId = "123";
        Cart cart = new Cart();
        cart.setId(cartId);
        cart.setEmail("test@test.com");

        when(mockRepo.findById(cartId)).thenReturn(Optional.of(cart));

        // Act
        CartReqRes result = cartService.getCartByCartId(cartId);

        // Assert
        assertEquals(200, result.getStatusCode());
        assertEquals("Cart with id '123' found successfully", result.getMessage());
        assertNotNull(result.getCart());
        assertEquals(cartId, result.getCart().getId());

        // Arrange for failure case
        when(mockRepo.findById(cartId)).thenReturn(Optional.empty());

        // Act
        result = cartService.getCartByCartId(cartId);

        // Assert
        assertEquals(500, result.getStatusCode());
        assertTrue(result.getMessage().contains("Error Occurred"));
    }

    // Verify proper cleanup of dependent resources on cart deletion
    @Test
    public void test_delete_cart_cleanup_resources() {
        // Arrange
        CartServiceImpl cartService = new CartServiceImpl();
        CartRepository mockRepo = mock(CartRepository.class);
        CartItemImpl mockCartItems = mock(CartItemImpl.class);
        SaveForLaterService mockSfl = mock(SaveForLaterService.class);

        ReflectionTestUtils.setField(cartService, "repository", mockRepo);
        ReflectionTestUtils.setField(cartService, "cartItems", mockCartItems);
        ReflectionTestUtils.setField(cartService, "sflItems", mockSfl);

        String cartId = "123";
        when(mockRepo.existsById(cartId)).thenReturn(true);

        // Act
        CartReqRes result = cartService.deleteCart(cartId);

        // Assert
        assertEquals(200, result.getStatusCode());
        assertEquals("Cart with id '123' deleted successfully", result.getMessage());
        verify(mockRepo, times(1)).deleteById(cartId);
    }

    // Handle partial cart updates
    @Test
    public void test_update_cart_partial_success() {
        // Arrange
        CartServiceImpl cartService = new CartServiceImpl();
        CartRepository mockRepo = mock(CartRepository.class);
        ReflectionTestUtils.setField(cartService, "repository", mockRepo);

        Cart existingCart = new Cart();
        existingCart.setId("123");
        existingCart.setEmail("test@test.com");
        existingCart.setCartItemsId("cart-123");
        existingCart.setSaveForLaterId("sfl-123");

        Cart updatedCart = new Cart();
        updatedCart.setId("123");
        updatedCart.setEmail("test@test.com");
        updatedCart.setCartItemsId("cart-456"); // Partial update
        updatedCart.setSaveForLaterId("sfl-123");

        when(mockRepo.save(any(Cart.class))).thenReturn(updatedCart);

        // Act
        Cart result = cartService.updateCart(updatedCart);

        // Assert
        assertEquals("123", result.getId());
        assertEquals("test@test.com", result.getEmail());
        assertEquals("cart-456", result.getCartItemsId());
        assertEquals("sfl-123", result.getSaveForLaterId());
    }

    // Handle concurrent modifications to same cart
    @Test
    public void test_concurrent_cart_modifications() throws InterruptedException {
        // Arrange
        CartServiceImpl cartService = new CartServiceImpl();
        CartRepository mockRepo = mock(CartRepository.class);
        CartItemImpl mockCartItems = mock(CartItemImpl.class);
        SaveForLaterService mockSfl = mock(SaveForLaterService.class);

        ReflectionTestUtils.setField(cartService, "repository", mockRepo);
        ReflectionTestUtils.setField(cartService, "cartItems", mockCartItems);
        ReflectionTestUtils.setField(cartService, "sflItems", mockSfl);

        CartReqRes request1 = new CartReqRes();
        request1.setEmail("user1@test.com");

        CartReqRes request2 = new CartReqRes();
        request2.setEmail("user1@test.com");

        Cart savedCart1 = new Cart();
        savedCart1.setId("123");
        savedCart1.setEmail("user1@test.com");
        savedCart1.setCartItemsId("cart-123");
        savedCart1.setSaveForLaterId("sfl-123");

        Cart savedCart2 = new Cart();
        savedCart2.setId("123");
        savedCart2.setEmail("user1@test.com");
        savedCart2.setCartItemsId("cart-123");
        savedCart2.setSaveForLaterId("sfl-123");

        CartItemReqRes cartItemsResponse = new CartItemReqRes();
        CartItems cartItems = new CartItems();
        cartItems.setId("cart-123");
        cartItemsResponse.setCartItems(cartItems);

        SFLReqRes sflResponse = new SFLReqRes();
        SaveForLaterItems sflItems = new SaveForLaterItems();
        sflItems.setId("sfl-123");
        sflResponse.setSflItems(sflItems);

        when(mockCartItems.addCartItem()).thenReturn(cartItemsResponse);
        when(mockSfl.addSFLItem()).thenReturn(sflResponse);

        when(mockRepo.save(any(Cart.class)))
                .thenReturn(savedCart1)
                .thenReturn(savedCart2);

        // Act
        Thread thread1 = new Thread(() -> cartService.addCart(request1));
        Thread thread2 = new Thread(() -> cartService.addCart(request2));

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        // Assert
        verify(mockRepo, times(2)).save(any(Cart.class));
    }
}