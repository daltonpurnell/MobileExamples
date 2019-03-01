/*
 * Copyright 2019 Phenix Real Time Solutions, Inc. Confidential and Proprietary. All Rights Reserved.
 *
 *  By using this code you agree to the Phenix Terms of Service found online here:
 *  http://phenixrts.com/terms-of-service.html
 */
import PhenixSdk
import UIKit

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

  var window: UIWindow?

  func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
    // Don't let the phone sleep while this app is running:
    UIApplication.shared.isIdleTimerDisabled = true
    self.setupAndStartPhenix()
    return true
  }

  func applicationWillTerminate(_ application: UIApplication) {
    self.teardownPhenix()
  }

  private var channelExpress: PhenixChannelExpress?

  private func setupAndStartPhenix() {
    let pcastExpressOptions = PhenixPCastExpressFactory.createPCastExpressOptionsBuilder()
        .withBackendUri(Configuration.kBackendEndpoint)
        .buildPCastExpressOptions()

    let roomExpressOptions = PhenixRoomExpressFactory.createRoomExpressOptionsBuilder()
        .withPCastExpressOptions(pcastExpressOptions)
        .buildRoomExpressOptions()

    let channelExpressOptions = PhenixChannelExpressFactory.createChannelExpressOptionsBuilder()
        .withRoomExpressOptions(roomExpressOptions)
        .buildChannelExpressOptions()

    self.channelExpress = PhenixChannelExpressFactory.createChannelExpress(channelExpressOptions)

    // Inject channel express:
    if let firstViewController = window?.rootViewController as? ViewController {
      firstViewController.channelExpress = self.channelExpress
    }
  }

  private func teardownPhenix() {
    self.channelExpress = nil;
  }
}

