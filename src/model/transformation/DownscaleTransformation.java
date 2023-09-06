package model.transformation;

import model.CustomColor;

/**
 * Represents a transformation to downscale the image to a new width and height inputted by the user
 * with any aspect ratio.
 */
public class DownscaleTransformation implements ITransformation {

  private final int newWidth;
  private final int newHeight;

  /**
   * Constructs a new downscale transformation with the given width and height.
   *
   * @param newWidth  is a new width of the picture in terms of pixels.
   * @param newHeight is a new height of the picture in terms of pixels.
   */
  public DownscaleTransformation(int newWidth, int newHeight) {
    this.newWidth = newWidth;
    this.newHeight = newHeight;
  }

  /**
   * Calculates the sample color value from all neighboring pixels using the formulas for
   * downscaling given by the assignment instructions.
   *
   * @param oldX the location of the old pixel in the new image in terms of x coordinate.
   * @param oldY the location of the old pixel in the new image in terms of y coordinate.
   * @param a    top left pixel of the neighboring pixels.
   * @param b    top right pixel of the neighboring pixels.
   * @param c    bottom left pixel of the neighboring pixels.
   * @param d    top right pixel of the neighboring pixels.
   * @return the sample color value from all for neighboring pixels.
   */
  private int samplePixelValues(float oldX, float oldY, int a, int b, int c, int d) {
    float m;
    float n;

    if ((int) oldX == oldX) {
      m = (a + b) / 2;
      n = (c + d) / 2;
    } else {
      m =
          ((float) b) * (oldX - (float) Math.floor(oldX)) + ((float) a) * ((float) Math.ceil(oldX)
              - oldX);
      n =
          ((float) d) * (oldX - (float) Math.floor(oldX)) + ((float) c) * ((float) Math.ceil(oldX)
              - oldX);
    }

    int color;

    if ((int) oldY == oldY) {
      color = (int) (Math.round(m + n) / 2.0);
    } else {
      color = (int) Math.round(n * (oldY - Math.floor(oldY)) + m * (Math.ceil(oldY) - oldY));
    }
    return color;
  }

  @Override
  public CustomColor[][] apply(CustomColor[][] pixels, int maxColorValue, int minColorValue) {
    float heightRatio = ((float) pixels.length) / this.newHeight;
    float widthRatio = ((float) pixels[0].length) / this.newWidth;

    CustomColor[][] downscaled = new CustomColor[newHeight][newWidth];

    for (int y = 0; y < newHeight; y++) {
      for (int x = 0; x < newWidth; x++) {
        float oldX = x * widthRatio;
        float oldY = y * heightRatio;

        try {
          int red = this.samplePixelValues(oldX, oldY,
              pixels[(int) Math.floor(oldY)][(int) Math.floor(oldX)].getRed(),
              pixels[(int) Math.floor(oldY)][(int) Math.ceil(oldX)].getRed(),
              pixels[(int) Math.ceil(oldY)][(int) Math.floor(oldX)].getRed(),
              pixels[(int) Math.ceil(oldY)][(int) Math.ceil(oldX)].getRed());

          int green = this.samplePixelValues(oldX, oldY,
              pixels[(int) Math.floor(oldY)][(int) Math.floor(oldX)].getGreen(),
              pixels[(int) Math.floor(oldY)][(int) Math.ceil(oldX)].getGreen(),
              pixels[(int) Math.ceil(oldY)][(int) Math.floor(oldX)].getGreen(),
              pixels[(int) Math.ceil(oldY)][(int) Math.ceil(oldX)].getGreen());

          int blue = this.samplePixelValues(oldX, oldY,
              pixels[(int) Math.floor(oldY)][(int) Math.floor(oldX)].getBlue(),
              pixels[(int) Math.floor(oldY)][(int) Math.ceil(oldX)].getBlue(),
              pixels[(int) Math.ceil(oldY)][(int) Math.floor(oldX)].getBlue(),
              pixels[(int) Math.ceil(oldY)][(int) Math.ceil(oldX)].getBlue());

          downscaled[y][x] = new CustomColor(red, green, blue);
        } catch (ArrayIndexOutOfBoundsException e) {
          System.out.println("x: " + x);
          System.out.println("y: " + y);
          System.out.println("widthRatio: " + widthRatio);
          System.out.println("heightRatio: " + heightRatio);
          System.out.println("oldX: " + oldX);
          System.out.println("oldY: " + oldY);
          throw e;
        }
      }
    }

    return downscaled;
  }
}
