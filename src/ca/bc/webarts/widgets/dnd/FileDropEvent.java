/*
 *  $Source: v:/cvsroot/open/projects/WebARTS/ca/bc/webarts/widgets/dnd/FileDropEvent.java,v $
 *  $Name:  $
 *  $Revision: 1.1 $
 *  $Date: 2001/11/01 05:54:40 $
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

/**
 * This is the event that is passed to the
 * {@link FileDropListener#filesDropped filesDropped(...)} method in
 * your {@link FileDropListener} when files are dropped onto
 * a registered drop target.
 *
 * <p>I'm releasing this code into the Public Domain. Enjoy.
 * </p>
 * <p><em>Original author: Robert Harder, rharder@usa.net</em></p>
 *
 * @author  Robert Harder
 * @author  rharder@usa.net
 * @version 1.1
 */
public class FileDropEvent extends java.util.EventObject
{

    private java.io.File[] files;

    /**
     * Constructs a {@link FileDropEvent} with the array
     * of files that were dropped and the
     * {@link FileDropBean} that initiated the event.
     *
     * @param files The array of files that were dropped
     * @source The event source
     * @since 1.1
     */
    public FileDropEvent( java.io.File[] files, Object source )
    {   super( source );
        this.files = files;
    }   // end constructor

    /**
     * Returns an array of files that were dropped on a
     * registered drop target.
     *
     * @return array of files that were dropped
     * @since 1.1
     */
    public java.io.File[] getFiles()
    {   return files;
    }   // end getFiles

}   // end class FileDropEvent

