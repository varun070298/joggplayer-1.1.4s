/*
 * $Id: FileListener.java,v 1.1 2002/06/26 16:12:43 Terry.Dye Exp $
 *
 * FileListener.java
 *
 * Created on 26. Juni 2002, 11:53
 */

package org.jconfig.event;

import java.util.EventListener;
/**
 *
 *
 * @author  Terry R. Dye
 */
public interface FileListener extends EventListener {
    /** This method, once implemented, will be called when the File object
     * itself changes.
     *
     * @param FileListener The FileListener object.
     */
    public void fileChanged( FileListenerEvent e );
}

/**
 * $Log: FileListener.java,v $
 * Revision 1.1  2002/06/26 16:12:43  Terry.Dye
 * Created
 *
 */
