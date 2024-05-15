package com.amitbartfeld.myneighborhood.graphic_items;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.content.res.AppCompatResources;

import com.amitbartfeld.myneighborhood.R;
import com.amitbartfeld.myneighborhood.constants.CloudPropertiesNames;
import com.amitbartfeld.myneighborhood.properties.type.ItemProperties;

import java.util.Map;

public class MessagesGraphicItem extends GraphicItem {
    public MessagesGraphicItem(Context context, AttributeSet set) {
        super(context, set);
    }

    public MessagesGraphicItem(Context context) {
        super(context);
    }

    @Override
    public void changeSpecificGraphicFields(ItemProperties itemProperties) {
        Map<String, Object> graphicComponents = itemProperties.properties;
        if (graphicComponents.get(CloudPropertiesNames.fullName).equals(mainActivityWrapper.getMainScreen().getGeneralOperations().fullName))
            setBackground(AppCompatResources.getDrawable(getContext(), R.drawable.item_background));
        else
            setBackground(AppCompatResources.getDrawable(getContext(), R.drawable.another_user_message_background));
        initTextByName(CloudPropertiesNames.fullName, graphicComponents);
        initTextByName(CloudPropertiesNames.text, graphicComponents);
        initTime(graphicComponents);
    }
}
