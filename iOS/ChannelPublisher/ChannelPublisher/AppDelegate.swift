/*
 * Copyright 2019 Phenix Real Time Solutions, Inc. Confidential and Proprietary. All Rights Reserved.
 *
 * By using this code you agree to the Phenix Terms of Service found online here:
 * http://phenixrts.com/terms-of-service.html
 */

import UIKit
import PhenixSdk

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    public static let channelExpress: PhenixChannelExpress = { createChannelExpress() }()

    /**
     * You can subscribe to the channel in a web browser with the link:
     * https://phenixrts.com/channel/#mobileSimpleChannel
     */
    public static let channelName = "mobileSimpleChannel"
    private static let backendEndpoint = "https://demo-integration.phenixrts.com/pcast"

    var window: UIWindow?

    private static func createChannelExpress() -> PhenixChannelExpress {
        let pcastExpressOptions = PhenixPCastExpressFactory.createPCastExpressOptionsBuilder()
            .withBackendUri(backendEndpoint)
            .buildPCastExpressOptions()

        let roomExpressOptions = PhenixRoomExpressFactory.createRoomExpressOptionsBuilder()
            .withPCastExpressOptions(pcastExpressOptions)
            .buildRoomExpressOptions()

        let channelExpressOptions = PhenixChannelExpressFactory.createChannelExpressOptionsBuilder()
            .withRoomExpressOptions(roomExpressOptions)
            .buildChannelExpressOptions()

        return PhenixChannelExpressFactory.createChannelExpress(channelExpressOptions)
    }
}

