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
   $Log: UIElement.java,v $
   Revision 1.3  2003/01/19 09:50:54  markl
   Javadoc & comment header updates.

   Revision 1.2  2001/03/12 09:28:01  markl
   Source code and Javadoc cleanup.

   Revision 1.1  1999/05/10 09:08:02  markl
   Initial revision
   ----------------------------------------------------------------------------
*/

package kiwi.ui;

/** This class represents a user interface element, such as a texture, icon,
 * or audio clip.
 *
 * @author Mark Lindner
 */

public class UIElement
  {
  private Object element;
  private String name;

  /** Construct a new <code>UIElement</code>.
   */
  
  public UIElement()
    {
    this(null, null);
    }

  /** Construct a new <code>UIElement</code> for the specified element and
   * name.
   *
   * @param element The user interface element object proper.
   * @param name A descriptive name for the element.
   */
  
  public UIElement(Object element, String name)
    {
    this.element = element;
    this.name = name;
    }

  /** Get the name of the element.
   *
   * @return The name of the element.
   */
  
  public String getName()
    {
    return(name);
    }

  /** Get the element object.
   *
   * @return The user interface element object proper.
   */
  
  public Object getObject()
    {
    return(element);
    }

  /** Get a string representation for this object.
   *
   * @return The name of the element.
   */

  public String toString()
    {
    return(name);
    }
  
  }

/* end of source file */
