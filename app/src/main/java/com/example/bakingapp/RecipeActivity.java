package com.example.bakingapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.bakingapp.constants.Constants;
import com.example.bakingapp.fragment.PlayerViewFragment;
import com.example.bakingapp.fragment.RecipeStepsFragment;
import com.example.bakingapp.fragment.StepDescriptionFragment;
import com.example.bakingapp.objects.Recipe;
import com.example.bakingapp.objects.RecipeIngredient;
import com.example.bakingapp.objects.RecipeSteps;

import java.util.ArrayList;
import java.util.List;

public class RecipeActivity extends AppCompatActivity implements RecipeStepsFragment.StepClickListener {

    Recipe currentRecipe;
    List<RecipeSteps> recipeSteps;
    List<RecipeIngredient> recipeIngredients;
     FragmentManager stepsFragmentManager, stepDescriptionFragmentManager,playerFragmentManager;
     StepDescriptionFragment stepDescriptionFragment;
     PlayerViewFragment playerViewFragment;
     boolean isLandscape=false;
     boolean isTablet=false;
     RecipeStepsFragment recipeStepsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);


        setUpUi();

            if (isLandscape && isTablet) {
                //Using 2 PaneLayout when using Tablet on LandScape
                setUpStepsFragment(R.id.recipe_steps_tablet_container);
                setUpPlayerFragment();
                setUpStepDescriptionFragment();
            } else {
                setUpStepsFragment(R.id.recipe_steps_container);
            }




    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if(isTablet){
            stepsFragmentManager.beginTransaction()
                    .remove(recipeStepsFragment)
                    .commit();
            if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
                setUpStepsFragment(R.id.recipe_steps_tablet_container);
                setUpPlayerFragment();
               setUpStepDescriptionFragment();

            }else if(newConfig.orientation==Configuration.ORIENTATION_PORTRAIT){

                playerFragmentManager.beginTransaction().remove(playerViewFragment).commit();
                stepDescriptionFragmentManager.beginTransaction().remove(stepDescriptionFragment).commit();

                setUpStepsFragment(R.id.recipe_steps_container);
            }

        }


    }

    public static String getRecipeIngredients(List<RecipeIngredient> recipeIngredients){
        StringBuilder ingredientsText=new StringBuilder();
        for(RecipeIngredient recipeIngredient : recipeIngredients){
            ingredientsText.append(recipeIngredient.getQuantity()+" ");
            ingredientsText.append(recipeIngredient.getMeasure());
            ingredientsText.append(" of ");
            ingredientsText.append(recipeIngredient.getIngredient()+ "\n");
        }

        return ingredientsText.toString();
    }

    private void setUpUi(){
        isLandscape=(getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE);
        isTablet=getResources().getBoolean(R.bool.isTablet);
        currentRecipe= getIntent().getParcelableExtra(Constants.RECIPE_PARTS_INTENT_EXTRA);
        setTitle(currentRecipe.getName());
        recipeSteps=currentRecipe.getSteps();
        recipeIngredients=currentRecipe.getIngredients();
    }

    private void setUpStepsFragment(int id){
         recipeStepsFragment=new RecipeStepsFragment();
        ArrayList<RecipeSteps> stepsArrayList =new ArrayList<>(recipeSteps);
        recipeStepsFragment.setRecipeSteps(stepsArrayList);
        recipeStepsFragment.setStepIngredientsText(getRecipeIngredients(recipeIngredients));

         stepsFragmentManager=getSupportFragmentManager();
        stepsFragmentManager.beginTransaction()
                .add(id,recipeStepsFragment)
                .commit();
    }
    private void setUpStepDescriptionFragment(){
        stepDescriptionFragmentManager=getSupportFragmentManager();
        stepDescriptionFragment=new StepDescriptionFragment();
        stepDescriptionFragment.setStepDescriptionText(recipeSteps.get(0).getDescription());

        stepDescriptionFragmentManager.beginTransaction()
                .add(R.id.step_description_tablet_container,stepDescriptionFragment)
                .commit();

    }

    private void setUpPlayerFragment(){
        playerFragmentManager=getSupportFragmentManager();
        playerViewFragment=new PlayerViewFragment();
        playerViewFragment.setContext(this);
        playerViewFragment.setUrlText(recipeSteps.get(0).getVideoURL());
        playerViewFragment.setThumbnailImage(recipeSteps.get(0).getThumbnailURL());
        playerFragmentManager.beginTransaction()
                .add(R.id.step_player_tablet_container,playerViewFragment)
                .commit();

    }

    @Override
    public void OnStepClickListener(int position) {
        if(isLandscape && isTablet) {
            playerViewFragment.updatePlayer(recipeSteps.get(position).getVideoURL(),recipeSteps.get(position).getThumbnailURL());
            stepDescriptionFragment.setDescriptionText(recipeSteps.get(position).getDescription());

        }else{
            ArrayList<RecipeSteps> stepsArrayList =new ArrayList<>(recipeSteps);
            Intent intent=new Intent(this, StepActivity.class);
        intent.putExtra(Constants.RECIPE_STEPS_INTENT_EXTRA,stepsArrayList);
        intent.putExtra(Constants.RECIPE_STEPS_POSITION_INTENT_EXTRA,position);
        startActivity(intent);

        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
