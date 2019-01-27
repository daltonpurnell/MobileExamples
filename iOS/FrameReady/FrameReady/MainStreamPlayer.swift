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
