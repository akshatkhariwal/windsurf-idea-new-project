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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category electronicsCategory;
    private Category clothingCategory;
    private Category emptyCategory;

    @BeforeEach
    void setUp() {
        // Create and persist categories
        electronicsCategory = new Category("Electronics", "Electronic devices and gadgets");
        clothingCategory = new Category("Clothing", "Apparel and accessories");
        emptyCategory = new Category("Books", "Books and publications");
        
        entityManager.persist(electronicsCategory);
        entityManager.persist(clothingCategory);
        entityManager.persist(emptyCategory);
        
        // Create and persist products
        Product smartphone = new Product("Smartphone", "Latest model smartphone", new BigDecimal("599.99"), 50);
        smartphone.setCategory(electronicsCategory);
        
        Product laptop = new Product("Laptop", "High-performance laptop", new BigDecimal("1299.99"), 30);
        laptop.setCategory(electronicsCategory);
        
        Product tshirt = new Product("T-Shirt", "Cotton t-shirt", new BigDecimal("19.99"), 200);
        tshirt.setCategory(clothingCategory);
        
        entityManager.persist(smartphone);
        entityManager.persist(laptop);
        entityManager.persist(tshirt);
        
        // Flush to ensure data is in the database
        entityManager.flush();
    }

    @Test
    void testFindByNameIgnoreCase() {
        // When
        Optional<Category> electronicsCategoryResult = categoryRepository.findByNameIgnoreCase("electronics");
        Optional<Category> clothingCategoryResult = categoryRepository.findByNameIgnoreCase("CLOTHING");
        Optional<Category> nonExistentCategory = categoryRepository.findByNameIgnoreCase("Furniture");
        
        // Then
        assertTrue(electronicsCategoryResult.isPresent());
        assertEquals(electronicsCategory.getId(), electronicsCategoryResult.get().getId());
        
        assertTrue(clothingCategoryResult.isPresent());
        assertEquals(clothingCategory.getId(), clothingCategoryResult.get().getId());
        
        assertFalse(nonExistentCategory.isPresent());
    }

    @Test
    void testFindByNameContainingIgnoreCase() {
        // When
        List<Category> electronicsResults = categoryRepository.findByNameContainingIgnoreCase("elect");
        List<Category> clothingResults = categoryRepository.findByNameContainingIgnoreCase("CLOTH");
        List<Category> nonExistentResults = categoryRepository.findByNameContainingIgnoreCase("furniture");
        
        // Then
        assertEquals(1, electronicsResults.size());
        assertEquals(electronicsCategory.getId(), electronicsResults.get(0).getId());
        
        assertEquals(1, clothingResults.size());
        assertEquals(clothingCategory.getId(), clothingResults.get(0).getId());
        
        assertTrue(nonExistentResults.isEmpty());
    }

    @Test
    void testExistsByNameIgnoreCase() {
        // When
        boolean electronicsExists = categoryRepository.existsByNameIgnoreCase("electronics");
        boolean clothingExists = categoryRepository.existsByNameIgnoreCase("CLOTHING");
        boolean booksExists = categoryRepository.existsByNameIgnoreCase("Books");
        boolean furnitureExists = categoryRepository.existsByNameIgnoreCase("Furniture");
        
        // Then
        assertTrue(electronicsExists);
        assertTrue(clothingExists);
        assertTrue(booksExists);
        assertFalse(furnitureExists);
    }

    @Test
    void testFindCategoriesWithProducts() {
        // When
        List<Category> categoriesWithProducts = categoryRepository.findCategoriesWithProducts();
        
        // Then
        assertEquals(2, categoriesWithProducts.size());
        
        // Check that the empty category is not included
        boolean containsEmptyCategory = categoriesWithProducts.stream()
                .anyMatch(c -> c.getId().equals(emptyCategory.getId()));
        assertFalse(containsEmptyCategory);
        
        // Check that electronics and clothing categories are included
        boolean containsElectronics = categoriesWithProducts.stream()
                .anyMatch(c -> c.getId().equals(electronicsCategory.getId()));
        boolean containsClothing = categoriesWithProducts.stream()
                .anyMatch(c -> c.getId().equals(clothingCategory.getId()));
        
        assertTrue(containsElectronics);
        assertTrue(containsClothing);
    }

    @Test
    void testFindCategoriesWithProductCount() {
        // When
        List<Object[]> categoriesWithCount = categoryRepository.findCategoriesWithProductCount();
        
        // Then
        assertEquals(3, categoriesWithCount.size());
        
        // Find electronics category result
        Object[] electronicsResult = categoriesWithCount.stream()
                .filter(result -> ((Category) result[0]).getId().equals(electronicsCategory.getId()))
                .findFirst()
                .orElse(null);
        
        // Find clothing category result
        Object[] clothingResult = categoriesWithCount.stream()
                .filter(result -> ((Category) result[0]).getId().equals(clothingCategory.getId()))
                .findFirst()
                .orElse(null);
        
        // Find empty category result
        Object[] emptyResult = categoriesWithCount.stream()
                .filter(result -> ((Category) result[0]).getId().equals(emptyCategory.getId()))
                .findFirst()
                .orElse(null);
        
        assertNotNull(electronicsResult);
        assertNotNull(clothingResult);
        assertNotNull(emptyResult);
        
        assertEquals(2L, electronicsResult[1]);
        assertEquals(1L, clothingResult[1]);
        assertEquals(0L, emptyResult[1]);
    }
}
