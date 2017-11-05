import javax.swing.*;
import javax.swing.plaf.BorderUIResource;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
/*
 * Created by JFormDesigner on Wed Nov 01 14:05:07 EDT 2017
 */


/**
 * @author unknown
 */
public class MainFrame extends JFrame {
    public static final int WINDOW_WIDTH = 500;
    public static final int WINDOW_HEIGHT = 500;

    public static final int SCROLL_WIDTH = 450;
    public static final int SCROLL_HEIGHT = 350;

    public static final int BUTTON_WIDTH = 150;
    public static final int BUTTON_HEIGHT = 40;


    private JFileChooser fileChooser;
    private JPanel mainPanel;
    private JPanel fileList;

    private ArrayList< Thread > threads;

    private int fileCount = 0;

    public MainFrame() {
        threads = new ArrayList< Thread >();

        initGUI();
        display();
    }

    private void initGUI() {
        setTitle( "Multi-threaded File Parser" );
        setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
        setSize( WINDOW_WIDTH, WINDOW_HEIGHT );
        setResizable( false );

        initLookAndFeel();
        loadIcon();
        initComponents();
    }

    private void loadIcon() {
        ImageIcon img = new ImageIcon( getClass().getResource( "folder.png" ) );
        setIconImage( img.getImage() );
    }

    private void display() {
        setVisible( true );
    }

    private void initLookAndFeel() {
        try {
            for ( UIManager.LookAndFeelInfo l : UIManager.getInstalledLookAndFeels() ) {
                System.out.println( l.getName() );
                if ( l.getName().equals( "Nimbus" ) ) {
                    UIManager.setLookAndFeel( l.getClassName() );
                }
            }
        } catch ( Exception e ) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }
    }

    private void initComponents() {
        fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled( true );

        mainPanel = new JPanel( new GridBagLayout() );
        setContentPane( mainPanel );

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.ipady = 15;
        c.anchor = GridBagConstraints.WEST;
        mainPanel.add( new JLabel( "Selected Files:" ), c );


        c.gridy = 1;
        c.gridwidth = 2;

        fileList = new JPanel();
        fileList.setPreferredSize( new Dimension( FilePanel.PANE_WIDTH, FilePanel.PANE_HEIGHT ) );


        BoxLayout box = new BoxLayout( fileList, BoxLayout.Y_AXIS );
        fileList.setLayout( box );


        JScrollPane scrollPane = new JScrollPane( fileList );
        scrollPane.setPreferredSize( new Dimension( SCROLL_WIDTH, SCROLL_HEIGHT ) );
        scrollPane.setBorder( BorderUIResource.getLoweredBevelBorderUIResource() );
        scrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
        mainPanel.add( scrollPane, c );


        JButton addFilesButton = new JButton( "Add Files" );
        addFilesButton.setPreferredSize( new Dimension( BUTTON_WIDTH, BUTTON_HEIGHT ) );
        addFilesButton.addActionListener( ( ae ) -> {
            fileChooser.showDialog( null, "Select" );

            File[] selectedFiles = fileChooser.getSelectedFiles();

            if ( selectedFiles.length > 0 ) {
                for ( int i = 0; i < selectedFiles.length; i++ )
                    addFile( selectedFiles[i] );
            }
        } );

        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        mainPanel.add( addFilesButton, c );


        JButton loadFiles = new JButton( "Load Files" );
        loadFiles.setPreferredSize( new Dimension( BUTTON_WIDTH, BUTTON_HEIGHT ) );
        loadFiles.addActionListener( ( e ) -> launchThreads() );
        c.gridx = 1;
        c.gridy = 2;
        c.anchor = GridBagConstraints.EAST;
        mainPanel.add( loadFiles, c );
    }


    private void addFile( File file ) {
        int position = locateFile( file );

        if ( position < 0 ) {
            fileList.add( new FilePanel( file, this ) );
            fileCount++;

            fileList.setPreferredSize( new Dimension( FilePanel.PANE_WIDTH, FilePanel.PANE_HEIGHT * fileCount ) );

            repaint();
            revalidate();
        }
    }

    public void removeFile( File file ) {
        int position = locateFile( file );

        if ( position >= 0 ) {
            fileList.remove( position );
            fileCount--;

            fileList.setPreferredSize( new Dimension( FilePanel.PANE_WIDTH, FilePanel.PANE_HEIGHT * fileCount ) );

            repaint();
            revalidate();
        }
    }

    private int locateFile( File file ) {
        int pos = -1;

        Component[] components = fileList.getComponents();
        for ( int i = 0; i < components.length; i++ ) {
            if ( components[i] instanceof FilePanel ) {
                FilePanel fp = ( FilePanel ) components[i];
                if ( fp.getFilePath().equals( file.getPath() ) ) {
                    pos = i;
                    break;
                }
            }
        }

        return pos;
    }


    private void launchThreads() {
        Component[] components = fileList.getComponents();
        for ( int i = 0; i < components.length; i++ ) {
            if ( components[i] instanceof FilePanel ) {
                FilePanel fp = ( FilePanel ) components[i];
                Thread t = new Thread( new FileParserThread( fp.getFilePath() ) );
                t.start();
            }
        }
    }
}
