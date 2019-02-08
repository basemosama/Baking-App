package com.example.bakingapp;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bakingapp.constants.Constants;
import com.example.bakingapp.fragment.RecipeStepsFragment;
import com.example.bakingapp.objects.RecipeIngredient;
import com.example.bakingapp.objects.Recipe;
import com.example.bakingapp.objects.RecipeSteps;

import java.util.ArrayList;
import java.util.List;

public class RecipeActivity extends AppCompatActivity  {

    boolean isExpanded=false;
    Recipe currentRecipe;
    List<RecipeSteps> recipeSteps;
    private CardView cardView;
    private TextView ingredientsDetails;
    List<RecipeIngredient> recipeIngredients;
    private ImageView cardArrow;
    private FrameLayout stepsFragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        ingredientsDetails=(TextView)findViewById(R.id.ingredients_details);
        cardView=(CardView)findViewById(R.id.ingredients_cardview);
        cardArrow=(ImageView)findViewById(R.id.card_arrow);
        stepsFragmentContainer=(FrameLayout)findViewById(R.id.recipe_steps_container);

       currentRecipe= (Recipe) getIntent().getParcelableExtra(Constants.RECIPE_PARTS_INTENT_EXTRA);
        Log.i("myTag",currentRecipe.getName());

        setTitle(currentRecipe.getName());
        recipeSteps=currentRecipe.getSteps();
        recipeIngredients=currentRecipe.getIngredients();
        String currentIngredents=getRecipeIngredients(recipeIngredients);
        ingredientsDetails.setText(currentIngredents);
        RecipeStepsFragment recipeStepsFragment=new RecipeStepsFragment();
        ArrayList<RecipeSteps> stepsArrayList =new ArrayList<>(recipeSteps);
        recipeStepsFragment.setRecipeSteps(stepsArrayList);
        final FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.recipe_steps_container,recipeStepsFragment)
                .commit();


        cardView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (isExpanded) {
                    ingredientsDetails.setVisibility(View.GONE);
                    cardArrow.setImageResource(R.drawable.arrow_down);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                       TransitionManager.beginDelayedTransition(stepsFragmentContainer);
                    }

                    isExpanded = false;
                } else {
                    ingredientsDetails.setVisibility(View.VISIBLE);
                    cardArrow.setImageResource(R.drawable.arrow_up);
                    isExpanded = true;

                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    TransitionManager.beginDelayedTransition(cardView);
                }


            }});



    }
    public String getRecipeIngredients(List<RecipeIngredient> recipeIngredients){
        StringBuilder ingredientsText=new StringBuilder();
        for(RecipeIngredient recipeIngredient : recipeIngredients){
            ingredientsText.append(recipeIngredient.getQuantity()+" ");
            ingredientsText.append(recipeIngredient.getMeasure());
            ingredientsText.append(" of ");
            ingredientsText.append(recipeIngredient.getIngredient()+ "\n");
        }

        return ingredientsText.toString();
    }

}
