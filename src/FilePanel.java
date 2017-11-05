import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * FilePanel.java - A FilePanel is a grouping of GUI elements and the data that is represents.  When the user selects a
 * File from Disk, the FilePath to that file will be passed in to a new FilePanel, which will represent that selection
 * in the GUI.  The FilePanel allows the user to edit the file path and remove the file from the list of select files.
 *
 * @author Andrew McGuiness
 * @version 03/Nov/2017
 */
public class FilePanel extends JPanel {
    // GUI Constants
    public static final int PANE_WIDTH = 440;
    public static final int PANE_HEIGHT = 40;
    public static final int BUTTON_WIDTH = 30;
    public static final int BUTTON_HEIGHT = 30;
    public static final int TEXT_WIDTH = 390;
    public static final int TEXT_HEIGHT = 30;

    private static final int Y_OFFSET = 5;
    private static final int X_OFFSET = 5;
    private static final int X_BUFFER = 10;

    // Cached reference to the Delete Icon
    private static ImageIcon icon = null;

    private JButton deleteButton;
    private JTextField fileName;
    private String filePath;
    private MainFrame controller;


    /**
     * Create and setup a new FilePanel that can be added to another container.
     *
     * @param file       File that the user selected
     * @param controller Main Window of the Application where this FilePanel will be displayed.
     */
    public FilePanel( File file, MainFrame controller ) {
        filePath = file.getPath();
        this.controller = controller;

        initGUI();
    }

    /**
     * Get the FilePath that this FilePanel represents.  This is NOT a File, but just it's absolute path on disk.
     *
     * @return Get the internal FilePath data.
     */
    public String getFilePath() {
        return filePath;
    }


    /**
     * @return File that matches the internal FilePath data.
     */
    public File getFile() {
        return new File( filePath );
    }


    /** The FileName TextField has been changed, modify the internal FileName data. */
    public void fileNameChanged() {
        filePath = fileName.getText();
    }


    // Initialize the GUI elements of the Panel
    private void initGUI() {
        initWindow();
        initContents();
    }

    // Create the Panel
    private void initWindow() {
        setLayout( null );
        setBounds( 0, 0, PANE_WIDTH, PANE_HEIGHT );

        setMinimumSize( new Dimension( PANE_WIDTH, PANE_HEIGHT ) );
        setMaximumSize( new Dimension( PANE_WIDTH, PANE_HEIGHT ) );
    }

    // Create the contents of the Panel
    private void initContents() {
        initFileNameText();
        initDeleteButton();

        add( fileName );
        add( deleteButton );
    }


    // Setup the delete button
    private void initDeleteButton() {
        //Cache a reference to the ImageIcon so all Instances and reuse it
        loadIcon();

        deleteButton = new JButton( icon );
        deleteButton.setContentAreaFilled( false );

        //Calculate the bounds of the Delete Button
        deleteButton.setBounds(
                TEXT_WIDTH + X_BUFFER, Y_OFFSET,
                BUTTON_WIDTH,
                BUTTON_HEIGHT );

        //Add a callback to the delete button to remove THIS from the fileList
        deleteButton.addActionListener( ( e ) -> controller.removeFile( getFile() ) );
    }

    // Load in, resize and cache a reference to the delete icon image
    private void loadIcon() {
        if ( icon == null )
            icon = new ImageIcon(
                    new ImageIcon( getClass().getResource( "assets/delete.png" ) )
                            .getImage()
                            .getScaledInstance( 30, 30, Image.SCALE_SMOOTH )
            );
    }

    // Setup the FileName JTextField
    private void initFileNameText() {
        fileName = new JTextField( filePath );
        fileName.setFont( new Font( "Arial", Font.PLAIN, 12 ) );
        fileName.setBounds( X_OFFSET, Y_OFFSET, TEXT_WIDTH, TEXT_HEIGHT );

        // Add a listener to monitor for changes to the fileName text
        fileName.getDocument().addDocumentListener( new FileNameChangeListener( this ) );
    }

}
