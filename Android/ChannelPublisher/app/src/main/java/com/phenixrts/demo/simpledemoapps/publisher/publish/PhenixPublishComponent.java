/*
 * Copyright 2019 Phenix Real Time Solutions, Inc. Confidential and Proprietary. All Rights Reserved.
 *
 * By using this code you agree to the Phenix Terms of Service found online here:
 * http://phenixrts.com/terms-of-service.html
 */

package com.phenixrts.demo.simpledemoapps.publisher.publish;

import static com.phenixrts.system.Utilities.requireNonNull;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.SurfaceView;
import androidx.annotation.Nullable;
import com.phenixrts.express.ChannelExpress;
import com.phenixrts.express.ChannelExpressFactory;
import com.phenixrts.express.ExpressPublisher;
import com.phenixrts.express.PCastExpressFactory;
import com.phenixrts.express.PublishOptions;
import com.phenixrts.express.PublishToChannelOptions;
import com.phenixrts.pcast.Renderer;
import com.phenixrts.pcast.android.AndroidVideoRenderSurface;
import com.phenixrts.room.ChannelOptions;
import com.phenixrts.room.RoomService;
import com.phenixrts.room.RoomServiceFactory;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class PhenixPublishComponent {

  private static final String TAG = PhenixPublishComponent.class.getSimpleName();

  private final ChannelExpress channelExpress;

  public PhenixPublishComponent(ChannelExpress channelExpress) {
    this.channelExpress = requireNonNull(channelExpress);
  }

  /**
   * @param channelName Target channel name for publishing
   * @param cameraConfiguration settings related to audio/video source
   * @param withPreview Container for publish preview.
   * @param callback Callback for publishing events
   * @param capabilities Publish session properties
   * @see ChannelExpress#publishToChannel(PublishToChannelOptions, ChannelExpress.PublishToChannelCallback)
   * Publishing without preview
   * @see <a href="https://phenixrts.com/docs/android/#publish-to-a-channel">Online
   * Documentation</a>
   */
  public void publish(@NotNull String channelName,
      @NotNull CameraConfiguration cameraConfiguration,
      @NotNull SurfaceView withPreview,
      @NotNull OnPublishCallback callback,
      @Nullable PublishCapability... capabilities) {

    // Using ChannelOptions means that the channel may or may not already exist.
    // If the channel ID is known in advance, it is recommended to use `withChannelId` instead
    // of `withChannelOptions` when assembling the `PublishToChannelOptions` below
    final ChannelOptions channelOptions = RoomServiceFactory.createChannelOptionsBuilder()
        .withName(channelName)
        .buildChannelOptions();

    // Capabilities
    PublishOptions publishOptions = PCastExpressFactory.createPublishOptionsBuilder()
        .withCapabilities(extractCapabilities(capabilities))        // optional
        .withMediaConstraints(cameraConfiguration.buildOptions())   // optional
        .withPreviewRenderer(new AndroidVideoRenderSurface(withPreview.getHolder())) // optional
        .buildPublishOptions();

    final PublishToChannelOptions publishToChannelOptions =
        ChannelExpressFactory.createPublishToChannelOptionsBuilder()
            .withChannelOptions(channelOptions) // required
            .withPublishOptions(publishOptions) // required
            .buildPublishToChannelOptions();

    channelExpress.publishToChannel(publishToChannelOptions,
        (requestStatus, roomService, expressPublisher, renderer) -> {
          Handler mainHandler = new Handler(Looper.getMainLooper());
          mainHandler.post(() -> {
            switch (requestStatus) {
              case OK:
                // room service should stored with strong reference, to protect from GC
                callback.onSuccess(roomService, expressPublisher, renderer);
                break;
              default:
                callback.onError(
                    new PublishException(requestStatus));
            }
          });
        });
  }

  public static String getChannelAlias(@NotNull RoomService roomService) {
    return roomService.getObservableActiveRoom().getValue().getObservableAlias().getValue();
  }

  public static void stopPublishing(RoomService roomService, ExpressPublisher publisher,
      Renderer renderer) {
    if (renderer != null) {
      renderer.stop();
    }

    if (publisher != null) {
      publisher.stop();
    }

    if (roomService != null) {
      roomService.leaveRoom((roomService1, requestStatus) -> {
        switch (requestStatus) {
          case OK:
            Log.d(TAG, "Room has been leaved");
            break;
          default:
            Log.w(TAG, "Notification: Error while leaving room");
        }
      });
    }
  }

  private static String[] extractCapabilities(PublishCapability... capabilities) {
    if (capabilities != null) {
      List<String> result = new ArrayList<>();
      for (PublishCapability capability : capabilities) {
        result.add(capability.getPhenixCapability());
      }
      return result.toArray(new String[0]);
    } else {
      return new String[0];
    }
  }
}