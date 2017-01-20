package in.sportscafe.nostragamus.module.fuzzysearch;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import java.util.List;

import in.sportscafe.nostragamus.module.common.Adapter;

import static com.google.android.gms.analytics.internal.zzy.s;

/**
 * Created by deepanshu on 5/10/16.
 */
public abstract class AbstractFuzzySearchModelImpl<T> implements AbstractFuzzySearchModel, OnFuzzySearchClickListener {

    public abstract Adapter getAdapter(Context context);

    public abstract void getFuzzySearchDetails(String key);

    public abstract void onNoSearch();

    private Adapter mFuzzySearchAdapter;

    private OnFuzzySearchModelListener onFuzzySearchModelListener;

    private String mCurrentKey;

    public AbstractFuzzySearchModelImpl(OnFuzzySearchModelListener listener) {
        this.onFuzzySearchModelListener = listener;
    }

    @Override
    public void init(Bundle bundle) {
    }

    @Override
    public Adapter createAdapter(Context context) {
        return mFuzzySearchAdapter = getAdapter(context);
    }

    @Override
    public void filterSearch(String key) {
        this.mCurrentKey = key;

        int length = key.length();
        if (length > 2) {
            getFuzzySearchDetails(key);
        } else {
            mFuzzySearchAdapter.clear();
            onNoSearch();
        }
    }

    public <T> void addAll(List<T> items) {
        mFuzzySearchAdapter.clear();
        if(!TextUtils.isEmpty(mCurrentKey)) {
            mFuzzySearchAdapter.addAll(items);
        }
        mFuzzySearchAdapter.notifyDataSetChanged();
    }

    public void onFailed() {
        onFuzzySearchModelListener.onFailed();
    }

    public void onNoInternet() {
        onFuzzySearchModelListener.onNoInternet();
    }

    @Override
    public void onFuzzyLbClick(String name) {
        mFuzzySearchAdapter.clear();
        mFuzzySearchAdapter.notifyDataSetChanged();

        onFuzzySearchModelListener.onSearchItemSelected(name);
    }

    public interface OnFuzzySearchModelListener {

        void onNoInternet();

        void onFailed();

        void onSearchItemSelected(String name);
    }
}