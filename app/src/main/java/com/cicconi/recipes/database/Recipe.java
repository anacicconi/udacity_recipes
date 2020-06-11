package com.cicconi.recipes.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "recipe")
public class Recipe implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public Integer id;
    @ColumnInfo(name = "api_id")
    public Integer apiId;
    public String name;
    public Integer servings;
    public String image;

    @Ignore
    public Recipe(int apiId, String name, int servings, String image) {
        this.apiId = apiId;
        this.name = name;
        this.servings = servings;
        this.image = image;
    }

    Recipe(int id, int apiId, String name, int servings, String image) {
        this.id = id;
        this.apiId = apiId;
        this.name = name;
        this.servings = servings;
        this.image = image;
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
}
