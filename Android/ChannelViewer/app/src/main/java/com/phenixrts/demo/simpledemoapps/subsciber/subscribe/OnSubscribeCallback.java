/*
 * Copyright 2019 Phenix Real Time Solutions, Inc. Confidential and Proprietary. All Rights Reserved.
 *
 *  By using this code you agree to the Phenix Terms of Service found online here:
 *  http://phenixrts.com/terms-of-service.html
 */

package com.phenixrts.demo.simpledemoapps.subsciber.subscribe;

import com.phenixrts.express.ExpressSubscriber;
import com.phenixrts.pcast.Renderer;
import com.phenixrts.room.RoomService;

/**
 * Interface to handle subscribe events
 */
public interface OnSubscribeCallback {

  /**
   * Triggers when you joined channel. Please save roomService in your local link.
   *
   * @param roomService to control current room session
   */
  void onJoinChannelSuccess(RoomService roomService);

  /**
   * Triggers when channel was subscribed and playback will be started in a moment
   *
   * @param subscriber subscriber to channel, help to control connection to active stream
   * @param renderer player for subscribed stream. Operate with audio video content
   */
  void onSubscribeSuccess(ExpressSubscriber subscriber, Renderer renderer);

  /**
   * Room is joined, but there is no active stream.
   * Will auto subscribe, when stream become available
   */
  void onNoActiveStream();

  /**
   * Triggers when some error occurred during joining or subscribing channel.
   *
   * @param error related error
   * @see SubscribeException.SubscribeChannelException
   * @see SubscribeException.JoinChannelException
   */
  void onError(SubscribeException error);
}
