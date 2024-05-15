package com.amitbartfeld.myneighborhood.activities.abstract_activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.amitbartfeld.myneighborhood.activityInterfaces.AmitProgressBarActivity;
import com.amitbartfeld.myneighborhood.activityInterfaces.AmitToastActivity;
import com.amitbartfeld.myneighborhood.constants.MessagesTexts;
import com.amitbartfeld.myneighborhood.operations.GeneralOperations;
import com.amitbartfeld.myneighborhood.graphical_helpers.AmitToast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class AmitActivity extends AppCompatActivity implements AmitProgressBarActivity, AmitToastActivity {
    public boolean isLoading = false;
    public AppCompatTextView toast;
    private AmitToast amitToast;
    public static long toastTimeLeft = 0;
    public static String lastToastText;
    public ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setProgressBar();
        setAmitToastView();
        amitToast = new AmitToast(toast, this);
    }

    public void runInBackground(Runnable r) {
        Thread thread = new Thread(r);
        thread.start();
    }

    public void showMessage(String message) {
        if (GeneralOperations.isInUiThread(this)) {
            new AlertDialog.Builder(AmitActivity.this).setMessage(message).setPositiveButton("אוקיי", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }).create().show();
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new AlertDialog.Builder(AmitActivity.this).setMessage(message).setPositiveButton("אוקיי", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).create().show();
                }
            });
        }
    }

    public void showMessageToast(String message) {
        if (GeneralOperations.isInUiThread(this)) {
            amitToast.showToast(message);
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    amitToast.showToast(message);
                }
            });
        }
    }

    public void showErrorMessageToast(String message) {
        stopLoading();
        if (GeneralOperations.isInUiThread(this)) {
            amitToast.showToast(message);
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    amitToast.showToast(message);
                }
            });
        }
    }



    public void showErrorMessage(String message) {
        stopLoading();
        showMessage(message);
    }

    public void showInternetError() {
        showErrorMessage(MessagesTexts.internetError);
    }

    public void startLoading() {
        if (GeneralOperations.isInUiThread(this)) {
            isLoading = true;
            progressBar.setVisibility(View.VISIBLE);
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    isLoading = true;
                    progressBar.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    public void stopLoading() {
        if (GeneralOperations.isInUiThread(this)) {
            isLoading = false;
            progressBar.setVisibility(View.GONE);
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    isLoading = false;
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        amitToast.activityDying();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AmitActivity.toastTimeLeft != 0) {
            amitToast.showToastAgain();
        }
    }
}
