package com.cicconi.recipes;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import androidx.test.rule.ActivityTestRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class) public class MainActivityTest {

    private static final String RECIPE_NAME = "Brownies";

    @Rule public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void itemWithTextBrownies_isPresent() {
        onView(withText(RECIPE_NAME)).check(matches(isDisplayed()));
    }

    @Test
    public void clickOnItem_opensRecipeDetailsActivity() {
        onView(withId(R.id.recyclerview_recipes)).perform(
            RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // check that the text view displays the name of the correct recipe at the position 0
        onView(withId(R.id.tv_recipe_title_recipe_details)).check(matches(withText(RECIPE_NAME)));
    }
}
