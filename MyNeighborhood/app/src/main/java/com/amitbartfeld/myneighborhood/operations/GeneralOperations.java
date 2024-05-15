package com.amitbartfeld.myneighborhood.operations;

import static com.amitbartfeld.myneighborhood.constants.NextActivityConstants.nextActivitiesIntentExtraFullName;
import static com.amitbartfeld.myneighborhood.constants.NextActivityConstants.nextActivitiesIntentExtraIsAdmin;
import static com.amitbartfeld.myneighborhood.constants.NextActivityConstants.nextActivitiesIntentExtraNeighborhoodNum;
import static com.amitbartfeld.myneighborhood.constants.NextActivityConstants.nextActivitiesIntentExtraPhoneNum;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.amitbartfeld.myneighborhood.R;
import com.amitbartfeld.myneighborhood.activities.abstract_activities.amit_activities.screen_type_activities.MainScreenRegularActivity;
import com.amitbartfeld.myneighborhood.activities.used_activities.only_main_screen.MessagesRegularActivity;
import com.amitbartfeld.myneighborhood.constants.CloudNames;
import com.amitbartfeld.myneighborhood.constants.CloudPropertiesNames;
import com.amitbartfeld.myneighborhood.graphic_items.EventsGraphicItem;
import com.amitbartfeld.myneighborhood.graphic_items.FindingsGraphicItem;
import com.amitbartfeld.myneighborhood.graphic_items.GraphicItem;
import com.amitbartfeld.myneighborhood.graphic_items.MarketGraphicItem;
import com.amitbartfeld.myneighborhood.graphic_items.MessagesGraphicItem;
import com.amitbartfeld.myneighborhood.graphic_items.ProfessionalReviewsGraphicItem;
import com.amitbartfeld.myneighborhood.graphic_items.ProfessionalsGraphicItem;
import com.amitbartfeld.myneighborhood.properties.EventsItemProperties;
import com.amitbartfeld.myneighborhood.properties.FindingsItemProperties;
import com.amitbartfeld.myneighborhood.properties.MarketItemProperties;
import com.amitbartfeld.myneighborhood.properties.MessagesItemProperties;
import com.amitbartfeld.myneighborhood.properties.ProfessionalsItemProperties;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Locale;

public class GeneralOperations {

    public final String phoneNum;
    public final String fullName;
    public final int neighborhoodNum;
    public final boolean isAdmin;
    private static boolean isAsyncButtonPressed = false;
    private static TextToSpeech tts;

    public GeneralOperations(String phone, String name, int neighborhoodNum, boolean isAdmin) {
        phoneNum = phone;
        fullName = name;
        this.neighborhoodNum = neighborhoodNum;
        this.isAdmin = isAdmin;
    }

    public static boolean adminOrMyPhone(String phoneNum, Context c) {
        // check if the user is an admin, else, check if the user's phone number is this phone number.
        return phoneNum.equals(StorageOperations.getPhoneNumber(c)) || StorageOperations.getIsAdmin(c);
    }

    public String getProfessionalsReviewSubUrl(long creationTime) {
        return getSubUrl(CloudNames.reviewsCollectionName, String.valueOf(creationTime));
    }

    public String getSubUrl(String collectionName, String documentName) {
        return "/"+collectionName+"/"+documentName;
    }

    public String getUrlFromScreenTypeAndOrderParameter(ScreenTypeGeneralOperations.ScreenType type, String orderedBy) {
        return getGeneralUrlFromScreenType(type) + orderedBy;
    }

    public String getGeneralUrlFromScreenType(ScreenTypeGeneralOperations.ScreenType type) {
        return CloudOperations.getCloudBaseUrl(neighborhoodNum) + ScreenTypeGeneralOperations.getGeneralSubUrlFromScreenType(type);
    }

    public static void EnterTheApp(AppCompatActivity currentActivity, String fullName, boolean isAdmin, int neighborhoodNum, String phoneNum) {
        Intent intent = new Intent(currentActivity, MessagesRegularActivity.class);
        intent.putExtra(nextActivitiesIntentExtraFullName, fullName);
        intent.putExtra(nextActivitiesIntentExtraIsAdmin, isAdmin);
        intent.putExtra(nextActivitiesIntentExtraNeighborhoodNum, neighborhoodNum);
        intent.putExtra(nextActivitiesIntentExtraPhoneNum, phoneNum);
        currentActivity.startActivity(intent);
    }

    public static void TTS(String text, Locale locale, Context context) {
        tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tts.setLanguage(locale);
                tts.setPitch(2f);
                tts.setSpeechRate(0.5f);
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
            }
        });
    }

    public static void asyncButtonWorking() {
        isAsyncButtonPressed = true;
    }

    public static void asyncButtonStopped() {
        isAsyncButtonPressed = false;
    }

    public static boolean canPressAsyncButton() {
        return !isAsyncButtonPressed;
    }

    public static boolean isInUiThread(Context c) {
        return c.getMainLooper().getThread() == Thread.currentThread();
    }

    public static class ScreenTypeGeneralOperations {
        public enum ScreenType {
            Market, Messages, Professionals, ProfessionalReviews, Findings, Events, Register, Login
        }

        public static GraphicItem getGraphicItemFromScreenType(MainScreenRegularActivity activity, DocumentSnapshot document, LinearLayoutCompat lili, ScreenType type, GeneralOperations generalOperations) {
            switch (type) {
                case Events:
                    EventsGraphicItem eventsGraphicItem = (EventsGraphicItem) getGraphicItemFromXml(activity, R.layout.events_item_layout);
                    eventsGraphicItem.init(activity, document.toObject(EventsItemProperties.class), lili, generalOperations);
                    return eventsGraphicItem;
                case Findings:
                    FindingsGraphicItem findingsGraphicItem = (FindingsGraphicItem) getGraphicItemFromXml(activity, R.layout.findings_item_layout);
                    findingsGraphicItem.init(activity, document.toObject(FindingsItemProperties.class), lili, generalOperations);
                    return findingsGraphicItem;
                case Market:
                    MarketGraphicItem marketGraphicItem = (MarketGraphicItem) getGraphicItemFromXml(activity, R.layout.market_item_layout);
                    marketGraphicItem.init(activity, document.toObject(MarketItemProperties.class), lili, generalOperations);
                    return marketGraphicItem;
                case Professionals:
                    ProfessionalsGraphicItem professionalsGraphicItem = (ProfessionalsGraphicItem) getGraphicItemFromXml(activity, R.layout.professionals_item_layout);
                    professionalsGraphicItem.init(activity, document.toObject(ProfessionalsItemProperties.class), lili, generalOperations);
                    return professionalsGraphicItem;
                case ProfessionalReviews:
                    ProfessionalReviewsGraphicItem professionalReviewsGraphicItem = (ProfessionalReviewsGraphicItem) getGraphicItemFromXml(activity, R.layout.professional_reviews_item_layout);
                    professionalReviewsGraphicItem.init(activity, document.toObject(ProfessionalsItemProperties.class), lili, generalOperations);
                    return professionalReviewsGraphicItem;
                case Messages:
                    MessagesGraphicItem messagesGraphicItem = (MessagesGraphicItem) getGraphicItemFromXml(activity, R.layout.messages_item_layout);
                    messagesGraphicItem.init(activity, document.toObject(MessagesItemProperties.class), lili, generalOperations);
                    return messagesGraphicItem;
            }
            return null;
        }

        private static GraphicItem getGraphicItemFromXml(Activity activity, int layoutFile) {
            return (GraphicItem) View.inflate(activity, layoutFile, null);
        }

        public static String getGeneralSubUrlFromScreenType(ScreenType type) {
            switch (type) {
                case Market:
                    return "/"+ CloudNames.marketDocument+"/";
                case Events:
                    return "/"+CloudNames.eventsDocument+"/";
                case Findings:
                    return "/"+CloudNames.findingsDocument+"/";
                case Messages:
                    return "/"+CloudNames.messagesDocument+"/";
                case Professionals:
                    return "/"+CloudNames.professionalsDocument+"/";
                case Register:
                case Login:
                    return "/"+CloudNames.residentsDocument+"/";
                case ProfessionalReviews:
                    //we can not know it without the phone number of the professional, but firebase uses it, so we need it.
                    return "";
            }
            throw new RuntimeException("The type was not in the 'getGeneralSubUrlFromScreenType' options");
        }

        public static String getOrderByFromScreenType(ScreenType type) {
            switch (type) {
                case Market:
                case Messages:
                case Findings:
                case Events:
                case ProfessionalReviews:
                    return CloudPropertiesNames.time;
                case Professionals:
                case Login:
                case Register:
                    return CloudPropertiesNames.phoneNumber;
            }
            throw new RuntimeException("Unknown Screen Type in getOrderByFromScreenType");
        }

        public static int getButtonIdFromScreenType(ScreenType screenType) {
            switch (screenType) {
                case Market:
                    return R.id.image_button1;
                case Events:
                    return R.id.image_button2;
                case Findings:
                    return R.id.image_button3;
                case Professionals:
                case ProfessionalReviews:
                    return R.id.image_button4;
                case Messages:
                    return R.id.image_button5;
            }
            return 0;
        }
    }

}
