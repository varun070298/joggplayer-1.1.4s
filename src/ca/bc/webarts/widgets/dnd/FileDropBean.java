/*
 *  $Source: v:/cvsroot/open/projects/WebARTS/ca/bc/webarts/widgets/dnd/FileDropBean.java,v $
 *  $Name:  $
 *  $Revision: 1.1 $
 *  $Date: 2001/11/01 05:54:32 $
 *  $Locker:  $
 */
/*
 *  Copyright (C) 2001 WebARTS Design, North Vancouver Canada
 *  http://www..webarts.bc.ca
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package ca.bc.webarts.widgets.dnd;

import java.beans.*;

/**
 * If you wish to use the FileDrop package as a JavaBean, this class will act as
 * an interface to the {@link FileDrop} class that handles all the dirty work.
 * After instantiating the bean, add components as drop targets using the
 * {@link #addFileDropTarget addFileDropTarget(...)} method. If the component
 * is a {@link java.awt.Container}, then all elements contained within will be
 * marked as a drop target as well.
 * Using the {@link FileDrop} technique manually in your code will give you more options.
 *
 * <p>I'm releasing this code into the Public Domain. Enjoy.
 * </p>
 * <p><em>Original author: Robert Harder, rharder@usa.net</em></p>
 *
 * @author  Robert Harder
 * @author  rharder@usa.net
 * @version 1.1
 */
public class FileDropBean
implements java.io.Serializable
{

    private javax.swing.event.EventListenerList listenerList = new javax.swing.event.EventListenerList();

    /** Creates new FileDropBean */
    public FileDropBean()
    {
    }

    /**
     * Registers a component as a drop target.
     * If the component is a container, then all elements contained
     * within will also be registered as drop targets, though only
     * the outer container will change borders during a drag and drop
     * operation (and even then, only if the container is a Swing component).
     *
     * @param comp The component to register as a drop target
     * @since 1.1
     */
    public void addFileDropTarget( java.awt.Component comp )
    {
        FileDrop.Listener listener = new FileDrop.Listener()
        {   public void filesDropped( java.io.File[] files )
            {   fireFileDropHappened( files );
            }   // end filesDropped
        };  // end listener
        boolean recursive = true;
        new FileDrop( comp, recursive, listener );
    }   // end newDropTarget


    /**
     * Unregisters a component as a drop target.
     *
     * @param comp The component to unregister
     * @since 1.1
     */
    public boolean removeFileDropTarget( java.awt.Component comp )
    {   return FileDrop.remove( comp );
    }   // end removeFileDropTarget


    /**
     * Register a listener for {@link FileDropEvent}s.
     *
     * @param listener The listener to register
     * @since 1.1
     */
    public void addFileDropListener (FileDropListener listener)
    {   listenerList.add( FileDropListener.class, listener );
    }   // end addFileDropListener



    /**
     * Unregister a listener for {@link FileDropEvent}s.
     *
     * @param listener The listener to unregister
     * @since 1.1
     */
    public void removeFileDropListener (FileDropListener listener)
    {   listenerList.remove( FileDropListener.class, listener );
    }   // end addFileDropListener


    /**
     * Fires a {@link FileDropEvent} with the given non-null
     * list of dropped files.
     *
     * @param files The files that were dropped
     * @since 1.1
     */
    protected void fireFileDropHappened( java.io.File[] files )
    {
        FileDropEvent evt = new FileDropEvent( files, this );

        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();

        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length-2; i>=0; i-=2)
        {   if (listeners[i]==FileDropListener.class)
            {   ((FileDropListener)listeners[i+1]).filesDropped(evt);
            }   // end if: correct listener type
        }   // end for: each listener
    }   // end fireFileDropHappened


}   // end clas FileDropBean

