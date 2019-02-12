package com.example.bakingapp.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bakingapp.R;
import com.example.bakingapp.adapters.RecipeStepsAdapter;
import com.example.bakingapp.objects.RecipeSteps;

import java.util.ArrayList;

public class RecipeStepsFragment extends Fragment implements RecipeStepsAdapter.OnStepClickListener {
    ArrayList<RecipeSteps> recipeSteps;
    String stepIngredientsText="";
    boolean isExpanded=false;
    ImageView cardArrow;
    private CardView cardView;
    public StepClickListener mStepClickListener;
    private TextView stepIngredients;
    private RecipeStepsAdapter recipeStepsAdapter;
    private RecyclerView recipesRecyclerView;

    public RecipeStepsFragment() {
    }

    public interface StepClickListener{
        void OnStepClickListener(int position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
        mStepClickListener=(StepClickListener)context;
    }catch (Exception e){
            throw new ClassCastException(context.toString() +" must implement stepClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_steps,container,false);
        stepIngredients=view.findViewById(R.id.ingredients_details);
        stepIngredients.setText(stepIngredientsText);

         cardView=view.findViewById(R.id.ingredients_cardview);
         cardArrow=view.findViewById(R.id.card_arrow);
         recipesRecyclerView= view.findViewById(R.id.recipe_steps_rv);
         recipeStepsAdapter =new RecipeStepsAdapter(getContext(),recipeSteps,this);
        recipesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recipesRecyclerView.setAdapter(recipeStepsAdapter);


        cardView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (isExpanded) {
                    stepIngredients.setVisibility(View.GONE);
                    cardArrow.setImageResource(R.drawable.arrow_down);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        TransitionManager.beginDelayedTransition(recipesRecyclerView);
                    }

                    isExpanded = false;
                } else {
                    stepIngredients.setVisibility(View.VISIBLE);
                    cardArrow.setImageResource(R.drawable.arrow_up);
                    isExpanded = true;

                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    TransitionManager.beginDelayedTransition(cardView);
                }


            }});




        return view;
    }

    public void setRecipeSteps(ArrayList<RecipeSteps> recipeSteps) {
        this.recipeSteps = recipeSteps;
    }
    public void setStepIngredientsText(String stepIngredientsText) {
        this.stepIngredientsText = stepIngredientsText;
    }


    @Override
    public void onItemClickListener(int position) {
        mStepClickListener.OnStepClickListener(position);

    }


}
