/**
 * Copyright 2018 PhenixP2P Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    WebView myWebView = (WebView) findViewById(R.id.myWebView);
    final String origin = "https://phenixrts.com/";
    final String url = "https://phenixrts.com/channel/?mss=mr#webViewDemo";

    myWebView.loadUrl(url);
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
            if (request.getOrigin().toString().equals(origin)) {
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
