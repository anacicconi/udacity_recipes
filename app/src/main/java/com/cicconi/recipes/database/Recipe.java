package com.cicconi.recipes.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import java.io.Serializable;
import java.util.List;

@Entity(tableName = "recipe")
public class Recipe implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public Integer id;
    @ColumnInfo(name = "api_id")
    public Integer apiId;
    public String name;
    public Integer servings;
    public String image;
    public Boolean favorite;

    @Ignore
    private List<Ingredient> ingredients;

    @Ignore
    public Recipe(int apiId, String name, int servings, String image, Boolean favorite) {
        this.apiId = apiId;
        this.name = name;
        this.servings = servings;
        this.image = image;
        this.favorite = favorite;
    }

    Recipe(int id, int apiId, String name, int servings, String image, Boolean favorite) {
        this.id = id;
        this.apiId = apiId;
        this.name = name;
        this.servings = servings;
        this.image = image;
        this.favorite = favorite;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public Integer getApiId() {
        return apiId;
    }

    public String getName() {
        return name;
    }

    public Integer getServings() {
        return servings;
    }

    public String getImage() {
        return image;
    }

    public Boolean isFavorite() {
        return favorite;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
}
