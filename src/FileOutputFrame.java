import javax.swing.*;
import java.awt.*;

/**
 * FileOutputFrame.java - Description
 *
 * @author Andrew McGuiness
 * @version 11/5/2017
 */
public class FileOutputFrame extends JFrame {
    //TODO: Make the top not look like shit.
    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_HEIGHT = 500;

    private static final int SCROLL_WIDTH = WINDOW_WIDTH - 50;
    private static final int SCROLL_HEIGHT = WINDOW_HEIGHT - 100;

    private static final int LABEL_WIDTH = 100;
    private static final int LABEL_HEIGHT = 50;

    private String fileName;
    private String fileContents;

    private int wordCount;
    private int letterCount;

    public FileOutputFrame( String fileName, String fileContents, int wordCount, int letterCount ) {
        this.fileName = fileName;
        this.fileContents = fileContents;
        this.wordCount = wordCount;
        this.letterCount = letterCount;

        initGUI();
        display();
    }

    private void initGUI() {
        setTitle( fileName );
        setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
        setSize( WINDOW_WIDTH, WINDOW_HEIGHT );


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

    private void display() {
        setVisible( true );
    }
}
