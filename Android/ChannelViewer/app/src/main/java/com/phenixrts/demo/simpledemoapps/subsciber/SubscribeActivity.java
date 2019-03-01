/*
 * Copyright 2019 Phenix Real Time Solutions, Inc. Confidential and Proprietary. All Rights Reserved.
 *
 * By using this code you agree to the Phenix Terms of Service found online here:
 * http://phenixrts.com/terms-of-service.html
 */

package com.phenixrts.demo.simpledemoapps.subsciber;

import static com.phenixrts.demo.simpledemoapps.subsciber.MyApplication.CHANNEL_ALIAS;

import android.app.Activity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.widget.Toast;
import androidx.annotation.StringRes;
import com.phenixrts.common.RequestStatus;
import com.phenixrts.demo.simpledemoapps.subsciber.subscribe.OnSubscribeCallback;
import com.phenixrts.demo.simpledemoapps.subsciber.subscribe.PhenixSubscribeComponent;
import com.phenixrts.demo.simpledemoapps.subsciber.subscribe.SubscribeException;
import com.phenixrts.express.ExpressSubscriber;
import com.phenixrts.pcast.Renderer;
import com.phenixrts.room.RoomService;

public class SubscribeActivity extends Activity implements OnSubscribeCallback {

  private PhenixSubscribeComponent phenixSubscribeComponent;
  private SurfaceView playerView;

  private RoomService currentRoomService;
  private ExpressSubscriber currentSubscriber;
  private Renderer currentRenderer;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    findViewById(R.id.subscribeButton).setOnClickListener(v -> subscribe());
    findViewById(R.id.unsubscibe_button).setOnClickListener(v -> unsubscribe());
    playerView = findViewById(R.id.playerView);

    phenixSubscribeComponent = new PhenixSubscribeComponent(MyApplication.channelExpress);
  }

  @Override
  protected void onStop() {
    unsubscribe();
    super.onStop();
  }

  private void subscribe() {
    // stop subscription, while we have access to current service, subscriber and renderer
    unsubscribe();

    phenixSubscribeComponent.subscribe(CHANNEL_ALIAS, playerView, this);
    Toast.makeText(this, "Subscribed", Toast.LENGTH_SHORT).show();
  }

  private void unsubscribe() {
    phenixSubscribeComponent
        .stopSubscription(currentRoomService, currentSubscriber, currentRenderer);
    Toast.makeText(this, "Unsubscribed", Toast.LENGTH_SHORT).show();
  }


  @Override
  public void onJoinChannelSuccess(RoomService roomService) {
    currentRoomService = roomService;
  }

  @Override
  public void onSubscribeSuccess(ExpressSubscriber subscriber, Renderer renderer) {
    currentSubscriber = subscriber;
    currentRenderer = renderer;
  }

  @Override
  public void onNoActiveStream() {
    Toast.makeText(SubscribeActivity.this, "Waiting for stream...", Toast.LENGTH_LONG)
        .show();
  }

  @Override
  public void onError(SubscribeException error) {
    Toast.makeText(SubscribeActivity.this,
        getHumanFriendlyPhenixStatusDescription(error.status),
        Toast.LENGTH_LONG).show();
  }

  @StringRes
  public static int getHumanFriendlyPhenixStatusDescription(RequestStatus status) {

    switch (status) {
      case OK:
        return R.string.phenix_request_status_ok;
      case NO_STREAM_PLAYING:
        return R.string.phenix_request_status_noStreamPlaying;
      case BAD_REQUEST:
        return R.string.phenix_request_status_badRequest;
      case UNAUTHORIZED:
        return R.string.phenix_request_status_unauthorized;
      case CONFLICT:
        return R.string.phenix_request_status_conflict;
      case GONE:
        return R.string.phenix_request_status_gone;
      case NOT_INITIALIZED:
        return R.string.phenix_request_status_notInitialized;
      case NOT_STARTED:
        return R.string.phenix_request_status_notStarted;
      case UPGRADE_REQUIRED:
        return R.string.phenix_request_status_upgradeRequired;
      case FAILED:
        return R.string.phenix_request_status_failed;
      case CAPACITY:
        return R.string.phenix_request_status_capacity;
      case TIMEOUT:
        return R.string.phenix_request_status_timeout;
      case NOT_READY:
        return R.string.phenix_request_status_notReady;
    }

    return 0;
  }
}
