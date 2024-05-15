package com.amitbartfeld.myneighborhood.listeners;

import com.amitbartfeld.myneighborhood.activities.abstract_activities.amit_activities.ScreenTypeActivity;

public abstract class ItemPropertiesInternetErrorDownloadListener extends CloudErrorListener implements AmitItemPropertiesDownloadListener{
    public ItemPropertiesInternetErrorDownloadListener(ScreenTypeActivity activity) {
        super(activity);
    }
}
