/*
 * Copyright 2019 Phenix Real Time Solutions, Inc. Confidential and Proprietary. All Rights Reserved.
 *
 * By using this code you agree to the Phenix Terms of Service found online here:
 * http://phenixrts.com/terms-of-service.html
 */

package com.phenixrts.demo.simpledemoapps.publisher;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import com.phenixrts.environment.android.AndroidContext;
import com.phenixrts.express.ChannelExpress;
import com.phenixrts.express.ChannelExpressFactory;
import com.phenixrts.express.ChannelExpressOptions;
import com.phenixrts.express.PCastExpressFactory;
import com.phenixrts.express.PCastExpressOptions;
import com.phenixrts.express.RoomExpressFactory;
import com.phenixrts.express.RoomExpressOptions;

public class MyApplication extends Application {

  private static final String TAG = MyApplication.class.getSimpleName();

  /**
   * It is recommended to create single instance during app creation
   */
  public static ChannelExpress channelExpress;
  /**
   * Channel to which you'd like to subscribe
   */
  public static final String CHANNEL_NAME = "mobileSimpleChannel";

  /**
   * Your custom backend URI.
   *
   * @see <a href=https://phenixrts.com/docs/api/#backend-setup-node-js-example>Backend setup
   * documentation</a>
   */
  private static final String BACKEND_URI = "https://demo-integration.phenixrts.com/pcast";

  @Override
  public void onCreate() {
    super.onCreate();

    channelExpress = createChannelExpress(this, BACKEND_URI);
  }

  public static ChannelExpress createChannelExpress(Context context, String backendUri) {
    AndroidContext.setContext(context.getApplicationContext());

    final PCastExpressOptions pcastExpressOptions =
        PCastExpressFactory.createPCastExpressOptionsBuilder()
            .withBackendUri(backendUri)
            .withUnrecoverableErrorCallback(
                (requestStatus, s) -> Log.e(TAG, "Phenix couldn't connect: " + s))
            .buildPCastExpressOptions();

    final RoomExpressOptions roomExpressOptions =
        RoomExpressFactory.createRoomExpressOptionsBuilder()
            .withPCastExpressOptions(pcastExpressOptions)
            .buildRoomExpressOptions();

    final ChannelExpressOptions channelExpressOptions =
        ChannelExpressFactory.createChannelExpressOptionsBuilder()
            .withRoomExpressOptions(roomExpressOptions)
            .buildChannelExpressOptions();

    return ChannelExpressFactory.createChannelExpress(channelExpressOptions);
  }
}
