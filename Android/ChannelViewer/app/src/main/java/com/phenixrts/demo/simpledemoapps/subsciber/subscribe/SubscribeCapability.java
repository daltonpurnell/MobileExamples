/*
 * Copyright 2019 Phenix Real Time Solutions, Inc. Confidential and Proprietary. All Rights Reserved.
 *
 *  By using this code you agree to the Phenix Terms of Service found online here:
 *  http://phenixrts.com/terms-of-service.html
 */

package com.phenixrts.demo.simpledemoapps.subsciber.subscribe;

import androidx.annotation.NonNull;

/**
 * Example of capabilities, which could be used for subscribing to channel.
 *
 * @see <a href="https://phenixrts.com/docs/api/#supported-stream-capabilities">Phenix API online documentation</a>
 */
public enum SubscribeCapability {

    /**
     * Use real-time streaming with typically less than 300 milliseconds delivery latency. This is the
     * default.
     */
    REAL_TIME("real-time"),
    /**
     * Use adaptive live streaming using HLS or MPEG+DASH with about 8-12 seconds delivery latency.
     * This can be used for broadcasts to clients with network connections with limited bandwidth or
     * highly fluctuating bandwidth. It requires the publisher stream to have the “streaming”
     * capability.
     */
    LIVE_STREAMING("streaming"),
    /**
     * Use broadcast real-time streaming with about 1 second delivery latency. This can be used for
     * users with bad WiFi networks with high jitter and/or interference.
     */
    BROADCAST("broadcasting"),
    /**
     * Use adaptive on-demand streaming using HLS or MPEG+DASH. This can be used to access the stream
     * after it was recorded. It requires the publisher stream to have the “on-demand” capability.
     */
    ON_DEMAND("on-demand"),
    /**
     * Use RTMP for playback with about 1-2 seconds delivery latency. It requires the publisher stream
     * to have the “rtmp” capability.
     */
    RTMP_PUSH("rtmp");

    final String internalFormat;

    SubscribeCapability(String s) {
        internalFormat = s;
    }

    @NonNull
    public static String[] convertCapabilities(SubscribeCapability... capabilities) {
        if (capabilities == null) {
            return new String[0];
        }

        String[] result = new String[capabilities.length];
        for (int i = 0; i < capabilities.length; i++) {
            result[i] = capabilities[i].internalFormat;
        }
        return result;
    }

}
