package com.amitbartfeld.myneighborhood.activities.used_activities.only_main_screen;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.amitbartfeld.myneighborhood.activities.abstract_activities.amit_activities.screen_type_activities.MainScreenRegularActivity;
import com.amitbartfeld.myneighborhood.operations.CloudOperations;
import com.amitbartfeld.myneighborhood.operations.GeneralOperations;
import com.amitbartfeld.myneighborhood.constants.MessagesTexts;
import com.amitbartfeld.myneighborhood.R;
import com.amitbartfeld.myneighborhood.graphic_items.MessagesGraphicItem;
import com.amitbartfeld.myneighborhood.listeners.AmitUploadListenerFirestore;
import com.amitbartfeld.myneighborhood.properties.MessagesItemProperties;
import com.amitbartfeld.myneighborhood.properties.type.CloudProperties;
import com.amitbartfeld.myneighborhood.properties.type.ItemProperties;

import java.util.Locale;

public class MessagesRegularActivity extends MainScreenRegularActivity {

    ImageButton send;
    EditText textBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hidePlusButton();
        findViewById(R.id.send_layout).setVisibility(View.VISIBLE);
        send = findViewById(R.id.send);
        textBox = findViewById(R.id.textbox);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    @Override
    protected ItemProperties getGeneralItemProperty() {
        return new MessagesItemProperties(mainScreen.getGeneralOperations(), "");
    }

    @Override
    public void setScreenType() {
        screenType = GeneralOperations.ScreenTypeGeneralOperations.ScreenType.Messages;
    }


    public void sendMessage() {
        String text = textBox.getText().toString().trim();
        textBox.setText("");
        MessagesItemProperties properties = new MessagesItemProperties(mainScreen.getGeneralOperations(), text);
        MessagesGraphicItem item = (MessagesGraphicItem) View.inflate(this, R.layout.messages_item_layout, null);
        item.init(this, properties, lili, mainScreen.getGeneralOperations());
        CloudOperations.setItemPropertyDocumentInFirestore(properties, new AmitUploadListenerFirestore() {
            @Override
            public void onUploadFinished(CloudProperties properties) {
                GeneralOperations.TTS("sent!", new Locale("en-US"), MessagesRegularActivity.this);
                scrollView.scrollTo(0, scrollView.getBottom());
            }

            @Override
            public void onError(String message) {
                showErrorMessage(MessagesTexts.messageSendingProblem);
            }
        });
    }

}
