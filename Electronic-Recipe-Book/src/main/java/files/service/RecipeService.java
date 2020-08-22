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
import uc.seng301.asg1.accessor.RecipeAccessor;
import uc.seng301.asg1.entity.Recipe;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This class manages Recipes' business logic.
 */
public class RecipeService {

    private final RecipeAccessor accessor;

    /**
     * Default Constructor
     * @param factory the JPA session factory (to be passed to accessor)
     */
    public RecipeService(SessionFactory factory) {
        accessor = new RecipeAccessor(factory);
    }

    /**
     * Deals with the insertion a recipe by checking the integrity of a recipe and calling the accessor
     * @param name the recipe name to be inserted
     * @return the ID of the newly inserted recipe with given name, -1 if given name is invalid
     */
    public int addRecipe(String name) {
        if (!recipeNameIsValid(name)) {
            return -1;
        }
        Recipe recipe = new Recipe();
        recipe.setName(name.trim());
        return accessor.save(recipe);
    }

    /**
     * Deals with checking a given recipe name is in valid format
     * @param name the recipe name being that needs checking
     * @return a boolean true if the given recipe name is valid. False otherwise
     */
    private boolean recipeNameIsValid(String name) {
        return null != name && !name.isBlank()
                && name.chars().allMatch(i -> Character.isAlphabetic(i) || Character.isWhitespace(i));
    }

    /**
     * Gets all recipe ids that have the same recipe name
     * @param recipeName recipe name to get all recipe ids by
     * @return a list of all the recipe ids with the given recipeName
     */
    public List<Short> getRecipeIdsByName(String recipeName) {
        return accessor.getRecipeListByName(recipeName.trim())
                .stream()
                .map(Recipe::getIdRecipe)
                .collect(Collectors.toList());
    }
}
