/*
 * Copyright 2019 Phenix Real Time Solutions, Inc. Confidential and Proprietary. All Rights Reserved.
 *
 * By using this code you agree to the Phenix Terms of Service found online here:
 * http://phenixrts.com/terms-of-service.html
 */

package com.phenixrts.demo.simpledemoapps.publisher.publish;

public class PublishConfiguration {

  public final String channelName;
  public final CameraConfiguration camera;
  public final PublishCapability[] capabilities;

  public PublishConfiguration(String channelName,
      CameraConfiguration camera,
      PublishCapability[] capabilities) {
    this.channelName = channelName;
    this.camera = camera;
    this.capabilities = capabilities;
  }


  String[] getCapabilities() {
    if (capabilities == null) {
      return null;
    }

    String[] result = new String[capabilities.length];
    for (int i = 0; i < capabilities.length; i++) {
      result[i] = capabilities[i].getPhenixCapability();
    }
    return result;
  }
}