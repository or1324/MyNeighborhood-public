package com.amitbartfeld.myneighborhood.graphic_items;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;

import com.amitbartfeld.myneighborhood.constants.CloudPropertiesNames;
import com.amitbartfeld.myneighborhood.constants.NextActivityConstants;
import com.amitbartfeld.myneighborhood.properties.type.ItemProperties;

public class ProfessionalsGraphicItem extends GraphicItem {
    public ProfessionalsGraphicItem(Context context, AttributeSet set) {
        super(context, set);
    }

    public ProfessionalsGraphicItem(Context context) {
        super(context);
    }

    @Override
    public void changeSpecificGraphicFields(ItemProperties properties) {
        initTextByName(CloudPropertiesNames.fullName, properties.properties);
        initTextByName(CloudPropertiesNames.text, properties.properties);
        initTextByName(CloudPropertiesNames.job, properties.properties);
        initTextByName(CloudPropertiesNames.phoneNumber, properties.properties);
        initButton(CloudPropertiesNames.ratings, new Runnable() {
            @Override
            public void run() {
                Intent intent = mainActivityWrapper.getActivity().getIntent();
                intent.putExtra(NextActivityConstants.isReviewsIntentExtra, true);
                intent.putExtra(NextActivityConstants.chosenProfessionalPhoneIntentExtra, properties.properties.get(CloudPropertiesNames.phoneNumber).toString());
                intent.putExtra(NextActivityConstants.chosenProfessionalNameIntentExtra, properties.properties.get(CloudPropertiesNames.fullName).toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                mainActivityWrapper.getActivity().setIntent(intent);
                mainActivityWrapper.getActivity().recreate();
            }
        });
    }
}
