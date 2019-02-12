package com.example.bakingapp.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.bakingapp.R;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

public class PlayerViewFragment extends Fragment {
    PlayerView playerView;
    SimpleExoPlayer player;
    private DefaultDataSourceFactory dataSourceFactory;
    Context context;
    String urlText;
    RelativeLayout relativeLayout;
    private ImageView playerImage;


    String thumbnailImage="";
    public PlayerViewFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player_view, container, false);
         playerView =  view.findViewById(R.id.player_view);
         relativeLayout=view.findViewById(R.id.player_relative_layout);
        playerImage =  view.findViewById(R.id.player_image);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(player==null){
            intializePlayer(urlText);
        }else {
            updatePlayer(urlText,thumbnailImage);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }


    private void intializePlayer(String urlText){
        player= ExoPlayerFactory.newSimpleInstance(getContext(),new DefaultTrackSelector());
        playerView.setPlayer(player);
        if(getContext()!=null) {
            dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                    Util.getUserAgent(getContext(), "baking-app"));
        }
        updateFragmentPlayer(urlText,thumbnailImage);

    }



    private void updateFragmentPlayer(String urlText, String image){
        setThumbnailImage(image);
        if(urlText !=null){
        if(urlText.isEmpty()){
            Toast.makeText(getContext(), R.string.no_video_available_message,Toast.LENGTH_SHORT).show();
            setPlayerImage(thumbnailImage);
            if(player!=null){
                releasePlayer();
            }
            playerView.setVisibility(View.GONE);
            playerImage.setVisibility(View.VISIBLE);
        }else{
            playerView.setVisibility(View.VISIBLE);
            playerImage.setVisibility(View.GONE);
            Uri videoUri = Uri.parse(urlText);
            ExtractorMediaSource mediaSource=new ExtractorMediaSource
                    .Factory(dataSourceFactory)
                    .createMediaSource(videoUri);
            player.prepare(mediaSource);
            player.setPlayWhenReady(true);
            player.addListener(new Player.EventListener() {
                @Override
                public void onPlayerError(ExoPlaybackException error) {
                    Toast.makeText(getContext(), R.string.playing_video_error_messge,Toast.LENGTH_SHORT).show();
                   setPlayerImage(thumbnailImage);
                    if(player!=null){
                        releasePlayer();
                    }
                    playerView.setVisibility(View.GONE);
                    playerImage.setVisibility(View.VISIBLE);

                }
            });

        }
        }
    }
    private void releasePlayer(){
        playerView.setPlayer(null);
        if(player!=null){
        player.release();
        player=null;}
    }


    public void updatePlayer(String urlText,String image){
        if(player!=null){
            updateFragmentPlayer(urlText,image);
        }else{
            intializePlayer(urlText);
        }
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setUrlText(String urlText) {
        this.urlText = urlText;
    }

    public void setThumbnailImage(String thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
    }

    private void setPlayerImage(String thumbnailImage){
        if(thumbnailImage.isEmpty()){
            Picasso.get()
                    .load(R.drawable.food_image)
                    .into(playerImage);
        }else{
            Picasso.get()
                    .load(thumbnailImage)
                    .placeholder(R.drawable.food_image)
                    .error(R.drawable.food_image)
                    .into(playerImage);
        }
    }

}
