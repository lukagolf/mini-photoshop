import static org.junit.Assert.assertEquals;

import java.awt.Color;
import model.CustomColor;
import model.IImageModel;
import model.ImageModelImpl;
import model.format.PPMFormat;
import model.transformation.color.GreyTransformation;
import model.transformation.color.SepiaTransformation;
import model.transformation.filter.BlurTransformation;
import model.transformation.filter.SharpTransformation;
import org.junit.Before;
import org.junit.Test;

/**
 * Represents the tests for all the methods in {@link ImageModelImpl}.
 */
public class ImageModelImplTest {

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
  public void testMinAndMaxValue() {
    assertEquals(0, this.teddyModel.minColorValue());
    assertEquals(255, this.teddyModel.maxColorValue());
  }

  @Test
  public void testSaveAndImport() {
    teddyModel.exportToFile("res/TeddyCopy.ppm", new PPMFormat());
    boatModel.exportToFile("res/BoatCopy.ppm", new PPMFormat());
    ImageModelImpl newModel = new ImageModelImpl("res/TeddyCopy.ppm", new PPMFormat());
    for (int i = 0; i < newModel.getHeight(); i++) {
      for (int j = 0; j < newModel.getWidth(); j++) {
        assertEquals(teddyModel.getColorAt(j, i), newModel.getColorAt(j, i));
      }
    }
  }

  @Test
  public void testGrayScaleCT() {
    // Apply a grayscale color transformation
    GreyTransformation greyTrans = new GreyTransformation();
    IImageModel greyTeddy = new ImageModelImpl(this.teddyModel);
    greyTeddy.transform(greyTrans);
    greyTeddy.exportToFile("res/TeddyGrey.ppm", new PPMFormat());
    boatModel.transform(greyTrans).exportToFile("res/BoatGrey.ppm", new PPMFormat());
    for (int i = 0; i < greyTeddy.getHeight(); i++) {
      for (int j = 0; j < greyTeddy.getWidth(); j++) {
        CustomColor pixel = greyTeddy.getColorAt(i, j);
        assertEquals(pixel.getRed(), pixel.getGreen());
        assertEquals(pixel.getRed(), pixel.getBlue());
      }
    }
    for (int i = 0; i < greyTeddy.getHeight(); i++) {
      for (int j = 0; j < greyTeddy.getWidth(); j++) {
        CustomColor pixelOriginal = teddyModel.getColorAt(j, i);
        assertEquals(
            (int) (0.2126 * pixelOriginal.getRed() + 0.7152 * pixelOriginal.getGreen()
                + 0.0722 * pixelOriginal.getBlue()),
            greyTeddy.getColorAt(j, i).getRed());
      }
    }
  }

  @Test
  public void testSepiaCT() {
    // Apply a sepia color transformation
    SepiaTransformation sepiaTrans = new SepiaTransformation();
    IImageModel sepiaTeddy = new ImageModelImpl(this.teddyModel);
    sepiaTeddy.transform(sepiaTrans);
    sepiaTeddy.exportToFile("res/TeddySepia.ppm", new PPMFormat());
    boatModel.transform(sepiaTrans).exportToFile("res/BoatSepia.ppm", new PPMFormat());
    for (int i = 0; i < sepiaTeddy.getHeight(); i++) {
      for (int j = 0; j < sepiaTeddy.getWidth(); j++) {
        CustomColor pixelOriginal = teddyModel.getColorAt(j, i);
        assertEquals(
            (int) Math.min(255, 0.393 * pixelOriginal.getRed() + 0.769 * pixelOriginal.getGreen()
                + 0.189 * pixelOriginal.getBlue()),
            sepiaTeddy.getColorAt(j, i).getRed());
        assertEquals(
            (int) Math.min(255, 0.349 * pixelOriginal.getRed() + 0.686 * pixelOriginal.getGreen()
                + 0.168 * pixelOriginal.getBlue()),
            sepiaTeddy.getColorAt(j, i).getGreen());
        assertEquals(
            (int) Math.min(255, 0.272 * pixelOriginal.getRed() + 0.534 * pixelOriginal.getGreen()
                + 0.131 * pixelOriginal.getBlue()),
            sepiaTeddy.getColorAt(j, i).getBlue());
      }
    }
  }

  @Test
  public void testBlurFilter() {
    // Apply a blur filter
    BlurTransformation blurTrans = new BlurTransformation();
    IImageModel blurTeddy = new ImageModelImpl(this.teddyModel).transform(blurTrans);
    blurTeddy.exportToFile("res/TeddyBlur.ppm", new PPMFormat());
    boatModel.transform(new BlurTransformation())
        .exportToFile("res/BoatBlur.ppm", new PPMFormat());
    for (int i = 0; i < blurTeddy.getHeight(); i++) {
      for (int j = 0; j < blurTeddy.getWidth(); j++) {

        CustomColor blurPix = blurTeddy.getColorAt(j, i);

        double[][] blurKernel = new double[][]{{0.0625, 0.125, 0.0625},
            {0.125, 0.25, 0.125},
            {0.0625, 0.125, 0.0625}};

        double compRed = 0.0;
        double compGreen = 0.0;
        double compBlue = 0.0;

        for (int offsetX = -1; offsetX <= 1; offsetX++) {
          for (int offsetY = -1; offsetY <= 1; offsetY++) {
            if (i + offsetX >= 0 && i + offsetX < this.teddyModel.getWidth() && j + offsetY >= 0
                && j + offsetY < this.teddyModel.getHeight()) {
              compRed +=
                  blurKernel[offsetX + 1][offsetY + 1] * this.teddyModel
                      .getColorAt(j + offsetY, i + offsetX).getRed();
              compGreen +=
                  blurKernel[offsetX + 1][offsetY + 1] * this.teddyModel
                      .getColorAt(j + offsetY, i + offsetX).getGreen();
              compBlue +=
                  blurKernel[offsetX + 1][offsetY + 1] * this.teddyModel
                      .getColorAt(j + offsetY, i + offsetX).getBlue();
            }
          }
        }
        assertEquals(new CustomColor((int) compRed, (int) compGreen, (int) compBlue), blurPix);
      }
    }
  }

  @Test
  public void testSharpFilter() {
    // Apply a sharp filter
    SharpTransformation sharpTrans = new SharpTransformation();
    IImageModel sharpTeddy = new ImageModelImpl(this.teddyModel).transform(sharpTrans);
    sharpTeddy.exportToFile("res/TeddySharp.ppm", new PPMFormat());
    boatModel.transform(sharpTrans).exportToFile("res/BoatSharp.ppm", new PPMFormat());
    for (int i = 0; i < sharpTeddy.getHeight(); i++) {
      for (int j = 0; j < sharpTeddy.getWidth(); j++) {

        CustomColor sharpPix = sharpTeddy.getColorAt(j, i);

        double[][] sharpKernel =
            new double[][]{{-0.125, -0.125, -0.125, -0.125, -0.125},
                {-0.125, 0.25, 0.25, 0.25, -0.125},
                {-0.125, 0.25, 1, 0.25, -0.125},
                {-0.125, 0.25, 0.25, 0.25, -0.125},
                {-0.125, -0.125, -0.125, -0.125, -0.125}};

        double compRed = 0.0;
        double compGreen = 0.0;
        double compBlue = 0.0;

        for (int offsetX = -2; offsetX <= 2; offsetX++) {
          for (int offsetY = -2; offsetY <= 2; offsetY++) {
            if (i + offsetX >= 0 && i + offsetX < this.teddyModel.getWidth() && j + offsetY >= 0
                && j + offsetY < this.teddyModel.getHeight()) {
              compRed +=
                  sharpKernel[offsetX + 2][offsetY + 2] * this.teddyModel
                      .getColorAt(j + offsetY, i + offsetX).getRed();
              compGreen +=
                  sharpKernel[offsetX + 2][offsetY + 2] * this.teddyModel
                      .getColorAt(j + offsetY, i + offsetX).getGreen();
              compBlue +=
                  sharpKernel[offsetX + 2][offsetY + 2] * this.teddyModel
                      .getColorAt(j + offsetY, i + offsetX).getBlue();
            }
          }
        }
        assertEquals(new CustomColor(Math.max(0, Math.min(255, (int) compRed)),
            Math.max(0, Math.min(255, (int) compGreen)),
            Math.max(0, Math.min(255, (int) compBlue))), sharpPix);
      }
    }
  }

  @Test
  public void testBlurPlusSharp() {
    // Apply a blur + sharp filter
    BlurTransformation blurTrans = new BlurTransformation();
    SharpTransformation sharpTrans = new SharpTransformation();
    IImageModel blurTeddy = new ImageModelImpl(this.teddyModel).transform(blurTrans);
    IImageModel bsTeddy = new ImageModelImpl(blurTeddy).transform(sharpTrans);

    CustomColor[][] blurPixels = new CustomColor[bsTeddy.getHeight()][bsTeddy.getWidth()];

    for (int i = 0; i < bsTeddy.getHeight(); i++) {
      for (int j = 0; j < bsTeddy.getWidth(); j++) {

        double[][] blurKernel = new double[][]{{0.0625, 0.125, 0.0625},
            {0.125, 0.25, 0.125},
            {0.0625, 0.125, 0.0625}};

        double compRed = 0.0;
        double compGreen = 0.0;
        double compBlue = 0.0;

        CustomColor blurPix = blurTeddy.getColorAt(j, i);

        for (int offsetX = -1; offsetX <= 1; offsetX++) {
          for (int offsetY = -1; offsetY <= 1; offsetY++) {
            if (i + offsetX >= 0 && i + offsetX < this.teddyModel.getWidth() && j + offsetY >= 0
                && j + offsetY < this.teddyModel.getHeight()) {
              compRed +=
                  blurKernel[offsetX + 1][offsetY + 1] * this.teddyModel
                      .getColorAt(j + offsetY, i + offsetX).getRed();
              compGreen +=
                  blurKernel[offsetX + 1][offsetY + 1] * this.teddyModel
                      .getColorAt(j + offsetY, i + offsetX).getGreen();
              compBlue +=
                  blurKernel[offsetX + 1][offsetY + 1] * this.teddyModel
                      .getColorAt(j + offsetY, i + offsetX).getBlue();
            }
          }
        }
        blurPixels[i][j] = new CustomColor((int) compRed, (int) compGreen, (int) compBlue);
        assertEquals(new CustomColor((int) compRed, (int) compGreen, (int) compBlue), blurPix);
      }
    }

    IImageModel blurPixelsImage = new ImageModelImpl(blurPixels, 255);

    for (int i = 0; i < bsTeddy.getHeight(); i++) {
      for (int j = 0; j < bsTeddy.getWidth(); j++) {

        CustomColor sharpPix = bsTeddy.getColorAt(j, i);

        double[][] sharpKernel =
            new double[][]{{-0.125, -0.125, -0.125, -0.125, -0.125},
                {-0.125, 0.25, 0.25, 0.25, -0.125},
                {-0.125, 0.25, 1, 0.25, -0.125},
                {-0.125, 0.25, 0.25, 0.25, -0.125},
                {-0.125, -0.125, -0.125, -0.125, -0.125}};

        double compRed = 0.0;
        double compGreen = 0.0;
        double compBlue = 0.0;

        for (int offsetX = -2; offsetX <= 2; offsetX++) {
          for (int offsetY = -2; offsetY <= 2; offsetY++) {
            if (i + offsetX >= 0 && i + offsetX < blurPixelsImage.getWidth() && j + offsetY >= 0
                && j + offsetY < blurPixelsImage.getHeight()) {
              compRed +=
                  sharpKernel[offsetX + 2][offsetY + 2] * blurPixelsImage
                      .getColorAt(j + offsetY, i + offsetX).getRed();
              compGreen +=
                  sharpKernel[offsetX + 2][offsetY + 2] * blurPixelsImage
                      .getColorAt(j + offsetY, i + offsetX).getGreen();
              compBlue +=
                  sharpKernel[offsetX + 2][offsetY + 2] * blurPixelsImage
                      .getColorAt(j + offsetY, i + offsetX).getBlue();
            }
          }
        }
        assertEquals(new CustomColor(Math.max(0, Math.min(255, (int) compRed)),
            Math.max(0, Math.min(255, (int) compGreen)),
            Math.max(0, Math.min(255, (int) compBlue))), sharpPix);
      }
    }
  }

  @Test
  public void testSepiaPlusGrey() {
    SepiaTransformation sepiaTrans = new SepiaTransformation();
    GreyTransformation greyTrans = new GreyTransformation();
    IImageModel sepiaTeddy = new ImageModelImpl(this.teddyModel).transform(sepiaTrans);
    IImageModel sgTeddy = new ImageModelImpl(sepiaTeddy).transform(greyTrans);
    CustomColor[][] sepiaPixels = new CustomColor[sepiaTeddy.getHeight()][sepiaTeddy.getWidth()];
    for (int i = 0; i < sepiaTeddy.getHeight(); i++) {
      for (int j = 0; j < sepiaTeddy.getWidth(); j++) {
        CustomColor pixelOriginal = teddyModel.getColorAt(j, i);
        CustomColor pixelSepia = new CustomColor(
            (int) Math.min(255, 0.393 * pixelOriginal.getRed() + 0.769 * pixelOriginal.getGreen()
                + 0.189 * pixelOriginal.getBlue()),
            (int) Math.min(255, 0.349 * pixelOriginal.getRed() + 0.686 * pixelOriginal.getGreen()
                + 0.168 * pixelOriginal.getBlue()),
            (int) Math.min(255, 0.272 * pixelOriginal.getRed() + 0.534 * pixelOriginal.getGreen()
                + 0.131 * pixelOriginal.getBlue()));
        assertEquals(pixelSepia.getRed(), sepiaTeddy.getColorAt(j, i).getRed());
        assertEquals(
            pixelSepia.getGreen(),
            sepiaTeddy.getColorAt(j, i).getGreen());
        assertEquals(
            pixelSepia.getBlue(),
            sepiaTeddy.getColorAt(j, i).getBlue());
        sepiaPixels[i][j] = pixelSepia;
      }
    }

    IImageModel sepiaPixelsImage = new ImageModelImpl(sepiaPixels, 255);

    for (int i = 0; i < sgTeddy.getHeight(); i++) {
      for (int j = 0; j < sgTeddy.getWidth(); j++) {
        CustomColor pixelOriginal = sepiaPixelsImage.getColorAt(j, i);
        assertEquals((int) (0.2126 * pixelOriginal.getRed() + 0.7152 * pixelOriginal.getGreen()
                + 0.0722 * pixelOriginal.getBlue()),
            sgTeddy.getColorAt(j, i).getRed());
      }
    }
  }

  @Test
  public void testBlurPlusGrey() {
    // Apply a blur filter + grey CT
    BlurTransformation blurTrans = new BlurTransformation();
    GreyTransformation greyTrans = new GreyTransformation();
    IImageModel blurTeddy = new ImageModelImpl(this.teddyModel).transform(blurTrans);
    IImageModel bgTeddy = new ImageModelImpl(blurTeddy).transform(greyTrans);

    CustomColor[][] blurPixels = new CustomColor[bgTeddy.getHeight()][bgTeddy.getWidth()];

    for (int i = 0; i < bgTeddy.getHeight(); i++) {
      for (int j = 0; j < bgTeddy.getWidth(); j++) {

        double[][] blurKernel = new double[][]{{0.0625, 0.125, 0.0625},
            {0.125, 0.25, 0.125},
            {0.0625, 0.125, 0.0625}};

        double compRed = 0.0;
        double compGreen = 0.0;
        double compBlue = 0.0;

        CustomColor blurPix = blurTeddy.getColorAt(j, i);

        for (int offsetX = -1; offsetX <= 1; offsetX++) {
          for (int offsetY = -1; offsetY <= 1; offsetY++) {
            if (i + offsetX >= 0 && i + offsetX < this.teddyModel.getWidth() && j + offsetY >= 0
                && j + offsetY < this.teddyModel.getHeight()) {
              compRed +=
                  blurKernel[offsetX + 1][offsetY + 1] * this.teddyModel
                      .getColorAt(j + offsetY, i + offsetX).getRed();
              compGreen +=
                  blurKernel[offsetX + 1][offsetY + 1] * this.teddyModel
                      .getColorAt(j + offsetY, i + offsetX).getGreen();
              compBlue +=
                  blurKernel[offsetX + 1][offsetY + 1] * this.teddyModel
                      .getColorAt(j + offsetY, i + offsetX).getBlue();
            }
          }
        }
        blurPixels[i][j] = new CustomColor((int) compRed, (int) compGreen, (int) compBlue);
        assertEquals(new CustomColor((int) compRed, (int) compGreen, (int) compBlue), blurPix);
      }
    }

    IImageModel blurPixelsImage = new ImageModelImpl(blurPixels, 255);

    for (int i = 0; i < bgTeddy.getHeight(); i++) {
      for (int j = 0; j < bgTeddy.getWidth(); j++) {
        CustomColor pixelOriginal = blurPixelsImage.getColorAt(j, i);
        assertEquals((int) (0.2126 * pixelOriginal.getRed() + 0.7152 * pixelOriginal.getGreen()
                + 0.0722 * pixelOriginal.getBlue()),
            bgTeddy.getColorAt(j, i).getRed());
      }
    }
  }

  @Test
  public void testSepiaPlusSharpen() {
    SepiaTransformation sepiaTrans = new SepiaTransformation();
    SharpTransformation sharpTrans = new SharpTransformation();
    IImageModel sepiaTeddy = new ImageModelImpl(this.teddyModel).transform(sepiaTrans);
    IImageModel ssTeddy = new ImageModelImpl(sepiaTeddy).transform(sharpTrans);
    CustomColor[][] sepiaPixels = new CustomColor[sepiaTeddy.getHeight()][sepiaTeddy.getWidth()];
    for (int i = 0; i < sepiaTeddy.getHeight(); i++) {
      for (int j = 0; j < sepiaTeddy.getWidth(); j++) {
        CustomColor pixelOriginal = teddyModel.getColorAt(j, i);
        CustomColor pixelSepia = new CustomColor(
            (int) Math.min(255, 0.393 * pixelOriginal.getRed() + 0.769 * pixelOriginal.getGreen()
                + 0.189 * pixelOriginal.getBlue()),
            (int) Math.min(255, 0.349 * pixelOriginal.getRed() + 0.686 * pixelOriginal.getGreen()
                + 0.168 * pixelOriginal.getBlue()),
            (int) Math.min(255, 0.272 * pixelOriginal.getRed() + 0.534 * pixelOriginal.getGreen()
                + 0.131 * pixelOriginal.getBlue()));
        assertEquals(pixelSepia.getRed(), sepiaTeddy.getColorAt(j, i).getRed());
        assertEquals(
            pixelSepia.getGreen(),
            sepiaTeddy.getColorAt(j, i).getGreen());
        assertEquals(
            pixelSepia.getBlue(),
            sepiaTeddy.getColorAt(j, i).getBlue());
        sepiaPixels[i][j] = pixelSepia;
      }
    }

    IImageModel sepiaPixelsImage = new ImageModelImpl(sepiaPixels, 255);

    for (int i = 0; i < ssTeddy.getHeight(); i++) {
      for (int j = 0; j < ssTeddy.getWidth(); j++) {

        CustomColor sharpPix = ssTeddy.getColorAt(j, i);

        double[][] sharpKernel =
            new double[][]{{-0.125, -0.125, -0.125, -0.125, -0.125},
                {-0.125, 0.25, 0.25, 0.25, -0.125},
                {-0.125, 0.25, 1, 0.25, -0.125},
                {-0.125, 0.25, 0.25, 0.25, -0.125},
                {-0.125, -0.125, -0.125, -0.125, -0.125}};

        double compRed = 0.0;
        double compGreen = 0.0;
        double compBlue = 0.0;

        for (int offsetX = -2; offsetX <= 2; offsetX++) {
          for (int offsetY = -2; offsetY <= 2; offsetY++) {
            if (i + offsetX >= 0 && i + offsetX < sepiaPixelsImage.getWidth() && j + offsetY >= 0
                && j + offsetY < sepiaPixelsImage.getHeight()) {
              compRed +=
                  sharpKernel[offsetX + 2][offsetY + 2] * sepiaPixelsImage
                      .getColorAt(j + offsetY, i + offsetX).getRed();
              compGreen +=
                  sharpKernel[offsetX + 2][offsetY + 2] * sepiaPixelsImage
                      .getColorAt(j + offsetY, i + offsetX).getGreen();
              compBlue +=
                  sharpKernel[offsetX + 2][offsetY + 2] * sepiaPixelsImage
                      .getColorAt(j + offsetY, i + offsetX).getBlue();
            }
          }
        }
        assertEquals(new CustomColor(Math.max(0, Math.min(255, (int) compRed)),
            Math.max(0, Math.min(255, (int) compGreen)),
            Math.max(0, Math.min(255, (int) compBlue))), sharpPix);
      }
    }
  }


  @Test(expected = IllegalArgumentException.class)
  public void testLoadNonExistantFile() {
    IImageModel img = new ImageModelImpl("non_existant_file.ppm", new PPMFormat());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullImage() {
    IImageModel img = new ImageModelImpl(null, 255);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidColorValue() {
    IImageModel img = new ImageModelImpl(this.validImage, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testOutOfRangeColorValue() {
    this.validImage[5][5] = new CustomColor(10, 10, 1000);
    IImageModel img = new ImageModelImpl(this.validImage, 255);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNonRectangularImage() {
    this.validImage[5] = new CustomColor[1];
    IImageModel img = new ImageModelImpl(this.validImage, 255);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullColumn() {
    this.validImage[5] = null;
    IImageModel img = new ImageModelImpl(this.validImage, 255);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEmptyImage() {
    IImageModel img = new ImageModelImpl(new CustomColor[0][0], 255);
  }
}
