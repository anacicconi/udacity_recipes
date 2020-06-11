package com.cicconi.recipes.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "ingredient")
public class Ingredient implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public double quantity;
    public String measure;
    public String ingredient;
    @ColumnInfo(name = "recipe_id")
    public Long recipeId;

    @Ignore
    public Ingredient(double quantity, String measure, String ingredient, Long recipeId) {
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
        this.recipeId = recipeId;
    }

    Ingredient(int id, double quantity, String measure, String ingredient, Long recipeId) {
        this.id = id;
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
        this.recipeId = recipeId;
    }

    public int getId() {
        return id;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public Long getRecipeId() {
        return recipeId;
    }
}
