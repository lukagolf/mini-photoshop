package controller;

/**
 * Interface the defines the operations of a Image processing controller. This controller will work
 * with a corresponding model to allow users to transform, filter and rearrange images of different
 * formats.
 */
public interface IImageController {

  /**
   * Start an instance of the controller application.
   *
   * @throws IllegalStateException if writing to the Appendable object used by it fails or reading
   *                               from the provided Readable fails
   */
  void startApp() throws IllegalStateException;
}
