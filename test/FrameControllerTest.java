import controller.FrameController;
import model.layered.LayerImageModel;
import org.junit.Test;
import view.FrameView;

/**
 * Tests if FrameController constructor is throwing an Illegal Argument Exception if model, view or
 * arguments are null.
 */
public class FrameControllerTest {

  @Test(expected = IllegalArgumentException.class)
  public void testNullModel() {
    new FrameController(null, new FrameView(new LayerImageModel()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullView() {
    new FrameController(new LayerImageModel(), null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullArguments() {
    new FrameController(null, null);
  }

}
