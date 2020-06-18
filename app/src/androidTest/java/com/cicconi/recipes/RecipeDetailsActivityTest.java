package com.cicconi.recipes;

import android.content.Context;
import android.content.Intent;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.cicconi.recipes.database.Recipe;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class) public class RecipeDetailsActivityTest {

    private static final String RECIPE_NAME = "Brownies";
    private static final String STEP_INSTRUCTION = "Recipe Introduction";

    @Rule
    public ActivityTestRule<RecipeDetailsActivity> mActivityTestRule =
        new ActivityTestRule<RecipeDetailsActivity>(RecipeDetailsActivity.class) {
            @Override
            protected Intent getActivityIntent() {
                Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
                Intent result = new Intent(targetContext, RecipeDetailsActivity.class);
                result.putExtra(Constants.EXTRA_RECIPE, mockRecipe());
                return result;
            }
        };

    @Test
    public void verifyRecipeData_isPresent() {
        onView(withId(R.id.tv_recipe_title_recipe_details)).check(matches(withText(RECIPE_NAME)));
    }

    @Test
    public void clickOnItem_opensStepDetailsActivity() {
        onView(withId(R.id.recyclerview_steps)).perform(
            RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // check that the text view displays the name of the correct recipe at the position 0
        onView(withId(R.id.tv_recipe_title_step_details)).check(matches(withText(RECIPE_NAME)));
        onView(withId(R.id.tv_step_instruction)).check(matches(withText(STEP_INSTRUCTION)));
    }

    private Recipe mockRecipe() {
        Recipe recipe = new Recipe(1, RECIPE_NAME, 0, null, true);
        recipe.setId(1);
        return recipe;
    }
}
