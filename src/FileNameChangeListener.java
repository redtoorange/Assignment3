import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * FileNameChangeListener.java - This listener it attached to a TextField of a FilePanel.  If the user modifies the text
 * inside of the TextField, the FilePanel will be notified.  This allows the user to modify the fileName of a FilePanel
 * manually.
 *
 * @author Andrew McGuiness
 * @version 11/5/2017
 */
public class FileNameChangeListener implements DocumentListener {
    private FilePanel controller;

    /**
     * Create a new FileNameChangeListener to monitor a TextField.  When the TextField is changed, the FilePanel will
     * be notified.
     *
     * @param controller FilePanel that should be notified of changes.
     */
    public FileNameChangeListener( FilePanel controller ) {
        this.controller = controller;
    }

    @Override
    public void insertUpdate( DocumentEvent e ) {
        changedUpdate( e );
    }

    @Override
    public void removeUpdate( DocumentEvent e ) {
        changedUpdate( e );
    }

    /**
     * The text inside of the observed TextField has been changed, so notify the FilePanel
     * @param e Not Used
     */
    @Override
    public void changedUpdate( DocumentEvent e ) {
        controller.fileNameChanged();
    }
}
