package com.cicconi.recipes;

import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Constraints;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import com.cicconi.recipes.adapter.RecipeAdapter;
import com.cicconi.recipes.database.Recipe;
import com.cicconi.recipes.viewmodel.MainViewModel;
import com.cicconi.recipes.worker.SyncRecipesWorker;
import com.facebook.stetho.Stetho;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int NUMBER_OF_COLUMNS = 1;

    private RecyclerView mRecyclerView;
    private RecipeAdapter mRecipeAdapter;
    private GridLayoutManager layoutManager;

    private MainViewModel viewModel;

    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessage;
    private TextView mNoResultsMessage;

    private Menu mMainMenu;

    private boolean loading = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Used to debug database content
        Stetho.initializeWithDefaults(this);

        synchronizeRecipes();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        mErrorMessage = findViewById(R.id.tv_error_message);
        mNoResultsMessage = findViewById(R.id.tv_no_results_message);
        mRecyclerView = findViewById(R.id.recyclerview_recipes);

        layoutManager = new GridLayoutManager(this, NUMBER_OF_COLUMNS, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mRecipeAdapter = new RecipeAdapter(this);
        mRecyclerView.setAdapter(mRecipeAdapter);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        loadRecipes();
        //handleRecyclerViewScroll();
    }

    private void synchronizeRecipes() {
        Constraints constraints = new Constraints.Builder()
            .setRequiresCharging(true)
            .build();

        PeriodicWorkRequest syncRecipesWorkRequest =
            new PeriodicWorkRequest.Builder(SyncRecipesWorker.class, 1, TimeUnit.DAYS)
                //.setConstraints(constraints)
                .build();

        WorkManager
            .getInstance(this)
            .enqueue(syncRecipesWorkRequest);
    }

    /*private void handleRecyclerViewScroll() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            if (dy > 0) {
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                // No need to handle pagination for favorite movies
                if (loading && viewModel.category != MovieCategory.FAVORITE) {
                    if ((visibleItemCount + firstVisibleItem) >= totalItemCount) {
                        loading = false;
                        if(viewModel.page != lastPage) {
                            viewModel.incrementPage();
                            viewModel.onAllMoviesSelected();
                        }
                    }
                }
            }
            }
        });
    }*/

    private void loadRecipes() {
        loadStart();

        viewModel.getRecipes().observe(this, recipes -> {
            Log.i(TAG, "recipes live data changed");
            loadFinish(recipes);
        });
    }

    private void loadStart() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    private void loadFinish(List<Recipe> recipes) {
        if (!recipes.isEmpty()) {
            loading = true;

            /*if(viewModel.page == Constants.FIRST_PAGE) {
                mRecyclerView.scrollToPosition(0);
            }*/

            showRecipeView();
            mRecipeAdapter.setRecipeData(recipes);
        } else {
            showErrorMessage();
        }
    }

    private void showRecipeView() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.INVISIBLE);
        mNoResultsMessage.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRecipeItemClick(Recipe recipe) {
        Intent recipeDetailsActivityIntent = new Intent(this, RecipeDetailsActivity.class);
        recipeDetailsActivityIntent.putExtra(Constants.EXTRA_RECIPE, recipe);
        startActivity(recipeDetailsActivityIntent);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMainMenu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, mMainMenu);

        enableMenuItem(mMainMenu.findItem(R.id.action_sort_popular));
        disableMenuItem(mMainMenu.findItem(R.id.action_sort_rating));
        disableMenuItem(mMainMenu.findItem(R.id.action_sort_favorite));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sort_popular) {
            updateCategory(MovieCategory.POPULAR);
            viewModel.onAllMoviesSelected();

            enableMenuItem(mMainMenu.findItem(R.id.action_sort_popular));
            disableMenuItem(mMainMenu.findItem(R.id.action_sort_rating));
            disableMenuItem(mMainMenu.findItem(R.id.action_sort_favorite));

            return true;
        }

        if (id == R.id.action_sort_rating) {
            updateCategory(MovieCategory.TOP_RATED);
            viewModel.onAllMoviesSelected();

            enableMenuItem(mMainMenu.findItem(R.id.action_sort_rating));
            disableMenuItem(mMainMenu.findItem(R.id.action_sort_popular));
            disableMenuItem(mMainMenu.findItem(R.id.action_sort_favorite));

            return true;
        }

        if (id == R.id.action_sort_favorite) {
            updateCategory(MovieCategory.FAVORITE);
            viewModel.onFavoriteMoviesSelected();

            enableMenuItem(mMainMenu.findItem(R.id.action_sort_favorite));
            disableMenuItem(mMainMenu.findItem(R.id.action_sort_popular));
            disableMenuItem(mMainMenu.findItem(R.id.action_sort_rating));

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void enableMenuItem(MenuItem item) {
        SpannableString spanString = new SpannableString(item.getTitle().toString());
        spanString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorAccent)), 0, spanString.length(), 0);
        item.setTitle(spanString);
    }

    private void disableMenuItem(MenuItem item) {
        SpannableString spanString = new SpannableString(item.getTitle().toString());
        spanString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorSecondaryText)), 0, spanString.length(), 0);
        item.setTitle(spanString);
    }*/
}
