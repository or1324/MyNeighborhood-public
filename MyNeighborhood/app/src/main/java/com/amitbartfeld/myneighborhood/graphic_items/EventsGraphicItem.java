package com.amitbartfeld.myneighborhood.graphic_items;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.NotificationManagerCompat;

import com.amitbartfeld.myneighborhood.R;
import com.amitbartfeld.myneighborhood.constants.CloudPropertiesNames;
import com.amitbartfeld.myneighborhood.constants.MessagesTexts;
import com.amitbartfeld.myneighborhood.operations.EventsOperations;
import com.amitbartfeld.myneighborhood.properties.EventsItemProperties;
import com.amitbartfeld.myneighborhood.properties.type.ItemProperties;
import com.amitbartfeld.myneighborhood.recievers.AlarmReceiver;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class EventsGraphicItem extends GraphicItem {

    public EventsGraphicItem(Context context, AttributeSet set) {
        super(context, set);
    }

    public EventsGraphicItem(Context context) {
        super(context);
    }

    @Override
    public void changeSpecificGraphicFields(ItemProperties properties) {
        initTime(properties.properties);
        initTextByName(CloudPropertiesNames.title, properties.properties);
        initTextByName(CloudPropertiesNames.text, properties.properties);
        initTextByName(CloudPropertiesNames.location, properties.properties);
        boolean isArriving = EventsOperations.doIArriveToThisEvent((EventsItemProperties) properties, mainActivityWrapper.getMainScreen().getGeneralOperations());
        AppCompatButton button = findViewById(getResources().getIdentifier(CloudPropertiesNames.peopleComing, "id", mainActivityWrapper.getActivity().getPackageName()));
        setBackgroundByArrivalState(isArriving, button);
        button.setText(String.valueOf(EventsOperations.getNumOfPeopleArrivingToThisEvent((EventsItemProperties) properties)));
        initButton(CloudPropertiesNames.peopleComing, () -> {
            boolean isComing = EventsOperations.switchMyArrivingStateToThisEvent((EventsItemProperties) properties, mainActivityWrapper.getMainScreen().getGeneralOperations(), null);
            setBackgroundByArrivalState(isComing, button);
            long numOfPeopleComing = EventsOperations.getNumOfPeopleArrivingToThisEvent((EventsItemProperties) properties);
            button.setText(String.valueOf(numOfPeopleComing));
        });
        initEventRegistration((Long) properties.properties.get(CloudPropertiesNames.time), properties.properties.get(CloudPropertiesNames.text).toString());
    }

    private void initEventRegistration(long creationTime, String description) {
        AppCompatButton registerEvent = findViewById(R.id.register_event);
        SharedPreferences preferences = getContext().getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
        Set<String> events = new HashSet<>(preferences.getStringSet("events", new HashSet<>()));
        boolean isRegistered = (EventsOperations.getEventFromStorageByTime(creationTime, events) != null);
        if (isRegistered)
            registerEvent.setText("בטל");
        else
            registerEvent.setText("הירשם");
        registerEvent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getContext().getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE);
                Set<String> events = new HashSet<>(preferences.getStringSet("events", new HashSet<>()));
                boolean isRegistered = (EventsOperations.getEventFromStorageByTime(creationTime, events) != null);
                if (!isRegistered) {
                    //User is not registered. Need to register.
                    //First, check if the event has passed.
                    if (creationTime < new Date().getTime()) {
                        mainActivityWrapper.getActivity().showMessageToast(MessagesTexts.canNotRegisterEvent);
                        return;
                    }
                    //First check if the user has something in the calender.
                    LinkedList<String> calendarEvents = EventsOperations.getEventsAtTime(creationTime, getContext());
                    if (!calendarEvents.isEmpty()) {
                        //There is an event at this time.
                        new AlertDialog.Builder(getContext()).setMessage("יש לך כבר אירועים בזמן הזה. האירועים:\n"+calendarEvents.toString()).setPositiveButton("Add anyway", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                registerEvent.setText("בטל");
                                registerToEvent(creationTime, description, events);
                                mainActivityWrapper.getActivity().showMessageToast(MessagesTexts.registeredEvent);
                                //At the end, save the new events state.
                                EventsOperations.saveEvents(events, getContext());
                            }
                        }).setNegativeButton("Don't add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();
                    } else {
                        //There is no event at this time
                        registerEvent.setText("בטל");
                        registerToEvent(creationTime, description, events);
                        mainActivityWrapper.getActivity().showMessageToast(MessagesTexts.registeredEvent);
                        //At the end, save the new events state.
                        EventsOperations.saveEvents(events, getContext());
                    }
                } else {
                    //need to unregister
                    registerEvent.setText("הירשם");
                    unregisterFromEvent(creationTime, events);
                    mainActivityWrapper.getActivity().showMessageToast(MessagesTexts.cancelledEvent);
                    //At the end, save the new events state.
                    EventsOperations.saveEvents(events, getContext());
                }
            }
        });
    }

    private void unregisterFromEvent(long creationTime, Set<String> events) {
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        String event = EventsOperations.getEventFromStorageByTime(creationTime, events);
        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        intent.putExtra("event", event);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), EventsOperations.getStorageEventCode(event), intent, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.cancel(pendingIntent);
        events.remove(event);
        NotificationManagerCompat.from(mainActivityWrapper.getActivity()).cancel(EventsOperations.getStorageEventCode(event));
    }

    private void registerToEvent(long creationTime, String description, Set<String> events) {
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Set<Integer> codesSet = EventsOperations.getEventCodesSet(events);
        int maxPreviousCode = EventsOperations.getMaxPreviousCode(codesSet);
        Integer availableCodeInPreviousBounds = EventsOperations.getAnAvailableCodeThatIsLessThan(maxPreviousCode, codesSet);
        int newCode;
        if (availableCodeInPreviousBounds != null)
            newCode = availableCodeInPreviousBounds;
        else
            newCode = maxPreviousCode+1;
        String event = creationTime+EventsOperations.getEventSeparator()+newCode+EventsOperations.getEventSeparator()+description;
        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        intent.putExtra("event", event);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), newCode, intent, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, creationTime, pendingIntent);
        events.add(event);
    }



    private void setBackgroundByArrivalState(boolean isArriving, AppCompatButton button) {
        if (isArriving)
            button.setBackground(AppCompatResources.getDrawable(getContext(), R.drawable.num_of_people_button_arriving));
        else
            button.setBackground(AppCompatResources.getDrawable(getContext(), R.drawable.num_of_people_button_not_arriving));
    }
}
