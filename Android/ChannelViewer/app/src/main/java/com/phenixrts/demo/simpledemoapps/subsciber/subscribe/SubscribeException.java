/*
 * Copyright 2019 Phenix Real Time Solutions, Inc. Confidential and Proprietary. All Rights Reserved.
 *
 *  By using this code you agree to the Phenix Terms of Service found online here:
 *  http://phenixrts.com/terms-of-service.html
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
