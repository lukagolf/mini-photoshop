import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import controller.FrameController;
import controller.IImageController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JColorChooser;
import model.ImageModelImpl;
import model.format.PNGFormat;
import model.format.PPMFormat;
import model.layered.ILayer;
import model.layered.ILayerImageModel;
import model.layered.LayerImageModel;
import org.junit.Before;
import org.junit.Test;

/**
 * Represents a class for testing events in the swing window view.
 */
public class FrameViewEventsTest {

  ILayerImageModel model;
  MockFrameView view;
  ActionListener viewListener;
  IImageController controller;

  @Before
  public void initialize() {
    this.model = new LayerImageModel();
    this.view = new MockFrameView(model);
    this.viewListener = view;
    this.controller = new FrameController(model, view);
    this.controller.startApp();
  }

  private void runCommand(String command) {
    viewListener.actionPerformed(new ActionEvent(new JColorChooser(), 0, command));
  }

  @Test
  public void testSaveImageEvent() {
    this.runCommand("layer add");
    this.runCommand("import png res/Teddy.png");
    this.runCommand("save ppm res/TeddySaveEvent.ppm");
    assertTrue(new ImageModelImpl("res/Teddy.png", new PNGFormat())
        .equals(new ImageModelImpl("res/TeddySaveEvent.ppm", new PPMFormat())));
  }

  @Test
  public void testLoadImageEvent() {
    assertEquals(0, this.model.getLayers().size());

    this.runCommand("layer add");
    assertEquals(1, this.model.getLayers().size());
    assertFalse(this.model.getLayers().get(this.model.getCurrent()).isLoaded());

    this.runCommand("import ppm res/Teddy.ppm");
    assertTrue(this.model.getLayers().get(this.model.getCurrent()).isLoaded());
  }

  @Test
  public void testSaveLoadModelEvent() {
    assertEquals(0, this.model.getLayers().size());

    this.runCommand("layer add");
    this.runCommand("import ppm res/Teddy.ppm");

    this.runCommand("layer add");
    this.runCommand("import png res/TeddyBlur.png");
    this.runCommand("layer toggle");

    this.runCommand("layer add");

    assertEquals(3, this.model.getLayers().size());

    assertTrue(this.model.getLayers().get(0).isTransparent());
    assertFalse(this.model.getLayers().get(0).isLoaded());

    assertTrue(this.model.getLayers().get(1).isTransparent());
    assertTrue(this.model.getLayers().get(1).isLoaded());

    assertFalse(this.model.getLayers().get(2).isTransparent());
    assertTrue(this.model.getLayers().get(2).isLoaded());

    this.runCommand("save model res/TeddySaveModelEvent");
    this.runCommand("import model res/TeddySaveModelEvent");
    assertEquals(3, this.model.getLayers().size());

    assertTrue(this.model.getLayers().get(0).isTransparent());
    assertFalse(this.model.getLayers().get(0).isLoaded());

    assertTrue(this.model.getLayers().get(1).isTransparent());
    assertTrue(this.model.getLayers().get(1).isLoaded());

    assertFalse(this.model.getLayers().get(2).isTransparent());
    assertTrue(this.model.getLayers().get(2).isLoaded());
  }

  @Test
  public void testTransformEvent() {
    this.runCommand("layer add");
    this.runCommand("import ppm res/Teddy.ppm");
    this.runCommand("transform blur");
    this.runCommand("save png res/TeddyBlurTransformEvent.png");
    assertTrue(new ImageModelImpl("res/TeddyBlur.png", new PNGFormat())
        .almostEquals(new ImageModelImpl("res/TeddyBlurTransformEvent.png", new PNGFormat())));
  }

  @Test
  public void testTransformations() {
    this.runCommand("layer add");
    this.runCommand("import ppm res/Teddy.ppm");

    this.runCommand("layer add");
    this.runCommand("import ppm res/Teddy.ppm");

    this.runCommand("layer add");
    this.runCommand("import ppm res/Teddy.ppm");

    this.runCommand("layer add");
    this.runCommand("import ppm res/Teddy.ppm");

    this.runCommand("transform blur");
    this.runCommand("save png res/TeddyBlurTransform.png");
    this.runCommand("layer toggle");
    this.runCommand("layer select 1");

    this.runCommand("transform sharpen");
    this.runCommand("save png res/TeddySharpTransform.png");
    this.runCommand("layer toggle");
    this.runCommand("layer select 2");

    this.runCommand("transform sepia");
    this.runCommand("save png res/TeddySepiaTransform.png");
    this.runCommand("layer toggle");
    this.runCommand("layer select 3");

    this.runCommand("transform greyscale");
    this.runCommand("save png res/TeddyGreyTransform.png");

    assertTrue(new ImageModelImpl("res/TeddyBlur.png", new PNGFormat())
        .almostEquals(new ImageModelImpl("res/TeddyBlurTransform.png", new PNGFormat())));

    assertTrue(new ImageModelImpl("res/TeddySharp.png", new PNGFormat())
        .almostEquals(new ImageModelImpl("res/TeddySharpTransform.png", new PNGFormat())));

    assertTrue(new ImageModelImpl("res/TeddySepiaTest.png", new PNGFormat())
        .almostEquals(new ImageModelImpl("res/TeddySepiaTransform.png", new PNGFormat())));

    assertTrue(new ImageModelImpl("res/TeddyGrey.ppm", new PPMFormat())
        .almostEquals(new ImageModelImpl("res/TeddyGreyTransform.png", new PNGFormat())));
  }

  @Test
  public void testScriptingEvent() {
    this.runCommand("load res/script4.txt");
    assertEquals(2, model.getLayers().size());
    assertFalse(model.getLayers().get(0).isLoaded());
    assertTrue(model.getLayers().get(1).isLoaded());
    assertFalse(model.getLayers().get(1).isTransparent());
    assertEquals("Script output:\n"
        + "Loaded image res/TeddyBlur.png as format png to layer 1.\n"
        + "Set layer 1 as transparent.\n"
        + "Set layer 1 as opaque.\n"
        + "Loaded image res/Teddy.png as format png to layer 1.\n"
        + "Applied sepia transformation to layer 1.\n"
        + "Moved layer 2 to position 1.Exported image to res/TeddySepiaTest.png using png format.\n"
        + "Exported all layers to res/scriptRes2.\n", this.view.getData());
  }

  @Test
  public void testMoveEvent() {
    this.runCommand("load res/script4.txt");
    assertEquals(2, model.getLayers().size());
    assertFalse(model.getLayers().get(0).isLoaded());
    assertTrue(model.getLayers().get(1).isLoaded());
    assertFalse(model.getLayers().get(1).isTransparent());

    assertEquals(0, this.model.getCurrent());
    this.runCommand("layer down");
    assertFalse(model.getLayers().get(1).isLoaded());
    assertTrue(model.getLayers().get(0).isLoaded());
    assertFalse(model.getLayers().get(0).isTransparent());

    this.runCommand("layer select 1");
    assertEquals(1, this.model.getCurrent());
    this.runCommand("layer up");
    assertFalse(model.getLayers().get(0).isLoaded());
    assertTrue(model.getLayers().get(1).isLoaded());
    assertFalse(model.getLayers().get(1).isTransparent());
  }

  @Test
  public void testAddSubEvent() {
    this.runCommand("load res/script4.txt");
    assertEquals(2, model.getLayers().size());
    ILayer layer = model.getLayers().get(1);

    this.runCommand("layer remove");
    assertEquals(1, model.getLayers().size());
    assertEquals(layer, model.getLayers().get(0));

    this.runCommand("layer add");
    assertEquals(2, model.getLayers().size());
    assertFalse(model.getLayers().get(0).isLoaded());
    assertEquals(layer, model.getLayers().get(1));
  }

  @Test
  public void testSetCurrentEvent() {
    this.runCommand("load res/script4.txt");
    assertEquals(2, model.getLayers().size());
    assertFalse(model.getLayers().get(0).isLoaded());
    assertTrue(model.getLayers().get(1).isLoaded());
    assertFalse(model.getLayers().get(1).isTransparent());

    assertEquals(0, this.model.getCurrent());

    this.runCommand("layer select 1");
    assertEquals(1, this.model.getCurrent());
  }

  @Test(expected = IllegalStateException.class)
  public void testUnknownCommand() {
    this.runCommand("unknown");
  }

  @Test(expected = IllegalStateException.class)
  public void testUnknownLayerCommand() {
    this.runCommand("layer unknown");
  }
}
