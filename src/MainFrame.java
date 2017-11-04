import javax.swing.*;
import javax.swing.border.BevelBorder;
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
    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_HEIGHT = 500;

    private static final int SCROLL_WIDTH = WINDOW_WIDTH - 50;
    private static final int SCROLL_HEIGHT = WINDOW_HEIGHT - 150;

    private static final int BUTTON_WIDTH = 150;
    private static final int BUTTON_HEIGHT = 40;
    JFileChooser fileChooser;
    private JPanel mainPanel;
    private ArrayList< Thread > threads;
    private ArrayList< File > files;
    private int fileCount = 0;

    public MainFrame() {
        threads = new ArrayList< Thread >();
        files = new ArrayList< File >();


        try {
            for ( UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels() ) {
                if ( "Nimbus".equals( info.getName() ) ) {
                    UIManager.setLookAndFeel( info.getClassName() );
                    break;
                }
            }
        } catch ( Exception e ) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }

        initComponents();

        setSize( WINDOW_WIDTH, WINDOW_HEIGHT );
        setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
        setVisible( true );
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

        final JPanel subPanel = new JPanel();

        GridBagLayout gbl = new GridBagLayout();


        subPanel.setLayout( gbl );


        JScrollPane scrollPane = new JScrollPane( subPanel );
        scrollPane.setPreferredSize( new Dimension( SCROLL_WIDTH, SCROLL_HEIGHT ) );
        scrollPane.setBorder( new BorderUIResource.BevelBorderUIResource( BevelBorder.LOWERED ) );


        mainPanel.add( scrollPane, c );


        JButton addFilesButton = new JButton( "Add Files" );
        addFilesButton.setPreferredSize( new Dimension( BUTTON_WIDTH, BUTTON_HEIGHT ) );
        addFilesButton.addActionListener( ( ae ) -> {
            fileChooser.showDialog( null, "Select" );

            File[] selectedFiles = fileChooser.getSelectedFiles();
            if ( selectedFiles.length > 0 ) {
                int start = fileCount;

                for ( int i = 0; i < selectedFiles.length; i++ ) {
                    if ( !files.contains( selectedFiles[i] ) ) {
                        files.add( selectedFiles[i] );
                        fileCount++;
                    }
                }

                GridBagConstraints sc = new GridBagConstraints();
                sc.gridx = 0;


                for ( int i = start; i < fileCount; i++ ) {
                    sc.gridy = i;
                    subPanel.add( new FilePanel( files.get( i ) ), sc );
                }

                repaint();
                revalidate();
            }
        } );

        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        mainPanel.add( addFilesButton, c );


        JButton loadFiles = new JButton( "Load Files" );
        loadFiles.setPreferredSize( new Dimension( BUTTON_WIDTH, BUTTON_HEIGHT ) );
        loadFiles.addActionListener(
                ( ae ) ->
                {
                    if ( !files.isEmpty() ) {
                        for ( File f : files ) {
                            Thread t = new Thread( new FileRunner( f ) );
                            threads.add( t );
                            t.start();
                        }
                    }
                }
        );
        c.gridx = 1;
        c.gridy = 2;
        c.anchor = GridBagConstraints.EAST;
        mainPanel.add( loadFiles, c );

    }
}
