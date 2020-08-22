/*
 * Copyright (c) 2020. University of Canterbury
 *
 * This file is part of SENG301 lab material.
 *
 *  The lab material is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published
 *  by the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  The lab material is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this material.  If not, see <https://www.gnu.org/licenses/>.
 */

package uc.seng301.asg1.service;

import org.hibernate.SessionFactory;
import uc.seng301.asg1.accessor.IngredientAccessor;
import uc.seng301.asg1.entity.Ingredient;
import uc.seng301.asg1.utils.Util;
import java.util.Arrays;

/**
 * This class manages Ingredients' business logic.
 */
public class IngredientService {

  private final IngredientAccessor accessor;

  /**
   * Default Constructor
   * @param factory the JPA session factory (to be passed to accessor)
   */
  public IngredientService(SessionFactory factory) {
    accessor = new IngredientAccessor(factory);
  }

  /**
   * Deals with the insertion an ingredient by checking the integrity of an ingredient and calling the accessor
   * @param name the ingredient name to be inserted
   * @return the ID of the newly inserted ingredient with given name, -1 if given name is invalid and -2 if given name
   * already exists in the database
   */
  public int addIngredient(String name) {
    if (!isOneIngredientNameValid(name)) {
      return -1;
    }
    name = name.trim();
    if (accessor.ingredientExists(name)) {
      return -2;
    }
    Ingredient ingredient = new Ingredient();
    ingredient.setName(name.trim());
    return accessor.save(ingredient);
  }

  /**
   * Checks whether an ingredient with same name already exists. If it doesn't it inserts ingredient with given name.
   * @param name the ingredient name to be checked
   * @return the ID of newly inserted ingredient with given name, or return ID of existing ingredient
   */
  public int returnOrAddIngredient(String name) {
    name = name.trim();
    if (accessor.ingredientExists(name)) {
      return accessor.getIngredientByName(name).getIdIngredient();
    }

    Ingredient ingredient = new Ingredient();
    ingredient.setName(name);
    return accessor.save(ingredient);
  }

  /**
   * Checks whether a given name for an ingredient is valid
   * @param name an ingredient name to be checked
   * @return true if given name contains only letters with or without white space
   */
  public static boolean isOneIngredientNameValid(String name) {
    return null != name && !name.isEmpty()
            && name.chars().allMatch(i -> Character.isAlphabetic(i) || Character.isWhitespace(i));
  }

  /**
   * Checks whether given the names of ingredients provided are valid
   * @param names ingredient names to be checked
   * @return true if given name contains only letters
   */
  public boolean ingredientNameIsValidForNames(String names) {
    String[] ingredientNames = names.split(",");
    return Arrays.stream(ingredientNames)
            .map(String::trim)
            .allMatch(Util::isNameValid);
  }
}
