package com.cicconi.recipes.viewmodel;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.cicconi.recipes.database.Step;
import com.cicconi.recipes.repository.StepRepository;

public class StepDetailsViewModel extends ViewModel {

    private static final String TAG = StepDetailsViewModel.class.getSimpleName();

    private StepRepository stepRepository;

    private LiveData<Step> previousStep;
    private LiveData<Step> nextStep;
    private Long recipeId;
    private int stepId;

    StepDetailsViewModel(@NonNull Context context, Step step) {
        stepRepository = new StepRepository(context);
        recipeId = step.recipeId;
        stepId = step.stepId;
    }

    public LiveData<Step> getPreviousStep() {
        stepId = stepId - 1;
        return stepRepository.getStepByRecipeAndStepId(recipeId, stepId);
    }

    public LiveData<Step> getNextStep() {
        stepId = stepId + 1;
        return stepRepository.getStepByRecipeAndStepId(recipeId, stepId);
    }
}
