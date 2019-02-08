package com.example.bakingapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bakingapp.R;
import com.example.bakingapp.StepActivity;
import com.example.bakingapp.adapters.RecipeStepsAdapter;
import com.example.bakingapp.constants.Constants;
import com.example.bakingapp.objects.RecipeSteps;

import java.util.ArrayList;

public class RecipeStepsFragment extends Fragment implements RecipeStepsAdapter.OnStepClickListener {
    ArrayList<RecipeSteps> recipeSteps;
    public RecipeStepsFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_steps,container,false);
        RecyclerView recipesRecyclerView= view.findViewById(R.id.recipe_steps_rv);
        RecipeStepsAdapter recipeStepsAdapter =new RecipeStepsAdapter(getContext(),recipeSteps,this);
        recipesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recipesRecyclerView.setAdapter(recipeStepsAdapter);


        return view;
    }

    public void setRecipeSteps(ArrayList<RecipeSteps> recipeSteps) {
        this.recipeSteps = recipeSteps;
    }

    @Override
    public void onItemClickListener(int position) {
        Toast.makeText(getContext(),recipeSteps.get(position).getShortDescription(),Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(getContext(), StepActivity.class);
        intent.putExtra(Constants.RECIPE_STEPS_INTENT_EXTRA,recipeSteps);
        intent.putExtra(Constants.RECIPE_STEPS_POSITION_INTENT_EXTRA,position);
        startActivity(intent);

    }
}
