package com.example.windsurfspringapp.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    void testCategoryCreation() {
        // Given
        String name = "Test Category";
        String description = "Test Description";

        // When
        Category category = new Category(name, description);

        // Then
        assertNotNull(category);
        assertEquals(name, category.getName());
        assertEquals(description, category.getDescription());
        assertNull(category.getId());
        assertNotNull(category.getProducts());
        assertTrue(category.getProducts().isEmpty());
    }

    @Test
    void testCategorySettersAndGetters() {
        // Given
        Category category = new Category();
        Long id = 1L;
        String name = "Test Category";
        String description = "Test Description";
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();
        Set<Product> products = new HashSet<>();

        // When
        category.setId(id);
        category.setName(name);
        category.setDescription(description);
        category.setCreatedAt(createdAt);
        category.setUpdatedAt(updatedAt);
        category.setProducts(products);

        // Then
        assertEquals(id, category.getId());
        assertEquals(name, category.getName());
        assertEquals(description, category.getDescription());
        assertEquals(createdAt, category.getCreatedAt());
        assertEquals(updatedAt, category.getUpdatedAt());
        assertEquals(products, category.getProducts());
    }

    @Test
    void testAddProduct() {
        // Given
        Category category = new Category("Electronics", "Electronic devices");
        Product product = new Product("Smartphone", "Latest model", null, 10);

        // When
        category.addProduct(product);

        // Then
        assertEquals(1, category.getProducts().size());
        assertTrue(category.getProducts().contains(product));
        assertEquals(category, product.getCategory());
    }

    @Test
    void testRemoveProduct() {
        // Given
        Category category = new Category("Electronics", "Electronic devices");
        Product product = new Product("Smartphone", "Latest model", null, 10);
        category.addProduct(product);

        // When
        category.removeProduct(product);

        // Then
        assertEquals(0, category.getProducts().size());
        assertFalse(category.getProducts().contains(product));
        assertNull(product.getCategory());
    }

    @Test
    void testToString() {
        // Given
        Category category = new Category();
        category.setId(1L);
        category.setName("Test Category");
        category.setDescription("Test Description");

        // When
        String toString = category.toString();

        // Then
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("name='Test Category'"));
        assertTrue(toString.contains("description='Test Description'"));
    }
}
