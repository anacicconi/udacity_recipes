package com.cicconi.recipes;

import android.content.Context;
import android.content.Intent;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import com.cicconi.recipes.database.Step;
import com.google.android.exoplayer2.SimpleExoPlayer;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class) public class StepDetailsActivityTest {

    private static final String RECIPE_NAME = "Brownies";
    private static final String STEP_INSTRUCTION = "Recipe Introduction";
    private static final String STEP_DESCRIPTION = "description";
    private static final String STEP_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffdc33_-intro-brownies/-intro-brownies.mp4";

    @Rule
    public ActivityTestRule<StepDetailsActivity> mActivityTestRule =
        new ActivityTestRule<StepDetailsActivity>(StepDetailsActivity.class) {
            @Override
            protected Intent getActivityIntent() {
                Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
                Intent result = new Intent(targetContext, StepDetailsActivity.class);
                result.putExtra(Constants.EXTRA_STEP, mockStep());
                result.putExtra(Constants.EXTRA_RECIPE_NAME, RECIPE_NAME);
                result.putExtra(Constants.EXTRA_STEP_COUNT, 0);
                result.putExtra(Constants.EXTRA_IS_TABLET, false);
                return result;
            }
        };

    @Test
    public void verifyStepData_isPresent() {
        onView(withId(R.id.tv_recipe_title_step_details)).check(matches(withText(RECIPE_NAME)));
        onView(withId(R.id.tv_step_instruction)).check(matches(withText(STEP_INSTRUCTION)));
        // check player is initialized
        onView(allOf(withId(R.id.pv_step_video), withClassName(is(SimpleExoPlayer.class.getName()))));
    }

    private Step mockStep() {
        return new Step(STEP_DESCRIPTION, STEP_INSTRUCTION, STEP_URL, null, 1, 1L);
    }
}
