package com.amitbartfeld.myneighborhood.graphical_helpers;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.amitbartfeld.myneighborhood.constants.GraphicalConstants;
import com.squareup.picasso.Picasso;

public class GeneralGraphicOperations {

    public static int getScreenWidth(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static int getScreenHeight(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels - getActionBarHeight(activity);
    }

    public static int getActionBarHeight(Context context) {
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            int actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,context.getResources().getDisplayMetrics());
            return actionBarHeight;
        }
        return 0;
    }

    public static void setMargins(View v, ViewParent parent) {
        if (parent.getClass().equals(LinearLayoutCompat.class))
            ((LinearLayoutCompat.LayoutParams)v.getLayoutParams()).setMargins(GraphicalConstants.viewMargins, GraphicalConstants.viewMargins, GraphicalConstants.viewMargins, GraphicalConstants.viewMargins);
        else if (parent.getClass().equals(ConstraintLayout.class))
            ((ConstraintLayout.LayoutParams)v.getLayoutParams()).setMargins(GraphicalConstants.viewMargins, GraphicalConstants.viewMargins, GraphicalConstants.viewMargins, GraphicalConstants.viewMargins);
        else if (parent.getClass().equals(RelativeLayout.class))
            ((RelativeLayout.LayoutParams)v.getLayoutParams()).setMargins(GraphicalConstants.viewMargins, GraphicalConstants.viewMargins, GraphicalConstants.viewMargins, GraphicalConstants.viewMargins);
        else
            throw new RuntimeException("View's layout not supported. Please ask Amit to add support to this layout.");
    }

    public static String getTextFromPaintForWidth(int textWidth, Paint paint, String text) {
        int currentSubstringStart = 0;
        String textToMeasure = text;
        while (getTextWidthFromPaint(textToMeasure, paint) > textWidth) {
            String subString = getSubstringFromPaintForWidth(textWidth, paint, textToMeasure);
            textToMeasure = text.substring(currentSubstringStart+subString.length());
            text = text.substring(0, currentSubstringStart)+subString + "\n" + textToMeasure;
            currentSubstringStart+=subString.length()+1;
        }
        return text;
    }

    public static String getSubstringFromPaintForWidth(int textWidth, Paint paint, String text) {
        String subString = "";
        int currentCharIndex = 0;
        while (getTextWidthFromPaint(subString, paint) < textWidth && currentCharIndex < text.length()) {
            subString+=text.charAt(currentCharIndex);
            currentCharIndex++;
        }
        return subString.substring(0, subString.length()-1);
    }

    public static int getRowHeightFromPaint(String row, Paint paint) {
        Rect bounds = new Rect();
        paint.getTextBounds(row, 0, row.length(), bounds);
        return bounds.height();
    }

    public static int getRowHeightFromPaintBelowBaseline(String row, Paint paint) {
        Rect bounds = new Rect();
        paint.getTextBounds(row, 0, row.length(), bounds);
        return bounds.bottom;
    }

    public static int getMaxHeightFromPaint(Paint paint) {
        Paint.FontMetrics metrics = paint.getFontMetrics();
        return (int) (metrics.descent - metrics.ascent);
    }

    public static int getTextHeightFromPaint(String text, Paint paint) {
        int height = 0;
        String[] rows = getTextRowArray(text);
        for (String row : rows)
            height+=getRowHeightFromPaint(row, paint)+ GraphicalConstants.linePadding;
        return height;
    }

    public static int getTextWidthFromPaint(String text, Paint paint) {
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return bounds.width();
    }

    public static boolean isRTLRow(String row) {
        if (row.isEmpty())
            return false;
        return Character.getDirectionality(row.charAt(0)) == Character.DIRECTIONALITY_RIGHT_TO_LEFT || Character.getDirectionality(row.charAt(0)) == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC;
    }



    public static String[] getTextRowArray(String text) {
        return text.split("\n");
    }

    public static void loadUrlToImageView(AppCompatImageView imageView, String url) {
        Picasso.get().load(url).into(imageView);
    }

}
