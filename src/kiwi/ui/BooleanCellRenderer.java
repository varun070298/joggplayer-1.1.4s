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
   $Log: BooleanCellRenderer.java,v $
   Revision 1.4  2003/01/19 09:50:52  markl
   Javadoc & comment header updates.

   Revision 1.3  2001/03/12 09:27:52  markl
   Source code and Javadoc cleanup.

   Revision 1.2  1999/07/06 09:17:59  markl
   Fixed rendering so that we inherit the table's colors properly.

   Revision 1.1  1999/04/23 07:26:28  markl
   Initial revision
   ----------------------------------------------------------------------------
*/

package kiwi.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.TableCellRenderer;

import kiwi.util.*;

/** A table cell renderer for displaying boolean values. Renders a value,
 * which may be either a <code>Boolean</code> object or a
 * <code>BooleanHolder</code> object, as a non-editable checkbox.
 *
 * @author Mark Lindner
 *
 * @see java.lang.Boolean
 * @see kiwi.util.BooleanHolder
 */

public class BooleanCellRenderer extends JCheckBox implements TableCellRenderer
  {
  private static final Border deselectedBorder = new EmptyBorder(1, 1, 1, 1);
  private static final Border selectedBorder
    = new LineBorder(Color.gray.darker());

  /** Construct a new <code>BooleanCellRenderer</code>
   */
  
  public BooleanCellRenderer()
    {
    setOpaque(true);
    setHorizontalAlignment(SwingConstants.CENTER);
    }

  /** Get a reference to the renderer component. */
  
  public Component getTableCellRendererComponent(JTable jTable, Object value,
                                                 boolean isSelected,
                                                 boolean hasFocus,
                                                 int row, int col)
    {
    boolean flag;
    if(value instanceof BooleanHolder)
      flag = ((BooleanHolder)value).getValue();
    else if(value instanceof Boolean)
      flag = ((Boolean)value).booleanValue();
    else
      flag = false;

    if(isSelected)
      setBackground(jTable.getSelectionBackground());
    else
      setBackground(jTable.getBackground());
          
    
    setSelected(flag);
    setBorder(isSelected ? selectedBorder : deselectedBorder);
    return(this);
    }
  
  }

/* end of source file */
