/*
 * Copyright 2019 Phenix Real Time Solutions, Inc. Confidential and Proprietary. All Rights Reserved.
 *
 *  By using this code you agree to the Phenix Terms of Service found online here:
 *  http://phenixrts.com/terms-of-service.html
 */
import AVFoundation

final class MainStreamPlayer {
  init() {
    let urlAsset = AVURLAsset(url: URL(string: Configuration.kMainStreamUrl)!)
    let videoOutputSettings = [String(kCVPixelBufferPixelFormatTypeKey): kCVPixelFormatType_32BGRA,
                               String(kCVPixelBufferCGBitmapContextCompatibilityKey): true] as [String : Any]

    self.avPlayerItemVideoOutput = AVPlayerItemVideoOutput(pixelBufferAttributes: videoOutputSettings)
    self.avPlayerItem = AVPlayerItem(asset: urlAsset)
    self.avPlayerItem.add(self.avPlayerItemVideoOutput)

    self.avPlayer = AVPlayer(playerItem: self.avPlayerItem)

    self.avPlayer.play()

    // NOTE: Would prefer to use AVQueuePlayer/AVPlayerLooper but that does not seem to work when there is a
    //       AVPlayerItemVideoOutput attached to the player item.
    self.loopPlayback()
  }

  public func tryGetCurrentFrame() -> CVPixelBuffer? {
    return self.avPlayerItemVideoOutput.copyPixelBuffer(
        forItemTime: self.avPlayerItem.currentTime(), itemTimeForDisplay: nil)
  }

  private let avPlayer: AVPlayer
  private let avPlayerItemVideoOutput: AVPlayerItemVideoOutput
  private let avPlayerItem: AVPlayerItem

  private func loopPlayback() {
    NotificationCenter.default.addObserver(
        forName: NSNotification.Name.AVPlayerItemDidPlayToEndTime,
        object: self.avPlayerItem,
        queue: nil) { notification in
          self.avPlayer.seek(to: CMTime.zero)
          self.avPlayer.play()
        }
  }
}
