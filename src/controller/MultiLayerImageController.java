package controller;

import controller.command.ICommand;
import controller.command.LayerImageCommands;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Scanner;
import model.layered.ILayerImageModel;
import model.layered.LayerImageModel;
import view.IImageView;
import view.MultiLayerImageView;

/**
 * An implementation of {@link IImageController} which must be used with an {@link
 * ILayerImageModel}. This controller reads commands from a HashMap (for example, from the command
 * set specified in {@link LayerImageCommands} and dispatches user commands with arguments to the
 * appropriate {@link ICommand} object.
 *
 * <p>This controller can also read exported models and scripts. See the main method for additional
 * details.
 */
public class MultiLayerImageController implements IImageController {

  /**
   * Starts the controller using the terminal for input (System.in) and output (System.out). By
   * specifying optional commandline arguments, this program can also read saves and execute from
   * scripts rather than System.in (see below).
   *
   * @param args optional arguments to direct this program to load a {@link ILayerImageModel} from a
   *             saved directory (using --from/-f) and to read commands from a script rather than
   *             from user input (using --script/-s).
   */
  public static void main(String[] args) {
    ILayerImageModel model = new LayerImageModel();
    Readable rd = new InputStreamReader(System.in);
    boolean script = false;

    // Read arguments for --from/-f/-from and --script/-s/-script
    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("--from") || args[i].equals("-f") || args[i].equals("-from")) {
        try {
          model = new LayerImageModel(args[i + 1]);
          i++;
        } catch (IndexOutOfBoundsException e) {
          System.err.println("Must specify directory to load image from!");
          return;
        }
      } else if (args[i].equals("--script") || args[i].equals("-s") || args[i].equals("-script")) {
        try {
          rd = new FileReader(args[i + 1]);
          script = true;
        } catch (FileNotFoundException e) {
          System.err
              .println("Could not read the specified script (" + e.getMessage() + ")");
          return;
        } catch (IndexOutOfBoundsException e) {
          System.err
              .println("Must specify script to read from!");
          return;
        }
        i++;
      } else if (args[i].equals("-text")) {
        continue;
      } else {
        System.err
            .println("Invalid argument '" + args[i]
                + "'. Valid arguments are --from/-f and --script/-s.");
        return;
      }
    }

    IImageView view = new MultiLayerImageView(model, System.out, script);

    new MultiLayerImageController(new LayerImageCommands(model, view).defaultCommands(),
        model, view, rd)
        .startApp();
  }

  private final Map<String, ICommand> layerImageCommands;
  private final IImageView view;
  private final Scanner in;

  /**
   * Creates a new controller with the specified command map, model, view and input stream. All
   * arguments must be non-null. To start the program once instantiated, call startApp().
   *
   * @param layerImageCommands the command map to use for interpreting user commands. {@link
   *                           LayerImageCommands} contains a complete set of commands to be used
   *                           with this controller.
   * @param model              the model to use for executing commands.
   * @param view               the view to send output to.
   * @param rd                 the readable stream to receive text commands from.
   * @throws IllegalArgumentException if any of the arguments are null.
   */
  public MultiLayerImageController(Map<String, ICommand> layerImageCommands, ILayerImageModel model,
      IImageView view, Readable rd) throws IllegalArgumentException {
    if (layerImageCommands == null || model == null || rd == null || view == null) {
      throw new IllegalArgumentException("Arguments cannot be null!");
    }
    this.layerImageCommands = layerImageCommands;
    this.view = view;
    this.in = new Scanner(rd);
  }

  @Override
  public void startApp() throws IllegalStateException {
    // Initial render
    this.view.renderApp();
    this.view.renderPrompt();
    ScriptUtil.executeScript(this.view, this.layerImageCommands, this.in);
  }
}
