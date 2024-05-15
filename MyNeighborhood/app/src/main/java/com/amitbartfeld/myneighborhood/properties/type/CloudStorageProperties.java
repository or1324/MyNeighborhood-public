package com.amitbartfeld.myneighborhood.properties.type;

import android.graphics.Bitmap;

import com.amitbartfeld.myneighborhood.operations.GeneralOperations;
import com.amitbartfeld.myneighborhood.constants.CloudPropertiesNames;

public class CloudStorageProperties extends CloudProperties {
    public CloudStorageProperties(GeneralOperations.ScreenTypeGeneralOperations.ScreenType type, Bitmap image) {
        long creationTime = System.nanoTime();
        cloudUrl = GeneralOperations.ScreenTypeGeneralOperations.getGeneralSubUrlFromScreenType(type) + creationTime + ".jpg";
        addProperty(CloudPropertiesNames.image, image);
    }
}
