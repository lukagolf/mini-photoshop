# How to use this program

This document contains instructions for using the user-interface, running the program, executing
commands and reading scripts.

## How to run the program

Locate the JAR executable in `res/hw07.jar`. This executable serves as an entry point to the
program.

When running the executable, the run-mode must be specified using one of the following options:

* `-script /path/to/script.txt` a path to a text file containing commands and comments on individual
  lines. See below for the syntax and supported commands. Once this script is finished executing,
  the program will exit.
* `-text` run in commandline-mode, reading and executing commands one-line at time.
* `-interactive` run the interactive GUI program.

This executable has one optional argument:

* `--from/-f /path/to/dir` to import a saved image state directory rather than start with an empty
  image (for use with `-script` and `-text`).

## Using the GUI

The first visible (loaded, non-transparent) layer is displayed in the frame. The top menu contains
all the operations supported by the program. The operations contained in each menu are described
below:

### `File` menu

* Allows saving the model to directory, preserving all layers and transparency.
* Allows saving the topmost visible layer to an image file.
* Allows importing a model from directory, replacing the layers of the current model.
* Allows importing an image to the current layer, replacing the currently loaded image.

### `Layers` menu

* Allows adding and removing layers.
* Allows toggling the transparency of the currently selected layer.
* Allows moving the currently selected layer up or down.
* Displays all layers in the image, their size, transparency and the currently selected layer.

### `Transformations` menu

* Blur transformation will blur the currently selected layer.
* Sharpen transformation will sharpen the currently selected layer.
* Greyscale transformation will apply a greyscale filter to the currently selected layer.
* Sepia transformation will apply a sepia filter to the currently selected layer.
* Downscale transformation will allow users to scale the image dimensions down, both width and
  height.
* Mosaic transformation will allow users to create a mosaic pattern from the image, with a specified
  number of seeds.

### `Scripting` menu

* Allows loading and executing a script from a file.

## Using the commmand-line and scripting modes

### How to run commands

For both interactive use and scripting, each command is on its own line. Commands are
space-delimited. The first term is the name of the command and subsequent terms are command
arguments. For a full listing of commands, see the below section.

Any lines starting with the character `#` will be treated as comments and will be ignored. However,
this must be the very first character in a line to be considered a comment.

Some commands will perform an operation on the currently selected layer. This is indicated with an
asterisks (`*`) next to the layer name in the program output and in the prompt that appears before
user input.

### List of supported commands

All command names are case-insensitive, though arguments may not be. Command names and arguments
must be separated by spaces and commands by new lines.

Lines beginning with `#` are comments and are ignored.

* `status` displays the status of the image, including all layers, dimensions, the currently
  selected layer, transparency and whether a layer is empty.
* `create` adds a new empty, transparent layer to the top of the image
* `transform` applies a transformation to the currently selected layer. Requires one argument: the
  type of transformation (one of `blur`, `sharpen`, `greyscale`, `sepia`) to be performed.
* `export` exports the first visible layer to an image file. Requires the file location as an
  argument. One optional argument is the image format to use. If this is not specified, the program
  will try to determine the correct format to use based on the file extension.
* `exportall` exports the state of the image to a directory. This preserves all information about
  the image, including transparent and empty layers as well as layer order. Requires one argument:
  the location of the export directory.
* `load` will load an image file to the currently selected layer and makes the current layer opaque.
  Requires the filename as an argument and optionally the format (if not provided, the program will
  attempt to determine the format using the file extension).
* `remove` deletes the layer at a specified index. Requires one argument: the index of the layer to
  remove.
* `current` will set the specified layer as the current layer. Requires one argument: the index of
  the layer to set as current.
* `transparent` sets the specified layer as transparent. Requires one argument: the index of the
  layer to set as transparent.
* `opaque` sets the specified layer as opaque. Requires one argument: the index of the layer to set
  as opaque.
* `move` moves a specified layer to a specified location, pushing back existing layers. This will
  also set the current layer to the next available layer. Requires two arguments: the index of the
  layer to move and the new position.
* `quit` or `exit` exits the program.

### Example runs

#### File `ExampleRun1.txt`:

##### Run with `java -jar res/hw07.jar -script res/ExampleRun1.txt`

1. Creates a new layer
2. Loads the image `res/Boat.ppm` to that layer
3. Creates a new layer
4. Loads the image `res/BoatBlur.ppm` to that layer
5. Moves layer 1 to index 2
6. Applies a sepia transformation to layer 1
7. Applies a blur transformation to layer 1
8. Sets layer 2 as transparent
9. Exports the image as `res/BoatSepiaBlur.jpg`
10. Saves the program state to directory `res/BoatState`
11. Exits the program

#### File `ExampleRun2.txt` (should be run after `ExampleRun1.txt`):

##### Run with `java -jar res/hw07.jar -script res/ExampleRun2.txt --from res/BoatState`

0. Reads program state from `res/BoatState`
1. Removes layer 1 from the image
2. Sets the new layer 1 as opaque
3. Creates a new layer
4. Sets layer 2 as the current layer
5. Overwrites the program state in directory `res/BoatState`
6. Exits the program