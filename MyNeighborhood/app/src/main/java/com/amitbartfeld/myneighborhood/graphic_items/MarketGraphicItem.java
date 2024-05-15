package com.amitbartfeld.myneighborhood.graphic_items;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.net.Uri;
import android.telephony.SmsManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.content.ContextCompat;

import com.amitbartfeld.myneighborhood.R;
import com.amitbartfeld.myneighborhood.activityInterfaces.main_screen_helper.MainScreenActivity;
import com.amitbartfeld.myneighborhood.constants.CloudPropertiesNames;
import com.amitbartfeld.myneighborhood.properties.type.CloudProperties;
import com.amitbartfeld.myneighborhood.properties.type.ItemProperties;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.LinkedList;
import java.util.Map;

public class MarketGraphicItem extends GraphicItem {
    public MarketGraphicItem(Context context, AttributeSet set) {
        super(context, set);
    }

    public MarketGraphicItem(Context context) {
        super(context);
    }

    @Override
    public void changeSpecificGraphicFields(ItemProperties itemProperties) {
        initTextByName(CloudPropertiesNames.itemName, itemProperties.properties);
        initPrice(Integer.parseInt(itemProperties.properties.get(CloudPropertiesNames.price).toString()));
        initTime(itemProperties.properties);
        initTextByName(CloudPropertiesNames.location, itemProperties.properties);
        initImageUrl(itemProperties.properties);
        initBoolean(CloudPropertiesNames.isUsed, "מצב: משומש", "מצב: חדש", itemProperties.properties);
        initTextByName(CloudPropertiesNames.text, itemProperties.properties);
        initSMSButton(itemProperties.properties.get(CloudPropertiesNames.phoneNumber).toString(), itemProperties.properties.get(CloudPropertiesNames.itemName).toString(), Integer.parseInt(itemProperties.properties.get(CloudPropertiesNames.price).toString()));
    }

    private void initPrice(int price) {
        ((TextView)findViewById(R.id.price)).setText(price +"₪");
    }

    private void initSMSButton(String phoneNumber, String item, int price) {
        findViewById(R.id.phone_number).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                    mainActivityWrapper.getMainScreen().setRunAfterResult(new Runnable() {
                        @Override
                        public void run() {
                            sendSMS(phoneNumber, item, price);
                        }
                    });
                    mainActivityWrapper.getMainScreen().getPermissionLauncher().launch(Manifest.permission.SEND_SMS);
                } else {
                    sendSMS(phoneNumber, item, price);
                }
            }
        });
    }

    private void sendSMS(String phoneNumber, String item, int price) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, "שלום, שמעתי שברצונך למכור "+item+". אשמח לקנות את המוצר.", null, null);
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setData(Uri.parse("sms:"));
        smsIntent.putExtra("address", phoneNumber);
        smsIntent.putExtra("sms_body","יש מצב שתעשה לי הנחה? "+price+"₪ "+"זה קצת יקר מדי, לא?");
        mainActivityWrapper.getActivity().startActivity(smsIntent);
    }
}
