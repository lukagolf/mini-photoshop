package controller.command;

/**
 * Represents a command supported by an {@link controller.IImageController}. Concrete
 * implementations must consume an {@link model.IImageModel} to perform the operations and possibly
 * an {@link view.IImageView} for outputting information.
 */
public interface ICommand {

  /**
   * Run the operation with the given string arguments from the user. String arguments should be
   * passed as-is, with no case changes. The first argument should be the name of the command that
   * was called (i.e. if the user calls create abc 123, args should be [create, abc, 123]).
   *
   * @param args The string arguments provided by the user, split up by spaces.
   * @throws IllegalArgumentException if the specified operation is invalid or otherwise fails.
   */
  void apply(String[] args) throws IllegalArgumentException;
}
