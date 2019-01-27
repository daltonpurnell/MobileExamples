/**
 * Copyright 2019 Phenix Real Time Solutions, Inc. All Rights Reserved.
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
import PhenixSdk
import UIKit

class ViewController: UIViewController {
  public var channelExpress: PhenixChannelExpress?

  public func stop() {
    DispatchQueue.main.async {
      self.renderer?.stop()
      self.renderer = nil
      self.userMedia = nil
      self.mainStreamPlayer = nil
    }
  }

  public func resume() {
    DispatchQueue.main.async {
      self.startMainStreamPlayer()
      self.openUserMedia()
    }
  }

  override func viewDidLoad() {
    super.viewDidLoad()
  }

  override func viewDidAppear(_ animated: Bool) {
    assert(self.channelExpress != nil)

    self.subscribeToAppStateNotifications()
    self.startMainStreamPlayer()
    self.openUserMedia()
  }

  override func viewWillDisappear(_ animated: Bool) {
    self.unsubscribeFromAppStateNotifications()
    self.mainStreamPlayer = nil
  }

  private let ciContext = CIContext(options: nil)

  private var renderer: PhenixRenderer?
  private var userMedia: PhenixUserMediaStream?
  private var mainStreamPlayer: MainStreamPlayer?

  private func openUserMedia() {
    let mediaConstraints = PhenixUserMediaOptions()
    mediaConstraints.video.enabled = true
    mediaConstraints.video.capabilityConstraints[PhenixDeviceCapability.facingMode.rawValue] =
        [PhenixDeviceConstraint.initWith(.user)]
    mediaConstraints.video.capabilityConstraints[PhenixDeviceCapability.frameRate.rawValue] =
        [PhenixDeviceConstraint.initWith(30)]
    mediaConstraints.video.capabilityConstraints[PhenixDeviceCapability.height.rawValue] =
        [PhenixDeviceConstraint.initWith(720)]
    mediaConstraints.video.capabilityConstraints[PhenixDeviceCapability.width.rawValue] =
        [PhenixDeviceConstraint.initWith(1280)]
    mediaConstraints.audio.enabled = true

    self.channelExpress?.pcastExpress.getUserMedia(mediaConstraints, { [weak self] (status, userMediaStream) in
      guard let strongSelf = self else {
        return
      }

      guard status == .ok else {
        NSLog("Failed to obtain local user media with status [\(status)]")
        return
      }

      strongSelf.userMediaReady(userMediaStream: userMediaStream!)
    })
  }

  private func userMediaReady(userMediaStream: PhenixUserMediaStream) {
    guard userMediaStream.mediaStream.getVideoTracks()?.count ?? 0 == 1 else {
      NSLog("Obtained user media contains does not have expected number of video tracks")
      return
    }

    self.userMedia = userMediaStream

    let videoTrack = userMediaStream.mediaStream.getVideoTracks()[0]

    userMediaStream.setFrameReadyCallback(videoTrack) { [weak self] (frameNotification) in
      frameNotification?.read(with: .BGRA) { [weak self] (cameraFrame) in
        guard let strongSelf = self else {
          return
        }

        guard let cameraFrame = cameraFrame else {
          return
        }

        let optionalCurrentAvPlayerPixelBuffer = strongSelf.mainStreamPlayer?.tryGetCurrentFrame()

        guard let currentAvPlayerPixelBuffer = optionalCurrentAvPlayerPixelBuffer else {
          return
        }

        strongSelf.combinePixelBuffer(currentAvPlayerPixelBuffer, with: CMSampleBufferGetImageBuffer(cameraFrame)!)

        let outputFrame = strongSelf.createOutputFrame(currentAvPlayerPixelBuffer, withTimingFrom: cameraFrame)

        frameNotification?.write(outputFrame)
      }
    }

    self.renderer = userMediaStream.mediaStream.createRenderer(Configuration.kRendererOptions)
    DispatchQueue.main.async {
      self.renderer?.start(self.view.layer)
    }
  }

  private func startMainStreamPlayer() {
    self.mainStreamPlayer = MainStreamPlayer()
  }

  private func combinePixelBuffer(_ base: CVPixelBuffer, with frame: CVPixelBuffer) {
    let baseHeight = CVPixelBufferGetHeight(base)
    let baseWidth = CVPixelBufferGetWidth(base)

    let frameHeight = CVPixelBufferGetHeight(frame)
    let frameWidth = CVPixelBufferGetWidth(frame)

    CVPixelBufferLockBaseAddress(base, CVPixelBufferLockFlags(rawValue: 0))
    let cgContext = base.createCoreGraphicsContext()

    // Nothing fancy: we just draw a small version of the frame into the base frame:
    cgContext.translateBy(x: 0, y: 0)
    cgContext.scaleBy(x: 0.25, y: 0.25)
    cgContext.draw(
        frame.createCoreGraphicsImage(self.ciContext),
        in: CGRect(x: baseWidth, y: baseHeight, width: frameWidth, height: frameHeight))

    CVPixelBufferUnlockBaseAddress(base, CVPixelBufferLockFlags(rawValue: 0))
  }

  private func createOutputFrame(
      _ pixelBuffer: CVPixelBuffer, withTimingFrom timingFrame: CMSampleBuffer) -> CMSampleBuffer {
    let presentationTimeStamp = CMSampleBufferGetPresentationTimeStamp(timingFrame)
    let duration = CMSampleBufferGetDuration(timingFrame)

    var sampleTimingInfo = CMSampleTimingInfo.init(
        duration: duration,
        presentationTimeStamp: presentationTimeStamp,
        decodeTimeStamp: CMTime.invalid)

    var formatDescription: CMFormatDescription? = nil
    CMVideoFormatDescriptionCreateForImageBuffer(
        allocator: kCFAllocatorDefault,
        imageBuffer: pixelBuffer,
        formatDescriptionOut: &formatDescription)

    var outputFrame: CMSampleBuffer? = nil
    CMSampleBufferCreateReadyWithImageBuffer(
        allocator: kCFAllocatorDefault,
        imageBuffer: pixelBuffer,
        formatDescription: formatDescription!,
        sampleTiming: &sampleTimingInfo,
        sampleBufferOut: &outputFrame)

    return outputFrame!
  }
}

extension ViewController {
  fileprivate func subscribeToAppStateNotifications() {
    NotificationCenter.default.addObserver(
        self,
        selector: #selector(onEnteredBackground(_:)),
        name: UIApplication.didEnterBackgroundNotification,
        object: nil)

    NotificationCenter.default.addObserver(
        self,
        selector: #selector(onEnteringForeground(_:)),
        name: UIApplication.willEnterForegroundNotification,
        object: nil)
  }

  fileprivate func unsubscribeFromAppStateNotifications() {
    NotificationCenter.default.removeObserver(
        self,
        name: UIApplication.didEnterBackgroundNotification,
        object: nil)

    NotificationCenter.default.removeObserver(
        self,
        name: UIApplication.willEnterForegroundNotification,
        object: nil)
  }

  @objc private func onEnteredBackground(_ notification: NSNotification) {
    self.stop()
  }

  @objc private func onEnteringForeground(_ notification: NSNotification) {
    self.resume()
  }
}
