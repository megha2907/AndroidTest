package com.jeeva.android.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.jeeva.android.R;

public class ShadowLayout extends FrameLayout {

    private int mShadowColor;
    private float mShadowRadius;
    private float mCornerRadius;
    private float mDx;
    private float mDy;
    private int mFillColor;

    public ShadowLayout(Context context) {
        super(context);
        initView(context, null);
    }

    public ShadowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public ShadowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        setWillNotDraw(false);
        initAttributes(context, attrs);

        int xPadding = (int) (mShadowRadius + Math.abs(mDx));
        int yPadding = (int) (mShadowRadius + Math.abs(mDy));
        setPadding(xPadding, yPadding, xPadding, yPadding);
    }

    private void initAttributes(Context context, AttributeSet attrs) {
        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.ShadowLayout, 0, 0);
        if (attr == null) {
            return;
        }

        try {
            mCornerRadius = attr.getDimension(R.styleable.ShadowLayout_sl_cornerRadius, getResources().getDimension(R.dimen.default_corner_radius));
            mShadowRadius = attr.getDimension(R.styleable.ShadowLayout_sl_shadowRadius, getResources().getDimension(R.dimen.default_shadow_radius));
            mDx = attr.getDimension(R.styleable.ShadowLayout_sl_dx, 0);
            mDy = attr.getDimension(R.styleable.ShadowLayout_sl_dy, 0);
            mShadowColor = attr.getColor(R.styleable.ShadowLayout_sl_shadowColor, getResources().getColor(R.color.default_shadow_color));
            mFillColor = attr.getColor(R.styleable.ShadowLayout_sl_fillColor, getResources().getColor(R.color.default_fill_color));
        } finally {
            attr.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        Bitmap outputBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        Canvas outputCanvas = new Canvas(outputBitmap);

        RectF shadowRect = new RectF(
                mShadowRadius,
                mShadowRadius,
                width - mShadowRadius,
                height - mShadowRadius);

        if (mDy > 0) {
            shadowRect.top += mDy;
            shadowRect.bottom -= mDy;
        } else if (mDy < 0) {
            shadowRect.top += Math.abs(mDy);
            shadowRect.bottom -= Math.abs(mDy);
        }

        if (mDx > 0) {
            shadowRect.left += mDx;
            shadowRect.right -= mDx;
        } else if (mDx < 0) {
            shadowRect.left += Math.abs(mDx);
            shadowRect.right -= Math.abs(mDx);
        }

        Paint shadowPaint = new Paint();
        shadowPaint.setAntiAlias(true);
        shadowPaint.setColor(mFillColor);
        shadowPaint.setStyle(Paint.Style.FILL);
        shadowPaint.setShadowLayer(mShadowRadius, mDx, mDy, mShadowColor);

        outputCanvas.drawRoundRect(shadowRect, mCornerRadius, mCornerRadius, shadowPaint);
        canvas.drawBitmap(outputBitmap, 0, 0, null);
    }

    public void setFillColor(int fillColor) {
        this.mFillColor = fillColor;
        invalidate();
    }
}