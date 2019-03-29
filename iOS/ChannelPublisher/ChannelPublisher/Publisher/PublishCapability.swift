/*
 * Copyright 2019 Phenix Real Time Solutions, Inc. Confidential and Proprietary. All Rights Reserved.
 *
 * By using this code you agree to the Phenix Terms of Service found online here:
 * http://phenixrts.com/terms-of-service.html
 */


public protocol PublishCapability {
    func getCapability() -> String
}


/**
 * Capabilities which could be used for subscribing to channels.
 *
 * @see <a href="https://phenixp2p.com/docs/api/#request5">Phenix API online documentation</a>
 */
enum PublishMode: String, PublishCapability {

    public func getCapability() -> String {
        return rawValue
    }


    /**
     * Use real-time streaming with typically less than 300 milliseconds delivery latency. This is
     * the default.
     */
    case realTime = "real-time"
    /**
     * Use adaptive live streaming using HLS or MPEG+DASH with about 8-12 seconds delivery latency.
     * This can be used for broadcasts to clients with network connections with limited bandwidth or
     * highly fluctuating bandwidth. It requires the publisher stream to have the “streaming”
     * capability.
     */
    case live = "streaming"
    /**
     * Use broadcast real-time streaming with about 1 second delivery latency. This can be used for
     * users with bad WiFi networks with high jitter and/or interference.
     */
    case broadcast = "broadcast"
    /**
     * Use adaptive on-demand streaming using HLS or MPEG+DASH. This can be used to access the
     * stream after it was recorded. It requires the publisher stream to have the “on-demand”
     * capability.
     */
    case onDemand = "on-demand"

    /**
     * This is a flag enables the "multi-bitrate" feature, which transcodes into several quality
     * levels at the backend and lets the subscriber pick the best one
     */
    case multiBitrate = "multi-bitrate"
    /**
     * Use RTMP for playback with about 1-2 seconds delivery latency. It requires the publisher
     * stream to have the “rtmp” capability.
     */
    case rtmpPush = "rtmp"

}


/**
 * If MBR is enabled, either of these, or both, can be specified to control which codec (VP8/H264)
 * is used for the MBR transcode output.
 *
 * Option for {@link PublishMode#MULTI_BITRATE}
 */
enum MultiBitrateCodec: String, PublishCapability {
    public func getCapability() -> String {
        return rawValue
    }

    case vp8 = "multi-bitrate-codec=vp8"
    case h264 = "multi-bitrate-codec=h264"
}


enum Quality: String, PublishCapability {
    public func getCapability() -> String {
        return rawValue
    }

    /**
     * 144p
     */
    case vvld = "vvld"
    /**
     * 240p
     */
    case vld = "vld"
    /**
     * 360p
     */
    case ld = "ld"
    /**
     * 480p
     */
    case sd = "sd"
    /**
     * 720p
     */
    case hd = "hd"
    /**
     * 1080p
     */
    case fhd = "fhd"
}
