package com.amitbartfeld.myneighborhood.properties;

import androidx.annotation.Keep;

import com.amitbartfeld.myneighborhood.constants.CloudPropertiesNames;
import com.amitbartfeld.myneighborhood.operations.GeneralOperations;
import com.amitbartfeld.myneighborhood.properties.type.OrderedByTimeProperties;

import java.util.LinkedList;
import java.util.List;
//The @Keep is needed for firebase
@Keep
public class EventsItemProperties extends OrderedByTimeProperties {
    // title, location, date
    @Keep
    public EventsItemProperties(GeneralOperations g, String text, String title, String location, long date, List<String> phoneNumsOfPeopleComing) {
        super(g, GeneralOperations.ScreenTypeGeneralOperations.ScreenType.Events);
        addProperty(CloudPropertiesNames.title, title);
        addProperty(CloudPropertiesNames.location, location);
        //overrides the time that was set in the orderedbytimeproperties to be the time of the event.
        addProperty(CloudPropertiesNames.time, date);
        addProperty(CloudPropertiesNames.text, text);
        addProperty(CloudPropertiesNames.peopleComing, phoneNumsOfPeopleComing);
    }

    //needed for firebase
    @Keep
    public EventsItemProperties() {
        super(new GeneralOperations("", "", 0, false), GeneralOperations.ScreenTypeGeneralOperations.ScreenType.Events);
    }
}
