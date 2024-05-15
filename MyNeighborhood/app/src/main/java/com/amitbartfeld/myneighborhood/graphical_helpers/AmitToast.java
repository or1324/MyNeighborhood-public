package com.amitbartfeld.myneighborhood.graphical_helpers;

import android.os.Handler;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;

import com.amitbartfeld.myneighborhood.activities.abstract_activities.AmitActivity;
import com.amitbartfeld.myneighborhood.constants.GraphicalConstants;

public class AmitToast {
    AppCompatTextView toast;
    AmitActivity activity;
    boolean canShowToast = false;
    long timeOfToast = 0;
    Handler handler = new Handler();
    public AmitToast(AppCompatTextView toast, AmitActivity activity) {
        this.toast = toast;
        this.activity = activity;
        toast.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                int width = right-left;
                int height = bottom-top;
                if (width != 0 && height != 0) {
                    toast.setX((GeneralGraphicOperations.getScreenWidth(activity) - width) / 2f);
                    toast.setY((GeneralGraphicOperations.getScreenHeight(activity) - height - GraphicalConstants.toastYMargin));
                    AmitActivity.toastTimeLeft = 1500;
                    timeOfToast = System.nanoTime();
                    continueToast();
                } else {
                    if (!canShowToast)
                        canShowToast = true;
                }
            }
        });
    }
    public void showToast(String str) {
        handler.removeCallbacksAndMessages(null);
        toast.setVisibility(View.GONE);
        toast.requestLayout();
        toast.setVisibility(View.VISIBLE);
        toast.setText(str);
        AmitActivity.lastToastText = str;
    }

    public void showToastAgain() {
        toast.setVisibility(View.VISIBLE);
        toast.setText(AmitActivity.lastToastText);
    }

    public void continueToast() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.setVisibility(View.GONE);
                AmitActivity.toastTimeLeft = 0;
            }
        }, AmitActivity.toastTimeLeft);
    }

    public void activityDying() {
        if (AmitActivity.toastTimeLeft != 0 && timeOfToast != 0) {
            AmitActivity.toastTimeLeft = AmitActivity.toastTimeLeft - ((System.nanoTime()-timeOfToast)/1000000L);
            if (AmitActivity.toastTimeLeft < 0)
                AmitActivity.toastTimeLeft = 0;
            handler.removeCallbacksAndMessages(null);
        }
    }
}
