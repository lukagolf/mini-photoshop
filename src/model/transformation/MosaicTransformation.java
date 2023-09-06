package model.transformation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import model.CustomColor;

/**
 * Represents a transformation to mosaic the image with the number of random seeds given by the
 * user. Also allows passing a random object for ease of testing.
 */
public class MosaicTransformation implements ITransformation {

  /**
   * Represents a cluster of pixels and all the operations related to that cluster.
   */
  private static class Cluster {

    int x;
    int y;
    int sumRed;
    int sumGreen;
    int sumBlue;
    int numOfPixels;

    /**
     * Constructs a new cluster with the given point at its center.
     *
     * @param x coordinate of the cluster centre
     * @param y coordinate of the cluster centre
     */
    public Cluster(int x, int y) {
      this.x = x;
      this.y = y;
    }

    /**
     * Calculates the distance between the two given points.
     *
     * @param xPos x point in space
     * @param yPos y point in space
     * @return distance between the two given points x and y
     */
    private double calcDistance(int xPos, int yPos) {
      return Math.sqrt((x - xPos) * (x - xPos) + (y - yPos) * (y - yPos));
    }

    /**
     * Adds the each RGB component of the given color the each totol sum of the RGB components.
     *
     * @param c provided custom color.
     */
    private void addPixel(CustomColor c) {
      sumRed += c.getRed();
      sumGreen += c.getGreen();
      sumBlue += c.getBlue();
      numOfPixels++;
    }

    /**
     * Calculates the average new color by dividing each of RGB components by the number of pixels
     * in this cluster.
     *
     * @return new color of the cluster.
     */
    private CustomColor getCustomColor() {
      return new CustomColor(sumRed / numOfPixels, sumGreen / numOfPixels,
          sumBlue / numOfPixels);
    }
  }

  private final int n;
  private final Random r;

  /**
   * Constructs a new instance of a mosaic transformation with a given number of seeds and an
   * instance of {@link Random} that can be used for deterministic testing.
   *
   * @param n the number of seeds to create.
   * @param r the random object to use for determining seed locations.
   */
  public MosaicTransformation(int n, Random r) {
    this.n = n;
    this.r = r;
  }

  /**
   * Convenience constructor that creates a MosaicTransformation with a randomly generated seed.
   *
   * @param n the number of seeds to create and randomly place.
   */
  public MosaicTransformation(int n) {
    this(n, new Random());
  }


  @Override
  public CustomColor[][] apply(CustomColor[][] pixels, int maxColorValue, int minColorValue) {
    List<Cluster> clusters = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      clusters.add(new Cluster(this.r.nextInt(pixels[0].length), this.r.nextInt(pixels.length)));
    }
    int[][] pointsCluster = new int[pixels[0].length][pixels.length];

    for (int x = 0; x < pixels[0].length; x++) {
      for (int y = 0; y < pixels.length; y++) {
        int nearest = 0;
        for (int i = 0; i < clusters.size(); i++) {
          if (clusters.get(nearest).calcDistance(x, y) > clusters.get(i).calcDistance(x, y)) {
            nearest = i;
          }
        }
        pointsCluster[x][y] = nearest;
        clusters.get(nearest).addPixel(pixels[x][y]);
      }
    }

    CustomColor[][] newPicture = new CustomColor[pixels.length][pixels[0].length];
    for (int x = 0; x < pixels[0].length; x++) {
      for (int y = 0; y < pixels.length; y++) {
        newPicture[x][y] = clusters.get(pointsCluster[x][y]).getCustomColor();
      }
    }
    return newPicture;
  }
}