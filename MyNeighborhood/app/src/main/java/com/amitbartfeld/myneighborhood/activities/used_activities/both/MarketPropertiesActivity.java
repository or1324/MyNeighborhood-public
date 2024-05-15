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
import com.amitbartfeld.myneighborhood.properties.MarketItemProperties;
import com.amitbartfeld.myneighborhood.properties.type.CloudProperties;

public class MarketPropertiesActivity extends ChooseImagePropertiesActivity {

    EditText price;
    CheckBox isUsed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        price = ((EditText)findViewById(R.id.edittext3));
        price.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        isUsed = findViewById(R.id.checkBox);
        isUsed.setVisibility(View.VISIBLE);
        isUsed.setText("משומש");
    }

    @Override
    protected void uploadToFirestore(String downloadURL) {
        String location = ((EditText)findViewById(R.id.edittext1)).getText().toString();
        String itemName = ((EditText)findViewById(R.id.edittext2)).getText().toString();
        int p = Integer.parseInt(price.getText().toString());
        String text = ((EditText)findViewById(R.id.edittext4)).getText().toString();
        boolean used = isUsed.isChecked();
        CloudOperations.setItemPropertyDocumentInFirestore(new MarketItemProperties(mainScreen.getGeneralOperations(), text, used, location, p, itemName, downloadURL), new InternetErrorUploadListenerFirestore(MarketPropertiesActivity.this) {
            @Override
            public void onUploadFinished(CloudProperties properties) {
                backToApp(MessagesTexts.marketUploaded);
            }
        });
    }

    @Override
    public void setScreenType() {
        screenType = GeneralOperations.ScreenTypeGeneralOperations.ScreenType.Market;
    }

    @Override
    public boolean checkInputSpecifically() {
        try {
            Integer.parseInt(price.getText().toString());
            return true;
        } catch (Exception e) {
            showErrorMessage(MessagesTexts.priceTooBig);
            return false;
        }
    }

    @Override
    protected String[] initEditTextNames() {
        return new String[]{EditTextHints.location, EditTextHints.itemName, EditTextHints.price, EditTextHints.text};
    }

    @Override
    public AmitActivity getActivity() {
        return this;
    }
}
