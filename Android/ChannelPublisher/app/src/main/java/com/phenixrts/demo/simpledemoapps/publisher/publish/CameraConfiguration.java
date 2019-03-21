/*
 * Copyright 2019 Phenix Real Time Solutions, Inc. Confidential and Proprietary. All Rights Reserved.
 *
 * By using this code you agree to the Phenix Terms of Service found online here:
 * http://phenixrts.com/terms-of-service.html
 */

package com.phenixrts.demo.simpledemoapps.publisher.publish;

import static java.util.Collections.singletonList;

import androidx.annotation.IntRange;
import com.phenixrts.pcast.DeviceCapability;
import com.phenixrts.pcast.DeviceConstraint;
import com.phenixrts.pcast.FacingMode;
import com.phenixrts.pcast.UserMediaOptions;

/**
 * Example of configuration media source
 */
public class CameraConfiguration {

  public FacingMode cameraLocation;
  public boolean isMicrophoneEnabled;
  public int fps;

  public CameraConfiguration(FacingMode cameraLocation,
      boolean isMicrophoneEnabled,
      @IntRange(from = 15, to = 30) int fps) {
    this.cameraLocation = cameraLocation;
    this.isMicrophoneEnabled = isMicrophoneEnabled;
    this.fps = fps;
  }

    /**
     * Build Phenix specific device settings
     */
  public UserMediaOptions buildOptions() {
    // Example constraints. Audio and video are enabled by default
    final UserMediaOptions mediaConstraints = new UserMediaOptions();

    // Video
    if (cameraLocation != null) {
      mediaConstraints.getVideoOptions().capabilityConstraints.put(
          DeviceCapability.FACING_MODE,
          singletonList(new DeviceConstraint(cameraLocation)));
      mediaConstraints.getVideoOptions().capabilityConstraints.put(
          DeviceCapability.FRAME_RATE,
          singletonList(new DeviceConstraint(fps)));
    } else {
      // Video is enabled by default
      mediaConstraints.getVideoOptions().enabled = false;
    }

    // Audio is enabled by default
    mediaConstraints.getAudioOptions().enabled = isMicrophoneEnabled;

    return mediaConstraints;
  }
}
