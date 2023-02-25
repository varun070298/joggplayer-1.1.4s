/*
 * $Id: FileWatcher.java,v 1.6 2002/08/13 15:17:46 Terry.Dye Exp $
 *
 * FileWatcher.java
 *
 * Created on 26. Juni 2002, 12:12
 */

package org.jconfig;

import org.jconfig.event.FileListener;
import org.jconfig.event.FileListenerEvent;

import java.io.File;

import java.lang.Thread;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;
/**
 * A class which implements an event dispatching mechanism to all
 * classes supporting the FileListener interface. This class will
 * notify all FileListeners when the configuration changes. Once
 * the FileWatcher has been shutdown, the class needs to be
 * reinstanciated and restarted.
 *
 * @author Andreas Mecky <andreas.mecky@xcom.de>
 * @author Terry R. Dye <terry.dye@xcom.de>
 */
public class FileWatcher extends Thread {

    private File file;
    private List fileListenerList;
    private volatile Thread watcher;
    private int interval = 10000;
    private long lastmodified;

    /**
     * Creates a new instance of FileWatcher by calling the FileWatcher( File )
     * Constructor.
     *
     * @param filename A String representing the path to the file to be watched.
     */
    public FileWatcher( String filename ) {
        this( new File( filename ) );
    }

    /**
     * Constructs a FileWatcher watching the specified File
     *
     * @param file The File to be watched
     */
    public FileWatcher( File file ) {
        this.file = file;
        this.lastmodified = file.lastModified();
        this.fileListenerList = new Vector();
    }

    /**
     * Adds FileListener
     *
     * @param fileListener The FileListener
     */
    public void addFileListener( FileListener fileListener ) {
        fileListenerList.add( fileListener );
    }

    /**
     * Set the timer interval. The default is 10 seconds
     *
     * @param seconds The number of seconds to set the interval when
     * to check for the changes to the file.
     */
    public void setInterval( int seconds ) {
        this.interval = seconds*1000;
    }

    /**
     * Tell thread to stop watching. Currently once a Thread is started
     * and stopped, a new FileWatcher will be required.
     */
    public void stopWatching() {
        this.watcher = null;
    }
    
    /**
     * Start the Thread on its journey to scan for changes to the 
     * file it is watching.
     */
    public void start() {
        watcher = new Thread( this );
        watcher.start();
    }

    /** 
     * Start the thread to call checkFile() 
     */
    public void run() {
        Thread thisThread = Thread.currentThread();
        while (thisThread == watcher) {
            try {
                watcher.sleep(interval);
            } catch (InterruptedException e){
                // can't do much from here with Exception
                watcher = null;
            }
            checkFile();
        }
    }
    
    /**
     * Retrieve an array of FileListeners.
     *
     * @return FileListeners as array of FileListener
     */
    public FileListener[] getFileListeners() {
        return (FileListener[])fileListenerList.toArray();
    }
    
    /* allows us to update the File object, in case we need to. */
    /**
     * Sets a new File to be watched. This causes the FileWatcher to watch
     * the given File and disregard the File that was used during Construction.
     *
     * @param file The File to be watched
     */    
    public void setFile( File file ) {
        this.file = file;
    }
    
    /* looks at the internal FileListenerList and keeps track of changed info */
    private void checkFile() {
        File newFile = file;
        if( newFile.lastModified() > lastmodified ) {
            lastmodified = newFile.lastModified();
            Iterator iterator = fileListenerList.iterator();
            while( iterator.hasNext() ) {
                FileListener listener = (FileListener)iterator.next();
                listener.fileChanged( new FileListenerEvent( newFile ) );
            }
        }
    }
    
    /** 
     * Used to test it all
     *
     * @param args None required
     */
    public static void main( String args[] ) {
        new FileWatcher( "config.xml" ).start();
    }
}

/**
 * $Log: FileWatcher.java,v $
 * Revision 1.6  2002/08/13 15:17:46  Terry.Dye
 * no message
 *
 * Revision 1.5  2002/08/07 15:18:17  Andreas.Mecky
 * no message
 *
 * Revision 1.4  2002/07/05 14:25:09  Terry.Dye
 * Daily changes
 *
 * Revision 1.3  2002/07/01 08:13:58  Terry.Dye
 * *** empty log message ***
 *
 * Revision 1.2  2002/06/28 09:53:25  Terry.Dye
 * Example updated to shutdown thread properly/safely.
 *
 * Revision 1.1  2002/06/26 16:12:43  Terry.Dye
 * Created
 *
 */
