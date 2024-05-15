package com.amitbartfeld.myneighborhood.properties.type;

import androidx.annotation.Keep;

import com.amitbartfeld.myneighborhood.constants.CloudPropertiesNames;
import com.amitbartfeld.myneighborhood.operations.GeneralOperations;
//The @Keep is needed for firebase
@Keep
public class OrderedByTimeProperties extends ItemProperties {
    @Keep
    public long creationTime;
    @Keep
    public OrderedByTimeProperties(GeneralOperations generalOperations, GeneralOperations.ScreenTypeGeneralOperations.ScreenType type) {
        super(type);
        addProperty(CloudPropertiesNames.phoneNumber, generalOperations.phoneNum);
        creationTime = System.currentTimeMillis();
        addProperty(CloudPropertiesNames.time, creationTime);
        cloudUrl = generalOperations.getUrlFromScreenTypeAndOrderParameter(type, String.valueOf(creationTime));
    }
}
