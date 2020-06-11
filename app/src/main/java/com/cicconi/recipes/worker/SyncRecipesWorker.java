package com.cicconi.recipes.worker;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.cicconi.recipes.database.Ingredient;
import com.cicconi.recipes.database.Step;
import com.cicconi.recipes.model.IngredientResponse;
import com.cicconi.recipes.model.RecipeResponse;
import com.cicconi.recipes.model.StepResponse;
import com.cicconi.recipes.network.RetrofitBuilder;
import com.cicconi.recipes.repository.IngredientRepository;
import com.cicconi.recipes.repository.RecipeRepository;
import com.cicconi.recipes.repository.StepRepository;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;

public class SyncRecipesWorker extends Worker {

    private static final String TAG = SyncRecipesWorker.class.getSimpleName();

    private RecipeRepository mRecipeRepository;
    private StepRepository mStepRepository;
    private IngredientRepository mIngredientRepository;
    private CompositeDisposable mCompositeDisposable;

    public SyncRecipesWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        mRecipeRepository = new RecipeRepository(context);
        mStepRepository = new StepRepository(context);
        mIngredientRepository = new IngredientRepository(context);
        mCompositeDisposable = new CompositeDisposable();
    }

    @NonNull
    @Override
    public Result doWork() {
        Disposable disposable = getRemoteRecipes()
            .doOnNext(i -> Log.d(TAG, String.format("Thread doWork: %s", Thread.currentThread().getName())))
            .subscribe(recipeResponse -> {
                    for (RecipeResponse remoteRecipe: recipeResponse) {
                        getLocalRecipe(remoteRecipe);
                    }
                },
                Throwable::printStackTrace,
                () -> Log.d(TAG, "Synch completed")
            );

        mCompositeDisposable.add(disposable);

        // TODO: succes here or on rx?
        return Result.success();

    }

    private Observable<List<RecipeResponse>> getRemoteRecipes() {
        return RetrofitBuilder.getClient().getRecipes()
            .subscribeOn(Schedulers.io())
            .onErrorReturn(e -> getEmptyApiResponse());
    }

    private void getLocalRecipe(RecipeResponse remoteRecipe) {
        Disposable disposable = mRecipeRepository.getRecipeByApiId(remoteRecipe.getId())
            .doOnError(error -> addRecipe(remoteRecipe))
            .subscribe(
                localRecipe -> {
                    if(localRecipe == null) {
                        addRecipe(remoteRecipe);
                    }
                },
                error -> Log.d(TAG, "New recipe received")
            );

        mCompositeDisposable.add(disposable);
    }

    private List<RecipeResponse> getEmptyApiResponse() {
        return new ArrayList<>();
    }

    private void addRecipe(RecipeResponse remoteRecipe) {
        Disposable disposable = mRecipeRepository.addRecipe(remoteRecipe.toRecipe())
            .subscribe(recipeId -> {
                Log.d(TAG, "New recipe added");

                List<StepResponse> stepsReponse = remoteRecipe.getSteps();

                for(StepResponse stepResponse: stepsReponse) {
                    addStep(stepResponse.toStep(recipeId));
                }

                List<IngredientResponse> ingredientResponses = remoteRecipe.getIngredients();

                for(IngredientResponse ingredientResponse: ingredientResponses) {
                    addIngredient(ingredientResponse.toIngredient(recipeId));
                }
            }, Throwable::printStackTrace);

        mCompositeDisposable.add(disposable);
    }

    private void addStep(Step step) {
        Disposable disposable = mStepRepository.addStep(step)
            .subscribe(
                () -> Log.d(TAG, "New step added"),
                Throwable::printStackTrace
            );

        mCompositeDisposable.add(disposable);
    }

    private void addIngredient(Ingredient ingredient) {
        Disposable disposable = mIngredientRepository.addIngredient(ingredient)
            .subscribe(
                () -> Log.d(TAG, "New ingredient added"),
                Throwable::printStackTrace
            );

        mCompositeDisposable.add(disposable);
    }

    @Override
    public void onStopped() {
        mCompositeDisposable.dispose();
    }
}
