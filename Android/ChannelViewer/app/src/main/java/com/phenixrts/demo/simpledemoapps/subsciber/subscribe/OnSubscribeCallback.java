/*
 * Copyright 2019 PhenixP2P Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
