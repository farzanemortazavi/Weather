package com.sematec.weather;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class CustomTextViewVazir extends AppCompatTextView {
    public CustomTextViewVazir(Context context) {
        super(context);
    }

    public CustomTextViewVazir(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface typeface=Typeface.createFromAsset(context.getAssets(),"vazir.ttf");
        this.setTypeface(typeface);
        this.setTextColor(Color.BLACK);
    }
}
