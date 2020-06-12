package com.cicconi.recipes.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import com.cicconi.recipes.CategoryType;
import com.cicconi.recipes.database.AppDatabase;
import com.cicconi.recipes.database.Recipe;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

public class RecipeRepository {
    private static final String TAG = RecipeRepository.class.getSimpleName();

    private static AppDatabase mDb;

    public RecipeRepository(Context context) {
        mDb = AppDatabase.getInstance(context);
    }

    public LiveData<List<Recipe>> getLocalRecipes(CategoryType categoryType) {
        switch (categoryType) {
            case FAVORITE:
                return mDb.recipeDAO().loadFavoriteRecipes();
            default:
                return mDb.recipeDAO().loadAllRecipes();
        }
    }

    public Single<Recipe> getRecipeByApiId(int apiId) {
        return mDb.recipeDAO().loadRecipeByApiId(apiId)
            .subscribeOn(Schedulers.io());
    }

    public LiveData<Boolean> getRecipeFavoriteStatus(int id) {
        return mDb.recipeDAO().loadRecipeFavoriteStatus(id);
    }

    public Single<Long> addRecipe(Recipe recipe) {
        return mDb.recipeDAO().insertRecipe(recipe)
            .subscribeOn(Schedulers.io());
    }

    public Completable updateFavoriteRecipeStatus(int id, boolean isFavorite) {
        return mDb.recipeDAO().updateFavoriteRecipeStatus(id, isFavorite)
            .subscribeOn(Schedulers.io())
            .onErrorComplete();
    }
}
