package controller.command;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import model.ImageModelImpl;
import model.ImageUtil;
import model.format.IImageFormatUtil;
import model.layered.ILayerImageModel;
import model.transformation.ITransformation;
import model.transformation.color.GreyTransformation;
import model.transformation.color.SepiaTransformation;
import model.transformation.filter.BlurTransformation;
import model.transformation.filter.SharpTransformation;
import view.IImageView;

/**
 * A convenience class that contains many of the commands that are supported by {@link
 * controller.MultiLayerImageController}. This class will provide the model and view to all enclosed
 * classes, which are individual {@link ICommand} classes.
 */
public class LayerImageCommands {

  private final ILayerImageModel model;
  private final IImageView view;

  /**
   * Instantiate this set of {@link ICommand}s to use the provided model and view.
   *
   * @param model the model to be used by this set of {@link ICommand}s.
   * @param view  the view to be used by this set of {@link ICommand}s.
   */
  public LayerImageCommands(ILayerImageModel model, IImageView view) {
    this.model = model;
    this.view = view;
  }

  /**
   * Convenience method that creates a new {@link Map} containing all the commands in this set. To
   * be used when creating a {@link controller.MultiLayerImageController}.
   *
   * @return the generated Map of all commands in this set.
   */
  public Map<String, ICommand> defaultCommands() {
    Map<String, ICommand> map = new HashMap<>();
    map.putIfAbsent("create", new CreateLayer());
    map.putIfAbsent("transform", new TransformImage());
    map.putIfAbsent("export", new ExportImage());
    map.putIfAbsent("exportall", new ExportAll());
    map.putIfAbsent("load", new LoadImage());
    map.putIfAbsent("remove", new RemoveLayer());
    map.putIfAbsent("current", new SetCurrent());
    map.putIfAbsent("transparent", new SetTransparent(true));
    map.putIfAbsent("opaque", new SetTransparent(false));
    map.putIfAbsent("move", new MoveLayer());
    return map;
  }

  /**
   * Creates a new, empty, transparent layer. This command takes no arguments.
   */
  public class CreateLayer implements ICommand {

    @Override
    public void apply(String[] args) throws IllegalArgumentException {
      model.addLayer();
    }
  }

  /**
   * Performs an image transformation (blur, sharpen, greyscale, sepia) on the current layer.
   *
   * <p>Requires one argument: the transformation type.
   */
  public class TransformImage implements ICommand {

    @Override
    public void apply(String[] args) throws IllegalArgumentException {
      if (args.length < 2) {
        throw new IllegalArgumentException("Must specify transformation type!");
      }

      ITransformation transformation;

      switch (args[1].toLowerCase(Locale.ROOT)) {
        case "blur":
          transformation = new BlurTransformation();
          break;
        case "sharpen":
          transformation = new SharpTransformation();
          break;
        case "greyscale":
          transformation = new GreyTransformation();
          break;
        case "sepia":
          transformation = new SepiaTransformation();
          break;
        default:
          throw new IllegalArgumentException(args[1]
              + " is not a valid transformation! Must be one of: blur, sharpen, greyscale, sepia");
      }
      model.transform(transformation);
      view.renderMessage(
          "Applied " + args[1].toLowerCase(Locale.ROOT) + " transformation to layer " + (model
              .getCurrent() + 1) + ".\n");
    }
  }

  /**
   * Exports the image to a file. The image exported is the first non-transparent, non-empty layer.
   * The format is specified by the user, or otherwise is extracted from the extension of the
   * provided filename.
   *
   * <p>Requires one argument: the filename to export to.
   *
   * <p>Optional argument: the format to export as.
   */
  public class ExportImage implements ICommand {

    @Override
    public void apply(String[] args) throws IllegalArgumentException {
      if (args.length < 2 || args[1].length() == 0) {
        throw new IllegalArgumentException("Must specify filename!");
      }
      String[] fileName = args[1].split("\\.");
      String formatString;
      // User-specified format
      if (args.length >= 3) {
        formatString = args[2].toLowerCase(Locale.ROOT);
      } else {
        // format from file extension
        formatString = fileName[fileName.length - 1].toLowerCase(Locale.ROOT);
      }
      IImageFormatUtil format = ImageUtil.formatFromString(formatString);
      model.exportToFile(args[1], format);
      view.renderMessage(
          "Exported image to " + args[1] + " using " + formatString + " format.\n");
    }
  }

  /**
   * Exports the {@link ILayerImageModel} to a directory containing an index file and a PNG for each
   * layer.
   *
   * <p>Exports preserve the entire state of an {@link ILayerImageModel} including empty and
   * transparent layers, and can be loaded using the --from/-f argument when running this
   * application.
   *
   * <p>Requires one argument: the location to create the export directory.
   */
  public class ExportAll implements ICommand {

    @Override
    public void apply(String[] args) throws IllegalArgumentException {
      if (args.length < 2 || args[1].length() == 0) {
        throw new IllegalArgumentException("Must specify export location!");
      }
      model.exportAllLayers(args[1]);
      view.renderMessage("Exported all layers to " + args[1] + ".\n");
    }
  }

  /**
   * Loads an image file into the currently selected layer. Images can be loaded onto any layer,
   * empty or non-empty, but will set the layer to opaque by default.
   *
   * <p>The format of the image is specified by the user or determined using the file extension.
   *
   * <p>Requires one argument: the location of the image.
   *
   * <p>Optional argument: the format of the image to import.
   */
  public class LoadImage implements ICommand {

    @Override
    public void apply(String[] args) throws IllegalArgumentException {
      if (args.length < 2 || args[1].length() == 0) {
        throw new IllegalArgumentException("Must specify file to import!");
      }
      String[] fileName = args[1].split("\\.");
      String formatString;
      // User-specified format
      if (args.length >= 3) {
        formatString = args[2].toLowerCase(Locale.ROOT);
      } else {
        // format from file extension
        formatString = fileName[fileName.length - 1].toLowerCase(Locale.ROOT);
      }
      IImageFormatUtil format = ImageUtil.formatFromString(formatString);
      model.loadImage(new ImageModelImpl(args[1], format));
      view.renderMessage(
          "Loaded image " + args[1] + " as format " + formatString + " to layer " + (
              model.getCurrent() + 1) + ".\n");
    }
  }

  /**
   * Removes the specified layer from the {@link ILayerImageModel} and sets the current layer to the
   * next available layer.
   *
   * <p>Requires one argument: the index of the layer to remove (starting from 1).
   */
  public class RemoveLayer implements ICommand {

    @Override
    public void apply(String[] args) throws IllegalArgumentException {
      if (args.length < 2 || args[1].length() == 0) {
        throw new IllegalArgumentException("Must specify index to remove!");
      }

      try {
        int index = Integer.parseInt(args[1]) - 1;
        model.removeLayer(index);
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException("Invalid index " + args[1]);
      }
    }
  }

  /**
   * Sets the current layer to the specified index. The index must refer to a valid layer.
   *
   * <p>Requires one argument: the index of the selected layer (starting from 1).
   */
  public class SetCurrent implements ICommand {

    @Override
    public void apply(String[] args) throws IllegalArgumentException {
      if (args.length < 2 || args[1].length() == 0) {
        throw new IllegalArgumentException("Must specify index to set as current layer!");
      }

      try {
        int index = Integer.parseInt(args[1]) - 1;
        model.setCurrent(index);
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException("Invalid index " + args[1]);
      }
    }
  }

  /**
   * Sets the transparency of the layer at the specified index. This class must be instantiated with
   * the transparency value to set.
   *
   * <p>Requires one argument: the index of the layer to set the transparency of (starting from 1).
   */
  public class SetTransparent implements ICommand {

    private final boolean transparent;

    /**
     * Instantiate this command to either set layers as transparent or opaque.
     *
     * @param transparent whether to set layers as transparent.
     */
    public SetTransparent(boolean transparent) {
      this.transparent = transparent;
    }

    @Override
    public void apply(String[] args) throws IllegalArgumentException {
      if (args.length < 2 || args[1].length() == 0) {
        throw new IllegalArgumentException("Must specify index to set transparency!");
      }

      try {
        int index = Integer.parseInt(args[1]) - 1;
        model.setLayerTransparency(index, this.transparent);
        view.renderMessage("Set layer " + args[1] + " as ");
        if (this.transparent) {
          view.renderMessage("transparent.\n");
        } else {
          view.renderMessage("opaque.\n");
        }
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException("Invalid index " + args[1]);
      }
    }
  }

  /**
   * Moves the layer specified by a given index to another specified index. Both indices must be
   * valid.
   *
   * <p>Requires two arguments: the index to move (starting from 1) and the destination (starting
   * from 1)
   */
  public class MoveLayer implements ICommand {

    @Override
    public void apply(String[] args) throws IllegalArgumentException {
      if (args.length < 3 || args[1].length() == 0 || args[2].length() == 0) {
        throw new IllegalArgumentException("Must specify index from and index to!");
      }

      try {
        int from = Integer.parseInt(args[1]) - 1;
        int to = Integer.parseInt(args[2]) - 1;
        model.moveLayer(from, to);
        view.renderMessage("Moved layer " + args[1] + " to position " + args[2] + ".");
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException("Invalid index provided!");
      }
    }
  }
}
