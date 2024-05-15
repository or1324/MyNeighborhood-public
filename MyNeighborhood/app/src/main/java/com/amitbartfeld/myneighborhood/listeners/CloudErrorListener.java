package com.amitbartfeld.myneighborhood.listeners;

import com.amitbartfeld.myneighborhood.activities.abstract_activities.amit_activities.ScreenTypeActivity;

public abstract class CloudErrorListener implements AmitErrorListener {

    ScreenTypeActivity activity;

    public CloudErrorListener(ScreenTypeActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onError(String message) {
        activity.onError(message);
    }
}