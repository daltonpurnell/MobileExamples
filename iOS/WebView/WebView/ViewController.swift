/*
 * Copyright 2019 Phenix Real Time Solutions, Inc. Confidential and Proprietary. All Rights Reserved.
 *
 *  By using this code you agree to the Phenix Terms of Service found online here:
 *  http://phenixrts.com/terms-of-service.html
 */
import UIKit
import WebKit

/**
 * WebView based channel viewer.<br/>
 * To publish a video to this channel, open the following link in your web browser:
 * In your web browser, open "https://phenixrts.com/channel/publish/#channelAlias"
 */
class ViewController: UIViewController, WKUIDelegate {

  /**
   * The channel alias is derived from the channel name.
   * For example, a channel with name "My Channel Name!" will have the alias "myChannelName".
   */
  private static let channelAlias = "webViewDemo"

  /**
   * Subscribe link to channel with alias `channelAlias`
   */
  private static let subscribeUrl = "https://phenixrts.com/channel/?mss=mr#" + channelAlias

  var webView: WKWebView!

  override func loadView() {
    let webConfiguration = WKWebViewConfiguration()
    webConfiguration.allowsInlineMediaPlayback = true
    webConfiguration.mediaTypesRequiringUserActionForPlayback = []

    self.webView = WKWebView(frame: .zero, configuration: webConfiguration)
    self.webView.uiDelegate = self

    self.view = self.webView
  }

  override func viewDidLoad() {
    super.viewDidLoad()

    let myURL = URL(string: ViewController.subscribeUrl)
    let myRequest = URLRequest(url: myURL!)

    self.webView.load(myRequest)
  }
}

