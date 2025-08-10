package com.example.windsurfspringapp.config;

import com.example.windsurfspringapp.model.Category;
import com.example.windsurfspringapp.model.Product;
import com.example.windsurfspringapp.repository.CategoryRepository;
import com.example.windsurfspringapp.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * Configuration class to initialize sample data for testing and development.
 * This will only run in "dev" profile to avoid populating production databases.
 */
@Configuration
@Profile("dev")
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(CategoryRepository categoryRepository, ProductRepository productRepository) {
        return args -> {
            // Create categories
            Category electronics = new Category("Electronics", "Electronic devices and gadgets");
            Category clothing = new Category("Clothing", "Apparel and accessories");
            Category books = new Category("Books", "Books, e-books, and publications");
            Category homeAndGarden = new Category("Home & Garden", "Home decor and garden supplies");
            Category sports = new Category("Sports & Outdoors", "Sports equipment and outdoor gear");

            // Save categories
            List<Category> categories = Arrays.asList(electronics, clothing, books, homeAndGarden, sports);
            categoryRepository.saveAll(categories);

            // Create products for Electronics
            Product smartphone = new Product("Smartphone", "Latest model with high-resolution camera", new BigDecimal("699.99"), 50);
            smartphone.setCategory(electronics);

            Product laptop = new Product("Laptop", "Powerful laptop for work and gaming", new BigDecimal("1299.99"), 30);
            laptop.setCategory(electronics);

            Product tablet = new Product("Tablet", "Lightweight tablet with long battery life", new BigDecimal("399.99"), 45);
            tablet.setCategory(electronics);

            // Create products for Clothing
            Product tShirt = new Product("T-Shirt", "Comfortable cotton t-shirt", new BigDecimal("19.99"), 100);
            tShirt.setCategory(clothing);

            Product jeans = new Product("Jeans", "Classic blue jeans", new BigDecimal("49.99"), 75);
            jeans.setCategory(clothing);

            // Create products for Books
            Product novel = new Product("Novel", "Bestselling fiction novel", new BigDecimal("14.99"), 60);
            novel.setCategory(books);

            Product cookbook = new Product("Cookbook", "Collection of gourmet recipes", new BigDecimal("24.99"), 40);
            cookbook.setCategory(books);

            // Create products for Home & Garden
            Product lamp = new Product("Lamp", "Modern design table lamp", new BigDecimal("39.99"), 25);
            lamp.setCategory(homeAndGarden);

            Product plant = new Product("Plant", "Indoor plant in decorative pot", new BigDecimal("29.99"), 35);
            plant.setCategory(homeAndGarden);

            // Create products for Sports & Outdoors
            Product tennisRacket = new Product("Tennis Racket", "Professional tennis racket", new BigDecimal("89.99"), 20);
            tennisRacket.setCategory(sports);

            Product basketball = new Product("Basketball", "Official size basketball", new BigDecimal("24.99"), 30);
            basketball.setCategory(sports);

            // Save all products
            List<Product> products = Arrays.asList(smartphone, laptop, tablet, tShirt, jeans, novel, cookbook, lamp, plant, tennisRacket, basketball);
            productRepository.saveAll(products);

            System.out.println("Sample data initialized with " + categoryRepository.count() + " categories and " + productRepository.count() + " products");
        };
    }
}
