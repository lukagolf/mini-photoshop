import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import controller.MultiLayerImageController;
import controller.command.ICommand;
import controller.command.LayerImageCommands;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;
import model.ImageModelImpl;
import model.format.PNGFormat;
import model.layered.ILayerImageModel;
import model.layered.LayerImageModel;
import org.junit.Before;
import org.junit.Test;
import view.IImageView;
import view.MultiLayerImageView;

/**
 * Represents a class that tests wheter we are able to run the scripts created and get the desirable
 * effect.
 */
public class ScriptTest {

  ILayerImageModel model;
  IImageView view;
  Appendable ap;
  Readable rd;
  Map<String, ICommand> layerImageCommands;

  @Before
  public void initialize() {
    ap = new StringBuilder();
    model = new LayerImageModel();
    view = new MultiLayerImageView(model, ap, true);
    layerImageCommands = new LayerImageCommands(model, view).defaultCommands();
  }

  @Test()
  public void testDifferentSizeImage() throws FileNotFoundException {
    rd = new FileReader("res/script.txt");
    new MultiLayerImageController(layerImageCommands, model, view, rd)
        .startApp();
    assertTrue(ap.toString().contains("Failed to execute the given command"));
  }

  @Test()
  public void testScript2() throws FileNotFoundException {
    rd = new FileReader("res/script2.txt");
    new MultiLayerImageController(layerImageCommands, model, view, rd)
        .startApp();
    ImageModelImpl img1 = new ImageModelImpl("res/scriptRes/0.png", new PNGFormat());
    ImageModelImpl img2 = new ImageModelImpl("res/scriptRes/1.png", new PNGFormat());
    assertTrue(img1.almostEquals(model.getLayers().get(0).getImage()));
    assertTrue(img2.almostEquals(model.getLayers().get(1).getImage()));

    assertEquals("Loaded image res/TeddyBlur.png as format png to layer 1.\n"
        + "Loaded image res/Teddy.png as format png to layer 1.\n"
        + "Exported all layers to res/scriptRes.\n", ap.toString());
  }

  @Test
  public void testNoCommand() throws FileNotFoundException {
    rd = new FileReader("res/script3.txt");
    new MultiLayerImageController(layerImageCommands, model, view, rd)
        .startApp();
    assertTrue(ap.toString().contains("The specified command 'test' does not exist."));
  }

  @Test()
  public void testAllCommands() throws FileNotFoundException {
    rd = new FileReader("res/script4.txt");
    new MultiLayerImageController(layerImageCommands, model, view, rd)
        .startApp();
    assertEquals(2, model.getLayers().size());
    assertFalse(model.getLayers().get(0).isLoaded());
    assertTrue(model.getLayers().get(1).isLoaded());
    assertFalse(model.getLayers().get(1).isTransparent());
  }
}
