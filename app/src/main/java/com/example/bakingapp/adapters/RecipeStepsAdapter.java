package com.example.bakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bakingapp.R;
import com.example.bakingapp.objects.RecipeSteps;

import java.util.List;

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsAdapter.RecipeStepsViewHolder> {
    private Context context;
    private List<RecipeSteps> recipeSteps;
    private OnStepClickListener onStepClickListener;

    public interface OnStepClickListener{
        void onItemClickListener(int position);
    }

    public RecipeStepsAdapter(Context context, List<RecipeSteps> recipeSteps, OnStepClickListener onStepClickListener) {
        this.context = context;
        this.recipeSteps = recipeSteps;
        this.onStepClickListener=onStepClickListener;
    }

    @NonNull
    @Override
    public RecipeStepsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
     View view=LayoutInflater.from(context)
             .inflate(R.layout.recipe_part,viewGroup,false);
        return new RecipeStepsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeStepsViewHolder recipeStepsViewHolder, int position) {
        recipeStepsViewHolder.bind(position);
    }

    @Override
    public int getItemCount() {
        if(recipeSteps==null){
            return 0;
        }
        return recipeSteps.size();
    }

    class RecipeStepsViewHolder extends RecyclerView.ViewHolder implements RecyclerView.OnClickListener{

        TextView stepText;
        private RecipeStepsViewHolder(@NonNull View itemView) {
            super(itemView);
            stepText=itemView.findViewById(R.id.step_text);
            itemView.setOnClickListener(this);
        }

        private void bind(int position){
            String stepDescription=recipeSteps.get(position).getShortDescription();
            stepText.setText(stepDescription);

        }

        @Override
        public void onClick(View view) {
            onStepClickListener.onItemClickListener(getAdapterPosition());
        }
    }
}
