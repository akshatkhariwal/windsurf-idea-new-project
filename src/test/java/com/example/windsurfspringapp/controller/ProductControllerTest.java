package com.example.windsurfspringapp.controller;

import com.example.windsurfspringapp.model.Category;
import com.example.windsurfspringapp.model.Product;
import com.example.windsurfspringapp.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

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
    void testGetAllProducts() throws Exception {
        when(productService.findAllProducts()).thenReturn(Arrays.asList(product1, product2));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Smartphone")))
                .andExpect(jsonPath("$[1].name", is("Laptop")));
    }

    @Test
    void testGetProductById() throws Exception {
        when(productService.findProductById(1L)).thenReturn(Optional.of(product1));
        when(productService.findProductById(3L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Smartphone")))
                .andExpect(jsonPath("$.price", is(599.99)));

        mockMvc.perform(get("/api/products/3"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateProduct() throws Exception {
        Map<String, Object> productRequest = new HashMap<>();
        productRequest.put("name", "Tablet");
        productRequest.put("description", "New tablet");
        productRequest.put("price", 399.99);
        productRequest.put("stockQuantity", 20);
        productRequest.put("categoryId", 1L);

        Product newProduct = new Product("Tablet", "New tablet", new BigDecimal("399.99"), 20);
        newProduct.setId(3L);
        newProduct.setCategory(category);

        when(productService.saveProduct(any(Product.class))).thenReturn(newProduct);

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Tablet")))
                .andExpect(jsonPath("$.price", is(399.99)));
    }

    @Test
    void testUpdateProduct() throws Exception {
        Map<String, Object> productRequest = new HashMap<>();
        productRequest.put("name", "Updated Smartphone");
        productRequest.put("description", "Updated model");
        productRequest.put("price", 649.99);
        productRequest.put("stockQuantity", 45);
        productRequest.put("categoryId", 1L);

        Product updatedProduct = new Product("Updated Smartphone", "Updated model", new BigDecimal("649.99"), 45);
        updatedProduct.setId(1L);
        updatedProduct.setCategory(category);

        when(productService.findProductById(1L)).thenReturn(Optional.of(product1));
        when(productService.saveProduct(any(Product.class))).thenReturn(updatedProduct);

        mockMvc.perform(put("/api/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Smartphone")))
                .andExpect(jsonPath("$.price", is(649.99)));
    }

    @Test
    void testDeleteProduct() throws Exception {
        when(productService.deleteProduct(1L)).thenReturn(true);
        when(productService.deleteProduct(3L)).thenReturn(false);

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());

        mockMvc.perform(delete("/api/products/3"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSearchProductsByName() throws Exception {
        when(productService.searchProductsByName("phone")).thenReturn(Arrays.asList(product1));

        mockMvc.perform(get("/api/products/search")
                .param("name", "phone"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Smartphone")));
    }

    @Test
    void testFindProductsByCategory() throws Exception {
        when(productService.findProductsByCategory(1L)).thenReturn(Arrays.asList(product1, product2));

        mockMvc.perform(get("/api/products/category/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Smartphone")))
                .andExpect(jsonPath("$[1].name", is("Laptop")));
    }

    @Test
    void testFindProductsByMaxPrice() throws Exception {
        when(productService.findProductsByMaxPrice(new BigDecimal("1000.00")))
                .thenReturn(Arrays.asList(product1));

        mockMvc.perform(get("/api/products/filter")
                .param("minPrice", "0.00")
                .param("maxPrice", "1000.00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Smartphone")));
    }

    @Test
    void testUpdateProductStock() throws Exception {
        Product updatedProduct = new Product("Smartphone", "Latest model", new BigDecimal("599.99"), 40);
        updatedProduct.setId(1L);
        updatedProduct.setCategory(category);

        when(productService.updateProductStock(1L, 40)).thenReturn(Optional.of(updatedProduct));
        when(productService.updateProductStock(3L, 40)).thenReturn(Optional.empty());

        mockMvc.perform(patch("/api/products/1/stock")
                .param("quantity", "40"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stockQuantity", is(40)));

        mockMvc.perform(patch("/api/products/3/stock")
                .param("quantity", "40"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFindLowStockProducts() throws Exception {
        when(productService.findLowStockProducts(40)).thenReturn(Arrays.asList(product2));

        mockMvc.perform(get("/api/products/low-stock")
                .param("threshold", "40"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Laptop")));
    }
}
