package com.genspark.cart_service.Services;

import com.genspark.cart_service.dto.SFLReqRes;
import com.genspark.cart_service.model.SaveForLaterItem;
import com.genspark.cart_service.model.SaveForLaterItems;
import com.genspark.cart_service.repository.SaveForLaterRepository;
import com.genspark.cart_service.services.SaveForLaterImpl;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SaveForLaterImplTest {


    // Creating new SFL cart with empty items map returns success status 200
    @Test
    public void test_add_sfl_item_success() {
        // Arrange
        SaveForLaterRepository mockRepository = mock(SaveForLaterRepository.class);
        SaveForLaterItems savedItems = new SaveForLaterItems();
        savedItems.setId("123");
        savedItems.setItems(new HashMap<>());
    
        when(mockRepository.save(any(SaveForLaterItems.class))).thenReturn(savedItems);
    
        // Act
        SaveForLaterImpl saveForLaterService = new SaveForLaterImpl();
        ReflectionTestUtils.setField(saveForLaterService, "repository", mockRepository);
        SFLReqRes result = saveForLaterService.addSFLItem();
    
        // Assert
        assertEquals(200, result.getStatusCode());
        assertEquals("Successfully Created Save For Later Cart", result.getMessage());
        assertNotNull(result.getSflItems());
        assertEquals("123", result.getSflItems().getId());
        assertTrue(result.getSflItems().getItems().isEmpty());
    
        verify(mockRepository).save(any(SaveForLaterItems.class));
    }

    // Creating SFL cart fails when repository save operation fails
    @Test
    public void test_add_sfl_item_repository_failure() {
        // Arrange
        SaveForLaterRepository mockRepository = mock(SaveForLaterRepository.class);
        when(mockRepository.save(any(SaveForLaterItems.class))).thenThrow(new RuntimeException("Database error"));

        // Act
        SaveForLaterImpl saveForLaterService = new SaveForLaterImpl();
        ReflectionTestUtils.setField(saveForLaterService, "repository", mockRepository);
        SFLReqRes result = saveForLaterService.addSFLItem();

        // Assert
        assertEquals(500, result.getStatusCode());
        assertTrue(result.getMessage().contains("Error: Database error"));
        assertNull(result.getSflItems());

        verify(mockRepository).save(any(SaveForLaterItems.class));
    }

    // Success/error status codes are consistently used (200/500)
    @Test
    public void test_add_sfl_item_error_handling() {
        // Arrange
        SaveForLaterRepository mockRepository = mock(SaveForLaterRepository.class);
        when(mockRepository.save(any(SaveForLaterItems.class))).thenThrow(new RuntimeException("Database error"));

        // Act
        SaveForLaterImpl saveForLaterService = new SaveForLaterImpl();
        ReflectionTestUtils.setField(saveForLaterService, "repository", mockRepository);
        SFLReqRes result = saveForLaterService.addSFLItem();

        // Assert
        assertEquals(500, result.getStatusCode());
        assertTrue(result.getMessage().contains("Error: Database error"));

        verify(mockRepository).save(any(SaveForLaterItems.class));
    }

    // Getting non-existent SFL cart ID throws RuntimeException
    @Test
    public void test_get_sfl_item_by_id_throws_runtime_exception_for_non_existent_id() {
        // Arrange
        String nonExistentId = "nonExistentId";
        SaveForLaterRepository mockRepository = mock(SaveForLaterRepository.class);
        when(mockRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        SaveForLaterImpl saveForLaterService = new SaveForLaterImpl();
        ReflectionTestUtils.setField(saveForLaterService, "repository", mockRepository);

        SFLReqRes result = saveForLaterService.getSFLItemById(nonExistentId);

        assertEquals(500, result.getStatusCode());
        assertTrue(result.getMessage().contains("Error Occurred: "));

        verify(mockRepository).findById(nonExistentId);
    }

    // Null item map is initialized when adding/updating items
    @Test
    public void test_null_item_map_initialization_on_add() {
        // Arrange
        SaveForLaterRepository mockRepository = mock(SaveForLaterRepository.class);
        SaveForLaterItems existingItems = new SaveForLaterItems();
        existingItems.setId("123");
        existingItems.setItems(null); // Simulate null items map

        SaveForLaterItem newItem = new SaveForLaterItem("product1", 2);

        when(mockRepository.findById("123")).thenReturn(Optional.of(existingItems));
        when(mockRepository.save(any(SaveForLaterItems.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        SaveForLaterImpl saveForLaterService = new SaveForLaterImpl();
        ReflectionTestUtils.setField(saveForLaterService, "repository", mockRepository);
        SFLReqRes result = saveForLaterService.addItem("123", newItem);

        // Assert
        assertEquals(200, result.getStatusCode());
        assertEquals("Successfully updated Save For Later cart", result.getMessage());
        assertNotNull(result.getSflItems());
        assertNotNull(result.getSflItems().getItems());
        assertEquals(1, result.getSflItems().getItems().size());
        assertTrue(result.getSflItems().getItems().containsKey("product1"));

        verify(mockRepository).findById("123");
        verify(mockRepository).save(any(SaveForLaterItems.class));
    }

    // Items map uses productId as key for efficient lookups
    @Test
    public void test_add_item_uses_product_id_as_key() {
        // Arrange
        SaveForLaterRepository mockRepository = mock(SaveForLaterRepository.class);
        String productId = "prod123";
        SaveForLaterItem item = new SaveForLaterItem(productId, 2);
        SaveForLaterItems sflItems = new SaveForLaterItems();
        sflItems.setId("123");
        sflItems.setItems(new HashMap<>());

        when(mockRepository.findById("123")).thenReturn(Optional.of(sflItems));
        when(mockRepository.save(any(SaveForLaterItems.class))).thenReturn(sflItems);

        // Act
        SaveForLaterImpl saveForLaterService = new SaveForLaterImpl();
        ReflectionTestUtils.setField(saveForLaterService, "repository", mockRepository);
        SFLReqRes result = saveForLaterService.addItem("123", item);

        // Assert
        assertEquals(200, result.getStatusCode());
        assertEquals("Successfully updated Save For Later cart", result.getMessage());
        assertNotNull(result.getSflItems());
        assertTrue(result.getSflItems().getItems().containsKey(productId));
        assertEquals(item, result.getSflItems().getItems().get(productId));

        verify(mockRepository).findById("123");
        verify(mockRepository).save(any(SaveForLaterItems.class));
    }

    // Deleting all items from SFL cart clears the map and saves empty cart
    @Test
    public void test_delete_all_items_clears_cart() {
        // Arrange
        SaveForLaterRepository mockRepository = mock(SaveForLaterRepository.class);
        String cartId = "123";
        SaveForLaterItems sflItems = new SaveForLaterItems();
        sflItems.setId(cartId);
        sflItems.setItems(new HashMap<>());
        sflItems.getItems().put("product1", new SaveForLaterItem("product1", 2));

        when(mockRepository.findById(cartId)).thenReturn(Optional.of(sflItems));
        when(mockRepository.save(any(SaveForLaterItems.class))).thenReturn(sflItems);

        // Act
        SaveForLaterImpl saveForLaterService = new SaveForLaterImpl();
        ReflectionTestUtils.setField(saveForLaterService, "repository", mockRepository);
        SFLReqRes result = saveForLaterService.deleteAllItem(cartId, null);

        // Assert
        assertEquals(200, result.getStatusCode());
        assertNotNull(result.getSflItems());
        assertTrue(result.getSflItems().getItems().isEmpty());

        verify(mockRepository).save(any(SaveForLaterItems.class));
    }

    // Adding new item to SFL cart updates items map and returns success
    @Test
    public void test_add_item_to_sfl_cart_success() {
        // Arrange
        SaveForLaterRepository mockRepository = mock(SaveForLaterRepository.class);
        String cartId = "123";
        SaveForLaterItem newItem = new SaveForLaterItem("product1", 2);
        SaveForLaterItems existingItems = new SaveForLaterItems();
        existingItems.setId(cartId);
        existingItems.setItems(new HashMap<>());

        when(mockRepository.findById(cartId)).thenReturn(Optional.of(existingItems));
        when(mockRepository.save(any(SaveForLaterItems.class))).thenReturn(existingItems);

        // Act
        SaveForLaterImpl saveForLaterService = new SaveForLaterImpl();
        ReflectionTestUtils.setField(saveForLaterService, "repository", mockRepository);
        SFLReqRes result = saveForLaterService.addItem(cartId, newItem);

        // Assert
        assertEquals(200, result.getStatusCode());
        assertEquals("Successfully updated Save For Later cart", result.getMessage());
        assertNotNull(result.getSflItems());
        assertEquals(cartId, result.getSflItems().getId());
        assertTrue(result.getSflItems().getItems().containsKey("product1"));
        assertEquals(Integer.valueOf(2), result.getSflItems().getItems().get("product1").getQuantity());

        verify(mockRepository).findById(cartId);
        verify(mockRepository).save(any(SaveForLaterItems.class));
    }

    // Cart ID validation happens before any cart operations
    @Test
    public void test_cart_id_validation_before_operations() {
        // Arrange
        SaveForLaterRepository mockRepository = mock(SaveForLaterRepository.class);
        String invalidId = "invalid-id";
        when(mockRepository.findById(invalidId)).thenReturn(Optional.empty());

        // Act
        SaveForLaterImpl saveForLaterService = new SaveForLaterImpl();
        ReflectionTestUtils.setField(saveForLaterService, "repository", mockRepository);
        SFLReqRes result = saveForLaterService.getSFLItemById(invalidId);

        // Assert
        assertEquals(500, result.getStatusCode());
        assertEquals("Error Occurred: Save For Later Cart Not Found", result.getMessage());

        verify(mockRepository).findById(invalidId);
    }

    // Getting existing SFL cart by ID returns cart with status 200
    @Test
    public void test_get_sfl_item_by_id_success() {
        // Arrange
        SaveForLaterRepository mockRepository = mock(SaveForLaterRepository.class);
        String cartId = "123";
        SaveForLaterItems sflItems = new SaveForLaterItems();
        sflItems.setId(cartId);
        sflItems.setItems(new HashMap<>());

        when(mockRepository.findById(cartId)).thenReturn(Optional.of(sflItems));

        // Act
        SaveForLaterImpl saveForLaterService = new SaveForLaterImpl();
        ReflectionTestUtils.setField(saveForLaterService, "repository", mockRepository);
        SFLReqRes result = saveForLaterService.getSFLItemById(cartId);

        // Assert
        assertEquals(200, result.getStatusCode());
        assertEquals("Save For Later Cart with id '123' found successfully", result.getMessage());
        assertNotNull(result.getSflItems());
        assertEquals(cartId, result.getSflItems().getId());

        verify(mockRepository).findById(cartId);
    }

    // Repository save operation fails during item updates
    @Test
    public void test_update_item_save_failure() {
        // Arrange
        SaveForLaterRepository mockRepository = mock(SaveForLaterRepository.class);
        String itemId = "123";
        SaveForLaterItem sflItem = new SaveForLaterItem("product1", 2);
        SaveForLaterItems sflItems = new SaveForLaterItems(itemId, new HashMap<>());
        sflItems.getItems().put(sflItem.getProductId(), sflItem);

        when(mockRepository.findById(itemId)).thenReturn(Optional.of(sflItems));
        when(mockRepository.save(any(SaveForLaterItems.class))).thenThrow(new RuntimeException("Save operation failed"));

        // Act
        SaveForLaterImpl saveForLaterService = new SaveForLaterImpl();
        ReflectionTestUtils.setField(saveForLaterService, "repository", mockRepository);
        SFLReqRes result = saveForLaterService.updateItem(itemId, sflItem);

        // Assert
        assertEquals(500, result.getStatusCode());
        assertTrue(result.getMessage().contains("Error Occurred: Save operation failed"));

        verify(mockRepository).findById(itemId);
        verify(mockRepository).save(any(SaveForLaterItems.class));
    }

    // Deleting items from empty cart still returns success status
    @Test
    public void test_delete_all_items_from_empty_cart() {
        // Arrange
        SaveForLaterRepository mockRepository = mock(SaveForLaterRepository.class);
        SaveForLaterItems emptyCart = new SaveForLaterItems();
        emptyCart.setId("123");
        emptyCart.setItems(new HashMap<>());

        when(mockRepository.findById("123")).thenReturn(Optional.of(emptyCart));
        when(mockRepository.save(any(SaveForLaterItems.class))).thenReturn(emptyCart);

        // Act
        SaveForLaterImpl saveForLaterService = new SaveForLaterImpl();
        ReflectionTestUtils.setField(saveForLaterService, "repository", mockRepository);
        SFLReqRes result = saveForLaterService.deleteAllItem("123", null);

        // Assert
        assertEquals(200, result.getStatusCode());
        assertNotNull(result.getSflItems());
        assertTrue(result.getSflItems().getItems().isEmpty());

        verify(mockRepository).findById("123");
        verify(mockRepository).save(any(SaveForLaterItems.class));
    }

    // Error messages include exception details in response
    @Test
    public void test_add_sfl_item_exception_handling() {
        // Arrange
        SaveForLaterRepository mockRepository = mock(SaveForLaterRepository.class);
        when(mockRepository.save(any(SaveForLaterItems.class))).thenThrow(new RuntimeException("Database error"));

        // Act
        SaveForLaterImpl saveForLaterService = new SaveForLaterImpl();
        ReflectionTestUtils.setField(saveForLaterService, "repository", mockRepository);
        SFLReqRes result = saveForLaterService.addSFLItem();

        // Assert
        assertEquals(500, result.getStatusCode());
        assertTrue(result.getMessage().contains("Error: Database error"));

        verify(mockRepository).save(any(SaveForLaterItems.class));
    }

    // Updating item with quantity 0 removes it from the cart
    @Test
    public void test_update_item_with_zero_quantity_removes_from_cart() {
        // Arrange
        SaveForLaterRepository mockRepository = mock(SaveForLaterRepository.class);
        String cartId = "123";
        String productId = "prod-1";
        SaveForLaterItem item = new SaveForLaterItem(productId, 0);
        Map<String, SaveForLaterItem> itemsMap = new HashMap<>();
        itemsMap.put(productId, new SaveForLaterItem(productId, 5));
        SaveForLaterItems sflItems = new SaveForLaterItems(cartId, itemsMap);

        when(mockRepository.findById(cartId)).thenReturn(Optional.of(sflItems));
        when(mockRepository.save(any(SaveForLaterItems.class))).thenReturn(sflItems);

        // Act
        SaveForLaterImpl saveForLaterService = new SaveForLaterImpl();
        ReflectionTestUtils.setField(saveForLaterService, "repository", mockRepository);
        SFLReqRes result = saveForLaterService.updateItem(cartId, item);

        // Assert
        assertEquals(200, result.getStatusCode());
        assertFalse(result.getSflItems().getItems().containsKey(productId));

        verify(mockRepository).save(any(SaveForLaterItems.class));
    }

    // Adding item to non-existent cart ID returns error status 500
    @Test
    public void test_add_item_to_non_existent_cart_id_returns_error() {
        // Arrange
        SaveForLaterRepository mockRepository = mock(SaveForLaterRepository.class);
        String nonExistentCartId = "nonExistentId";
        SaveForLaterItem item = new SaveForLaterItem("product123", 1);

        when(mockRepository.findById(nonExistentCartId)).thenReturn(Optional.empty());

        // Act
        SaveForLaterImpl saveForLaterService = new SaveForLaterImpl();
        ReflectionTestUtils.setField(saveForLaterService, "repository", mockRepository);
        SFLReqRes result = saveForLaterService.addItem(nonExistentCartId, item);

        // Assert
        assertEquals(500, result.getStatusCode());
        assertTrue(result.getMessage().contains("Error Occurred"));

        verify(mockRepository).findById(nonExistentCartId);
    }

    // Updating existing item quantity in SFL cart saves changes successfully
    @Test
    public void test_update_item_quantity_success() {
        // Arrange
        SaveForLaterRepository mockRepository = mock(SaveForLaterRepository.class);
        String cartId = "123";
        String productId = "prod-001";
        SaveForLaterItem existingItem = new SaveForLaterItem(productId, 1);
        SaveForLaterItems sflItems = new SaveForLaterItems(cartId, new HashMap<>());
        sflItems.getItems().put(productId, existingItem);

        when(mockRepository.findById(cartId)).thenReturn(Optional.of(sflItems));
        when(mockRepository.save(any(SaveForLaterItems.class))).thenReturn(sflItems);

        SaveForLaterItem updatedItem = new SaveForLaterItem(productId, 5);

        // Act
        SaveForLaterImpl saveForLaterService = new SaveForLaterImpl();
        ReflectionTestUtils.setField(saveForLaterService, "repository", mockRepository);
        SFLReqRes result = saveForLaterService.updateItem(cartId, updatedItem);

        // Assert
        assertEquals(200, result.getStatusCode());
        assertNotNull(result.getSflItems());
        assertEquals(5, result.getSflItems().getItems().get(productId).getQuantity());

        verify(mockRepository).findById(cartId);
        verify(mockRepository).save(any(SaveForLaterItems.class));
    }

    // Response includes updated cart state after modifications
    @Test
    public void test_add_item_updates_cart_state() {
        // Arrange
        SaveForLaterRepository mockRepository = mock(SaveForLaterRepository.class);
        String cartId = "123";
        SaveForLaterItem newItem = new SaveForLaterItem("product1", 2);
        SaveForLaterItems existingCart = new SaveForLaterItems(cartId, new HashMap<>());
        existingCart.getItems().put("product2", new SaveForLaterItem("product2", 1));

        when(mockRepository.findById(cartId)).thenReturn(Optional.of(existingCart));
        when(mockRepository.save(any(SaveForLaterItems.class))).thenReturn(existingCart);

        // Act
        SaveForLaterImpl saveForLaterService = new SaveForLaterImpl();
        ReflectionTestUtils.setField(saveForLaterService, "repository", mockRepository);
        SFLReqRes result = saveForLaterService.addItem(cartId, newItem);

        // Assert
        assertEquals(200, result.getStatusCode());
        assertEquals("Successfully updated Save For Later cart", result.getMessage());
        assertNotNull(result.getSflItems());
        assertEquals(2, result.getSflItems().getItems().size());
        assertTrue(result.getSflItems().getItems().containsKey("product1"));
        assertEquals(Integer.valueOf(2), result.getSflItems().getItems().get("product1").getQuantity());

        verify(mockRepository).findById(cartId);
        verify(mockRepository).save(any(SaveForLaterItems.class));
    }
}