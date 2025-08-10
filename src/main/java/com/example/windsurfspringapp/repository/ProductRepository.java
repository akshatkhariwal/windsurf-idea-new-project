package com.example.windsurfspringapp.repository;

import com.example.windsurfspringapp.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repository interface for Product entity providing CRUD operations and custom queries.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Find products by name containing the given string (case insensitive)
    List<Product> findByNameContainingIgnoreCase(String name);
    
    // Find products by category id
    List<Product> findByCategoryId(Long categoryId);
    
    // Find products with price less than or equal to the given value
    List<Product> findByPriceLessThanEqual(BigDecimal maxPrice);
    
    // Find products with stock quantity greater than the given value
    List<Product> findByStockQuantityGreaterThan(Integer minStockQuantity);
    
    // Custom query to find products by name and with price in range
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
           "AND p.price BETWEEN :minPrice AND :maxPrice")
    List<Product> findByNameAndPriceRange(
            @Param("name") String name,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice);
    
    // Custom query to find products that are low in stock (below threshold)
    @Query("SELECT p FROM Product p WHERE p.stockQuantity < :threshold")
    List<Product> findLowStockProducts(@Param("threshold") Integer threshold);
}
