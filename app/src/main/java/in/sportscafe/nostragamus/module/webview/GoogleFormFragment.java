package in.sportscafe.nostragamus.module.webview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.jeeva.android.Log;

import in.sportscafe.nostragamus.R;

/**
 * Created by Jeeva on 26/3/16.
 */
public class GoogleFormFragment extends AbstractWebViewFragment {

    private static final String TAG = "GoogleFormFragment";

    private static final String FORM_RESPONSE_KEY = "formResponse";

    private static final String LOAD_URL = "loadUrl";

    private OnGoogleFormListener mGoogleFormListener;

    public static GoogleFormFragment newInstance(String loadUrl) {
        Log.d("loadUrl", loadUrl + "");

        Bundle args = new Bundle();
        args.putString(LOAD_URL, loadUrl);

        GoogleFormFragment fragment = new GoogleFormFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnGoogleFormListener) {
            this.mGoogleFormListener = (OnGoogleFormListener) context;
        } else {
            throw new IllegalArgumentException("The activity should implement the OnGoogleFormListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_link, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(null == savedInstanceState) {
            loadUrl(getLoadUrl());
        }
    }

    @Override
    public WebView getWebView() {
        return (WebView) findViewById(R.id.link_wv);
    }

    @Override
    public boolean handleUrlLoading(String url) {
        Log.d(TAG, "Redirect Url --> " + url);

        if(url.contains(FORM_RESPONSE_KEY)) {
            mGoogleFormListener.onFormSubmitted();
            return true;
        }

        return super.handleUrlLoading(url);
    }

    private String getLoadUrl() {
        return getArguments().getString(LOAD_URL);
    }

    public interface OnGoogleFormListener {

        void onFormSubmitted();
    }
}