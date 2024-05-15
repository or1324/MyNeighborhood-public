package com.amitbartfeld.myneighborhood.activityInterfaces.main_screen_helper;

import static com.amitbartfeld.myneighborhood.constants.NextActivityConstants.nextActivitiesIntentExtraFullName;
import static com.amitbartfeld.myneighborhood.constants.NextActivityConstants.nextActivitiesIntentExtraIsAdmin;
import static com.amitbartfeld.myneighborhood.constants.NextActivityConstants.nextActivitiesIntentExtraNeighborhoodNum;
import static com.amitbartfeld.myneighborhood.constants.NextActivityConstants.nextActivitiesIntentExtraPhoneNum;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.amitbartfeld.myneighborhood.activities.used_activities.only_main_screen.MarketRegularActivity;
import com.amitbartfeld.myneighborhood.operations.GeneralOperations;

public class MainScreenActivityHelper {
    private GeneralOperations generalOperations;
    private ActivityResultLauncher<String> launcher = null;
    private Runnable runAfterResult;

    public MainScreenActivityHelper() {

    }

    public void onCreate(AppCompatActivity activity) {
            launcher = activity.registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    if (result) {
                        runAfterResult.run();
                    }
                }
            });
        Bundle extras = activity.getIntent().getExtras();
        generalOperations = new GeneralOperations(extras.getString(nextActivitiesIntentExtraPhoneNum), extras.getString(nextActivitiesIntentExtraFullName), extras.getInt(nextActivitiesIntentExtraNeighborhoodNum), extras.getBoolean(nextActivitiesIntentExtraIsAdmin));
    }

    public void setRunAfterResult(Runnable r) {
        runAfterResult = r;
    }

    public ActivityResultLauncher<String> getPermissionLauncher() {
        return launcher;
    }

    public void moveToScreen(Intent intent, Activity currentActivity) {
        intent.putExtra(nextActivitiesIntentExtraPhoneNum, generalOperations.phoneNum);
        intent.putExtra(nextActivitiesIntentExtraFullName, generalOperations.fullName);
        intent.putExtra(nextActivitiesIntentExtraNeighborhoodNum, generalOperations.neighborhoodNum);
        intent.putExtra(nextActivitiesIntentExtraIsAdmin, generalOperations.isAdmin);
        currentActivity.startActivity(intent);
    }

    public GeneralOperations getGeneralOperations() {
        return generalOperations;
    }

}
