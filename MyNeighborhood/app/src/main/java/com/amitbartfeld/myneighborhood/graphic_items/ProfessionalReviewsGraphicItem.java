package com.amitbartfeld.myneighborhood.graphic_items;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RatingBar;

import androidx.appcompat.widget.AppCompatTextView;

import com.amitbartfeld.myneighborhood.constants.CloudPropertiesNames;
import com.amitbartfeld.myneighborhood.properties.type.ItemProperties;

public class ProfessionalReviewsGraphicItem extends GraphicItem {
    public ProfessionalReviewsGraphicItem(Context context, AttributeSet set) {
        super(context, set);
    }

    public ProfessionalReviewsGraphicItem(Context context) {
        super(context);
    }
    @Override
    public void changeSpecificGraphicFields(ItemProperties properties) {
        initTextByName(CloudPropertiesNames.fullName, properties.properties);
        initTextByName(CloudPropertiesNames.text, properties.properties);
        setRating((long)properties.properties.get(CloudPropertiesNames.rate));
    }

    private void setRating(long rating) {
        RatingBar ratingBar = findViewById(getResources().getIdentifier(CloudPropertiesNames.rate, "id", mainActivityWrapper.getActivity().getPackageName()));
        ratingBar.setRating(rating);
    }
}
