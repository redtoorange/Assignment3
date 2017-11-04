import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * ${FILE_NAME}.java - Description
 *
 * @author
 * @version 03/Nov/2017
 */
public class FilePanel extends JPanel {
    private JButton deleteButton;
    private JTextField fileName;
    private File file;

    public FilePanel(File file ){
        setMinimumSize( new Dimension( 300, 25 ) );
//        setPreferredSize( new Dimension( 300, 25 ) );
        this.file = file;
        fileName = new JTextField( file.getPath() );
        fileName.setMinimumSize( new Dimension( 250, 25 ) );
        deleteButton = new JButton( "X" );
        deleteButton.setBackground( Color.RED );
        deleteButton.setMinimumSize( new Dimension( 25, 25 ) );
        add( fileName );
        add( deleteButton );
        System.out.println( "Should have added a file panel" );
    }

}
