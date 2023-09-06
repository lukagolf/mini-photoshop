import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import controller.MultiLayerImageController;
import controller.command.ICommand;
import controller.command.LayerImageCommands;
import java.io.StringReader;
import java.util.Map;
import model.ImageModelImpl;
import model.format.PNGFormat;
import model.layered.ILayerImageModel;
import model.layered.LayerImageModel;
import model.transformation.color.GreyTransformation;
import org.junit.Before;
import org.junit.Test;
import view.IImageView;
import view.MultiLayerImageView;

/**
 * Represents a class that tests each of the batch commands individually.
 */
public class BatchCommandTest {

  ILayerImageModel model;
  IImageView view;
  Appendable ap;
  Map<String, ICommand> layerImageCommands;
  String[] args;


  @Before
  public void initialize() {
    ap = new StringBuilder();
    model = new LayerImageModel();
    view = new MultiLayerImageView(model, ap, false);
    layerImageCommands = new LayerImageCommands(model, view).defaultCommands();
  }

  @Test
  public void testWrongCommand() {
    new MultiLayerImageController(layerImageCommands, model, view,
        new StringReader("test")).startApp();
    assertTrue(ap.toString().contains("The specified command 'test' does not exist."));
  }

  @Test
  public void testCreate() {
    args = new String[1];
    args[0] = "create";
    ICommand command = layerImageCommands.get("create");
    command.apply(args);
    assertEquals(1, model.getLayers().size());
  }

  @Test
  public void testTransform() {
    args = new String[2];
    args[0] = "transform";
    args[1] = "greyscale";
    ICommand command = layerImageCommands.get("transform");

    model.addLayer();
    model.setCurrent(0);
    model.loadImage(new ImageModelImpl("res/Teddy.png", new PNGFormat()));

    command.apply(args);
    assertEquals(1, model.getLayers().size());

    ImageModelImpl img = new ImageModelImpl("res/Teddy.png", new PNGFormat());
    img.transform(new GreyTransformation());
    assertTrue(img.almostEquals(model.getLayers().get(0).getImage()));
  }

  @Test
  public void testExportLoad() {
    args = new String[3];
    args[0] = "export";
    args[1] = "res/test.png";
    args[2] = "PNG";
    ICommand command = layerImageCommands.get("export");
    model.addLayer();
    model.setCurrent(0);
    ImageModelImpl img = new ImageModelImpl("res/Teddy.png", new PNGFormat());
    model.loadImage(img);

    command.apply(args);
    model.addLayer();
    model.setCurrent(0);

    args = new String[2];
    args[0] = "load";
    args[1] = "res/test.png";
    command = layerImageCommands.get("load");
    command.apply(args);
    assertTrue(img.almostEquals(model.getLayers().get(1).getImage()));
  }

  @Test
  public void testExportAll() {
    args = new String[2];
    args[0] = "exportall";
    args[1] = "res/ExportTest";
    ICommand command = layerImageCommands.get("exportall");
    model.addLayer();
    model.setCurrent(0);
    ImageModelImpl img = new ImageModelImpl("res/Teddy.png", new PNGFormat());
    model.loadImage(img);
    command.apply(args);

    model.addLayer();
    model.setCurrent(0);

    args = new String[2];
    args[0] = "load";
    args[1] = "res/ExportTest/0.png";
    command = layerImageCommands.get("load");
    command.apply(args);
    assertTrue(img.almostEquals(model.getLayers().get(1).getImage()));
  }

  @Test
  public void testRemove() {
    args = new String[2];
    args[0] = "remove";
    args[1] = "1";
    ICommand command = layerImageCommands.get("remove");

    model.addLayer();
    model.setCurrent(0);
    model.loadImage(new ImageModelImpl("res/Teddy.png", new PNGFormat()));

    command.apply(args);
    assertEquals(0, model.getLayers().size());
  }

  @Test
  public void testCurrent() {
    args = new String[2];
    args[0] = "current";
    args[1] = "2";
    ICommand command = layerImageCommands.get("current");

    model.addLayer();
    model.addLayer();
    model.setCurrent(0);
    command.apply(args);
    assertEquals(1, model.getCurrent());
  }

  @Test
  public void testTransparent() {
    args = new String[2];
    args[0] = "transparent";
    args[1] = "1";
    ICommand command = layerImageCommands.get("transparent");

    model.addLayer();
    model.setCurrent(0);
    model.loadImage(new ImageModelImpl("res/Teddy.png", new PNGFormat()));
    model.setLayerTransparency(0, false);
    command.apply(args);
    assertTrue(model.getLayers().get(0).isTransparent());
  }

  @Test
  public void testOpaque() {
    args = new String[2];
    args[0] = "opaque";
    args[1] = "1";
    ICommand command = layerImageCommands.get("opaque");

    model.addLayer();
    model.setCurrent(0);
    model.loadImage(new ImageModelImpl("res/Teddy.png", new PNGFormat()));
    model.setLayerTransparency(0, true);
    command.apply(args);
    assertTrue(!model.getLayers().get(0).isTransparent());
  }

  @Test
  public void testMove() {
    args = new String[3];
    args[0] = "move";
    args[1] = "2";
    args[2] = "1";
    ICommand command = layerImageCommands.get("move");

    model.addLayer();
    model.setCurrent(0);
    model.loadImage(new ImageModelImpl("res/Teddy.png", new PNGFormat()));
    model.addLayer();
    assertTrue(!model.getLayers().get(0).isLoaded());
    assertTrue(model.getLayers().get(1).isLoaded());
    command.apply(args);
    assertTrue(model.getLayers().get(0).isLoaded());
    assertTrue(!model.getLayers().get(1).isLoaded());
  }
}
