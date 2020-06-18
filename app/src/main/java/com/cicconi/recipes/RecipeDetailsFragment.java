package com.cicconi.recipes;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class RecipeDetailsFragment extends Fragment implements StepAdapter.StepClickListener {

    private static final String TAG = RecipeDetailsFragment.class.getSimpleName();

    private RecipeDetailsViewModel mViewModel;
    private Recipe mRecipe;

    private ScrollView mRecipeLayout;
    private TextView mErrorMessage;
    private TextView mRecipeTitle;
    private TextView mIngredientsLabel;
    private TextView mStepsLabel;
    private ImageView mFavoriteIcon;

    private View rootView;

    private int mStepsCount;

    private CompositeDisposable compositeDisposable;

    public RecipeDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_recipe_details, container, false);

        mRecipeLayout = rootView.findViewById(R.id.recipe_layout);
        mErrorMessage = rootView.findViewById(R.id.tv_error_message);
        mRecipeTitle = rootView.findViewById(R.id.tv_recipe_title);
        mIngredientsLabel = rootView.findViewById(R.id.tv_ingredients_label);
        mStepsLabel = rootView.findViewById(R.id.tv_steps_label);
        mFavoriteIcon = rootView.findViewById(R.id.iv_favorite);

        Intent intent = requireActivity().getIntent();
        if (intent.hasExtra(Constants.EXTRA_RECIPE)) {
            mRecipe = (Recipe) intent.getExtras().getSerializable(Constants.EXTRA_RECIPE);

            if(null == mRecipe) {
                showErrorMessage();
            } else {
                showRecipeView();
            }
        }

        return rootView;
    }

    private void showErrorMessage() {
        mRecipeLayout.setVisibility(View.GONE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    private void showRecipeView() {
        RecipeDetailsViewModelFactory factory = new RecipeDetailsViewModelFactory(requireContext(), mRecipe);
        mViewModel = new ViewModelProvider(this, factory).get(RecipeDetailsViewModel.class);

        mRecipeTitle.setText(mRecipe.getName());

        loadIngredients();
        loadSteps();
        handleFavoriteIcon();
    }

    private void handleFavoriteIcon() {
        mViewModel.getRecipeFavoriteStatus().observe(requireActivity(), isFavorite -> {
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
                Toast.makeText(requireActivity(), "An error occurred", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(requireActivity(), "The recipe was added to favorites", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireActivity(), "The recipe was removed from favorites", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateWidgetsFavoriteList() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(requireContext());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(requireContext(), RecipeAppWidget.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_grid_view);
    }

    private void loadIngredients() {
        RecyclerView mIngredientsRecyclerView = rootView.findViewById(R.id.recyclerview_ingredients);
        LinearLayoutManager ingredientsLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        mIngredientsRecyclerView.setLayoutManager(ingredientsLayoutManager);
        mIngredientsRecyclerView.setHasFixedSize(true);
        // Disable scroll
        mIngredientsRecyclerView.setNestedScrollingEnabled(false);

        IngredientAdapter mIngredientAdapter = new IngredientAdapter();
        mIngredientsRecyclerView.setAdapter(mIngredientAdapter);

        mViewModel.getIngredients().observe(requireActivity(), new Observer<List<Ingredient>>() {
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
        RecyclerView mStepsRecyclerView = rootView.findViewById(R.id.recyclerview_steps);
        LinearLayoutManager stepsLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        mStepsRecyclerView.setLayoutManager(stepsLayoutManager);
        mStepsRecyclerView.setHasFixedSize(true);
        // Disable scroll
        mStepsRecyclerView.setNestedScrollingEnabled(false);

        StepAdapter mStepAdapter = new StepAdapter(this);
        mStepsRecyclerView.setAdapter(mStepAdapter);

        mViewModel.getSteps().observe(requireActivity(), new Observer<List<Step>>() {
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
        Intent stepDetailsActivityIntent = new Intent(requireContext(), StepDetailsActivity.class);
        stepDetailsActivityIntent.putExtra(Constants.EXTRA_STEP, step);
        stepDetailsActivityIntent.putExtra(Constants.EXTRA_RECIPE_NAME, mRecipe.name);
        stepDetailsActivityIntent.putExtra(Constants.EXTRA_STEP_COUNT, mStepsCount);
        startActivity(stepDetailsActivityIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}
