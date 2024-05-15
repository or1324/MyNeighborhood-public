package com.amitbartfeld.myneighborhood.activities.used_activities.only_main_screen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.amitbartfeld.myneighborhood.R;
import com.amitbartfeld.myneighborhood.activities.abstract_activities.amit_activities.screen_type_activities.main_screen_only_activities.ListRegularActivity;
import com.amitbartfeld.myneighborhood.activities.used_activities.both.ProfessionalReviewsPropertiesActivity;
import com.amitbartfeld.myneighborhood.constants.CloudPropertiesNames;
import com.amitbartfeld.myneighborhood.constants.NextActivityConstants;
import com.amitbartfeld.myneighborhood.operations.GeneralOperations;
import com.amitbartfeld.myneighborhood.activities.used_activities.both.ProfessionalsPropertiesActivity;
import com.amitbartfeld.myneighborhood.properties.ProfessionalReviewsItemProperties;
import com.amitbartfeld.myneighborhood.properties.ProfessionalsItemProperties;
import com.amitbartfeld.myneighborhood.properties.type.ItemProperties;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class ProfessionalsRegularActivity extends ListRegularActivity {

    float averageReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras.getBoolean(NextActivityConstants.isReviewsIntentExtra)) {
            findViewById(R.id.image_button4).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getIntent().removeExtra(NextActivityConstants.isReviewsIntentExtra);
                    getIntent().removeExtra(NextActivityConstants.chosenProfessionalPhoneIntentExtra);
                    getIntent().removeExtra(NextActivityConstants.chosenProfessionalNameIntentExtra);
                    recreate();
                }
            });
        }
    }

    @Override
    public void addDownloadedViewsToActivityAsGraphicItems(List<DocumentSnapshot> documents) {
        super.addDownloadedViewsToActivityAsGraphicItems(documents);
        Bundle extras = getIntent().getExtras();
        averageReview = 0;
        if (extras.getBoolean(NextActivityConstants.isReviewsIntentExtra)) {
            for (DocumentSnapshot snapshot : documents) {
                ItemProperties properties = new ItemProperties(snapshot);
                averageReview += (long)properties.properties.get(CloudPropertiesNames.rate);
            }
            averageReview /= (float) documents.size();
            TextView title = findViewById(R.id.title);
            title.setText("Professional: " + extras.getString(NextActivityConstants.chosenProfessionalNameIntentExtra));
            findViewById(R.id.professional_title_layout).setVisibility(View.VISIBLE);
            RatingBar ratingBar = findViewById(R.id.rate);
            ratingBar.setRating(averageReview);
        }
    }

    @Override
    public void setScreenType() {
        Bundle extras = getIntent().getExtras();
        if (extras.getBoolean(NextActivityConstants.isReviewsIntentExtra))
            screenType = GeneralOperations.ScreenTypeGeneralOperations.ScreenType.ProfessionalReviews;
        else
            screenType = GeneralOperations.ScreenTypeGeneralOperations.ScreenType.Professionals;
    }

    @Override
    protected void openPropertiesScreen() {
        Bundle extras = getIntent().getExtras();
        Intent intent;
        if (extras.getBoolean(NextActivityConstants.isReviewsIntentExtra)) {
            intent = new Intent(this, ProfessionalReviewsPropertiesActivity.class);
            intent.putExtra(NextActivityConstants.chosenProfessionalPhoneIntentExtra, extras.getString(NextActivityConstants.chosenProfessionalPhoneIntentExtra));
        }
        else
            intent = new Intent(this, ProfessionalsPropertiesActivity.class);
        mainScreen.moveToScreen(intent, this);
    }

    @Override
    protected ItemProperties getGeneralItemProperty() {
        Bundle extras = getIntent().getExtras();
        if (extras.getBoolean(NextActivityConstants.isReviewsIntentExtra))
            return new ProfessionalReviewsItemProperties(mainScreen.getGeneralOperations(), (byte)0, "", extras.getString(NextActivityConstants.chosenProfessionalPhoneIntentExtra));
        return new ProfessionalsItemProperties(mainScreen.getGeneralOperations(), "", "", "", "");
    }
}
