package com.cicconi.recipes.viewmodel;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.cicconi.recipes.database.Recipe;

public class DetailsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final Context context;

    private final Recipe recipe;

    public DetailsViewModelFactory(Context context, Recipe recipe) {
        this.context = context;
        this.recipe = recipe;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DetailsViewModel(context, recipe);
    }
}
