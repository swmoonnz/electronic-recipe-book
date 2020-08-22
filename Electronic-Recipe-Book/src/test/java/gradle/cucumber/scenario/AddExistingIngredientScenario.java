package gradle.cucumber.asg1.scenario;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.Assert;
import uc.seng301.asg1.accessor.IngredientAccessor;
import uc.seng301.asg1.service.IngredientService;

public class AddExistingIngredientScenario {
    private SessionFactory factory;
    private IngredientService service;
    private IngredientAccessor accessor;
    private int ingredientId;

    @Before
    public void setUp() {
        // see test/resource file that is using an embedded H2 database (see https://www.h2database.com/html/main.html)
        // with this h2 database, you are not using the sqlite file and therefore can create self-contained tests
        // you can simply create a test-dedicated environment by putting test resources under the test folder
        Configuration configuration = new Configuration();
        configuration.configure();

        // for acceptance tests, you may also use Mockito (see Lab.5) instead of relying on an H2 database.
        factory = configuration.buildSessionFactory();
        service = new IngredientService(factory);
        accessor = new IngredientAccessor(factory);
    }

    @And("the user tries to insert another ingredient {string}")
    public void theUserTriesInsertAnotherIngredient(String ingredientName) {
        ingredientId = service.addIngredient(ingredientName);
    }

    @Then("the ingredient {string} is not saved in the database")
    public void theIngredientIsNotSavedInTheDatabase(String ingredientName) {
        Assert.assertEquals(ingredientId, -2);
    }


}
