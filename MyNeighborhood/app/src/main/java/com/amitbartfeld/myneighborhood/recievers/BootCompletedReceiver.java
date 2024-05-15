package com.amitbartfeld.myneighborhood.recievers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.amitbartfeld.myneighborhood.operations.EventsOperations;

import java.util.HashSet;
import java.util.Set;

public class BootCompletedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            SharedPreferences preferences = context.getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
            Set<String> events = new HashSet<>(preferences.getStringSet("events", new HashSet<>()));
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            for (String event : events) {
                Intent eventIntent = new Intent(context, AlarmReceiver.class);
                eventIntent.putExtra("event", event);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, EventsOperations.getStorageEventCode(event), eventIntent, PendingIntent.FLAG_IMMUTABLE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, EventsOperations.getStorageEventTime(event), pendingIntent);
            }
        }
    }
}