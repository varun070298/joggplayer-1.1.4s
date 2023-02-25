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
   $Log: ModelTreeCellRenderer.java,v $
   Revision 1.5  2003/01/19 09:50:53  markl
   Javadoc & comment header updates.

   Revision 1.4  2001/03/12 09:27:58  markl
   Source code and Javadoc cleanup.

   Revision 1.3  1999/06/08 08:57:26  markl
   Fixed to inherit font from JTree.

   Revision 1.2  1999/01/10 02:53:23  markl
   added GPL header & RCS tag
   ----------------------------------------------------------------------------
*/

package kiwi.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.tree.TreeCellRenderer;

import kiwi.ui.model.*;

/** An implementation of <code>TreeCellRenderer</code> for use with
  * <code>JTree</code>s that are connected to a <code>ITreeModel</code> via a
  * <code>TreeModelTreeAdapter</code>. This cell renderer consults the tree
  * model for a cell's rendering information, such as its label and icon.
  *
  * @see javax.swing.JList
  * @see kiwi.ui.model.ITreeModel
  * @see kiwi.ui.model.TreeModelListAdapter
  *
  * @author Mark Lindner
  */

public class ModelTreeCellRenderer extends JLabel implements TreeCellRenderer
  {
  private ITreeModel model;
  private Color highlightBackground = Color.blue.darker();
  private Color highlightForeground = Color.white;

  /** Construct a new <code>ModelTreeCellRenderer</code>.
    *
    * @param model The tree model that will be used with this renderer.
    */

  public ModelTreeCellRenderer(ITreeModel model)
    {
    this.model = model;
    }

  /** Return the component (in this case a <code>JLabel</code> that is used as
    * a "rubber stamp" for drawing items in the <code>JTree</code>. The
    * renderer will consult the tree model for each node's rendering
    * information.
    *
    * @param tree The associated <code>JTree</code> instance.
    * @param value The object to draw (assumed to be an
    * <code>ITreeNode</code>).
    * @param isSelected <code>true</code> if this item is currently selected
    * in the tree.
    * @param hasFocus <code>true</code> if this item currently has focus in
    * the tree.
    * @param expanded <code>true</code> if this item is currently expanded in
    * the tree.
    * @param row The row number for this item in the tree.
    * @param leaf <code>true</code> if this item is a leaf.
    */

  public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                boolean isSelected,
                                                boolean expanded, boolean leaf,
						int row, boolean hasFocus)
    {
    if(model != null)
      {
      setIcon((Icon)model.getValueForProperty("icon", (ITreeNode)value));
      setText((String)model.getValueForProperty("label", (ITreeNode)value));
      }

    setFont(tree.getFont());
    setOpaque(isSelected);
    
    if(isSelected)
      {
      this.setBackground(highlightBackground);
      this.setForeground(highlightForeground);
      }
    else
      {
      this.setBackground(tree.getBackground());
      this.setForeground(tree.getForeground());
      }

    return(this);
    }

  /** Set the background color for a highlighted item. This method will be
    * deprecated once <code>JTree.getSelectionBackground()</code> is
    * implemented.
    *
    * @param bg The new background color.
    */

  public void setHighlightBackground(Color bg)
    {
    highlightBackground = bg;
    }

  /** Set the foreground color for a highlighted item.  This method will be
    * deprecated once <code>JTree.getSelectionForeground()</code> is
    * implemented.
    *
    * @param fg The new foreground color.
    */

  public void setHighlightForeground(Color fg)
    {
    highlightForeground = fg;
    }

  }

/* end of source file */
