# iOS FrameReady API Example

### Building ###
1) Make sure you have `git-lfs` installed
2) Inside project folder run `pod install`
-- The SDK is in a [private repository](https://github.com/PhenixRTS/iOSSDK). In case the SDK is not downloaded correctly, please confirm that you have access to the repository.
3) Open Xcode workspace file (`.xcworkspace` extension)
4) Build and Run!

### What does it do? ###
* Opens local user media stream (`PhenixPCastExpress.getUserMedia`)
  * Captures video from built in camera (user facing) and built in microphone
* Starts a stream using built in iOS `AVPlayer` ("https://archive.org/download/ElephantsDream/ed_hd_512kb.mp4")
* Sets up a frame-ready callback (`PhenixUserMediaStream.setFrameReadyCallback`) for video track
  * Callback will be invoked for each camera frame we capture
  * Inside callback we:
    * Read frame from local camera in `BGRA` pixel format
    * Read most recent video frame from `AVPlayer` stream
    * Composite the 2 frames (superimposing small version of camera frame on top of stream frame)
    * Write back composited frame
* Render local user media stream in main view, which will show the composited stream

Notes:
* When the app is backgrounded, the local user media and the `AVPlayer` stream will be stopped
* When the app comes back into foreground, local user media and `AVPlayer` stream will be restarted
