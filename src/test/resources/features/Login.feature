Feature: User Login
  As a registered user of the CubeCart store
  I want to log in to my account
  So that I can access my orders and profile

  Background:
    Given the user is on the Login page

  Scenario: Login with invalid credentials
    When the user logs in with email "invaliduser@mail.com" and password "WrongPassword123"
    Then an error message should be displayed
