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

import android.content.Context;
import android.util.Log;
import androidx.annotation.StringRes;
import com.phenixrts.common.RequestStatus;
import com.phenixrts.environment.android.AndroidContext;
import com.phenixrts.express.*;

public class PhenixHelper {

    private static final String TAG = PhenixHelper.class.getSimpleName();

    /**
     * Your custom backend URI.
     *
     * @see <a href=https://phenixrts.com/docs/api/#backend-setup-node-js-example>Backend setup
     * documentation</a>
     */
    public static final String BACKEND_URI = "https://demo-integration.phenixrts.com/pcast";
    public static final String PCAST_URI = "";

    /**
     * Channel to which you'd like to subscribe
     */
    public static final String CHANNEL_ALIAS = "mobileSimpleChannel";


    /**
     * Simple {@link ChannelExpress} creator
     *
     * @param context    context with same life time
     * @param backendUri your personal backend uri
     * @return channel express with minimum configuration
     */
    public static ChannelExpress createChannelExpress(Context context, String backendUri,
                                                      String pcastUri) {
        AndroidContext.setContext(context.getApplicationContext());

        final PCastExpressOptions pcastExpressOptions =
                PCastExpressFactory.createPCastExpressOptionsBuilder()
                        .withBackendUri(backendUri)
                        .withPCastUri(pcastUri)
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
