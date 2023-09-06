package model.transformation.color;

/**
 * Represents a class for grey scale color transformation on the image.
 */
public class GreyTransformation extends ColorTransformation {

  private static final double[][] transform = new double[][]{{0.2126, 0.7152, 0.0722},
      {0.2126, 0.7152, 0.0722},
      {0.2126, 0.7152, 0.0722}};

  @Override
  protected double[][] getTransform() {
    return transform;
  }
}