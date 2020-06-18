package com.cicconi.recipes.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import io.reactivex.Completable;
import java.util.List;

@Dao
public interface IngredientDAO {

    @Query("SELECT * FROM ingredient ORDER BY id")
    LiveData<List<Ingredient>> loadAllIngredients();

    @Query("SELECT * FROM ingredient WHERE recipe_id = :recipeId ORDER BY id")
    LiveData<List<Ingredient>> loadIngredientsByRecipeId(int recipeId);

    @Query("SELECT * FROM ingredient WHERE recipe_id = :recipeId ORDER BY id")
    List<Ingredient> loadIngredientsByRecipeIdForWidget(int recipeId);

    @Query("SELECT * FROM ingredient WHERE id = :id")
    LiveData<Ingredient> loadIngredientById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertIngredient(Ingredient ingredient);

    @Query("DELETE FROM ingredient WHERE id = :id")
    Completable deleteIngredient(int id);
}
