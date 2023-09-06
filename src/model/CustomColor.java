package model;

import java.awt.Color;

/**
 * Represents a color with red, green, and blue values. This class differs from {@link
 * java.awt.Color} since it allows unbounded color values in both directions.
 */
public class CustomColor {

  private static final int MAX_COLOR_INT = 256;

  private final int red;
  private final int green;
  private final int blue;

  /**
   * Primary constructor for this object, creates a new color from provided red, green and blue
   * integer values. These values may be any legal integer.
   *
   * @param red   the red value of the color.
   * @param green the green value of the color.
   * @param blue  the blue value of the color.
   */
  public CustomColor(int red, int green, int blue) {
    this.red = red;
    this.green = green;
    this.blue = blue;
  }

  /**
   * Convenience constructor that copies the color value from a {@link java.awt.Color} object.
   *
   * @param source The Color object to copy from.
   */
  public CustomColor(Color source) {
    this(source.getRed(), source.getGreen(), source.getBlue());
  }

  /**
   * Gets the red value of this color.
   *
   * @return an integer representing the red component.
   */
  public int getRed() {
    return this.red;
  }

  /**
   * Gets the green value of this color.
   *
   * @return an integer representing the green component.
   */
  public int getGreen() {
    return this.green;
  }

  /**
   * Gets the blue value of this color.
   *
   * @return an integer representing the blue component.
   */
  public int getBlue() {
    return this.blue;
  }

  @Override
  public String toString() {
    return this.getRed() + " " + this.getGreen() + " " + this.getBlue();
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof CustomColor) {
      CustomColor otherColor = (CustomColor) other;

      return this.getRed() == otherColor.getRed() && this.getGreen() == otherColor.getGreen()
          && this.getBlue() == otherColor.getBlue();

    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return this.getRed() * MAX_COLOR_INT * MAX_COLOR_INT
        + this.getGreen() * MAX_COLOR_INT + this.getBlue();
  }

}
