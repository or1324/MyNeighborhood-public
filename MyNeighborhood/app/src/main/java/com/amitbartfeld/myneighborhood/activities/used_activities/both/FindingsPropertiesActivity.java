package com.amitbartfeld.myneighborhood.activities.used_activities.both;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.amitbartfeld.myneighborhood.activities.abstract_activities.AmitActivity;
import com.amitbartfeld.myneighborhood.activities.abstract_activities.amit_activities.screen_type_activities.both.ChooseImagePropertiesActivity;
import com.amitbartfeld.myneighborhood.operations.CloudOperations;
import com.amitbartfeld.myneighborhood.constants.EditTextHints;
import com.amitbartfeld.myneighborhood.operations.GeneralOperations;
import com.amitbartfeld.myneighborhood.R;
import com.amitbartfeld.myneighborhood.constants.MessagesTexts;
import com.amitbartfeld.myneighborhood.listeners.InternetErrorUploadListenerFirestore;
import com.amitbartfeld.myneighborhood.properties.FindingsItemProperties;
import com.amitbartfeld.myneighborhood.properties.type.CloudProperties;

public class FindingsPropertiesActivity extends ChooseImagePropertiesActivity {

    private CheckBox isFinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((EditText)findViewById(R.id.edittext3)).setInputType(InputType.TYPE_DATETIME_VARIATION_DATE);
        isFinding = findViewById(R.id.checkBox);
        isFinding.setText("זו מציאה");
        isFinding.setVisibility(View.VISIBLE);
    }

    @Override
    protected void uploadToFirestore(String downloadURL) {
        String text = ((EditText)findViewById(R.id.edittext1)).getText().toString();
        String location = ((EditText)findViewById(R.id.edittext2)).getText().toString();
        String date = ((EditText)findViewById(R.id.edittext3)).getText().toString();
        boolean finding = isFinding.isChecked();
        CloudOperations.setItemPropertyDocumentInFirestore(new FindingsItemProperties(mainScreen.getGeneralOperations(), date, location, downloadURL, finding, text), new InternetErrorUploadListenerFirestore(FindingsPropertiesActivity.this) {
            @Override
            public void onUploadFinished(CloudProperties properties) {
                backToApp(MessagesTexts.findingUploaded);
            }
        });
    }

    @Override
    public void setScreenType() {
        screenType = GeneralOperations.ScreenTypeGeneralOperations.ScreenType.Findings;
    }

    @Override
    public boolean checkInputSpecifically() {
        if (image == null) {
            showErrorMessage(MessagesTexts.haveToChooseImage);
            return false;
        }
        return true;
    }

    @Override
    protected String[] initEditTextNames() {
        return new String[]{EditTextHints.text, EditTextHints.location, EditTextHints.findingDate};
    }

    @Override
    public AmitActivity getActivity() {
        return null;
    }
}
