package in.sportscafe.nostragamus.module.resultspeek;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jeeva.android.widgets.CustomProgressbar;

import in.sportscafe.nostragamus.R;
import in.sportscafe.nostragamus.module.common.NostraBaseActivity;

/**
 * Created by deepanshi on 12/5/16.
 */

public class FeedWebView extends NostraBaseActivity {

    private WebView webView;
    private Toolbar mtoolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setShouldAnimateActivity(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_webview);

        webView = (WebView) findViewById(R.id.webView);

        if( savedInstanceState == null ) {
            String mUrl = getIntent().getStringExtra("url");
            startWebView(mUrl);
        }

    }

    private void startWebView(String url) {

        //Create new webview Client to show progress dialog
        //When opening a url or click on link

        webView.setWebViewClient(new WebViewClient() {

            //If you will not use this method url links are opeen in new brower not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            //Show loader on url load
            public void onLoadResource (WebView view, String url) {

            }
            public void onPageFinished(WebView view, String url) {
//                CustomProgressbar.getProgressbar(FeedWebView.this).dismissProgress();
            }

        });

        // Javascript inabled on webview
        webView.getSettings().setJavaScriptEnabled(true);


        //Load url in webview
//        CustomProgressbar.getProgressbar(FeedWebView.this).show();
        webView.loadUrl(url);

        initToolBar();


    }

    @Override
    // Detect when the back button is pressed
    public void onBackPressed() {
        if(webView.canGoBack()) {
            webView.goBack();
        } else {
            // Let the system handle the back button
            super.onBackPressed();
        }
    }

    public void initToolBar() {
        mtoolbar = (Toolbar) findViewById(R.id.webview_toolbar);
        mtoolbar.setTitle("Nostragamus");
        setSupportActionBar(mtoolbar);
        mtoolbar.setNavigationIcon(R.drawable.back_icon_grey);
        mtoolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }

        );
    }


}