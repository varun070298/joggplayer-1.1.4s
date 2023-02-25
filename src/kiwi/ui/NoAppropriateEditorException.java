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
   $Log: NoAppropriateEditorException.java,v $
   Revision 1.4  2003/01/19 09:50:53  markl
   Javadoc & comment header updates.

   Revision 1.3  2001/03/12 09:27:58  markl
   Source code and Javadoc cleanup.

   Revision 1.2  1999/01/10 02:56:27  markl
   added GPL header & RCS tag
   ----------------------------------------------------------------------------
*/

package kiwi.ui;

/** This exception is thrown by MDI-related classes such as
  * <code>WorkspaceManager</code> when an appropriate editor for a given class
  * or object cannot be created.
  *
  * @see kiwi.ui.WorkspaceEditorFactory
  *
  * @author Mark Lindner
  */

public class NoAppropriateEditorException extends Exception
  {
  private Class clazz;

  /** Construct a new <code>NoAppropriateEditorException</code>.
    *
    * @param msg The message.
    * @param clazz The class object associated with the requested editor.
    */

  public NoAppropriateEditorException(String msg, Class clazz)
    {
    super(msg);
    this.clazz = clazz;
    }

  /** Get the class object associated with the requested editor. */

  public Class getObjectType()
    {
    return(clazz);
    }

  }

/* end of source file */
