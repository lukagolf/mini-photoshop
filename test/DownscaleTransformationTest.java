import static org.junit.Assert.assertEquals;

import java.awt.Color;
import model.CustomColor;
import model.IImageModel;
import model.ImageModelImpl;
import model.format.JPGFormat;
import model.format.PNGFormat;
import model.format.PPMFormat;
import model.transformation.DownscaleTransformation;
import model.transformation.ITransformation;
import org.junit.Before;
import org.junit.Test;

/**
 * This class tests the implementation of downscaling transformation.
 */
public class DownscaleTransformationTest {

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
    ITransformation downscaleTrans = new DownscaleTransformation(128, 128);
    IImageModel downscaleTeddy = new ImageModelImpl(this.teddyModel).transform(downscaleTrans);
    downscaleTeddy.exportToFile("res/TeddyDownscale.png", new PNGFormat());
    boatModel.transform(new DownscaleTransformation(128, 128))
        .exportToFile("res/BoatDownScale.jpg", new JPGFormat());

    for (int i = 0; i < 128; i++) {
      for (int j = 0; j < 128; j++) {
        CustomColor c = new CustomColor(
            teddyModel.getColorAt(2 * i, 2 * j).getRed(),
            teddyModel.getColorAt(2 * i, 2 * j).getGreen(),
            teddyModel.getColorAt(2 * i, 2 * j).getBlue());

        assertEquals(c, downscaleTeddy.getColorAt(i, j));
      }
    }
  }
}
