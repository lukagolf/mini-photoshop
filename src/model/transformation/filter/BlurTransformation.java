package model.transformation.filter;

/**
 * Represents a class for blurring transformation on the image.
 */
public class BlurTransformation extends FilteringTransformation {

  private static final double[][] transform = new double[][]{{0.0625, 0.125, 0.0625},
      {0.125, 0.25, 0.125}, {0.0625, 0.125, 0.0625}};

  @Override
  protected double[][] getTransform() {
    return transform;
  }
}
