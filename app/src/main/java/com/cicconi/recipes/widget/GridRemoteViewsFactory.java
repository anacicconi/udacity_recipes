package com.cicconi.recipes.widget;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.cicconi.recipes.Constants;
import com.cicconi.recipes.R;
import com.cicconi.recipes.database.Ingredient;
import com.cicconi.recipes.database.Recipe;
import com.cicconi.recipes.repository.IngredientRepository;
import com.cicconi.recipes.repository.RecipeRepository;
import java.util.ArrayList;
import java.util.List;

public class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private RecipeRepository mRecipeRepository;
    private IngredientRepository mIngredientRepository;
    private List<Recipe> mFavoriteRecipes = new ArrayList<>();

    public GridRemoteViewsFactory(Context context) {
        mContext = context;
        mRecipeRepository = new RecipeRepository(context);
        mIngredientRepository = new IngredientRepository(context);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        List<Recipe> favoriteRecipes = mRecipeRepository.getFavoriteRecipesForWidget();

        for (Recipe recipe: favoriteRecipes) {
            List<Ingredient> ingredients = mIngredientRepository.getIngredientsByRecipeIdForWidget(recipe.id);
            recipe.setIngredients(ingredients);

            mFavoriteRecipes.add(recipe);
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (mFavoriteRecipes.isEmpty()) return 0;
        return mFavoriteRecipes.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (mFavoriteRecipes == null || mFavoriteRecipes.size() == 0) return null;

        Recipe favoriteRecipe = mFavoriteRecipes.get(position);

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.recipe_app_widget_grid_item);
        views.setTextViewText(R.id.tv_recipe_title, favoriteRecipe.getName());
        List<String> ingredientNames = new ArrayList<>();
        for(Ingredient ingredient: favoriteRecipe.getIngredients()) {
            ingredientNames.add(ingredient.ingredient);
        }
        String ingredients = TextUtils.join(", ", ingredientNames);
        views.setTextViewText(R.id.tv_ingredients, ingredients);

        // Fill in the onClick PendingIntent Template using the specific recipe id
        Intent fillInIntent = new Intent();
        fillInIntent.putExtra(Constants.EXTRA_RECIPE, favoriteRecipe);
        views.setOnClickFillInIntent(R.id.app_widget_layout, fillInIntent);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1; // Treat all items in the GridView the same
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
