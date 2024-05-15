package com.amitbartfeld.myneighborhood.activities.abstract_activities.amit_activities.screen_type_activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.amitbartfeld.myneighborhood.activities.abstract_activities.amit_activities.ScreenTypeActivity;
import com.amitbartfeld.myneighborhood.constants.MessagesTexts;
import com.amitbartfeld.myneighborhood.R;
import com.amitbartfeld.myneighborhood.listeners.AmitDownloadsListenerFirestore;
import com.amitbartfeld.myneighborhood.listeners.AmitUploadListenerFirestore;
import com.amitbartfeld.myneighborhood.properties.type.CloudProperties;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public abstract class PropertiesActivity extends ScreenTypeActivity implements AmitUploadListenerFirestore, AmitDownloadsListenerFirestore {

    public final static int editTextNum = 4;
    private EditText[] editTexts = new EditText[editTextNum];
    private String[] editTextNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_properties);
        super.onCreate(savedInstanceState);
        initEditTexts();
        editTextNames = initEditTextNames();
        putTextInEditTexts(editTextNames);
        findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextButtonClicked();
            }
        });
    }

    private void initEditTexts() {
        for (int i = 0; i < editTextNum; i++) {
            editTexts[i] = findViewById(getResources().getIdentifier("edittext"+(i+1), "id", getPackageName()));
        }
    }

    @Override
    public void setProgressBar() {
        progressBar = findViewById(R.id.load);
    }

    protected void putTextInEditTexts(String[] editTextNames) {
        for (int i = 0; i < editTextNames.length; i++) {
            editTexts[i].setHint(editTextNames[i]);
            editTexts[i].setVisibility(View.VISIBLE);
        }
    }

    protected boolean isEverythingFilled() {
        // check if everything was filled well
        for(int i = 0; i < editTextNames.length; i++) {
            if (editTexts[i].getText().toString().trim().equals("")) {
                return false;
            }
        }
        return true;
    }

    protected abstract boolean checkInputSpecifically();

    protected abstract void upload();


    @Override
    public void onError(String message) {
        showInternetError();
        FirebaseCrashlytics.getInstance().log(message);
    }

    @Override
    public void onDownloadsFinished(List<DocumentSnapshot> documents) {
        stopLoading();
    }

    @Override
    public void onUploadFinished(CloudProperties properties) {
        stopLoading();
    }

    @Override
    public void setAmitToastView() {
        toast = findViewById(R.id.toast);
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    private void nextButtonClicked() {
        if (!isLoading) {
            startLoading();
            if (isEverythingFilled()) {
                runInBackground(new Runnable() {
                    @Override
                    public void run() {
                        if (checkInputSpecifically())
                            upload();
                    }
                });
            } else
                showErrorMessage(MessagesTexts.everyFieldMustBeFilled);
        }
    }

    protected abstract String[] initEditTextNames();

    protected void removeEditTextAtIndex(int index) {
        editTexts[index].setVisibility(View.INVISIBLE);
        String[] newArray = new String[editTextNames.length-1];
        for (int i = 0; i < editTextNames.length; i++)
            if (i != index)
                newArray[i] = editTextNames[i];
        editTextNames = newArray;
    }

    protected void showEditTextAtIndex(int index) {
        editTexts[index].setVisibility(View.VISIBLE);
        String[] newArray = new String[editTextNames.length+1];
        for (int i = 0; i < editTextNames.length; i++) {
            if (i == index)
                break;
            newArray[i] = editTextNames[i];
        }
        newArray[index] = editTexts[index].getHint().toString();
        for (int i = index; i < editTextNames.length; i++) {
            newArray[i] = editTextNames[i];
        }
        editTextNames = newArray;
    }

}