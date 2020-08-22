
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

package gradle.cucumber.asg1.scenario;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.Assert;
import uc.seng301.asg1.accessor.IngredientAccessor;
import uc.seng301.asg1.service.IngredientService;

public class AddNewIngredientScenario {

  private SessionFactory factory;
  private IngredientService service;
  private IngredientAccessor accessor;

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

  @Given("the ingredient {string} does not already exist")
  public void theIngredientDoesNotExistAlready(String ingredientName) {
    // look carefully in the implementation code if it matches this scenario
    Assert.assertFalse(accessor.ingredientExists(ingredientName));
  }

  @When("the user inserts an ingredient {string}")
  public void theUserInsertsAnIngredient(String ingredientName) {
    Assert.assertNotEquals(service.addIngredient(ingredientName), -1);
  }

  @Then("the ingredient {string} is saved in the database")
  public void theIngredientIsSavedInTheDatabase(String ingredientName) {
    Assert.assertEquals(accessor.getIngredientByName(ingredientName).getName(), ingredientName);
  }

}