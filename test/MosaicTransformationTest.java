import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;
import model.CustomColor;
import model.IImageModel;
import model.ImageModelImpl;
import model.format.JPGFormat;
import model.format.PNGFormat;
import model.format.PPMFormat;
import model.transformation.ITransformation;
import model.transformation.MosaicTransformation;
import org.junit.Before;
import org.junit.Test;

/**
 * This class tests the implementation of mosaic transformation.
 */
public class MosaicTransformationTest {

  CustomColor[][] validImage;
  ImageModelImpl teddyModel;
  ImageModelImpl boatModel;

  @Before
  public void initialize() {
    this.teddyModel = new ImageModelImpl("res/Teddy.ppm", new PPMFormat());
    this.boatModel = new ImageModelImpl("res/Boat.ppm", new PPMFormat());
    this.validImage = new CustomColor[10][15];
    for (int y = 0; y < this.validImage.length; y++) {
      for (int x = 0; x < this.validImage[y].length; x++) {
        this.validImage[y][x] = new CustomColor(Color.red);
      }
    }
  }

  @Test
  public void testDownscale() {
    // Apply a blur filter
    ITransformation mosaicTrans = new MosaicTransformation(1000);
    IImageModel mosaicTeddy = new ImageModelImpl(this.teddyModel).transform(mosaicTrans);
    mosaicTeddy.exportToFile("res/TeddyMosaic.png", new PNGFormat());
    boatModel.transform(new MosaicTransformation(1000))
        .exportToFile("res/BoatMosaic.jpg", new JPGFormat());

    Set<CustomColor> colors = new HashSet<>();
    int redSum = 0;
    int blueSum = 0;
    int greenSum = 0;
    int redSumMosaic = 0;
    int blueSumMosaic = 0;
    int greenSumMosaic = 0;

    for (int i = 0; i < teddyModel.getWidth(); i++) {
      for (int j = 0; j < teddyModel.getHeight(); j++) {
        redSum += teddyModel.getColorAt(i, j).getRed();
        greenSum += teddyModel.getColorAt(i, j).getGreen();
        blueSum += teddyModel.getColorAt(i, j).getBlue();
      }
    }
    for (int i = 0; i < mosaicTeddy.getWidth(); i++) {
      for (int j = 0; j < mosaicTeddy.getHeight(); j++) {
        redSumMosaic += mosaicTeddy.getColorAt(i, j).getRed();
        greenSumMosaic += mosaicTeddy.getColorAt(i, j).getGreen();
        blueSumMosaic += mosaicTeddy.getColorAt(i, j).getBlue();
        colors.add(mosaicTeddy.getColorAt(i, j));
      }
    }
    assertTrue(colors.size() <= 1000);
    assertTrue(redSum / redSumMosaic < 1.1 && redSum / redSumMosaic > 0.9);
    assertTrue(blueSum / blueSumMosaic < 1.1 && blueSum / blueSumMosaic > 0.9);
    assertTrue(greenSum / greenSumMosaic < 1.1 && greenSum / greenSumMosaic > 0.9);
  }
}
