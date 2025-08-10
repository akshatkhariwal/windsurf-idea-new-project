package com.example.windsurfspringapp.controller;

import com.example.windsurfspringapp.model.Category;
import com.example.windsurfspringapp.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller for managing product categories.
 */
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Get all categories.
     *
     * @return List of all categories
     */
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.findAllCategories());
    }

    /**
     * Get a category by ID.
     *
     * @param id Category ID
     * @return Category if found, 404 otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        return categoryService.findCategoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Create a new category.
     *
     * @param category Category to create
     * @return Created category with ID
     */
    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody Category category) {
        // Check if category with same name already exists
        if (categoryService.categoryExists(category.getName())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Category with name '" + category.getName() + "' already exists"));
        }
        
        Category savedCategory = categoryService.saveCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
    }

    /**
     * Update an existing category.
     *
     * @param id Category ID
     * @param category Updated category data
     * @return Updated category if found, 404 otherwise
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id, @RequestBody Category category) {
        return categoryService.findCategoryById(id)
                .map(existingCategory -> {
                    // Check if name is being changed and if new name already exists
                    if (!existingCategory.getName().equalsIgnoreCase(category.getName()) && 
                        categoryService.categoryExists(category.getName())) {
                        return ResponseEntity
                                .status(HttpStatus.CONFLICT)
                                .body(Map.of("error", "Category with name '" + category.getName() + "' already exists"));
                    }
                    
                    category.setId(id);
                    return ResponseEntity.ok(categoryService.saveCategory(category));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Delete a category.
     *
     * @param id Category ID
     * @return 204 No Content if deleted, 404 if not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        if (categoryService.deleteCategory(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Search categories by name.
     *
     * @param name Name to search for
     * @return List of matching categories
     */
    @GetMapping("/search")
    public ResponseEntity<List<Category>> searchCategories(@RequestParam String name) {
        return ResponseEntity.ok(categoryService.searchCategoriesByName(name));
    }

    /**
     * Get categories that have products.
     *
     * @return List of categories with products
     */
    @GetMapping("/with-products")
    public ResponseEntity<List<Category>> getCategoriesWithProducts() {
        return ResponseEntity.ok(categoryService.findCategoriesWithProducts());
    }

    /**
     * Get product counts for each category.
     *
     * @return Map of category ID to product count
     */
    @GetMapping("/product-counts")
    public ResponseEntity<Map<Long, Long>> getCategoryProductCounts() {
        return ResponseEntity.ok(categoryService.getCategoryProductCounts());
    }
}
