package com.example.bakingapp.objects;


import android.support.annotation.Nullable;
import android.support.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicBoolean;

public class SimpleIdlingResource implements IdlingResource{

    @Nullable
    private volatile ResourceCallback mCallback;
    private AtomicBoolean mIdlingResource=new AtomicBoolean(true);


    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public boolean isIdleNow() {
        return mIdlingResource.get();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        mCallback=callback;
    }
    public void setIdleState(boolean isIdle) {
        mIdlingResource.set(isIdle);
        if (isIdle && mCallback != null) {
            mCallback.onTransitionToIdle();
        }
    }}
