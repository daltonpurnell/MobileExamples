/*
 * Copyright 2019 Phenix Real Time Solutions, Inc. Confidential and Proprietary. All Rights Reserved.
 *
 * By using this code you agree to the Phenix Terms of Service found online here:
 * http://phenixrts.com/terms-of-service.html
 */

package com.phenixrts.demo.simpledemoapps.publisher;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;
import static com.phenixrts.demo.simpledemoapps.publisher.MyApplication.CHANNEL_NAME;

import android.Manifest.permission;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.phenixrts.demo.simpledemoapps.publisher.publish.CameraConfiguration;
import com.phenixrts.demo.simpledemoapps.publisher.publish.OnPublishCallback;
import com.phenixrts.demo.simpledemoapps.publisher.publish.PhenixPublishComponent;
import com.phenixrts.demo.simpledemoapps.publisher.publish.PublishCapability.PublishMode;
import com.phenixrts.demo.simpledemoapps.publisher.publish.PublishCapability.Quality;
import com.phenixrts.demo.simpledemoapps.publisher.publish.PublishException;
import com.phenixrts.express.ExpressPublisher;
import com.phenixrts.pcast.FacingMode;
import com.phenixrts.pcast.Renderer;
import com.phenixrts.room.RoomService;

public class PublishActivity extends Activity implements OnPublishCallback {

  private static final String TAG = PublishActivity.class.getSimpleName();

  private PhenixPublishComponent phenixPublishComponent;
  private SurfaceView previewView;
  private TextView subscribeLinkLabel;

  private RoomService currentRoomService;
  private ExpressPublisher currentPublisher;
  private Renderer currentRenderer;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    findViewById(R.id.publishButton).setOnClickListener(v -> publishCameraSource());
    findViewById(R.id.stopPublishingButton).setOnClickListener(v -> stopPublishing());
    subscribeLinkLabel = findViewById(R.id.subscribeLinkLabel);

    previewView = findViewById(R.id.previewView);

    phenixPublishComponent = new PhenixPublishComponent(MyApplication.channelExpress);

    // Check permissions
    if (ContextCompat.checkSelfPermission(this, permission.CAMERA)
        != PackageManager.PERMISSION_GRANTED
        || ContextCompat.checkSelfPermission(this, permission.RECORD_AUDIO)
        != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this,
          new String[]{permission.CAMERA, permission.RECORD_AUDIO}, 123);
    }
  }

  @Override
  protected void onStop() {
    stopPublishing();
    super.onStop();
  }

  private void publishCameraSource() {
    // stop subscription, while we have access to current service, subscriber and renderer
    stopPublishing();

    phenixPublishComponent.publish(CHANNEL_NAME,
        new CameraConfiguration(FacingMode.USER, true, 30),
        previewView,
        this,
        PublishMode.REAL_TIME, Quality.HD);
    Toast.makeText(this, "Publishing", LENGTH_SHORT).show();
  }

  private void stopPublishing() {
    PhenixPublishComponent.stopPublishing(currentRoomService, currentPublisher, currentRenderer);
  }

  @Override
  public void onSuccess(RoomService roomService, ExpressPublisher expressPublisher,
      Renderer renderer) {
    currentRoomService = roomService;
    currentPublisher = expressPublisher;
    currentRenderer = renderer;

    subscribeLinkLabel.setText(String.format(getString(R.string.subscribeRealTimeLink),
        PhenixPublishComponent.getChannelAlias(roomService)));

    Toast.makeText(PublishActivity.this, "Publishing", LENGTH_SHORT).show();
  }

  @Override
  public void onError(PublishException e) {

    switch (e.status) {
      case BAD_REQUEST:
        // Missing or invalid arguments, reattempt with fixed parameters
        Log.e(TAG, "Unable to complete the request with the current settings");
        break;
      case UNAUTHORIZED:
        // New token required, do not retry
        Log.e(TAG, "You are not authorized to view this channel");
        break;
      case CONFLICT:
        // The request conflicts with the current state of the resource, do not retry
        Log.e(TAG, "Resource conflict occurred");
        break;
      case GONE:
        // The resource referenced is no longer available, do not retry
        Log.e(TAG, "Channel does not exist");
        break;
      case NOT_INITIALIZED:
        // PCast not initialized, do not retry
        Log.e(TAG, "PCast not initialized");
        break;
      case NOT_STARTED:
        // PCast not started, do not retry
        Log.e(TAG, "PCast not started");
        break;
      case UPGRADE_REQUIRED:
        // Upgrade is required, do not retry
        Log.e(TAG, "Upgrade required");
        break;
      case FAILED:
        // General error, retry once
        Log.e(TAG, "Something went wrong, please try again");
        break;
      case CAPACITY:
        // Capacity error, retry with back-off
        Log.e(TAG, "Too many viewers, please try again");
        break;
      case TIMEOUT:
        // Timeout error, retry once
        Log.e(TAG, "Timeout error, please try again");
        break;
      case NOT_READY:
        // Resource is not ready yet, retry
        Log.e(TAG, "We are not ready, please retry");
        break;
      case OK:
      case NO_STREAM_PLAYING:
      default:
        Log.e(TAG, "Not expected for publishing"); // please contact Phenix tea)m
    }

    Toast.makeText(PublishActivity.this, "Error: please check logs", LENGTH_LONG).show();
  }
}
