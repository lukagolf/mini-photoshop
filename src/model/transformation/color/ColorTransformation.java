package model.transformation.color;

import model.CustomColor;
import model.transformation.ClampTransformation;

/**
 * Represents an abstract class for all color transformations (sepia and grey scale) on the image.
 */
public abstract class ColorTransformation extends ClampTransformation {

  @Override
  protected CustomColor[][] applyTransformation(CustomColor[][] pixels) {
    double[][] transform = getTransform();
    CustomColor[][] newColor = new CustomColor[pixels.length][pixels[0].length];
    for (int i = 0; i < pixels.length; i++) {
      for (int j = 0; j < pixels[0].length; j ++) {
        double red = transform[0][0] * pixels[i][j].getRed()
            + transform[0][1] * pixels[i][j].getGreen()
            + transform[0][2] * pixels[i][j].getBlue();
        double green = transform[1][0] * pixels[i][j].getRed()
            + transform[1][1] * pixels[i][j].getGreen()
            + transform[1][2] * pixels[i][j].getBlue();
        double blue = transform[2][0] * pixels[i][j].getRed()
            + transform[2][1] * pixels[i][j].getGreen()
            + transform[2][2] * pixels[i][j].getBlue();
        newColor[i][j] = new CustomColor((int)red, (int)green, (int)blue);
      }
    }
    return newColor;
  }

  /**
   * Gets the transforming matrix of this color transformation.
   *
   * @return transforming matrix of the color transformation
   */
  protected abstract double[][] getTransform();
}
