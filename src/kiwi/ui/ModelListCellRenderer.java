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
   $Log: ModelListCellRenderer.java,v $
   Revision 1.6  2003/01/19 09:50:53  markl
   Javadoc & comment header updates.

   Revision 1.5  2001/03/12 09:27:58  markl
   Source code and Javadoc cleanup.

   Revision 1.4  1999/06/08 08:56:44  markl
   Fixed to inherit font from JList.

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

import kiwi.ui.model.*;

/** An implementation of <code>ListCellRenderer</code> for use with
  * <code>JList</code>s that are connected to a <code>ITreeModel</code> via
  * a <code>TreeModelListAdapter</code>. This cell renderer consults the tree
  * model for a cell's rendering information, such as its label and icon.
  *
  * @see javax.swing.JList
  * @see kiwi.ui.model.ITreeModel
  * @see kiwi.ui.model.TreeModelListAdapter
  *
  * @author Mark Lindner
  */

public class ModelListCellRenderer extends JLabel implements ListCellRenderer
  {
  private EmptyBorder emptyBorder = new EmptyBorder(0, 0, 0, 0);
  private int OFFSET = 18;
  private ITreeModel model;

  /** Construct a new <code>ModelListCellRenderer</code>.
    *
    * @param model The tree model that will be used with this renderer.
    */

  public ModelListCellRenderer(ITreeModel model)
    {
    this.model = model;
    }

  /** Return the component (in this case a <code>JLabel</code> that is used as
    * a "rubber stamp" for drawing items in the <code>JList</code>. The
    * renderer will consult the tree model for each node's rendering
    * information.
    *
    * @param list The associated <code>JList</code> instance.
    * @param value The object to draw (assumed to be a
    * <code>TreeModelListAdapter.ListEntry</code>).
    * @param index The index of the item in the list.
    * @param isSelected <code>true</code> if this item is currently selected
    * in the list.
    * @param hasFocus <code>true</code> if this item currently has focus in
    * the list.
    */

  public Component getListCellRendererComponent(JList list, Object value,
						int index, boolean isSelected,
						boolean hasFocus)
    {
    TreeModelListAdapter.ListEntry listEntry
      = (TreeModelListAdapter.ListEntry)value;

    if(listEntry != null)
      {
      Border border;
      ITreeNode n = listEntry.getObject();

      setIcon((Icon)model.getValueForProperty("icon", n));
      setText((String)model.getValueForProperty("label", n));

      if(index != -1)
	border = new EmptyBorder(1, OFFSET * listEntry.getLevel(), 1, 0);
      else
	border = emptyBorder;

      this.setBorder(border);

      setFont(list.getFont());
      setOpaque(isSelected);
      
      if(isSelected)
	{
	this.setBackground(list.getSelectionBackground());
	this.setForeground(list.getSelectionForeground());
	}
      else
	{
	this.setBackground(list.getBackground());
	this.setForeground(list.getForeground());
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
