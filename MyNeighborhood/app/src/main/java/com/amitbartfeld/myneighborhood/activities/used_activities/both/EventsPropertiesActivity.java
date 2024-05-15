package com.amitbartfeld.myneighborhood.activities.used_activities.both;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.widget.AppCompatButton;

import com.amitbartfeld.myneighborhood.activities.abstract_activities.amit_activities.screen_type_activities.MainScreenPropertiesActivity;
import com.amitbartfeld.myneighborhood.operations.CloudOperations;
import com.amitbartfeld.myneighborhood.operations.GeneralOperations;
import com.amitbartfeld.myneighborhood.R;
import com.amitbartfeld.myneighborhood.constants.EditTextHints;
import com.amitbartfeld.myneighborhood.constants.MessagesTexts;
import com.amitbartfeld.myneighborhood.listeners.InternetErrorUploadListenerFirestore;
import com.amitbartfeld.myneighborhood.properties.EventsItemProperties;
import com.amitbartfeld.myneighborhood.properties.type.CloudProperties;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

public class EventsPropertiesActivity extends MainScreenPropertiesActivity {
    Calendar eventDate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatButton pickDate = findViewById(R.id.properties_button);
        pickDate.setVisibility(View.VISIBLE);
        pickDate.setText("בחר תאריך");
        TextView timeTextView = findViewById(R.id.properties_textview);
        timeTextView.setVisibility(View.VISIBLE);
        pickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                eventDate = Calendar.getInstance();
                new DatePickerDialog(
                        // on below line we are passing context.
                        EventsPropertiesActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                eventDate.set(Calendar.YEAR, year);
                                eventDate.set(Calendar.MONTH, monthOfYear);
                                eventDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                Calendar now = Calendar.getInstance();
                                new TimePickerDialog(EventsPropertiesActivity.this, new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                        eventDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                        eventDate.set(Calendar.MINUTE, minute);
                                        SimpleDateFormat format = new SimpleDateFormat();
                                        timeTextView.setText(format.format(eventDate.getTime()));
                                    }
                                }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true).show();
                            }
                        }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    @Override
    public void setScreenType() {
        screenType = GeneralOperations.ScreenTypeGeneralOperations.ScreenType.Events;
    }

    @Override
    public boolean checkInputSpecifically() {
        if (eventDate == null) {
            showErrorMessageToast(MessagesTexts.haveToChooseEventTime);
            return false;
        }
        return true;
    }

    @Override
    public void upload() {
        String title = ((EditText)findViewById(R.id.edittext1)).getText().toString();
        String text = ((EditText)findViewById(R.id.edittext2)).getText().toString();
        String location = ((EditText)findViewById(R.id.edittext3)).getText().toString();
        startLoading();
        CloudOperations.setItemPropertyDocumentInFirestore(new EventsItemProperties(mainScreen.getGeneralOperations(), text, title, location, eventDate.getTimeInMillis(), new LinkedList<>()), new InternetErrorUploadListenerFirestore(this) {
            @Override
            public void onUploadFinished(CloudProperties properties) {
                backToApp(MessagesTexts.eventUploaded);
            }
        });
    }

    @Override
    protected String[] initEditTextNames() {
        return new String[]{EditTextHints.title, EditTextHints.text, EditTextHints.eventLocation};
    }
}
