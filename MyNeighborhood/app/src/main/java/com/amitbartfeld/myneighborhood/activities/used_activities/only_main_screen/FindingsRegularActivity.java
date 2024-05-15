package com.amitbartfeld.myneighborhood.activities.used_activities.only_main_screen;

import android.app.Activity;
import android.content.Intent;

import com.amitbartfeld.myneighborhood.activities.abstract_activities.amit_activities.screen_type_activities.main_screen_only_activities.ListRegularActivity;
import com.amitbartfeld.myneighborhood.operations.GeneralOperations;
import com.amitbartfeld.myneighborhood.activities.used_activities.both.FindingsPropertiesActivity;
import com.amitbartfeld.myneighborhood.properties.FindingsItemProperties;
import com.amitbartfeld.myneighborhood.properties.type.ItemProperties;

public class FindingsRegularActivity extends ListRegularActivity {

    @Override
    public void setScreenType() {
        screenType = GeneralOperations.ScreenTypeGeneralOperations.ScreenType.Findings;
    }

    @Override
    protected void openPropertiesScreen() {
        Intent intent = new Intent(this, FindingsPropertiesActivity.class);
        mainScreen.moveToScreen(intent, this);
    }

    @Override
    protected ItemProperties getGeneralItemProperty() {
        return new FindingsItemProperties(mainScreen.getGeneralOperations(), "", "", "", false, "");
    }
}
