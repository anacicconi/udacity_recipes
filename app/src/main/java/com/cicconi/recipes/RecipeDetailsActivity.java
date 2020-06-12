package com.cicconi.recipes;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
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
import com.cicconi.recipes.widget.RecipeAppWidget;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import java.util.List;

public class RecipeDetailsActivity extends AppCompatActivity implements StepAdapter.StepClickListener {

    private static final String TAG = RecipeDetailsActivity.class.getSimpleName();

    private RecipeDetailsViewModel mViewModel;
    private Recipe mRecipe;

    ScrollView mRecipeLayout;
    TextView mErrorMessage;
    TextView mRecipeTitle;
    TextView mIngredientsLabel;
    TextView mStepsLabel;
    ImageView mFavoriteIcon;

    int mStepsCount;

    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        mRecipeLayout = findViewById(R.id.recipe_layout);
        mErrorMessage = findViewById(R.id.tv_error_message);
        mRecipeTitle = findViewById(R.id.tv_recipe_title);
        mIngredientsLabel = findViewById(R.id.tv_ingredients_label);
        mStepsLabel = findViewById(R.id.tv_steps_label);
        mFavoriteIcon = findViewById(R.id.iv_favorite);

        Intent intent = getIntent();
            if (intent.hasExtra(Constants.EXTRA_RECIPE)) {
                mRecipe = (Recipe) intent.getExtras().getSerializable(Constants.EXTRA_RECIPE);

                if(null == mRecipe) {
                    showErrorMessage();
                } else {
                    showRecipeView();
                }
            }

        compositeDisposable = new CompositeDisposable();
    }

    private void showErrorMessage() {
        mRecipeLayout.setVisibility(View.GONE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    private void showRecipeView() {
        RecipeDetailsViewModelFactory factory = new RecipeDetailsViewModelFactory(this, mRecipe);
        mViewModel = new ViewModelProvider(this, factory).get(RecipeDetailsViewModel.class);

        mRecipeTitle.setText(mRecipe.getName());

        loadIngredients();
        loadSteps();
        handleFavoriteIcon();
    }

    private void handleFavoriteIcon() {
        mViewModel.getRecipeFavoriteStatus().observe(this, isFavorite -> {
            Log.i(TAG, "isFavorite live data changed: " + isFavorite);
            setFavoriteIconColor(isFavorite);
            onFavoriteIconClick(isFavorite);
        });
    }

    private void onFavoriteIconClick(boolean isFavorite) {
        mFavoriteIcon.setOnClickListener(view -> {
           updateRecipeFavoriteStatus(isFavorite);
        });
    }

    private void updateRecipeFavoriteStatus(boolean isFavorite) {
        boolean newFavoriteStatus = !isFavorite;
        Disposable disposable = mViewModel.onFavoriteStatusUpdated(newFavoriteStatus)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError(e -> {
                e.printStackTrace();
                Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show();
            })
            .subscribe(
                () -> {
                    setFavoriteIconColor(newFavoriteStatus);
                    displayFavoriteUpdateStatusMessage(newFavoriteStatus);
                    updateWidgetsFavoriteList();
                },
                Throwable::printStackTrace
            );

        compositeDisposable.add(disposable);
    }

    private void setFavoriteIconColor(Boolean isFavorite) {
        if(isFavorite) {
            mFavoriteIcon.setColorFilter(getResources().getColor(R.color.colorFavorite));
        } else {
            mFavoriteIcon.setColorFilter(getResources().getColor(R.color.colorSecondaryText));
        }
    }

    private void displayFavoriteUpdateStatusMessage(Boolean isFavorite) {
        if(isFavorite) {
            Toast.makeText(this, "The recipe was added to favorites", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "The recipe was removed from favorites", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateWidgetsFavoriteList() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeAppWidget.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_grid_view);
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
                    mStepsCount = steps.size();
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
        stepDetailsActivityIntent.putExtra(Constants.EXTRA_RECIPE_NAME, mRecipe.name);
        stepDetailsActivityIntent.putExtra(Constants.EXTRA_STEP_COUNT, mStepsCount);
        startActivity(stepDetailsActivityIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_recipes_all) {
            Intent mainActivityIntent = new Intent(this, MainActivity.class);
            startActivity(mainActivityIntent);

            return true;
        }

        if (id == R.id.action_recipes_favorite) {
            Intent mainActivityIntent = new Intent(this, MainActivity.class);
            mainActivityIntent.putExtra(Constants.EXTRA_CATEGORY_TYPE, CategoryType.FAVORITE);
            startActivity(mainActivityIntent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}
