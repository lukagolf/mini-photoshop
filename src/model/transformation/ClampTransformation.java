package model.transformation;

import model.CustomColor;
import model.ImageUtil;

/**
 * Represents an abstract class for all transformations (filtering and color) on the image.
 */
public abstract class ClampTransformation implements ITransformation {

  /**
   * Ensures that all the pixels in the image are within valid boundaries of the color values.
   *
   * @param pixels        represents a matrix of all pixels in an image
   * @param maxColorValue represents a maximum color value allowed in the image
   * @param minColorValue represents a minimum color value allowed in the image
   */
  private void clamping(CustomColor[][] pixels, int maxColorValue, int minColorValue) {
    for (int i = 0; i < pixels.length; i++) {
      for (int j = 0; j < pixels[0].length; j++) {
        pixels[i][j] = new CustomColor(
            Math.min(maxColorValue, Math.max(minColorValue, pixels[i][j].getRed())),
            Math.min(maxColorValue, Math.max(minColorValue, pixels[i][j].getGreen())),
            Math.min(maxColorValue, Math.max(minColorValue, pixels[i][j].getBlue())));
      }
    }
  }

  /**
   * Applies this transformation on the matrix of pixels of the image.
   *
   * @param pixels represents a matrix of all pixels in the image.
   * @return transformed matrix of the image.
   */
  protected abstract CustomColor[][] applyTransformation(CustomColor[][] pixels);


  @Override
  public CustomColor[][] apply(CustomColor[][] pixels, int maxColorValue, int minColorValue)
      throws IllegalArgumentException {
    ImageUtil.validation(pixels, maxColorValue, minColorValue);
    CustomColor[][] newPixels = applyTransformation(pixels);
    clamping(newPixels, maxColorValue, minColorValue);
    return newPixels;
  }
}
