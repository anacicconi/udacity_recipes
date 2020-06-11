package com.cicconi.recipes.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "step")
public class Step implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "short_description")
    public String shortDescription;
    public String description;
    @ColumnInfo(name = "video_url")
    public String videoURL;
    @ColumnInfo(name = "thumbnail_url")
    public String thumbnailURL;
    @ColumnInfo(name = "recipe_id")
    public Long recipeId;

    @Ignore
    public Step(String shortDescription, String description, String videoURL, String thumbnailURL, Long recipeId) {
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
        this.recipeId = recipeId;
    }

    Step(int id, String shortDescription, String description, String videoURL, String thumbnailURL, Long recipeId) {
        this.id = id;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.thumbnailURL = thumbnailURL;
        this.recipeId = recipeId;
    }

    public int getId() {
        return id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public Long getRecipeId() { return recipeId; }

    public static class RecipeDAO {
    }
}
