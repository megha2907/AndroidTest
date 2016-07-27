package com.jeeva.android.widgets.customfont;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

public class CustomFont {

    private String[] mFontPaths = null;

    private static CustomFont sCustomFont = new CustomFont();

    private CustomFont() {
    }

    public static CustomFont getInstance() {
        return sCustomFont;
    }

    public void init(String... fontPaths) {
        this.mFontPaths = fontPaths;
    }

    public void setFont(Context context, TextView textView, int fontValue) {
        if(null != mFontPaths && fontValue > -1 && mFontPaths.length > fontValue) {
            textView.setTypeface(getTypeFace(context, fontValue));
        }
    }

    public Typeface getTypeFace(Context context, int fontValue) {
        return Typefaces.get(context, mFontPaths[fontValue]);
    }
}