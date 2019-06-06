/*
 * Copyright 2019 Phenix Real Time Solutions, Inc. Confidential and Proprietary. All Rights Reserved.
 *
 *  By using this code you agree to the Phenix Terms of Service found online here:
 *  http://phenixrts.com/terms-of-service.html
 */
package com.phenixrts.examples.webview;

import android.annotation.TargetApi;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * WebView based channel viewer.<br/>
 * To publish a video to this channel, open the following link in your web browser:
 * "https://phenixrts.com/channel/publish/#{@link MainActivity#CHANNEL_ALIAS}"
 */
public class MainActivity extends AppCompatActivity {

  private static String TAG = MainActivity.class.getSimpleName();

  private static final String ORIGIN = "https://phenixrts.com/";

  /**
   * The channel alias is derived from the channel name.
   * For example, a channel with name "My Channel Name!" will have the alias "myChannelName".
   */
  private static final String CHANNEL_ALIAS = "webViewDemo";

  /**
   * Subscribe link to channel with alias {@link MainActivity#CHANNEL_ALIAS}
   */
  private static final String SUBSCRIBE_URL = "https://phenixrts.com/channel/?mss=mr#" + CHANNEL_ALIAS;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    WebView myWebView = (WebView) findViewById(R.id.myWebView);
    myWebView.loadUrl(SUBSCRIBE_URL);
    myWebView.setWebViewClient(new WebViewClient() {
      @SuppressWarnings("deprecation")
      @Override
      public boolean shouldOverrideUrlLoading(WebView view, String url) {

        Log.i("WebView", " ==> " + url);
        view.loadUrl(url);
        return true;
      }

      public boolean onConsoleMessage(ConsoleMessage cm) {
        Log.d("WebView",
            cm.message() + " -- From line " + cm.lineNumber() + " of " + cm.sourceId());
        return true;
      }

      @Override
      public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        // Handle the error and notify the user
        Log.e(TAG, "SSL error: " + error);
        handler.proceed();
      }
    });

    myWebView.setWebChromeClient(new WebChromeClient() {
      // Need to accept permissions to use the camera and audio
      @Override
      public void onPermissionRequest(final PermissionRequest request) {
        Log.d("WebView", "onPermissionRequest");
        MainActivity.this.runOnUiThread(new Runnable() {
          @TargetApi(Build.VERSION_CODES.LOLLIPOP)
          @Override
          public void run() {
            // Make sure the request is coming from our file
            // Warning: This check may fail for local files
            if (request.getOrigin().toString().equals(ORIGIN)) {
              request.grant(request.getResources());
            } else {
              request.deny();
            }
          }
        });
      }
    });

    WebSettings webSettings = myWebView.getSettings();
    webSettings.setJavaScriptEnabled(true);
    webSettings.setAllowFileAccessFromFileURLs(true);
    webSettings.setAllowUniversalAccessFromFileURLs(true);
    webSettings.setMediaPlaybackRequiresUserGesture(false);
    Log.i("WebView", webSettings.getUserAgentString());
  }
}
