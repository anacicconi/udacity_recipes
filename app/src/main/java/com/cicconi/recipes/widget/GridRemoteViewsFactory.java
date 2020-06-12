package com.cicconi.recipes.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.cicconi.recipes.Constants;
import com.cicconi.recipes.R;
import com.cicconi.recipes.database.Recipe;
import com.cicconi.recipes.repository.RecipeRepository;
import java.util.List;

public class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private RecipeRepository mRecipeRepository;
    private List<Recipe> favoriteRecipes;

    public GridRemoteViewsFactory(Context context) {
        mContext = context;
        mRecipeRepository = new RecipeRepository(context);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        favoriteRecipes = mRecipeRepository.getFavoriteRecipesForWidget();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (favoriteRecipes.isEmpty()) return 0;
        return favoriteRecipes.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (favoriteRecipes == null || favoriteRecipes.size() == 0) return null;

        Recipe favoriteRecipe = favoriteRecipes.get(position);

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.recipe_app_widget_grid_item);
        views.setTextViewText(R.id.tv_recipe_title, favoriteRecipe.getName());

        // Fill in the onClick PendingIntent Template using the specific recipe id
        Intent fillInIntent = new Intent();
        fillInIntent.putExtra(Constants.EXTRA_RECIPE, favoriteRecipe);
        views.setOnClickFillInIntent(R.id.iv_recipe_img, fillInIntent);

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
