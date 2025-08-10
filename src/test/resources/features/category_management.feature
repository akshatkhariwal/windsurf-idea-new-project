Feature: Category Management
  As a store manager
  I want to manage product categories
  So that I can organize products effectively

  Scenario: Create a new category
    When I create a category with the following details:
      | name        | description           |
      | Electronics | Electronic devices    |
    Then the category should be created successfully
    And the category details should match:
      | name        | description           |
      | Electronics | Electronic devices    |

  Scenario: Attempt to create a duplicate category
    Given a category exists with the following details:
      | name        | description           |
      | Electronics | Electronic devices    |
    When I try to create a category with the name "Electronics"
    Then the category creation should fail
    And I should receive a duplicate category error

  Scenario: Update a category
    Given a category exists with the following details:
      | name        | description           |
      | Electronics | Electronic devices    |
    When I update the category description to "Electronic devices and accessories"
    Then the category should be updated successfully
    And the category description should be "Electronic devices and accessories"

  Scenario: Delete a category
    Given a category exists with the following details:
      | name        | description           |
      | Temporary   | Temporary category    |
    When I delete the category
    Then the category should be deleted successfully
    And the category should no longer exist

  Scenario: Find categories with products
    Given the following categories exist:
      | name        | description           |
      | Electronics | Electronic devices    |
      | Clothing    | Apparel and accessories |
      | Empty       | Empty category        |
    And the following products exist:
      | name       | description        | price  | stockQuantity | category    |
      | Smartphone | Latest smartphone  | 599.99 | 50           | Electronics |
      | T-Shirt    | Cotton t-shirt     | 19.99  | 100          | Clothing    |
    When I search for categories with products
    Then I should find 2 categories
    And the categories should include "Electronics" and "Clothing"
    And the categories should not include "Empty"
