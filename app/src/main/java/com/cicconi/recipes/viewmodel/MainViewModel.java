package com.cicconi.recipes.viewmodel;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.cicconi.recipes.database.Recipe;
import com.cicconi.recipes.repository.RecipeRepository;
import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<Recipe>> recipes;

    public MainViewModel(@NonNull Application application) {
        super(application);
        Context context = application.getApplicationContext();
        RecipeRepository recipeRepository = new RecipeRepository(context);

        recipes = recipeRepository.getLocalRecipes();
    }

    public LiveData<List<Recipe>> getRecipes() {
        return recipes;
    }
}
