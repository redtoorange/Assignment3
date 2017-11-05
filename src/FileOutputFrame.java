import javax.swing.*;
import java.awt.*;

/**
 * FileOutputFrame.java - A special JFrame designed to contain the output from a FileParserThread.  This Window will
 * contain the entire contents of the selected file, the word count and the letter count of the file.
 *
 * @author Andrew McGuiness
 * @version 11/5/2017
 */
public class FileOutputFrame extends JFrame {
    // GUI Constants
    public static final int WINDOW_WIDTH = 500;
    public static final int WINDOW_HEIGHT = 500;

    public static final int SCROLL_WIDTH = 450;
    public static final int SCROLL_HEIGHT = 400;

    public static final int LABEL_WIDTH = 100;
    public static final int LABEL_HEIGHT = 50;

    // A Cached reference to the icon for the JFrame.
    private static ImageIcon imageIcon = null;

    private String fileName;        // Name of the file this Window represents
    private String fileContents;    // Contents of the file

    private int wordCount;          // Number of Words inside the file, using newlines and spaces for delimiting
    private int letterCount;        // NUmber of a-zA-Z letters inside the file

    /**
     * Create a new JFrame window based on input from a FileParserThread
     * @param fileName      Name of the file that was parsed
     * @param fileContents  Contents of the file that was parsed
     * @param wordCount     Words inside the file contents, space and newline delimited
     * @param letterCount   Letter count of the file contents, a-zA-Z
     */
    public FileOutputFrame( String fileName, String fileContents, int wordCount, int letterCount ) {
        this.fileName = fileName;
        this.fileContents = fileContents;
        this.wordCount = wordCount;
        this.letterCount = letterCount;

        initGUI();
        display();
    }


    // Setup the GUI
    private void initGUI() {
        initWindow();
        initComponents();
    }


    //Load in and setup the contents of the Window
    private void initComponents() {
        JPanel filePanel = new JPanel( new GridBagLayout() );
        setContentPane( filePanel );

        GridBagConstraints c = new GridBagConstraints();

        JLabel wordCountLabel = new JLabel( "Words: " + wordCount );
        wordCountLabel.setPreferredSize( new Dimension( LABEL_WIDTH, LABEL_HEIGHT ) );
        c.gridx = 0;
        c.gridy = 0;
        filePanel.add( wordCountLabel, c );

        JLabel charCountLabel = new JLabel( "Letters: " + letterCount );
        wordCountLabel.setPreferredSize( new Dimension( LABEL_WIDTH, LABEL_HEIGHT ) );
        c.gridx = 1;
        c.anchor = GridBagConstraints.EAST;
        filePanel.add( charCountLabel, c );


        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.weighty = 1;

        JTextArea textArea = new JTextArea();
        textArea.setText( fileContents );

        JScrollPane scrollPane = new JScrollPane( textArea );
        scrollPane.setPreferredSize( new Dimension( SCROLL_WIDTH, SCROLL_HEIGHT ) );

        filePanel.add( scrollPane, c );
    }


    // Setup the window
    private void initWindow() {
        setTitle( fileName );
        setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
        setSize( WINDOW_WIDTH, WINDOW_HEIGHT );
        setResizable( false );

        loadIcon();
    }

    // Load in and Cache a reference to the IconImage
    private void loadIcon() {
        if ( imageIcon == null )
            imageIcon = new ImageIcon( getClass().getResource( "assets/note.png" ) );
        setIconImage( imageIcon.getImage() );
    }


    //Display the window
    private void display() {
        setVisible( true );
    }
}
