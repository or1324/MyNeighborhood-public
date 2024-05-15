package com.amitbartfeld.myneighborhood.listeners;

import com.amitbartfeld.myneighborhood.activities.abstract_activities.amit_activities.ScreenTypeActivity;

public abstract class InternetErrorDownloadListenerFirestore extends CloudErrorListener implements AmitDownloadListenerFirestore {
    public InternetErrorDownloadListenerFirestore(ScreenTypeActivity activity) {
        super(activity);
    }
}
