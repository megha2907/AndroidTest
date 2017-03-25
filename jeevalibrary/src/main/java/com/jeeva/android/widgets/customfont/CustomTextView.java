package com.jeeva.android.widgets.customfont;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import com.jeeva.android.R;


public class CustomTextView extends TextView {

    private boolean isDestroyed = false;


    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode())
            init(context, attrs, defStyleAttr);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())
            init(context, attrs, android.R.attr.textViewStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView, defStyleAttr, 0);
        CustomFont.getInstance().setFont(context, this, a.getInt(R.styleable.CustomTextView_typeface, -1));
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isDestroyed = true;
    }
}