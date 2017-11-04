import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.BorderUIResource;
import java.awt.*;
import java.io.File;

/**
 * ${FILE_NAME}.java - Description
 *
 * @author
 * @version 03/Nov/2017
 */
public class FilePanel extends JPanel {
    private static final int PANE_WIDTH = 440;
    private static final int PANE_HEIGHT = 40;

    private static final int BUTTON_WIDTH = 40;
    private static final int BUTTON_HEIGHT = PANE_HEIGHT;

    private static final int TEXT_WIDTH = PANE_WIDTH - BUTTON_WIDTH;
    private static final int TEXT_HEIGHT = PANE_HEIGHT - 10;

    private JButton deleteButton;
    private JTextField fileName;
    private String filePath;
    private MainFrame controller;

    public FilePanel( File file, MainFrame controller ) {
        filePath = file.getPath();
        this.controller = controller;

        initGUI();
    }

    private void initGUI() {
        setLayout( new GridBagLayout() );

        setPreferredSize( new Dimension( PANE_WIDTH, PANE_HEIGHT ) );
        setBorder( BorderUIResource.getEtchedBorderUIResource() );


        fileName = new JTextField( filePath );
        fileName.setMinimumSize( new Dimension( TEXT_WIDTH, TEXT_HEIGHT ) );
        fileName.getDocument().addDocumentListener( new DocumentListener() {
            @Override
            public void insertUpdate( DocumentEvent e ) {
                changedUpdate( e );
            }

            @Override
            public void removeUpdate( DocumentEvent e ) {
                changedUpdate( e );
            }

            @Override
            public void changedUpdate( DocumentEvent e ) {
                filePath = fileName.getText();
                System.out.println( "File name changed to : " + filePath );
            }
        } );


        deleteButton = new JButton( "X" );
        deleteButton.setBackground( Color.RED );
        deleteButton.setPreferredSize( new Dimension( BUTTON_WIDTH, BUTTON_HEIGHT ) );
        deleteButton.addActionListener( (e) -> controller.removeFile( getFile() ) );

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        c.gridy = 0;
        c.gridx = 0;


        add( fileName, c );

        c.anchor = GridBagConstraints.EAST;
        c.gridx = 1;
        add( deleteButton, c );
    }

    public String getFilePath(){
        return filePath;
    }

    public File getFile() {
        return new File( filePath);
    }
}
