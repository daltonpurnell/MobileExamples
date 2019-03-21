/*
 * Copyright 2019 Phenix Real Time Solutions, Inc. Confidential and Proprietary. All Rights Reserved.
 *
 * By using this code you agree to the Phenix Terms of Service found online here:
 * http://phenixrts.com/terms-of-service.html
 */

package com.phenixrts.demo.simpledemoapps.publisher.publish;

import com.phenixrts.express.ExpressPublisher;
import com.phenixrts.pcast.Renderer;
import com.phenixrts.room.RoomService;

public interface OnPublishCallback {

  void onSuccess(RoomService roomService, ExpressPublisher expressPublisher, Renderer renderer);

  void onError(PublishException e);

}
