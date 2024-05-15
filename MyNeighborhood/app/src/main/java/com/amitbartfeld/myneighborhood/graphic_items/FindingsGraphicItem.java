package com.amitbartfeld.myneighborhood.graphic_items;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.amitbartfeld.myneighborhood.R;
import com.amitbartfeld.myneighborhood.activityInterfaces.main_screen_helper.MainScreenActivity;
import com.amitbartfeld.myneighborhood.constants.CloudPropertiesNames;
import com.amitbartfeld.myneighborhood.properties.type.CloudProperties;
import com.amitbartfeld.myneighborhood.properties.type.ItemProperties;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.LinkedList;
import java.util.Map;

public class FindingsGraphicItem extends GraphicItem{
    public FindingsGraphicItem(Context context, AttributeSet set) {
        super(context, set);
    }

    public FindingsGraphicItem(Context context) {
        super(context);
    }

    @Override
    public void changeSpecificGraphicFields(ItemProperties itemProperties) {
        Map<String, Object> graphicComponents = itemProperties.properties;
        initBoolean(CloudPropertiesNames.isFinding, "מצאתי", "איבדתי", graphicComponents);
        AppCompatTextView finding = findViewById(getResources().getIdentifier(CloudPropertiesNames.isFinding, "id", mainActivityWrapper.getActivity().getPackageName()));
        if (((boolean)graphicComponents.get(CloudPropertiesNames.isFinding)))
            finding.setBackground(AppCompatResources.getDrawable(getContext(), R.drawable.found_background));
        else
            finding.setBackground(AppCompatResources.getDrawable(getContext(), R.drawable.lost_background));
        initImageUrl(graphicComponents);
        initTextByName(CloudPropertiesNames.date, graphicComponents);
        initTextByName(CloudPropertiesNames.phoneNumber, graphicComponents);
        initTextByName(CloudPropertiesNames.fullName, graphicComponents);
        initTextByName(CloudPropertiesNames.text, graphicComponents);
        initTextByName(CloudPropertiesNames.location, graphicComponents);
    }
}
