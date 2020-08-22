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

package uc.seng301.asg1.accessor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import uc.seng301.asg1.entity.Ingredient;

/**
 * This class handles the access to the Ingredient beans
 */
public class IngredientAccessor {

  private final SessionFactory sessionFactory;
  private static final Logger logger = LogManager.getLogger(IngredientAccessor.class);

  /**
   * Default Constructor
   * @param sessionFactory the sessionFactory to access the database
   */
  public IngredientAccessor(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }

  /**
   * Save an ingredient into the database
   * @param ingredient the ingredient to save
   * @return the ingredient ID or -1 if an error occurred
   * @throws IllegalArgumentException if passed argument is null
   */
  public int save(Ingredient ingredient) {
    if (null == ingredient) {
      throw new IllegalArgumentException("Ingredient can't be null");
    }
    int result;
    Transaction transaction = null;
    try (Session session = sessionFactory.openSession()) {
      transaction = session.beginTransaction();
      result = (Short) session.save(ingredient);
      transaction.commit();
      logger.debug("saved {}", ingredient);
    } catch (Exception e) {
      result = -1;
      logger.error("Unable to save ingredient", e);
      if (null != transaction) {
        transaction.rollback();
      }
    }
    return result;
  }

  /**
   * Retrieve an ingredient by its given name
   * @param name a name to look for
   * @return the Ingredient if it exists in database, null otherwise
   */
  public Ingredient getIngredientByName(String name) {
    Ingredient result = null;
    try (Session session = sessionFactory.openSession()) {
      result = session.createQuery("FROM Ingredient WHERE name ='" + name + "'", Ingredient.class).uniqueResult();
    } catch (Exception e) {
      logger.error("Unable to retrieve ingredient with name " + name, e);
    }
    return result;
  }

  /**
   * Checks whether an ingredient with given name already exists
   * @param name an ingredient name
   * @return true if an ingredient with given name already exists, false otherwise
   */
  public boolean ingredientExists(String name) {
    boolean result = false;
    try (Session session = sessionFactory.openSession()) {
      result = session.createQuery(
          "SELECT COUNT(id_ingredient) FROM Ingredient WHERE name ='" + name + "'", Long.class)
          .uniqueResult() == 1L;
    } catch (Exception e) {
      logger.error("Unable to retrieve ingredient with name " + name, e);
    }
    return result;
  }

  /**
   * Retrieve an ingredient by its given id
   * @param id an id to look for
   * @return the Ingredient if it exists in database, null otherwise
   */
  public Ingredient getIngredientById(Short id) {
    Ingredient result = null;
    try (Session session = sessionFactory.openSession()) {
      result = session.createQuery("FROM Ingredient WHERE idIngredient ='" + id + "'", Ingredient.class).uniqueResult();
    } catch (Exception e) {
      logger.error("Unable to retrieve ingredient with id " + id, e);
    }
    return result;
  }

}
