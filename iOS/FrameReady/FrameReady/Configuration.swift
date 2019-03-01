/*
 * Copyright 2019 Phenix Real Time Solutions, Inc. Confidential and Proprietary. All Rights Reserved.
 *
 *  By using this code you agree to the Phenix Terms of Service found online here:
 *  http://phenixrts.com/terms-of-service.html
 */
import PhenixSdk

struct Configuration {
  public static let kMainStreamUrl = "https://archive.org/download/ElephantsDream/ed_hd_512kb.mp4"
  public static let kBackendEndpoint = "https://demo.phenixrts.com/demo/"

  public static let kRendererOptions = { () -> PhenixRendererOptions in
    let options = PhenixRendererOptions()
    options.aspectRatioMode = .letterbox
    // Rendering local user media will by default mirror the video, need to explicitly disable:
    options.autoMirroringEnabled = false
    return options
  }()

  private init() {}
}
