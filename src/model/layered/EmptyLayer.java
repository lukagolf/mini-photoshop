package model.layered;

import model.IImageModel;

/**
 * Represents an empty {@link ILayer}. To be used before any image is loaded into a layer. Empty
 * layers have no state/fields and are always transparent. Trying to get the image from an empty
 * layer will throw an {@link UnsupportedOperationException}.
 */
public class EmptyLayer implements ILayer {

  @Override
  public boolean isTransparent() {
    return true;
  }

  @Override
  public boolean isLoaded() {
    return false;
  }

  @Override
  public IImageModel getImage() throws UnsupportedOperationException {
    throw new UnsupportedOperationException("This layer does not contain an image!");
  }

  @Override
  public String toString() {
    return "empty";
  }

  @Override
  public boolean equals(Object o) {
    return o instanceof EmptyLayer; // all empty layers are equivalent
  }

  @Override
  public int hashCode() {
    return 1;
  }
}
