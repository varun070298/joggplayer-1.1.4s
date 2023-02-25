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
   $Log: TreeTable.java,v $
   Revision 1.4  2003/01/19 09:50:54  markl
   Javadoc & comment header updates.

   Revision 1.3  2001/03/12 09:28:01  markl
   Source code and Javadoc cleanup.

   Revision 1.2  1999/01/10 03:01:37  markl
   added GPL header & RCS tag
   ----------------------------------------------------------------------------
*/

package kiwi.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.table.*;

import kiwi.ui.model.*;

/** A composite tree/table component. This class is an extension of
  * <code>JTable</code> in which the first column presents information in a
  * hierarchical form analogous to <code>JTree</code> (but not necessarily
  * implemented as such).
  * <p>
  * See <code>FilesystemTableView</code> for an example of
  * <code>TreeTable</code>.
  *
  * @see kiwi.ui.FilesystemTableView
  *
  * @author Mark Lindner
  */

public class TreeTable extends JTable
  {
  private ITreeModel model = null;
  private TreeModelTableAdapter tableAdapter = null;

  /** Construct a new <code>TreeTable</code>. The component is created without
    * a data model; use <code>setTreeModel()</code> to set the model.
    *
    * @see #setTreeModel
    */
  
  public TreeTable()
    {
    super();

    tableAdapter = new TreeModelTableAdapter(this);
    
    setShowGrid(false);
    setIntercellSpacing(new Dimension(0, 0));
    }

  /** Set this component's data model.
    *
    * @param model The <code>ITreeModel</code> to associate with this
    * component.
    * @see #getTreeModel
    */
  
  public void setTreeModel(ITreeModel model)
    {
    this.model = model;

    tableAdapter.setTreeModel(model);
    setModel(tableAdapter);
    ModelTreeCellRenderer r = new ModelTreeCellRenderer(model);
    r.setHighlightBackground(getSelectionBackground());
    r.setHighlightForeground(getSelectionForeground());

    // Install the tree editor renderer and editor.

    TableColumn col0 = getColumn(getColumnName(0));
    col0.setCellRenderer(new ModelTableCellRenderer(model));
    }

  /** Get this component's data model.
    *
    * @return The <code>ITreeModel</code> associated with this component.
    * @see #setTreeModel
    */
  
  public ITreeModel getTreeModel()
    {
    return(model);
    }
  
  }

/* end of source file */
