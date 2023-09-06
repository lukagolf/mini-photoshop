package model.format;

/**
 * Represents PNG image format. Extends from the {@link ImageIOFormat} class to provide the PNG
 * format string so {@link javax.imageio.ImageIO} knows what format to import as.
 */
public class PNGFormat extends ImageIOFormat {

  @Override
  protected String getFormatString() {
    return "png";
  }
}
