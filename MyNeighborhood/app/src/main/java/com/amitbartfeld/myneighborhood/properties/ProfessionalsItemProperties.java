package com.amitbartfeld.myneighborhood.properties;

import androidx.annotation.Keep;

import com.amitbartfeld.myneighborhood.constants.CloudPropertiesNames;
import com.amitbartfeld.myneighborhood.operations.GeneralOperations;
import com.amitbartfeld.myneighborhood.properties.type.ItemProperties;
//The @Keep is needed for firebase
public class ProfessionalsItemProperties extends ItemProperties {
    // professional's fullName, professional's job, description, phoneNum
    @Keep
    public ProfessionalsItemProperties(GeneralOperations generalOperations, String job, String text, String phone, String fullName) {
        super(GeneralOperations.ScreenTypeGeneralOperations.ScreenType.Professionals);
        cloudUrl = generalOperations.getUrlFromScreenTypeAndOrderParameter(type, phone);
        addProperty(CloudPropertiesNames.fullName, fullName);
        addProperty(CloudPropertiesNames.job, job);
        addProperty(CloudPropertiesNames.text, text);
        addProperty(CloudPropertiesNames.phoneNumber, phone);
    }

    //needed for firebase
    @Keep
    public ProfessionalsItemProperties() {
    }
}
