package model.format;

import model.CustomColor;
import model.IViewImageModel;

/**
 * Utility interface that provides methods for importing and exporting a particular file format.
 * This interface only supports importing and exporting raster images (i.e. PPM, PNG, JPEG files).
 */
public interface IImageFormatUtil {

  /**
   * Utility method for exporting a given {@link IViewImageModel} to an image file.
   *
   * @param filename the location to save the image to.
   * @param image    the image to save as a file.
   * @throws IllegalArgumentException if the file writing operation fails.
   */
  void exportImage(String filename, IViewImageModel image) throws IllegalArgumentException;

  /**
   * Utility method for importing a file as an array of pixels representing an image.
   *
   * @param filename the location to read the file from.
   * @return the array of pixels that have been read from the file.
   * @throws IllegalArgumentException if the file reading operation fails.
   */
  CustomColor[][] importImage(String filename) throws IllegalArgumentException;
}
