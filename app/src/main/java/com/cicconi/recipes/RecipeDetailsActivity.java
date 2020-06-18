package com.cicconi.recipes;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class RecipeDetailsActivity extends AppCompatActivity {

    private static final String TAG = RecipeDetailsActivity.class.getSimpleName();

    private boolean isTablet = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        if(findViewById(R.id.fragment_step_details_container) != null) {
            isTablet = true;
        }
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

    public boolean isTablet() {
        return isTablet;
    }
}
