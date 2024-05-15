package com.amitbartfeld.myneighborhood.listeners;

import com.amitbartfeld.myneighborhood.properties.type.CloudProperties;

public interface AmitUploadListenerFirestore extends AmitErrorListener {
    void onUploadFinished(CloudProperties properties);
}
