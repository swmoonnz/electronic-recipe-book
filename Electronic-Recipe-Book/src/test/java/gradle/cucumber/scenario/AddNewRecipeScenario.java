package gradle.cucumber.asg1.scenario;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.Assert;
import uc.seng301.asg1.accessor.RecipeAccessor;
import uc.seng301.asg1.service.RecipeService;

public class AddNewRecipeScenario {

    private SessionFactory factory;
    private RecipeService service;
    private RecipeAccessor accessor;

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

    @Given("the recipe {string} does not already exist")
    public void theRecipeDoesNotExistAlready(String recipeName) {
        // look carefully in the implementation code if it matches this scenario
        Assert.assertFalse(accessor.recipeExists(recipeName));
    }

    @When("the user inserts a recipe {string}")
    public void theUserInsertsRecipe(String recipeName) {
        Assert.assertNotEquals(service.addRecipe(recipeName), -1);
    }

    @Then("the recipe {string} is saved in the database")
    public void theRecipeIsSavedInTheDatabase(String recipeName) {
        Assert.assertEquals(accessor.getRecipeByName(recipeName).getName(), recipeName);
    }
}
