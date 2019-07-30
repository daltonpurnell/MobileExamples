/*
 * Copyright 2019 Phenix Real Time Solutions, Inc. Confidential and Proprietary. All Rights Reserved.
 *
 *  By using this code you agree to the Phenix Terms of Service found online here:
 *  http://phenixrts.com/terms-of-service.html
 */
package com.phenixrts.examples.webview;

import android.annotation.SuppressLint;
import android.net.http.SslError;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

/**
 * WebView based channel viewer.<br/> To publish a video to this channel, open the following link in
 * your web browser: "https://phenixrts.com/channel/publish/#{@link MainActivity#CHANNEL_ALIAS}"
 */
public class MainActivity extends AppCompatActivity {

  private static String TAG = MainActivity.class.getSimpleName();

  /**
   * The channel alias is derived from the channel name. For example, a channel with name "My
   * Channel Name!" will have the alias "myChannelName".
   */
  private static final String CHANNEL_ALIAS = "webViewDemo";

  /**
   * Subscribe link to channel with alias {@link MainActivity#CHANNEL_ALIAS}
   */
  private static final String SUBSCRIBE_URL = "https://phenixrts.com/channel/#" + CHANNEL_ALIAS;

  private WebView webView;
  private EditText editUrl;

  @SuppressLint("SetJavaScriptEnabled")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    webView = findViewById(R.id.myWebView);
    editUrl = findViewById(R.id.newUrl);

    editUrl.setText(SUBSCRIBE_URL);
    editUrl.setOnEditorActionListener((v, actionId, event) -> {
      String newUrl = editUrl.getText().toString();
      webView.loadUrl(newUrl);
      return true;
    });

    webView.setWebViewClient(new WebViewClient() {
      @Override
      public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        // Handle the error and notify the user
        Log.e(TAG, "SSL error: " + error);
        handler.proceed();
      }
    });
    // JS is required for video player
    webView.getSettings().setJavaScriptEnabled(true);
    webView.getSettings().setMediaPlaybackRequiresUserGesture(false);

    webView.loadUrl(SUBSCRIBE_URL);
  }
}
