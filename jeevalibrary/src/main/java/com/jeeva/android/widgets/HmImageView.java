/**
 * Copyright (C) 2013 The Android Open Source Project
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jeeva.android.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.jeeva.android.ExceptionTracker;
import com.jeeva.android.Log;
import com.jeeva.android.R;
import com.jeeva.android.volley.Volley;

/**
 * Handles fetching an image from a URL as well as the life-cycle of the
 * associated request.
 */
public class HmImageView extends ImageView {

    private static final String TAG = "HmImageView";

    public interface FillType {

        int WIDTH = 1;

        int HEIGHT = 2;
    }

    public interface CropType {

        int LEFT = 1;

        int TOP = 2;

        int RIGHT = 4;

        int BOTTOM = 8;
    }

    /**
     * Current ImageContainer. (either in-flight or finished)
     */
    private ImageContainer mImageContainer;

    /**
     * The URL of the network image to load
     */
    private String mUrl;

    /**
     * Resource ID of the image to be used as a placeholder until the network image is loaded.
     */
    private int mDefaultImageId;

    /**
     * Resource ID of the image to be used if the network response fails.
     */
    private int mErrorImageId;

    private boolean mShowAnimation = true;

    private boolean mLoadImage = true;

    private int mFillType = -1;

    private int mCropType = -1;

    private OnImageLoadedListener mImageLoadedListener;

    private boolean mImageLoaded = false;

    public interface OnImageLoadedListener {
        void onImageLoaded(String url, Bitmap bitmap);
    }

    public HmImageView(Context context) {
        this(context, null);
    }

    public HmImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HmImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if(null != attrs) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HmImageView);
            mDefaultImageId = a.getResourceId(R.styleable.HmImageView_defaultImage, 0);
            mErrorImageId = a.getResourceId(R.styleable.HmImageView_errorImage, 0);
            mShowAnimation = a.getBoolean(R.styleable.HmImageView_showAnimation, true);
            mFillType = a.getInt(R.styleable.HmImageView_fillType, -1);
            mCropType = a.getInt(R.styleable.HmImageView_cropType, -1);

            a.recycle();
        }
    }

    private String formatUrl(String url) {
        // Todo format should be removed after fixed the url space issue's
        if (null != url) {
            url = url.replaceAll(" ", "%20");
        }
        Log.d(TAG, "Image url --> " + url);
        return url;
    }

    public void setImageUrl(String url) {
        mImageLoaded = false;

        mUrl = formatUrl(url);
        // The URL has potentially changed. See if we need to load it.
        loadImageIfNecessary(false);
    }

    public String getImageUrl() {
        return mUrl;
    }

    public void setImageLoadedListener(OnImageLoadedListener loadedListener) {
        this.mImageLoadedListener = loadedListener;
    }

    public void setLoadImage(boolean loadImage) {
        this.mLoadImage = loadImage;
    }

    public boolean isImageLoaded() {
        return mImageLoaded;
    }

    /**
     * Loads the image for the view if it isn't already loaded.
     *
     * @param isInLayoutPass True if this was invoked from a layout pass, false otherwise.
     */
    void loadImageIfNecessary(final boolean isInLayoutPass) {

        if (mLoadImage) {
            int width = getWidth();
            int height = getHeight();

            boolean wrapWidth = false, wrapHeight = false;
            if (getLayoutParams() != null) {
                wrapWidth = getLayoutParams().width == LayoutParams.WRAP_CONTENT;
                wrapHeight = getLayoutParams().height == LayoutParams.WRAP_CONTENT;
            }

            // if the view's bounds aren't known yet, and this is not a wrap-content/wrap-content
            // view, hold off on loading the image.
            boolean isFullyWrapContent = wrapWidth && wrapHeight;
            if (width == 0 && height == 0 && !isFullyWrapContent) {
                return;
            }

            if (TextUtils.isEmpty(mUrl)) {
                // if the URL to be loaded in this view is empty, cancel any old requests and clear the
                // currently loaded image.

                if (mImageContainer != null) {
                    mImageContainer.cancelRequest();
                    mImageContainer = null;
                }
                setDefaultImageOrNull();
                return;
            }

            // if there was an old request in this view, check if it needs to be canceled.
            if (mImageContainer != null && mImageContainer.getRequestUrl() != null) {
                if (mImageContainer.getRequestUrl().equals(mUrl)) {
                    // if the request is from the same URL, return.
                    return;
                } else {
                    // if there is a pre-existing request, cancel it if it's fetching a different URL.
                    mImageContainer.cancelRequest();
                    setDefaultImageOrNull();
                }
            }

            // Calculate the max image width / height to use while ignoring WRAP_CONTENT dimens.
            int maxWidth = wrapWidth ? 0 : width;
            int maxHeight = wrapHeight ? 0 : height;

            // The pre-existing content of this view didn't match the current URL. Load the new image
            // from the network.
            ImageContainer newContainer = Volley.getInstance().getImageLoader().get(mUrl,
                    new ImageListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (mErrorImageId != 0) {
                                setImageResource(mErrorImageId);
                            }
                        }

                        @Override
                        public void onResponse(final ImageContainer response, boolean isImmediate) {
                            // If this was an immediate response that was delivered inside of a layout
                            // pass do not set the image immediately as it will trigger a requestLayout
                            // inside of a layout. Instead, defer setting the image by posting back to
                            // the main thread.
                            Log.d("HmImageView", "isImmediate --> " + isImmediate + ", isInLayoutPass --> " + isInLayoutPass);

                            if (isImmediate && isInLayoutPass) {
                                post(new Runnable() {
                                    @Override
                                    public void run() {
                                        onResponse(response, false);
                                    }
                                });
                                return;
                            }

                            Bitmap bitmap = response.getBitmap();
                            if (null != bitmap) {

                                if(mFillType != -1) {
                                    bitmap = applyFillType(bitmap);
                                }

                                setImageDrawable(new BitmapDrawable(getResources(), bitmap));
                                mImageLoaded = true;

                                if(null != mImageLoadedListener) {
                                    mImageLoadedListener.onImageLoaded(mUrl, bitmap);
                                }

                            } else if (mDefaultImageId != 0) {

                                setImageResource(mDefaultImageId);
                            }
                        }
                    }, maxWidth, maxHeight);

            // update the ImageContainer to be the new bitmap container.
            mImageContainer = newContainer;
        } /*else {
            setDefaultImageOrNull();
        }*/
    }

    private void setDefaultImageOrNull() {
        if (mDefaultImageId != 0) {
            setImageResource(mDefaultImageId);
        } else {
            setImageBitmap(null);
        }
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        if (mShowAnimation) {
            TransitionDrawable td = new TransitionDrawable(new Drawable[]{
                    new ColorDrawable(getResources().getColor(android.R.color.transparent)),
                    drawable
            });
            super.setImageDrawable(td);
            td.startTransition(500);
        } else super.setImageDrawable(drawable);
        invalidate();
    }

    public Bitmap getBitmap() {
        try {
            Drawable drawable = getDrawable();

            if (null == drawable) {
                return null;
            }

            if (drawable instanceof BitmapDrawable) {
                return ((BitmapDrawable) drawable).getBitmap();
            }

            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            ExceptionTracker.track(e);
        }

        return null;
    }

    private Bitmap applyFillType(Bitmap bitmap) {
        Log.d(TAG, "Reached applyFillType");

        float bitmapWidth = bitmap.getWidth();
        float bitmapHeight = bitmap.getHeight();
        float bitmapRatio = bitmapWidth / bitmapHeight;

        Log.d(TAG, "Bitmap Specs, Width --> " + bitmapWidth + "  Height --> " + bitmapHeight + "  Ratio --> " + bitmapRatio);

        int viewWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int viewHeight = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();

        Log.d(TAG, "ImageView Specs, Width --> " + viewWidth + "  Height --> " + viewHeight);

        if (viewWidth > 0 && viewHeight > 0) {
            switch (mFillType) {
                case FillType.WIDTH:
                    Log.d(TAG, "Reached full width");
                    return Bitmap.createScaledBitmap(bitmap, viewWidth, (int) (viewWidth / bitmapRatio), true);
                case FillType.HEIGHT:
                    Log.d(TAG, "Reached full height -- > " + getHeight());
                    return Bitmap.createScaledBitmap(bitmap, (int) (viewHeight * bitmapRatio), viewHeight, true);
            }
        }

        Log.d(TAG, "Both width and height should be greater than zero");
        return bitmap;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        loadImageIfNecessary(true);
    }

    @Override
    protected boolean setFrame(int l, int t, int r, int b) {
        if (getDrawable() == null || mCropType == -1)
            return super.setFrame(l, t, r, b);

        Matrix matrix = getImageMatrix();

        int viewWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int viewHeight = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        int drawableWidth = getDrawable().getIntrinsicWidth();
        int drawableHeight = getDrawable().getIntrinsicHeight();

        //Get the scale
        float scale;
        if (drawableWidth * viewHeight > drawableHeight * viewWidth) {
            scale = (float) viewHeight / (float) drawableHeight;
        } else {
            scale = (float) viewWidth / (float) drawableWidth;
        }

        float left = 0;
        float top = 0;
        float right = drawableWidth;
        float bottom = drawableHeight;

        if(containsFlag(mCropType, CropType.LEFT)) {
            right = viewWidth / scale;
        }

        if(containsFlag(mCropType, CropType.TOP)) {
            bottom = viewHeight / scale;
        }

        if(containsFlag(mCropType, CropType.RIGHT)) {
            left = drawableWidth - (viewWidth / scale);
        }

        if(containsFlag(mCropType, CropType.BOTTOM)) {
            top = drawableHeight - (viewHeight / scale);
        }

        //Define the rect to take image portion from
        RectF drawableRect = new RectF(left, top, right, bottom);
//        RectF drawableRect = new RectF(0, 0, drawableWidth, (viewHeight / scale));


        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        matrix.setRectToRect(drawableRect, viewRect, Matrix.ScaleToFit.FILL);

        setImageMatrix(matrix);

        return super.setFrame(l, t, r, b);
    }

    private boolean containsFlag(int flagSet, int flag){
        return (flagSet|flag) == flagSet;
    }

    @Override
    protected void onDetachedFromWindow() {
        if (mImageContainer != null) {
            // If the view was bound to an image request, cancel it and clear
            // out the image from the view.
            mImageContainer.cancelRequest();
            setImageBitmap(null);
            // also clear out the container so we can reload the image if necessary.
            mImageContainer = null;
        }
        super.onDetachedFromWindow();
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        invalidate();
    }
}