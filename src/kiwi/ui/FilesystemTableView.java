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
   $Log: FilesystemTableView.java,v $
   Revision 1.5  2003/01/19 09:46:48  markl
   replaced JScrollPane instances with KScrollPane.

   Revision 1.4  2001/03/12 09:27:55  markl
   Source code and Javadoc cleanup.

   Revision 1.3  1999/06/08 08:56:10  markl
   Added setFont() method.

   Revision 1.2  1999/01/10 02:05:37  markl
   added GPL header & RCS tag
   ----------------------------------------------------------------------------
*/

package kiwi.ui;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;

import kiwi.ui.model.*;

/** This class represents a filesystem table component. It displays
  * hierarchical data (ultimately obtained from a
  * <code>FilesystemDataSource</code>) in a <code>TreeTable</code> component.
  * The filesystem (or portion thereof) being displayed by the component can
  * be changed at any time.
  *
  * <p><center>
  * <img src="snapshot/FilesystemTableView.gif"><br>
  * <i>An example FilesystemTableView.</i>
  * </center>
  *
  * @see kiwi.ui.model.FilesystemDataSource  
  * @see kiwi.ui.TreeTable
  *
  * @author Mark Lindner
  */

public class FilesystemTableView extends KPanel
  {
  private TreeTable table;
  private ITreeModel model = null;
  private boolean ignoreFiles = false;

  /** Construct a new <code>FilesystemTableView</code>. The table initially has
    * no data model; use <code>setRoot()</code> to initialize the component.
    *
    * @see #setRoot
    */

  public FilesystemTableView()
    {
    this(false);
    }

  /** Construct a new <code>FilesystemTableView</code>. The list initially has
    * no data model; use <code>setRoot()</code> to initialize the component.
    *
    * @param ignoreFiles A flag specifying whether this table should ignore
    * files and only display directories.
    * @see #setRoot
    */

  public FilesystemTableView(boolean ignoreFiles)
    {
    this.ignoreFiles = ignoreFiles;

    setLayout(new BorderLayout(0, 0));

    table = new TreeTable();
    table.setBackground(Color.white);
    KScrollPane scrollPane = new KScrollPane(table);
    scrollPane.setBackground(Color.white);
    scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants
					  .VERTICAL_SCROLLBAR_ALWAYS);
    add("Center", scrollPane);

    setMultipleSelectionsAllowed(false);
    }

  /** Specify whether multiple selections are allowed in this component.
    *
    * @param flag If <code>true</code>, multiple discontiguous selections will
    * be allowed; otherwise only single selection is allowed (the default).
    */

  public void setMultipleSelectionsAllowed(boolean flag)
    {
    table.getSelectionModel()
      .setSelectionMode(flag ? ListSelectionModel.MULTIPLE_INTERVAL_SELECTION
                        : ListSelectionModel.SINGLE_SELECTION);
    }
  
  /** Set the root of the filesystem to be displayed by this component. This
    * causes the component to be reset and repainted.
    *
    * @param root The root directory of the filesystem to display.
    */

  public void setRoot(File root)
    {
    FilesystemDataSource fds = new FilesystemDataSource(root, ignoreFiles);
    model = new DynamicTreeModel(fds);

    table.setTreeModel(model);
    repaint();
    }
  
  /** Get the currently selected item in the table. If there is more than one
    * item selected in the table, gets the last or most recently selected item.
    *
    * @return The <code>File</code> object for the currently selected item in
    * the table, or <code>null</code> if there is no selection.
    * @see #getSelectedFiles
    */

  public File getSelectedFile()
    {
    int row = table.getSelectedRow();
    return((row < 0) ? null : fileForRow(row));
    }

  /** Get the currently selected items in the table.
    *
    * @return An array of <code>File</code> objects corresponding to the
    * currently selected items in the table. If there is no selection, an empty
    * array is returned.
    * @see #getSelectedFile
    */
  
  public File[] getSelectedFiles()
    {
    int rows[] = table.getSelectedRows();
    File f[] = new File[rows.length];
    for(int i = 0; i < rows.length; i++)
      f[i] = fileForRow(rows[i]);
    
    return(f);
    }

  /** Get the <code>File</code> object for a given row in the table.
    *
    * @param row The row index of the item..
    * @return The <code>File</code> object at the given row.
    */

  protected final File fileForRow(int row)
    {
    TreeModelTableAdapter.TableEntry item
      = (TreeModelTableAdapter.TableEntry)table.getValueAt(row, 0);
    return((item == null) ? null
           : (File)(item.getObject().getObject()));
    }
  
  /** Get the <code>TreeTable</code> that is embedded in this component. */

  public final TreeTable getTreeTable()
    {
    return(table);
    }

  /** Set the font for this component.
   *
   * @param font The new font.
   */

  public void setFont(Font font)
    {
    super.setFont(font);
    if(table != null)
      table.setFont(font);
    }  

  }

/* end of source file */
