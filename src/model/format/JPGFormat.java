package model.format;

/**
 * Represents JPG image format. Extends from the {@link ImageIOFormat} class to provide the JPG
 * format string so {@link javax.imageio.ImageIO} knows what format to import as.
 */
public class JPGFormat extends ImageIOFormat {

  @Override
  protected String getFormatString() {
    return "jpg";
  }
}
