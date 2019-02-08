package com.example.bakingapp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bakingapp.R;

public class StepDescriptionFragment extends Fragment {

    String stepDescriptionText="";
    public StepDescriptionFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_steps,container,false);
        TextView stepDescription=(TextView)view.findViewById(R.id.step_description);
        stepDescription.setText(stepDescriptionText);
        return view;
    }
    public void setStepDescriptionText(String stepDescriptionText) {
        this.stepDescriptionText = stepDescriptionText;
    }

}
