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
   $Log: PathEditor.java,v $
   Revision 1.7  2003/01/19 09:46:48  markl
   replaced JScrollPane instances with KScrollPane.

   Revision 1.6  2001/03/20 00:54:53  markl
   Fixed deprecated calls.

   Revision 1.5  2001/03/12 09:27:59  markl
   Source code and Javadoc cleanup.

   Revision 1.4  1999/11/15 03:36:13  markl
   API fix.

   Revision 1.3  1999/06/28 09:37:13  markl
   I18N.

   Revision 1.2  1999/01/10 02:56:27  markl
   added GPL header & RCS tag
   ----------------------------------------------------------------------------
*/

package kiwi.ui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.event.*;

import kiwi.ui.model.*;
import kiwi.ui.dialog.DirectorySelectorDialog;
import kiwi.util.*;

/** This class represents a component for editing a path (a list of
  * directories). It could be used for editing a binary search path, or a
  * Java classpath, for example. It provides <i>New</i>, <i>Delete</i>,
  * <i>Move Up</i>, and <i>Move Down</i> buttons.
  *
  * <p><center>
  * <img src="snapshot/PathEditor.gif"><br>
  * <i>An example PathEditor.</i>
  * </center>
  *
  * @author Mark Lindner
  */

public class PathEditor extends KPanel implements ActionListener
  {
  private JTable paths;
  private KButton b_delete, b_up, b_down, b_new;
  private DefaultTableModel tmodel;
  private PathListCellEditor cellEditor;
  private DirectorySelectorDialog d_path;
  private ListSelectionModel sel;
  private LocaleData loc, loc2;

  /** Construct a new <code>PathEditor</code>. */

  public PathEditor()
    {
    setBackground(SystemColor.control);
    setLayout(new BorderLayout(5, 5));

    KPanel panel1 = new KPanel();
    panel1.setLayout(new FlowLayout(FlowLayout.RIGHT, 1, 1));

    ResourceManager rm = KiwiUtils.getResourceManager();
    LocaleManager lm = LocaleManager.getDefaultLocaleManager();
    
    loc = lm.getLocaleData("KiwiDialogs");
    loc2 = lm.getLocaleData("KiwiMisc");
    
    b_new = new KButton(rm.getIcon("item-new.gif"));
    b_new.addActionListener(this);
    b_new.setToolTipText(loc.getMessage("kiwi.tooltip.new"));
    panel1.add(b_new);

    b_delete = new KButton(rm.getIcon("delete.gif"));
    b_delete.addActionListener(this);
    b_delete.setToolTipText(loc.getMessage("kiwi.tooltip.delete"));
    panel1.add(b_delete);

    b_up = new KButton(rm.getIcon("item-up.gif"));
    b_up.addActionListener(this);
    b_up.setToolTipText(loc.getMessage("kiwi.tooltip.move_up"));
    panel1.add(b_up);

    b_down = new KButton(rm.getIcon("item-down.gif"));
    b_down.addActionListener(this);
    b_down.setToolTipText(loc.getMessage("kiwi.tooltip.move_down"));
    panel1.add(b_down);

    add("North", panel1);

    String[] data = {};
    paths = new KTable();
    tmodel = new DefaultTableModel();
    paths.setModel(tmodel);
    // paths.setTableHeader(null);
    paths.setAutoCreateColumnsFromModel(false);
    paths.setColumnSelectionAllowed(false);
    paths.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    paths.setShowGrid(false);
    sel = paths.getSelectionModel();
    Vector v = new Vector();

    cellEditor = new PathListCellEditor();

    cellEditor.addActionListener(this);

    tmodel.addColumn(loc.getMessage("kiwi.label.directory_list"));
    TableColumn col;

    paths.addColumn(col = new TableColumn(0, 100));
    col.setCellEditor(cellEditor);

    for(int i = 0; i < data.length; i++)
      tmodel.addRow(new Object[] { data[i] });

    KScrollPane scrollPane = new KScrollPane(paths);
    scrollPane.setBorder(new BevelBorder(BevelBorder.LOWERED));
    scrollPane.setBackground(Color.white);
    scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants
					  .VERTICAL_SCROLLBAR_ALWAYS);
    scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants
					    .HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.addComponentListener(new ComponentAdapter()
      {
      public void componentResized(ComponentEvent ev)
	{
	if(paths.isEditing())
	  paths.getCellEditor().cancelCellEditing();
	
	KScrollPane sp = (KScrollPane)ev.getComponent();
	Dimension sz = sp.getViewport().getSize();
	paths.setSize(sz);
	}
      });

    add("Center", scrollPane);

    setSize(400, 400);
    }

  /** Handle events. This method is public as an implementation side-effect. */

  public void actionPerformed(ActionEvent evt)
    {
    Object o = evt.getSource();
    boolean editing = paths.isEditing();

    if(o == b_new && !editing)
      {
      int i = paths.getRowCount();
      tmodel.addRow(new Object[] {""}); // root path???
      paths.editCellAt(i, 0);
      sel.setSelectionInterval(i, i);
      }
    else if(o == b_delete && !editing)
      {
      int i = paths.getSelectedRow();
      if(i < 0) return;
      if(sel.isSelectionEmpty()) return;

      tmodel.removeRow(i);
      if(i >= paths.getRowCount()) i = 0;
      sel.setSelectionInterval(i, i);

      }
    else if(o == b_up && !editing)
      {
      int i = paths.getSelectedRow();
      if(i < 1) return;

      tmodel.moveRow(i, i, i - 1);
      sel.setSelectionInterval(i - 1, i - 1);
      }
    else if(o == b_down && !editing)
      {
      int i = paths.getSelectedRow();
      if(i < 0 || i > (paths.getRowCount() - 2)) return;

      tmodel.moveRow(i, i, i + 1);
      sel.setSelectionInterval(i + 1, i + 1);
      }
    }

  /** Return an array of the paths currently displayed by this view.
    */
  
  public DirectoryPath getDirectoryPath()
    {
    int c = tmodel.getRowCount();
    String[] paths = new String[c];
    for(int i = 0; i < c; i++)
      paths[i] = (String)tmodel.getValueAt(i, 0);

    return(new DirectoryPath(paths));
    }

  /** Set the list of paths to be displayed by this view.
    */

  public void setDirectoryPath(DirectoryPath path)
    {
    String dirs[] = path.getDirectories();
    
    tmodel.setNumRows(0);
    for(int i = 0; i < dirs.length; i++)
      tmodel.addRow(new Object[] { dirs[i] });
    }
  
  /* the custom cell editor */

  private class PathListCellEditor extends AbstractCellEditor
    implements TableCellEditor, ActionListener
    {
    private KPanel jp;
    private JTextField text;
    private JButton b_browse;
    private Vector alisteners;
    private int row = -1;
    private DirectorySelectorDialog d_select = null;
    private Frame parentFrame;

    PathListCellEditor()
      {
      jp = new KPanel();
      jp.setLayout(new BorderLayout(2, 2));

      text = new JTextField();
      text.addActionListener(this);
      jp.add("Center", text);

      b_browse = new KButton(loc.getMessage("kiwi.button.browse") + "...");
      b_browse.setMargin(new Insets(0, 2, 0, 2));
      b_browse.addActionListener(this);
      jp.add("East", b_browse);

      alisteners = new Vector();
      }

    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected,
                                                 int columnID, int rowIndex)
      {
      text.setText((String)value);
      return(jp);
      }

    public Object getCellEditorValue()
      {
      return(text.getText());
      }

    public boolean isCellEditable(EventObject ev)
      {
      boolean ok = false;

      if(ev == null)
	{
	text.requestFocus();
	return(true);
	}

      MouseEvent mev = (MouseEvent)ev;
      int newrow = ((JTable)mev.getSource())
      	.rowAtPoint(new Point(mev.getX(), mev.getY()));

      if(newrow == row)
	{
	text.requestFocus();
	ok = true;
	}
      else
	row = newrow;

      return(ok);
      }

    public boolean shouldSelectCell(EventObject evt)
      {
      text.requestFocus();
      return(true);
      }

    public boolean stopCellEditing()
      {
      // if input is valid...
      fireEditingStopped();
      return(true);
      }

    public void cancelCellEditing()
      {
      fireEditingCanceled();
      }

    public void addActionListener(ActionListener l)
      {
      alisteners.addElement(l);
      }

    public void removeActionListener(ActionListener l)
      {
      alisteners.removeElement(l);
      }

    public void actionPerformed(ActionEvent evt)
      {
      Object o = evt.getSource();

      if(o == text)
	{
	// if input is valid...
	fireEditingStopped();
	}
      else if(o == b_browse)
	{
        if(d_select == null)
          {
          d_select = new DirectorySelectorDialog(KiwiUtils.getPhantomFrame(),
                                                 true);
          d_select.setRoot(new File(System.getProperty("file.separator")));
          }
        KiwiUtils.centerWindow(d_select);
	d_select.setVisible(true);
        if(!d_select.isCancelled())
          {
          String p = d_select.getFile().getAbsolutePath();
          if(p != null)
            {
            text.setText(p);
            fireEditingStopped();
            }
          }
	}
      }
    }

  }

/* end of source file */
