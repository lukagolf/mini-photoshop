package view;

/**
 * The view component of the image MVC application. This view supports rendering the model state, as
 * well as rendering provided messages and generating a user input prompt. Implementations will
 * likely require instantiation with a {@link model.IImageModel} to read state information from.
 */
public interface IImageView {

  /**
   * Renders the current state of the model.
   *
   * @throws IllegalStateException if writing to the output stream fails.
   */
  void renderApp() throws IllegalStateException;

  /**
   * Writes the provided message to the output.
   *
   * @param message the message to output.
   * @throws IllegalStateException if writing to the output stream fails.
   */
  void renderMessage(String message) throws IllegalStateException;

  /**
   * Writes a user prompt to the output, indicating the user to provide additional input.
   *
   * @throws IllegalStateException if writing to the output stream fails.
   */
  void renderPrompt() throws IllegalStateException;

}
