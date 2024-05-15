package com.amitbartfeld.myneighborhood.properties;

import androidx.annotation.Keep;

import com.amitbartfeld.myneighborhood.constants.CloudPropertiesNames;
import com.amitbartfeld.myneighborhood.operations.GeneralOperations;
import com.amitbartfeld.myneighborhood.properties.type.ItemProperties;
//The @Keep is needed for firebase
@Keep
public class UserItemProperties extends ItemProperties {
    @Keep
    public GeneralOperations generalOperations;
    @Keep
    public UserItemProperties(GeneralOperations generalOperations, GeneralOperations.ScreenTypeGeneralOperations.ScreenType type) {
        super(type);
        this.generalOperations = generalOperations;
        cloudUrl = generalOperations.getUrlFromScreenTypeAndOrderParameter(type, generalOperations.phoneNum);
        addProperty(CloudPropertiesNames.fullName, generalOperations.fullName);
        addProperty(CloudPropertiesNames.isAdmin, generalOperations.isAdmin);
    }
    //needed for firebase
    @Keep
    public UserItemProperties() {

    }
}
