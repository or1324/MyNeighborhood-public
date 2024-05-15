package com.amitbartfeld.myneighborhood.listeners;

import com.amitbartfeld.myneighborhood.properties.type.ItemProperties;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public interface AmitDownloadListenerFirestore extends AmitErrorListener {
    void onDownloadFinished(DocumentSnapshot document);
}
