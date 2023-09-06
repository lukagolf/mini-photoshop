package model.transformation.filter;

import model.CustomColor;
import model.transformation.ClampTransformation;

/**
 * Represents an abstract class for all filtering transformations (blurring and sharpening) on the
 * image.
 */
public abstract class FilteringTransformation extends ClampTransformation {

  @Override
  protected CustomColor[][] applyTransformation(CustomColor[][] pixels) {
    double[][] transform = getTransform();
    int offset = transform.length / 2;
    CustomColor[][] newColor = new CustomColor[pixels.length][pixels[0].length];
    for (int i = 0; i < pixels.length; i++) {
      for (int j = 0; j < pixels[0].length; j++) {
        double red = 0;
        double green = 0;
        double blue = 0;
        for (int x = 0; x < transform.length; x++) {
          for (int y = 0; y < transform.length; y++) {
            if (i + x - offset >= 0 && i + x - offset < pixels.length
                && j + y - offset >= 0 && j + y - offset < pixels[0].length) {
              red += transform[x][y] * pixels[i + x - offset][j + y - offset].getRed();
              green += transform[x][y] * pixels[i + x - offset][j + y - offset].getGreen();
              blue += transform[x][y] * pixels[i + x - offset][j + y - offset].getBlue();
            }
          }
        }
        newColor[i][j] = new CustomColor((int) red, (int) green, (int) blue);
      }
    }
    return newColor;
  }

  /**
   * Gets the transforming matrix of this filtering transformation.
   *
   * @return transforming matrix of the filtering transformation
   */
  protected abstract double[][] getTransform();
}
