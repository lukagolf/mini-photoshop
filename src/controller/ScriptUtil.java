package controller;

import controller.command.ICommand;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import view.IImageView;

/**
 * Represents a class containing a static method for reading, parsing and executing the script with
 * the given view, command set, and input stream.
 */
public class ScriptUtil {

  /**
   * While there are remaining commands in the script the method is executing the script with the
   * given view, layerImageCommands, and input stream.
   *
   * @param view               the view to send output to.
   * @param layerImageCommands he command map to use for interpreting user commands. {@link
   *                           controller.command.LayerImageCommands} contains a complete set of
   *                           commands to be used with this controller.
   * @param in                 input stream.
   */
  public static void executeScript(IImageView view, Map<String, ICommand> layerImageCommands,
      Scanner in) {

    // Input loop
    while (in.hasNextLine()) {
      String[] args = in.nextLine().split(" ");
      if (args.length == 0) {
        view.renderMessage("No command specified!");
        continue;
      }

      String command = args[0].toLowerCase(Locale.ROOT);

      // Ignore comments
      if (command.length() > 0 && command.charAt(0) != '#') {
        // Special commands
        if (command.equals("quit") || command.equals("exit")) {
          view.renderMessage("Quitting...");
          return;
        } else if (command.equals("status")) {
          view.renderApp();
        } else {
          if (layerImageCommands.containsKey(command)) {
            try {
              layerImageCommands.get(command).apply(args);
              view.renderApp();
            } catch (IllegalArgumentException e) {
              view.renderMessage("Failed to execute the given command " + args[0]
                  + "! (" + e.getMessage() + ")\n");
            }
          } else {
            view.renderMessage("The specified command '" + command + "' does not exist.\n");
          }
        }
      }

      // Print a prompt for user input
      view.renderPrompt();
    }
  }
}
