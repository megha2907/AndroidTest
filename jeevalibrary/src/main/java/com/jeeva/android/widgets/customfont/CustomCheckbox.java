package com.jeeva.android.widgets.customfont;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.CheckBox;

import com.jeeva.android.R;


public class CustomCheckbox extends CheckBox {

    public CustomCheckbox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (!isInEditMode())
            init(context, attrs);
    }

    public CustomCheckbox(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())
            init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomCheckbox);
        CustomFont.getInstance().setFont(context, this, a.getInt(R.styleable.CustomCheckbox_typeface, -1));
    }

    @Override
    public int getCompoundPaddingLeft() {
        final float scale = this.getResources().getDisplayMetrics().density;
        return (super.getCompoundPaddingLeft() + (int) (2.0f * scale + 0.5f));
    }

    @Override
    public int getCompoundPaddingBottom() {
        final float scale = this.getResources().getDisplayMetrics().density;
        return (super.getCompoundPaddingBottom() + (int) (2.0f * scale + 0.5f));
    }

    @Override
    public int getCompoundPaddingTop() {
        final float scale = this.getResources().getDisplayMetrics().density;
        return (super.getCompoundPaddingTop() + (int) (2.0f * scale + 0.5f));
    }

    @Override
    public int getCompoundPaddingRight() {
        final float scale = this.getResources().getDisplayMetrics().density;
        return (super.getCompoundPaddingRight() + (int) (2.0f * scale + 0.5f));
    }
}