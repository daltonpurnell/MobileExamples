/*
 * Copyright 2019 Phenix Real Time Solutions, Inc. Confidential and Proprietary. All Rights Reserved.
 *
 * By using this code you agree to the Phenix Terms of Service found online here:
 * http://phenixrts.com/terms-of-service.html
 */

import PhenixSdk

public struct CameraConfiguration {

    public var camera: PhenixFacingMode?
    public var framerate: Int?
    public var mic: Bool?

    public func buildConfiguration() -> PhenixUserMediaOptions {
        let mediaConstraints = PhenixUserMediaOptions()

        // Camera
        if let camera = camera {
            mediaConstraints.video.enabled = true
            mediaConstraints.video.capabilityConstraints[PhenixDeviceCapability.facingMode.rawValue] = [PhenixDeviceConstraint.initWith(camera)]
            // Framerate
            if let fps = framerate {
                mediaConstraints.video.capabilityConstraints[PhenixDeviceCapability.frameRate.rawValue] = [PhenixDeviceConstraint.initWith(Double(fps))]
            }
        } else {
            // camera is enabled by default
            mediaConstraints.video.enabled = false
        }

        if let mic = self.mic {
            mediaConstraints.audio.enabled = mic
        }

        return mediaConstraints
    }

}
