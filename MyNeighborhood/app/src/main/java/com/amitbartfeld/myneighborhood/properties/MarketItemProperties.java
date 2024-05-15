package com.amitbartfeld.myneighborhood.properties;

import androidx.annotation.Keep;

import com.amitbartfeld.myneighborhood.constants.CloudPropertiesNames;
import com.amitbartfeld.myneighborhood.operations.GeneralOperations;
import com.amitbartfeld.myneighborhood.properties.type.OrderedByTimeProperties;
//The @Keep is needed for firebase
@Keep
public class MarketItemProperties extends OrderedByTimeProperties {
    // isUsed, location, price, itemName, text

    @Keep
    public MarketItemProperties(GeneralOperations g, String text, boolean isUsed, String location, int price, String itemName, String bitmapUrl) {
        super(g, GeneralOperations.ScreenTypeGeneralOperations.ScreenType.Market);
        addProperty(CloudPropertiesNames.isUsed, isUsed);
        addProperty(CloudPropertiesNames.location, location);
        addProperty(CloudPropertiesNames.imageBitmapUrl, bitmapUrl);
        addProperty(CloudPropertiesNames.price, price);
        addProperty(CloudPropertiesNames.itemName, itemName);
        addProperty(CloudPropertiesNames.fullName, g.fullName);
        addProperty(CloudPropertiesNames.text, text);
    }

    @Keep
    //needed for firebase
    public MarketItemProperties() {
        super(new GeneralOperations("", "", 0, false), GeneralOperations.ScreenTypeGeneralOperations.ScreenType.Market);
    }}
