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
   $Log: WorkspaceEditorFactory.java,v $
   Revision 1.4  2003/01/19 09:50:54  markl
   Javadoc & comment header updates.

   Revision 1.3  2001/03/12 09:28:02  markl
   Source code and Javadoc cleanup.

   Revision 1.2  1999/01/10 03:05:32  markl
   added GPL header & RCS tag
   ----------------------------------------------------------------------------
*/

package kiwi.ui;

/** This interface represents a factory that creates appropriate
  * <code>WorkspaceEditor</code>s for specified objects and classes.
  *
  * @author Mark Lindner
  */

public interface WorkspaceEditorFactory 
  {

  /** General-purpose factory method for <code>WorkspaceEditor</code>s. Returns
    * a <code>WorkspaceEditor</code> instance that is appropriate for editing
    * the specified object.
    *
    * @param obj The object to edit.
    * @exception NoAppropriateEditorException If a suitable editor could not be
    * found for the object.
    * @see #getEditorForType
    */

  public WorkspaceEditor getEditorForObject(Object obj)
    throws NoAppropriateEditorException;

  /** General-purpose factory method for <code>WorkspaceEditor</code>s. Returns
    * a <code>WorkspaceEditor</code> instance that is appropriate for editing
    * the specified object.
    *
    * @param obj The object to edit.
    * @exception NoAppropriateEditorException If a suitable editor could not be
    * found for the class.
    * @see #getEditorForObject
    */

  public WorkspaceEditor getEditorForType(Class clazz)
    throws NoAppropriateEditorException;
  }

/* end of source file */
