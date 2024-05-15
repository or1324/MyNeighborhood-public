package com.amitbartfeld.myneighborhood.activities.abstract_activities.amit_activities.screen_type_activities.properties_only_activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.amitbartfeld.myneighborhood.R;
import com.amitbartfeld.myneighborhood.activities.abstract_activities.amit_activities.screen_type_activities.PropertiesActivity;
import com.amitbartfeld.myneighborhood.properties.UserItemProperties;

public abstract class NewUserPropertiesActivity extends PropertiesActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void onSignInSucceeded(UserItemProperties user, String neighborhoodCode) {
        stopLoading();
        enterTheUser(user, neighborhoodCode);
    }

    public abstract void enterTheUser(UserItemProperties user, String neighborhoodCode);
}
