package uc.seng301.asg1.accessor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;


import uc.seng301.asg1.entity.Recipe;

import java.util.List;

public class RecipeAccessor {

    private final SessionFactory sessionFactory;
    private static final Logger logger = LogManager.getLogger(IngredientAccessor.class);

    /**
     * Default Constructor
     * @param sessionFactory the sessionFactory to access the database
     */
    public RecipeAccessor(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Save an recipe into the database
     * @param recipe the ingredient to save
     * @return the recipe ID or -1 if an error occurred
     * @throws IllegalArgumentException if passed argument is null
     */
    public int save(Recipe recipe) {
        if (null == recipe) {
            throw new IllegalArgumentException("Recipe can't be null");
        }
        int result;
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            result = (Short) session.save(recipe);
            transaction.commit();
            logger.debug("saved {}", recipe);
        } catch (Exception e) {
            result = -1;
            logger.error("Unable to save recipe", e);
            if (null != transaction) {
                transaction.rollback();
            }
        }
        return result;
    }

    /**
     * Retrieve an recipe by its given name
     * @param name a name to look for
     * @return the Recipe if it exists in database, null otherwise
     */
    public Recipe getRecipeByName(String name) {
       Recipe result = null;
        try (Session session = sessionFactory.openSession()) {
            result = session.createQuery("FROM Recipe WHERE name ='" + name + "'", Recipe.class).uniqueResult();
        } catch (Exception e) {
            logger.error("Unable to retrieve recipe with name " + name, e);
        }
        return result;
    }

    /**
     * Retrieve all recipes by its given name in a list
     * @param name a name to look for
     * @return the Recipes in a list if it exists in database, null otherwise
     */
    public List<Recipe> getRecipeListByName(String name) {
        List<Recipe> result = null;
        try (Session session = sessionFactory.openSession()) {
            result = session.createQuery("SELECT r " +
                    "FROM Recipe r WHERE r.name ='" + name + "'", Recipe.class).getResultList();
        } catch (Exception e) {
            logger.error("Unable to retrieve recipe list with names " + name, e);
        }
        return result;
    }

    /**
     * Checks whether a recipe with given name already exists
     * @param name an recipe name
     * @return true if an recipe with given name already exists, false otherwise
     */
    public boolean recipeExists(String name) {
        boolean result = false;
        try (Session session = sessionFactory.openSession()) {
            result = session.createQuery(
                    "SELECT COUNT(id_recipe) FROM Recipe WHERE name ='" + name + "'", Long.class)
                    .uniqueResult() >= 1;

        } catch (Exception e) {
            logger.error("Unable to retrieve recipe with name " + name, e);
        }
        return result;
    }
}
