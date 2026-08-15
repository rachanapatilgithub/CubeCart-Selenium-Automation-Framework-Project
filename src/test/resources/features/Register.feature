Feature: User Registration
  As a new visitor of the CubeCart store
  I want to register a new account
  So that I can log in and shop later

  Background:
    Given the user is on the Register page

  Scenario: Register a new user with valid details
    When the user fills the registration form with all valid details
    And the user clicks the Register button
    Then the registration should be successful and browser closed
