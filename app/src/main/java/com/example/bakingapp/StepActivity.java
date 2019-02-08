package com.example.bakingapp;

import android.content.res.Configuration;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bakingapp.constants.Constants;
import com.example.bakingapp.fragment.StepDescriptionFragment;
import com.example.bakingapp.objects.RecipeSteps;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoListener;

import java.util.List;

public class StepActivity extends AppCompatActivity {
    List<RecipeSteps> recipeSteps;
    int stepsPosition;
    private TextView descriptionTv;
    PlayerView playerView;
    SimpleExoPlayer player;
    RelativeLayout.LayoutParams paramsNotFullscreen;
    ScrollView scrollView;
    TextView descriptionNameTv,stepNumberTv;
    LinearLayout linearLayout;
    ImageView nextImage,previousImage;
    private DefaultDataSourceFactory dataSourceFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        setUpUI();
        setUpNextAndPrevious();



    }

    @Override
    protected void onStart() {
        super.onStart();

        if(player!=null){
            updatePlayer();
        }else{
            intializePlayer();
        }

    }
    @Override
    protected void onStop() {
        super.onStop();
        releasePlayer();

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.i("myOrien","landscape");

 
            paramsNotFullscreen=(RelativeLayout.LayoutParams) playerView.getLayoutParams();
            RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(paramsNotFullscreen);
            params.setMargins(0, 0, 0, 0);
            params.height= ViewGroup.LayoutParams.MATCH_PARENT;
            params.width=ViewGroup.LayoutParams.MATCH_PARENT;
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            playerView.setLayoutParams(params);
            descriptionNameTv.setVisibility(View.GONE);
            scrollView.setVisibility(View.GONE);
            linearLayout.setVisibility(View.GONE);

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Log.i("myOrien","portarit");
            playerView.setLayoutParams(paramsNotFullscreen);
            descriptionNameTv.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.VISIBLE);

        }

    }


    public void setUpUI(){

        descriptionTv=(TextView)findViewById(R.id.step_description);
        stepNumberTv=(TextView)findViewById(R.id.step_number);
        playerView=(PlayerView)findViewById(R.id.player_view);
        descriptionNameTv=(TextView)findViewById(R.id.step_description_name);
        scrollView=(ScrollView) findViewById(R.id.step_description_scroll_view);
        linearLayout=(LinearLayout) findViewById(R.id.controls_linear_layout);
        nextImage=(ImageView)findViewById(R.id.next);
        previousImage=(ImageView)findViewById(R.id.previous);

        recipeSteps=getIntent().getParcelableArrayListExtra(Constants.RECIPE_STEPS_INTENT_EXTRA);
        stepsPosition=getIntent().getIntExtra(Constants.RECIPE_STEPS_POSITION_INTENT_EXTRA,0);
        descriptionTv.setText(recipeSteps.get(stepsPosition).getDescription());
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
                descriptionTv.setText(recipeSteps.get(stepsPosition).getDescription());
                if(player!=null){
                updatePlayer();
                }else{
                    intializePlayer();
                }
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

                descriptionTv.setText(recipeSteps.get(stepsPosition).getDescription());
                if(player!=null){
                    updatePlayer();
                }else{
                    intializePlayer();
                }
                String stepNumber="("+(stepsPosition+1)+"/"+recipeSteps.size()+")";
                stepNumberTv.setText(stepNumber);

            }
        });

    }
    private void intializePlayer(){
        player= ExoPlayerFactory.newSimpleInstance(this,new DefaultTrackSelector());
        playerView.setPlayer(player);
         dataSourceFactory=new DefaultDataSourceFactory(this,
                Util.getUserAgent(this,"baking-app"));
        updatePlayer();

    }
    private void updatePlayer(){
        String urlText=recipeSteps.get(stepsPosition).getVideoURL();
        if(urlText.isEmpty()){
            Toast.makeText(this,"Sorry , There is no video available",Toast.LENGTH_SHORT).show();
            playerView.setVisibility(View.GONE);
        }else{
            playerView.setVisibility(View.VISIBLE);
            Uri videoUri = Uri.parse(urlText);
            ExtractorMediaSource mediaSource=new ExtractorMediaSource
                    .Factory(dataSourceFactory)
                    .createMediaSource(videoUri);
            player.prepare(mediaSource);
            player.setPlayWhenReady(true);
        }
    }
    private void releasePlayer(){
        playerView.setPlayer(null);
        player.release();
        player=null;
    }
    private void setUpStepFragment(){
        FragmentManager fragmentManager=getSupportFragmentManager();
        StepDescriptionFragment stepDescriptionFragment=new StepDescriptionFragment();
        stepDescriptionFragment.setStepDescriptionText(recipeSteps.get(stepsPosition).getDescription());

        fragmentManager.beginTransaction()
                .add(R.id.recipe_steps_container,stepDescriptionFragment)
                .commit();

    }
}
