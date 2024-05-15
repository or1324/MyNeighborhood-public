package com.amitbartfeld.myneighborhood.listeners;

import com.amitbartfeld.myneighborhood.activities.abstract_activities.amit_activities.ScreenTypeActivity;

public abstract class InternetErrorVerificationListener extends CloudErrorListener implements AmitVerificationListener{


    public InternetErrorVerificationListener(ScreenTypeActivity activity) {
        super(activity);
    }
}
