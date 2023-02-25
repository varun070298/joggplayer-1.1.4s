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
   $Log: KTable.java,v $
   Revision 1.6  2003/02/06 07:40:31  markl
   Removed references to Metal Look & Feel.

   Revision 1.5  2003/01/19 09:49:18  markl
   Updated header cell renderer to display appropriate sort icon.

   Revision 1.4  2001/06/26 06:18:16  markl
   Bugfix to header construction code.

   Revision 1.3  2001/03/12 09:27:57  markl
   Source code and Javadoc cleanup.
   ----------------------------------------------------------------------------
*/

package kiwi.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import kiwi.ui.model.*;
import kiwi.util.*;

/** An extension of <code>JTable</code> that fixes a number of blatant bugs
 * and limitations in that component.
 *
 * <p><center>
 * <img src="snapshot/KTable.gif"><br>
 * <i>An example sortable KTable.</i>
 * </center>
 *
 * @author Mark Lindner
 */

public class KTable extends JTable
  {
  private TableSorter sorter;
  private boolean sortable = false, editable = true;
  private TableModel realModel = null;
  private Icon i_sort, i_rsort;
  private _HeaderRenderer headerRenderer;
  private boolean columnReordering = false;
  
  /** Construct a new <code>KTable</code>.
   */
  
  public KTable()
    {
    i_sort = KiwiUtils.getResourceManager().getIcon("sort-down.gif");
    i_rsort = KiwiUtils.getResourceManager().getIcon("sort-up.gif");
    
    setAutoCreateColumnsFromModel(true);
    
    setShowHorizontalLines(false);
    setShowVerticalLines(false);
    setShowGrid(false);

    setSelectionModel(new _SelectionModel());

    setTableHeader(new _TableHeader(getColumnModel()));

    setAutoResizeMode(AUTO_RESIZE_ALL_COLUMNS);
    sizeColumnsToFit(AUTO_RESIZE_ALL_COLUMNS);
    }

  public void createDefaultColumnsFromModel()
    {
    super.createDefaultColumnsFromModel();

    prepareHeader();
    }

  /** Get the row translation for a given row in the table. If sorting is
   * turned on, the visual row may not be the same as the row in the underlying
   * data model; this method obtains the model row corresponding to the
   * specified visual row, whether sorting is turned on or not.
   *
   * @param row The visual row.
   * @return The corresponding row in the data model.
   */

  public int getRowTranslation(int row)
    {
    return(sortable ? sorter.getRowTranslation(row) : row);
    }
  
  /** Set the data model for this table.
   *
   * @param model The new model.
   */
  
  public void setModel(TableModel model)
    {
    realModel = model;
    if(sortable)
      {
      sorter = new TableSorter(realModel);
      super.setModel(sorter);
      sorter.registerTableHeaderListener(this);
      }
    else
      super.setModel(realModel);
    }

  /** Prepare the header to use the custom header renderer.
   */

  private void prepareHeader()
    {
    TableColumnModel cmodel = getColumnModel();
    headerRenderer = new _HeaderRenderer();
    
    int cols = cmodel.getColumnCount();

    for(int i = 0; i < cols; i++)
      {
      TableColumn tc = cmodel.getColumn(i);
      tc.setHeaderRenderer(headerRenderer);
      }
    }

  /** Enable or disable this table. A disabled table cannot be edited or
   * sorted, nor can the selection be changed, nor the columns reordered or
   * resized.
   *
   * @param editable A flag specifying whether this table should be enabled
   * or disabled.
   */

  public void setEnabled(boolean enabled)
    {
    Color fg = (enabled
                ? UIManager.getColor("Table.selectionForeground")
                : UIManager.getColor("Label.disabledForeground"));
    
    setForeground(fg);
    setSelectionForeground(fg);
    super.setEnabled(enabled);
    getTableHeader().setEnabled(enabled);
    
    }

  /** Set the editable state of this table.
   *
   * @param editable A flag specifying whether this table is editable.
   */
  
  public void setEditable(boolean editable)
    {
    this.editable = editable;
    }

  /** Determine if the table is editable.
   *
   * @return <code>true</code> if the table is editable and <code>false</code>
   * otherwise.
   */
  
  public boolean isEditable()
    {
    return(editable);
    }

  /** Set the sortable state of this table.
   *
   * @param sortable A flag specifying whether this table is sortable.
   */
  
  public void setSortable(boolean sortable)
    {
    if(this.sortable == sortable)
      return;
    
    this.sortable = sortable;

    if(sortable)
      {
      if(realModel != null)
        {
        sorter = new TableSorter(realModel);
        super.setModel(sorter);
        sorter.registerTableHeaderListener(this);
        }
      }
    else
      {
      if(realModel != null)
        super.setModel(realModel);
      sorter = null;
      }

    prepareHeader();    
    }

  /** Determine if the table is sortable.
   *
   * @return <code>true</code> if the table is sortable and <code>false</code>
   * otherwise.
   */
  
  public boolean isSortable()
    {
    return(sortable);
    }  

  /** Determine if a cell is editable. Editability of a given cell is
   * ultimately determined by the table model, unless the table has been
   * made non-editable via a call to <code>setEditable()</code>, or if it has
   * been disabled via a call to <code>setEnabled()</code>.
   *
   * @param row The row of the cell.
   * @param col The column of the cell.
   * @return <code>true</code> if the cell at the specified coordinates is
   * editable, or <code>false</code> if it is not editable or if the table has
   * been made non-editable.
   * @see #setEditable
   */

  public boolean isCellEditable(int row, int col)
    {
    if(!isEnabled())
      return(false);
    
    return(editable ? getModel().isCellEditable(row, col) : false);
    }

  /** Scroll the table to ensure that a given row is visible.
   *
   * @param row The row that must be visible.
   */
  
  public void ensureRowIsVisible(int row)
    {
    Rectangle r = getCellRect(row, 0, false);
    r.y = ((rowHeight + rowMargin) * row) + (getSize().height / 2);
    scrollRectToVisible(r);
    }

  /** Stop any cell edit that is in progress on the table. */

  public void stopEditing()
    {
    int row = getEditingRow();
    int col = getEditingColumn();

    getCellEditor(row, col).stopCellEditing();
    }
  
  /* A custom list selection model that honors disabled state by disallowing
   * changes to the current selection(s).
   */

  private class _SelectionModel extends DefaultListSelectionModel
    {
    public void clearSelection()
      {
      if(isEnabled())
        super.clearSelection();
      }
    
    public void addSelectionInterval(int a, int b)
      {
      if(isEnabled())
        super.addSelectionInterval(a, b);
      }
    
    public void insertIndexInterval(int a, int b, boolean before)
      {
      if(isEnabled())
        super.insertIndexInterval(a, b, before);
      }
    
    public void removeIndexInterval(int a, int b)
      {
      if(isEnabled())
        super.removeIndexInterval(a, b);
      }

    public void setSelectionInterval(int a, int b)
      {
      if(isEnabled())
        super.setSelectionInterval(a, b);
      }

    public void setAnchorSelectionIndex(int a)
      {
      if(isEnabled())
        super.setAnchorSelectionIndex(a);
      }

    public void setLeadSelectionIndex(int a)
      {
      if(isEnabled())
        super.setLeadSelectionIndex(a);
      }
    }

  /* A custom header renderer that displays a sort icon in each column if the
   * table is sortable.
   */
  
  private class _HeaderRenderer extends HeaderCellRenderer
    {
    public Component getTableCellRendererComponent(JTable table,
                                                   Object value,
                                                   boolean isSelected,
                                                   boolean hasFocus,
                                                   int row,
                                                   int column)
      {
      if(isSortable())
        {
        int sortCol = sorter.getSortedColumn();
        if((sortCol < 0) || (sortCol != column))
          setIcon(null);
        else
          setIcon(sorter.isSortedAscending() ? i_sort : i_rsort);
        }
      else
        setIcon(null);
      
      return(super.getTableCellRendererComponent(table, value, isSelected,
                                                 hasFocus, row, column));
      } 
    }

  /* A custom table header that honors disabled state by disallowing column
   * resizing and reordering.
   */

  private class _TableHeader extends JTableHeader
    {

    public _TableHeader(TableColumnModel model)
      {
      super(model);
      }
    
    public boolean getResizingAllowed()
      {
      return(isEnabled() ? super.getResizingAllowed() : false);
      }

    public boolean getReorderingAllowed()
      {
      return(isEnabled() ? super.getReorderingAllowed() : false);
      }    
    }

  }

/* end of source file */
