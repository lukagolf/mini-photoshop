package model.layered;

import java.util.List;
import model.IImageModel;

/**
 * Represents the additional operations of an image model that supports multiple layers. Can also be
 * used as a {@link IImageModel}, although the mapping of methods is up to the implementation. Part
 * of this model state is a pointer to the currently selected layer, to be used for operations like
 * loading images, applying transformations, etc.
 */
public interface ILayerImageModel extends IImageModel, IViewLayerImageModel {

  /**
   * Creates a new, transparent, empty layer and adds it to the top of image.
   */
  public void addLayer();

  /**
   * Loads an {@link IImageModel} into the currently selected layer. This can also be used to load
   * images from files if supported by the implementation of the model.
   *
   * @param image the image model to load into the currently selected layer.
   * @throws IllegalArgumentException if there are no layers.
   */
  public void loadImage(IImageModel image) throws IllegalArgumentException;

  /**
   * Deletes the layer at the current index and shifts the currently selected layer to the next
   * available layer.
   *
   * @param index the layer to delete.
   * @throws IllegalArgumentException if the specified index is invalid.
   */
  public void removeLayer(int index) throws IllegalArgumentException;

  /**
   * Creates an export of the state of this model in a specified directory.
   *
   * <p>Exports preserve all state information about a model, including unloaded and transparent
   * layers. Each layer is saved as a PNG image, along with an index image in the specified
   * directory.
   *
   * <p>This directory can then be imported to re-create the model exactly.
   *
   * @param dirname the directory to save the export to.
   * @throws IllegalArgumentException if the specified directory could not be written to.
   */
  public void exportAllLayers(String dirname) throws IllegalArgumentException;

  /**
   * Sets the currently selected layer.
   *
   * @param index the layer to set as the current layer.
   * @throws IllegalArgumentException if the specified index does not exist.
   */
  public void setCurrent(int index) throws IllegalArgumentException;

  /**
   * Sets the transparency of the specified layer.
   *
   * @param index       the layer to set the transparency of.
   * @param transparent the new transparency value (true = transparent; false = opaque)
   * @throws IllegalArgumentException if the specified index does not exist.
   */
  public void setLayerTransparency(int index, boolean transparent) throws IllegalArgumentException;

  /**
   * Moves the specified index to a specified destination. Moving a layer will push all subsequent
   * layers forward.
   *
   * @param prevIndex the index of the layer to move.
   * @param newIndex  the index to move the layer to.
   * @throws IllegalArgumentException if either index does not exist.
   */
  public void moveLayer(int prevIndex, int newIndex) throws IllegalArgumentException;

  /**
   * Sets the given layers to this list of layers by replacing all existing layers.
   *
   * @param layers that will replace all the existing layers.
   */
  public void setLayers(List<ILayer> layers);
}
