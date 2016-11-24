package com.jeeva.android.volley;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;

public class Volley {

    public static final String TAG = Volley.class.getSimpleName();

    private RequestQueue mRequestQueue;

    private ImageLoader mImageLoader;

    private static Volley sVolley = new Volley();

    public static synchronized Volley getInstance() {
        return sVolley;
    }

    public void initVolley(Context context) {
        mRequestQueue = com.android.volley.toolbox.Volley.newRequestQueue(context);
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }



    public ImageLoader getImageLoader() {
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public void invalidateAll() {
        getCache().clear();
    }

    public void invalidate(String key) {
        getCache().invalidate(key, true);
    }

    public void remove(String key) {
        getCache().remove(getCacheKey(key, 0, 0));
    }

    private Cache getCache() {
        return mRequestQueue.getCache();
    }
    /**
     * Creates a cache key for use with the L1 cache.
     * @param url The URL of the request.
     * @param maxWidth The max-width of the output.
     * @param maxHeight The max-height of the output.
     */
    public String getCacheKey(String url, int maxWidth, int maxHeight) {
        return new StringBuilder(url.length() + 12).append("#W").append(maxWidth)
                .append("#H").append(maxHeight).append(url).toString();
    }

}