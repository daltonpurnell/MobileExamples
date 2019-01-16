/**
 * Copyright 2018 PhenixP2P Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

