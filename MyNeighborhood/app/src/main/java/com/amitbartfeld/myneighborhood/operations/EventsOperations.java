package com.amitbartfeld.myneighborhood.operations;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.provider.CalendarContract;

import androidx.core.content.ContextCompat;

import com.amitbartfeld.myneighborhood.constants.CloudPropertiesNames;
import com.amitbartfeld.myneighborhood.listeners.AmitUploadListenerFirestore;
import com.amitbartfeld.myneighborhood.properties.EventsItemProperties;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class EventsOperations {

    public static void saveEvents(Set<String> events, Context context) {
        SharedPreferences preferences = context.getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet("events", events);
        editor.apply();
    }

    public static boolean doIArriveToThisEvent(EventsItemProperties eventsItemProperties, GeneralOperations generalOperations) {
        return ((List<String>)eventsItemProperties.properties.get(CloudPropertiesNames.peopleComing)).contains(generalOperations.phoneNum);
    }

    public static void addMeToThisEventAsync(EventsItemProperties eventsItemProperties, GeneralOperations generalOperations, AmitUploadListenerFirestore listener) {
        ((List<String>)eventsItemProperties.properties.get(CloudPropertiesNames.peopleComing)).add(generalOperations.phoneNum);
        CloudOperations.setItemPropertyDocumentInFirestore(eventsItemProperties, listener);
    }

    public static void removeMeFromThisEventAsync(EventsItemProperties eventsItemProperties, GeneralOperations generalOperations, AmitUploadListenerFirestore listener) {
        ((List<String>)eventsItemProperties.properties.get(CloudPropertiesNames.peopleComing)).remove(generalOperations.phoneNum);
        CloudOperations.setItemPropertyDocumentInFirestore(eventsItemProperties, listener);
    }

    //returns whether or not the user will arrive after the switch.
    public static boolean switchMyArrivingStateToThisEvent(EventsItemProperties eventsItemProperties, GeneralOperations generalOperations, AmitUploadListenerFirestore listener) {
        if (doIArriveToThisEvent(eventsItemProperties, generalOperations)) {
            removeMeFromThisEventAsync(eventsItemProperties, generalOperations, listener);
            return false;
        }
        else {
            addMeToThisEventAsync(eventsItemProperties, generalOperations, listener);
            return true;
        }
    }

    public static int getNumOfPeopleArrivingToThisEvent(EventsItemProperties eventsItemProperties) {
        return ((List<String>)eventsItemProperties.properties.get(CloudPropertiesNames.peopleComing)).size();
    }

    //returns null if there is no such code
    public static Integer getAnAvailableCodeThatIsLessThan(int maxCode, Set<Integer> eventCodes) {
        for (int i = 1; i < maxCode; i++) {
            if (!eventCodes.contains(i))
                return i;
        }
        return null;
    }

    public static HashSet<Integer> getEventCodesSet(Set<String> events) {
        HashSet<Integer> codesSet = new HashSet<>();
        for (String event : events) {
            int currentCode = getStorageEventCode(event);
            codesSet.add(currentCode);
        }
        return codesSet;
    }

    public static int getMaxPreviousCode(Set<Integer> codesSet) {
        int maxCode = 0;
        for (int code : codesSet) {
            if (code > maxCode)
                maxCode = code;
        }
        return maxCode;
    }

    public static String getEventFromStorageByTime(long creationTime, Set<String> events) {
        for (String event : events)
            if (getStorageEventTime(event) == creationTime)
                return event;
        return null;
    }

    public static int getStorageEventCode(String event) {
        return Integer.parseInt(event.split(getEventSeparator())[1]);
    }

    public static long getStorageEventTime(String event) {
        return Long.parseLong(event.split(getEventSeparator())[0]);
    }

    public static String getStorageEventDescription(String event) {
        return event.split(getEventSeparator())[2];
    }

    public static String getEventSeparator() {
        return "_";
    }

    public static LinkedList<String> getEventsAtTime(long time, Context context) {
        String[] projection = new String[]{CalendarContract.Instances.EVENT_ID, CalendarContract.Instances.TITLE, CalendarContract.Instances.BEGIN, CalendarContract.Instances.END};
        Cursor cursor = CalendarContract.Instances.query(context.getContentResolver(), projection, time, time);
        LinkedList<String> events = new LinkedList<>();
        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            do {
                events.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        return events;
    }
}
