package com.example.windsurfspringapp.service;

import com.example.windsurfspringapp.model.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing products.
 */
public interface ProductService {

    /**
     * Find all products in the system.
     *
     * @return List of all products
     */
    List<Product> findAllProducts();

    /**
     * Find a product by its ID.
     *
     * @param id The product ID
     * @return Optional containing the product if found, empty otherwise
     */
    Optional<Product> findProductById(Long id);

    /**
     * Save a new product or update an existing one.
     *
     * @param product The product to save
     * @return The saved product with updated ID and timestamps
     */
    Product saveProduct(Product product);

    /**
     * Delete a product by its ID.
     *
     * @param id The product ID to delete
     * @return true if the product was deleted, false if it didn't exist
     */
    boolean deleteProduct(Long id);

    /**
     * Search for products by name (case insensitive, partial match).
     *
     * @param name The name to search for
     * @return List of matching products
     */
    List<Product> searchProductsByName(String name);

    /**
     * Find products by category ID.
     *
     * @param categoryId The category ID
     * @return List of products in the category
     */
    List<Product> findProductsByCategory(Long categoryId);

    /**
     * Find products with price less than or equal to the specified maximum.
     *
     * @param maxPrice The maximum price
     * @return List of products with price <= maxPrice
     */
    List<Product> findProductsByMaxPrice(BigDecimal maxPrice);

    /**
     * Find products by name and price range.
     *
     * @param name The product name (partial match)
     * @param minPrice The minimum price
     * @param maxPrice The maximum price
     * @return List of matching products
     */
    List<Product> findProductsByNameAndPriceRange(String name, BigDecimal minPrice, BigDecimal maxPrice);

    /**
     * Find products that are low in stock (below the specified threshold).
     *
     * @param threshold The stock threshold
     * @return List of products with stock quantity below threshold
     */
    List<Product> findLowStockProducts(Integer threshold);

    /**
     * Update the stock quantity of a product.
     *
     * @param productId The product ID
     * @param quantity The new stock quantity
     * @return The updated product, or empty if the product doesn't exist
     */
    Optional<Product> updateProductStock(Long productId, Integer quantity);
}
