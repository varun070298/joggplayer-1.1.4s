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
   $Log: ColorSwatchCellRenderer.java,v $
   Revision 1.3  2003/01/19 09:50:53  markl
   Javadoc & comment header updates.

   Revision 1.2  2001/03/12 09:27:52  markl
   Source code and Javadoc cleanup.

   Revision 1.1  2000/10/15 09:31:46  markl
   New class.
   ----------------------------------------------------------------------------
*/

package kiwi.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import kiwi.text.ColorFormatter;

/** A cell renderer for color swatches.
 *
 * @author Mark Lindner
 */

public class ColorSwatchCellRenderer extends JLabel
  implements ListCellRenderer, TableCellRenderer
  {
  private static ColorSwatch swatch = new ColorSwatch();
  private Border emptyBorder, highlightBorder;

  /** Construct a new <code>ColorSwatchCellRenderer</code> with the
   * specified highlight color.
   *
   * @param highlightColor The highlight color.
   */
  
  public ColorSwatchCellRenderer(Color highlightColor)
    {
    emptyBorder = BorderFactory.createEmptyBorder(2, 2, 2, 2);
    highlightBorder = BorderFactory.createLineBorder(highlightColor, 2);

    setIcon(swatch);
    }

  /** Get a table cell renderer component.
   */

  public Component getTableCellRendererComponent(JTable table, Object value,
                                                 boolean isSelected,
                                                 boolean hasFocus, int row,
                                                 int column)
    {
    return(getRenderer((Color)value, isSelected));
    }
  
  /** Get a list cell renderer component.
   */
  
  public Component getListCellRendererComponent(JList list, Object value,
                                                int index, boolean isSelected,
                                                boolean hasFocus)
    {
    return(getRenderer((Color)value, isSelected));
    }

  /* Prepare the renderer.
   */

  private Component getRenderer(Color c, boolean isSelected)
    {
    swatch.setColor(c);
    setText(ColorFormatter.nameForColor(c));
    
    setBorder(isSelected ? highlightBorder : emptyBorder);

    return(this);
    }

  }

/* end of source file */
