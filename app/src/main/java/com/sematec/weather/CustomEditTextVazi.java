package com.sematec.weather;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

public class CustomEditTextVazi extends AppCompatEditText {
    public CustomEditTextVazi(Context context) {
        super(context);
    }

    public CustomEditTextVazi(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface typeface=Typeface.createFromAsset(context.getAssets(),"vazir.ttf");
        this.setTypeface(typeface);
    }
}
