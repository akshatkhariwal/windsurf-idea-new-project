package com.example.windsurfspringapp.service.impl;

import com.example.windsurfspringapp.model.Category;
import com.example.windsurfspringapp.repository.CategoryRepository;
import com.example.windsurfspringapp.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Implementation of the CategoryService interface.
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> findCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Optional<Category> findCategoryByName(String name) {
        return categoryRepository.findByNameIgnoreCase(name);
    }

    @Override
    @Transactional
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    @Transactional
    public boolean deleteCategory(Long id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<Category> searchCategoriesByName(String name) {
        return categoryRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public List<Category> findCategoriesWithProducts() {
        return categoryRepository.findCategoriesWithProducts();
    }

    @Override
    public Map<Long, Long> getCategoryProductCounts() {
        List<Object[]> results = categoryRepository.findCategoriesWithProductCount();
        Map<Long, Long> categoryCounts = new HashMap<>();
        
        for (Object[] result : results) {
            Category category = (Category) result[0];
            Long count = (Long) result[1];
            categoryCounts.put(category.getId(), count);
        }
        
        return categoryCounts;
    }

    @Override
    public boolean categoryExists(String name) {
        return categoryRepository.existsByNameIgnoreCase(name);
    }
}
