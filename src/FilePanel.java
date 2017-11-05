import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.File;

/**
 * ${FILE_NAME}.java - Description
 *
 * @author
 * @version 03/Nov/2017
 */
public class FilePanel extends JPanel {
    private static ImageIcon icon = null;

    private static final int PANE_WIDTH = 450;
    private static final int PANE_HEIGHT = 40;

    private static final int BUTTON_WIDTH = 30;
    private static final int BUTTON_HEIGHT = 30;

    private static final int TEXT_WIDTH = 400;
    private static final int TEXT_HEIGHT = 30;

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
        setLayout( null );
        setBounds( 0, 0, PANE_WIDTH, PANE_HEIGHT );

//        setPreferredSize( new Dimension( PANE_WIDTH, PANE_HEIGHT ) );
        setMaximumSize( new Dimension( PANE_WIDTH, PANE_HEIGHT ) );
        setMinimumSize( new Dimension( PANE_WIDTH, PANE_HEIGHT ) );


        //TODO: Fix the filename font
        fileName = new JTextField( filePath );
        fileName.setBounds( 5, 5, TEXT_WIDTH, TEXT_HEIGHT );
//        fileName.setMinimumSize( new Dimension( TEXT_WIDTH, TEXT_HEIGHT ) );
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

        if( icon == null)
            icon = initDeleteIcon();


        deleteButton = new JButton( icon );
        deleteButton.setContentAreaFilled(false);

        deleteButton.setBounds( TEXT_WIDTH+10, 5, BUTTON_WIDTH, BUTTON_HEIGHT );
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

    private ImageIcon initDeleteIcon() {
        // Create icon
        ImageIcon imageIcon = new ImageIcon(getClass().getResource( "delete.png" )); // load the image to a imageIcon
        Image image = imageIcon.getImage(); // transform it
        Image newimg = image.getScaledInstance(30, 30,  Image.SCALE_SMOOTH); // scale it the smooth way
        imageIcon = new ImageIcon(newimg);  // transform it back
        return imageIcon;
    }

    public String getFilePath(){
        return filePath;
    }

    public File getFile() {
        return new File( filePath);
    }
}
