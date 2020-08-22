Feature: PreparationStep

Scenario: AddNewRecipeSinglePreparationStep
  Given the recipe "chicken fried rice" does not already exist
  When the user inserts a recipe "chicken fried rice"
  And adds a preparation step "prepare the rice" with ingredient "rice" to recipe "chicken fried rice"
  Then the recipe "chicken fried rice" is saved in the database
  And recipe "chicken fried rice" has preparation step 1 as "prepare the rice"
  And the preparation step has ingredient "rice"


  Scenario: Add New Recipe with multiple preparation steps
    Given the recipe "tiramisu" does not already exist
    When the user inserts a recipe "tiramisu"
    And adds a preparation step "whisk the eggs" with ingredient "egg" to recipe "tiramisu"
    And adds another preparation step "add the flour" with ingredient "flour" to the recipe "tiramisu"
    Then the recipe "tiramisu" is saved in the database
    And recipe "tiramisu" has preparation step 1 as "whisk the eggs"
    And the preparation step has ingredient "egg"
    And recipe "tiramisu" has another preparation step 2 as "add the flour"
    And the second preparation step has ingredient "flour"

