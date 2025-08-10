# Windsurf Spring Boot Application

A Spring Boot web application created with Windsurf IDE.

## Features

- Spring Boot 3.5.4
- Spring Web (REST API)
- Spring Data JPA
- H2 In-Memory Database
- Maven build system
- Java 17
- Comprehensive domain model with JPA entities
- RESTful API endpoints for product and category management
- Service layer with business logic
- Sample data initialization for development
- Behavior-driven testing with Cucumber

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

### Running the Application

1. Clone the repository
2. Navigate to the project directory
3. Run the application:

```bash
mvn spring-boot:run
```

Or build and run the JAR:

```bash
mvn clean package
java -jar target/windsurf-spring-app-0.0.1-SNAPSHOT.jar
```

### Accessing the Application

- Main application: http://localhost:8080
- REST API endpoints:
  - Products: http://localhost:8080/api/products
  - Categories: http://localhost:8080/api/categories
- H2 Database Console: http://localhost:8080/h2-console

### H2 Database Configuration

- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: `password`

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/example/windsurfspringapp/
│   │       ├── WindsurfSpringAppApplication.java
│   │       ├── config/
│   │       │   └── DataInitializer.java
│   │       ├── controller/
│   │       │   ├── ProductController.java
│   │       │   └── CategoryController.java
│   │       ├── model/
│   │       │   ├── Product.java
│   │       │   └── Category.java
│   │       ├── repository/
│   │       │   ├── ProductRepository.java
│   │       │   └── CategoryRepository.java
│   │       └── service/
│   │           ├── ProductService.java
│   │           └── CategoryService.java
│   └── resources/
│       └── application.properties
└── test/
    ├── java/
    │   └── com/example/windsurfspringapp/
    │       ├── WindsurfSpringAppApplicationTests.java
    │       ├── controller/
    │       │   ├── ProductControllerTest.java
    │       │   └── CategoryControllerTest.java
    │       ├── cucumber/
    │       │   ├── CucumberTestRunner.java
    │       │   ├── config/
    │       │   │   └── CucumberSpringConfiguration.java
    │       │   └── steps/
    │       │       ├── ProductStepDefinitions.java
    │       │       └── CategoryStepDefinitions.java
    │       ├── repository/
    │       │   ├── ProductRepositoryTest.java
    │       │   └── CategoryRepositoryTest.java
    │       └── service/
    │           ├── ProductServiceTest.java
    │           └── CategoryServiceTest.java
    └── resources/
        └── features/
            ├── product_management.feature
            └── category_management.feature
```

## Domain Model

The application manages products and categories with the following relationships:
- A Category can have multiple Products
- Each Product belongs to one Category

### Product Entity
- id: Long (primary key)
- name: String
- description: String
- price: BigDecimal
- stockQuantity: Integer
- category: Category (many-to-one relationship)

### Category Entity
- id: Long (primary key)
- name: String (unique)
- description: String
- products: List<Product> (one-to-many relationship)

## REST API Endpoints

### Products
- GET /api/products - List all products
- GET /api/products/{id} - Get product by ID
- POST /api/products - Create a new product
- PUT /api/products/{id} - Update a product
- DELETE /api/products/{id} - Delete a product
- GET /api/products/search?name={name} - Search products by name
- GET /api/products/category/{categoryId} - Find products by category
- GET /api/products/price?maxPrice={maxPrice} - Find products by maximum price
- PATCH /api/products/{id}/stock?quantity={quantity} - Update product stock
- GET /api/products/low-stock?threshold={threshold} - Find low stock products

### Categories
- GET /api/categories - List all categories
- GET /api/categories/{id} - Get category by ID
- POST /api/categories - Create a new category
- PUT /api/categories/{id} - Update a category
- DELETE /api/categories/{id} - Delete a category
- GET /api/categories/search?name={name} - Search categories by name
- GET /api/categories/with-products - Find categories with products
- GET /api/categories/product-counts - Get product counts per category

## Sample Data

When running with the "dev" profile (default), the application initializes with sample data:
- Categories: Electronics, Clothing, Books
- Products: Several products in each category

## Testing

### Unit and Integration Tests

Run tests with:

```bash
mvn test
```

### Behavior-Driven Tests (Cucumber)

The application includes Cucumber tests for behavior-driven development:
- Feature files in `src/test/resources/features/`
- Step definitions in `src/test/java/com/example/windsurfspringapp/cucumber/steps/`

Run Cucumber tests with:

```bash
mvn test
```

## Building

Build the project with:

```bash
mvn clean package
```

## Deployment

The application can be deployed to any environment that supports Java applications:

### JAR Deployment
After building the application, deploy the JAR file:
```bash
java -jar target/windsurf-spring-app-0.0.1-SNAPSHOT.jar
```

### Environment Configuration
You can customize the application using environment variables or by providing an external `application.properties` file:
```bash
java -jar target/windsurf-spring-app-0.0.1-SNAPSHOT.jar --spring.config.location=file:/path/to/application.properties
```

### Profiles
The application supports different Spring profiles:
- `dev` (default): Includes sample data initialization
- `test`: Used for testing
- `prod`: For production deployment (disable H2 console, etc.)

To activate a specific profile:
```bash
java -jar target/windsurf-spring-app-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

## API Examples

### Creating a Product
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{
    "name": "New Smartphone",
    "description": "Latest model with advanced features",
    "price": 799.99,
    "stockQuantity": 25,
    "categoryId": 1
  }'
```

### Searching Products by Category
```bash
curl http://localhost:8080/api/products/category/1
```

### Updating Product Stock
```bash
curl -X PATCH "http://localhost:8080/api/products/1/stock?quantity=50"
```

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.
