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
   $Log: NumericCellEditor.java,v $
   Revision 1.4  2003/01/19 09:50:53  markl
   Javadoc & comment header updates.

   Revision 1.3  2001/03/12 09:27:58  markl
   Source code and Javadoc cleanup.

   Revision 1.2  1999/07/25 13:40:54  markl
   Minor enhancements and bug fixes.

   Revision 1.1  1999/07/12 08:51:41  markl
   Initial revision
   ----------------------------------------------------------------------------
*/

package kiwi.ui;

import java.awt.*;
import java.text.*;
import javax.swing.*;

/** A cell editor for editing numeric fields, including integer, decimal,
 * percentage, and currency amounts, formatted according to the rules of the
 * current locale.
 *
 * @author Mark Lindner
 *
 * @see kiwi.text.FormatConstants
 * @see kiwi.util.LocaleManager
 * @see kiwi.ui.NumericField
 * @see kiwi.ui.NumericTableCellRenderer
 * @see kiwi.db.DomainObjectFieldAdapter
 */

public class NumericCellEditor extends DefaultCellEditor
  {
  private NumericField field;

  /** Construct a new <code>NumericCellEditor</code> of the specified type.
   *
   * @param type The data type to be edited by this field; one of the
   * constants <code>CURRENCY_FORMAT</code>, <code>DECIMAL_FORMAT</code>,
   * <code>INTEGER_FORMAT</code< or <code>PERCENTAGE_FORMAT</code>, defined in
   * <code>kiwi.text.FormatConstants</code>.
   */
  
  public NumericCellEditor(int type)
    {
    this(type, 2);
    }

  /** Construct a new <code>NumericCellEditor</code> of the specified type and
   * number of decimals displayed.
   *
   * @param type The data type to be edited by this field; one of the
   * constants <code>CURRENCY_FORMAT</code>, <code>DECIMAL_FORMAT</code>,
   * <code>INTEGER_FORMAT</code< or <code>PERCENTAGE_FORMAT</code>, defined in
   * <code>kiwi.text.FormatConstants</code>.
   * @param decimals The number of decimal places to be displayed (for
   * non-integer values only).
   */
  
  public NumericCellEditor(int type, int decimals)
    {
    super(new NumericField(1, type));
    
    field = (NumericField)editorComponent;
    field.setDecimals(decimals);
    }

  /** Stop cell editing. This method stops cell editing (effectively
   * committing the edit) only if the data entered is validated successfully.
   *
   * @return <code>true</code> if cell editing may stop, and <code>false</code>
   * otherwise.
   */
  
  public final boolean stopCellEditing()
    {
    return(validate());
    }

  /* perform the validation */
  
  private boolean validate()
    {
    boolean ok = field.validateInput();

    if(ok)
      fireEditingStopped();

    return(ok);
    }

  /** Get the value currently in the cell editor.
   *
   * @return The current value, as a <code>Double</code>.
   */
   
  public Object getCellEditorValue()
    {
    field.validateInput();
    
    return(new Double(field.getValue()));
    }

  /** Set the formatting type.
   *
   * @param type The data type to be edited by this cell editor. See the
   * constructor for more information.
   */  
    
  public void setType(int type)
    {
    field.setType(type);
    }

  /** Get the formatting type.
   *
   * @return The data type being edited by this cell editor.
   */
  
  public int getType()
    {
    return(field.getType());
    }

  /** Set the number of decimal places to display for non-integer values.
   *
   * @param decimals The number of decimal places.
   * @exception java.lang.IllegalArgumentException If <code>decimals</code>
   * is less than 0.
   */
  
  public void setDecimals(int decimals)
    {
    field.setDecimals(decimals);
    }

  /** Get the number of decimal places being displayed by this cell editor.
   *
   * @return The number of decimal places.
   */
  
  public int getDecimals()
    {
    return(field.getDecimals());
    }

  /* Prepare the editor for a value. */
  
  private Component _prepareEditor(Object value)
    {
    if(value.getClass().getSuperclass() == Number.class)
      field.setValue(((Number)value).doubleValue());

    return(field);
    }

  /** Get an editor for a JTable. */
  
  public Component getTableCellEditorComponent(JTable table, Object value,
                                               boolean isSelected, int row,
                                               int column)
    {
    return(_prepareEditor(value));
    }

  /** Get an editor for a JTree. */
  
  public Component getTreeCellEditorComponent(JTree tree, Object value,
                                              boolean isSelected,
                                              boolean expanded, boolean leaf,
                                              int row)
    {
    return(_prepareEditor(value));
    }

  }

/* end of source file */
