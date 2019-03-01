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
