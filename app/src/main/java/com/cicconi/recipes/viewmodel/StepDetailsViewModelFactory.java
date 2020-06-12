package com.cicconi.recipes.viewmodel;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.cicconi.recipes.database.Step;

public class StepDetailsViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final Context context;
    private final Step step;

    public StepDetailsViewModelFactory(Context context, Step step) {
        this.context = context;
        this.step = step;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new StepDetailsViewModel(context, step);
    }
}
