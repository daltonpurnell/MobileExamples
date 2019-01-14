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
