import javax.swing.*;
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
    private JPanel mainPanel;
    private ArrayList<Thread> threads;
    private ArrayList<File> files;

    public MainFrame() {
        threads = new ArrayList<Thread>(  );
        files = new ArrayList<File>(  );


        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }

        initComponents();

        setSize( 500, 500 );
        setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );
        setVisible( true );
    }

    private void initComponents() {
//        setResizable( false );
        fileChooser = new JFileChooser(  );
        fileChooser.setMultiSelectionEnabled( true );


        GridBagLayout gbl = new GridBagLayout();

        mainPanel = new JPanel( new GridBagLayout() );
        setContentPane( mainPanel );

        final GridBagConstraints c = new GridBagConstraints(  );
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;

        final JPanel subPanel = new JPanel();

        subPanel.setLayout( new BoxLayout( subPanel, BoxLayout.Y_AXIS ) );
        JScrollPane scrollPane = new JScrollPane( subPanel );
        scrollPane.setPreferredSize( new Dimension( 450, 300 ) );
        mainPanel.add( scrollPane, c );


        JButton addFilesButton = new JButton( "Add Files" );
        addFilesButton.setPreferredSize( new Dimension( 150, 50 ) );
        addFilesButton.addActionListener( (ae)-> {
            fileChooser.showDialog( null, "Select" );

            File[] selectedFiles = fileChooser.getSelectedFiles();
            if( selectedFiles.length > 0){
                for( int i = 0; i < selectedFiles.length; i++){
                    files.add( selectedFiles[i] );
                    c.gridx = 0;
                    c.gridy = 4 + i;
                    subPanel.add( new FilePanel( selectedFiles[i] ), c );
                }
                repaint();
                revalidate();
            }
        } );

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        mainPanel.add( addFilesButton, c );


        JButton loadFiles = new JButton( "Load Files" );
        loadFiles.setPreferredSize( new Dimension( 150, 50 ) );
        loadFiles.addActionListener(
                (ae)->
                {
                    if( !files.isEmpty()){
                        for( File f : files){
                            Thread t = new Thread( new FileRunner( f ) );
                            threads.add( t );
                            t.start();
                        }
                    }
                }
        );
        c.gridx = 1;
        c.gridy = 1;
//        c.gridwidth = 2;
        mainPanel.add( loadFiles, c );

    }

    JFileChooser fileChooser;
}
