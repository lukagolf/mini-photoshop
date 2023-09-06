import java.awt.Color;
import model.CustomColor;
import model.ImageUtil;
import org.junit.Before;
import org.junit.Test;

/**
 * Represents a class that contains the tests for all possible exceptions in {@link ImageUtil}.
 */
public class ImageUtilExceptionsTest {

  CustomColor[][] validImage;

  @Before
  public void initData() {
    this.validImage = new CustomColor[10][15];
    for (int y = 0; y < this.validImage.length; y++) {
      for (int x = 0; x < this.validImage[y].length; x++) {
        this.validImage[y][x] = new CustomColor(Color.red);
      }
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLoadNonExistantFile() {
    ImageUtil.readPPM("non_existant_file.ppm");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullImage() {
    ImageUtil.validation(null, 255, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidColorValue() {
    ImageUtil.validation(this.validImage, 0, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testOutOfRangeColorValue() {
    this.validImage[5][5] = new CustomColor(10, 10, 1000);
    ImageUtil.validation(this.validImage, 255, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNonRectangularImage() {
    this.validImage[5] = new CustomColor[1];
    ImageUtil.validation(this.validImage, 255, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullColumn() {
    this.validImage[5] = null;
    ImageUtil.validation(this.validImage, 255, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEmptyImage() {
    ImageUtil.validation(new CustomColor[0][0], 255, 0);
  }
}
