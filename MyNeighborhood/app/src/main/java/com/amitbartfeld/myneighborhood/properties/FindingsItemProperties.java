package com.amitbartfeld.myneighborhood.properties;

import androidx.annotation.Keep;

import com.amitbartfeld.myneighborhood.constants.CloudPropertiesNames;
import com.amitbartfeld.myneighborhood.operations.GeneralOperations;
import com.amitbartfeld.myneighborhood.properties.type.OrderedByTimeProperties;
//The @Keep is needed for firebase
@Keep
public class FindingsItemProperties extends OrderedByTimeProperties {
    // isFinding, imageBitmap, location, date
    @Keep
    public FindingsItemProperties(GeneralOperations generalOperations, String date, String location, String bitmapUrl, boolean isFinding, String text) {
        super(generalOperations, GeneralOperations.ScreenTypeGeneralOperations.ScreenType.Findings);
        addProperty(CloudPropertiesNames.isFinding, isFinding);
        addProperty(CloudPropertiesNames.imageBitmapUrl, bitmapUrl);
        addProperty(CloudPropertiesNames.location, location);
        addProperty(CloudPropertiesNames.fullName, generalOperations.fullName);
        addProperty(CloudPropertiesNames.date, date);
        addProperty(CloudPropertiesNames.text, text);
    }

    //needed for firebase
    @Keep
    public FindingsItemProperties() {
        super(new GeneralOperations("", "", 0, false), GeneralOperations.ScreenTypeGeneralOperations.ScreenType.Findings);
    }
}