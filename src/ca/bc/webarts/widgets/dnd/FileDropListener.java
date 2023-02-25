/*
 *  $Source: v:/cvsroot/open/projects/WebARTS/ca/bc/webarts/widgets/dnd/FileDropListener.java,v $
 *  $Name:  $
 *  $Revision: 1.1 $
 *  $Date: 2001/11/01 05:54:45 $
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
 * When using the FileDrop package in its JavaBean form,
 * this listener will receive events when files are dropped
 * onto registered targets.
 *
 *
 * <p>I'm releasing this code into the Public Domain. Enjoy.
 * </p>
 * <p><em>Original author: Robert Harder, rharder@usa.net</em></p>
 *
 * @author  Robert Harder
 * @author  rharder@usa.net
 * @version 1.1
 */
public interface FileDropListener extends java.util.EventListener
{

    /**
     * Fired by the {@link FileDropBean} when files are dropped
     * onto a drop target.
     *
     * @param evt The {@link FileDropEvent} associated with this event
     * @since 1.1
     */
    public abstract void filesDropped( FileDropEvent evt );


}   // end interface FileDropListener

