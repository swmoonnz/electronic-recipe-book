
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

package uc.seng301.asg1;

import org.hibernate.HibernateException;
import org.hibernate.Metamodel;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.persistence.metamodel.EntityType;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import uc.seng301.asg1.accessor.RecipeAccessor;
import uc.seng301.asg1.entity.Ingredient;
import uc.seng301.asg1.entity.Recipe;
import uc.seng301.asg1.service.IngredientService;
import uc.seng301.asg1.service.RecipeService;
import uc.seng301.asg1.service.PreparationStepService;


import java.util.*;
import java.util.stream.Collectors;

/**
 * This class contains the main method of MyRecipe App. It handles
 * <ul>
 * <li> all Command Line Interface (CLI) menu items</li>
 * <li> the management of Java Persistence API (JPA) sessions</li>
 * </ul>
 */
public class App {

  // a JPA session factory is needed to access the actual data repository
  private final SessionFactory ourSessionFactory;

  // the scanner for the CLI
  private final Scanner cli;

  // a logger is useful to print messages in a configurable and flexible way
  // see resources/log4j2.xml configuration file
  private final Logger logger = LogManager.getLogger(App.class.getName());

  /**
   * Default constructor
   */
  public App() {
    Configuration configuration = new Configuration();
    configuration.configure();
    ourSessionFactory = configuration.buildSessionFactory();
    cli = new Scanner(System.in);
  }

  /**
   * Get a JPA session factory (i.e. to create sessions to interact with the database)
   * @return the JPA session factory
   * @see org.hibernate.SessionFactory
   */
  public SessionFactory getSessionFactory() {
    return ourSessionFactory;
  }

  /**
   * Get the command line reader to read input from users
   * @return the Scanner opened on the command line
   */
  public Scanner getScanner() {
    return cli;
  }

  /**
   * Main method, starts the Command Line Interface client for MyRecipe app
   * @param args none expected
   */
  public static void main(final String[] args) {
    App app = new App();
    IngredientService ingredientService = new IngredientService(app.getSessionFactory());
    RecipeService recipeService = new RecipeService(app.getSessionFactory());
    PreparationStepService preparationStepService = new PreparationStepService(app.getSessionFactory());
    RecipeAccessor recipeAccessor = new RecipeAccessor(app.getSessionFactory());

    System.out.println(app.welcome());

    boolean quit = false;
    while (!quit) {
      System.out.println(app.mainMenu());
      String input = app.getScanner().nextLine();
      int menuItem;
      try {
        menuItem = Integer.parseInt(input);
      } catch (NumberFormatException e) {
        menuItem = -1;
      }
      switch (menuItem) {
        case 1:
          app.addIngredientMenu(app.getScanner(), ingredientService);
          break;

        case 2:
          app.addRecipeMenu(app.getScanner(), recipeService, preparationStepService,
                  ingredientService, recipeAccessor);
          break;

        case 9:
          System.out.println(app.serializeDatabaseContent());
          break;

        case 0:
          quit = true;
          break;

        default:
          System.out.println("Unknown value entered");
      }
    }
  }


  /*----------------
   * PRIVATE METHODS
   ----------------*/

  /**
   * Create the welcome message for the MyRecipe App
   *
   * @return the App banner
   */
  private String welcome() {
    return "\n######################################################\n\n" +
            "  Welcome to MyRecipe - the ultimate recipe book app\n\n" +
            "######################################################";
  }

  /**
   * Create the main menu of MyRecipe App
   *
   * @return the menu with all top level user options
   */
  private String mainMenu() {
    return "\nWhat do you want to do next?\n" +
            "\t 1. Add an ingredient\n" +
            "\t 2. Add a recipe\n" +
            "\t 9. Print database content\n" +
            "\t 0. Exit\n" +
            "\n" +
            "Your Answer: ";
  }

  /**
   * Menu handling the creation of a new ingredient
   * @param scanner the input stream to get user input from
   * @param service the ingredient service layer
   */
  private void addIngredientMenu(Scanner scanner, IngredientService service) {
    System.out.println("Enter a name for your ingredient:");
    String name = scanner.nextLine();
    int ingredientId;

    while ((ingredientId = service.addIngredient(name)) < 0) {
      if (ingredientId == -2) {
        System.out.println("Ingredient already exists in the database");
        return;
      }
      else {
        System.out.println("Invalid name for ingredient (contains other characters than letters), please try again");
        name = scanner.nextLine();
      }
    }
    System.out.println("Ingredient added with ID:" + ingredientId);
  }

  /**
   * Menu handling the creation of a new recipe
   * @param scanner the input stream to get user input from
   * @param recipeService the recipe service layer
   * @param preparationStepService the preparation step service layer
   * @param ingredientService the ingredient step service layer
   * @param accessor the recipe accessor layer
   */
  private void addRecipeMenu(Scanner scanner, RecipeService recipeService, PreparationStepService preparationStepService,
                             IngredientService ingredientService, RecipeAccessor accessor) {
    System.out.println("Enter a name for your recipe:");
    String name = scanner.nextLine();
    int recipeId;
    String choice ="y";
//    Check if the recipe already exists in the database
    if (accessor.recipeExists(name)) {
      System.out.println("Recipe already exists in the database");
      List<Short> recipeIds = recipeService.getRecipeIdsByName(name);
      System.out.println(preparationStepService.getRecipeSummary(recipeIds));
      System.out.println("Do you want to create a new recipe with the same name? y|n");
      choice = scanner.nextLine();

      if (choice.equals("n")) {
        return;
      }
    }

    while ((recipeId = recipeService.addRecipe(name)) == -1) {
      System.out.println("Invalid name for recipe (contains other characters than letters), please try again");
      name = scanner.nextLine();
    }
    Short stepNumber = 0;
    while (choice.equals("y")) {
      stepNumber ++;
      System.out.println("Enter a preparation step for this recipe (press enter to finish):");
      String stepDescriptionToAdd = scanner.nextLine();
      System.out.println("Enter the ingredients for this preparation Step:");
      String ingredientsToAdd = scanner.nextLine();

      while (!ingredientService.ingredientNameIsValidForNames(ingredientsToAdd)) {
        System.out.println("Invalid name for ingredients (contains other characters than letters), please try again");
        ingredientsToAdd = scanner.nextLine();
      }
      Set<String> ingredients;
      ingredients = Arrays.stream(ingredientsToAdd.split(",")).map(String::trim).collect(Collectors.toSet());
      List<Integer> ingredientIds = ingredients.stream().map(ingredientService::returnOrAddIngredient).collect(Collectors.toList());
      preparationStepService.addPreparationStep(recipeId, stepDescriptionToAdd, ingredientIds, stepNumber);
      System.out.println("Do you want to add another preparation step? y|n");
      choice = scanner.nextLine();
    }
      System.out.println("Recipe " + name + " is saved with ID: " + recipeId + "\n");
  }


  /**
   * Compile the content of all entities stored in the database into user-friendly String.<br>
   * Highly relies on all Entities's toString() method to be fully user-friendly.
   *
   * @return a serialised version of the content of the database.
   */
  private String serializeDatabaseContent() {
    StringBuilder result = new StringBuilder();
    // by using try-with-resource, any resource will be closed auto-magically at the end of the surrounding try-catch
    // see https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html
    // if a resource is not opened inside a try-with-resource (or is not (Auto)Closeable, remember to
    // close it manually in the finally statement of a try-catch
    try (Session session = ourSessionFactory.openSession()) {
      logger.info("querying all managed entities in database");
      Metamodel metamodel = session.getSessionFactory().getMetamodel();
      for (EntityType<?> entityType : metamodel.getEntities()) {
        String entityName = entityType.getName();
        result.append("Content of ").append(entityName).append("\n");
        Query<Object> query = session.createQuery("from " + entityName, Object.class);
        logger.info("executing HQL query '{}'", query.getQueryString());
        for (Object o : query.list()) {
          result.append("\t").append(o.toString()).append("\n");
        }
      }

    } catch (HibernateException e) {
      result.append("Couldn't query content because of error").append(e.getLocalizedMessage());
      logger.error(e);
    }
    return result.toString();
  }

}
