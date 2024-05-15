package com.amitbartfeld.myneighborhood.listeners;

import com.amitbartfeld.myneighborhood.properties.type.CloudProperties;

public interface AmitUploadListenerStorage extends AmitErrorListener {
    void onUploadFinished(String downloadURL);
}
