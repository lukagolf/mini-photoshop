package model.layered;

import model.IImageModel;

/**
 * Represents a single layer in a {@link ILayerImageModel}. Layers contain an image, a transparency
 * (either transparent or opaque) and can be loaded ({@link LayerImpl}) or unloaded ({@link
 * EmptyLayer}).
 */
public interface ILayer {

  /**
   * Gets the transparency of this layer.
   *
   * @return whether this layer is transparent (otherwise it is opaque).
   */
  public boolean isTransparent();

  /**
   * Gets if the layer is loaded with an image.
   *
   * @return whether the layer is loaded.
   */
  public boolean isLoaded();

  /**
   * Gets the image associated with this layer.
   *
   * @return the image associated with this layer.
   * @throws UnsupportedOperationException if this layer is unloaded.
   */
  public IImageModel getImage() throws UnsupportedOperationException;

}
