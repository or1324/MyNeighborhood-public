package com.amitbartfeld.myneighborhood.properties;

import androidx.annotation.Keep;

import com.amitbartfeld.myneighborhood.constants.CloudPropertiesNames;
import com.amitbartfeld.myneighborhood.operations.GeneralOperations;
import com.amitbartfeld.myneighborhood.properties.type.OrderedByTimeProperties;
//The @Keep is needed for firebase
@Keep
public class MessagesItemProperties extends OrderedByTimeProperties {
    @Keep
    public MessagesItemProperties(GeneralOperations generalOperations, String text) {
        super(generalOperations, GeneralOperations.ScreenTypeGeneralOperations.ScreenType.Messages);
        addProperty(CloudPropertiesNames.fullName, generalOperations.fullName);
        addProperty(CloudPropertiesNames.text, text);
    }

    //needed for firebase
    @Keep
    public MessagesItemProperties() {
        super(new GeneralOperations("", "", 0, false), GeneralOperations.ScreenTypeGeneralOperations.ScreenType.Messages);
    }
}
