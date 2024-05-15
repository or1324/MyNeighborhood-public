package com.amitbartfeld.myneighborhood.activities.used_activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.amitbartfeld.myneighborhood.activities.abstract_activities.AmitActivity;
import com.amitbartfeld.myneighborhood.constants.MessagesTexts;
import com.amitbartfeld.myneighborhood.graphical_helpers.AmitToast;
import com.amitbartfeld.myneighborhood.operations.CloudOperations;
import com.amitbartfeld.myneighborhood.constants.CloudPropertiesNames;
import com.amitbartfeld.myneighborhood.operations.GeneralOperations;
import com.amitbartfeld.myneighborhood.R;
import com.amitbartfeld.myneighborhood.operations.StorageOperations;
import com.amitbartfeld.myneighborhood.activities.used_activities.only_properties.LoginActivity;
import com.amitbartfeld.myneighborhood.listeners.AmitDownloadListenerFirestore;
import com.amitbartfeld.myneighborhood.neighborhood_encryption.Encryption;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Map;

public class MainActivity extends AmitActivity {

    ActivityResultLauncher<String[]> permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
        @Override
        public void onActivityResult(Map<String, Boolean> result) {
            if (result.containsValue(false)) {
                showErrorMessage(MessagesTexts.permissionsError);
            }
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED)
                permissionLauncher.launch(new String[]{Manifest.permission.READ_CALENDAR, Manifest.permission.POST_NOTIFICATIONS});
            else
                NotificationManagerCompat.from(this).cancelAll();
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher.launch(new String[]{Manifest.permission.READ_CALENDAR});
            NotificationManagerCompat.from(this).cancelAll();
        } else {
            NotificationManagerCompat.from(this).cancelAll();
        }
        findViewById(R.id.start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GeneralOperations.canPressAsyncButton()) {
                    GeneralOperations.asyncButtonWorking();
                    startLoading();
                    enterTheApp();
                }
            }
        });
    }

    public void enterTheApp() {
            //auth check. if authenticated - skip. else - login.
        if (checkAuth()) {
            CloudOperations.getCodeMechanism(new AmitDownloadListenerFirestore() {
                @Override
                public void onDownloadFinished(DocumentSnapshot document) {
                    int neighborhoodNum = Encryption.decodeCode(StorageOperations.getNeighborhoodCode(MainActivity.this), document.getString(CloudPropertiesNames.codeString));
                    GeneralOperations.EnterTheApp(MainActivity.this, StorageOperations.getFullName(MainActivity.this), StorageOperations.getIsAdmin(MainActivity.this), neighborhoodNum, StorageOperations.getPhoneNumber(MainActivity.this));
                    GeneralOperations.asyncButtonStopped();
                    stopLoading();
                }

                @Override
                public void onError(String message) {
                    showInternetError();
                    GeneralOperations.asyncButtonStopped();
                    stopLoading();
                }
            });

        } else {
            CloudOperations.auth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    GeneralOperations.asyncButtonStopped();
                    stopLoading();
                }
            });
        }
    }

    public boolean checkAuth() {
        FirebaseUser currentUser = CloudOperations.auth.getCurrentUser();
        if(currentUser != null && !currentUser.isAnonymous()) {
            return true;
        }
            return false;
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

    @Override
    public void setProgressBar() {
        progressBar = findViewById(R.id.load);
    }
}