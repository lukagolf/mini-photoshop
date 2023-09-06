package model;

import java.awt.Color;

/**
 * Builder class for programmatically creating a checkerboard {@link IImageModel}, allowing users to
 * specify tile colors, sizes and dimensions.
 */
public class CheckerboardImageBuilder {

  private CustomColor c1;
  private CustomColor c2;
  private int x;
  private int y;
  private int height;
  private int width;

  /**
   * Constructs a new builder using the default of a black and white, 20x10 checkerboard with tiles
   * of 50px x 50px.
   */
  public CheckerboardImageBuilder() {
    this.c1 = new CustomColor(Color.black);
    this.c2 = new CustomColor(Color.white);
    this.x = 20;
    this.y = 10;
    this.width = 50;
    this.height = 50;
  }

  /**
   * Sets the primary color of the checkerboard to be created.
   * @param c1 primary color to use.
   * @return a reference to this builder to enable method chaining.
   */
  public CheckerboardImageBuilder setC1(CustomColor c1) {
    this.c1 = c1;
    return this;
  }

  /**
   * Sets the secondary color of the checkerboard to be created.
   * @param c2 secondary color to use.
   * @return a reference to this builder to enable method chaining.
   */
  public CheckerboardImageBuilder setC2(CustomColor c2) {
    this.c2 = c2;
    return this;
  }

  /**
   * Sets the width of the image in tiles.
   * @param x the width of the image in tiles.
   * @return a reference to this builder to enable method chaining.
   */
  public CheckerboardImageBuilder setX(int x) {
    this.x = x;
    return this;
  }

  /**
   * Sets the height of the image in tiles.
   * @param y the height of the image in tiles.
   * @return a reference to this builder to enable method chaining.
   */
  public CheckerboardImageBuilder setY(int y) {
    this.y = y;
    return this;
  }

  /**
   * Sets the width of each tile.
   * @param width width of tiles in pixels.
   * @return a reference to this builder to enable method chaining.
   */
  public CheckerboardImageBuilder width(int width) {
    this.width = width;
    return this;
  }

  /**
   * Sets the height of each tile.
   * @param height height of tiles in pixels.
   * @return a reference to this builder to enable method chaining.
   */
  public CheckerboardImageBuilder height(int height) {
    this.height = height;
    return this;
  }

  /**
   * Creates the {@link IImageModel} with the specified parameters.
   * @return a new checkerboard image.
   */
  public IImageModel create() {
    CustomColor[][] pixels = new CustomColor[this.height * this.y][this.width * this.x];
    for (int j = 0; j < pixels.length; j++) {
      for (int i = 0; i < pixels[y].length; i++) {
        int sqX = i / this.width;
        int sqY = j / this.height;
        if (sqX % 2 == sqY % 2) {
          pixels[j][i] = this.c1;
        } else {
          pixels[j][i] = this.c2;
        }
      }
    }

    return new ImageModelImpl(pixels, 255);
  }
}
