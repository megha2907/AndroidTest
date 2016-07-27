package com.jeeva.android.widgets.customfont;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

import com.jeeva.android.R;


public class CustomAutoTextView extends AutoCompleteTextView /*implements TextWatcher*/ {

    private boolean enoughToFilter;

    public CustomAutoTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (!isInEditMode())
            init(context, attrs);
    }

    public CustomAutoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode())
            init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomAutoTextView);
        enoughToFilter = a.getBoolean(R.styleable.CustomAutoTextView_enoughToFilter, false);
        CustomFont.getInstance().setFont(context, this, a.getInt(R.styleable.CustomAutoTextView_typeface, -1));
    }

    public void setEnoughToFilter(boolean enoughToFilter) {
        this.enoughToFilter = enoughToFilter;
    }

    @Override
    public boolean enoughToFilter() {
        if (enoughToFilter) {
            return true;
        }
        return super.enoughToFilter();
    }

    /*@Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if(enoughToFilter) {
            setText(getText().toString());
        }
    }*/
}