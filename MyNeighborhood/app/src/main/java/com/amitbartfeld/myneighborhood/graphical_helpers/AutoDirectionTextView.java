package com.amitbartfeld.myneighborhood.graphical_helpers;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

//Has to be used with ConstraintLayout.
public class AutoDirectionTextView extends androidx.appcompat.widget.AppCompatTextView {
    public AutoDirectionTextView(Context context) {
        super(context);
        alignViewByTextDirectionality();
    }

    public AutoDirectionTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        alignViewByTextDirectionality();
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        alignViewByTextDirectionality();
    }

    private void alignViewByTextDirectionality() {
        try {
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) getLayoutParams();
            if (getText() != null)
                if (GeneralGraphicOperations.isRTLRow(getText().toString())) {
                    layoutParams.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
                    layoutParams.leftToLeft = ConstraintLayout.LayoutParams.UNSET;
                } else {
                    layoutParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
                    layoutParams.rightToRight = ConstraintLayout.LayoutParams.UNSET;
                }
        } catch (Exception ignored) {

        }
    }
}
