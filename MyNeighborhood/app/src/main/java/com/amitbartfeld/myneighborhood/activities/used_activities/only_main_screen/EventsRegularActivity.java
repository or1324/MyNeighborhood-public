package com.amitbartfeld.myneighborhood.activities.used_activities.only_main_screen;

import android.content.Intent;

import com.amitbartfeld.myneighborhood.activities.abstract_activities.amit_activities.screen_type_activities.main_screen_only_activities.ListRegularActivity;
import com.amitbartfeld.myneighborhood.operations.GeneralOperations;
import com.amitbartfeld.myneighborhood.activities.used_activities.both.EventsPropertiesActivity;
import com.amitbartfeld.myneighborhood.properties.EventsItemProperties;
import com.amitbartfeld.myneighborhood.properties.type.ItemProperties;

public class EventsRegularActivity extends ListRegularActivity {

    @Override
    public void setScreenType() {
        screenType = GeneralOperations.ScreenTypeGeneralOperations.ScreenType.Events;
    }

    @Override
    protected void openPropertiesScreen() {
        Intent intent = new Intent(this, EventsPropertiesActivity.class);
        mainScreen.moveToScreen(intent, this);
    }

    @Override
    protected ItemProperties getGeneralItemProperty() {
        return new EventsItemProperties(mainScreen.getGeneralOperations(), "", "", "", 0, null);
    }
}
