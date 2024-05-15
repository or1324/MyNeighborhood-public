package com.amitbartfeld.myneighborhood.activities.abstract_activities.amit_activities;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.amitbartfeld.myneighborhood.activities.abstract_activities.AmitActivity;
import com.amitbartfeld.myneighborhood.listeners.AmitDownloadsListenerFirestore;
import com.amitbartfeld.myneighborhood.listeners.AmitErrorListener;
import com.amitbartfeld.myneighborhood.listeners.AmitUploadListenerFirestore;
import com.amitbartfeld.myneighborhood.operations.GeneralOperations;
import com.amitbartfeld.myneighborhood.properties.type.CloudProperties;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public abstract class ScreenTypeActivity extends AmitActivity implements AmitErrorListener, AmitDownloadsListenerFirestore, AmitUploadListenerFirestore {
    protected GeneralOperations.ScreenTypeGeneralOperations.ScreenType screenType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setScreenType();
    }

    @Override
    public void onError(String message) {
        showInternetError();
        FirebaseCrashlytics.getInstance().log(message);
    }

    @Override
    public void onDownloadsFinished(List<DocumentSnapshot> documents) {
        stopLoading();
    }

    @Override
    public void onUploadFinished(CloudProperties properties) {
        stopLoading();
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    public abstract void setScreenType();
}
