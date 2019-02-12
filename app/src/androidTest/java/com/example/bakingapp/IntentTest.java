package com.example.bakingapp;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
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
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class IntentTest {
    private SimpleIdlingResource simpleIdlingResource;


    @Rule
    public IntentsTestRule<MainActivity> intentsTestRule=new IntentsTestRule<>(MainActivity.class);

    @Before
    public void registerIdlingResource(){
        simpleIdlingResource=intentsTestRule.getActivity().getSimpleIdlingResource();
        IdlingRegistry.getInstance().register(simpleIdlingResource);

    }


    @Before
    public void stubAllExternalIntents(){
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }

    @Test
    public void recipeIntentTest(){
        onView(withId(R.id.recipes_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        intended(hasComponent(RecipeActivity.class.getName()));
    }

    @Test
    public void stepIntentTest(){
        onView(withId(R.id.recipes_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        onView(withId(R.id.recipe_steps_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        intended(allOf(
                hasExtra(Constants.RECIPE_STEPS_POSITION_INTENT_EXTRA,0),
                hasComponent(StepActivity.class.getName())));
    }


    @After
    public void unRegisterIdlingResource(){
        if(simpleIdlingResource!=null) {
            IdlingRegistry.getInstance().unregister(simpleIdlingResource);
        }
    }

}
