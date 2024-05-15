package com.amitbartfeld.myneighborhood.listeners;

import com.amitbartfeld.myneighborhood.properties.type.ItemProperties;
import com.google.firebase.firestore.DocumentSnapshot;

public interface AmitItemPropertiesDownloadListener extends AmitErrorListener {
    void onDownloadFinished(ItemProperties properties);
}
