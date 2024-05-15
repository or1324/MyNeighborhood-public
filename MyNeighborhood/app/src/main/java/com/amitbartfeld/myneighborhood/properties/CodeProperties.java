package com.amitbartfeld.myneighborhood.properties;

import androidx.annotation.Keep;

import com.amitbartfeld.myneighborhood.constants.CloudNames;
import com.amitbartfeld.myneighborhood.constants.CloudPropertiesNames;
import com.amitbartfeld.myneighborhood.properties.type.CloudProperties;
//The @Keep is needed for firebase
@Keep
public class CodeProperties extends CloudProperties {
    @Keep
    public CodeProperties(String code) {
        cloudUrl = CloudNames.codeMechanismCollection+"/"+CloudNames.codeMechanismDocument;
        addProperty(CloudPropertiesNames.codeString, code);
    }

    //needed for firebase
    @Keep
    public CodeProperties() {
    }
}
