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
   $Log: ModelTableCellRenderer.java,v $
   Revision 1.7  2003/01/19 09:50:53  markl
   Javadoc & comment header updates.

   Revision 1.6  2002/08/11 09:51:30  markl
   Javadoc corrections.

   Revision 1.5  2001/03/12 09:27:58  markl
   Source code and Javadoc cleanup.

   Revision 1.4  1999/06/08 08:57:10  markl
   Fixed to inherit font from TreeTable.

   Revision 1.3  1999/02/27 08:19:08  markl
   Made constructors public.

   Revision 1.2  1999/01/10 02:53:23  markl
   added GPL header & RCS tag
   ----------------------------------------------------------------------------
*/

package kiwi.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import kiwi.ui.model.*;

/** An implementation of <code>TableCellRenderer</code> for use with
  * <code>JTable</code>s that are connected to a <code>ITreeModel</code> via a
  * <code>TreeModelTableAdapter</code>. This cell renderer consults the tree
  * model for a cell's rendering information, such as its label and icon.
  *
  * @see javax.swing.JTable
  * @see kiwi.ui.model.ITreeModel
  * @see kiwi.ui.model.TreeModelTableAdapter
  *
  * @author Mark Lindner
  */

public class ModelTableCellRenderer extends JLabel implements TableCellRenderer
  {
  private EmptyBorder emptyBorder = new EmptyBorder(0, 0, 0, 0);
  private int OFFSET = 18;
  private ITreeModel model;

  /** Construct a new <code>ModelTableCellRenderer</code>.
    *
    * @param model The tree model that will be used with this renderer.
    */

  public ModelTableCellRenderer(ITreeModel model)
    {
    this.model = model;
    }

  /** Return the component (in this case a <code>JLabel</code> that is used as
    * a "rubber stamp" for drawing items in the <code>JTable</code>. The
    * renderer will consult the tree model for each node's rendering
    * information.
    *
    * @param table The associated <code>JTable</code> instance.
    * @param value The object to draw (assumed to be a
    * <code>TreeModelTableAdapter.TableEntry</code>).
    * @param index The index of the item in the list.
    * @param isSelected <code>true</code> if this item is currently selected
    * in the table.
    * @param hasFocus <code>true</code> if this item currently has focus in the
    * table.
    */

  public Component getTableCellRendererComponent(JTable table, Object value,
						 boolean isSelected,
						 boolean hasFocus, int row,
						 int column)
    {
    TreeModelTableAdapter.TableEntry tableEntry
      = (TreeModelTableAdapter.TableEntry)value;

    if(tableEntry != null)
      {
      Border border;
      ITreeNode n = tableEntry.getObject();

      setIcon((Icon)model.getValueForProperty("icon", n));
      setText((String)model.getValueForProperty("label", n));

      if(row != -1)
	border = new EmptyBorder(1, OFFSET * tableEntry.getLevel(), 1, 0);
      else
	border = emptyBorder;

      this.setBorder(border);

      setFont(table.getFont());
      setOpaque(isSelected);

      if(isSelected)
	{
	this.setBackground(table.getSelectionBackground());
	this.setForeground(table.getSelectionForeground());
	}
      else
	{
	this.setBackground(table.getBackground());
	this.setForeground(table.getForeground());
	}
      }
    else
      {
      setText("");
      }
    return(this);
    }

  }

/* end of source file */
