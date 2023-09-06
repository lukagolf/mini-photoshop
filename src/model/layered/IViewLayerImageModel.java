package model.layered;

import java.util.List;
import model.IViewImageModel;

/**
 * The view-only, non-mutating methods supported by a layered image model. This interface also
 * supports all the read-only methods of a standard image model, and therefore is also
 * backwards-compatible with users of that interface.
 */
public interface IViewLayerImageModel extends IViewImageModel {

  /**
   * Gets a list of all the layers in this model.
   *
   * @return a cloned (deep-copy) of all layers in this model.
   */
  public List<ILayer> getLayers();

  /**
   * Gets the index of the currently selected layer.
   *
   * @return the index of the currently selected layer. Note that if there are no layers, this
   *         method will return 0.
   */
  public int getCurrent();
}
