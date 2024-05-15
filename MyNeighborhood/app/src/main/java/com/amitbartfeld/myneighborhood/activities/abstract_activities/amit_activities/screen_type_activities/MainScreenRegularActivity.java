package com.amitbartfeld.myneighborhood.activities.abstract_activities.amit_activities.screen_type_activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ScrollView;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.amitbartfeld.myneighborhood.activities.abstract_activities.AmitActivity;
import com.amitbartfeld.myneighborhood.activities.used_activities.only_main_screen.EventsRegularActivity;
import com.amitbartfeld.myneighborhood.activities.used_activities.only_main_screen.FindingsRegularActivity;
import com.amitbartfeld.myneighborhood.activities.used_activities.MainActivity;
import com.amitbartfeld.myneighborhood.activities.used_activities.only_main_screen.MarketRegularActivity;
import com.amitbartfeld.myneighborhood.activities.used_activities.only_main_screen.MessagesRegularActivity;
import com.amitbartfeld.myneighborhood.activities.used_activities.only_main_screen.ProfessionalsRegularActivity;
import com.amitbartfeld.myneighborhood.activities.abstract_activities.amit_activities.ScreenTypeActivity;
import com.amitbartfeld.myneighborhood.activityInterfaces.main_screen_helper.MainScreenActivity;
import com.amitbartfeld.myneighborhood.activityInterfaces.main_screen_helper.MainScreenActivityHelper;
import com.amitbartfeld.myneighborhood.listeners.InternetErrorDownloadsListenerFirestore;
import com.amitbartfeld.myneighborhood.operations.CloudOperations;
import com.amitbartfeld.myneighborhood.operations.GeneralOperations;
import com.amitbartfeld.myneighborhood.R;
import com.amitbartfeld.myneighborhood.constants.MessagesTexts;
import com.amitbartfeld.myneighborhood.graphic_items.GraphicItem;
import com.amitbartfeld.myneighborhood.properties.type.ItemProperties;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public abstract class MainScreenRegularActivity extends ScreenTypeActivity implements View.OnClickListener, MainScreenActivity {

    public LinearLayoutCompat lili;
    public AppCompatImageButton plusButton;
    public static final int imageButtonNum = 5;
    ImageButton[] imageButtons = new ImageButton[imageButtonNum];
    public ScrollView scrollView;
    public MainScreenActivityHelper mainScreen = new MainScreenActivityHelper();
    private boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main_screen);
        super.onCreate(savedInstanceState);
        mainScreen.onCreate(this);
        startLoading();
        lili = findViewById(R.id.lili);
        lili.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (isFirst) {
                    scrollView.scrollTo(0, scrollView.getBottom());
                    isFirst = false;
                }
            }
        });
        for (int i = 0; i < imageButtonNum; i++) {
            imageButtons[i] = findViewById(getResources().getIdentifier("image_button"+(i+1), "id", getPackageName()));
            imageButtons[i].setOnClickListener(this);
        }
        plusButton = findViewById(R.id.plus_button);
        scrollView = findViewById(R.id.scroll);
        findViewById(GeneralOperations.ScreenTypeGeneralOperations.getButtonIdFromScreenType(screenType)).setBackgroundColor(Color.GRAY);
        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (GeneralOperations.canPressAsyncButton()) {
                    GeneralOperations.asyncButtonWorking();
                    CloudOperations.logout(MainScreenRegularActivity.this);
                    showMessageToast(MessagesTexts.logoutSuccessfully);
                    Intent intent = new Intent(MainScreenRegularActivity.this, MainActivity.class);
                    startActivity(intent);
                    GeneralOperations.asyncButtonStopped();
                }
            }
        });
    }

    @Override
    public void setProgressBar() {
        progressBar = findViewById(R.id.load);
    }

    @Override
    public void setAmitToastView() {
        toast = findViewById(R.id.toast);
    }

    @Override
    public void onClick(View view) {
        // The only imageButtons are in the row in the bottom of the screen
        if (view.getClass() == AppCompatImageButton.class) {
            Intent intent = null;
            if (view.getId() == R.id.image_button1 && getClass() != MarketRegularActivity.class) {
                intent = new Intent(this, MarketRegularActivity.class);
            } else if (view.getId() == R.id.image_button2 && getClass() != EventsRegularActivity.class) {
                intent = new Intent(this, EventsRegularActivity.class);
            } else if (view.getId() == R.id.image_button3 && getClass() != FindingsRegularActivity.class) {
                intent = new Intent(this, FindingsRegularActivity.class);
            } else if (view.getId() == R.id.image_button4 && getClass() != ProfessionalsRegularActivity.class) {
                intent = new Intent(this, ProfessionalsRegularActivity.class);
            } else if (view.getId() == R.id.image_button5 && getClass() != MessagesRegularActivity.class) {
                intent = new Intent(this, MessagesRegularActivity.class);
            }
            if (intent != null) {
                mainScreen.moveToScreen(intent, this);
            }
        }
    }

    protected void hidePlusButton() {
        plusButton.setVisibility(View.GONE);
    }
    protected void showPlusButton() {
        plusButton.setVisibility(View.VISIBLE);
    }

    public GraphicItem[] getGraphicItemsFromDocuments(List<DocumentSnapshot> documents) {
        GraphicItem[] graphicItems = new GraphicItem[documents.size()];
        int counter = 0;
        for (DocumentSnapshot document : documents) {
            graphicItems[counter] = GeneralOperations.ScreenTypeGeneralOperations.getGraphicItemFromScreenType(this, document, lili, screenType, mainScreen.getGeneralOperations());
            counter++;
        }
        return graphicItems;
    }

    public void addDownloadedViewsToActivityAsGraphicItems(List<DocumentSnapshot> documents) {
        for (int i = 0; i < documents.size()-100; i++) {
            documents.get(i).getReference().delete();
            documents.remove(i);
        }
        getGraphicItemsFromDocuments(documents);
        stopLoading();
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    protected abstract ItemProperties getGeneralItemProperty();

    private void downloadViews() {
        CloudOperations.getPropertyCollectionFromCloudFirestore(getGeneralItemProperty(), new InternetErrorDownloadsListenerFirestore(this) {

            @Override
            public void onDownloadsFinished(List<DocumentSnapshot> documents) {
                addDownloadedViewsToActivityAsGraphicItems(documents);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    @Override
    public AmitActivity getActivity() {
        return this;
    }

    @Override
    public MainScreenActivityHelper getMainScreen() {
        return mainScreen;
    }

    @Override
    protected void onStart() {
        super.onStart();
        lili.removeAllViews();
        downloadViews();
    }
}