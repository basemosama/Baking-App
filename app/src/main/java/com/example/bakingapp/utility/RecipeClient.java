package com.example.bakingapp.utility;

import com.example.bakingapp.objects.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface RecipeClient {
    @GET
    Call<List<Recipe>> getRecipes(@Url String url);
}
