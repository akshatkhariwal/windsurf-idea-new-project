# Windsurf Spring Boot Application

A Spring Boot web application created with Windsurf IDE.

## Features

- Spring Boot 3.5.4
- Spring Web (REST API)
- Spring Data JPA
- H2 In-Memory Database
- Maven build system
- Java 17

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
- Hello endpoint: http://localhost:8080/hello?name=YourName
- Health check: http://localhost:8080/health
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
│   │       └── controller/
│   │           └── HelloController.java
│   └── resources/
│       └── application.properties
└── test/
    └── java/
        └── com/example/windsurfspringapp/
            └── WindsurfSpringAppApplicationTests.java
```

## Testing

Run tests with:

```bash
mvn test
```

## Building

Build the project with:

```bash
mvn clean package
