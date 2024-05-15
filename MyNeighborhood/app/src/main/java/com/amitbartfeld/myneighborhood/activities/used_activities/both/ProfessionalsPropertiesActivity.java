package com.amitbartfeld.myneighborhood.activities.used_activities.both;

import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.widget.EditText;

import com.amitbartfeld.myneighborhood.activities.abstract_activities.AmitActivity;
import com.amitbartfeld.myneighborhood.activities.abstract_activities.amit_activities.screen_type_activities.MainScreenPropertiesActivity;
import com.amitbartfeld.myneighborhood.listeners.InternetErrorDownloadListenerFirestore;
import com.amitbartfeld.myneighborhood.listeners.ItemPropertiesInternetErrorDownloadListener;
import com.amitbartfeld.myneighborhood.operations.CloudOperations;
import com.amitbartfeld.myneighborhood.constants.EditTextHints;
import com.amitbartfeld.myneighborhood.operations.GeneralOperations;
import com.amitbartfeld.myneighborhood.R;
import com.amitbartfeld.myneighborhood.constants.MessagesTexts;
import com.amitbartfeld.myneighborhood.listeners.InternetErrorUploadListenerFirestore;
import com.amitbartfeld.myneighborhood.properties.ProfessionalsItemProperties;
import com.amitbartfeld.myneighborhood.properties.type.CloudProperties;
import com.amitbartfeld.myneighborhood.properties.type.ItemProperties;
import com.google.firebase.firestore.DocumentSnapshot;

public class ProfessionalsPropertiesActivity extends MainScreenPropertiesActivity {

    EditText phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        phone = findViewById(R.id.edittext4);
        phone.setInputType(InputType.TYPE_CLASS_PHONE);
        phone.setGravity(Gravity.RIGHT);
    }

    @Override
    public void setScreenType() {
        screenType = GeneralOperations.ScreenTypeGeneralOperations.ScreenType.Professionals;
    }

    @Override
    public boolean checkInputSpecifically() {
        return true;
    }

    @Override
    public void upload() {
        String fullName = ((EditText)findViewById(R.id.edittext1)).getText().toString();
        String job = ((EditText)findViewById(R.id.edittext2)).getText().toString();
        String text = ((EditText)findViewById(R.id.edittext3)).getText().toString();
        String p = phone.getText().toString();
        ProfessionalsItemProperties professionalsItemProperties = new ProfessionalsItemProperties(mainScreen.getGeneralOperations(), job, text, p, fullName);
        CloudOperations.getItemPropertyDocumentFromFirestore(professionalsItemProperties, new ItemPropertiesInternetErrorDownloadListener(ProfessionalsPropertiesActivity.this) {
            @Override
            public void onDownloadFinished(ItemProperties properties) {
                if (!properties.properties.isEmpty())
                    backToApp(MessagesTexts.professionalAlreadyExists);
                else {
                    CloudOperations.setItemPropertyDocumentInFirestore(professionalsItemProperties, new InternetErrorUploadListenerFirestore(ProfessionalsPropertiesActivity.this) {
                        @Override
                        public void onUploadFinished(CloudProperties properties) {
                            backToApp(MessagesTexts.professionalUploaded);
                        }
                    });
                }
            }
        });
    }

    @Override
    protected String[] initEditTextNames() {
        return new String[] {EditTextHints.professionalName, EditTextHints.professionalJob, EditTextHints.text, EditTextHints.professionalPhoneNum};
    }

    @Override
    public AmitActivity getActivity() {
        return this;
    }
}
