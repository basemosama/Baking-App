package com.example.bakingapp;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bakingapp.constants.Constants;
import com.example.bakingapp.fragment.PlayerViewFragment;
import com.example.bakingapp.fragment.StepDescriptionFragment;
import com.example.bakingapp.objects.RecipeSteps;

import java.util.List;

public class StepActivity extends AppCompatActivity {
    List<RecipeSteps> recipeSteps;
    int stepsPosition;

    FrameLayout stepDescriptionContainer,playerViewContainer;
    TextView stepNumberTv;
    LinearLayout linearLayout;
    ImageView nextImage,previousImage;
    private StepDescriptionFragment stepDescriptionFragment;
    FragmentManager stepDescriptionFragmentManager;
    FragmentManager playerFragmentManager;
    private PlayerViewFragment playerViewFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        setUpUI();
        if(savedInstanceState==null) {
            setUpStepFragment();
            setUpPlayerFragment();
            setUpNextAndPrevious();
            setUpConfiguration(getResources().getConfiguration().orientation);
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setUpConfiguration(newConfig.orientation);

    }


    public void setUpUI(){

        stepNumberTv=findViewById(R.id.step_number);
        linearLayout= findViewById(R.id.controls_linear_layout);
        nextImage=findViewById(R.id.next);
        previousImage=findViewById(R.id.previous);
        stepDescriptionContainer=findViewById(R.id.step_description_container);
        playerViewContainer=findViewById(R.id.player_view_container);
        recipeSteps=getIntent().getParcelableArrayListExtra(Constants.RECIPE_STEPS_INTENT_EXTRA);
        stepsPosition=getIntent().getIntExtra(Constants.RECIPE_STEPS_POSITION_INTENT_EXTRA,0);
        String stepNumber="("+(stepsPosition+1)+"/"+recipeSteps.size()+")";
        stepNumberTv.setText(stepNumber);

        if(stepsPosition>=recipeSteps.size()){
            nextImage.setVisibility(View.INVISIBLE);
        }
        if(stepsPosition>=0){
            previousImage.setVisibility(View.VISIBLE);
        }

    }
    private void setUpNextAndPrevious(){
        nextImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stepsPosition++;
                if(stepsPosition>=recipeSteps.size()-1){
                    nextImage.setVisibility(View.INVISIBLE);
                }
                if(stepsPosition>0){
                    previousImage.setVisibility(View.VISIBLE);
                }
                String urlText=recipeSteps.get(stepsPosition).getVideoURL();
                String imageUrl=recipeSteps.get(stepsPosition).getThumbnailURL();

                playerViewFragment.updatePlayer(urlText,imageUrl);
                stepDescriptionFragment.setDescriptionText(recipeSteps.get(stepsPosition).getDescription());
                String stepNumber="("+(stepsPosition+1)+"/"+recipeSteps.size()+")";
                stepNumberTv.setText(stepNumber);

            }
        });
        previousImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stepsPosition--;
                if(stepsPosition<=0){
                    previousImage.setVisibility(View.INVISIBLE);
                }
                if(stepsPosition<recipeSteps.size()-1){
                    nextImage.setVisibility(View.VISIBLE);
                }
                String urlText=recipeSteps.get(stepsPosition).getVideoURL();
                String imageUrl=recipeSteps.get(stepsPosition).getThumbnailURL();

                playerViewFragment.updatePlayer(urlText,imageUrl);

                stepDescriptionFragment.setDescriptionText(recipeSteps.get(stepsPosition).getDescription());
                String stepNumber="("+(stepsPosition+1)+"/"+recipeSteps.size()+")";
                stepNumberTv.setText(stepNumber);

            }
        });

    }

    private void setUpStepFragment(){
         stepDescriptionFragmentManager=getSupportFragmentManager();
         stepDescriptionFragment=new StepDescriptionFragment();
        stepDescriptionFragment.setStepDescriptionText(recipeSteps.get(stepsPosition).getDescription());

        stepDescriptionFragmentManager.beginTransaction()
                .add(R.id.step_description_container,stepDescriptionFragment)
                .commit();

    }
    private void setUpPlayerFragment(){
        playerFragmentManager=getSupportFragmentManager();
        playerViewFragment=new PlayerViewFragment();
        playerViewFragment.setContext(this);
        playerViewFragment.setUrlText(recipeSteps.get(stepsPosition).getVideoURL());
        playerViewFragment.setThumbnailImage(recipeSteps.get(stepsPosition).getThumbnailURL());
        playerFragmentManager.beginTransaction()
                .add(R.id.player_view_container,playerViewFragment)
                .commit();

    }

    private void setUpConfiguration(int orientation){
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            playerViewContainer.setLayoutParams(new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));

            linearLayout.setVisibility(View.GONE);
            stepDescriptionContainer.setVisibility(View.GONE);

        } else if (orientation == Configuration.ORIENTATION_PORTRAIT){

            playerViewContainer.setLayoutParams(new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    getResources().getDimensionPixelSize(R.dimen.player_height)));
            linearLayout.setVisibility(View.VISIBLE);
            stepDescriptionContainer.setVisibility(View.VISIBLE);

        }

    }
}
