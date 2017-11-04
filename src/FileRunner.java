import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

/**
 * ${FILE_NAME}.java - Description
 *
 * @author
 * @version 03/Nov/2017
 */
public class FileRunner implements Runnable {
    private int wordCount = 0;
    private int letterCount = 0;
    private String fileContents = "";

    private File file = null;
    private boolean validFile = false;

    public FileRunner( String filePath ) {
        file = new File( filePath );

        if ( file.exists() )
            validFile = true;

        else
            displayError( "Could not load file <" + filePath + ">." );
    }

    @Override
    public void run() {
        if ( validFile && parseFile() )
            initGUI();
    }


    private boolean parseFile() {
        boolean success = true;

        try {
            BufferedReader reader = new BufferedReader( new FileReader( file ) );
            Scanner scanner = new Scanner( reader );
            StringBuilder builder = new StringBuilder();

            while ( scanner.hasNextLine() ) {
                String s = scanner.nextLine();
                builder.append( s );
                builder.append( "\n" );

                Scanner lineScanner = new Scanner( s );
                while ( lineScanner.hasNext() ) {
                    String token = lineScanner.next();

                    // Ensure that the token was not empty
                    if ( token.length() > 0 ) {
                        boolean validWord = false;

                        //Check the Token's characters to see if they are letters
                        for ( char c : token.toCharArray() ) {
                            //Found a letter!
                            if ( c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' ) {
                                letterCount++;
                                validWord = true;
                            }
                        }

                        //The token contains atleast one valid character,
                        //  so it should be counted
                        if ( validWord )
                            wordCount++;
                    }


                }
            }

            fileContents = builder.toString();
            scanner.close();
            reader.close();
        } catch ( Exception e ) {
            displayError( "A fatal error has occurred trying to parse <" + file.getPath() + ">." );
            success = false;
        }

        return success;
    }

    private void initGUI() {
        JFrame fileFrame = new JFrame( file.getName() );
        fileFrame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );

        JPanel filePanel = new JPanel( new GridBagLayout() );
        filePanel.setPreferredSize( new Dimension( 500, 800 ) );

        GridBagConstraints c = new GridBagConstraints();

        JLabel wordCountLabel = new JLabel( "Words: " + wordCount );
        wordCountLabel.setPreferredSize( new Dimension( 100, 50 ) );
        c.gridx = 0;
        c.gridy = 0;
        filePanel.add( wordCountLabel, c );

        JLabel charCountLabel = new JLabel( "Letters: " + letterCount );
        c.gridx = 2;
        c.anchor = GridBagConstraints.EAST;
        filePanel.add( charCountLabel, c );


        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 3;

        JTextArea textArea = new JTextArea();
        textArea.setText( fileContents );

        JScrollPane scrollPane = new JScrollPane( textArea );
        scrollPane.setPreferredSize( new Dimension( 500, 800 ) );

        filePanel.add( scrollPane, c );

        fileFrame.setContentPane( filePanel );
        fileFrame.setSize( 520, 900 );
        fileFrame.setVisible( true );
    }

    private void displayError( String message ) {
        JOptionPane.showMessageDialog( null,
                message,
                "File Load Error",
                JOptionPane.ERROR_MESSAGE );
    }
}
