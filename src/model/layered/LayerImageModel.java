package model.layered;

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import model.CustomColor;
import model.IImageModel;
import model.IViewImageModel;
import model.ImageModelImpl;
import model.format.IImageFormatUtil;
import model.format.PNGFormat;
import model.transformation.ITransformation;

/**
 * An implementation of a model that supports multiple layers. This model can also be used as a
 * {@link IImageModel} or {@link IViewImageModel}. The currently selected layer will be used to
 * perform operations from those interfaces. All other operations require an index representing the
 * layer to affect.
 *
 * <p>All layers in this model must be of the same size.
 *
 * <p>Exporting this model as an image will export the first visible (i.e. non-transparent, loaded)
 * layer.
 */
public class LayerImageModel implements ILayerImageModel {

  // determines which image format to use for the internal representation of multi-layer images
  private static final IImageFormatUtil MULTI_LAYER_FORMAT = new PNGFormat();
  private static final String MULTI_LAYER_FORMAT_EXT = "png";

  private List<ILayer> layers;
  private int current;

  /**
   * Constructor that creates an instance of this model from a provided list of layers. This
   * constructor is also used by other convenience constructors.
   *
   * @param layers the layers to instantiate this class with.
   */
  public LayerImageModel(List<ILayer> layers) {
    this.layers = layers;
  }

  /**
   * Convenience constructor to create an instance of this model with no layers. The current pointer
   * will be set to 0.
   */
  public LayerImageModel() {
    this(new ArrayList<>());
  }


  /**
   * Convenience constructor to clone another {@link LayerImageModel}. This constructor will create
   * a deep copy of the provided model.
   *
   * @param model the model to clone from.
   */
  public LayerImageModel(LayerImageModel model) {
    this(model.getLayers());
  }

  /**
   * Constructor to create this model from a given directory, previously exported using the
   * exportAllLayers() method.
   *
   * <p>Exports preserve all state information about a model, including unloaded and transparent
   * layers. Each layer is saved as a PNG image, along with an index image in the specified
   * directory.
   *
   * @param dirname the directory to load the export from.
   * @throws IllegalArgumentException if the directory contents cannot be read.
   */
  public LayerImageModel(String dirname) throws IllegalArgumentException {
    Scanner sc;
    try {
      sc = new Scanner(new FileInputStream(dirname + "/layers.txt"));
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException(
          "The provided file " + dirname + "/layers.txt doesn't exist!");
    }
    List<ILayer> multiLayers = new ArrayList<>();
    while (sc.hasNextLine()) {
      String[] s = sc.nextLine().split("\t");
      if (s.length < 1) {
        throw new IllegalArgumentException("Invalid syntax: each line must contain a layer!");
      }
      if (s[0].equals("e")) {
        multiLayers.add(new EmptyLayer());
        if (s.length != 1) {
          throw new IllegalArgumentException(
              "Invalid syntax: there should not be parameters for empty layers!");
        }
      } else {
        if (s.length != 2 || s[0].length() != 1) {
          throw new IllegalArgumentException(
              "Invalid syntax: layers should have a transparency setting and file name!");
        }
        boolean transparent = s[0].equals("t");
        multiLayers.add(
            new LayerImpl(new ImageModelImpl(dirname + "/" + s[1], MULTI_LAYER_FORMAT),
                transparent));
      }
    }
    this.layers = multiLayers;
  }

  @Override
  public void setLayers(List<ILayer> layers) {
    this.layers = layers;
  }

  /**
   * Helper method to get the image from the currently selected layer.
   *
   * @return the image stored in the currently selected layer.
   * @throws IllegalArgumentException if the currently selected layer does not exist (no layers) or
   *                                  is empty.
   */
  private IImageModel getCurrentImage() throws IllegalArgumentException {
    if (this.layers == null) {
      throw new IllegalArgumentException("Layers are null");
    }

    try {
      if (this.layers.get(this.current) != null) {
        return this.layers.get(this.current).getImage();
      }
    } catch (IndexOutOfBoundsException e) {
      throw new IllegalArgumentException("There is no layer at index " + this.current + "!");
    } catch (UnsupportedOperationException e) {
      throw new IllegalArgumentException("The layer at " + this.current + " is empty!");
    }
    return null;
  }

  @Override
  public IImageModel transform(ITransformation transformation) throws IllegalArgumentException {
    return this.getCurrentImage().transform(transformation);
  }

  private IImageModel getFirstVisibleImage() throws IllegalArgumentException {
    for (ILayer layer : this.layers) {
      if (!layer.isTransparent() && layer.isLoaded()) {
        return layer.getImage();
      }
    }
    throw new IllegalArgumentException("All layers are either transparent or empty!");
  }

  @Override
  public void exportToFile(String filename, IImageFormatUtil format)
      throws IllegalArgumentException {
    this.getFirstVisibleImage().exportToFile(filename, format);
  }

  @Override
  public void exportAllLayers(String dirname)
      throws IllegalArgumentException {
    try {
      new File(dirname).mkdirs();
      FileWriter indexFile = new FileWriter(dirname + "/layers.txt");
      for (int i = 0; i < layers.size(); i++) {
        if (layers.get(i).isLoaded()) {
          String filename = i + "." + MULTI_LAYER_FORMAT_EXT;
          layers.get(i).getImage().exportToFile(dirname + "/" + filename, MULTI_LAYER_FORMAT);
          if (layers.get(i).isTransparent()) {
            indexFile.write("t");
          } else {
            indexFile.write("o");
          }
          indexFile.write("\t" + filename);
        } else {
          indexFile.write("e");
        }
        indexFile.write("\n");
      }
      indexFile.close();
    } catch (IOException e) {
      throw new IllegalArgumentException("Failed to export image! (" + e.getMessage() + ")");
    }
  }

  @Override
  public CustomColor getColorAt(int x, int y) throws IllegalArgumentException {

    return this.getCurrentImage().getColorAt(x, y);
  }

  @Override
  public int getWidth() throws IllegalArgumentException {
    return this.getCurrentImage().getWidth();
  }

  @Override
  public int getHeight() throws IllegalArgumentException {
    return this.getCurrentImage().getHeight();
  }

  @Override
  public int minColorValue() throws IllegalArgumentException {
    return this.getCurrentImage().minColorValue();
  }

  @Override
  public int maxColorValue() throws IllegalArgumentException {
    return this.getCurrentImage().maxColorValue();
  }

  @Override
  public CustomColor[][] asArray() throws IllegalArgumentException {
    return this.getCurrentImage().asArray();
  }

  @Override
  public boolean almostEquals(Object o) {
    return this.getCurrentImage().almostEquals(o);
  }

  @Override
  public Image toImage() throws IllegalArgumentException {
    return this.getFirstVisibleImage().toImage();
  }

  @Override
  public void addLayer() throws IllegalArgumentException {
    if (this.layers == null) {
      throw new IllegalArgumentException("The layers are null.");
    }
    this.layers.add(0, new EmptyLayer());
  }

  @Override
  public void loadImage(IImageModel image) {
    IViewImageModel prevImage = null;
    for (ILayer img : this.layers) {
      if (img.isLoaded()) {
        prevImage = img.getImage();
        break;
      }
    }
    if (prevImage != null) {
      if (prevImage.getHeight() != image.getHeight() || prevImage.getWidth() != image.getWidth()) {
        throw new IllegalArgumentException("The image does not have the proper dimensions.");
      }
    }
    try {
      this.layers
          .set(this.current, new LayerImpl(image, false));
    } catch (IndexOutOfBoundsException e) {
      throw new IllegalArgumentException("The layer at index " + this.current + " does not exist!");
    }
  }

  @Override
  public void removeLayer(int index) throws IllegalArgumentException {
    if (this.layers == null) {
      throw new IllegalArgumentException("The layers are null.");
    }
    if (this.layers.size() <= index) {
      throw new IllegalArgumentException("The layer at index " + index + " does not exist!");
    }
    this.layers.remove(index);
    if (this.current != 0 && this.current >= index) {
      this.current--;
    }
  }

  @Override
  public List<ILayer> getLayers() {
    List<ILayer> deepCopy = new ArrayList<>();
    for (ILayer layer : this.layers) {
      if (layer.isLoaded()) {
        deepCopy.add(new LayerImpl(new ImageModelImpl(layer.getImage()), layer.isTransparent()));
      } else {
        deepCopy.add(new EmptyLayer());
      }
    }
    return deepCopy;
  }

  @Override
  public void setCurrent(int index) throws IllegalArgumentException {
    if (this.layers.size() <= index || index < 0) {
      throw new IllegalArgumentException("The layer at index " + index + " does not exist!");
    }
    this.current = index;
  }

  @Override
  public int getCurrent() {
    return this.current;
  }

  @Override
  public void setLayerTransparency(int index, boolean transparent) throws IllegalArgumentException {
    try {
      this.layers.set(index, new LayerImpl(this.layers.get(index).getImage(), transparent));
    } catch (NullPointerException e) {
      throw new IllegalArgumentException("The layers are null.");
    } catch (IndexOutOfBoundsException e) {
      throw new IllegalArgumentException("The layer at index " + index + " does not exist!");
    } catch (UnsupportedOperationException e) {
      throw new IllegalArgumentException(
          "The layer at index " + index + " does not have an image loaded!");
    }
  }

  @Override
  public void moveLayer(int prevIndex, int newIndex) throws IllegalArgumentException {
    try {
      this.layers.add(newIndex, this.layers.remove(prevIndex));
    } catch (IndexOutOfBoundsException e) {
      throw new IllegalArgumentException(
          "Could not move layer since either previous index or new index was out of bounds.");
    }
  }

}
