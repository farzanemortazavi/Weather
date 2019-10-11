package com.sematec.weather;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class CustomTextViewBlackVazir extends AppCompatTextView {
    public CustomTextViewBlackVazir(Context context) {
        super(context);
    }

    public CustomTextViewBlackVazir(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface typeface=Typeface.createFromAsset(context.getAssets(),"vazirblack.ttf");
        this.setTypeface(typeface);
        this.setTextColor(Color.BLACK);
    }
}
