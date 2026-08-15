Feature: Add Product
  As a store administrator
  I want to add a new product
  So that customers can view and purchase it in the store

  Background:
    Given the admin is logged in to the Admin Panel
    And the admin is on the Add Product page

  Scenario: Add a new product with valid details
    When the admin fills the product details with a valid product name
    And the admin clicks the Save button
    Then the product should be saved successfully

  Scenario: Add a product without a product name
    When the admin clicks the Save button without entering a product name
    Then the product should not be saved
