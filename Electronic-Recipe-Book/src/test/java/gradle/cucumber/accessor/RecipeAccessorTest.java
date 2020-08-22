package gradle.cucumber.asg1.accessor;

import org.junit.Test;


import uc.seng301.asg1.accessor.RecipeAccessor;
import uc.seng301.asg1.entity.Recipe;

import static org.junit.Assert.*;

import org.hibernate.query.Query;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.BeforeClass;

public class RecipeAccessorTest {

    private static SessionFactory factory;
    private static RecipeAccessor accessor;

    @BeforeClass
    public static void init() {
        Configuration configuration = new Configuration();
        configuration.configure();
        factory = configuration.buildSessionFactory();
        accessor = new RecipeAccessor(factory);
    }

    @Test
    public void testSaveRecipeOneWord() {
        String name = "pizza";
        Recipe recipe = new Recipe();
        recipe.setName(name);
        accessor.save(recipe);
        Query<Recipe> saved = factory.openSession().createQuery("FROM Recipe WHERE name ='" + name + "'", Recipe.class);
        assertEquals("Unable to retrieve " + name, 1, saved.list().size());
    }

    @Test
    public void testSaveRecipeMultipleWords() {
        String name = "mince pie";
        Recipe recipe = new Recipe();
        recipe.setName(name);
        accessor.save(recipe);
        Query<Recipe> saved = factory.openSession().createQuery("FROM Recipe WHERE NAME ='" + name + "'", Recipe.class);
        assertEquals("Unable to retrieve " + name, 1, saved.list().size());
    }

    @Test
    public void testGetRecipeByName() {
        String name = "lasagne";
        Recipe recipe = new Recipe();
        recipe.setName(name);
        accessor.save(recipe);
        assertEquals("Unable to retrieve " + name, recipe, accessor.getRecipeByName(name));
    }

    @Test
    public void testRecipeExists() {
        String name = "tiramisu";
        Recipe recipe = new Recipe();
        recipe.setName(name);
        accessor.save(recipe);
        assertTrue("Recipe does not exist! " + name, accessor.recipeExists(name));
    }
}
