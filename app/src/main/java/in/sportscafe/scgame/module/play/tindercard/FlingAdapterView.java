package in.sportscafe.scgame.module.play.tindercard;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AdapterView;

/**
 * Created by dionysis_lorentzos on 6/8/14
 * for package com.lorentzos.swipecards
 * and project Swipe cards.
 * Use with caution dinausaurs might appear!
 */
abstract class FlingAdapterView extends AdapterView {

    private int heightMeasureSpec;
    private int widthMeasureSpec;


    public FlingAdapterView(Context context) {
        super(context);
    }

    public FlingAdapterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlingAdapterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setSelection(int i) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.widthMeasureSpec = widthMeasureSpec;
        this.heightMeasureSpec = heightMeasureSpec;
    }


    public int getWidthMeasureSpec() {
        return widthMeasureSpec;
    }

    public int getHeightMeasureSpec() {
        return heightMeasureSpec;
    }


}
