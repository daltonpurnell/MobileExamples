/*
 * Copyright 2019 Phenix Real Time Solutions, Inc. Confidential and Proprietary. All Rights Reserved.
 *
 *  By using this code you agree to the Phenix Terms of Service found online here:
 *  http://phenixrts.com/terms-of-service.html
 */

package com.phenixrts.demo.simpledemoapps.subsciber;

import static com.phenixrts.demo.simpledemoapps.subsciber.PhenixHelper.BACKEND_URI;
import static com.phenixrts.demo.simpledemoapps.subsciber.PhenixHelper.CHANNEL_ALIAS;
import static com.phenixrts.demo.simpledemoapps.subsciber.PhenixHelper.PCAST_URI;

import android.app.Activity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.widget.Toast;
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

    phenixSubscribeComponent = new PhenixSubscribeComponent(this, BACKEND_URI, PCAST_URI);
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
        PhenixHelper.getHumanFriendlyPhenixStatusDescription(error.status),
        Toast.LENGTH_LONG).show();
  }
}
