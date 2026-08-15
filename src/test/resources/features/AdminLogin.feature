Feature: Admin Login
  As a store administrator
  I want to log in to the Admin Panel
  So that I can manage products, orders and customers

  Background:
    Given the admin opens the Admin URL

  Scenario: Admin logs in with valid credentials
    When the admin logs in with valid credentials
    Then the admin dashboard should be displayed

  Scenario: Admin logs in with invalid credentials
    When the admin logs in with username "admin" and password "wrongPassword123"
    Then the admin login should fail
