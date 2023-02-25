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
   $Log: DateHolder.java,v $
   Revision 1.4  2003/01/19 09:42:38  markl
   Javadoc & comment header updates.

   Revision 1.3  2001/03/12 02:57:39  markl
   Source code cleanup.

   Revision 1.2  1999/07/19 03:58:48  markl
   Set default subtype.

   Revision 1.1  1999/07/16 07:12:31  markl
   Initial revision
   ----------------------------------------------------------------------------
*/

package kiwi.util;

import java.util.*;

import kiwi.text.*;

/** A mutable holder for a <code>Date</code> value.
 *
 * @author Mark Lindner
 */

public class DateHolder extends HolderObject
  {
  /** The current value. */
  protected Date value;
  
  /** Construct a new <code>DateHolder</code> with an initial value of the
   * current date and time, and a default subtype of
   * <code>FormatConstants.DATE_FORMAT</code>.
   *
   * @see kiwi.text.FormatConstants
   */
  
  public DateHolder()
    {
    this(new Date());
    }

  /** Construct a new <code>DateHolder</code> with a specified initial
    * value, and a default subtype of
    * <code>FormatConstants.DATE_FORMAT</code>.
    *
    * @param value The initial value.
    */
  
  public DateHolder(Date value)
    {
    this(value, FormatConstants.DATE_FORMAT);
    }

  /** Construct a new <code>DateHolder</code> with a specified initial
    * value and subtype. A subtype is particularly useful for a
    * <code>Date</code> object, since it may be used to specify which part
    * of the value is significant (such as the date, the time, or both).
    *
    * @param value The initial value.
    */
  
  public DateHolder(Date value, int subtype)
    {
    super(subtype);
    
    this.value = value;
    }

  /** Set the <code>DateHolder</code>'s value.
    *
    * @param value The new value.
    */
  
  public synchronized final void setValue(Date value)
    {
    this.value = value;
    }

  /** Get the <code>DateHolder</code>'s value.
    *
    * @return The current value.
    */
  
  public synchronized final Date getValue()
    {
    return(value);
    }

  /** Get a string representation for this object. */
  
  public String toString()
    {
    return(String.valueOf(value));
    }

  /** Compare this holder object to another. */
  
  public int compareTo(HolderObject other)
    {
    Date v = ((DateHolder)other).getValue();

    return(value.before(v) ? -1 : (value.after(v) ? 1 : 0));
    }
  
  }

/* end of source file */
