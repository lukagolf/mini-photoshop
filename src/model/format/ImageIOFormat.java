package model.format;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import model.CustomColor;
import model.IViewImageModel;

/**
 * An abstract class that uses Java's built in {@link javax.imageio.ImageIO} class to import and
 * export JPG and PNG images. See {@link PNGFormat} and {@link JPGFormat} for concrete
 * implementations.
 */
public abstract class ImageIOFormat implements IImageFormatUtil {

  @Override
  public CustomColor[][] importImage(String filename) throws IllegalArgumentException {
    try {
      BufferedImage im = javax.imageio.ImageIO.read(new File(filename));
      if (im == null) {
        throw new IOException("Invalid file format!");
      }
      CustomColor[][] pixels = new CustomColor[im.getHeight()][im.getWidth()];
      for (int y = 0; y < pixels.length; y++) {
        for (int x = 0; x < pixels[y].length; x++) {
          pixels[y][x] = new CustomColor(new Color(im.getRGB(x, y)));
        }
      }
      return pixels;
    } catch (IOException e) {
      throw new IllegalArgumentException(
          "Failed to read the given file " + filename + ": " + e.getMessage());
    }
  }

  @Override
  public void exportImage(String filename, IViewImageModel image) throws IllegalArgumentException {
    BufferedImage im = new BufferedImage(image.getWidth(), image.getHeight(),
        BufferedImage.TYPE_INT_RGB);
    for (int y = 0; y < image.getHeight(); y++) {
      for (int x = 0; x < image.getWidth(); x++) {
        CustomColor c = image.getColorAt(x, y);
        im.setRGB(x, y, new Color(c.getRed(), c.getGreen(), c.getBlue()).getRGB());
      }
    }
    try {
      javax.imageio.ImageIO.write(im, this.getFormatString(), new File(filename));
    } catch (IOException e) {
      throw new IllegalArgumentException("Failed to write image: " + e.getMessage());
    }
  }

  protected abstract String getFormatString();
}
