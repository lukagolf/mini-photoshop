package model;

import model.transformation.ITransformation;

/**
 * All of the operations supported by an image model. This represents a grid of pixels upon which
 * you can apply filters.
 */
public interface IImageModel extends IViewImageModel {

  /**
   * Transforms this image model with the given transformation.
   *
   * <p>Effect: modifies the pixels of the image model based on the provided transformation.
   *
   * @param transformation represents the type of the transformation that will be applied to this
   *                       image model.
   * @return a reference to this image model, to allow method chaining.
   * @throws IllegalArgumentException if the transformed image does not satisfy the invariants
   *                                  (non-null, rectangular, at least 1x1).
   */
  public IImageModel transform(ITransformation transformation) throws IllegalArgumentException;
}
