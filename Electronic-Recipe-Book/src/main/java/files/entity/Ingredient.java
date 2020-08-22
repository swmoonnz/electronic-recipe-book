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
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.List;
import java.util.Objects;

@Entity
public class Ingredient {
  private Short idIngredient;
  private String name;
  private List<PreparationStep> inSteps;

  @Id
  @GeneratedValue
  @Column(name = "id_ingredient")
  public Short getIdIngredient() {
    return idIngredient;
  }

  public void setIdIngredient(Short idIngredient) {
    this.idIngredient = idIngredient;
  }

  @Basic
  @Column(name = "name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @ManyToMany(cascade = { CascadeType.ALL })
  @JoinTable(
      name = "ingredient_usedin_step",
      joinColumns = { @JoinColumn(name = "id_ingredient") },
      inverseJoinColumns = { @JoinColumn(name = "id_step") }
  )
  public List<PreparationStep> getInSteps() {
    return inSteps;
  }

  public void setInSteps(List<PreparationStep> steps) {
    this.inSteps = steps;
  }

  @Override
  public String toString() {
    return "ingredient with ID " + idIngredient + " and name " + name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Ingredient that = (Ingredient) o;
    return Objects.equals(idIngredient, that.idIngredient) &&
        Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(idIngredient, name);
  }
}
