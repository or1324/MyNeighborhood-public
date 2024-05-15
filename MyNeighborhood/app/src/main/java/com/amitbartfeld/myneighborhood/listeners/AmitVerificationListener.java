package com.amitbartfeld.myneighborhood.listeners;

import com.amitbartfeld.myneighborhood.properties.type.CloudProperties;

public interface AmitVerificationListener extends AmitErrorListener {
    void onVerificationSucceeded();
}
