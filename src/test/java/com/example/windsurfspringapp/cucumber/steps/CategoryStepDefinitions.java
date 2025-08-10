package com.example.windsurfspringapp.cucumber.steps;

import com.example.windsurfspringapp.model.Category;
import com.example.windsurfspringapp.repository.CategoryRepository;
import com.example.windsurfspringapp.service.CategoryService;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryStepDefinitions {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category currentCategory;
    private List<Category> foundCategories;
    private Exception lastException;

    @Given("a category exists with the following details:")
    public void aCategoryExistsWithTheFollowingDetails(DataTable dataTable) {
        createCategoryFromDataTable(dataTable);
    }

    @When("I create a category with the following details:")
    public void iCreateACategoryWithTheFollowingDetails(DataTable dataTable) {
        createCategoryFromDataTable(dataTable);
    }

    @When("I try to create a category with the name {string}")
    public void iTryToCreateACategoryWithTheName(String categoryName) {
        try {
            Category category = new Category(categoryName, "Test description");
            currentCategory = categoryService.saveCategory(category);
        } catch (Exception e) {
            lastException = e;
        }
    }

    @When("I update the category description to {string}")
    public void iUpdateTheCategoryDescriptionTo(String newDescription) {
        try {
            currentCategory.setDescription(newDescription);
            currentCategory = categoryService.saveCategory(currentCategory);
        } catch (Exception e) {
            lastException = e;
        }
    }

    @When("I delete the category")
    public void iDeleteTheCategory() {
        try {
            categoryService.deleteCategory(currentCategory.getId());
        } catch (Exception e) {
            lastException = e;
        }
    }

    @When("I search for categories with products")
    public void iSearchForCategoriesWithProducts() {
        foundCategories = categoryService.findCategoriesWithProducts();
    }

    @Then("the category should be created successfully")
    public void theCategoryShouldBeCreatedSuccessfully() {
        assertNotNull(currentCategory);
        assertNotNull(currentCategory.getId());
    }

    @Then("the category details should match:")
    public void theCategoryDetailsShouldMatch(DataTable dataTable) {
        Map<String, String> expectedDetails = dataTable.asMaps().get(0);
        
        assertEquals(expectedDetails.get("name"), currentCategory.getName());
        assertEquals(expectedDetails.get("description"), currentCategory.getDescription());
    }

    @Then("the category creation should fail")
    public void theCategoryCreationShouldFail() {
        assertNotNull(lastException);
    }

    @Then("I should receive a duplicate category error")
    public void iShouldReceiveADuplicateCategoryError() {
        assertTrue(lastException instanceof DataIntegrityViolationException);
    }

    @Then("the category should be updated successfully")
    public void theCategoryShouldBeUpdatedSuccessfully() {
        assertNotNull(currentCategory);
    }

    @Then("the category description should be {string}")
    public void theCategoryDescriptionShouldBe(String expectedDescription) {
        assertEquals(expectedDescription, currentCategory.getDescription());
    }

    @Then("the category should be deleted successfully")
    public void theCategoryShouldBeDeletedSuccessfully() {
        assertNull(lastException);
    }

    @Then("the category should no longer exist")
    public void theCategoryShouldNoLongerExist() {
        Optional<Category> category = categoryService.findCategoryById(currentCategory.getId());
        assertFalse(category.isPresent());
    }

    @Then("I should find {int} categories")
    public void iShouldFindCategories(Integer expectedCount) {
        assertEquals(expectedCount, foundCategories.size());
    }

    @Then("the categories should include {string} and {string}")
    public void theCategoriesShouldIncludeAnd(String category1, String category2) {
        List<String> categoryNames = foundCategories.stream().map(Category::getName).toList();
        assertTrue(categoryNames.contains(category1));
        assertTrue(categoryNames.contains(category2));
    }

    @Then("the categories should not include {string}")
    public void theCategoriesShouldNotInclude(String categoryName) {
        List<String> categoryNames = foundCategories.stream().map(Category::getName).toList();
        assertFalse(categoryNames.contains(categoryName));
    }

    private void createCategoryFromDataTable(DataTable dataTable) {
        Map<String, String> categoryData = dataTable.asMaps().get(0);
        
        Category category = new Category(
                categoryData.get("name"),
                categoryData.get("description")
        );
        
        try {
            currentCategory = categoryService.saveCategory(category);
        } catch (Exception e) {
            lastException = e;
        }
    }
}
