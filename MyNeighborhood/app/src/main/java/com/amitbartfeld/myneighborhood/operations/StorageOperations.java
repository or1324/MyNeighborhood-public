package com.amitbartfeld.myneighborhood.operations;

import android.content.Context;
import android.content.SharedPreferences;

import com.amitbartfeld.myneighborhood.constants.StorageConstants;

public class StorageOperations {

    public static String getPhoneNumber(Context c) {
        SharedPreferences sharedPreferences = c.getSharedPreferences(StorageConstants.DATABASE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(StorageConstants.PHONE_NUMBER_DATABASE, null);
    }

    public static String getFullName(Context c) {
        SharedPreferences sharedPreferences = c.getSharedPreferences(StorageConstants.DATABASE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(StorageConstants.FULL_NAME_DATABASE, null);
    }

    public static boolean getIsAdmin(Context c) {
        SharedPreferences sharedPreferences = c.getSharedPreferences(StorageConstants.DATABASE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(StorageConstants.IS_ADMIN_DATABASE, false);
    }

    public static String getNeighborhoodCode(Context c) {
        SharedPreferences sharedPreferences = c.getSharedPreferences(StorageConstants.DATABASE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(StorageConstants.NEIGHBORHOOD_CODE_DATABASE, null);
    }

    public static void savePhoneNumber(Context c, String phoneNumber) {
        SharedPreferences sharedPreferences = c.getSharedPreferences(StorageConstants.DATABASE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(StorageConstants.PHONE_NUMBER_DATABASE, phoneNumber);
        editor.apply();
    }

    public static void saveFullName(Context c, String fullName) {
        SharedPreferences sharedPreferences = c.getSharedPreferences(StorageConstants.DATABASE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(StorageConstants.FULL_NAME_DATABASE, fullName);
        editor.apply();
    }

    public static void saveIsAdmin(Context c, boolean isAdmin) {
        SharedPreferences sharedPreferences = c.getSharedPreferences(StorageConstants.DATABASE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(StorageConstants.IS_ADMIN_DATABASE, isAdmin);
        editor.apply();
    }

    public static void saveNeighborhoodCode(Context c, String neighborhoodCode) {
        SharedPreferences sharedPreferences = c.getSharedPreferences(StorageConstants.DATABASE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(StorageConstants.NEIGHBORHOOD_CODE_DATABASE, neighborhoodCode);
        editor.apply();
    }

    public static void saveEverything(String phoneNumber, String fullName, boolean isAdmin, String neighborhoodCode, Context c) {
        saveIsAdmin(c, isAdmin);
        saveNeighborhoodCode(c, neighborhoodCode);
        saveFullName(c, fullName);
        savePhoneNumber(c, phoneNumber);
    }

    public static void deleteEverything(Context c) {
        SharedPreferences sharedPreferences = c.getSharedPreferences(StorageConstants.DATABASE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(StorageConstants.IS_ADMIN_DATABASE, false);
        editor.putString(StorageConstants.NEIGHBORHOOD_CODE_DATABASE, "");
        editor.putString(StorageConstants.FULL_NAME_DATABASE, null);
        editor.putString(StorageConstants.PHONE_NUMBER_DATABASE, null);
        editor.apply();
    }

}
