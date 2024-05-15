package com.amitbartfeld.myneighborhood.activities.abstract_activities.amit_activities.screen_type_activities.main_screen_only_activities;

import android.os.Bundle;
import android.view.View;

import com.amitbartfeld.myneighborhood.activities.abstract_activities.amit_activities.screen_type_activities.MainScreenRegularActivity;

public abstract class ListRegularActivity extends MainScreenRegularActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showPlusButton();
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPropertiesScreen();
            }
        });
    }

    protected abstract void openPropertiesScreen();
}
