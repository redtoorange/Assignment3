import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * FileNameChangeListener.java - Description
 *
 * @author Andrew McGuiness
 * @version 11/5/2017
 */
public class FileNameChangeListener implements DocumentListener {
    private FilePanel filePanel;

    public FileNameChangeListener( FilePanel filePanel ){
        this.filePanel = filePanel;
    }

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
        filePanel.fileNameChanged();
    }
}
