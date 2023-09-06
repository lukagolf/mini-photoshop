import java.awt.event.ActionEvent;
import model.layered.IViewLayerImageModel;
import view.FrameView;
import view.MultiLayerImageView;

/**
 * Mock version of {@link FrameView} replacing methods that require user actions (such as selecting
 * save/import files) so tests can be fully automated.
 */
public class MockFrameView extends FrameView {

  private String data;
  private IViewLayerImageModel model;

  /**
   * Create a new mock frame view with the given model. For testing, a standard {@link
   * model.layered.LayerImageModel} can be used. Keeps a local copy of the model to avoid modifying
   * production files.
   *
   * @param model the model to use with this mock view.
   */
  public MockFrameView(IViewLayerImageModel model) {
    super(model);
    this.data = "";
    this.model = model;
  }

  /**
   * Collects and returns all the data printed by the view and its parent.
   *
   * @return the string data outputted by the view.
   */
  public String getData() {
    return data;
  }

  @Override
  public void renderApp() throws IllegalStateException {
    super.renderApp();
    StringBuilder out = new StringBuilder();
    new MultiLayerImageView(this.model, out, true).renderApp();
    this.data += out.toString();
  }

  @Override
  public void renderMessage(String message) throws IllegalStateException {
    this.data += message;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String[] command = e.getActionCommand().split(" ");

    switch (command[0]) {
      case "save":
        try {
          super.emitSaveEvent(command[1], command[2]);
        } catch (IllegalStateException exception) {
          System.err.println(exception.getMessage());
        }
        break;
      case "load":
        try {
          super.emitLoadEvent(command[1]);
        } catch (IllegalStateException exception) {
          System.err.println(exception.getMessage());
        }
        break;
      case "import":
        try {
          super.emitImportEvent(command[1], command[2]);
        } catch (IllegalStateException exception) {
          System.err.println(exception.getMessage());
        }
        break;
      default:
        super.actionPerformed(e);
        break;
    }
  }
}
