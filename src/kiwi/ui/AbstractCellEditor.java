/* ----------------------------------------------------------------------------
   The Kiwi Toolkit
   Copyright (C) 1998-2003 Mark A. Lindner

   This file is part of Kiwi.
   
   This library is free software; you can redistribute it and/or
   modify it under the terms of the GNU Library General Public
   License as published by the Free Software Foundation; either
   version 2 of the License, or (at your option) any later version.

   This library is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   Library General Public License for more details.

   You should have received a copy of the GNU Library General Public
   License along with this library; if not, write to the Free
   Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 
   The author may be contacted at:
   
   mark_a_lindner@yahoo.com
   ----------------------------------------------------------------------------
   $Log: AbstractCellEditor.java,v $
   Revision 1.5  2003/01/19 09:50:52  markl
   Javadoc & comment header updates.

   Revision 1.4  2001/03/12 09:27:51  markl
   Source code and Javadoc cleanup.

   Revision 1.3  2000/10/11 10:45:27  markl
   Cosmetic changes.

   Revision 1.2  1999/01/10 01:00:58  markl
   added GPL header & RCS tag
   ----------------------------------------------------------------------------
*/

package kiwi.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.EventObject;
import java.io.Serializable;
import javax.swing.*;
import javax.swing.event.*;

/**
  * A base class for CellEditors, providing default implementations for all
  * methods in the CellEditor interface and support for managing a series
  * of listeners.
  *
  * @see javax.swing.CellEditor
  *
  * @author Philip Milne
  */

public class AbstractCellEditor implements CellEditor
  {
  protected EventListenerList listenerList = new EventListenerList();

  public Object getCellEditorValue()
    {
    return null;
    }

  public boolean isCellEditable(EventObject evt)
    {
    return(true);
    }

  public boolean shouldSelectCell(EventObject evt)
    {
    return(false);
    }

  public boolean stopCellEditing()
    {
    return(true);
    }

  public void cancelCellEditing()
    {
    }

  public void addCellEditorListener(CellEditorListener listener)
    {
    listenerList.add(CellEditorListener.class, listener);
    }

  public void removeCellEditorListener(CellEditorListener listener)
    {
    listenerList.remove(CellEditorListener.class, listener);
    }

  /**
    * Notify all listeners that have registered interest for notification on
    * this event type.
    *
    * @see EventListenerList
    */

  protected void fireEditingStopped()
    {
    // Guaranteed to return a non-null array

    Object[] listeners = listenerList.getListenerList();

    // Process the listeners last to first, notifying
    // those that are interested in this event

    for(int i = listeners.length - 2; i >= 0; i -= 2)
      {
      if(listeners[i] == CellEditorListener.class)
        {
	((CellEditorListener)listeners[i + 1])
          .editingStopped(new ChangeEvent(this));
	}
      }
    }

  /**
    * Notify all listeners that have registered interest for notification on
    * this event type.
    *
    * @see EventListenerList
    */

  protected void fireEditingCanceled()
    {
    // Guaranteed to return a non-null array
    Object[] listeners = listenerList.getListenerList();

    // Process the listeners last to first, notifying
    // those that are interested in this event
    for(int i = listeners.length - 2; i >= 0; i -= 2)
      if(listeners[i] == CellEditorListener.class)
	((CellEditorListener)listeners[i + 1])
          .editingCanceled(new ChangeEvent(this));
    }
  
  }

/* end of source file */
