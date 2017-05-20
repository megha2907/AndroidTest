package in.sportscafe.nostragamus.module.common;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import in.sportscafe.nostragamus.R;

/**
 * Created by deepanshi on 5/20/17.
 */

public class NostragamusWebView extends AppCompatActivity {

    private WebView webView;
    private Toolbar mtoolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_webview);

        webView = (WebView) findViewById(R.id.webView);

        if( savedInstanceState == null ) {
            String url =
                    getIntent().getDataString().replace("myscheme://", "http://");
            startWebView(url);
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
                try{

                }catch(Exception exception){
                    exception.printStackTrace();
                }
            }

        });

        // Javascript inabled on webview
        webView.getSettings().setJavaScriptEnabled(true);


        //Load url in webview
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