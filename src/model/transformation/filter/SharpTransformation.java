package model.transformation.filter;

/**
 * Represents a class for sharpening transformation on the image.
 */
public class SharpTransformation extends FilteringTransformation {

  private static final double[][] transform =
      new double[][]{{-0.125, -0.125, -0.125, -0.125, -0.125}, {-0.125, 0.25, 0.25, 0.25, -0.125},
          {-0.125, 0.25, 1, 0.25, -0.125}, {-0.125, 0.25, 0.25, 0.25, -0.125},
          {-0.125, -0.125, -0.125, -0.125, -0.125}};

  @Override
  protected double[][] getTransform() {
    return transform;
  }
}
