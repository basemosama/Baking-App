package com.example.bakingapp;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.bakingapp.constants.Constants;
import com.example.bakingapp.objects.SimpleIdlingResource;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MainUiTest {

    private SimpleIdlingResource simpleIdlingResource;
    @Rule
    public ActivityTestRule<MainActivity>activityTestRule=
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void registerIdlingResource(){
        simpleIdlingResource=activityTestRule.getActivity().getSimpleIdlingResource();
        IdlingRegistry.getInstance().register(simpleIdlingResource);

    }


    @Test
    public void recipesTest(){
        onView(withId(R.id.recipes_rv)).perform(RecyclerViewActions.scrollToPosition(0));
        onView(withText(Constants.RECIPE_NAME_TEST1)).check(matches(isDisplayed()));
        onView(withId(R.id.recipes_rv)).perform(RecyclerViewActions.scrollToPosition(1));
        onView(withText(Constants.RECIPE_NAME_TEST2)).check(matches(isDisplayed()));
        onView(withId(R.id.recipes_rv)).perform(RecyclerViewActions.scrollToPosition(2));
        onView(withText(Constants.RECIPE_NAME_TEST3)).check(matches(isDisplayed()));
        onView(withId(R.id.recipes_rv)).perform(RecyclerViewActions.scrollToPosition(3));
        onView(withText(Constants.RECIPE_NAME_TEST4)).check(matches(isDisplayed()));
    }


    @Test
    public void recipesClickTest(){
        onView(withId(R.id.recipes_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        onView(withId(R.id.ingredients)).check(matches(withText(Constants.RECIPE_INGREDIENT_NAME_TEST)));
        onView(withId(R.id.recipe_steps_rv)).perform(RecyclerViewActions.scrollToPosition(0));
        onView(withText(Constants.RECIPE_STEP_NAME_TEST1)).check(matches(isDisplayed()));
        onView(withId(R.id.recipe_steps_rv)).perform(RecyclerViewActions.scrollToPosition(1));
        onView(withText(Constants.RECIPE_STEP_NAME_TEST2)).check(matches(isDisplayed()));
        onView(withId(R.id.recipe_steps_rv)).perform(RecyclerViewActions.scrollToPosition(2));
        onView(withText(Constants.RECIPE_STEP_NAME_TEST3)).check(matches(isDisplayed()));
        onView(withId(R.id.recipe_steps_rv)).perform(RecyclerViewActions.scrollToPosition(3));
        onView(withText(Constants.RECIPE_STEP_NAME_TEST4)).check(matches(isDisplayed()));
        onView(withId(R.id.recipe_steps_rv)).perform(RecyclerViewActions.scrollToPosition(4));
        onView(withText(Constants.RECIPE_STEP_NAME_TEST5)).check(matches(isDisplayed()));
        onView(withId(R.id.recipe_steps_rv)).perform(RecyclerViewActions.scrollToPosition(5));
        onView(withText(Constants.RECIPE_STEP_NAME_TEST6)).check(matches(isDisplayed()));
        onView(withId(R.id.recipe_steps_rv)).perform(RecyclerViewActions.scrollToPosition(6));
        onView(withText(Constants.RECIPE_STEP_NAME_TEST7)).check(matches(isDisplayed()));
    }

    @Test
    public void stepClickTestAndNextAndPreviousClickTest(){
        onView(withId(R.id.recipes_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(3,click()));
        onView(withId(R.id.recipe_steps_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(3,click()));
        onView(withId(R.id.step_description)).check(matches(withText(Constants.RECIPE_STEP_DESCRIPTION_TEST1)));
        onView(withId(R.id.next)).perform(click());
        onView(withId(R.id.step_description)).check(matches(withText(Constants.RECIPE_STEP_DESCRIPTION_TEST2)));
        onView(withId(R.id.previous)).perform(click());
        onView(withId(R.id.step_description)).check(matches(withText(Constants.RECIPE_STEP_DESCRIPTION_TEST1)));
    }

    @After
    public void unRegisterIdlingResource(){
        if(simpleIdlingResource!=null) {
            IdlingRegistry.getInstance().unregister(simpleIdlingResource);
        }
    }





}
