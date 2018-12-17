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

class ViewController: UIViewController, WKUIDelegate {

  public static let kUrl = "https://phenixrts.com/channel/?mss=mr#webViewDemo"

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

    let myURL = URL(string: ViewController.kUrl)
    let myRequest = URLRequest(url: myURL!)

    self.webView.load(myRequest)
  }
}

