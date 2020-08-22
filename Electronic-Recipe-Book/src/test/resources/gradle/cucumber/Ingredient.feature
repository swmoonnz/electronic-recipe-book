#
# Copyright (c) 2020. University of Canterbury
#
# This file is part of SENG301 lab material.
#
#  The lab material is free software: you can redistribute it and/or modify
#  it under the terms of the GNU Lesser General Public License as published
#  by the Free Software Foundation, either version 3 of the License, or
#  (at your option) any later version.
#
#  The lab material is distributed in the hope that it will be useful,
#  but WITHOUT ANY WARRANTY; without even the implied warranty of
#  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
#  GNU Lesser General Public License for more details.
#
#  You should have received a copy of the GNU Lesser General Public License
#  along with Foobar. If not, see <https://www.gnu.org/licenses/>.
#

#
# This feature file contains all acceptance scenarios related to CRUD management of ingredient
#

Feature: Ingredient

  Scenario: AddNewIngredient
    Given the ingredient "red onion" does not already exist
    When the user inserts an ingredient "red onion"
    Then the ingredient "red onion" is saved in the database

  Scenario: AddExistingIngredient
    Given the ingredient "red onion" does not already exist
    When the user inserts an ingredient "red onion"
    And the user tries to insert another ingredient "red onion"
    Then the ingredient "red onion" is not saved in the database