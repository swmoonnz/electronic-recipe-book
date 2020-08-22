package uc.seng301.asg1.accessor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import uc.seng301.asg1.entity.PreparationStep;

import java.util.List;

/**
 * This class handles the access to the Preparation Step beans
 */
public class PreparationStepAccessor {

    private final SessionFactory sessionFactory;
    private static final Logger logger = LogManager.getLogger(IngredientAccessor.class);

    /**
     * Default Constructor
     * @param sessionFactory the sessionFactory to access the database
     */
    public PreparationStepAccessor(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Save an preparation step into the database
     * @param preparationStep the preparationStep to save
     * @return the ingredient ID or -1 if an error occurred
     * @throws IllegalArgumentException if passed argument is null
     */
    public int save(PreparationStep preparationStep) {
        if (null == preparationStep) {
            throw new IllegalArgumentException("preparationStep can't be null");
        }
        int result;
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            result = (Short) session.save(preparationStep);
            transaction.commit();
            logger.debug("saved {}", preparationStep);
        } catch (Exception e) {
            result = -1;
            logger.error("Unable to save preparationStep", e);
            if (null != transaction) {
                transaction.rollback();
            }
        }
        return result;
    }

    /**
     * Get the latest step number of a recipe in database
     * @param recipeId the recipe id to look for
     * @return the last step number of the recipe
     */
    public short getLatestStepNumberByRecipeId(Short recipeId) {
        short result = 0;
        try (Session session = sessionFactory.openSession()) {
            result = session.createQuery(
                    "SELECT MAX(p.stepNumber) " +
                            "FROM PreparationStep p " +
                            "WHERE p.recipe = " + recipeId,
                    Short.class
            ).uniqueResult();
        } catch (Exception e) {
            logger.error("Unable to get preparation step by id " + recipeId);
        }
        return result;
    }

    /**
     * Get all the preparation steps for a recipe using the recipe id
     * @param recipeId the recipe id to use to look for preparation steps
     * @return all the preparation steps that belongs to a recipe
     */
    public List<PreparationStep> getPreparationStepsByRecipeId(Short recipeId) {
        List<PreparationStep> result = null;
        try (Session session = sessionFactory.openSession()) {
            result = session.createQuery(
                    "SELECT p " +
                            "FROM PreparationStep p " +
                            "LEFT JOIN FETCH p.recipe " +
                            "LEFT JOIN FETCH p.ingredients " +
                            "WHERE p.recipe = " + recipeId,
                    PreparationStep.class
            ).getResultList();
        } catch (Exception e) {
            logger.error("Unable to get preparation step by id " + recipeId);
        }
        return result;
    }
}
