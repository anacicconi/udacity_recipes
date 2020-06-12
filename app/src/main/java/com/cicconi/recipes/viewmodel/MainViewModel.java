package com.cicconi.recipes.viewmodel;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import com.cicconi.recipes.CategoryType;
import com.cicconi.recipes.database.Recipe;
import com.cicconi.recipes.repository.RecipeRepository;
import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();

    private final MutableLiveData<CategoryType> category = new MutableLiveData();
    private LiveData<List<Recipe>> recipes;

    public MainViewModel(@NonNull Application application) {
        super(application);
        Context context = application.getApplicationContext();
        RecipeRepository recipeRepository = new RecipeRepository(context);

        //recipes = recipeRepository.getLocalRecipes();
        // The recipes will be returned based on the value set for category
        category.setValue(CategoryType.ALL);
        recipes = Transformations.switchMap(category, (value) -> recipeRepository.getLocalRecipes(value));
    }

    public LiveData<List<Recipe>> getRecipes() {
        return recipes;
    }

    public void setCategory(CategoryType categoryType) {
        category.setValue(categoryType);
    }
}
