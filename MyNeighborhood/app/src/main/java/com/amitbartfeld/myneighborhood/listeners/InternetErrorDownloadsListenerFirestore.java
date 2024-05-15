package com.amitbartfeld.myneighborhood.listeners;


import com.amitbartfeld.myneighborhood.activities.abstract_activities.amit_activities.ScreenTypeActivity;

public abstract class InternetErrorDownloadsListenerFirestore extends CloudErrorListener implements AmitDownloadsListenerFirestore {
    public InternetErrorDownloadsListenerFirestore(ScreenTypeActivity activity) {
        super(activity);
    }
}
