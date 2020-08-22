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

package uc.seng301.asg1.entity;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "preparation_step", catalog = "")
public class PreparationStep {

  @Id
  @GeneratedValue
  @Column(name = "id_step")
  private Short idStep;

  @Basic
  @Column(name = "step_number")
  private Short stepNumber;

  @Basic
  @Column(name = "description")
  private String description;

  @ManyToMany
  @JoinTable(
          name = "ingredient_usedin_step",
          joinColumns = {@JoinColumn(name = "id_step")},
          inverseJoinColumns = {@JoinColumn(name = "id_ingredient")}
  )
  private Set<Ingredient> ingredients;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id_recipe")
  private Recipe recipe;

  public Set<Ingredient> getIngredients() {
    return ingredients;
  }

  public void setIngredients(Set<Ingredient> ingredients) {
    this.ingredients = ingredients;

  }

  public Recipe getRecipe() {
    return recipe;
  }

  public void setRecipe(Recipe recipe) {
    this.recipe = recipe;
  }

  public Short getIdStep() {
    return idStep;
  }

  public void setIdStep(Short idStep) {
    this.idStep = idStep;
  }

  public Short getStepNumber() {
    return stepNumber;
  }

  public void setStepNumber(Short stepNumber) {
    this.stepNumber = stepNumber;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return "Preparation step with ID " + idStep + ": " + description + " with ingredients: "
            + ingredients.stream().map(Ingredient::getName).collect(Collectors.joining(", "));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PreparationStep that = (PreparationStep) o;
    return Objects.equals(idStep, that.idStep) &&
            Objects.equals(stepNumber, that.stepNumber) &&
            Objects.equals(description, that.description);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idStep, stepNumber, description);
  }
}
