package com.example.windsurfspringapp.controller;

import com.example.windsurfspringapp.model.Category;
import com.example.windsurfspringapp.service.CategoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    private Category category1;
    private Category category2;

    @BeforeEach
    void setUp() {
        category1 = new Category("Electronics", "Electronic devices");
        category1.setId(1L);

        category2 = new Category("Clothing", "Apparel and accessories");
        category2.setId(2L);
    }

    @Test
    void testGetAllCategories() throws Exception {
        when(categoryService.findAllCategories()).thenReturn(Arrays.asList(category1, category2));

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Electronics")))
                .andExpect(jsonPath("$[1].name", is("Clothing")));
    }

    @Test
    void testGetCategoryById() throws Exception {
        when(categoryService.findCategoryById(1L)).thenReturn(Optional.of(category1));
        when(categoryService.findCategoryById(3L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Electronics")))
                .andExpect(jsonPath("$.description", is("Electronic devices")));

        mockMvc.perform(get("/api/categories/3"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateCategory() throws Exception {
        Map<String, String> categoryRequest = new HashMap<>();
        categoryRequest.put("name", "Books");
        categoryRequest.put("description", "Books and publications");

        Category newCategory = new Category("Books", "Books and publications");
        newCategory.setId(3L);

        when(categoryService.categoryExists("Books")).thenReturn(false);
        when(categoryService.saveCategory(any(Category.class))).thenReturn(newCategory);

        mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Books")))
                .andExpect(jsonPath("$.description", is("Books and publications")));
    }

    @Test
    void testCreateDuplicateCategory() throws Exception {
        Map<String, String> categoryRequest = new HashMap<>();
        categoryRequest.put("name", "Electronics");
        categoryRequest.put("description", "Electronic devices");

        when(categoryService.categoryExists("Electronics")).thenReturn(true);

        mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryRequest)))
                .andExpect(status().isConflict());
    }

    @Test
    void testUpdateCategory() throws Exception {
        Map<String, String> categoryRequest = new HashMap<>();
        categoryRequest.put("name", "Electronics");
        categoryRequest.put("description", "Updated electronic devices");

        Category updatedCategory = new Category("Electronics", "Updated electronic devices");
        updatedCategory.setId(1L);

        when(categoryService.findCategoryById(1L)).thenReturn(Optional.of(category1));
        when(categoryService.saveCategory(any(Category.class))).thenReturn(updatedCategory);

        mockMvc.perform(put("/api/categories/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Electronics")))
                .andExpect(jsonPath("$.description", is("Updated electronic devices")));
    }

    @Test
    void testDeleteCategory() throws Exception {
        when(categoryService.deleteCategory(1L)).thenReturn(true);
        when(categoryService.deleteCategory(3L)).thenReturn(false);

        mockMvc.perform(delete("/api/categories/1"))
                .andExpect(status().isNoContent());

        mockMvc.perform(delete("/api/categories/3"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSearchCategoriesByName() throws Exception {
        when(categoryService.searchCategoriesByName("elect")).thenReturn(Arrays.asList(category1));

        mockMvc.perform(get("/api/categories/search")
                .param("name", "elect"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Electronics")));
    }

    @Test
    void testFindCategoriesWithProducts() throws Exception {
        when(categoryService.findCategoriesWithProducts()).thenReturn(Arrays.asList(category1, category2));

        mockMvc.perform(get("/api/categories/with-products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Electronics")))
                .andExpect(jsonPath("$[1].name", is("Clothing")));
    }

    @Test
    void testGetCategoryProductCounts() throws Exception {
        Map<Long, Long> productCounts = new HashMap<>();
        productCounts.put(1L, 5L);
        productCounts.put(2L, 3L);

        when(categoryService.getCategoryProductCounts()).thenReturn(productCounts);

        mockMvc.perform(get("/api/categories/product-counts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.1", is(5)))
                .andExpect(jsonPath("$.2", is(3)));
    }
}
