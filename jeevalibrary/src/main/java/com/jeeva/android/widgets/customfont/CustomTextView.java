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
            init(context, attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())
            init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);
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