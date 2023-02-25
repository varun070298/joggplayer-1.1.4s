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
   $Log: DateField.java,v $
   Revision 1.1  2003/01/19 09:37:59  markl
   New class.

   ----------------------------------------------------------------------------
*/

package kiwi.ui;

import java.text.*;
import java.util.*;

import kiwi.util.*;

/** A subclass of <code>DataField</code> for the input and display of
 * dates.
 *
 * @author Mark Lindner
 *
 * @since Kiwi 1.4
 */

public class DateField extends DataField
  {
  private LocaleManager locmgr = LocaleManager.getDefaultLocaleManager();
  private ParsePosition pos = new ParsePosition(0);
  /** The formatter used to parse and format dates in this field. */
  protected DateFormat dateFormat;
  /** The current date entered in this field. */
  protected Date date = null;

  /** Construct a new <code>DateField</code> with the specified width and a
   * default, locale-specific date format.
   *
   * @param widht The width of the field.
   */
  
  public DateField(int width)
    {
    this(width, null);
    }

  /** Construct a new <code>DateField</code> with the specified width and
   * date format.
   *
   * @param width The width of the field.
   * @param format The date format.
   */
  
  public DateField(int width, String format) throws IllegalArgumentException
    {
    super(width);

    LocaleManager locmgr = LocaleManager.getDefaultLocaleManager();
    
    if(format == null)
      dateFormat = locmgr.getShortDateFormat();
    else
      dateFormat = new SimpleDateFormat(format);

    setFont(KiwiUtils.boldFont);
    }

  /** Parse the contents of the field as a date, and return the date.
   *
   * @return The parsed date, or <code>null</code> if parsing failed.
   */

  public Date getDate()
    {
    checkInput();
    
    return(date);
    }

  /** Set the data to be displayed by this field. The date is formatted as a
   * string, according to the rules of the current locale, and displayed in the
   * field. Invalid input flagging is automatically turned off.
   */

  public void setDate(Date date)
    {
    this.date = date;

    setText((date == null) ? "" : dateFormat.format(date));
    
    invalid = false;
    paintInvalid(invalid);
    }

  /** Determine if the given input is valid for this field.
   *
   * @return <code>true</code> if the input is valid, and <code>false</code>
   * otherwise.
   */
  
  protected boolean checkInput()
    {
    invalid = false;

    try
      {
      pos.setIndex(0);
      String s = getText();
      Date d = dateFormat.parse(s, pos);
      trapGarbage(s);
      setDate(d);
      }
    catch(ParseException ex)
      {
      invalid = true;
      date = null;
      }

    paintInvalid(invalid);

    return(!invalid);
    }  

  /*
   */
  
  private void trapGarbage(String s) throws ParseException
    {
    if(pos.getIndex() != s.length())
      throw(new ParseException("Garbage in string " + s, pos.getIndex()));
    }
    
  }

/* end of source file */
