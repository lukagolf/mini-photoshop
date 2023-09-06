package view;

import java.io.IOException;
import java.util.List;
import model.layered.ILayer;
import model.layered.IViewLayerImageModel;

/**
 * A view component for a {@link controller.MultiLayerImageController}. This object is associated
 * with a {@link MultiLayerImageView} from which it reads state information and writes all output to
 * a provided {@link Appendable}.
 *
 * <p>Application state is rendered by listing each layer, along with its status (whether it is
 * currently selected, dimensions, transparency and whether it is loaded).
 *
 * <p>The prompt will also display the currently selected layer unless there is none.
 */
public class MultiLayerImageView implements IImageView {

  private final IViewLayerImageModel model;
  private final Appendable ap;
  private final boolean script;

  /**
   * Instantiate this view with a model to read state information from and an appendable to write
   * output to.
   *
   * @param model  whose state to use.
   * @param ap     to write to.
   * @param script whether this view is being used with a script. If it is, then don't print the
   *               prompt.
   */
  public MultiLayerImageView(IViewLayerImageModel model, Appendable ap, boolean script) {
    this.model = model;
    this.ap = ap;
    this.script = script;
  }

  @Override
  public void renderApp() throws IllegalStateException {
    if (script) {
      return;
    }
    List<ILayer> layers = this.model.getLayers();
    this.renderMessage("Total " + layers.size() + " layers:\n");
    for (int i = 0; i < layers.size(); i++) {
      this.renderMessage("\t");
      if (i == this.model.getCurrent()) {
        this.renderMessage("* ");
      }
      this.renderMessage("Layer " + (i + 1) + ": ");
      this.renderMessage(layers.get(i).toString());
      this.renderMessage("\n\n");
    }
  }

  @Override
  public void renderMessage(String message) throws IllegalStateException {
    try {
      ap.append(message);
    } catch (IOException e) {
      throw new IllegalArgumentException("Failed to write to the provided appendable!");
    }
  }

  @Override
  public void renderPrompt() {
    if (!script) {
      //this.renderMessage("\n");
      if (this.model.getLayers().size() > 0) {
        this.renderMessage("Layer " + (this.model.getCurrent() + 1));
      }
      this.renderMessage("> ");
    }
  }
}
