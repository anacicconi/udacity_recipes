package com.cicconi.recipes.viewmodel;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.cicconi.recipes.database.Ingredient;
import com.cicconi.recipes.database.Recipe;
import com.cicconi.recipes.database.Step;
import com.cicconi.recipes.repository.IngredientRepository;
import com.cicconi.recipes.repository.StepRepository;
import java.util.List;

public class DetailsViewModel extends ViewModel {

    private static final String TAG = DetailsViewModel.class.getSimpleName();

    private LiveData<List<Ingredient>> ingredients;
    private LiveData<List<Step>> steps;

    DetailsViewModel(@NonNull Context context, Recipe recipe) {
        IngredientRepository ingredientRepository = new IngredientRepository(context);
        StepRepository stepRepository = new StepRepository(context);

        ingredients = ingredientRepository.getIngredientsForARecipe(recipe.id);
        steps = stepRepository.getStepsForARecipe(recipe.id);
    }

    public LiveData<List<Ingredient>> getIngredients() {
        return ingredients;
    }

    public LiveData<List<Step>> getSteps() {
        return steps;
    }
}
