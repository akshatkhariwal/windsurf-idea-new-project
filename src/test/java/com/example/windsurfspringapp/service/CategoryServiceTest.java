package com.example.windsurfspringapp.service;

import com.example.windsurfspringapp.model.Category;
import com.example.windsurfspringapp.repository.CategoryRepository;
import com.example.windsurfspringapp.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category1;
    private Category category2;
    private List<Object[]> categoryProductCounts;

    @BeforeEach
    void setUp() {
        category1 = new Category("Electronics", "Electronic devices");
        category1.setId(1L);

        category2 = new Category("Clothing", "Apparel and accessories");
        category2.setId(2L);

        // Create mock data for category product counts
        categoryProductCounts = new ArrayList<>();
        categoryProductCounts.add(new Object[]{category1, 5L});
        categoryProductCounts.add(new Object[]{category2, 3L});
    }

    @Test
    void testFindAllCategories() {
        // Given
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category1, category2));

        // When
        List<Category> categories = categoryService.findAllCategories();

        // Then
        assertEquals(2, categories.size());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void testFindCategoryById() {
        // Given
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category1));
        when(categoryRepository.findById(3L)).thenReturn(Optional.empty());

        // When
        Optional<Category> foundCategory = categoryService.findCategoryById(1L);
        Optional<Category> notFoundCategory = categoryService.findCategoryById(3L);

        // Then
        assertTrue(foundCategory.isPresent());
        assertEquals(category1.getId(), foundCategory.get().getId());
        assertFalse(notFoundCategory.isPresent());
        
        verify(categoryRepository, times(1)).findById(1L);
        verify(categoryRepository, times(1)).findById(3L);
    }

    @Test
    void testFindCategoryByName() {
        // Given
        when(categoryRepository.findByNameIgnoreCase("Electronics")).thenReturn(Optional.of(category1));
        when(categoryRepository.findByNameIgnoreCase("Furniture")).thenReturn(Optional.empty());

        // When
        Optional<Category> foundCategory = categoryService.findCategoryByName("Electronics");
        Optional<Category> notFoundCategory = categoryService.findCategoryByName("Furniture");

        // Then
        assertTrue(foundCategory.isPresent());
        assertEquals(category1.getId(), foundCategory.get().getId());
        assertFalse(notFoundCategory.isPresent());
        
        verify(categoryRepository, times(1)).findByNameIgnoreCase("Electronics");
        verify(categoryRepository, times(1)).findByNameIgnoreCase("Furniture");
    }

    @Test
    void testSaveCategory() {
        // Given
        Category newCategory = new Category("Books", "Books and publications");
        when(categoryRepository.save(any(Category.class))).thenReturn(newCategory);

        // When
        Category savedCategory = categoryService.saveCategory(newCategory);

        // Then
        assertNotNull(savedCategory);
        assertEquals("Books", savedCategory.getName());
        verify(categoryRepository, times(1)).save(newCategory);
    }

    @Test
    void testDeleteCategory() {
        // Given
        when(categoryRepository.existsById(1L)).thenReturn(true);
        when(categoryRepository.existsById(3L)).thenReturn(false);
        doNothing().when(categoryRepository).deleteById(anyLong());

        // When
        boolean deleted1 = categoryService.deleteCategory(1L);
        boolean deleted2 = categoryService.deleteCategory(3L);

        // Then
        assertTrue(deleted1);
        assertFalse(deleted2);
        
        verify(categoryRepository, times(1)).deleteById(1L);
        verify(categoryRepository, never()).deleteById(3L);
    }

    @Test
    void testSearchCategoriesByName() {
        // Given
        when(categoryRepository.findByNameContainingIgnoreCase("elect")).thenReturn(List.of(category1));

        // When
        List<Category> categories = categoryService.searchCategoriesByName("elect");

        // Then
        assertEquals(1, categories.size());
        assertEquals(category1.getId(), categories.get(0).getId());
        verify(categoryRepository, times(1)).findByNameContainingIgnoreCase("elect");
    }

    @Test
    void testFindCategoriesWithProducts() {
        // Given
        when(categoryRepository.findCategoriesWithProducts()).thenReturn(Arrays.asList(category1, category2));

        // When
        List<Category> categories = categoryService.findCategoriesWithProducts();

        // Then
        assertEquals(2, categories.size());
        verify(categoryRepository, times(1)).findCategoriesWithProducts();
    }

    @Test
    void testGetCategoryProductCounts() {
        // Given
        when(categoryRepository.findCategoriesWithProductCount()).thenReturn(categoryProductCounts);

        // When
        Map<Long, Long> countMap = categoryService.getCategoryProductCounts();

        // Then
        assertEquals(2, countMap.size());
        assertEquals(5L, countMap.get(1L));
        assertEquals(3L, countMap.get(2L));
        verify(categoryRepository, times(1)).findCategoriesWithProductCount();
    }

    @Test
    void testCategoryExists() {
        // Given
        when(categoryRepository.existsByNameIgnoreCase("Electronics")).thenReturn(true);
        when(categoryRepository.existsByNameIgnoreCase("Furniture")).thenReturn(false);

        // When
        boolean exists1 = categoryService.categoryExists("Electronics");
        boolean exists2 = categoryService.categoryExists("Furniture");

        // Then
        assertTrue(exists1);
        assertFalse(exists2);
        
        verify(categoryRepository, times(1)).existsByNameIgnoreCase("Electronics");
        verify(categoryRepository, times(1)).existsByNameIgnoreCase("Furniture");
    }
}
