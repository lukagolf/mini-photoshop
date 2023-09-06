package controller;

import controller.command.LayerImageCommands;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import model.ImageModelImpl;
import model.ImageUtil;
import model.layered.ILayerImageModel;
import model.layered.LayerImageModel;
import model.transformation.DownscaleTransformation;
import model.transformation.MosaicTransformation;
import model.transformation.color.GreyTransformation;
import model.transformation.color.SepiaTransformation;
import model.transformation.filter.BlurTransformation;
import model.transformation.filter.SharpTransformation;
import view.FrameView;
import view.IImageEventView;
import view.IImageView;
import view.IViewListener;
import view.MultiLayerImageView;

/**
 * An implementation of {@link IImageController} which must be used with an {@link
 * ILayerImageModel}. This controller reads the user input from the {@link IImageEventView} object
 * and calls the corresponding action expected from the model using event handlers.
 */
public class FrameController implements IImageController, IViewListener {

  private IImageEventView view;

  /**
   * Starts the controller using the terminal for input (System.in) and output (System.out). By
   * specifying optional commandline arguments, this program can also read saves and execute from
   * scripts rather than System.in (see below).
   *
   * @param args optional arguments to direct this program to load a {@link ILayerImageModel} from a
   *             saved directory (using --from/-f) and to read commands from a script rather than
   *             from user input (using --script/-s).
   */
  public static void main(String[] args) {
    List<String> argList = Arrays.asList(args);

    // Headless run modes
    if (argList.contains("-script")) {
      MultiLayerImageController.main(args);
      return;
    } else if (argList.contains("-text")) {
      MultiLayerImageController.main(argList.toArray(new String[argList.size()]));
      return;
    } else if (argList.contains("-interactive")) {

      ILayerImageModel model = new LayerImageModel(new ArrayList<>());
      IImageEventView view = new FrameView(model);
      FrameController controller = new FrameController(model, view);

      controller.startApp();
    } else {
      System.err.println(
          "No run valid run mode specified. Valid options are '-script', '-text', '-interactive'.");
    }
  }

  private ILayerImageModel model;

  /**
   * Creates a new controller with the specified model, and view. The model must support layer
   * operations through the {@link ILayerImageModel} interface and the view must support registering
   * events through the {@link IImageEventView} interface. All arguments must be non-null. To start
   * the program once instantiated, call startApp().
   *
   * @param model the model to use for creating a controller.
   * @param view  the view to send output to.
   * @throws IllegalArgumentException if any of the arguments are null.
   */
  public FrameController(ILayerImageModel model, IImageEventView view)
      throws IllegalArgumentException {
    if (model == null || view == null) {
      throw new IllegalArgumentException("One/Both arguments are null!");
    }
    this.model = model;
    this.view = view;
  }

  @Override
  public void startApp() throws IllegalStateException {
    this.view.registerViewEventListener(this);
    this.view.renderApp();
  }

  @Override
  public void handleSaveEvent(String type, String location) {
    try {
      if (type.equals("model")) {
        this.model.exportAllLayers(location);
      } else {
        this.model.exportToFile(location, ImageUtil.formatFromString(type));
      }
    } catch (IllegalArgumentException e) {
      this.view.renderMessage("Failed to export: " + e.getMessage());
    }
  }

  @Override
  public void handleTransformEvent(String type) throws IllegalStateException {
    try {
      switch (type) {
        case "blur":
          this.model.transform(new BlurTransformation());
          break;
        case "sharpen":
          this.model.transform(new SharpTransformation());
          break;
        case "sepia":
          this.model.transform(new SepiaTransformation());
          break;
        case "greyscale":
          this.model.transform(new GreyTransformation());
          break;
        default:
          throw new IllegalStateException("Invalid transformation!");
      }
    } catch (IllegalArgumentException e) {
      this.view.renderMessage("Failed to perform transformation: " + e.getMessage());
    }
    this.view.renderApp();
  }

  @Override
  public void handleScriptingEvent(String location) {
    try {
      StringBuilder scriptOutput = new StringBuilder();
      IImageView scriptView = new MultiLayerImageView(this.model, scriptOutput, true);
      ScriptUtil.executeScript(scriptView,
          new LayerImageCommands(this.model, scriptView).defaultCommands(),
          new Scanner(new File(location)));
      this.view.renderApp();
      this.view.renderMessage("Script output:\n" + scriptOutput.toString());
    } catch (FileNotFoundException e) {
      this.view.renderMessage("Could not find the requested file '" + location + "'.");
    }
  }

  @Override
  public void handleImportEvent(String type, String location) {
    if (type.equals("model")) {
      try {
        this.model.setLayers(new LayerImageModel(location).getLayers());
      } catch (IllegalArgumentException e) {
        this.view.renderMessage("Failed to import model: " + e.getMessage());
      }
    } else {
      try {
        this.model.loadImage(new ImageModelImpl(location, ImageUtil.formatFromString(type)));
      } catch (IllegalArgumentException e) {
        this.view.renderMessage("Failed to load image: " + e.getMessage());
      }
    }
    this.view.renderApp();
  }

  @Override
  public void handleAddSubEvent(boolean add) {
    if (add) {
      this.model.addLayer();
    } else {
      try {
        this.model.removeLayer(this.model.getCurrent());
      } catch (IllegalArgumentException e) {
        this.view.renderMessage("Failed to remove layer: " + e.getMessage());
      }
    }
    this.view.renderApp();
  }

  @Override
  public void handleToggleEvent() {
    try {
      this.model.setLayerTransparency(this.model.getCurrent(),
          !this.model.getLayers().get(this.model.getCurrent()).isTransparent());
    } catch (IllegalArgumentException e) {
      this.view.renderMessage("Failed to toggle transparency: " + e.getMessage());
    }
    this.view.renderApp();
  }

  @Override
  public void handleMoveEvent(boolean up) {
    try {
      if (up && this.model.getCurrent() > 0) {
        this.model.moveLayer(model.getCurrent(), model.getCurrent() - 1);
      } else if (!up) {
        this.model.moveLayer(model.getCurrent(), model.getCurrent() + 1);
      }
    } catch (IllegalArgumentException e) {
      this.view.renderMessage("Failed to move layer: " + e.getMessage());
    }
    this.view.renderApp();
  }

  @Override
  public void handleSetCurrentEvent(int index) {
    try {
      this.model.setCurrent(index);
    } catch (IllegalArgumentException e) {
      this.view.renderMessage("Failed to set current layer: " + e.getMessage());
    }
    this.view.renderApp();
  }

  @Override
  public void handleDownscaleEvent(int x, int y) {
    try {
      if (x > this.model.getWidth() || y > this.model.getHeight()) {
        this.view.renderMessage(
            "New dimensions must be smaller than current dimensions (" + this.model.getWidth()
                + "x" + this.model.getHeight() + ")");
        return;
      }
      if (x <= 0 || y <= 0) {
        this.view.renderMessage("New dimensions must be at least 1x1");
        return;
      }

      // Apply downscale to all layers
      int oldCurrent = this.model.getCurrent();
      for (int i = 0; i < this.model.getLayers().size(); i++) {
        this.model.setCurrent(i);
        this.model.transform(new DownscaleTransformation(x, y));
      }
      this.model.setCurrent(oldCurrent);
      this.view.renderApp();
    } catch (IllegalArgumentException e) {
      this.view.renderMessage("Failed to perform transformation: " + e.getMessage());
    }
  }

  @Override
  public void handleMosaicEvent(int seeds) {
    try {
      if (seeds > this.model.getWidth() * this.model.getHeight()) {
        this.view.renderMessage(
            "There must be fewer than " + this.model.getWidth() * this.model.getHeight()
                + " seeds");
        return;
      }
      if (seeds <= 0) {
        this.view.renderMessage("Must be at least 1 seed.");
        return;
      }
      this.model.transform(new MosaicTransformation(seeds));
      this.view.renderApp();
    } catch (IllegalArgumentException e) {
      this.view.renderMessage("Failed to perform transformation: " + e.getMessage());
    }
  }
}
