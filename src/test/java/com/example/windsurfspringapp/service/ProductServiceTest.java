package com.example.windsurfspringapp.service;

import com.example.windsurfspringapp.model.Category;
import com.example.windsurfspringapp.model.Product;
import com.example.windsurfspringapp.repository.ProductRepository;
import com.example.windsurfspringapp.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product1;
    private Product product2;
    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category("Electronics", "Electronic devices");
        category.setId(1L);

        product1 = new Product("Smartphone", "Latest model", new BigDecimal("599.99"), 50);
        product1.setId(1L);
        product1.setCategory(category);

        product2 = new Product("Laptop", "High performance", new BigDecimal("1299.99"), 30);
        product2.setId(2L);
        product2.setCategory(category);
    }

    @Test
    void testFindAllProducts() {
        // Given
        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        // When
        List<Product> products = productService.findAllProducts();

        // Then
        assertEquals(2, products.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testFindProductById() {
        // Given
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        when(productRepository.findById(3L)).thenReturn(Optional.empty());

        // When
        Optional<Product> foundProduct = productService.findProductById(1L);
        Optional<Product> notFoundProduct = productService.findProductById(3L);

        // Then
        assertTrue(foundProduct.isPresent());
        assertEquals(product1.getId(), foundProduct.get().getId());
        assertFalse(notFoundProduct.isPresent());
        
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).findById(3L);
    }

    @Test
    void testSaveProduct() {
        // Given
        Product newProduct = new Product("Tablet", "New tablet", new BigDecimal("399.99"), 20);
        when(productRepository.save(any(Product.class))).thenReturn(newProduct);

        // When
        Product savedProduct = productService.saveProduct(newProduct);

        // Then
        assertNotNull(savedProduct);
        assertEquals("Tablet", savedProduct.getName());
        verify(productRepository, times(1)).save(newProduct);
    }

    @Test
    void testDeleteProduct() {
        // Given
        when(productRepository.existsById(1L)).thenReturn(true);
        when(productRepository.existsById(3L)).thenReturn(false);
        doNothing().when(productRepository).deleteById(anyLong());

        // When
        boolean deleted1 = productService.deleteProduct(1L);
        boolean deleted2 = productService.deleteProduct(3L);

        // Then
        assertTrue(deleted1);
        assertFalse(deleted2);
        
        verify(productRepository, times(1)).deleteById(1L);
        verify(productRepository, never()).deleteById(3L);
    }

    @Test
    void testSearchProductsByName() {
        // Given
        when(productRepository.findByNameContainingIgnoreCase("phone")).thenReturn(List.of(product1));

        // When
        List<Product> products = productService.searchProductsByName("phone");

        // Then
        assertEquals(1, products.size());
        assertEquals(product1.getId(), products.get(0).getId());
        verify(productRepository, times(1)).findByNameContainingIgnoreCase("phone");
    }

    @Test
    void testFindProductsByCategory() {
        // Given
        when(productRepository.findByCategoryId(1L)).thenReturn(Arrays.asList(product1, product2));

        // When
        List<Product> products = productService.findProductsByCategory(1L);

        // Then
        assertEquals(2, products.size());
        verify(productRepository, times(1)).findByCategoryId(1L);
    }

    @Test
    void testFindProductsByMaxPrice() {
        // Given
        BigDecimal maxPrice = new BigDecimal("1000.00");
        when(productRepository.findByPriceLessThanEqual(maxPrice)).thenReturn(List.of(product1));

        // When
        List<Product> products = productService.findProductsByMaxPrice(maxPrice);

        // Then
        assertEquals(1, products.size());
        assertEquals(product1.getId(), products.get(0).getId());
        verify(productRepository, times(1)).findByPriceLessThanEqual(maxPrice);
    }

    @Test
    void testFindProductsByNameAndPriceRange() {
        // Given
        String name = "laptop";
        BigDecimal minPrice = new BigDecimal("1000.00");
        BigDecimal maxPrice = new BigDecimal("1500.00");
        when(productRepository.findByNameAndPriceRange(name, minPrice, maxPrice)).thenReturn(List.of(product2));

        // When
        List<Product> products = productService.findProductsByNameAndPriceRange(name, minPrice, maxPrice);

        // Then
        assertEquals(1, products.size());
        assertEquals(product2.getId(), products.get(0).getId());
        verify(productRepository, times(1)).findByNameAndPriceRange(name, minPrice, maxPrice);
    }

    @Test
    void testFindLowStockProducts() {
        // Given
        Integer threshold = 40;
        when(productRepository.findLowStockProducts(threshold)).thenReturn(List.of(product2));

        // When
        List<Product> products = productService.findLowStockProducts(threshold);

        // Then
        assertEquals(1, products.size());
        assertEquals(product2.getId(), products.get(0).getId());
        verify(productRepository, times(1)).findLowStockProducts(threshold);
    }

    @Test
    void testUpdateProductStock() {
        // Given
        Product updatedProduct = new Product("Smartphone", "Latest model", new BigDecimal("599.99"), 45);
        updatedProduct.setId(1L);
        updatedProduct.setCategory(category);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        when(productRepository.findById(3L)).thenReturn(Optional.empty());
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        // When
        Optional<Product> result1 = productService.updateProductStock(1L, 45);
        Optional<Product> result2 = productService.updateProductStock(3L, 10);

        // Then
        assertTrue(result1.isPresent());
        assertEquals(45, result1.get().getStockQuantity());
        assertFalse(result2.isPresent());
        
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).findById(3L);
        verify(productRepository, times(1)).save(any(Product.class));
    }
}
