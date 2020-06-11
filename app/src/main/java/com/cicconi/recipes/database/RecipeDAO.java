package com.cicconi.recipes.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import io.reactivex.Completable;
import io.reactivex.Single;
import java.util.List;

@Dao
public interface RecipeDAO {

    @Query("SELECT * FROM recipe ORDER BY id")
    LiveData<List<Recipe>> loadAllRecipes();

    // Not used on a view so no need for a live data
    @Query("SELECT * FROM recipe WHERE api_id = :apiId")
    Single<Recipe> loadRecipeByApiId(int apiId);

    @Query("SELECT * FROM recipe WHERE id = :id")
    LiveData<Recipe> loadRecipeById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<Long> insertRecipe(Recipe recipe);

    @Query("DELETE FROM recipe WHERE id = :id")
    Completable deleteRecipe(int id);
}
