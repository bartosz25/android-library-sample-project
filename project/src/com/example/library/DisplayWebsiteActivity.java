package com.example.library;
  
import com.example.library.context.MainApplication;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle; 
import android.util.Log; 
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;
import android.webkit.WebStorage;

public class DisplayWebsiteActivity extends BaseActivity {

    private final static String LOG_TAG = DisplayWebsiteActivity.class.getName();
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, "Creating ContactFormActivity");
        super.onCreate(savedInstanceState);
        final Activity activity = this;
        setContentView(R.layout.display_website);

        final Resources resources = getResources();

        webView = (WebView) findViewById(R.id.displayWebsite);
        webView.getSettings().setJavaScriptEnabled(false);
        webView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                String messageTemplate = resources.getString(R.string.error_loading_webiste);
                Log.d(LOG_TAG, String.format(messageTemplate, description));
                Toast.makeText(activity, String.format(messageTemplate, description), Toast.LENGTH_LONG).show();
            }
        });
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.getSettings().setAppCacheMaxSize(1024*1024*8); // cache size is 8 mb
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        Log.d(LOG_TAG, "Found cache repository " + appCachePath);
        //webView.getSettings().setAppCachePath(appCachePath);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAppCacheEnabled(true); 
        // decomment if you want to see onReceivedError event :         
    //webView.loadUrl("http://www.google_88888____çç.com");
        webView.loadUrl(MainApplication.BASE_URL+"?source=androidApp");
    }

    /**
     * Override back button behaviour. It behaves like browser back button
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}