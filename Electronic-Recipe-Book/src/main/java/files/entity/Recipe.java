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

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Entity
public class Recipe {
  private Short idRecipe;
  private String name;
  private List<PreparationStep> preparationSteps;

  @OneToMany(mappedBy = "recipe")
  public List<PreparationStep> getPreparationSteps() {
    return preparationSteps;
  }

  public void setPreparationSteps(List<PreparationStep> preparationSteps) {
    this.preparationSteps = preparationSteps;
  }

  @Id
  @GeneratedValue
  @Column(name = "id_recipe")
  public Short getIdRecipe() {
    return idRecipe;
  }

  public void setIdRecipe(Short idRecipe) {
    this.idRecipe = idRecipe;
  }

  @Basic
  @Column(name = "name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "recipe with ID " + idRecipe + " name " + name + " with steps: "
        + preparationSteps.stream().map(PreparationStep::toString).collect(Collectors.joining(", "));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Recipe recipe = (Recipe) o;
    return Objects.equals(idRecipe, recipe.idRecipe) &&
        Objects.equals(name, recipe.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idRecipe, name);
  }
}
