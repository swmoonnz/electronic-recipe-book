package gradle.cucumber.asg1.scenario;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.Assert;
import uc.seng301.asg1.accessor.RecipeAccessor;
import uc.seng301.asg1.service.RecipeService;

public class  AddExistingRecipeScenario {
    private SessionFactory factory;
    private RecipeService service;
    private RecipeAccessor accessor;
    private int recipeId;

    @Before
    public void setUp() {
        // see test/resource file that is using an embedded H2 database (see https://www.h2database.com/html/main.html)
        // with this h2 database, you are not using the sqlite file and therefore can create self-contained tests
        // you can simply create a test-dedicated environment by putting test resources under the test folder
        Configuration configuration = new Configuration();
        configuration.configure();

        // for acceptance tests, you may also use Mockito (see Lab.5) instead of relying on an H2 database.
        factory = configuration.buildSessionFactory();
        service = new RecipeService(factory);
        accessor = new RecipeAccessor(factory);
    }

    @When("the user tries to insert add another recipe {string}")
    public void theUserTriesInsertAnotherRecipe(String recipeName) {
        Assert.assertNotEquals(service.addRecipe(recipeName), -1);
    }

    @Then("the recipe {string} already exists in the database")
    public void theRecipeExistsInDatabase(String recipeName) {
        Assert.assertTrue(accessor.recipeExists(recipeName));
    }

    @And("another recipe {string} is saved in the database")
    public void theSameRecipeIsSavedInTheDatabase(String recipeName) {
        Assert.assertEquals(accessor.getRecipeListByName(recipeName).size(), 2);
    }
}
