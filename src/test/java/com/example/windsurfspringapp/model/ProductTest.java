package com.example.windsurfspringapp.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void testProductCreation() {
        // Given
        String name = "Test Product";
        String description = "Test Description";
        BigDecimal price = new BigDecimal("29.99");
        Integer stockQuantity = 100;

        // When
        Product product = new Product(name, description, price, stockQuantity);

        // Then
        assertNotNull(product);
        assertEquals(name, product.getName());
        assertEquals(description, product.getDescription());
        assertEquals(price, product.getPrice());
        assertEquals(stockQuantity, product.getStockQuantity());
        assertNull(product.getId());
        assertNull(product.getCategory());
    }

    @Test
    void testProductSettersAndGetters() {
        // Given
        Product product = new Product();
        Long id = 1L;
        String name = "Test Product";
        String description = "Test Description";
        BigDecimal price = new BigDecimal("29.99");
        Integer stockQuantity = 100;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();
        Category category = new Category("Test Category", "Test Category Description");

        // When
        product.setId(id);
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStockQuantity(stockQuantity);
        product.setCreatedAt(createdAt);
        product.setUpdatedAt(updatedAt);
        product.setCategory(category);

        // Then
        assertEquals(id, product.getId());
        assertEquals(name, product.getName());
        assertEquals(description, product.getDescription());
        assertEquals(price, product.getPrice());
        assertEquals(stockQuantity, product.getStockQuantity());
        assertEquals(createdAt, product.getCreatedAt());
        assertEquals(updatedAt, product.getUpdatedAt());
        assertEquals(category, product.getCategory());
    }

    @Test
    void testToString() {
        // Given
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setPrice(new BigDecimal("29.99"));
        product.setStockQuantity(100);

        // When
        String toString = product.toString();

        // Then
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("name='Test Product'"));
        assertTrue(toString.contains("price=29.99"));
        assertTrue(toString.contains("stockQuantity=100"));
    }
}
