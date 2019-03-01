/*
 * Copyright 2019 Phenix Real Time Solutions, Inc. Confidential and Proprietary. All Rights Reserved.
 *
 *  By using this code you agree to the Phenix Terms of Service found online here:
 *  http://phenixrts.com/terms-of-service.html
 */
package com.phenixrts.demo.simpledemoapps.subsciber.subscribe;

import static com.phenixrts.demo.simpledemoapps.subsciber.PhenixHelper.createChannelExpress;
import static com.phenixrts.system.Utilities.requireNonNull;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.SurfaceView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.phenixrts.express.ChannelExpress;
import com.phenixrts.express.ChannelExpressFactory;
import com.phenixrts.express.ExpressSubscriber;
import com.phenixrts.express.JoinChannelOptions;
import com.phenixrts.express.JoinChannelOptionsBuilder;
import com.phenixrts.express.JoinRoomOptions;
import com.phenixrts.express.RoomExpressFactory;
import com.phenixrts.pcast.AspectRatioMode;
import com.phenixrts.pcast.Renderer;
import com.phenixrts.pcast.RendererOptions;
import com.phenixrts.pcast.android.AndroidVideoRenderSurface;
import com.phenixrts.room.RoomService;
import java.io.IOException;

public class PhenixSubscribeComponent {

  private static final String TAG = PhenixSubscribeComponent.class.getSimpleName();

  private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

  private final ChannelExpress channelExpress;

  /**
   * PhenixHelper for easy subscribe to channels
   *
   * @param backendUri backend url to be used for creating {@link ChannelExpress}
   * @param pcastUri optional, for custom authentication
   */
  public PhenixSubscribeComponent(@NonNull Context context,
      @NonNull String backendUri,
      @Nullable String pcastUri) {

    Log.d(TAG, String.format("Creating ChannelExpress with backendUri [%s] and pcastUri [%s]",
        backendUri, pcastUri));

    this.channelExpress = createChannelExpress(requireNonNull(context),
        requireNonNull(backendUri), pcastUri);
  }

  /**
   * @param channelExpress to be used for all channel subscribe related operations
   */
  public PhenixSubscribeComponent(ChannelExpress channelExpress) {
    Log.d(TAG, "Reusing existing ChannelExpress");
    this.channelExpress = requireNonNull(channelExpress);
  }

  private JoinChannelOptions buildOptions(String channelAlias, SurfaceView playerView,
      SubscribeCapability... capabilities) {

    final String[] capabilityStrings = SubscribeCapability.convertCapabilities(capabilities);

    final JoinRoomOptions joinRoomOptions = RoomExpressFactory.createJoinRoomOptionsBuilder()
        .withRoomAlias(channelAlias)
        .withCapabilities(capabilityStrings)
        .buildJoinRoomOptions();

    JoinChannelOptionsBuilder joinChannelOptionsBuilder = ChannelExpressFactory
        .createJoinChannelOptionsBuilder()
        .withJoinRoomOptions(joinRoomOptions);

    // preview options
    if (playerView != null) {
      final AndroidVideoRenderSurface renderSurface = new AndroidVideoRenderSurface(
          playerView.getHolder());
      final RendererOptions rendererOptions = new RendererOptions();
      rendererOptions.aspectRatioMode = AspectRatioMode.LETTERBOX;

      joinChannelOptionsBuilder
          .withRenderer(renderSurface)
          .withRendererOptions(rendererOptions);
    }

    return joinChannelOptionsBuilder.buildJoinChannelOptions();
  }

  /**
   * Try to subscribe to channel and play it content, when ready. <br/> PLease provide listener and
   * save to local strong reference to {@link com.phenixrts.room.RoomService} and {@link
   * com.phenixrts.express.ExpressSubscriber} from {@link OnSubscribeCallback#onJoinChannelSuccess(RoomService)},
   * {@link OnSubscribeCallback#onSubscribeSuccess(ExpressSubscriber, Renderer)}. <br/> To
   * unsubscribe, use {@link RoomService#leaveRoom(RoomService.LeaveRoomCallback)}
   *
   * @param channelAlias of channel to be played
   * @param playerView surface to play video content on
   * @param callback listener for events.
   * @param capabilities subscribe configurations. If null - {@link SubscribeCapability#REAL_TIME}
   */
  public void subscribe(String channelAlias, @Nullable SurfaceView playerView,
      OnSubscribeCallback callback, SubscribeCapability... capabilities) {

    if (channelAlias == null || channelAlias.isEmpty() || callback == null) {
      throw new NullPointerException("Please provide configuration and listener");
    }

    channelExpress.joinChannel(buildOptions(channelAlias, playerView, capabilities),
        // join channel callback
        (requestStatus, roomService) ->
            mainThreadHandler.post(() -> {
              switch (requestStatus) {
                case OK:
                  callback.onJoinChannelSuccess(roomService);
                  break;
                default:
                  callback.onError(new SubscribeException.JoinChannelException(requestStatus));
              }
            }),
        // subscribe channel callback
        (requestStatus, expressSubscriber, renderer) ->
            mainThreadHandler.post(() -> {
              switch (requestStatus) {
                case OK:
                  callback.onSubscribeSuccess(expressSubscriber, renderer);
                  break;
                case NO_STREAM_PLAYING:
                  // We are joined channel, but there is no active content.
                  // We will automatically subscribe to channel, when content appear
                  callback.onNoActiveStream();
                  break;
                default:
                  callback.onError(new SubscribeException.SubscribeChannelException(requestStatus));
              }
            }));
  }

  /**
   * Completly stop subscription
   */
  public void stopSubscription(RoomService roomService, ExpressSubscriber expressSubscriber,
      Renderer renderer) {

    if (renderer != null) {
      renderer.stop();
    }

    if (expressSubscriber != null) {
      expressSubscriber.stop();
    }

    if (roomService != null) {
      try {
        roomService.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

}
