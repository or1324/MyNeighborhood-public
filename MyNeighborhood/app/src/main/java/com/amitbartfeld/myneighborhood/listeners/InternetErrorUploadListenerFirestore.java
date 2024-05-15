package com.amitbartfeld.myneighborhood.listeners;

import com.amitbartfeld.myneighborhood.activities.abstract_activities.amit_activities.ScreenTypeActivity;

public abstract class InternetErrorUploadListenerFirestore extends CloudErrorListener implements AmitUploadListenerFirestore {

    public InternetErrorUploadListenerFirestore(ScreenTypeActivity activity) {
        super(activity);
    }
}
