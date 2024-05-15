package com.amitbartfeld.myneighborhood.properties.type;

import androidx.annotation.Keep;

import com.amitbartfeld.myneighborhood.operations.GeneralOperations;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

//The @Keep is needed for firebase
@Keep
public class ItemProperties extends CloudProperties {
    // text, phone number
    @Keep
    public GeneralOperations.ScreenTypeGeneralOperations.ScreenType type;
    @Keep
    public ItemProperties(GeneralOperations.ScreenTypeGeneralOperations.ScreenType type) {
        this.type = type;
    }
    @Keep
    public ItemProperties(DocumentSnapshot documentSnapshot) {
        if (documentSnapshot == null)
            return;
        ItemProperties me = documentSnapshot.toObject(ItemProperties.class);
        if (me == null)
            return;
        this.type = me.type;
        properties = me.properties;
        cloudUrl = me.cloudUrl;
    }

    //needed for firebase
    @Keep
    public ItemProperties() {

    }
}
