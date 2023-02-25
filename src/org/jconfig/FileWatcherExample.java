/*
 * $Id: FileWatcherExample.java,v 1.3 2002/07/05 14:25:09 Terry.Dye Exp $
 *
 * FileWatcherExample.java
 *
 * Created on 26. Juni 2002, 12:31
 */

package org.jconfig;

import java.lang.Thread;

import org.jconfig.event.FileListener;
import org.jconfig.event.FileListenerEvent;
/**
 *
 * @author  Terry R. Dye
 */
public class FileWatcherExample {

    /** Creates a new instance of FileWatcherExample */
    public FileWatcherExample() {
    }

    public static void main( String args[] ) throws Exception {
        FileWatcher fileWatcher = new FileWatcher( "config.xml" );
        fileWatcher.start();
        fileWatcher.addFileListener( new DoSomething() );

        FileWatcher fileWatcher2 = new FileWatcher( "config2.xml" );
        fileWatcher2.start();
        fileWatcher2.addFileListener( new DoSomethingElse() );
    }
    
    public void run() {
        int i = 10;
        while( i-- > 0 ) {
            System.out.println("waiting");
            try {
                Thread.sleep( 10000 );
            } catch( Exception e ) {
                return;
            }
        }
        System.out.println("done waiting");
    }
}

class DoSomething implements FileListener {

    /**
     * my little test
     */
    public DoSomething() {}

    /** This method, once implemented, will be called when the File object
     * itself changes.
     *
     * @param FileListener The FileListener object.
     */
    public void fileChanged( FileListenerEvent e ) {
        System.out.println( "File changed: " + e );
    }
}

class DoSomethingElse implements FileListener {

    /**
     * my little test
     */
    public DoSomethingElse() {}

    /** This method, once implemented, will be called when the File object
     * itself changes.
     *
     * @param FileListener The FileListener object.
     */
    public void fileChanged( FileListenerEvent e ) {
        System.out.println( "Something else changed: " + e );
    }
}
/**
 * $Log: FileWatcherExample.java,v $
 * Revision 1.3  2002/07/05 14:25:09  Terry.Dye
 * Daily changes
 *
 * Revision 1.2  2002/06/28 09:53:25  Terry.Dye
 * Example updated to shutdown thread properly/safely.
 *
 * Revision 1.1  2002/06/26 16:12:43  Terry.Dye
 * Created
 *
 */
