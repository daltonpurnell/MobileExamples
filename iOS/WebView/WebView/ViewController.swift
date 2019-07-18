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

    @IBOutlet weak var webView: WKWebView!
    @IBOutlet weak var editedUrl: UITextField!

    /**
     * The channel alias is derived from the channel name.
     * For example, a channel with name "My Channel Name!" will have the alias "myChannelName".
     */
    private let channelAlias = "webViewDemo"

    /**
     * Subscribe link to channel with alias `channelAlias`
     */
    private lazy var subscribeUrl = "https://phenixrts.com/channel/#" + channelAlias

    override func viewDidLoad() {
        super.viewDidLoad()

        editedUrl.text = subscribeUrl

        webView.configuration.allowsInlineMediaPlayback = true
        webView.configuration.mediaTypesRequiringUserActionForPlayback = []
        webView.uiDelegate = self
        loadUrl(self)
    }

    @IBAction func loadUrl(_ sender: Any) {
        if let urlText = editedUrl?.text, let url = URL(string: urlText) {
            print("Info", "Loading url \(url)")
            webView.load(URLRequest(url: url))
        }
    }
}

