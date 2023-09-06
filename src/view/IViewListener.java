package view;

/**
 * Interface representing the event handlers for an {@link IImageEventView}. Implementing this
 * interface will allow controllers to receive events by passing themselves as the event listener.
 */
public interface IViewListener {

  /**
   * Handle a user save event. Triggered through the File->Save menu. Supports both model and image
   * save operations.
   *
   * @param type     the type of image to save. Possible values are 'png', 'jpg', 'ppm', and 'model'
   *                 (exports the model to a directory).
   * @param location the file location selected by the user.
   */
  void handleSaveEvent(String type, String location);

  /**
   * Handle a user transform event. Triggered through the Transform menu.
   *
   * @param type the type of transformation to perform on the current layer. Possible types 'blur',
   *             'sharpen', 'greyscale', 'sepia', 'downscale', 'mosaic'.
   * @throws IllegalStateException if the transformation fails.
   */
  void handleTransformEvent(String type) throws IllegalStateException;

  /**
   * Handle loading a user selected script event. Triggered through the Scripting menu.
   *
   * @param location the location of the script to load.
   */
  void handleScriptingEvent(String location);

  /**
   * Handle an import event for a user-selected image or model directory.
   *
   * @param type     the type of image to save. Possible values are 'png', 'jpg', 'ppm', and 'model'
   *                 (loads the model from directory).
   * @param location the location to the file or directory to import.
   */
  void handleImportEvent(String type, String location);

  /**
   * Handle a layer event to add or remove the currently selected layer.
   *
   * @param add whether to add (true) or remove (false) a layer.
   */
  void handleAddSubEvent(boolean add);

  /**
   * Handle a layer event to toggle the visibility (transparency) of the currently selected layer.
   */
  void handleToggleEvent();

  /**
   * Handle a layer event to move the currently selected layer up or down.
   *
   * @param up whether to move the layer up (true) or down (false) one position.
   */
  void handleMoveEvent(boolean up);

  /**
   * Handle a layer event to set the pointer to the current layer.
   *
   * @param index the index to set the current layer to.
   */
  void handleSetCurrentEvent(int index);

  /**
   * Handle a downscale transformation event.
   *
   * @param x the new width to scale to.
   * @param y the new height to scale to.
   */
  void handleDownscaleEvent(int x, int y);

  /**
   * Handle a mosaic transformation event.
   *
   * @param seeds the number of seed clusters to create.
   */
  void handleMosaicEvent(int seeds);
}
