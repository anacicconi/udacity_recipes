package com.cicconi.recipes.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import io.reactivex.Completable;
import java.util.List;

@Dao
public interface StepDAO {

    @Query("SELECT * FROM step ORDER BY id")
    LiveData<List<Step>> loadAllSteps();

    @Query("SELECT * FROM step WHERE recipe_id = :recipeId ORDER BY step_id")
    LiveData<List<Step>> loadStepsByRecipeId(int recipeId);

    @Query("SELECT * FROM step WHERE id = :id")
    LiveData<Step> loadStepById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertStep(Step step);

    @Query("DELETE FROM step WHERE id = :id")
    Completable deleteStep(int id);
}
