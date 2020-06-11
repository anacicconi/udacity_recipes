package com.cicconi.recipes.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import com.cicconi.recipes.database.AppDatabase;
import com.cicconi.recipes.database.Step;
import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

public class StepRepository {
    private static final String TAG = StepRepository.class.getSimpleName();

    private static AppDatabase mDb;

    public StepRepository(Context context) {
        mDb = AppDatabase.getInstance(context);
    }

    public LiveData<List<Step>> getSteps() {
        return mDb.stepDAO().loadAllSteps();
    }

    public LiveData<List<Step>> getStepsForARecipe(int recipeId) {
        return mDb.stepDAO().loadStepsByRecipeId(recipeId);
    }

    public Completable addStep(Step step) {
        return mDb.stepDAO().insertStep(step)
            .subscribeOn(Schedulers.io())
            .onErrorComplete();
    }
}
