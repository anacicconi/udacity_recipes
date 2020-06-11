package com.cicconi.recipes.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import com.cicconi.recipes.database.AppDatabase;
import com.cicconi.recipes.database.Ingredient;
import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

public class IngredientRepository {
    private static final String TAG = IngredientRepository.class.getSimpleName();

    private static AppDatabase mDb;

    public IngredientRepository(Context context) {
        mDb = AppDatabase.getInstance(context);
    }

    public LiveData<List<Ingredient>> getIngredients() {
        return mDb.ingredientDAO().loadAllIngredients();
    }

    public LiveData<List<Ingredient>> getIngredientsForARecipe(int recipeId) {
        return mDb.ingredientDAO().loadIngredientsByRecipeId(recipeId);
    }

    public Completable addIngredient(Ingredient ingredient) {
        return mDb.ingredientDAO().insertIngredient(ingredient)
            .subscribeOn(Schedulers.io())
            .onErrorComplete();
    }
}
