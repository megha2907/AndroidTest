package com.jeeva.android.widgets.customfont;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Button;

import com.jeeva.android.R;


public class CustomButton extends Button{

    public CustomButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (!isInEditMode())
            init(context, attrs);
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())
            init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomButton);
        CustomFont.getInstance().setFont(context, this, a.getInt(R.styleable.CustomButton_typeface, -1));
    }



}