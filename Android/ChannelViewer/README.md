# Channel Viewer Example Application
This application shows how to subscribe to a channel using the [Phenix Channel Express API](https://phenixrts.com/docs/android/#channel-express).

This example application shows how to:
1. Select the capabilities to subscribe with
2. Wait for the stream to start
3. Stop the stream
4. Handle potential subscription errors

For more details and additional features, please refer to our [Channel Express API](https://phenixrts.com/docs/android/low-level/) documentation.

## How to Run
1) Make sure you have `git-lfs` installed
2) After cloning the repository, download the Phenix SDK by running `git submodule init` and `git submodule update`.
-- The SDK is in a [private repository](https://github.com/PhenixRTS/AndroidSDK/). In case the SDK is not downloaded correctly, please confirm that you have access to the repository.
3) To be able to subscribe, you need to publish to a channel first.
-- You can use our online [channel demo](https://demo.phenixrts.com/channel/publish/#mobileSimpleChannel) to publish a stream.
4) If desired, modify `PhenixHelper.CHANNEL_ALIAS` constant

## See Also
### Related Examples
* [Mobile Examples](https://github.com/PhenixRTS/MobileExamples)
* [Web Examples](https://github.com/PhenixRTS/WebExamples)
### Documentation
* [Channel Viewer Tutorial](https://phenixrts.com/docs/android/#view-a-channel)
* [Phenix Channel Express](https://phenixrts.com/docs/android/#channel-express)
* [Phenix Low Level API](https://phenixrts.com/docs/android/low-level/)
* [Phenix Platform Documentation](http://phenixrts.com/docs/)