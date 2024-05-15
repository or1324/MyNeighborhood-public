package com.amitbartfeld.myneighborhood.activities.used_activities.only_properties;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.amitbartfeld.myneighborhood.activities.abstract_activities.amit_activities.screen_type_activities.properties_only_activities.NewUserPropertiesActivity;
import com.amitbartfeld.myneighborhood.operations.CloudOperations;
import com.amitbartfeld.myneighborhood.operations.GeneralOperations;
import com.amitbartfeld.myneighborhood.constants.EditTextHints;
import com.amitbartfeld.myneighborhood.constants.MessagesTexts;
import com.amitbartfeld.myneighborhood.R;
import com.amitbartfeld.myneighborhood.listeners.InternetErrorDownloadListenerFirestore;
import com.amitbartfeld.myneighborhood.operations.PhoneNumberOperations;
import com.amitbartfeld.myneighborhood.properties.UserItemProperties;
import com.google.firebase.firestore.DocumentSnapshot;

public class LoginActivity extends NewUserPropertiesActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViewById(R.id.register).setVisibility(View.VISIBLE);
        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        ((EditText)findViewById(R.id.edittext1)).setInputType(InputType.TYPE_CLASS_PHONE);
    }

    @Override
    public void enterTheUser(UserItemProperties user, String neighborhoodCode) {
        showMessageToast(MessagesTexts.loggingInCompletedSuccessfully + " " + user.generalOperations.fullName + "!");
        GeneralOperations.EnterTheApp(this, user.generalOperations.fullName, user.generalOperations.isAdmin, user.generalOperations.neighborhoodNum, user.generalOperations.phoneNum);
    }

    @Override
    public void setScreenType() {
        screenType = GeneralOperations.ScreenTypeGeneralOperations.ScreenType.Login;
    }

    @Override
    public boolean checkInputSpecifically() {
        try {
            PhoneNumberOperations.getPhoneNumber(((EditText)findViewById(R.id.edittext1)));
        } catch (Exception e) {
            showErrorMessageToast(MessagesTexts.phoneCountryCodeError);
            return false;
        }
        return true;
    }

    @Override
    public void upload() {
        String phoneNumber = null;
        try {
            phoneNumber = PhoneNumberOperations.getPhoneNumber((EditText)findViewById(R.id.edittext1));
        } catch (Exception ignored) {
            //will never happen since I check for it in checkInputSpecifically.
        }
        final String finalPhoneNumber = phoneNumber;
        CloudOperations.getPhoneNumberNeighborhoodNum(phoneNumber, new InternetErrorDownloadListenerFirestore(this) {
            @Override
            public void onDownloadFinished(DocumentSnapshot document) {
                if (document == null) {
                    showErrorMessage(MessagesTexts.phoneNumberDoesNotExists);
                } else {
                    int neighborhoodNum = Integer.parseInt(document.getId());
                    //If it is 0 then the user does not exist
                    CloudOperations.login(finalPhoneNumber, neighborhoodNum,  LoginActivity.this);
                }
            }
        });
    }

    @Override
    protected String[] initEditTextNames() {
        return new String[] {EditTextHints.phoneNumber};
    }


}
