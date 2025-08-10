package com.example.windsurfspringapp.repository;

import com.example.windsurfspringapp.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Category entity providing CRUD operations and custom queries.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Find category by name (case insensitive)
    Optional<Category> findByNameIgnoreCase(String name);
    
    // Find categories by name containing the given string (case insensitive)
    List<Category> findByNameContainingIgnoreCase(String name);
    
    // Check if a category with the given name exists
    boolean existsByNameIgnoreCase(String name);
    
    // Custom query to find categories with products
    @Query("SELECT DISTINCT c FROM Category c JOIN c.products p")
    List<Category> findCategoriesWithProducts();
    
    // Custom query to find categories with product count
    @Query("SELECT c, COUNT(p) FROM Category c LEFT JOIN c.products p GROUP BY c")
    List<Object[]> findCategoriesWithProductCount();
}
