import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import model.ImageModelImpl;
import model.format.JPGFormat;
import model.format.PNGFormat;
import model.format.PPMFormat;
import model.layered.ILayerImageModel;
import model.layered.LayerImageModel;
import model.transformation.color.GreyTransformation;
import model.transformation.color.SepiaTransformation;
import model.transformation.filter.BlurTransformation;
import org.junit.Test;

/**
 * Represents a class that tests whether we are able to load, set and transform the layers and then
 * export them in the format that we desire.
 */
public class LayerImageModelTest {

  @Test
  public void testThreeLayerImage() {
    ILayerImageModel li = new LayerImageModel();

    li.addLayer();
    li.setCurrent(0);
    li.loadImage(new ImageModelImpl("res/Teddy.ppm", new PPMFormat()));

    li.addLayer();
    li.setCurrent(0);
    li.loadImage(new ImageModelImpl("res/Teddy.png", new PNGFormat()));

    li.addLayer();
    li.setCurrent(0);
    li.loadImage(new ImageModelImpl("res/Teddy.jpg", new JPGFormat()));

    li.setCurrent(0);
    li.transform(new BlurTransformation());
    li.exportToFile("res/TeddyBlur.png", new PNGFormat());

    li.setCurrent(1);
    li.transform(new GreyTransformation());
    li.exportToFile("res/TeddyGrey.jpg", new JPGFormat());

    li.setCurrent(2);
    li.transform(new SepiaTransformation());
    li.exportToFile("res/TeddySepia.png", new PNGFormat());

    li.removeLayer(1);

    li.exportAllLayers("res/TeddyLayers");

    ILayerImageModel li2 = new LayerImageModel("res/TeddyLayers");
    assertEquals(2, li2.getLayers().size());
    assertTrue(li2.getLayers().get(1).getImage().getHeight() == li.getHeight());

    assertTrue(li2.getLayers().get(1).getImage().getWidth() == li.getWidth());
  }
}
