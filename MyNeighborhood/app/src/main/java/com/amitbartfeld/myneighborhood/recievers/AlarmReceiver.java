package com.amitbartfeld.myneighborhood.recievers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.amitbartfeld.myneighborhood.R;
import com.amitbartfeld.myneighborhood.activities.used_activities.MainActivity;
import com.amitbartfeld.myneighborhood.operations.EventsOperations;

import java.util.HashSet;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        createNotificationChannel(context);
        Intent i = new Intent(context, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,  i, PendingIntent.FLAG_IMMUTABLE);
        String event = intent.getExtras().getString("event");
        SharedPreferences sharedPreferences = context.getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        HashSet<String> events = new HashSet<>(sharedPreferences.getStringSet("events", new HashSet<>()));
        events.remove(event);
        editor.putStringSet("events", events);
        editor.apply();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "amit_event_notification_channel").setSmallIcon(R.drawable.ic_launcher).setContentTitle("יש אירוע עכשיו").setContentText(EventsOperations.getStorageEventDescription(event)).setDefaults(NotificationCompat.DEFAULT_ALL).setPriority(NotificationCompat.PRIORITY_HIGH).setContentIntent(pendingIntent).setOngoing(true);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(EventsOperations.getStorageEventCode(event), builder.build());
    }

    private void createNotificationChannel(Context context) {
        CharSequence name = "event notification channel";
        String description = "this notification channel signals that there is an event now in the neighborhood.";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("amit_event_notification_channel", name, importance);
        channel.setDescription(description);
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}