package com.amitbartfeld.myneighborhood.properties.type;

import androidx.annotation.Keep;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
//The @Keep is needed for firebase
@Keep
public class CloudProperties implements Serializable {
    @Keep
    public Map<String, Object> properties = new HashMap<>();
    @Keep
    public String cloudUrl;
    @Keep
    protected void addProperty(String name, Object value) {
        properties.put(name, value);
    }

    //needed for firebase
    @Keep
    public CloudProperties() {

    }
}
