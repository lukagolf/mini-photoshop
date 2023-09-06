import static org.junit.Assert.assertEquals;

import java.awt.Color;
import model.CheckerboardImageBuilder;
import model.CustomColor;
import model.IImageModel;
import model.format.PPMFormat;
import org.junit.Test;

/**
 * Represents a class that contains the tests for all methods in {@link CheckerboardImageBuilder}.
 */
public class CheckerboardImageBuilderTest {

  @Test
  public void testCheckerboard() {
    IImageModel checkerBoard = new CheckerboardImageBuilder().setC1(new CustomColor(Color.red))
        .setC2(new CustomColor(Color.white))
        .setX(15).setY(10).create();
    checkerBoard.exportToFile("res/checkerboard.ppm", new PPMFormat());
    for (int i = 0; i < checkerBoard.getWidth(); i++) {
      for (int j = 0; j < checkerBoard.getHeight(); j++) {
        int squareX = i / 50;
        int squareY = j / 50;
        if (squareX % 2 == squareY % 2) {
          assertEquals(new CustomColor(Color.red), checkerBoard.getColorAt(i, j));
        } else {
          assertEquals(new CustomColor(Color.white), checkerBoard.getColorAt(i, j));
        }
      }
    }
  }

  @Test
  public void testCheckerboardCustomWidthHeight() {
    IImageModel checkerBoard = new CheckerboardImageBuilder().setC1(new CustomColor(Color.red))
        .setC2(new CustomColor(Color.white))
        .setX(15).setY(10).width(10).height(20).create();
    for (int i = 0; i < checkerBoard.getWidth(); i++) {
      for (int j = 0; j < checkerBoard.getHeight(); j++) {
        int squareX = i / 10;
        int squareY = j / 20;
        if (squareX % 2 == squareY % 2) {
          assertEquals(new CustomColor(Color.red), checkerBoard.getColorAt(i, j));
        } else {
          assertEquals(new CustomColor(Color.white), checkerBoard.getColorAt(i, j));
        }
      }
    }
  }

  @Test
  public void testCheckerboardDefaults() {
    IImageModel checkerBoard = new CheckerboardImageBuilder().create();
    for (int i = 0; i < checkerBoard.getWidth(); i++) {
      for (int j = 0; j < checkerBoard.getHeight(); j++) {
        int squareX = i / 50;
        int squareY = j / 50;
        if (squareX % 2 == squareY % 2) {
          assertEquals(new CustomColor(Color.black), checkerBoard.getColorAt(i, j));
        } else {
          assertEquals(new CustomColor(Color.white), checkerBoard.getColorAt(i, j));
        }
      }
    }
  }
}
