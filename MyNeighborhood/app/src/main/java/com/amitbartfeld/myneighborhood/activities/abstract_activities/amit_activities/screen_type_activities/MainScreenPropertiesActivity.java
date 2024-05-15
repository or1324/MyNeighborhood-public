package com.amitbartfeld.myneighborhood.activities.abstract_activities.amit_activities.screen_type_activities;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.amitbartfeld.myneighborhood.activities.abstract_activities.AmitActivity;
import com.amitbartfeld.myneighborhood.activityInterfaces.main_screen_helper.MainScreenActivity;
import com.amitbartfeld.myneighborhood.activityInterfaces.main_screen_helper.MainScreenActivityHelper;


public abstract class MainScreenPropertiesActivity extends PropertiesActivity implements MainScreenActivity {

    public MainScreenActivityHelper mainScreen = new MainScreenActivityHelper();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainScreen.onCreate(this);
    }

    @Override
    public AmitActivity getActivity() {
        return this;
    }

    @Override
    public MainScreenActivityHelper getMainScreen() {
        return mainScreen;
    }

    protected void backToApp(String message) {
        stopLoading();
        showMessageToast(message);
        finish();
    }
}
