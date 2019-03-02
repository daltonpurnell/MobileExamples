/*
 * Copyright 2019 Phenix Real Time Solutions, Inc. Confidential and Proprietary. All Rights Reserved.
 *
 * By using this code you agree to the Phenix Terms of Service found online here:
 * http://phenixrts.com/terms-of-service.html
 */

import UIKit
import PhenixSdk

class ViewController: UIViewController {

    private let TAG = String(describing: ViewController.self)

    @IBOutlet weak var playerView: UIView!

    private lazy var phenixSubscribeComponent = {
        PhenixSubscribeComponent(channelExpress: AppDelegate.channelExpress)
    }()

    // Keep strong reference to room service to ensure we remain joined
    private var currentRoomService: PhenixRoomService?
    private var currentSubscriber: PhenixExpressSubscriber?
    private var currentRenderer: PhenixRenderer?


    override func viewDidLoad() {
        super.viewDidLoad()
        // keep screen on
        UIApplication.shared.isIdleTimerDisabled = true
    }

    override func viewDidDisappear(_ animated: Bool) {
        UIApplication.shared.isIdleTimerDisabled = false
        super.viewDidDisappear(animated)
    }

    @IBAction func subscribe(_ sender: Any) {
        // stop subscription, while we have access to current service, subscriber and renderer
        unsubscribe(sender)

        phenixSubscribeComponent.subscribe(configuration: PhenixSubscribeComponent.Configuration(channelAlias: AppDelegate.channelAlias, capability: nil),
                                           playerView: playerView, subscribeCallback: self)
    }

    @IBAction func unsubscribe(_ sender: Any) {
        print("INFO", "unsubscribing...")
        currentRenderer?.stop()
        currentSubscriber?.stop()
        currentRoomService = nil
    }
}


extension ViewController: OnSubscribeCallback {

    func onJoinChannelSuccess(roomService: PhenixRoomService) {
        print("INFO", "Channel joined")
        self.currentRoomService = roomService
    }

    func onSubscribeSuccess(subscriber: PhenixExpressSubscriber, renderer: PhenixRenderer) {
        self.currentSubscriber = subscriber
        self.currentRenderer = renderer

        print("INFO", "Channel subscribed")
    }

    func onNoActiveStream() {
        print("INFO", "No stream playing")
    }

    func onError(error: SubscribeError) {
        switch (error) {
        case .joinChannel(let status):
            print("WARNING", TAG, "Join channel error: \(status.getLocalisedStatusMessage()). Error #\(status.rawValue)")
        case .subscribeChannel(let status):
            print("WARNING", TAG, "Cannot subscribe, with message \(status.getLocalisedStatusMessage()) (Error #\(status.rawValue))")
        }
    }

}
