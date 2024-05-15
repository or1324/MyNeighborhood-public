package com.amitbartfeld.myneighborhood.activities.used_activities.only_main_screen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.amitbartfeld.myneighborhood.activities.abstract_activities.amit_activities.screen_type_activities.main_screen_only_activities.ListRegularActivity;
import com.amitbartfeld.myneighborhood.operations.GeneralOperations;
import com.amitbartfeld.myneighborhood.activities.used_activities.both.MarketPropertiesActivity;
import com.amitbartfeld.myneighborhood.properties.MarketItemProperties;
import com.amitbartfeld.myneighborhood.properties.type.ItemProperties;

public class MarketRegularActivity extends ListRegularActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected ItemProperties getGeneralItemProperty() {
        return new MarketItemProperties(mainScreen.getGeneralOperations(), "", false, "", 0, "", "");
    }


    @Override
    public void setScreenType() {
        screenType = GeneralOperations.ScreenTypeGeneralOperations.ScreenType.Market;
    }

    @Override
    protected void openPropertiesScreen() {
        Intent intent = new Intent(this, MarketPropertiesActivity.class);
        mainScreen.moveToScreen(intent, this);
    }
}