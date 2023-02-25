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
   $Log: ColoredCellRenderer.java,v $
   Revision 1.4  2003/01/19 09:50:53  markl
   Javadoc & comment header updates.

   Revision 1.3  2001/03/12 09:27:53  markl
   Source code and Javadoc cleanup.

   Revision 1.2  1999/01/10 01:00:58  markl
   added GPL header & RCS tag
   ----------------------------------------------------------------------------
*/

package kiwi.ui;

import java.awt.*;
import javax.swing.*;

import kiwi.ui.ColoredString;

/** A cell renderer for <code>ColoredString</code>s; for use with
  * <code>JList</code>s.
  *
  * @see kiwi.ui.ColoredString
  * @see javax.swing.JList
  *
  * @author Mark Lindner
  */

public class ColoredCellRenderer extends JLabel implements ListCellRenderer
  {
  private Color gray = new Color(100, 100, 100);

  /** Construct a new <code>ColeredCellRenderer</code>. */

  public ColoredCellRenderer()
    {
    setOpaque(true);
    }

  /** Return the component (in this case a <code>JLabel</code> that is used
    * as a "rubber stamp" for drawing items in the <code>JList</code>. The
    * background of the cell is black, and the foreground will be the color of
    * the colored string.
    *
    * @param list The associated <code>JList</code> instance.
    * @param value The <code>ColoredString</code> to draw.
    * @param index The offset of the item in the list.
    * @param isSelected <code>true</code> if this item is currently selected
    * in the list.
    * @param cellHasFocus <code>true</code> if this item currently has focus
    * in the list.
    */

  public Component getListCellRendererComponent(JList list,
      	                                        Object value, int index,
                                                boolean isSelected,
                                                boolean cellHasFocus)
    {
    ColoredString s = (ColoredString)value;

    setText(s.getString());
    setBackground(isSelected ? gray : Color.black);
    setForeground(s.getColor());
    return(this);
    }

  }

/* end of source file */
