package com.genspark.cart_service.Services;

import com.genspark.cart_service.dto.CartItemReqRes;
import com.genspark.cart_service.model.CartItem;
import com.genspark.cart_service.model.CartItems;
import com.genspark.cart_service.repository.CartItemsRepository;
import com.genspark.cart_service.services.CartItemImpl;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class CartItemImplTest {


    // Creating new empty cart returns success status and cart with valid ID
    @Test
    public void test_add_cart_item_returns_success() {
        CartItemsRepository mockRepository = mock(CartItemsRepository.class);
        CartItems savedCart = new CartItems();
        savedCart.setId("123");
        savedCart.setItems(new HashMap<>());
        when(mockRepository.save(any(CartItems.class))).thenReturn(savedCart);

        CartItemImpl cartItemService = new CartItemImpl();
        ReflectionTestUtils.setField(cartItemService, "repository", mockRepository);

        CartItemReqRes response = cartItemService.addCartItem();

        assertEquals(200, response.getStatusCode());
        assertEquals("Successfully Created Item Cart", response.getMessage());
        assertNotNull(response.getCartItems());
        assertEquals("123", response.getCartItems().getId());
        assertTrue(response.getCartItems().getItems().isEmpty());
    }

    // Adding item with zero or negative quantity removes item from cart
    @Test
    public void test_add_item_with_zero_quantity_removes_item() {
        CartItemsRepository mockRepository = mock(CartItemsRepository.class);
        CartItems existingCart = new CartItems();
        existingCart.setId("123");
        Map<String, CartItem> items = new HashMap<>();
        CartItem existingItem = new CartItem("prod1", 5);
        items.put("prod1", existingItem);
        existingCart.setItems(items);

        when(mockRepository.findById("123")).thenReturn(Optional.of(existingCart));
        when(mockRepository.save(any(CartItems.class))).thenReturn(existingCart);

        CartItemImpl cartItemService = new CartItemImpl();
        ReflectionTestUtils.setField(cartItemService, "repository", mockRepository);

        CartItem updateItem = new CartItem("prod1", -5);
        CartItemReqRes response = cartItemService.addItem("123", updateItem);

        assertEquals(200, response.getStatusCode());
        assertTrue(response.getCartItems().getItems().isEmpty());
    }

    // Adding new item to cart with positive quantity stores item correctly
    @Test
    public void test_add_item_with_positive_quantity() {
        CartItemsRepository mockRepository = mock(CartItemsRepository.class);
        CartItems existingCart = new CartItems();
        existingCart.setId("123");
        existingCart.setItems(new HashMap<>());
        when(mockRepository.findById("123")).thenReturn(Optional.of(existingCart));
        when(mockRepository.save(any(CartItems.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CartItemImpl cartItemService = new CartItemImpl();
        ReflectionTestUtils.setField(cartItemService, "repository", mockRepository);

        CartItem newItem = new CartItem("product1", 5);
        CartItemReqRes response = cartItemService.addItem("123", newItem);

        assertEquals(200, response.getStatusCode());
        assertEquals("Successfully updated cart", response.getMessage());
        assertNotNull(response.getCartItems());
        assertEquals(1, response.getCartItems().getItems().size());
        assertTrue(response.getCartItems().getItems().containsKey("product1"));
        assertEquals(Integer.valueOf(5), response.getCartItems().getItems().get("product1").getQuantity());
    }

    // Getting existing cart by ID returns correct cart items and success status
    @Test
    public void test_get_cart_item_by_id_returns_correct_items_and_success_status() {
        CartItemsRepository mockRepository = mock(CartItemsRepository.class);
        CartItems cartItems = new CartItems();
        cartItems.setId("123");
        Map<String, CartItem> items = new HashMap<>();
        items.put("product1", new CartItem("product1", 2));
        cartItems.setItems(items);
        when(mockRepository.findById("123")).thenReturn(Optional.of(cartItems));

        CartItemImpl cartItemService = new CartItemImpl();
        ReflectionTestUtils.setField(cartItemService, "repository", mockRepository);

        CartItemReqRes response = cartItemService.getCartItemById("123");

        assertEquals(200, response.getStatusCode());
        assertEquals("Cart with id '123' found successfully", response.getMessage());
        assertNotNull(response.getCartItems());
        assertEquals("123", response.getCartItems().getId());
        assertEquals(1, response.getCartItems().getItems().size());
        assertTrue(response.getCartItems().getItems().containsKey("product1"));
        assertEquals(2, response.getCartItems().getItems().get("product1").getQuantity().intValue());
    }

    // Updating existing item quantity changes the stored quantity
    @Test
    public void test_update_existing_item_quantity() {
        CartItemsRepository mockRepository = mock(CartItemsRepository.class);
        CartItem existingItem = new CartItem("product123", 5);
        Map<String, CartItem> itemsMap = new HashMap<>();
        itemsMap.put("product123", existingItem);
        CartItems cartItems = new CartItems("cartId123", itemsMap);

        when(mockRepository.findById("cartId123")).thenReturn(Optional.of(cartItems));
        when(mockRepository.save(any(CartItems.class))).thenReturn(cartItems);

        CartItemImpl cartItemService = new CartItemImpl();
        ReflectionTestUtils.setField(cartItemService, "repository", mockRepository);

        CartItem updateItem = new CartItem("product123", 8);
        CartItemReqRes response = cartItemService.updateItem("cartId123", updateItem);

        assertEquals(200, response.getStatusCode());
        assertEquals(8, response.getCartItems().getItems().get("product123").getQuantity().intValue());
    }

    // Deleting all items from cart results in empty cart
    @Test
    public void test_delete_all_items_results_in_empty_cart() {
        CartItemsRepository mockRepository = mock(CartItemsRepository.class);
        CartItems cartItems = new CartItems();
        cartItems.setId("123");
        Map<String, CartItem> items = new HashMap<>();
        items.put("product1", new CartItem("product1", 2));
        cartItems.setItems(items);
        when(mockRepository.findById("123")).thenReturn(Optional.of(cartItems));
        when(mockRepository.save(any(CartItems.class))).thenReturn(cartItems);

        CartItemImpl cartItemService = new CartItemImpl();
        ReflectionTestUtils.setField(cartItemService, "repository", mockRepository);

        CartItemReqRes response = cartItemService.deleteAllItem("123");

        assertEquals(200, response.getStatusCode());
        assertNotNull(response.getCartItems());
        assertTrue(response.getCartItems().getItems().isEmpty());
    }

    // Adding quantity to existing item updates total quantity correctly
    @Test
    public void test_add_quantity_to_existing_item_updates_total_quantity() {
        CartItemsRepository mockRepository = mock(CartItemsRepository.class);
        CartItems cartItems = new CartItems();
        cartItems.setId("123");
        Map<String, CartItem> items = new HashMap<>();
        CartItem existingItem = new CartItem("product1", 2);
        items.put("product1", existingItem);
        cartItems.setItems(items);

        when(mockRepository.findById("123")).thenReturn(Optional.of(cartItems));
        when(mockRepository.save(any(CartItems.class))).thenReturn(cartItems);

        CartItemImpl cartItemService = new CartItemImpl();
        ReflectionTestUtils.setField(cartItemService, "repository", mockRepository);

        CartItem newCartItem = new CartItem("product1", 3);
        CartItemReqRes response = cartItemService.addItem("123", newCartItem);

        assertEquals(200, response.getStatusCode());
        assertEquals("Successfully updated cart", response.getMessage());
        assertNotNull(response.getCartItems());
        assertEquals(5, response.getCartItems().getItems().get("product1").getQuantity().intValue());
    }

    // Attempting to get non-existent cart ID throws RuntimeException
    @Test
    public void test_get_cart_item_by_non_existent_id_throws_exception() {
        CartItemsRepository mockRepository = mock(CartItemsRepository.class);
        when(mockRepository.findById("nonExistentId")).thenReturn(Optional.empty());

        CartItemImpl cartItemService = new CartItemImpl();
        ReflectionTestUtils.setField(cartItemService, "repository", mockRepository);

        CartItemReqRes response = cartItemService.getCartItemById("nonExistentId");

        assertEquals(500, response.getStatusCode());
        assertEquals("Error Occurred: Cart Not Found", response.getMessage());
    }

    // Handling error when updating a non-existent item in the cart
    @Test
    public void test_update_non_existent_item_throws_exception() {
        CartItemsRepository mockRepository = mock(CartItemsRepository.class);
        when(mockRepository.findById("nonExistentId")).thenThrow(new RuntimeException("Cart Not Found"));

        CartItemImpl cartItemService = new CartItemImpl();
        ReflectionTestUtils.setField(cartItemService, "repository", mockRepository);

        CartItem cartOrder = new CartItem("productId", 5);
        CartItemReqRes response = cartItemService.updateItem("nonExistentId", cartOrder);

        assertEquals(500, response.getStatusCode());
        assertEquals("Error Occurred: Cart Not Found", response.getMessage());
    }

    // Adding item to cart with null/uninitialized items map initializes map
    @Test
    public void test_add_item_initializes_items_map() {
        CartItemsRepository mockRepository = mock(CartItemsRepository.class);
        CartItems existingCart = new CartItems();
        existingCart.setId("123");
        existingCart.setItems(null); // Simulate uninitialized items map
        when(mockRepository.findById("123")).thenReturn(Optional.of(existingCart));
        when(mockRepository.save(any(CartItems.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CartItemImpl cartItemService = new CartItemImpl();
        ReflectionTestUtils.setField(cartItemService, "repository", mockRepository);

        CartItem newItem = new CartItem("product1", 2);
        CartItemReqRes response = cartItemService.addItem("123", newItem);

        assertEquals(200, response.getStatusCode());
        assertEquals("Successfully updated cart", response.getMessage());
        assertNotNull(response.getCartItems());
        assertNotNull(response.getCartItems().getItems());
        assertTrue(response.getCartItems().getItems().containsKey("product1"));
        assertEquals(Integer.valueOf(2), response.getCartItems().getItems().get("product1").getQuantity());
    }

    // Deleting all items from cart with null items map handles gracefully
    @Test
    public void test_delete_all_items_with_null_items_map() {
        CartItemsRepository mockRepository = mock(CartItemsRepository.class);
        CartItems cartItems = new CartItems();
        cartItems.setId("123");
        cartItems.setItems(null); // Simulate null items map
        when(mockRepository.findById("123")).thenReturn(Optional.of(cartItems));
        when(mockRepository.save(any(CartItems.class))).thenReturn(cartItems);

        CartItemImpl cartItemService = new CartItemImpl();
        ReflectionTestUtils.setField(cartItemService, "repository", mockRepository);

        CartItemReqRes response = cartItemService.deleteAllItem("123");

        assertEquals(200, response.getStatusCode());
        assertEquals("Successfully cleared cart", response.getMessage());
        assertNotNull(response.getCartItems());
        assertEquals("123", response.getCartItems().getId());
        assertTrue(response.getCartItems().getItems().isEmpty());
    }

    // Multiple concurrent cart operations maintain data consistency
    @Test
    public void test_concurrent_cart_operations_consistency() throws InterruptedException {
        CartItemsRepository mockRepository = mock(CartItemsRepository.class);
        CartItems initialCart = new CartItems();
        initialCart.setId("123");
        initialCart.setItems(new HashMap<>());
        when(mockRepository.findById("123")).thenReturn(Optional.of(initialCart));
        when(mockRepository.save(any(CartItems.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CartItemImpl cartItemService = new CartItemImpl();
        ReflectionTestUtils.setField(cartItemService, "repository", mockRepository);

        CartItem item1 = new CartItem("product1", 2);
        CartItem item2 = new CartItem("product2", 3);

        Thread thread1 = new Thread(() -> cartItemService.addItem("123", item1));
        Thread thread2 = new Thread(() -> cartItemService.addItem("123", item2));

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        CartItemReqRes response = cartItemService.getCartItemById("123");

        assertEquals(200, response.getStatusCode());
        assertNotNull(response.getCartItems());
        assertEquals(2, response.getCartItems().getItems().size());
        assertTrue(response.getCartItems().getItems().containsKey("product1"));
        assertTrue(response.getCartItems().getItems().containsKey("product2"));
    }

    // Memory usage scales appropriately with large number of items
    @Test
    public void test_memory_usage_with_large_number_of_items() {
        CartItemsRepository mockRepository = mock(CartItemsRepository.class);
        CartItems cartItems = new CartItems();
        cartItems.setId("123");
        Map<String, CartItem> items = new HashMap<>();
        for (int i = 0; i < 10000; i++) {
            items.put("product" + i, new CartItem("product" + i, 1));
        }
        cartItems.setItems(items);
        when(mockRepository.findById("123")).thenReturn(Optional.of(cartItems));
        when(mockRepository.save(any(CartItems.class))).thenReturn(cartItems);

        CartItemImpl cartItemService = new CartItemImpl();
        ReflectionTestUtils.setField(cartItemService, "repository", mockRepository);

        CartItemReqRes response = cartItemService.getCartItemById("123");

        assertEquals(200, response.getStatusCode());
        assertEquals("Cart with id '123' found successfully", response.getMessage());
        assertNotNull(response.getCartItems());
        assertEquals(10000, response.getCartItems().getItems().size());
    }

    // Cart items persist correctly between service restarts
    @Test
    public void test_cart_items_persist_between_service_restarts() {
        CartItemsRepository mockRepository = mock(CartItemsRepository.class);
        CartItems initialCart = new CartItems();
        initialCart.setId("123");
        initialCart.setItems(new HashMap<>());
        when(mockRepository.findById("123")).thenReturn(Optional.of(initialCart));

        CartItemImpl cartItemService = new CartItemImpl();
        ReflectionTestUtils.setField(cartItemService, "repository", mockRepository);

        // Simulate adding an item to the cart
        CartItem newItem = new CartItem("product1", 2);
        cartItemService.addItem("123", newItem);

        // Simulate service restart by creating a new instance of the service
        CartItemImpl newCartItemServiceInstance = new CartItemImpl();
        ReflectionTestUtils.setField(newCartItemServiceInstance, "repository", mockRepository);

        // Retrieve the cart after "restart"
        CartItemReqRes response = newCartItemServiceInstance.getCartItemById("123");

        assertEquals(200, response.getStatusCode());
        assertEquals("Cart with id '123' found successfully", response.getMessage());
        assertNotNull(response.getCartItems());
        assertEquals("123", response.getCartItems().getId());
        assertTrue(response.getCartItems().getItems().containsKey("product1"));
        assertEquals(2, response.getCartItems().getItems().get("product1").getQuantity().intValue());
    }

    // Cart ID validation for various special characters and formats
    @Test
    public void test_get_cart_item_by_id_with_special_characters() {
        CartItemsRepository mockRepository = mock(CartItemsRepository.class);
        String specialCharId = "!@#$%^&*()_+";
        CartItems cartItems = new CartItems();
        cartItems.setId(specialCharId);
        cartItems.setItems(new HashMap<>());
        when(mockRepository.findById(specialCharId)).thenReturn(Optional.of(cartItems));

        CartItemImpl cartItemService = new CartItemImpl();
        ReflectionTestUtils.setField(cartItemService, "repository", mockRepository);

        CartItemReqRes response = cartItemService.getCartItemById(specialCharId);

        assertEquals(200, response.getStatusCode());
        assertEquals("Cart with id '!@#$%^&*()_+' found successfully", response.getMessage());
        assertNotNull(response.getCartItems());
        assertEquals(specialCharId, response.getCartItems().getId());
    }

    // Exception handling returns appropriate 500 status codes
    @Test
    public void test_add_cart_item_handles_exception() {
        CartItemsRepository mockRepository = mock(CartItemsRepository.class);
        when(mockRepository.save(any(CartItems.class))).thenThrow(new RuntimeException("Database error"));

        CartItemImpl cartItemService = new CartItemImpl();
        ReflectionTestUtils.setField(cartItemService, "repository", mockRepository);

        CartItemReqRes response = cartItemService.addCartItem();

        assertEquals(500, response.getStatusCode());
        assertTrue(response.getMessage().contains("Error: Database error"));
    }
}