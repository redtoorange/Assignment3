import javax.swing.*;
import javax.swing.plaf.BorderUIResource;
import java.awt.*;
import java.io.File;

/**
 * MainFrame.java - The MainFrame of the Application provides the user with the ability to select files with a
 * FileChooser and view the files in a FileList.  The files can be removed from the FileList with the delete buttons
 * and the exact path to the files can be modified through the TextFields.
 * <p>
 * Once the user has added the files that want, the load files button will launch a separate Thread to parse each file
 * in parallel.  The contents of each file will be output and displayed in their own Windows.
 *
 * @author Andrew McGuiness
 * @version 11/5/2017
 */
public class MainFrame extends JFrame {
    // GUI Constants
    public static final int WINDOW_WIDTH = 500;
    public static final int WINDOW_HEIGHT = 500;

    public static final int SCROLL_WIDTH = 450;
    public static final int SCROLL_HEIGHT = 350;

    public static final int BUTTON_WIDTH = 150;
    public static final int BUTTON_HEIGHT = 40;


    private ResultsWriter resultsWriter;
    private JPanel fileList;    // Container for all FilePanels
    private int fileCount = 0;  // Number of files that have been select


    /** Initialize the GUI and then display the primary window. */
    public MainFrame() {
        resultsWriter = new ResultsWriter( "results.txt" );

        initGUI();
        display();
        resultsWriter.cleanUp();
    }


    // Setup the Main Window and the Components
    private void initGUI() {
        initWindow();
        initComponents();
    }


    // Setup the primary JFrame
    private void initWindow() {
        setTitle( "Multi-threaded File Parser" );
        setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
        setSize( WINDOW_WIDTH, WINDOW_HEIGHT );
        setResizable( false );

        setLookAndFeel();
        loadIcon();
    }


    // Load in the file for the Window
    private void loadIcon() {
        ImageIcon img = new ImageIcon( getClass().getResource( "assets/folder.png" ) );
        setIconImage( img.getImage() );
    }


    // Show the Window
    private void display() {
        setVisible( true );
    }


    // Attempt to set the LookAndFeel to something less horrid
    private void setLookAndFeel() {
        try {
            for ( UIManager.LookAndFeelInfo l : UIManager.getInstalledLookAndFeels() ) {
                if ( l.getName().equals( "Nimbus" ) ) {
                    UIManager.setLookAndFeel( l.getClassName() );
                    break;
                }
            }
        } catch ( Exception e ) {
            System.err.println( "Failed to load Nimbus look and feel." );
        }
    }


    // Initialize the Buttons and the FileList JPanel to hold the fileList
    private void initComponents() {
        JPanel mainPanel = new JPanel( new GridBagLayout() );
        setContentPane( mainPanel );

        // Create a label for the File List
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.ipady = 15;
        c.anchor = GridBagConstraints.WEST;
        mainPanel.add( new JLabel( "Selected Files:" ), c );


        // Create the JPanel that will hold all the created FilePanels
        fileList = new JPanel();
        fileList.setPreferredSize( new Dimension( FilePanel.PANE_WIDTH, FilePanel.PANE_HEIGHT ) );
        fileList.setLayout( new BoxLayout( fileList, BoxLayout.Y_AXIS ) );


        // Create a ScrollPane to hold the FileList
        JScrollPane scrollPane = new JScrollPane( fileList );
        scrollPane.setPreferredSize( new Dimension( SCROLL_WIDTH, SCROLL_HEIGHT ) );
        scrollPane.setBorder( BorderUIResource.getLoweredBevelBorderUIResource() );
        scrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
        c.gridy = 1;
        c.gridwidth = 2;
        mainPanel.add( scrollPane, c );


        // Open up the file chooser and let the user select files to load.
        //  if the file is already present, it will be ignored.
        JButton addFilesButton = new JButton( "Add Files" );
        addFilesButton.setPreferredSize( new Dimension( BUTTON_WIDTH, BUTTON_HEIGHT ) );
        addFilesButton.addActionListener( ( e ) -> selectFiles() );
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        mainPanel.add( addFilesButton, c );


        // The launch button will create and start all the Threads, one for each
        //  filePanel within the fileList.
        JButton loadFiles = new JButton( "Load Files" );
        loadFiles.setPreferredSize( new Dimension( BUTTON_WIDTH, BUTTON_HEIGHT ) );
        loadFiles.addActionListener( ( e ) -> launchFileThreads() );
        c.gridx = 1;
        c.anchor = GridBagConstraints.EAST;
        mainPanel.add( loadFiles, c );
    }

    // Attempt to add a file to the fileList
    private void addFile( File file ) {
        int position = locateFile( file );  //See if the file is already there

        // If the position is -1, then the File wasn't found
        if ( position < 0 ) {
            // Add the filePanel to the fileList JPanel
            fileList.add( new FilePanel( file, this ) );
            fileCount++;

            // Resize the panel
            fileList.setPreferredSize( new Dimension( FilePanel.PANE_WIDTH, FilePanel.PANE_HEIGHT * fileCount ) );

            repaint();
            revalidate();
        }
    }

    // Attempt to remove a file from the fileList
    public void removeFile( File file ) {
        int position = locateFile( file );  //Find it

        // If position is -1, the file wasn't found
        if ( position >= 0 ) {
            // Remove it from the panel
            fileList.remove( position );
            fileCount--;

            // Resize the panel
            fileList.setPreferredSize( new Dimension( FilePanel.PANE_WIDTH, FilePanel.PANE_HEIGHT * fileCount ) );

            repaint();
            revalidate();
        }
    }

    // Find the position of a file within the fileList
    private int locateFile( File file ) {
        int pos = -1;   // If a file wasn't found, it's position is -1

        Component[] components = fileList.getComponents();
        for ( int i = 0; i < components.length; i++ ) {
            if ( components[i] instanceof FilePanel ) {
                FilePanel fp = ( FilePanel ) components[i];

                // Found the file, set pos to it's index and break out
                if ( fp.getFilePath().equals( file.getPath() ) ) {
                    pos = i;
                    break;
                }
            }
        }

        return pos;
    }


    // Launch a FileParserThread for each selected file.
    private void launchFileThreads() {
        Component[] components = fileList.getComponents();
        for ( int i = 0; i < components.length; i++ ) {
            if ( components[i] instanceof FilePanel ) {
                //Get the FileName from the FilePanel and create a FileParserThread with it
                FilePanel fp = ( FilePanel ) components[i];
                Thread t = new Thread( new FileParserThread( fp.getFilePath(), resultsWriter ) );
                t.start();
            }
        }
    }


    // Launch the file chooser and allow the user to pick some files
    private void selectFiles() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle( "Select Files to add" );
        fileChooser.setMultiSelectionEnabled( true );

        fileChooser.showDialog( null, "Add File(s)" );

        // Attempt to add the selected files
        for ( File nextFile : fileChooser.getSelectedFiles() )
            addFile( nextFile );
    }




}
