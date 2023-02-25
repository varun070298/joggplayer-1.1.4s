/*
 * $Id: FileListenerEvent.java,v 1.1 2002/06/26 16:12:43 Terry.Dye Exp $
 *
 * FileListenerEvent.java
 *
 * Created on 26. Juni 2002, 12:01
 */

package org.jconfig.event;

import java.io.File;
/**
 * The <code>FileListenerEvent</code> is the event that is delivered to
 * anyone who who implements the DirectoryListener interface. It is a simple
 * class to provide access to the File object when the File object changes.
 *
 * @author  Terry R. Dye
 */
public class FileListenerEvent {

    private File file;

    /**
     * Creates a new instance of FileListenerEvent
     */
    public FileListenerEvent(File file) {
        this.file = file;
    }

    /**
     * Return the File that triggered this event.
     */
    public File getFile() {
        return this.file;
    }
}

/**
 * $Log: FileListenerEvent.java,v $
 * Revision 1.1  2002/06/26 16:12:43  Terry.Dye
 * Created
 *
 */
