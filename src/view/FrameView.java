package view;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import model.layered.IViewLayerImageModel;

/**
 * A view component for a {@link controller.FrameController}. This object is associated with a
 * {@link JFrame} from which it reads state information and writes all output to a provided GUI
 * frame. This object also contains EventListeners for all actions supported by the GUI in the frame
 * menus. Events are broadcast to all registered listeners (using the registerEventListener)
 * method.
 */
public class FrameView extends JFrame implements IImageEventView, ActionListener {

  private final JLabel imageLabel;
  private final IViewLayerImageModel model;
  private List<JRadioButtonMenuItem> layerButtons;
  private final JMenu layersMenu;
  private final ButtonGroup layersGroup;
  List<IViewListener> viewListeners;

  /**
   * Instantiate this view with all of the components of the window along with its menus, submenus
   * and window scrolls.
   *
   * @param model whose state to use.
   */
  public FrameView(IViewLayerImageModel model) {
    super();
    setTitle("LIME: Layered Image Manipulation and Enhancement");
    setSize(1000, 500);

    this.model = model;

    this.viewListeners = new ArrayList<>();

    JMenuBar menuBar = new JMenuBar();

    JMenu fileMenu = new JMenu("File");
    this.layersMenu = new JMenu("Layers");
    JMenu transformationMenu = new JMenu("Transformations");
    JMenu scriptingMenu = new JMenu("Scripting");
    JMenu saveSubMenu = new JMenu("Save As...");
    JMenu importSubMenu = new JMenu("Import From...");

    // items from Script menu
    JMenuItem loadScript = new JMenuItem("Load Script from File");
    loadScript.addActionListener(this);
    loadScript.setActionCommand("load");

    // items from File menu
    JMenuItem saveAsPPM = new JMenuItem("Save As PPM");
    saveAsPPM.addActionListener(this);
    saveAsPPM.setActionCommand("save ppm");

    JMenuItem saveAsPNG = new JMenuItem("Save As PNG");
    saveAsPNG.addActionListener(this);
    saveAsPNG.setActionCommand("save png");

    JMenuItem saveAsJPG = new JMenuItem("Save As JPG");
    saveAsJPG.addActionListener(this);
    saveAsJPG.setActionCommand("save jpg");

    JMenuItem saveModel = new JMenuItem("Save Model To Directory");
    saveModel.addActionListener(this);
    saveModel.setActionCommand("save model");

    JMenuItem loadPPM = new JMenuItem("Import PPM");
    loadPPM.addActionListener(this);
    loadPPM.setActionCommand("import ppm");

    JMenuItem loadPNG = new JMenuItem("Import PNG");
    loadPNG.addActionListener(this);
    loadPNG.setActionCommand("import png");

    JMenuItem loadJPG = new JMenuItem("Import JPG");
    loadJPG.addActionListener(this);
    loadJPG.setActionCommand("import jpg");

    JMenuItem loadModel = new JMenuItem("Import Model from Directory");
    loadModel.addActionListener(this);
    loadModel.setActionCommand("import model");

    // items in Layers menu
    JMenuItem addLayer = new JMenuItem("Add Layer");
    addLayer.addActionListener(this);
    addLayer.setActionCommand("layer add");

    JMenuItem removeLayer = new JMenuItem("Remove Layer");
    removeLayer.addActionListener(this);
    removeLayer.setActionCommand("layer remove");

    JMenuItem toggleLayer = new JMenuItem("Toggle Layer Transparency");
    toggleLayer.addActionListener(this);
    toggleLayer.setActionCommand("layer toggle");

    JMenuItem upLayer = new JMenuItem("Move Layer Up");
    upLayer.addActionListener(this);
    upLayer.setActionCommand("layer up");

    JMenuItem downLayer = new JMenuItem("Move Layer Down");
    downLayer.addActionListener(this);
    downLayer.setActionCommand("layer down");

    this.layerButtons = new ArrayList<>();
    this.layersGroup = new ButtonGroup();

    // items in Transformation menu
    JMenuItem blurImage = new JMenuItem("Blur Image");
    blurImage.addActionListener(this);
    blurImage.setActionCommand("transform blur");

    JMenuItem sharpenImage = new JMenuItem("Sharpen Image");
    sharpenImage.addActionListener(this);
    sharpenImage.setActionCommand("transform sharpen");

    JMenuItem sepiaImage = new JMenuItem("Sepia Image");
    sepiaImage.addActionListener(this);
    sepiaImage.setActionCommand("transform sepia");

    JMenuItem greyImage = new JMenuItem("Greyscale Image");
    greyImage.addActionListener(this);
    greyImage.setActionCommand("transform greyscale");

    JMenuItem downscaleImage = new JMenuItem("Downscale Image");
    downscaleImage.addActionListener(this);
    downscaleImage.setActionCommand("transform downscale");

    JMenuItem mosaicImage = new JMenuItem("Mosaic Image");
    mosaicImage.addActionListener(this);
    mosaicImage.setActionCommand("transform mosaic");

    // File sub-menu
    saveSubMenu.add(saveAsPPM);
    saveSubMenu.add(saveAsPNG);
    saveSubMenu.add(saveAsJPG);

    // File menu
    fileMenu.add(saveModel);
    fileMenu.add(saveSubMenu);

    fileMenu.addSeparator();

    importSubMenu.add(loadPPM);
    importSubMenu.add(loadPNG);
    importSubMenu.add(loadJPG);

    fileMenu.add(loadModel);
    fileMenu.add(importSubMenu);

    // Layers menu
    layersMenu.add(addLayer);
    layersMenu.add(removeLayer);
    layersMenu.add(toggleLayer);
    layersMenu.addSeparator();
    layersMenu.add(upLayer);
    layersMenu.add(downLayer);
    layersMenu.addSeparator();

    // Transformation menu
    transformationMenu.add(blurImage);
    transformationMenu.add(sharpenImage);
    transformationMenu.add(sepiaImage);
    transformationMenu.add(greyImage);
    transformationMenu.add(downscaleImage);
    transformationMenu.add(mosaicImage);

    // Scripting menu
    scriptingMenu.add(loadScript);

    // Menu bar
    menuBar.add(fileMenu);
    menuBar.add(layersMenu);
    menuBar.add(transformationMenu);
    menuBar.add(scriptingMenu);

    this.setJMenuBar(menuBar);

    JPanel mainPanel = new JPanel();
    //for elements to be arranged vertically within this panel
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
    mainPanel.setPreferredSize(new Dimension(1000, 500));

    imageLabel = new JLabel();
    JScrollPane scrollPanel = new JScrollPane(imageLabel);
    scrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

    mainPanel.add(scrollPanel);
    add(mainPanel);
    this.pack();
  }

  /**
   * Helper method that reads layer state information from the model and re-draws the layer menu.
   */
  private void updateLayerButtons() {
    // first remove old layer buttons
    for (JRadioButtonMenuItem button : this.layerButtons) {
      layersGroup.remove(button);
      layersMenu.remove(button);
    }

    this.layerButtons = new ArrayList<>();
    for (int i = 0; i < this.model.getLayers().size(); i++) {
      JRadioButtonMenuItem item = new JRadioButtonMenuItem(
          "Layer " + (i + 1) + ": " + this.model.getLayers().get(i).toString());
      item.setSelected(this.model.getCurrent() == i);
      item.addActionListener(this);
      item.setActionCommand("layer select " + i);
      this.layerButtons.add(item);
      layersGroup.add(item);
      layersMenu.add(item);
    }
  }

  @Override
  public void renderApp() throws IllegalStateException {
    // set window parameters
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setVisible(true);

    this.imageLabel.setIcon(null);

    try {
      this.updateLayerButtons();
      Image toRender = this.model.toImage();
      this.imageLabel.setIcon(new ImageIcon(toRender));
    } catch (IllegalArgumentException e) {
      return;
    }
  }

  @Override
  public void renderMessage(String message) throws IllegalStateException {
    JOptionPane.showMessageDialog(this, message);
  }

  @Override
  public void renderPrompt() throws IllegalStateException {
    return;
  }

  // Event emitters:
  // loading scripts
  protected void emitLoadEvent(String location) {
    for (IViewListener listener : this.viewListeners) {
      listener.handleScriptingEvent(location);
    }
  }

  // saving images/models
  protected void emitSaveEvent(String type, String location) {
    for (IViewListener listener : this.viewListeners) {
      listener.handleSaveEvent(type, location);
    }
  }

  // transforming layers
  private void emitTransformEvent(String type) {
    for (IViewListener listener : this.viewListeners) {
      listener.handleTransformEvent(type);
    }
  }

  // downscaling layers
  private void emitDownscaleEvent(int x, int y) {
    for (IViewListener listener : this.viewListeners) {
      listener.handleDownscaleEvent(x, y);
    }
  }

  // mosaic layers
  private void emitMosaicEvent(int seeds) {
    for (IViewListener listener : this.viewListeners) {
      listener.handleMosaicEvent(seeds);
    }
  }

  // import images/models
  protected void emitImportEvent(String type, String location) {
    for (IViewListener listener : this.viewListeners) {
      listener.handleImportEvent(type, location);
    }
  }

  // add/remove layers
  private void emitAddSubEvent(boolean add) {
    for (IViewListener listener : this.viewListeners) {
      listener.handleAddSubEvent(add);
    }
  }

  // toggle layer transparency
  private void emitToggleEvent() {
    for (IViewListener listener : this.viewListeners) {
      listener.handleToggleEvent();
    }
  }

  // move layers up/down
  private void emitMoveEvent(boolean up) {
    for (IViewListener listener : this.viewListeners) {
      listener.handleMoveEvent(up);
    }
  }

  // set layer as current
  private void emitSetCurrentEvent(int index) {
    for (IViewListener listener : this.viewListeners) {
      listener.handleSetCurrentEvent(index);
    }
  }

  // open file/folder chooser dialog
  private String getUserFileLocation(boolean open, boolean dir, String filter)
      throws IllegalStateException {
    JFileChooser fc = new JFileChooser();
    if (dir) {
      fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    }
    if (filter != null) {
      fc.addChoosableFileFilter(new FileNameExtensionFilter("Image Files", filter));
    }
    int returnVal;
    if (open) {
      returnVal = fc.showOpenDialog(this);
    } else {
      returnVal = fc.showSaveDialog(this);
    }
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      return fc.getSelectedFile().getAbsolutePath();
    } else {
      throw new IllegalStateException("No file was selected.");
    }
  }

  // prompt the user for some input
  private String userPrompt(String message) throws IllegalStateException {
    String response = JOptionPane.showInputDialog(this, message);
    if (response == null) {
      throw new IllegalStateException("User did not answer.");
    }
    return response;
  }

  @Override
  public void registerViewEventListener(IViewListener listener) {
    this.viewListeners.add(Objects.requireNonNull(listener));
  }

  @Override
  public void actionPerformed(ActionEvent e) throws IllegalStateException {
    String[] command = e.getActionCommand().split(" ");

    switch (command[0]) {
      case "save":
        try {
          String filter = null;
          if (!command[1].equals("model")) {
            filter = command[1];
          }
          this.emitSaveEvent(command[1], this.getUserFileLocation(false, false, filter));
        } catch (IllegalStateException exception) {
          System.err.println(exception.getMessage());
        }
        break;
      case "transform":
        if (command[1].equals("downscale")) {
          try {
            this.emitDownscaleEvent(Integer.parseInt(this.userPrompt("New width:")),
                Integer.parseInt(this.userPrompt("New height:")));
          } catch (IllegalStateException exception) {
            System.err.println(exception.getMessage());
          } catch (NumberFormatException exception) {
            this.renderMessage("Must provide an integer value!");
          }
        } else if (command[1].equals("mosaic")) {
          try {
            this.emitMosaicEvent(Integer.parseInt(this.userPrompt("Number of seeds:")));
          } catch (IllegalStateException exception) {
            System.err.println(exception.getMessage());
          } catch (NumberFormatException exception) {
            this.renderMessage("Must provide an integer value!");
          }
        } else {
          this.emitTransformEvent(command[1]);
        }
        this.renderApp();
        break;
      case "load":
        try {
          this.emitLoadEvent(this.getUserFileLocation(true, false, null));
        } catch (IllegalStateException exception) {
          System.err.println(exception.getMessage());
        }
        break;
      case "import":
        try {
          String filter = null;
          if (!command[1].equals("model")) {
            filter = command[1];
          }
          this.emitImportEvent(command[1],
              this.getUserFileLocation(true, command[1].equals("model"), filter));
        } catch (IllegalStateException exception) {
          System.err.println(exception.getMessage());
        }
        break;
      case "layer":
        this.handleLayerEvent(command);
        break;
      default:
        throw new IllegalStateException("Unknown command " + command[0] + " issued.");
    }
  }

  // helper method that deals that handles layer operations and emits the appropriate event
  private void handleLayerEvent(String[] command) throws IllegalStateException {
    switch (command[1]) {
      case "add":
        this.emitAddSubEvent(true);
        break;
      case "remove":
        this.emitAddSubEvent(false);
        break;
      case "toggle":
        this.emitToggleEvent();
        break;
      case "up":
        this.emitMoveEvent(true);
        break;
      case "down":
        this.emitMoveEvent(false);
        break;
      case "select":
        this.emitSetCurrentEvent(Integer.parseInt(command[2]));
        break;
      default:
        throw new IllegalStateException("Unknown layer command " + command[1] + " issued.");
    }
  }
}
