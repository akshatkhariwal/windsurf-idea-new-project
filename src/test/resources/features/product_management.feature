Feature: Product Management
  As a store manager
  I want to manage products
  So that I can keep track of inventory and sales

  Background:
    Given the following categories exist:
      | name        | description           |
      | Electronics | Electronic devices    |
      | Clothing    | Apparel and accessories |

  Scenario: Create a new product
    When I create a product with the following details:
      | name       | description        | price  | stockQuantity | category    |
      | Smartphone | Latest smartphone  | 599.99 | 50           | Electronics |
    Then the product should be created successfully
    And the product details should match:
      | name       | description        | price  | stockQuantity | category    |
      | Smartphone | Latest smartphone  | 599.99 | 50           | Electronics |

  Scenario: Update product stock
    Given a product exists with the following details:
      | name       | description        | price  | stockQuantity | category    |
      | Smartphone | Latest smartphone  | 599.99 | 50           | Electronics |
    When I update the stock quantity to 30
    Then the product stock should be updated to 30

  Scenario: Find products by category
    Given the following products exist:
      | name       | description        | price  | stockQuantity | category    |
      | Smartphone | Latest smartphone  | 599.99 | 50           | Electronics |
      | Laptop     | Powerful laptop    | 1299.99| 30           | Electronics |
      | T-Shirt    | Cotton t-shirt     | 19.99  | 100          | Clothing    |
    When I search for products in category "Electronics"
    Then I should find 2 products
    And the products should include "Smartphone" and "Laptop"

  Scenario: Find products by price range
    Given the following products exist:
      | name       | description        | price  | stockQuantity | category    |
      | Smartphone | Latest smartphone  | 599.99 | 50           | Electronics |
      | Laptop     | Powerful laptop    | 1299.99| 30           | Electronics |
      | T-Shirt    | Cotton t-shirt     | 19.99  | 100          | Clothing    |
    When I search for products with price between 500.00 and 1000.00
    Then I should find 1 product
    And the product should be "Smartphone"

  Scenario: Find low stock products
    Given the following products exist:
      | name       | description        | price  | stockQuantity | category    |
      | Smartphone | Latest smartphone  | 599.99 | 50           | Electronics |
      | Laptop     | Powerful laptop    | 1299.99| 10           | Electronics |
      | T-Shirt    | Cotton t-shirt     | 19.99  | 5            | Clothing    |
    When I search for products with stock below 20
    Then I should find 2 products
    And the products should include "Laptop" and "T-Shirt"
