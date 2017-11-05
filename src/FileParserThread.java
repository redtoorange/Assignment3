import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * FileParserThread.java - Description
 *
 * @author  Andrew McGuiness
 * @version 03/Nov/2017
 */
public class FileParserThread implements Runnable {
    private int wordCount = 0;
    private int letterCount = 0;
    private String fileContents = "";

    private File file = null;
    private boolean validFile = false;

    public FileParserThread( String filePath ) {
        file = new File( filePath );

        if ( file.exists() )
            validFile = true;

        else
            displayError( "Could not load file <" + filePath + ">." );
    }

    @Override
    public void run() {
        if ( validFile && parseFile() )
            new FileOutputFrame( file.getName(), fileContents, wordCount, letterCount );
    }


    private boolean parseFile() {
        boolean success = true;

        try {
            extractContents();
        } catch ( Exception e ) {
            displayError( "A fatal error has occurred trying to parse <" + file.getPath() + ">." );
            success = false;
        }

        return success;
    }

    private void extractContents() throws IOException {
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


    private void displayError( String message ) {
        JOptionPane.showMessageDialog( null,
                message,
                "File Load Error",
                JOptionPane.ERROR_MESSAGE );
    }
}
