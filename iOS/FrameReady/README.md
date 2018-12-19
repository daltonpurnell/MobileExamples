# iOS FrameReady API Example

### Building ###
* Ensure iOS SDK has been cloned: run `git-submodule-setup.sh` in the repository root folder
* Open Xcode project `FrameReady.xcodeproj`
* Build and Run!

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
