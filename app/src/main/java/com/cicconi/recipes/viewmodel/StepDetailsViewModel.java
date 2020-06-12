package com.cicconi.recipes.viewmodel;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.cicconi.recipes.database.Step;
import com.cicconi.recipes.repository.StepRepository;

public class StepDetailsViewModel extends ViewModel {

    private static final String TAG = StepDetailsViewModel.class.getSimpleName();

    private LiveData<Step> previousStep;
    private LiveData<Step> nextStep;

    StepDetailsViewModel(@NonNull Context context, Step step) {
        StepRepository stepRepository = new StepRepository(context);

        previousStep = stepRepository.getStepById(step.id - 1);
        nextStep = stepRepository.getStepById(step.id + 1);
    }

    public LiveData<Step> getPreviousStep() {
        return previousStep;
    }

    public LiveData<Step> getNextStep() {
        return nextStep;
    }
}
