import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * FileParserThread.java - A Runnable that will parse a single file that was selected by the User.  The file will be
 * checked for existence, then it's contents will be parsed.  Finally, the data parsed from the file will be displayed
 * inside of a new FileOutputFrame.
 *
 * @author Andrew McGuiness
 * @version 03/Nov/2017
 */
public class FileParserThread implements Runnable {
    private int wordCount = 0;
    private int letterCount = 0;
    private String fileContents = "";

    private ResultsWriter resultsWriter;
    private File file = null;
    private boolean validFile = false;

    /**
     * Create a new Runnable Object that will parse the contents of a file and display the results inside of a special
     * Window.
     *
     * @param filePath Path to the file that needs to be parsed.
     */
    public FileParserThread( String filePath, ResultsWriter resultsWriter ) {
        this.resultsWriter = resultsWriter;
        file = new File( filePath );

        if ( file.exists() )
            validFile = true;
        else
            displayError( "Could not load file <" + filePath + ">." );
    }

    /** Attempt to parse the contents of the file, and if successful, display the output inside of a new Window. */
    @Override
    public void run() {
        if ( validFile && parseFile() ){
            String output = Thread.currentThread().getName() +
                    ":\t the file \"" + file.getName() +"\" has " + wordCount + " words and " +
                     + letterCount + " letters.\n";

            // Write to the results file about this threads's completion
            writeResults( output );

            //Avoid Swing Thread errors by offloading the GUI onto the EDT
            SwingUtilities.invokeLater( new Runnable() {
                @Override
                public void run() {
                    new FileOutputFrame( file.getName(), fileContents, wordCount, letterCount );
                }
            } );

        }

    }


    // Attempt to parse the file, catching errors and pushing them to a popup
    private boolean parseFile() {
        boolean success = true;

        try {
            parseLines();
        } catch ( Exception e ) {
            displayError( "A fatal error has occurred trying to parse <" + file.getPath() + ">." );
            success = false;
        }

        return success;
    }

    // Parse the file, line by line.
    private void parseLines() throws IOException {
        BufferedReader reader = new BufferedReader( new FileReader( file ) );
        Scanner scanner = new Scanner( reader );
        StringBuilder builder = new StringBuilder();

        while ( scanner.hasNextLine() ) {
            String line = scanner.nextLine();
            builder.append( line );
            builder.append( "\n" );

            Scanner lineScanner = new Scanner( line );
            while ( lineScanner.hasNext() )
                parseToken( lineScanner.next() );
        }

        fileContents = builder.toString();
        scanner.close();
        reader.close();
    }

    // Parse the next token in the current line
    private void parseToken( String token ) {
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


    // Some error has occurred, notify the user with a popup.
    private void displayError( String message ) {
        JOptionPane.showMessageDialog( null,
                message,
                "File Load Error",
                JOptionPane.ERROR_MESSAGE );
    }

    private void writeResults( String text ){
        boolean written = false;
        while( !written ){
            written = resultsWriter.write( text );
            if( !written ) {
                try {
                    Thread.sleep( 10 );
                } catch ( InterruptedException e ) {
                    e.printStackTrace();
                }
            }
        }
    }
}
