import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * ResultsWriter.java - Description
 *
 * @author Andrew McGuiness
 * @version 11/5/2017
 */
public class ResultsWriter {
    private File results;
    private volatile boolean writing = false;
    private FileWriter resultsWriter;
    private PrintWriter out;


    public ResultsWriter( String filename ) {
        results = new File( filename );
    }

    public boolean write( String text ) {
        boolean success = false;

        if ( !writing ) {
            writing = true;

            try {
                if ( resultsWriter == null )
                    resultsWriter = new FileWriter( results, true );
                if ( out == null )
                    out = new PrintWriter( resultsWriter );

                out.println( text );
                out.flush();

            } catch ( Exception e ) {
                System.out.println( "Something bad happened with the writer" );
            }

            success = true;
            writing = false;
        }

        return success;
    }

    public void cleanUp() {
        try {
            if ( resultsWriter != null )
                resultsWriter.close();
            if ( out != null )
                out.close();
        } catch ( Exception e ) {
            System.out.println( "Something bad happened with the writer" );
        }
    }
}
