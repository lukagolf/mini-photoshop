# Mini Photoshop

## Features

* Interactive graphic-user interface supporting all features of the text-based interface.
* Interactive text-based user interface that allows issuing commands and viewing the state of the
  program.
* Support for reading and executing scripts from files.
* Support for multi-layer images
* Adding and removing layers
* Transparent and opaque layers
* Empty layers
* Loading images (PPM, PNG or JPG) into layers
* Selecting individual layers
* Performing transformations on layers (blur, sharpen, greyscale, or sepia)
* Moving/re-arranging layers
* Saving the topmost visible layer to a file (PPM, PNG or JPG)
* Saving the state of the program to a directory, preserving all information including layer order,
  transparent and empty layers
* Restoring a saved program state

## How to run/use/examples

See the [`USEME.md`](USEME.md) file in this directory.

## Extra

Both image downscaling and image mosaicking were implemented in this program. Both operations are
implemented as standard `ITransformation`s, similar to blur, sharpen, greyscale and sepia, and can
be passed into any `IImageModel`'s transform method. They can be accessed from the Transformations
menu.

Examples are provided in the `res/` directory:

* `TeddyDownscale.png`: version of the `Teddy.png` image downscaled while maintaining aspect ratio.
* `TeddyDownscaleStretched.png`: version of the `Teddy.png` image downscaled to a new aspect ratio.
* `TeddyMosiacked*.png`: versions of the `Teddy.png` image randomly mosaicked with 100, 1,000 and
  10,000 seeds.

## Assumptions

The following assumptions and generalizations were made from the assignment description during the
development of this program:

* All layers must be of the same dimensions
* The program should not and does not support blending multiple layers
* Transformations are only applied to the currently selected layer
* Image alpha channels should not be preserved
* Layers can only be completely opaque or completely transparent
* Layers are ordered, but not named
* The program is run primarily using the main method
* The architecture of the assignment should follow the MVC model

## Limitations

The following limitations apply in
the use of this program as a general image editor:

* Alpha channels are not supported
* Partial transparency is not supported
* Only the PPM, PNG and JPG image formats are supported
* Layers cannot be named
* Only blur, sharpen, greyscale, sepia, downscale and mosaic transformations are supported
* Layers or images cannot be cropped

#### Image citations:

Teddy Bear Image: https://www.speedrun.com/teddy_floppy_ear_-_kayaking

Boat
Image: https://www.greatlakesscuttlebutt.com/news/featured-news/river-boats-na-small-boats-big-adventure/
