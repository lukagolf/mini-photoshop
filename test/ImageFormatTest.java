import static org.junit.Assert.assertEquals;

import model.CustomColor;
import model.IImageModel;
import model.ImageModelImpl;
import model.format.JPGFormat;
import model.format.PNGFormat;
import model.format.PPMFormat;
import org.junit.Test;

/**
 * Represents a class that tests whether the importing of PNG and JPG works properly.
 */
public class ImageFormatTest {

  @Test
  public void testImportPNG() {
    IImageModel teddyPNG = new ImageModelImpl("res/Teddy.png", new PNGFormat());
    IImageModel teddyPPM = new ImageModelImpl("res/Teddy.ppm", new PPMFormat());
    for (int y = 0; y < teddyPNG.getHeight(); y++) {
      for (int x = 0; x < teddyPNG.getWidth(); x++) {
        assertEquals(teddyPPM.getColorAt(x, y), teddyPNG.getColorAt(x, y));
      }
    }
  }

  @Test
  public void testImportJPG() {
    IImageModel teddyJPG = new ImageModelImpl("res/Teddy.jpg", new JPGFormat());
    IImageModel teddyPPM = new ImageModelImpl("res/Teddy.ppm", new PPMFormat());
    for (int y = 0; y < teddyJPG.getHeight(); y++) {
      for (int x = 0; x < teddyJPG.getWidth(); x++) {
        CustomColor ppmColor = teddyPPM.getColorAt(x, y);
        CustomColor jpgColor = teddyJPG.getColorAt(x, y);
        int delta = 4;
        assertEquals(ppmColor.getRed(), jpgColor.getRed(), delta);
        assertEquals(ppmColor.getGreen(), jpgColor.getGreen(), delta);
        assertEquals(ppmColor.getBlue(), jpgColor.getBlue(), delta);
      }
    }
  }
}
