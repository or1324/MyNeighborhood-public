package com.amitbartfeld.myneighborhood.activityInterfaces.main_screen_helper;

import android.app.Activity;

import androidx.activity.result.ActivityResultLauncher;

import com.amitbartfeld.myneighborhood.activities.abstract_activities.AmitActivity;
import com.amitbartfeld.myneighborhood.operations.GeneralOperations;

public interface MainScreenActivity {
    MainScreenActivityHelper getMainScreen();
    AmitActivity getActivity();

}
