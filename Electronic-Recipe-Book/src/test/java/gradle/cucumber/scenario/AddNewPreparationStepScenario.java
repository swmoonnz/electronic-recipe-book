package gradle.cucumber.asg1.scenario;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import uc.seng301.asg1.entity.PreparationStep;
import uc.seng301.asg1.service.IngredientService;
import uc.seng301.asg1.service.PreparationStepService;
import uc.seng301.asg1.service.RecipeService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AddNewPreparationStepScenario {

    private SessionFactory factory;
    private RecipeService recipeService;
    private IngredientService ingredientService;
    private PreparationStepService preparationStepService;

    private PreparationStep preparationStepInDatabase;

    @Before
    public void setUp() {
        Configuration configuration = new Configuration();
        configuration.configure();

        factory = configuration.buildSessionFactory();
        recipeService = new RecipeService(factory);
        ingredientService = new IngredientService(factory);
        preparationStepService = new PreparationStepService(factory);
    }

    @And("adds a preparation step {string} with ingredient {string} to recipe {string}")
    public void addAPreparationStepWithIngredient(String stepDescription, String ingredientName, String recipeName) {
        PreparationStep preparationStep = new PreparationStep();
        preparationStep.setDescription(stepDescription);
        Short stepNumber = 1;

        int ingredientId = ingredientService.returnOrAddIngredient(ingredientName);
        Short recipeId = recipeService.getRecipeIdsByName(recipeName).get(0);
        preparationStepService.addPreparationStep(recipeId, stepDescription, new ArrayList<>(Collections.singletonList(ingredientId)), stepNumber);
    }

    @And("recipe {string} has preparation step {short} as {string}")
    public void recipeHasPreparationStepAs(String recipeName, Short stepNum, String stepDescription) {
        List<Short> recipeIds = recipeService.getRecipeIdsByName(recipeName);
        Short recipeId = recipeIds.get(0);
        preparationStepInDatabase = preparationStepService.getPreparationStepsByRecipeId(recipeId).get(0);
        assertEquals(preparationStepInDatabase.getDescription(), stepDescription);
        assertEquals(preparationStepInDatabase.getStepNumber(), stepNum);
    }

    @And("the preparation step has ingredient {string}")
    public void thePreparationStepHasIngredient(String ingredientName) {
        assertEquals(1,
                preparationStepInDatabase.getIngredients().stream()
                        .filter(e -> e.getName().equals(ingredientName))
                        .count());
    }
}
//        throw new io.cucumber.java.PendingException();}
