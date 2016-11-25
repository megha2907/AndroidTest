package in.sportscafe.nostragamus.module.webview;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import in.sportscafe.nostragamus.Constants.Alerts;
import in.sportscafe.nostragamus.Nostragamus;
import in.sportscafe.nostragamus.module.common.NostragamusFragment;


/**
 * Created by Jeeva on 26/3/16.
 */
public abstract class AbstractWebViewFragment extends NostragamusFragment {

    public abstract WebView getWebView();

    private WebView mWebView;

    @SuppressLint("JavascriptInterface")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mWebView = getWebView();

        WebSettings settings = mWebView.getSettings();

        // To support for dom storage api
        settings.setDomStorageEnabled(true);

        // To support for javascript
        settings.setJavaScriptEnabled(true);

        // It is to increase the webView performance
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        // It is also to increase the performance
        if (Build.VERSION.SDK_INT >= 19) {
            // chromium, enable hardware acceleration
            mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            // older android version, disable hardware acceleration
            mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        mWebView.addJavascriptInterface(new WebSocketFactory(mWebView), "WebSocketFactory");

        setKeyListener();

        setForProgress();
    }

    public void loadUrl(String url) {
        if(Nostragamus.getInstance().hasNetworkConnection()) {
            mWebView.loadUrl(url);
        } else {
            showMessage(Alerts.NO_NETWORK_CONNECTION, "RETRY",
                    new OnRetryClickListener(url));
        }
    }

    public void loadHtmlContent(String baseUrl, String data,
                                String mimeType, String encoding, String historyUrl) {
        mWebView.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
    }

    /**
     * It is for showing the progressbar while loading the page inside webView
     */
    private void setForProgress() {
        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showProgressbar();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                dismissProgressbar();
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                handleUrlLoading(url);
            }
        });
    }

    public boolean handleUrlLoading(String url) {
        if(Nostragamus.getInstance().hasNetworkConnection()) {
            return false;
        }
        showMessage(Alerts.NO_NETWORK_CONNECTION, "RETRY",
                new OnRetryClickListener(url));
        return true;
    }

    class OnRetryClickListener implements View.OnClickListener {

        private String mUrl;

        public OnRetryClickListener(String url) {
            this.mUrl = url;
        }

        @Override
        public void onClick(View view) {
            mWebView.loadUrl(mUrl);
        }
    }

    /**
     * It is to detect the back press and ask webview to go back to the previous page
     */
    private void setKeyListener() {
        mWebView.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
                    handler.sendEmptyMessage(1);
                    return true;
                }
                return false;
            }

        });
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1: {
                    mWebView.goBack();
                }
                break;
            }
        }
    };
}