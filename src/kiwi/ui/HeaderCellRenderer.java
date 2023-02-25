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
   $Log: HeaderCellRenderer.java,v $
   Revision 1.3  2003/01/19 09:50:53  markl
   Javadoc & comment header updates.

   Revision 1.2  2001/03/12 09:27:55  markl
   Source code and Javadoc cleanup.

   Revision 1.1  1999/04/23 07:26:21  markl
   Initial revision
   ----------------------------------------------------------------------------
*/

package kiwi.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

/** A customized header table cell renderer that left-justifies the header
 * text and may optionally render an icon.
 *
 * @author Mark Lindner
 */

public class HeaderCellRenderer extends JPanel implements TableCellRenderer
  {
  private Icon sortIcon;
  private static final Border headerBorder
    = new BevelBorder(BevelBorder.RAISED);
  private JLabel l_text, l_icon;
   
  /** Construct a new <code>HeaderCellRenderer</code>.
   */
   
  public HeaderCellRenderer()
    {
    setOpaque(false);
    setBorder(headerBorder);
    setLayout(new BorderLayout(0, 0));

    l_text = new JLabel();
    l_text.setVerticalTextPosition(SwingConstants.CENTER);
    l_text.setHorizontalTextPosition(SwingConstants.LEFT);
    l_text.setOpaque(false);
    add("Center", l_text);

    l_icon = new JLabel("");
    l_icon.setOpaque(false);
    add("East", l_icon);
    }

  /** Set the icon for the renderer. The icon is rendered against the right
   * edge of the renderer.
   *
   * @param icon The icon.
   */

  public void setIcon(Icon icon)
    {
    l_icon.setIcon(icon);
    }
  
  /** Get the cell renderer component.
   */
   
  public Component getTableCellRendererComponent(JTable table, Object value,
                                                 boolean isSelected,
                                                 boolean hasFocus, int row,
                                                 int column)
    {
    l_text.setText(value.toString());
    
    return(this);
    }

  }

/* end of source file */
