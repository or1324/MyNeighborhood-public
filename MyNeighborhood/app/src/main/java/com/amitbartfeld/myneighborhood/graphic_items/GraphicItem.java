package com.amitbartfeld.myneighborhood.graphic_items;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.amitbartfeld.myneighborhood.activities.abstract_activities.amit_activities.ScreenTypeActivity;
import com.amitbartfeld.myneighborhood.activities.abstract_activities.amit_activities.screen_type_activities.MainScreenRegularActivity;
import com.amitbartfeld.myneighborhood.activityInterfaces.main_screen_helper.MainScreenActivity;
import com.amitbartfeld.myneighborhood.constants.CloudPropertiesNames;
import com.amitbartfeld.myneighborhood.constants.MessagesTexts;
import com.amitbartfeld.myneighborhood.graphical_helpers.GeneralGraphicOperations;
import com.amitbartfeld.myneighborhood.listeners.InternetErrorUploadListenerFirestore;
import com.amitbartfeld.myneighborhood.operations.CloudOperations;
import com.amitbartfeld.myneighborhood.operations.GeneralOperations;
import com.amitbartfeld.myneighborhood.properties.type.CloudProperties;
import com.amitbartfeld.myneighborhood.properties.type.ItemProperties;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public abstract class GraphicItem extends ConstraintLayout {

    MainScreenActivity mainActivityWrapper;
    private ItemProperties properties;
    private GeneralOperations operations;

    public void init(MainScreenActivity wrapper, ItemProperties properties, LinearLayoutCompat parent, GeneralOperations operations) {
        this.properties = properties;
        this.operations = operations;
        initGenerally(wrapper, parent);
    }

    private void initGenerally(MainScreenActivity wrapper, LinearLayoutCompat parent) {
        setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        parent.addView(this);
        mainActivityWrapper = wrapper;
        if (operations.isAdmin) {
            setClickable(true);
            setFocusable(true);
            setLongClickable(true);
            setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("האם אתה בטוח שברצונך למחוק את הפריט הנוכחי מהשכונה?").setPositiveButton("כן!!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            CloudOperations.removeItemPropertyFromFirestore(properties, new InternetErrorUploadListenerFirestore((ScreenTypeActivity) mainActivityWrapper.getActivity()) {
                                @Override
                                public void onUploadFinished(CloudProperties properties) {
                                    mainActivityWrapper.getActivity().showMessageToast(MessagesTexts.deletedItem);
                                }
                            });
                        }
                    }).show();
                    return false;
                }
            });
        }
        dealWithGraphics();
    }

    public GraphicItem(Context context, AttributeSet set) {
        super(context, set);
    }

    public GraphicItem(Context context) {
        super(context);
    }

    private void dealWithGraphics() {
        changeSpecificGraphicFields(properties);
    }

    public abstract void changeSpecificGraphicFields(ItemProperties properties);

    protected void initTextByName(String name, Map<String, Object> graphicComponents) {
        AppCompatTextView textView = findViewById(getResources().getIdentifier(name, "id", mainActivityWrapper.getActivity().getPackageName()));
        textView.setText(graphicComponents.get(name).toString());
    }

    protected void initTime(Map<String, Object> graphicComponents) {
        AppCompatTextView textView = findViewById(getResources().getIdentifier(CloudPropertiesNames.time, "id", mainActivityWrapper.getActivity().getPackageName()));
        long time = (long) graphicComponents.get(CloudPropertiesNames.time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm dd.MM.yyyy");
        String timeText = simpleDateFormat.format(new Date(time));
        textView.setText(timeText);
    }

    protected void initImageUrl(Map<String, Object> graphicComponents) {
        AppCompatImageView appCompatImageView = findViewById(getResources().getIdentifier(CloudPropertiesNames.imageBitmapUrl, "id", mainActivityWrapper.getActivity().getPackageName()));
        GeneralGraphicOperations.loadUrlToImageView(appCompatImageView, graphicComponents.get(CloudPropertiesNames.imageBitmapUrl).toString());
    }

    protected void initBoolean(String name, String textOptionTrue, String textOptionFalse, Map<String, Object> graphicComponents) {
        AppCompatTextView textView = findViewById(getResources().getIdentifier(name, "id", mainActivityWrapper.getActivity().getPackageName()));
        if (((boolean)graphicComponents.get(name))) {
            textView.setText(textOptionTrue);
        } else {
            textView.setText(textOptionFalse);
        }
    }

    protected void initButton(String name, Runnable onClick) {
        View button = findViewById(getResources().getIdentifier(name, "id", mainActivityWrapper.getActivity().getPackageName()));
        View.OnClickListener listener = v -> onClick.run();
        button.setOnClickListener(listener);
    }

}
