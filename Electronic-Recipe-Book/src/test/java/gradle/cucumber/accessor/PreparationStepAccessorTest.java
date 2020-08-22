package gradle.cucumber.asg1.accessor;

import org.junit.Test;
import org.junit.BeforeClass;

import uc.seng301.asg1.accessor.PreparationStepAccessor;
import uc.seng301.asg1.accessor.RecipeAccessor;
import uc.seng301.asg1.entity.PreparationStep;
import uc.seng301.asg1.entity.Recipe;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class PreparationStepAccessorTest {

    private static SessionFactory factory;
    private static PreparationStepAccessor accessor;
    private static PreparationStepAccessor accessor1;
    private static RecipeAccessor recipeAccessor;
    private static Recipe recipe1;
    private static Recipe recipe2;

    @BeforeClass
    public static void init() {
        // see test/resource file that is using an embedded H2 database (see https://www.h2database.com/html/main.html)
        // with this h2 database, you are not using the sqlite file and therefore can create self-contained tests
        // you can simply create a test-dedicated environment by putting test resources under the test folder
        Configuration configuration = new Configuration();
        configuration.configure();
        factory = configuration.buildSessionFactory();
        accessor = new PreparationStepAccessor(factory);
        accessor1 = new PreparationStepAccessor(factory);
        recipeAccessor = new RecipeAccessor(factory);

        recipe1 = new Recipe();
        recipe1.setName("Tiramisu");
        recipe1.setIdRecipe((short) recipeAccessor.save(recipe1));

        recipe2 = new Recipe();
        recipe2.setName("Beef Noodle Soup");
        recipe2.setIdRecipe((short) recipeAccessor.save(recipe2));
     }

    @Test
    public void testSavePreparationStep() {
        String stepDescription = "Oil the pan";
        Short stepNumber = (short) 1;
        PreparationStep preparationStep = new PreparationStep();
        preparationStep.setDescription(stepDescription);
        preparationStep.setStepNumber(stepNumber);
        accessor.save(preparationStep);
        Query<PreparationStep> saved = factory.openSession().createQuery("" +
                "FROM PreparationStep p " +
                "WHERE p.stepNumber =" + stepNumber + " " +
                "AND p.description = '" + stepDescription + "'", PreparationStep.class);
        assertEquals("Unable to retrieve " + stepDescription, 1, saved.list().size());
    }

    @Test
    public void testGetOnePreparationStepByRecipeId() {
        String stepDescription = "Grate the cheese";
        Short stepNumber = (short) 1;
        PreparationStep preparationStep = new PreparationStep();
        preparationStep.setStepNumber(stepNumber);
        preparationStep.setDescription(stepDescription);
        preparationStep.setRecipe(recipe1);
        accessor.save(preparationStep);

        List<PreparationStep> savedPreparationSteps = accessor.getPreparationStepsByRecipeId(recipe1.getIdRecipe());
        assertEquals("Unable to retrieve " + stepDescription, 1, savedPreparationSteps.size());
        assertEquals(stepDescription, savedPreparationSteps.get(0).getDescription());
        assertEquals(stepNumber, savedPreparationSteps.get(0).getStepNumber());
    }

    @Test
    public void testGetLatestStepNumberByRecipeId() {
        String stepDescription = "Boil the pasta";
        Short stepNumber = (short) 1;
        PreparationStep preparationStep = new PreparationStep();
        preparationStep.setStepNumber(stepNumber);
        preparationStep.setDescription(stepDescription);
        preparationStep.setRecipe(recipe2);
        accessor.save(preparationStep);

        stepDescription = "season the chicken";
        stepNumber = (short) 2;
        preparationStep = new PreparationStep();
        preparationStep.setStepNumber(stepNumber);
        preparationStep.setDescription(stepDescription);
        preparationStep.setRecipe(recipe2);
        accessor.save(preparationStep);

        int latestStepNumber = accessor.getLatestStepNumberByRecipeId(recipe2.getIdRecipe());
        assertEquals(latestStepNumber, (int) stepNumber);
    }

}
