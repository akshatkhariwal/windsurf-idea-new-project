package com.example.windsurfspringapp.cucumber.steps;

import com.example.windsurfspringapp.model.Category;
import com.example.windsurfspringapp.model.Product;
import com.example.windsurfspringapp.repository.CategoryRepository;
import com.example.windsurfspringapp.repository.ProductRepository;
import com.example.windsurfspringapp.service.CategoryService;
import com.example.windsurfspringapp.service.ProductService;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ProductStepDefinitions {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Product currentProduct;
    private List<Product> foundProducts;
    private Exception lastException;

    @Before
    public void setup() {
        foundProducts = new ArrayList<>();
        lastException = null;
    }

    @After
    public void cleanup() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Given("the following categories exist:")
    public void theFollowingCategoriesExist(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps();
        for (Map<String, String> row : rows) {
            Category category = new Category(
                    row.get("name"),
                    row.get("description")
            );
            categoryService.saveCategory(category);
        }
    }

    @Given("a product exists with the following details:")
    public void aProductExistsWithTheFollowingDetails(DataTable dataTable) {
        createProductFromDataTable(dataTable);
    }

    @Given("the following products exist:")
    public void theFollowingProductsExist(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps();
        for (Map<String, String> row : rows) {
            String categoryName = row.get("category");
            Optional<Category> categoryOpt = categoryService.findCategoryByName(categoryName);
            
            Category category = categoryOpt.orElseGet(() -> {
                Category newCategory = new Category(categoryName, "Auto-created category");
                return categoryService.saveCategory(newCategory);
            });
            
            Product product = new Product(
                    row.get("name"),
                    row.get("description"),
                    new BigDecimal(row.get("price")),
                    Integer.parseInt(row.get("stockQuantity"))
            );
            product.setCategory(category);
            productService.saveProduct(product);
        }
    }

    @When("I create a product with the following details:")
    public void iCreateAProductWithTheFollowingDetails(DataTable dataTable) {
        createProductFromDataTable(dataTable);
    }

    @When("I update the stock quantity to {int}")
    public void iUpdateTheStockQuantityTo(Integer stockQuantity) {
        try {
            Optional<Product> updatedProduct = productService.updateProductStock(currentProduct.getId(), stockQuantity);
            updatedProduct.ifPresent(product -> currentProduct = product);
        } catch (Exception e) {
            lastException = e;
        }
    }

    @When("I search for products in category {string}")
    public void iSearchForProductsInCategory(String categoryName) {
        Optional<Category> categoryOpt = categoryService.findCategoryByName(categoryName);
        if (categoryOpt.isPresent()) {
            foundProducts = productService.findProductsByCategory(categoryOpt.get().getId());
        } else {
            foundProducts = new ArrayList<>();
        }
    }

    @When("I search for products with price between {double} and {double}")
    public void iSearchForProductsWithPriceBetween(Double minPrice, Double maxPrice) {
        foundProducts = productService.findProductsByNameAndPriceRange(
                "", 
                new BigDecimal(minPrice), 
                new BigDecimal(maxPrice)
        );
    }

    @When("I search for products with stock below {int}")
    public void iSearchForProductsWithStockBelow(Integer threshold) {
        foundProducts = productService.findLowStockProducts(threshold);
    }

    @Then("the product should be created successfully")
    public void theProductShouldBeCreatedSuccessfully() {
        assertNotNull(currentProduct);
        assertNotNull(currentProduct.getId());
    }

    @Then("the product details should match:")
    public void theProductDetailsShouldMatch(DataTable dataTable) {
        Map<String, String> expectedDetails = dataTable.asMaps().get(0);
        
        assertEquals(expectedDetails.get("name"), currentProduct.getName());
        assertEquals(expectedDetails.get("description"), currentProduct.getDescription());
        assertEquals(new BigDecimal(expectedDetails.get("price")), currentProduct.getPrice());
        assertEquals(Integer.parseInt(expectedDetails.get("stockQuantity")), currentProduct.getStockQuantity());
        assertEquals(expectedDetails.get("category"), currentProduct.getCategory().getName());
    }

    @Then("the product stock should be updated to {int}")
    public void theProductStockShouldBeUpdatedTo(Integer expectedStock) {
        assertEquals(expectedStock, currentProduct.getStockQuantity());
    }

    @Then("I should find {int} product(s)")
    public void iShouldFindProducts(Integer expectedCount) {
        assertEquals(expectedCount, foundProducts.size());
    }

    @Then("the product should be {string}")
    public void theProductShouldBe(String productName) {
        assertEquals(1, foundProducts.size());
        assertEquals(productName, foundProducts.get(0).getName());
    }

    @Then("the products should include {string} and {string}")
    public void theProductsShouldIncludeAnd(String product1, String product2) {
        List<String> productNames = foundProducts.stream().map(Product::getName).toList();
        assertTrue(productNames.contains(product1));
        assertTrue(productNames.contains(product2));
    }

    private void createProductFromDataTable(DataTable dataTable) {
        Map<String, String> productData = dataTable.asMaps().get(0);
        
        String categoryName = productData.get("category");
        Optional<Category> categoryOpt = categoryService.findCategoryByName(categoryName);
        
        Category category = categoryOpt.orElseGet(() -> {
            Category newCategory = new Category(categoryName, "Auto-created category");
            return categoryService.saveCategory(newCategory);
        });
        
        Product product = new Product(
                productData.get("name"),
                productData.get("description"),
                new BigDecimal(productData.get("price")),
                Integer.parseInt(productData.get("stockQuantity"))
        );
        product.setCategory(category);
        
        try {
            currentProduct = productService.saveProduct(product);
        } catch (Exception e) {
            lastException = e;
        }
    }
}
