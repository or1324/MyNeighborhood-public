package com.amitbartfeld.myneighborhood.activities.used_activities.only_properties;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;

import com.amitbartfeld.myneighborhood.activities.abstract_activities.amit_activities.screen_type_activities.properties_only_activities.NewUserPropertiesActivity;
import com.amitbartfeld.myneighborhood.constants.CloudNames;
import com.amitbartfeld.myneighborhood.operations.CloudOperations;
import com.amitbartfeld.myneighborhood.constants.CloudPropertiesNames;
import com.amitbartfeld.myneighborhood.constants.EditTextHints;
import com.amitbartfeld.myneighborhood.operations.GeneralOperations;
import com.amitbartfeld.myneighborhood.constants.MessagesTexts;
import com.amitbartfeld.myneighborhood.R;
import com.amitbartfeld.myneighborhood.listeners.InternetErrorDownloadListenerFirestore;
import com.amitbartfeld.myneighborhood.neighborhood_encryption.Encryption;
import com.amitbartfeld.myneighborhood.operations.PhoneNumberOperations;
import com.amitbartfeld.myneighborhood.properties.UserItemProperties;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends NewUserPropertiesActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViewById(R.id.register).setVisibility(View.VISIBLE);
        findViewById(R.id.checkBox).setVisibility(View.VISIBLE);
        ((AppCompatButton)findViewById(R.id.register)).setText("התחבר");
        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        ((AppCompatCheckBox)(findViewById(R.id.checkBox))).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    removeEditTextAtIndex(2);
                } else {
                    showEditTextAtIndex(2);
                }
            }
        });
        ((EditText)findViewById(R.id.edittext2)).setInputType(InputType.TYPE_CLASS_PHONE);
    }

    @Override
    public void setScreenType() {
        screenType = GeneralOperations.ScreenTypeGeneralOperations.ScreenType.Register;
    }

    @Override
    public boolean checkInputSpecifically() {
        try {
            PhoneNumberOperations.getPhoneNumber(((EditText)findViewById(R.id.edittext2)));
        } catch (Exception e) {
            showErrorMessageToast(MessagesTexts.phoneCountryCodeError);
            return false;
        }
        return true;
    }

    @Override
    public void upload() {
        String fullName = ((EditText) findViewById(R.id.edittext1)).getText().toString().trim();
        String phoneNumber = null;
        try {
            phoneNumber = PhoneNumberOperations.getPhoneNumber((EditText)findViewById(R.id.edittext2));
        } catch (Exception ignored) {
            //will never happen since I check for it in checkInputSpecifically.
        }
        final String finalPhoneNumber = phoneNumber;
        boolean isNewNeighborhood = ((AppCompatCheckBox) findViewById(R.id.checkBox)).isChecked();
        String neighborhoodCode = ((EditText) findViewById(R.id.edittext3)).getText().toString().trim();
        CloudOperations.getPhoneNumberNeighborhoodNum(phoneNumber, new InternetErrorDownloadListenerFirestore(this) {
            @Override
            public void onDownloadFinished(DocumentSnapshot document) {
                if (document != null) {
                    showErrorMessage(MessagesTexts.phoneNumberBelongsToAnotherUser);
                } else {
                    // document = null
                    if (isNewNeighborhood) {
                        CloudOperations.registerToNewNeighborhood(finalPhoneNumber, fullName, RegisterActivity.this);
                    } else {
                        checkNeighborhoodAndRegisterExisting(neighborhoodCode, finalPhoneNumber, fullName);
                    }
                }
            }
        });
    }

    @Override
    protected String[] initEditTextNames() {
        return new String[] {EditTextHints.fullName, EditTextHints.phoneNumber, EditTextHints.neighborhoodCode};
    }

    protected void checkNeighborhoodAndRegisterExisting(String neighborhoodCode, String phoneNumber, String fullName) {
        CloudOperations.getCodeMechanism(new InternetErrorDownloadListenerFirestore(RegisterActivity.this) {
            @Override
            public void onDownloadFinished(DocumentSnapshot document) {
                int neighborhoodNum = Encryption.decodeCode(neighborhoodCode, document.getString(CloudPropertiesNames.codeString));
                if (neighborhoodNum == 0) {
                    showErrorMessage(MessagesTexts.invalidNeighborhoodCode);
                } else {
                    DocumentReference d = FirebaseFirestore.getInstance().document(CloudNames.neighborhoodCollection + "/" + neighborhoodNum);
                    d.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                if (task.getResult() == null) {
                                    showErrorMessage(MessagesTexts.invalidNeighborhoodCode);
                                }
                                else {
                                    CloudOperations.registerToExistingNeighborhood(phoneNumber, fullName, neighborhoodCode, neighborhoodNum, RegisterActivity.this);
                                }
                            } else
                                showErrorMessage(MessagesTexts.invalidNeighborhoodCode);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void enterTheUser(UserItemProperties user, String neighborhoodCode) {
        if (user.generalOperations.isAdmin) {
            TextView showText = new TextView(this);
            showText.setText(neighborhoodCode);
            showText.setTextIsSelectable(true);
            new AlertDialog.Builder(this).setTitle(MessagesTexts.registrationCompletedSuccessfullyWithCode).setView(showText).setPositiveButton(MessagesTexts.okButtonDialog, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    GeneralOperations.EnterTheApp(RegisterActivity.this, user.generalOperations.fullName, user.generalOperations.isAdmin, user.generalOperations.neighborhoodNum, user.generalOperations.phoneNum);
                }
            }).show();
        } else {
            showMessageToast(MessagesTexts.registrationCompletedSuccessfullyNoCode);
            GeneralOperations.EnterTheApp(this, user.generalOperations.fullName, user.generalOperations.isAdmin, user.generalOperations.neighborhoodNum, user.generalOperations.phoneNum);
        }
    }
}