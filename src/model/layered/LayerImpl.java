package model.layered;

import model.IImageModel;

/**
 * An implementation of {@link ILayer} representing a loaded layer with an {@link IImageModel}. This
 * class cannot have a null image. To represent empty layers, use {@link EmptyLayer}.
 */
public class LayerImpl implements ILayer {

  private final boolean transparent;
  private final IImageModel image;

  /**
   * Creates a new layer object with and image and a transparency setting. Layers are final and
   * cannot be modified.
   *
   * @param image       the image stored in this layer. This must not be null.
   * @param transparent whether this layer is transparent.
   * @throws IllegalArgumentException if the provided image is null.
   */
  public LayerImpl(IImageModel image, boolean transparent) throws IllegalArgumentException {
    if (image == null) {
      throw new IllegalArgumentException("The image must not be null!");
    }
    this.transparent = transparent;
    this.image = image;
  }

  @Override
  public boolean isTransparent() {
    return this.transparent;
  }

  @Override
  public boolean isLoaded() {
    return true;
  }

  @Override
  public IImageModel getImage() {
    return this.image;
  }

  @Override
  public String toString() {
    String res = "";

    res += "loaded (" + this.getImage().getWidth() + "x" + this.getImage().getHeight() + ")";
    if (this.isTransparent()) {
      res += ", transparent";
    } else {
      res += ", opaque";
    }
    return res;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    } else if (!(o instanceof LayerImpl)) {
      return false;
    } else {
      LayerImpl other = (LayerImpl) o;
      return this.isTransparent() == other.isTransparent() && this.isLoaded() == other.isLoaded()
          && this.getImage().equals(other.getImage());
    }
  }

  @Override
  public int hashCode() {
    int hashCode = this.getImage().hashCode();
    if (this.isTransparent()) {
      hashCode += 1;
    }
    return hashCode;
  }
}
