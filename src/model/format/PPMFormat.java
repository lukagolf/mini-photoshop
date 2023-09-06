package model.format;

import java.io.FileWriter;
import java.io.IOException;
import model.CustomColor;
import model.IViewImageModel;
import model.ImageUtil;

/**
 * Utility class that provides method for importing and exporting PPM files.
 */
public class PPMFormat implements IImageFormatUtil {

  @Override
  public void exportImage(String filename, IViewImageModel image) throws IllegalArgumentException {
    try {
      FileWriter myImage = new FileWriter(filename);
      myImage.write("P3\n");
      myImage.write(image.getWidth() + " " + image.getHeight() + "\n");
      myImage.write(image.maxColorValue() + "\n");
      for (int i = 0; i < image.getHeight(); i++) {
        for (int j = 0; j < image.getWidth(); j++) {
          CustomColor pix = image.getColorAt(j, i);
          myImage.write(pix.getRed() + "\n" + pix.getGreen()
              + "\n" + pix.getBlue() + "\n");
        }
      }
      myImage.close();
    } catch (IOException e) {
      throw new IllegalArgumentException("Failed to export image!");
    }
  }

  @Override
  public CustomColor[][] importImage(String filename) throws IllegalArgumentException {
    return ImageUtil.readPPM(filename);
  }
}
