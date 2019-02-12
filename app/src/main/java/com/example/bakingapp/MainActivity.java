package com.example.bakingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.bakingapp.adapters.RecipesAdapter;
import com.example.bakingapp.constants.Constants;
import com.example.bakingapp.objects.Recipe;
import com.example.bakingapp.objects.SimpleIdlingResource;
import com.example.bakingapp.utility.RecipeClient;

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
    @Nullable
    SimpleIdlingResource simpleIdlingResource=getSimpleIdlingResource();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpUi();
        getRecipes();
    }

    @Override
    public void onRecipeClickListener(int position) {
        Intent intent =new Intent(this,RecipeActivity.class);
        intent.putExtra(Constants.RECIPE_PARTS_INTENT_EXTRA,myRecipes.get(position));
        startActivity(intent);
    }

    private void setUpUi(){
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

    }

    public void getRecipes(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.RECIPES_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RecipeClient client = retrofit.create(RecipeClient.class);

        Call<List<Recipe>> recipeCall=client.getRecipes();
        if (simpleIdlingResource != null) {
            simpleIdlingResource.setIdleState(false);
        }

        recipeCall.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                List<Recipe> recipes=response.body();
                recipesAdapter.updateAdapter(recipes);
                saveRecipe();
                if(simpleIdlingResource!=null)
                simpleIdlingResource.setIdleState(true);
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Toast.makeText(MainActivity.this, R.string.recipe_load_error_message,Toast.LENGTH_SHORT).show();
                if(simpleIdlingResource!=null)
                    simpleIdlingResource.setIdleState(true);

            }
        });

    }
    public void saveRecipe(){
        SharedPreferences sharedPreferences=getSharedPreferences(Constants.INGREDIENTS_PREFRENCE_NAME,MODE_PRIVATE);
        if(myRecipes !=null&& myRecipes.size()>0){
        sharedPreferences.edit().putString(Constants.RECIPE_NAME_PREFERENCE,myRecipes.get(0).getName()).apply();
        String ingredients=RecipeActivity.getRecipeIngredients(myRecipes.get(0).getIngredients());
        sharedPreferences.edit().putString(Constants.RECIPE_INGREDIENTS_PREFRENCE,ingredients).apply();
        }
    }

    @VisibleForTesting
    @Nullable
    public SimpleIdlingResource getSimpleIdlingResource(){
        if(simpleIdlingResource==null){
            simpleIdlingResource=new SimpleIdlingResource();
        }
        return simpleIdlingResource;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
