/*
 * Copyright 2019 Phenix Real Time Solutions, Inc. Confidential and Proprietary. All Rights Reserved.
 *
 *  By using this code you agree to the Phenix Terms of Service found online here:
 *  http://phenixrts.com/terms-of-service.html
 */
import CoreImage
import CoreVideo

extension CVPixelBuffer {
  func createCoreGraphicsImage(_ ciContext: CIContext) -> CGImage {
    let ciImage = CIImage(cvPixelBuffer: self)
    return ciContext.createCGImage(ciImage, from: ciImage.extent)!
  }

  func createCoreGraphicsContext() -> CGContext {
    let height = CVPixelBufferGetHeight(self)
    let width = CVPixelBufferGetWidth(self)
    let pixelData = CVPixelBufferGetBaseAddress(self)

    let rgbColorSpace = CGColorSpaceCreateDeviceRGB()
    let bitmapInfo = CGBitmapInfo(
        rawValue: CGBitmapInfo.byteOrder32Little.rawValue | CGImageAlphaInfo.premultipliedFirst.rawValue)
    let context = CGContext(
        data: pixelData,
        width: Int(width),
        height: Int(height),
        bitsPerComponent: 8,
        bytesPerRow: CVPixelBufferGetBytesPerRow(self),
        space: rgbColorSpace,
        bitmapInfo: bitmapInfo.rawValue)

    return context!
  }
}
