package com.example.windsurfspringapp.controller;

import com.example.windsurfspringapp.model.Product;
import com.example.windsurfspringapp.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * REST controller for managing products.
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Get all products.
     *
     * @return List of all products
     */
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.findAllProducts());
    }

    /**
     * Get a product by ID.
     *
     * @param id Product ID
     * @return Product if found, 404 otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.findProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Create a new product.
     *
     * @param product Product to create
     * @return Created product with ID
     */
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product savedProduct = productService.saveProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    /**
     * Update an existing product.
     *
     * @param id Product ID
     * @param product Updated product data
     * @return Updated product if found, 404 otherwise
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return productService.findProductById(id)
                .map(existingProduct -> {
                    product.setId(id);
                    return ResponseEntity.ok(productService.saveProduct(product));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Delete a product.
     *
     * @param id Product ID
     * @return 204 No Content if deleted, 404 if not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (productService.deleteProduct(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Search products by name.
     *
     * @param name Name to search for
     * @return List of matching products
     */
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String name) {
        return ResponseEntity.ok(productService.searchProductsByName(name));
    }

    /**
     * Find products by category.
     *
     * @param categoryId Category ID
     * @return List of products in the category
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(productService.findProductsByCategory(categoryId));
    }

    /**
     * Find products by price range and optional name.
     *
     * @param name Product name (optional)
     * @param minPrice Minimum price
     * @param maxPrice Maximum price
     * @return List of matching products
     */
    @GetMapping("/filter")
    public ResponseEntity<List<Product>> filterProducts(
            @RequestParam(required = false) String name,
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        
        if (name != null && !name.trim().isEmpty()) {
            return ResponseEntity.ok(productService.findProductsByNameAndPriceRange(name, minPrice, maxPrice));
        } else {
            return ResponseEntity.ok(productService.findProductsByMaxPrice(maxPrice));
        }
    }

    /**
     * Update product stock quantity.
     *
     * @param id Product ID
     * @param quantity New stock quantity
     * @return Updated product if found, 404 otherwise
     */
    @PatchMapping("/{id}/stock")
    public ResponseEntity<Product> updateProductStock(
            @PathVariable Long id,
            @RequestParam Integer quantity) {
        
        return productService.updateProductStock(id, quantity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get products with low stock.
     *
     * @param threshold Stock threshold (default: 10)
     * @return List of products with stock below threshold
     */
    @GetMapping("/low-stock")
    public ResponseEntity<List<Product>> getLowStockProducts(
            @RequestParam(defaultValue = "10") Integer threshold) {
        
        return ResponseEntity.ok(productService.findLowStockProducts(threshold));
    }
}
