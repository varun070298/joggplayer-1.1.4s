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
   $Log: FilesystemListView.java,v $
   Revision 1.5  2003/01/19 09:46:48  markl
   replaced JScrollPane instances with KScrollPane.

   Revision 1.4  2001/03/12 09:27:54  markl
   Source code and Javadoc cleanup.

   Revision 1.3  1999/06/08 08:55:54  markl
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

/** This class represents a filesystem list component. It displays hierarchical
  * data (ultimately obtained from a <code>FilesystemDataSource</code>) in a
  * <code>JList</code> component. The filesystem (or portion thereof) being
  * displayed by the component can be changed at any time.
  *
  * <p><center>
  * <img src="snapshot/FilesystemListView.gif"><br>
  * <i>An example FilesystemListView.</i>
  * </center>
  *
  * @see kiwi.ui.model.FilesystemDataSource
  * @see javax.swing.JList
  *
  * @author Mark Lindner
  */

public class FilesystemListView extends KPanel
  {
  private JList list = null;
  private TreeModelListAdapter adapter = null;
  private ITreeModel model = null;
  private boolean ignoreFiles = false;

  /** Construct a new <code>FilesystemListView</code>. The list initially has
    * no data model; use <code>setRoot()</code> to initialize the component.
    *
    * @see #setRoot
    */

  public FilesystemListView()
    {
    this(false);
    }

  /** Construct a new <code>FilesystemListView</code>. The list initially has
    * no data model; use <code>setRoot()</code> to initialize the component.
    *
    * @param ignoreFiles A flag specifying whether this list should ignore
    * files and only display directories.
    * @see #setRoot
    */

  public FilesystemListView(boolean ignoreFiles)
    {
    this.ignoreFiles = ignoreFiles;

    setLayout(new BorderLayout(0, 0));

    list = new JList();
    adapter = new TreeModelListAdapter(list);
    list.setBackground(Color.white);
    KScrollPane scrollPane = new KScrollPane(list);
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
    list.getSelectionModel()
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
    if(adapter != null) adapter.dispose(); // dispose of the old adapter
    adapter = new TreeModelListAdapter(list);
    adapter.setTreeModel(model);
    list.setModel(adapter);
    list.setCellRenderer(new ModelListCellRenderer(model));
    repaint();
    }

  /** Get the currently selected item in the list.
    *
    * @return The <code>File</code> object for the currently selected item in
    * the list, or <code>null</code> if there is no selection. If there is
    * more than one item selected in the list, gets the first selected item.
    * @see #getSelectedFiles
    */

  public File getSelectedFile()
    {
    int index = list.getSelectedIndex();
    return((index < 0) ? null : fileForIndex(index));
    }

  /** Get the currently selected items in the list.
    *
    * @return An array of <code>File</code> objects corresponding to the
    * currently selected items in the list. If there is no selection, an empty
    * array is returned.
    * @see #getSelectedFile
    */

  public File[] getSelectedFiles()
    {
    int indices[] = list.getSelectedIndices();
    File f[] = new File[indices.length];
    for(int i = 0; i < indices.length; i++)
      f[i] = fileForIndex(indices[i]);
    
    return(f);
    }
  
  /** Get the <code>File</code> object for a given index in the list.
    *
    * @param index The index of the item.
    * @return The <code>File</code> object at the given index.
    */

  protected final File fileForIndex(int index)
    {
    TreeModelListAdapter.ListEntry item
      = (TreeModelListAdapter.ListEntry)list.getSelectedValue();
    return((item == null) ? null
           : (File)(item.getObject().getObject()));
    }  
  
  /** Get the <code>JList</code> that is embedded in this component.
    */

  public final JList getJList()
    {
    return(list);
    }

  /** Set the font for this component.
   *
   * @param font The new font.
   */

  public void setFont(Font font)
    {
    super.setFont(font);
    if(list != null)
      list.setFont(font);
    }  

  }

/* end of source file */
