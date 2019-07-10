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

    private let publisher = PhenixPublishComponent(channelExpress: AppDelegate.channelExpress)

    // Keep strong reference to room service to ensure we remain joined
    private var currentRoomService: PhenixRoomService?
    private var currentPublisher: PhenixExpressPublisher?
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

    @IBAction func publish(_ sender: Any) {
        // stop subscription, while we have access to current service, subscriber and renderer
        finishPublishing(sender)
        publisher.publishToChannel(configuration: PublishConfiguration(channelName: AppDelegate.channelName,
                                                                       capabilities: [PublishMode.realTime],
                                                                       camera: CameraConfiguration(camera: .user, framerate: 15, mic: true)),
                                   previewView: playerView, callback: self)
    }

    @IBAction func finishPublishing(_ sender: Any) {
        print("INFO", "unsubscribing...")
        PhenixPublishComponent.stopPublishing(roomService: currentRoomService, publisher: currentPublisher, renderer: currentRenderer)
        currentRoomService = nil
        currentPublisher = nil
        currentRenderer = nil
    }
}

extension ViewController: OnPublishCallback {
    func onSuccess(roomService: PhenixRoomService?, publisher: PhenixExpressPublisher, renderer: PhenixRenderer?) {
        currentRoomService = roomService
        currentPublisher = publisher
        currentRenderer = renderer
        print("INFO", TAG, "Publishing to channel")
    }

    func onError(status: PhenixRequestStatus) {
        print("ERROR", TAG, "Cannot publish to channel: \(status.getLocalisedStatusMessage()). Error #\(status.rawValue)")
    }
}

private extension PhenixRequestStatus {
    func getLocalisedStatusMessage() -> String {
        switch self {
        case .ok: return NSLocalizedString("PhenixRequestStatusOk", comment: "")
        case .noStreamPlaying: return NSLocalizedString("PhenixRequestStatusNoStreamPlaying", comment: "")
        case .badRequest: return NSLocalizedString("PhenixRequestStatusBadRequest", comment: "")
        case .unauthorized: return NSLocalizedString("PhenixRequestStatusUnauthorized", comment: "")
        case .conflict: return NSLocalizedString("PhenixRequestStatusConflict", comment: "")
        case .gone: return NSLocalizedString("PhenixRequestStatusGone", comment: "")
        case .notInitialized: return NSLocalizedString("PhenixRequestStatusNotInitialized", comment: "")
        case .notStarted: return NSLocalizedString("PhenixRequestStatusNotStarted", comment: "")
        case .upgradeRequired: return NSLocalizedString("PhenixRequestStatusUpgradeRequired", comment: "")
        case .failed: return NSLocalizedString("PhenixRequestStatusFailed", comment: "")
        case .capacity: return NSLocalizedString("PhenixRequestStatusCapacity", comment: "")
        case .timeout: return NSLocalizedString("PhenixRequestStatusTimeout", comment: "")
        case .notReady: return NSLocalizedString("PhenixRequestStatusNotReady", comment: "")
        case .rateLimited: return NSLocalizedString("PhenixRequestStatusRateLimited", comment: "")
        }
    }
}
