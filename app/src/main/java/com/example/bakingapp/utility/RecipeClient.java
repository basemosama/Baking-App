package com.example.bakingapp.utility;

import com.example.bakingapp.constants.Constants;
import com.example.bakingapp.objects.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipeClient {
    @GET(Constants.RECIPES_PATH_URL)
    Call<List<Recipe>> getRecipes();
}
