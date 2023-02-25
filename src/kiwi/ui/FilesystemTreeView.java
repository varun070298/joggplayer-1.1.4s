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
   $Log: FilesystemTreeView.java,v $
   Revision 1.6  2003/01/19 09:46:48  markl
   replaced JScrollPane instances with KScrollPane.

   Revision 1.5  2001/03/12 09:27:55  markl
   Source code and Javadoc cleanup.

   Revision 1.4  1999/06/08 08:56:24  markl
   Added setFont() method.

   Revision 1.3  1999/04/18 12:57:47  markl
   Bug fix; moved adapter.dispose() out of constructor into setRoot().

   Revision 1.2  1999/01/10 02:05:37  markl
   added GPL header & RCS tag
   ----------------------------------------------------------------------------
*/

package kiwi.ui;

import java.awt.*;
import java.io.File;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.border.*;

import kiwi.ui.model.*;

/** This class represents a filesystem tree component. It displays hierarchical
  * data (ultimately obtained from a <code>FilesystemDataSource</code>) in a
  * <code>JTree</code> component. The filesystem (or portion thereof) being
  * displayed by the component can be changed at any time.
  *
  * <p><center>
  * <img src="snapshot/FilesystemTreeView.gif"><br>
  * <i>An example FilesystemTreeView.</i>
  * </center>
  *
  * @see kiwi.ui.model.FilesystemDataSource  
  * @see javax.swing.JTree
  *
  * @author Mark Lindner
  */

public class FilesystemTreeView extends KPanel
  {
  private JTree tree = null;
  private TreeModelTreeAdapter adapter = null;
  private DynamicTreeModel model = null;
  private boolean ignoreFiles = false;

  /** Construct a new <code>FilesystemTreeView</code>. The tree initially has
    * no data model; use <code>setRoot()</code> to initialize the component.
    *
    * @see #setRoot
    */

  public FilesystemTreeView()
    {
    this(false);
    }

  /** Construct a new <code>FilesystemTreeView</code>. The tree initially has
    * no data model; use <code>setRoot()</code> to initialize the component.
    *
    * @param ignoreFiles A flag specifying whether this list should ignore
    * files and only display directories.
    * @see #setRoot    
    */

  public FilesystemTreeView(boolean ignoreFiles)
    {
    this.ignoreFiles = ignoreFiles;

    setLayout(new BorderLayout(0, 0));

    tree = new JTree();
    adapter = new TreeModelTreeAdapter(tree);
    tree.setBackground(Color.white);
    KScrollPane scrollPane = new KScrollPane(tree);
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
    tree.getSelectionModel()
      .setSelectionMode(flag ? TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION
                        : TreeSelectionModel.SINGLE_TREE_SELECTION);
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
    adapter = new TreeModelTreeAdapter(tree);
    adapter.setTreeModel(model);
    tree.setModel(adapter);
    tree.setCellRenderer(new ModelTreeCellRenderer(model));
    repaint();
    }

  /** Get the currently selected item in the tree. If there is more than one
    * item selected in the tree, gets the first selected item.
    *
    * @return The <code>File</code> object for the currently selected item in
    * the tree, or <code>null</code> if there is no selection.
    * @see #getSelectedFiles
    */

  public File getSelectedFile()
    {
    TreePath path = tree.getSelectionPath();
    return((path == null) ? null : fileForPath(path));
    }

  /** Get the currently selected items in the tree.
    *
    * @return An array of <code>File</code> objects corresponding to the
    * currently selected items in the tree. If there is no selection, an empty
    * array is returned.
    * @see #getSelectedFile
    */
  
  public File[] getSelectedFiles()
    {
    TreePath paths[] = tree.getSelectionPaths();
    File f[] = new File[paths.length];
    for(int i = 0; i < paths.length; i++)
      f[i] = fileForPath(paths[i]);

    return(f);
    }

  /** Get the <code>File</code> object for a given path in the tree.
    *
    * @param path The <code>TreePath</code> of the item.
    * @return The <code>File</code> object at the end of the given path.
    */
  
  protected final File fileForPath(TreePath path)
    {
    ITreeNode node = (ITreeNode)path.getLastPathComponent();
    return((node == null) ? null : (File)(node.getObject()));
    }

  /** Get the <code>JTree</code> that is embedded in this component. */

  public final JTree getJTree()
    {
    return(tree);
    }

  /** Set the component's opacity. */

  public void setOpaque(boolean flag)
    {
    super.setOpaque(flag);
    if(tree != null)
      tree.setOpaque(flag);
    }

  /** Set the font for this component.
   *
   * @param font The new font.
   */

  public void setFont(Font font)
    {
    super.setFont(font);
    if(tree != null)
      tree.setFont(font);
    }

  }

/* end of source file */
