package com.example.windsurfspringapp.repository;

import com.example.windsurfspringapp.model.Category;
import com.example.windsurfspringapp.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category electronicsCategory;
    private Category clothingCategory;
    private Product smartphone;
    private Product laptop;
    private Product tshirt;

    @BeforeEach
    void setUp() {
        // Create and persist categories
        electronicsCategory = new Category("Electronics", "Electronic devices and gadgets");
        clothingCategory = new Category("Clothing", "Apparel and accessories");
        
        entityManager.persist(electronicsCategory);
        entityManager.persist(clothingCategory);
        
        // Create products
        smartphone = new Product("Smartphone", "Latest model smartphone", new BigDecimal("599.99"), 50);
        smartphone.setCategory(electronicsCategory);
        
        laptop = new Product("Laptop", "High-performance laptop", new BigDecimal("1299.99"), 30);
        laptop.setCategory(electronicsCategory);
        
        tshirt = new Product("T-Shirt", "Cotton t-shirt", new BigDecimal("19.99"), 200);
        tshirt.setCategory(clothingCategory);
        
        // Persist products
        entityManager.persist(smartphone);
        entityManager.persist(laptop);
        entityManager.persist(tshirt);
        
        // Flush to ensure data is in the database
        entityManager.flush();
    }

    @Test
    void testFindByNameContainingIgnoreCase() {
        // When
        List<Product> laptopResults = productRepository.findByNameContainingIgnoreCase("laptop");
        List<Product> phoneResults = productRepository.findByNameContainingIgnoreCase("phone");
        List<Product> shirtResults = productRepository.findByNameContainingIgnoreCase("shirt");
        
        // Then
        assertEquals(1, laptopResults.size());
        assertEquals(laptop.getId(), laptopResults.get(0).getId());
        
        assertEquals(1, phoneResults.size());
        assertEquals(smartphone.getId(), phoneResults.get(0).getId());
        
        assertEquals(1, shirtResults.size());
        assertEquals(tshirt.getId(), shirtResults.get(0).getId());
    }

    @Test
    void testFindByCategoryId() {
        // When
        List<Product> electronicsProducts = productRepository.findByCategoryId(electronicsCategory.getId());
        List<Product> clothingProducts = productRepository.findByCategoryId(clothingCategory.getId());
        
        // Then
        assertEquals(2, electronicsProducts.size());
        assertEquals(1, clothingProducts.size());
        
        assertTrue(electronicsProducts.stream()
                .anyMatch(p -> p.getId().equals(smartphone.getId())));
        assertTrue(electronicsProducts.stream()
                .anyMatch(p -> p.getId().equals(laptop.getId())));
        
        assertEquals(tshirt.getId(), clothingProducts.get(0).getId());
    }

    @Test
    void testFindByPriceLessThanEqual() {
        // When
        List<Product> cheapProducts = productRepository.findByPriceLessThanEqual(new BigDecimal("600.00"));
        List<Product> allProducts = productRepository.findByPriceLessThanEqual(new BigDecimal("2000.00"));
        
        // Then
        assertEquals(2, cheapProducts.size());
        assertEquals(3, allProducts.size());
        
        assertTrue(cheapProducts.stream()
                .anyMatch(p -> p.getId().equals(smartphone.getId())));
        assertTrue(cheapProducts.stream()
                .anyMatch(p -> p.getId().equals(tshirt.getId())));
    }

    @Test
    void testFindByStockQuantityGreaterThan() {
        // When
        List<Product> highStockProducts = productRepository.findByStockQuantityGreaterThan(100);
        List<Product> mediumStockProducts = productRepository.findByStockQuantityGreaterThan(40);
        
        // Then
        assertEquals(1, highStockProducts.size());
        assertEquals(tshirt.getId(), highStockProducts.get(0).getId());
        
        assertEquals(2, mediumStockProducts.size());
    }

    @Test
    void testFindByNameAndPriceRange() {
        // When
        List<Product> expensiveElectronics = productRepository.findByNameAndPriceRange(
                "laptop", new BigDecimal("1000.00"), new BigDecimal("2000.00"));
        
        List<Product> cheapElectronics = productRepository.findByNameAndPriceRange(
                "phone", new BigDecimal("0.00"), new BigDecimal("600.00"));
        
        // Then
        assertEquals(1, expensiveElectronics.size());
        assertEquals(laptop.getId(), expensiveElectronics.get(0).getId());
        
        assertEquals(1, cheapElectronics.size());
        assertEquals(smartphone.getId(), cheapElectronics.get(0).getId());
    }

    @Test
    void testFindLowStockProducts() {
        // When
        List<Product> lowStock = productRepository.findLowStockProducts(40);
        
        // Then
        assertEquals(1, lowStock.size());
        assertEquals(laptop.getId(), lowStock.get(0).getId());
    }
}
