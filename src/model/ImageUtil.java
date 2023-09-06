package model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
import model.format.IImageFormatUtil;
import model.format.JPGFormat;
import model.format.PNGFormat;
import model.format.PPMFormat;


/**
 * This class contains utility methods to read a PPM image from file and simply print its contents.
 * Feel free to change this method as required.
 */
public class ImageUtil {

  /**
   * Read an image file in the PPM format and print the colors.
   *
   * @param filename the path of the file.
   */
  public static CustomColor[][] readPPM(String filename) {
    Scanner sc;

    try {
      sc = new Scanner(new FileInputStream(filename));
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("The provided file " + filename + " doesn't exist!");
    }
    StringBuilder builder = new StringBuilder();
    //read the file line by line, and populate a string. This will throw away any comment lines
    while (sc.hasNextLine()) {
      String s = sc.nextLine();
      if (s.length() == 0 || s.charAt(0) != '#') {
        builder.append(s + System.lineSeparator());
      }
    }

    //now set up the scanner to read from the string we just built
    sc = new Scanner(builder.toString());

    String token;

    token = sc.next();
    if (!token.equals("P3")) {
      System.out.println("Invalid PPM file: plain RAW file should begin with P3");
    }
    int width = sc.nextInt();
    int height = sc.nextInt();
    int maxValue = sc.nextInt();

    CustomColor[][] pixels = new CustomColor[height][width];

    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        pixels[i][j] = new CustomColor(sc.nextInt(), sc.nextInt(), sc.nextInt());
      }
    }

    return pixels;
  }

  /**
   * Determines if the provided list is rectangular (i.e. all columns have the same length).
   *
   * @param pixels A 2D ArrayList representing the image.
   * @return True if the list is rectangular, false otherwise.
   * @throws IllegalArgumentException If the provided {@link List} is null, smaller than 1x1 or
   *                                  contains null columns.
   */
  public static boolean isRectangular(CustomColor[][] pixels) throws IllegalArgumentException {
    if (pixels == null) {
      throw new IllegalArgumentException("The given list must not be null!");
    } else if (pixels.length < 1 || pixels[0].length < 1) {
      throw new IllegalArgumentException("The given list must be at least 1x1!");
    }
    int height = pixels[0].length;
    for (CustomColor[] col : pixels) {
      if (col == null) {
        throw new IllegalArgumentException("The given list must not contain null columns!");
      }
      if (col.length != height) {
        return false;
      }
    }
    return true;
  }

  /**
   * Checks whether the given pixels of an image are not larger that the provided maximum color
   * value.
   *
   * @param pixels        represents a matrix of all pixels in an image
   * @param maxColorValue represents the maximum value each component of RGB can take
   * @return true if all pixels have color value withing the maximum value
   */
  public static boolean validColorValues(CustomColor[][] pixels, int maxColorValue) {
    for (CustomColor[] col : pixels) {
      for (CustomColor pixel : col) {
        if (pixel == null || pixel.getRed() > maxColorValue || pixel.getGreen() > maxColorValue
            || pixel.getBlue() > maxColorValue) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Throws an IllegalArgumentException if maximum color value is less than 1, if matrix of pixels
   * is not rectangular, and if any of the pixels have invalid color values.
   *
   * @param pixels        represents a matrix of all pixels in an image
   * @param maxColorValue represents the maximum value each component of RGB can take
   * @param minColorValue represents the minimum value each component of RGB can take
   * @throws IllegalArgumentException if maximum color value is less minimum, if matrix of pixels is
   *                                  not rectangular, or if any of the pixels have invalid color
   *                                  values.
   */
  public static void validation(CustomColor[][] pixels, int maxColorValue, int minColorValue)
      throws IllegalArgumentException {
    // This method will also ensure the image is non-null and not empty.
    if (maxColorValue < minColorValue) {
      throw new IllegalArgumentException(
          "The maximum color value must be greater than the minimum color value.");
    } else if (!isRectangular(pixels)) {
      throw new IllegalArgumentException(
          "The given list must be rectangular (i.e. all columns have the same length).");
    } else if (!validColorValues(pixels, maxColorValue)) {
      throw new IllegalArgumentException(
          "The given list must not contain any values greater than the max color value of "
              + maxColorValue + ".");
    }
  }

  /**
   * Determines the format referenced by a string.
   *
   * @param s the string to de-reference.
   * @return the format specified by the string.
   * @throws IllegalArgumentException if the format specified by the string is unrecognized.
   */
  public static IImageFormatUtil formatFromString(String s) throws IllegalArgumentException {
    IImageFormatUtil format;
    switch (s) {
      case "ppm":
        format = new PPMFormat();
        break;
      case "png":
        format = new PNGFormat();
        break;
      case "jpg":
        format = new JPGFormat();
        break;
      default:
        throw new IllegalArgumentException(
            "Unsupported format specified, must be one of: ppm, png, jpg");
    }
    return format;
  }

  /**
   * Tests reading a ppm file.
   *
   * @param args command line arguments.
   */
  public static void main(String[] args) {
    String filename;

    if (args.length > 0) {
      filename = args[0];
    } else {
      filename = "res/Teddy.ppm";
    }
    ImageUtil.readPPM(filename);
  }
}

