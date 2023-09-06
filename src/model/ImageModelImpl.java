package model;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import model.format.IImageFormatUtil;
import model.transformation.ITransformation;

/**
 * Represents a 2D RGB image with 8-bit colors.
 */
public class ImageModelImpl implements IImageModel {

  private CustomColor[][] pixels;
  private final int maxColorValue;

  /**
   * Constructs a new {@link ImageModelImpl} from a 2D list.
   *
   * <p>Class invariants: images cannot null, and must be at least 1x1 and rectangular.
   *
   * @param pixels The list from which to construct the image.
   * @throws IllegalArgumentException if the provided {@link List} of pixels is null, empty or not
   *                                  rectangular.
   */
  public ImageModelImpl(CustomColor[][] pixels, int maxColorValue)
      throws IllegalArgumentException {
    ImageUtil.validation(pixels, maxColorValue, 0);
    this.pixels = pixels;
    this.maxColorValue = maxColorValue;

  }

  /**
   * Constructs an image from a specified file using a given {@link model.format.IImageFormatUtil}.
   *
   * @param filename the name of the file to import.
   * @param format   the format to import as.
   * @throws IllegalArgumentException if the import operation fails.
   */
  public ImageModelImpl(String filename, IImageFormatUtil format) throws IllegalArgumentException {
    this(format.importImage(filename), 255);
  }

  /**
   * Creates a copy of the given {@link IImageModel} instance.
   *
   * @param model The model to copy.
   */
  public ImageModelImpl(IImageModel model) {
    this(model.asArray(), 255);
  }


  @Override
  public void exportToFile(String filename, IImageFormatUtil format) {
    try {
      format.exportImage(filename, this);
    } catch (IllegalArgumentException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
  }

  @Override
  public CustomColor getColorAt(int x, int y) {
    return this.pixels[y][x];
  }

  @Override
  public int getHeight() {
    return this.pixels.length;
  }

  @Override
  public int getWidth() {
    return this.pixels[0].length;
  }

  @Override
  public int minColorValue() {
    return 0;
  }

  @Override
  public int maxColorValue() {
    return this.maxColorValue;
  }

  @Override
  public CustomColor[][] asArray() {
    CustomColor[][] colors = new CustomColor[pixels.length][];
    for (int i = 0; i < pixels.length; i++) {
      colors[i] = pixels[i].clone();
    }
    return colors;
  }

  @Override
  public IImageModel transform(ITransformation transformation) throws IllegalArgumentException {
    CustomColor[][] newPixels = transformation
        .apply(this.pixels, this.maxColorValue(), this.minColorValue());
    ImageUtil.validation(newPixels, this.maxColorValue(), this.minColorValue());
    this.pixels = newPixels;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ImageModelImpl that = (ImageModelImpl) o;
    return maxColorValue == that.maxColorValue && Arrays.deepEquals(pixels, that.pixels);
  }

  @Override
  public boolean almostEquals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ImageModelImpl that = (ImageModelImpl) o;
    if (maxColorValue != that.maxColorValue) {
      return false;
    }
    for (int y = 0; y < getHeight(); y++) {
      for (int x = 0; x < getWidth(); x++) {
        CustomColor ppmColor = getColorAt(x, y);
        CustomColor jpgColor = that.getColorAt(x, y);
        int delta = 2;
        if (Math.abs(ppmColor.getRed() - jpgColor.getRed()) > delta) {
          return false;
        }
        if (Math.abs(ppmColor.getGreen() - jpgColor.getGreen()) > delta) {
          return false;
        }
        if (Math.abs(ppmColor.getBlue() - jpgColor.getBlue()) > delta) {
          return false;
        }
      }
    }
    return true;
  }

  @Override
  public Image toImage() {
    BufferedImage im = new BufferedImage(this.getWidth(), this.getHeight(),
        BufferedImage.TYPE_INT_RGB);
    for (int y = 0; y < this.getHeight(); y++) {
      for (int x = 0; x < this.getWidth(); x++) {
        CustomColor c = this.getColorAt(x, y);
        im.setRGB(x, y, new Color(c.getRed(), c.getGreen(), c.getBlue()).getRGB());
      }
    }
    return im;
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(maxColorValue);
    result = 31 * result + Arrays.hashCode(pixels);
    return result;
  }
}
