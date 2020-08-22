Feature: Recipe

  Scenario: AddNewRecipe
    Given the recipe "mince pie" does not already exist
    When the user inserts a recipe "mince pie"
    Then the recipe "mince pie" is saved in the database

  Scenario: AddExistingRecipe
    Given the recipe "mince pie" does not already exist
    When the user inserts a recipe "mince pie"
    Then the recipe "mince pie" is saved in the database
    When the user tries to insert add another recipe "mince pie"
    Then the recipe "mince pie" already exists in the database
    And another recipe "mince pie" is saved in the database