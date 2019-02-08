package com.example.bakingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.GridLayout;
import android.widget.Toast;

import com.example.bakingapp.adapters.RecipesAdapter;
import com.example.bakingapp.constants.Constants;
import com.example.bakingapp.objects.Recipe;
import com.example.bakingapp.utility.RecipeClient;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements RecipesAdapter.RecipeClickListener {

    RecyclerView recipesRecyclerView;
    RecipesAdapter recipesAdapter;
    List<Recipe> myRecipes=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recipesRecyclerView=findViewById(R.id.recipes_rv);
      boolean isTablet=getResources().getBoolean(R.bool.isTablet);
        if(isTablet) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
            recipesRecyclerView.setLayoutManager(gridLayoutManager);

        }else {
            LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
            recipesRecyclerView.setLayoutManager(linearLayoutManager);

        }

         recipesAdapter=new RecipesAdapter(this,myRecipes,this);
        recipesRecyclerView.setAdapter(recipesAdapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://fnbr.co/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RecipeClient client = retrofit.create(RecipeClient.class);

        Call<List<Recipe>> recipeCall=client.getRecipes(Constants.RECIPES_URL);
        recipeCall.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
              List<Recipe> recipes=response.body();
               recipesAdapter.updateAdapter(recipes);
              Log.i("My call ",new Gson().toJson(myRecipes));
                Log.i("My call2",myRecipes.get(0).getName());

            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.i("My call failed",t.getLocalizedMessage());

            }
        });
    }

    @Override
    public void onRecipeClickListener(int position) {
        Toast.makeText(this,recipesAdapter.getRecipes().get(position).getName(),Toast.LENGTH_SHORT).show();

        Intent intent =new Intent(this,RecipeActivity.class);
        intent.putExtra(Constants.RECIPE_PARTS_INTENT_EXTRA,myRecipes.get(position));
        Log.i("myTag3", String.valueOf(myRecipes.get(position).getSteps().size()));

        startActivity(intent);
    }
}
