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
   $Log: DocumentBrowserView.java,v $
   Revision 1.5  2003/01/19 09:46:47  markl
   replaced JScrollPane instances with KScrollPane.

   Revision 1.4  2001/03/12 09:27:54  markl
   Source code and Javadoc cleanup.

   Revision 1.3  2000/07/31 02:02:35  markl
   Made root node invisible.

   Revision 1.2  1999/01/10 02:05:37  markl
   added GPL header & RCS tag
   ----------------------------------------------------------------------------
*/

package kiwi.ui;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.net.URL;

import kiwi.ui.model.*;

/** This class represents a general-purpose browser component for viewing a
  * hierarchically-organized collection of documents. The interface consists of
  * a split pane with a tree component in the left pane and an HTML component
  * in the right pane.
  *
  * <p><center>
  * <img src="snapshot/DocumentBrowserView.gif"><br>
  * <i>An example DocumentBrowserView.</i>
  * </center>
  *
  * @see kiwi.ui.DocumentBrowserFrame
  *
  * @author Mark Lindner
  */

public class DocumentBrowserView extends KPanel
  {
  private JTree tree = null;
  private JEditorPane html = null;
  private TreeModelTreeAdapter adapter;
  private ITreeModel model;
  
  /** Construct a new <code>DocumentBrowserView</code>.
    *
    * @param model The tree data model for this browser.
    */

  public DocumentBrowserView(ITreeModel model)
    {
    this.model = model;
    setLayout(new GridLayout(1, 0));
    JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    split.setOpaque(false);
    add(split);

    tree = new JTree();
    tree.setBackground(Color.white);
    tree.setRootVisible(false);
    KScrollPane scroll = new KScrollPane(tree);

    adapter = new TreeModelTreeAdapter(tree);
    adapter.setTreeModel(model);

    tree.setModel(adapter);
    tree.setCellRenderer(new ModelTreeCellRenderer(model));

    split.setLeftComponent(scroll);

    html = new JEditorPane();
    html.setBackground(Color.white);
    html.setEditable(false);

    html.addHyperlinkListener(new HyperlinkListener()
      {
      public void hyperlinkUpdate(HyperlinkEvent evt)
	{
        ITreeNode nodes[] = nodesForPath(evt.getURL().getFile());
        expandNodes(nodes);

	showNode((DocumentDataSource.DocumentNode)
                 (nodes[nodes.length - 1]).getObject());
	}
      });

    scroll = new KScrollPane(html);
    scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants
					.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    split.setRightComponent(scroll);

    split.setDividerLocation(150);
    
    DefaultTreeSelectionModel ts = new DefaultTreeSelectionModel();
    ts.setSelectionMode(ts.SINGLE_TREE_SELECTION);

    tree.addTreeSelectionListener(new TreeSelectionListener()
      {
      public void valueChanged(TreeSelectionEvent evt)
	{
	ITreeNode item = (ITreeNode)evt.getPath().getLastPathComponent();
	if(item.getObject() instanceof DocumentDataSource.DocumentNode)
	  {
	  DocumentDataSource.DocumentNode helpNode
	    = (DocumentDataSource.DocumentNode)(item.getObject());
	  if(helpNode.isExpandable()) return;
	  showNode(helpNode);
	  }
	}
      });
    }

/*  public void setOpaque(boolean opaque)
    {
    super.setOpaque(opaque);
    if(pane != null) pane.setOpaque(opaque);
    if(tree != null) tree.setOpaque(opaque);
    } */
  
  /** Display a specific page in the browser.
    *
    * @param page The URL of the page to display.
    */

  private void showNode(DocumentDataSource.DocumentNode node)
    {
    try
      {
      html.setPage(node.getURL());
      }
    catch(java.io.IOException ex)
      {
      // more graceful way to handle this?
      ex.printStackTrace();
      }
    }

  private void expandNodes(ITreeNode nodes[])
    {
    TreePath path = adapter.getPathForNode(nodes[nodes.length - 1]);
    tree.scrollPathToVisible(path);
    tree.setSelectionPath(path);
    }
  
  private ITreeNode[] nodesForPath(String path)
    {
    StringTokenizer st = new StringTokenizer(path, "/");
    ITreeNode curNode = model.getRoot();
    Vector v = new Vector();

LOOP:

    for(;;)
      {
      v.addElement(curNode);
      if(!curNode.isExpandable()) break;

      if(!st.hasMoreTokens()) break;
      String s = st.nextToken();
      model.expand(curNode);
      ITreeNode children[] = model.getChildren(curNode);
      for(int i = 0; i < children.length; i++)
        {
        String file = ((DocumentDataSource.DocumentNode)
                       (children[i].getObject())).getFile();
        if(s.equals(file))
          {
          curNode = children[i];
          continue LOOP;
          }
        }

      return(null);
      }

    if(st.hasMoreTokens()) return(null);
    ITreeNode n[] = new ITreeNode[v.size()];
    v.copyInto(n);

    return(n);
    }
  
  }

/* end of source file */
