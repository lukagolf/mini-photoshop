package model;

import java.awt.Image;
import model.format.IImageFormatUtil;

/**
 * Observing (non-mutating) methods of an Image model. To be used by a view implementation, to avoid
 * mutating the model.
 */
public interface IViewImageModel {

  /**
   * Save this image to a file at the given location using the provided {@link IImageFormatUtil}.
   *
   * @param filename the filename to save this image to.
   * @param format   image format to export to.
   * @throws IllegalArgumentException if the export operation fails.
   */
  void exportToFile(String filename, IImageFormatUtil format)
      throws IllegalArgumentException;

  /**
   * Retrieves the color at a given x- and y-coordinate.
   *
   * @param x The x-coordinate of the pixel.
   * @param y The x-coordinate of the pixel.
   * @return The color at the specified location.
   */
  CustomColor getColorAt(int x, int y);

  /**
   * Gets the width of this image.
   *
   * @return The width of the image.
   */
  int getWidth();

  /**
   * Gets the width of this image.
   *
   * @return The width of the image.
   */
  int getHeight();

  /**
   * Returns the minimum possible value for a color channel of an image (red, green or blue).
   *
   * @return An integer representing the minimum possible value.
   */
  int minColorValue();

  /**
   * Returns the maximum possible value for a color channel of an image (red, green or blue).
   *
   * @return An integer representing the maximum possible value.
   */
  int maxColorValue();

  /**
   * Returns a 2D array representation of this image.
   *
   * @return A 2D array representing this image.
   */
  CustomColor[][] asArray();

  /**
   * Similar method to equals that accounts for slight variations in color value due to JPG lossy
   * compression.
   *
   * @param o the object to compare this image to.
   * @return true if the two objects are "almost" equal, as defined above.
   */
  boolean almostEquals(Object o);

  /**
   * Creates a new image from the pixel data stored in this model. This can be used to create GUIs,
   * for example, using {@link java.awt.image.BufferedImage}.
   *
   * @return the {@link java.awt} representation of this image.
   * @throws IllegalArgumentException if the layer cannot be rendered.
   */
  Image toImage() throws IllegalArgumentException;
}
