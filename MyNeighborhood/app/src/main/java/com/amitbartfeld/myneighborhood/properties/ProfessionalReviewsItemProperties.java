package com.amitbartfeld.myneighborhood.properties;

import androidx.annotation.Keep;

import com.amitbartfeld.myneighborhood.constants.CloudPropertiesNames;
import com.amitbartfeld.myneighborhood.operations.GeneralOperations;
import com.amitbartfeld.myneighborhood.properties.type.OrderedByTimeProperties;

public class ProfessionalReviewsItemProperties extends OrderedByTimeProperties {
    //review writer's full name, text, rate, professional's phone number
    @Keep
    public ProfessionalReviewsItemProperties(GeneralOperations generalOperations, byte rate, String text, String professionalPhone) {
        super(generalOperations, GeneralOperations.ScreenTypeGeneralOperations.ScreenType.ProfessionalReviews);
        //overrides the cloud url
        cloudUrl = generalOperations.getUrlFromScreenTypeAndOrderParameter(GeneralOperations.ScreenTypeGeneralOperations.ScreenType.Professionals, professionalPhone)+generalOperations.getProfessionalsReviewSubUrl(creationTime);
        addProperty(CloudPropertiesNames.fullName, generalOperations.fullName);
        addProperty(CloudPropertiesNames.text, text);
        addProperty(CloudPropertiesNames.rate, (int)rate);
    }

    //needed for firebase
    @Keep
    public ProfessionalReviewsItemProperties() {
        super(new GeneralOperations("", "", 0, false), GeneralOperations.ScreenTypeGeneralOperations.ScreenType.ProfessionalReviews);
    }
}
