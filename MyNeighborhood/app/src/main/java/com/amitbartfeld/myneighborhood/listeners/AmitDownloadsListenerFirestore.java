package com.amitbartfeld.myneighborhood.listeners;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public interface AmitDownloadsListenerFirestore extends AmitErrorListener {
    void onDownloadsFinished(List<DocumentSnapshot> documents);
}
