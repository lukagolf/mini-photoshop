package model.transformation;

import model.CustomColor;
import model.IImageModel;

/**
 * Represents a transformation that can be applied to an {@link IImageModel}.
 */
public interface ITransformation {

  /**
   * Applies this transformation to the given {@link IImageModel}. The exact way that it is applied
   * is determined by implementing classes.
   *
   * @param pixels represents a matrix of all pixels in the image
   * @param maxColorValue represents the maximum allowed color value in the image
   * @param minColorValue represents the minimum allowed color value in the image
   * @return a reference to the modified pixel matrix of the image, to allow method chaining.
   */
  public CustomColor[][] apply(CustomColor[][] pixels, int maxColorValue, int minColorValue);
}
