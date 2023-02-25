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
   $Log: DateTableCellRenderer.java,v $
   Revision 1.3  2003/01/19 09:50:53  markl
   Javadoc & comment header updates.

   Revision 1.2  2002/08/11 10:09:55  markl
   Removed deprecated call.

   Revision 1.1  2002/08/11 09:51:59  markl
   Readded to package.

   Revision 1.2  2001/09/26 07:05:36  markl
   Version fixes, email address fixes.

   Revision 1.1  2000/08/07 08:24:54  markl
   Import into CVS.
   ----------------------------------------------------------------------------
*/

package kiwi.ui;

import javax.swing.*;
import javax.swing.table.*;
import java.util.*;

import kiwi.text.*;
import kiwi.util.*;

/** A table cell renderer for displaying dates and/or times, formatted
 * according to the rules of the current locale.
 *
 * @author Mark Lindner
 *
 * @see kiwi.text.FormatConstants
 * @see kiwi.util.LocaleManager
 */

public class DateTableCellRenderer extends DefaultTableCellRenderer
  {
  /** A string representation of the "unknown value"; a value that is either
   * of the wrong type or for which there is no available format.
   */
  public static final String VALUE_UNKNOWN = "???";

  private LocaleManager lm = LocaleManager.getDefaultLocaleManager();
  private int type;

  /** Construct a new <code>DateTableCellRenderer</code> for dates.
   */
  
  public DateTableCellRenderer()
    {
    this(FormatConstants.DATE_FORMAT);
    }
  
  /** Construct a new <code>DateTableCellRenderer</code> of the specified
   * type.
   *
   * @param type The fromatting type to be used by this field; one of the
   * constants <code>DATE_FORMAT</code>, <code>TIME_FORMAT/code>, or
   * <code>DATE_TIME_FORMAT</code>, defined in
   * <code>kiwi.text.FormatConstants</code>.
   */
  
  public DateTableCellRenderer(int type)
    {
    this.type = type;
    }

  /** Set the formatting type.
   *
   * @param type The data type to be rendered by this cell renderer. See the
   * constructor for more information.
   */
  
  public void setType(int type)
    {
    this.type = type;
    }

  /** Get the formatting type.
   *
   * @return The data type being rendered by this cell renderer.
   */
  
  public int getType()
    {
    return(type);
    }

  /** Set the value to be displayed by this cell renderer. It is assumed that
   * the object passed in is a <code>Double</code> instance; if any other
   * type of object is passed in, or if the rendering type is not recognized,
   * the <code>VALUE_UNKNOWN</code> string will be rendered in the cell.
   *
   * @param value The value to render (must be a <code>Double</code>).
   */
  
  protected void setValue(Object value)
    {
    Date d = null;
    
    if(value instanceof Date)
      d = (Date)value;
    else if(value instanceof Calendar)
      d = ((Calendar)value).getTime();
    else if(value instanceof DateHolder)
      d = ((DateHolder)value).getValue();
    
    String s = VALUE_UNKNOWN;
    if(d != null)
      {
      switch(type)
        {
        case FormatConstants.DATE_FORMAT:
          s = lm.formatDate(d);
          break;

        case FormatConstants.TIME_FORMAT:
          s = lm.formatTime(d);
          break;
        
        case FormatConstants.DATE_TIME_FORMAT:
          s = lm.formatDateTime(d);
          break;          
        }
      }

    setText(s);
    }
  
  }

/* end of source file */
