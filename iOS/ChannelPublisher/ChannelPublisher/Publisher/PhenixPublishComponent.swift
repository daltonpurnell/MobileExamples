/*
 * Copyright 2019 Phenix Real Time Solutions, Inc. Confidential and Proprietary. All Rights Reserved.
 *
 * By using this code you agree to the Phenix Terms of Service found online here:
 * http://phenixrts.com/terms-of-service.html
 */

import PhenixSdk
import UIKit

/**
 * Prerequisites for publishing
 */
public struct PublishConfiguration {
    var channelName: String
    var capabilities: [PublishCapability]
    var camera: CameraConfiguration
}

/**
 * Will be called in main thread
 */
public protocol OnPublishCallback {
    func onSuccess(roomService: PhenixRoomService?, publisher: PhenixExpressPublisher, renderer: PhenixRenderer?)
    func onError(status: PhenixRequestStatus)
}

/**
 * do not forget to add permissions to info.plist:
 * "Privacy - Camera Usage Description"
 * "Privacy - Microphone Usage Description"
 */
public class PhenixPublishComponent {
    private let channelExpress: PhenixChannelExpress

    init(channelExpress: PhenixChannelExpress) {
        self.channelExpress = channelExpress
    }

    public func publishToChannel(configuration: PublishConfiguration, previewView: UIView?, callback: OnPublishCallback) {
        print("INFO", "Publishing \(configuration)")

        /*
         Using ChannelOptions means that the channel may or may not already exist.
         If the channel ID is known in advance, it is recommended to use `withChannelId` instead
         of `withChannelOptions` when assembling the `PhenixPublishToChannelOptions` below
         */
        let channelOptions = PhenixRoomServiceFactory.createChannelOptionsBuilder()
            .withName(configuration.channelName)
            .buildChannelOptions()

        let publishToChannelOptions = PhenixChannelExpressFactory.createPublishToChannelOptionsBuilder()
            .withChannelOptions(channelOptions)!

        // Capabilities
        let allCapabilities = configuration.capabilities.map { (capability) -> String in
            return capability.getCapability()
        }

        let publishOptionsBuilder = PhenixPCastExpressFactory.createPublishOptionsBuilder()!
            .withCapabilities(allCapabilities)!

        // configure publish preview
        if let previewView = previewView {
            let previewOptions = PhenixRendererOptions()
            previewOptions.aspectRatioMode = .letterbox

            publishOptionsBuilder.withPreviewRendererOptions(previewOptions)
            publishOptionsBuilder.withPreviewRenderer(previewView.layer)
        }

        // set videosource configuration
        publishOptionsBuilder.withMediaConstraints(configuration.camera.buildConfiguration())

        publishToChannelOptions.withPublishOptions(publishOptionsBuilder.buildPublishOptions())

        let options = publishToChannelOptions.buildPublishToChannelOptions()

        channelExpress.publish(toChannel: options) { (status, roomService, publisher, renderer) in
            DispatchQueue.main.async {
                switch(status) {
                case .ok: callback.onSuccess(roomService: roomService!, publisher: publisher!, renderer: renderer)
                default: callback.onError(status: status)
                }
            }
        }
    }

    /**
     * Completly stop publishing
     */
    public static func stopPublishing(roomService: PhenixRoomService?, publisher: PhenixExpressPublisher?, renderer: PhenixRenderer?) {
        renderer?.stop()
        publisher?.stop()
        roomService?.leaveRoom({ (_, _) in
            print("INFO", "Publishing finished")
        })
    }
}
