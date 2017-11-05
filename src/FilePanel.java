import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * FilePanel.java - Description
 *
 * @author  Andrew McGuiness
 * @version 03/Nov/2017
 */
public class FilePanel extends JPanel {
    public static final int PANE_WIDTH = 440;
    public static final int PANE_HEIGHT = 40;
    public static final int BUTTON_WIDTH = 30;
    public static final int BUTTON_HEIGHT = 30;
    public static final int TEXT_WIDTH = 390;
    public static final int TEXT_HEIGHT = 30;

    private static final int Y_OFFSET = 5;
    private static final int X_OFFSET = 5;
    private static final int X_BUFFER = 10;

    private static ImageIcon icon = null;

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

        setMinimumSize( new Dimension( PANE_WIDTH, PANE_HEIGHT ) );
        setMaximumSize( new Dimension( PANE_WIDTH, PANE_HEIGHT ) );

        initFileNameText();
        initDeleteButton();

        add( fileName );
        add( deleteButton );
    }

    private void initDeleteButton() {
        //Cache a reference to the ImageIcon so all Instances and reuse it
        if ( icon == null )
            icon = initDeleteIcon();

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

    private void initFileNameText() {
        fileName = new JTextField( filePath );
        fileName.setFont( new Font( "Arial", Font.PLAIN, 12 ));
        fileName.setBounds( X_OFFSET, Y_OFFSET, TEXT_WIDTH, TEXT_HEIGHT );
        fileName.getDocument().addDocumentListener( new FileNameChangeListener( this) );
    }

    private ImageIcon initDeleteIcon() {
        return new ImageIcon(
                new ImageIcon( getClass().getResource( "delete.png" ) )
                        .getImage()
                        .getScaledInstance( 30, 30, Image.SCALE_SMOOTH )
        );
    }

    public String getFilePath() {
        return filePath;
    }

    public File getFile() {
        return new File( filePath );
    }

    public void fileNameChanged(){
        filePath = fileName.getText();
    }
}
