
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

/*
 * This Java source file was generated by the Gradle 'init' task.
 */

package gradle.cucumber.asg1.accessor;

import org.junit.Test;

import uc.seng301.asg1.accessor.IngredientAccessor;
import uc.seng301.asg1.entity.Ingredient;

import static org.junit.Assert.*;

import org.hibernate.query.Query;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.BeforeClass;

public class IngredientAccessorTest {

  private static SessionFactory factory;
  private static IngredientAccessor accessor;

  @BeforeClass
  public static void init() {
    // see test/resource file that is using an embedded H2 database (see https://www.h2database.com/html/main.html)
    // with this h2 database, you are not using the sqlite file and therefore can create self-contained tests
    // you can simply create a test-dedicated environment by putting test resources under the test folder
    Configuration configuration = new Configuration();
    configuration.configure();
    factory = configuration.buildSessionFactory();
    accessor = new IngredientAccessor(factory);
  }

  @Test
  public void testSaveIngredientOneWord() {
    String name = "carrot";
    Ingredient ingredient = new Ingredient();
    ingredient.setName(name);
    accessor.save(ingredient);
    Query<Ingredient> saved = factory.openSession().createQuery("FROM Ingredient WHERE name ='" + name + "'", Ingredient.class);
    assertEquals("Unable to retrieve " + name, 1, saved.list().size());
  }

  @Test
  public void testSaveIngredientMultipleWords() {
    String name = "red onion";
    Ingredient ingredient = new Ingredient();
    ingredient.setName(name);
    accessor.save(ingredient);
    Query<Ingredient> saved = factory.openSession().createQuery("FROM Ingredient WHERE NAME ='" + name + "'", Ingredient.class);
    assertEquals("Unable to retrieve " + name, 1, saved.list().size());
  }

  @Test
  public void testGetIngredientByName() {
    // it's ok to rely on accessor.save here since we have a unit test that checks that bit for us
    // and we want to avoid code duplications
    String name = "cucumber";
    Ingredient ingredient = new Ingredient();
    ingredient.setName(name);
    accessor.save(ingredient);
    assertEquals("Unable to retrieve " + name, ingredient, accessor.getIngredientByName(name));
  }

  @Test
  public void testIngredientExists() {
    String name = "tomato";
    Ingredient ingredient = new Ingredient();
    ingredient.setName(name);
    accessor.save(ingredient);
    assertTrue("Ingredient does not exist! " + name, accessor.ingredientExists(name));
  }
}
