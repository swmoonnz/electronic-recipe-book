package uc.seng301.asg1.service;

import org.hibernate.SessionFactory;
import uc.seng301.asg1.accessor.PreparationStepAccessor;
import uc.seng301.asg1.accessor.RecipeAccessor;
import uc.seng301.asg1.entity.Ingredient;
import uc.seng301.asg1.entity.PreparationStep;
import uc.seng301.asg1.entity.Recipe;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This class manages PreparationSteps' business logic.
 */
public class PreparationStepService {

    private final PreparationStepAccessor accessor;

    /**
     * Default Constructor
     * @param factory the JPA session factory (to be passed to accessor)
     */
    public PreparationStepService(SessionFactory factory) { accessor = new PreparationStepAccessor(factory); }

    /**
     * Deals with the insertion a preparation step with the recipe and all the ingredients it is associated with
     * @param recipeId the recipe that the preparation step belongs to
     * @param stepDescription the preparation step description
     * @param ingredientIds all the ingredients in the preparation step
     * @param stepNumber the step number of the preparation step
     * @return the ID of the newly inserted preparation step
     */
    public int addPreparationStep(int recipeId, String stepDescription, List<Integer> ingredientIds, Short stepNumber) {
        Recipe recipe = new Recipe();
        PreparationStep preparationStep = new PreparationStep();
        recipe.setIdRecipe((short) recipeId);
        preparationStep.setRecipe(recipe);
        stepDescription = stepDescription.trim();
        preparationStep.setDescription(stepDescription);

        Set<Ingredient> ingredients = new HashSet<>();

        for (int i : ingredientIds) {
            Ingredient preparationStepIngredient = new Ingredient();
            preparationStepIngredient.setIdIngredient((short) i);
            ingredients.add(preparationStepIngredient);
        }
        preparationStep.setIngredients(ingredients);
        preparationStep.setStepNumber(stepNumber);

        return accessor.save(preparationStep);
    }

    /**
     * Gets all the preparation steps of a recipe using the recipe id
     * @param recipeId
     * @return list of all the preparation steps of a recipe
     */
    public List<PreparationStep> getPreparationStepsByRecipeId(Short recipeId) {
        return accessor.getPreparationStepsByRecipeId(recipeId);
    }

    /**
     * Get the summary of a recipe and all of its preparation steps
     * @param recipeIds
     * @return the recipe and the contents of the preparations steps as a string
     */
    public String getRecipeSummary(List<Short> recipeIds) {
        StringBuilder summary = new StringBuilder();
        for (Short id : recipeIds) {
            summary.append("Recipe ID: ").append(id).append("\n");
            List<PreparationStep> preparationSteps = accessor.getPreparationStepsByRecipeId(id);
            summary.append(preparationSteps.stream()
                    .map(p -> p.getStepNumber() + ". " + p.getDescription())
                    .limit(3L)
                    .collect(Collectors.joining("\n")));
            summary.append("\n\n");
        }
        return summary.toString();
    }

}
