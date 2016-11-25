package in.sportscafe.nostragamus.module.webview;

import android.webkit.WebView;

import java.net.URI;
import java.util.Random;

/**
 * Created by deepanshu on 17/8/16.
 */

public class WebSocketFactory {

    /** The app view. */
    WebView mWebView;

    /**
     * Instantiates a new web socket factory.
     *
     * @param mWebView
     *            the app view
     */
    public WebSocketFactory(WebView mWebView) {
        this.mWebView = mWebView;
    }

    public WebSocket getInstance(String url) {
        // use Draft75 by default
        return getInstance(url, WebSocket.Draft.DRAFT75);
    }

    public WebSocket getInstance(String url, WebSocket.Draft draft) {
        WebSocket socket = null;
        Thread th = null;
        try {
            socket = new WebSocket(mWebView, new URI(url), draft, getRandonUniqueId());
            th = socket.connect();
            return socket;
        } catch (Exception e) {
            //Log.v("websocket", e.toString());
            if(th != null) {
                th.interrupt();
            }
        }
        return null;
    }

    /**
     * Generates random unique ids for WebSocket instances
     *
     * @return String
     */
    private String getRandonUniqueId() {
        return "WEBSOCKET." + new Random().nextInt(100);
    }
}
