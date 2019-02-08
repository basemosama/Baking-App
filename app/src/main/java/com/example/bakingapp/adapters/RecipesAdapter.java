package com.example.bakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bakingapp.R;
import com.example.bakingapp.objects.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipesViewHolder> {
    private Context context;
    private List<Recipe> recipes;
    private RecipeClickListener recipeClickListener;

    public interface RecipeClickListener{
        void onRecipeClickListener(int positiom);
    }


    public RecipesAdapter(Context context, List<Recipe> recipes,RecipeClickListener recipeClickListener) {
        this.context = context;
        this.recipes = recipes;
        this.recipeClickListener=recipeClickListener;
    }


    @NonNull
    @Override
    public RecipesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.recipe_item,viewGroup,false);
        RecipesViewHolder recipesViewHolder=new RecipesViewHolder(view);
        return recipesViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecipesViewHolder recipesViewHolder, int position) {
        recipesViewHolder.bind(position);
    }


    @Override
    public int getItemCount() {
        if(recipes==null){
            return 0;
        }
        return recipes.size();
    }


    class RecipesViewHolder extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener {
        ImageView recipeImage;
        TextView recipeName;
        private RecipesViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeImage=itemView.findViewById(R.id.recipe_image);
            recipeName=itemView.findViewById(R.id.recipe_name);
            itemView.setOnClickListener(this);
        }

        private void bind (int position){
            recipeName.setText(recipes.get(position).getName());
            String imageUrl=recipes.get(position).getImage();
            if(imageUrl.isEmpty()){
                Picasso.get().load(R.drawable.food_error).into(recipeImage);
            }else{
            Picasso.get()
                    .load(imageUrl)
                    .error(R.drawable.food_error)
                    .placeholder(R.drawable.food_error)
                    .into(recipeImage);
        }
        }


        @Override
        public void onClick(View view) {
            recipeClickListener.onRecipeClickListener(getAdapterPosition());
        }
    }


    public void updateAdapter(List<Recipe> newRecipes){
        recipes.clear();
        recipes.addAll(newRecipes);
        notifyDataSetChanged();
    }

    public List<Recipe> getRecipes(){
        return recipes;
    }

    }



