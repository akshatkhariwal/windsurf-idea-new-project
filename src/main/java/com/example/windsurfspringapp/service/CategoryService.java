package com.example.windsurfspringapp.service;

import com.example.windsurfspringapp.model.Category;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service interface for managing product categories.
 */
public interface CategoryService {

    /**
     * Find all categories in the system.
     *
     * @return List of all categories
     */
    List<Category> findAllCategories();

    /**
     * Find a category by its ID.
     *
     * @param id The category ID
     * @return Optional containing the category if found, empty otherwise
     */
    Optional<Category> findCategoryById(Long id);

    /**
     * Find a category by its name (case insensitive).
     *
     * @param name The category name
     * @return Optional containing the category if found, empty otherwise
     */
    Optional<Category> findCategoryByName(String name);

    /**
     * Save a new category or update an existing one.
     *
     * @param category The category to save
     * @return The saved category with updated ID and timestamps
     */
    Category saveCategory(Category category);

    /**
     * Delete a category by its ID.
     * Note: This will fail if there are products associated with the category.
     *
     * @param id The category ID to delete
     * @return true if the category was deleted, false if it didn't exist
     */
    boolean deleteCategory(Long id);

    /**
     * Search for categories by name (case insensitive, partial match).
     *
     * @param name The name to search for
     * @return List of matching categories
     */
    List<Category> searchCategoriesByName(String name);

    /**
     * Find all categories that have at least one product.
     *
     * @return List of categories with products
     */
    List<Category> findCategoriesWithProducts();

    /**
     * Get a map of category IDs to their product counts.
     *
     * @return Map of category ID to product count
     */
    Map<Long, Long> getCategoryProductCounts();

    /**
     * Check if a category with the given name already exists.
     *
     * @param name The category name
     * @return true if a category with the name exists, false otherwise
     */
    boolean categoryExists(String name);
}
