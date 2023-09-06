package model.transformation.color;

/**
 * Represents a class for sepia color transformation on the image.
 */
public class SepiaTransformation extends ColorTransformation {

  private static final double[][] transform = new double[][]{{0.393, 0.769, 0.189},
      {0.349, 0.686, 0.168},
      {0.272, 0.534, 0.131}};

  @Override
  protected double[][] getTransform() {
    return transform;
  }
}
