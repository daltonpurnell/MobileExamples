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

import com.phenixrts.common.RequestStatus;
import org.jetbrains.annotations.NotNull;

/**
 * General exception of channel subscription.
 *
 * @see JoinChannelException
 * @see SubscribeException
 */
public abstract class SubscribeException extends Exception {

  public final RequestStatus status;

  SubscribeException(RequestStatus status) {
    this.status = status;
  }

  @Override
  @NotNull
  public String toString() {
    return getClass().getSimpleName() + "{" +
        "status=" + status +
        '}';
  }

  /**
   * Join channel failed
   */
  public static class JoinChannelException extends SubscribeException {

    JoinChannelException(RequestStatus status) {
      super(status);
    }
  }

  /**
   * You joined channel but cannot subscribe to it
   */
  public static class SubscribeChannelException extends SubscribeException {

    SubscribeChannelException(RequestStatus status) {
      super(status);
    }
  }
}
