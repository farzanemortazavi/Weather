package com.sematec.weather;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class CustomTextViewBoldVazir extends AppCompatTextView {
    public CustomTextViewBoldVazir(Context context) {
        super(context);
    }

    public CustomTextViewBoldVazir(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface typeface=Typeface.createFromAsset(context.getAssets(),"vazirb.ttf");
        this.setTypeface(typeface);
        this.setTextColor(Color.BLACK);
    }
}
