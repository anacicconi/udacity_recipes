package com.cicconi.recipes.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import com.cicconi.recipes.database.AppDatabase;
import com.cicconi.recipes.database.Recipe;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

public class RecipeRepository {
    private static final String TAG = RecipeRepository.class.getSimpleName();

    private static AppDatabase mDb;

    public RecipeRepository(Context context) {
        mDb = AppDatabase.getInstance(context);
    }

    public LiveData<List<Recipe>> getLocalRecipes() {
        return mDb.recipeDAO().loadAllRecipes();
    }

    public Single<Recipe> getRecipeByApiId(int apiId) {
        return mDb.recipeDAO().loadRecipeByApiId(apiId)
            .subscribeOn(Schedulers.io());
    }

    public Single<Long> addRecipe(Recipe recipe) {
        return mDb.recipeDAO().insertRecipe(recipe)
            .subscribeOn(Schedulers.io());
    }
}
