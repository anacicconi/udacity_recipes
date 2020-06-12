package com.cicconi.recipes;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.cicconi.recipes.adapter.IngredientAdapter;
import com.cicconi.recipes.adapter.StepAdapter;
import com.cicconi.recipes.database.Ingredient;
import com.cicconi.recipes.database.Recipe;
import com.cicconi.recipes.database.Step;
import com.cicconi.recipes.viewmodel.RecipeDetailsViewModel;
import com.cicconi.recipes.viewmodel.RecipeDetailsViewModelFactory;
import java.util.List;

public class RecipeDetailsActivity extends AppCompatActivity implements StepAdapter.StepClickListener {

    private static final String TAG = RecipeDetailsActivity.class.getSimpleName();

    private RecipeDetailsViewModel mViewModel;
    private Recipe mRecipe;

    ScrollView mRecipeLayout;
    TextView mErrorMessage;
    TextView mIngredientsLabel;
    TextView mStepsLabel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        mRecipeLayout = findViewById(R.id.recipe_layout);
        mErrorMessage = findViewById(R.id.tv_error_message);
        mIngredientsLabel = findViewById(R.id.tv_ingredients_label);
        mStepsLabel = findViewById(R.id.tv_steps_label);

        Intent intent = getIntent();
            if (intent.hasExtra(Constants.EXTRA_RECIPE)) {
                mRecipe = (Recipe) intent.getExtras().getSerializable(Constants.EXTRA_RECIPE);

                if(null == mRecipe) {
                    showErrorMessage();
                } else {
                    showRecipeView();
                }
            }
    }

    private void showErrorMessage() {
        mRecipeLayout.setVisibility(View.GONE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    private void showRecipeView() {
        RecipeDetailsViewModelFactory factory = new RecipeDetailsViewModelFactory(this, mRecipe);
        mViewModel = new ViewModelProvider(this, factory).get(RecipeDetailsViewModel.class);

        loadIngredients();
        loadSteps();
    }

    private void loadIngredients() {
        RecyclerView mIngredientsRecyclerView = findViewById(R.id.recyclerview_ingredients);
        LinearLayoutManager ingredientsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mIngredientsRecyclerView.setLayoutManager(ingredientsLayoutManager);
        mIngredientsRecyclerView.setHasFixedSize(true);
        // Disable scroll
        mIngredientsRecyclerView.setNestedScrollingEnabled(false);

        IngredientAdapter mIngredientAdapter = new IngredientAdapter();
        mIngredientsRecyclerView.setAdapter(mIngredientAdapter);

        mViewModel.getIngredients().observe(this, new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(List<Ingredient> ingredients) {
                Log.i(TAG, "ingredients live data changed");
                if (!ingredients.isEmpty()) {
                    mIngredientsLabel.setVisibility(View.VISIBLE);
                    mIngredientAdapter.setIngredientData(ingredients);
                }

                // Removing observer because this data won't be updated
                mViewModel.getIngredients().removeObserver(this);
            }
        });
    }

    private void loadSteps() {
        RecyclerView mStepsRecyclerView = findViewById(R.id.recyclerview_steps);
        LinearLayoutManager stepsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mStepsRecyclerView.setLayoutManager(stepsLayoutManager);
        mStepsRecyclerView.setHasFixedSize(true);
        // Disable scroll
        mStepsRecyclerView.setNestedScrollingEnabled(false);

        StepAdapter mStepAdapter = new StepAdapter(this);
        mStepsRecyclerView.setAdapter(mStepAdapter);

        mViewModel.getSteps().observe(this, new Observer<List<Step>>() {
            @Override
            public void onChanged(List<Step> steps) {
                Log.i(TAG, "steps live data changed");
                if (!steps.isEmpty()) {
                    mStepsLabel.setVisibility(View.VISIBLE);
                    mStepAdapter.setStepData(steps);
                }

                // Removing observer because this data won't be updated
                mViewModel.getSteps().removeObserver(this);
            }
        });
    }

    @Override
    public void onStepItemClick(Step step) {
        Intent stepDetailsActivityIntent = new Intent(this, StepDetailsActivity.class);
        stepDetailsActivityIntent.putExtra(Constants.EXTRA_STEP, step);
        startActivity(stepDetailsActivityIntent);
    }
}
