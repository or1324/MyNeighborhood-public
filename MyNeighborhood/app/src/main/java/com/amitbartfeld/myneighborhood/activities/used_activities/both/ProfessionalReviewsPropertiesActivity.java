package com.amitbartfeld.myneighborhood.activities.used_activities.both;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.amitbartfeld.myneighborhood.R;
import com.amitbartfeld.myneighborhood.activities.abstract_activities.AmitActivity;
import com.amitbartfeld.myneighborhood.activities.abstract_activities.amit_activities.screen_type_activities.MainScreenPropertiesActivity;
import com.amitbartfeld.myneighborhood.constants.EditTextHints;
import com.amitbartfeld.myneighborhood.constants.MessagesTexts;
import com.amitbartfeld.myneighborhood.constants.NextActivityConstants;
import com.amitbartfeld.myneighborhood.listeners.InternetErrorUploadListenerFirestore;
import com.amitbartfeld.myneighborhood.operations.CloudOperations;
import com.amitbartfeld.myneighborhood.operations.GeneralOperations;
import com.amitbartfeld.myneighborhood.properties.ProfessionalReviewsItemProperties;
import com.amitbartfeld.myneighborhood.properties.type.CloudProperties;

public class ProfessionalReviewsPropertiesActivity extends MainScreenPropertiesActivity {
    EditText rating;
    String professionalPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        professionalPhone = extras.getString(NextActivityConstants.chosenProfessionalPhoneIntentExtra);
        rating = findViewById(R.id.rating);
        showRating();
    }

    private void showRating() {
        findViewById(R.id.rating_layout).setVisibility(View.VISIBLE);
    }

    @Override
    public void setScreenType() {
        screenType = GeneralOperations.ScreenTypeGeneralOperations.ScreenType.Professionals;
    }

    @Override
    public boolean checkInputSpecifically() {
        try {
            if (Byte.parseByte(rating.getText().toString()) >= 1 && Byte.parseByte(rating.getText().toString()) <= 5)
                return true;
        } catch (Exception e) {
            showErrorMessage(MessagesTexts.ratingError);
            return false;
        }
        return false;
    }

    @Override
    public void upload() {
        byte rate = Byte.parseByte(rating.getText().toString());
        String text = ((EditText)findViewById(R.id.edittext1)).getText().toString();
        CloudOperations.setItemPropertyDocumentInFirestore(new ProfessionalReviewsItemProperties(mainScreen.getGeneralOperations(), rate, text, professionalPhone), new InternetErrorUploadListenerFirestore(this) {
            @Override
            public void onUploadFinished(CloudProperties properties) {
                backToApp(MessagesTexts.professionalUploaded);
            }
        });
    }

    @Override
    protected String[] initEditTextNames() {
        return new String[] {EditTextHints.text};
    }

    @Override
    public AmitActivity getActivity() {
        return this;
    }
}
