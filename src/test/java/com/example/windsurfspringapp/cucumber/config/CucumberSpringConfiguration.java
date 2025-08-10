package com.example.windsurfspringapp.cucumber.config;

import com.example.windsurfspringapp.WindsurfSpringAppApplication;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Configuration class for Cucumber tests.
 * This class integrates Cucumber with Spring Boot test context.
 */
@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CucumberSpringConfiguration {
    // This class is intentionally empty
    // Its purpose is to enable Spring Boot test context for Cucumber tests
}
