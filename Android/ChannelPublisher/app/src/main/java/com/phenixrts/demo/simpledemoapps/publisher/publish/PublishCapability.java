/*
 * Copyright 2019 Phenix Real Time Solutions, Inc. Confidential and Proprietary. All Rights Reserved.
 *
 * By using this code you agree to the Phenix Terms of Service found online here:
 * http://phenixrts.com/terms-of-service.html
 */

package com.phenixrts.demo.simpledemoapps.publisher.publish;

public interface PublishCapability {

  /**
   * @return phenix specific capability string, that could be used for publishing
   * @see com.phenixrts.express.PublishOptionsBuilder#withCapabilities(String[])
   */
  String getPhenixCapability();

  /**
   * Capabilities which could be used for subscribing to channels.
   *
   * @see <a href="https://phenixp2p.com/docs/api/#request5">Phenix API online documentation</a>
   */
  enum PublishMode implements PublishCapability {

    /**
     * Use real-time streaming with typically less than 300 milliseconds delivery latency. This is
     * the default.
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
    BROADCAST("broadcast"),
    /**
     * Use adaptive on-demand streaming using HLS or MPEG+DASH. This can be used to access the
     * stream after it was recorded. It requires the publisher stream to have the “on-demand”
     * capability.
     */
    ON_DEMAND("on-demand"),

    /**
     * This is a flag enables the "multi-bitrate" feature, which transcodes into several quality
     * levels at the backend and lets the subscriber pick the best one
     */
    MULTI_BITRATE("multi-bitrate"),
    /**
     * Use RTMP for playback with about 1-2 seconds delivery latency. It requires the publisher
     * stream to have the “rtmp” capability.
     */
    RTMP_PUSH("rtmp");

    private final String internalFormat;

    PublishMode(String s) {
      internalFormat = s;
    }

    @Override
    public String getPhenixCapability() {
      return internalFormat;
    }
  }

  /**
   * If MBR is enabled, either of these, or both, can be specified to control which codec (VP8/H264)
   * is used for the MBR transcode output.
   *
   * Option for {@link PublishMode#MULTI_BITRATE}
   */
  enum MultiBitrateCodec implements PublishCapability {
    VP8("multi-bitrate-codec=vp8"),
    H264("multi-bitrate-codec=h264");

    private final String internalFormat;

    MultiBitrateCodec(String s) {
      internalFormat = s;
    }

    @Override
    public String getPhenixCapability() {
      return internalFormat;
    }
  }

  enum Quality implements PublishCapability {
    /**
     * 144p
     */
    VVLD("vvld"),
    /**
     * 240p
     */
    VLD("vld"),
    /**
     * 360p
     */
    LD("ld"),
    /**
     * 480p
     */
    SD("sd"),
    /**
     * 720p
     */
    HD("hd"),
    /**
     * 1080p
     */
    FHD("fhd");

    private final String internalFormat;

    Quality(String internal) {
      internalFormat = internal;
    }

    @Override
    public String getPhenixCapability() {
      return internalFormat;
    }
  }

}