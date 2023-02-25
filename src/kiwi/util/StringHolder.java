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
   $Log: StringHolder.java,v $
   Revision 1.9  2003/01/19 09:42:39  markl
   Javadoc & comment header updates.

   Revision 1.8  2001/03/20 00:54:56  markl
   Fixed deprecated calls.

   Revision 1.7  2001/03/12 03:16:51  markl
   *** empty log message ***

   Revision 1.6  2000/06/09 01:47:40  markl
   Modified to use Collator to compare strings.

   Revision 1.5  1999/07/25 13:39:39  markl
   Added a constructor that accepts a subtype.

   Revision 1.4  1999/07/16 07:12:50  markl
   Modified to subclass HolderObject.

   Revision 1.3  1999/02/09 05:02:56  markl
   added toString() method

   Revision 1.2  1999/01/10 03:56:22  markl
   added GPL header & RCS tag
   ----------------------------------------------------------------------------
*/

package kiwi.util;

import java.text.*;

/** A mutable holder for a <code>String</code> value.
  *
  * @author Mark Lindner
  */

public class StringHolder extends HolderObject
  {
  /** The current value. */
  protected String value;

  /** Construct a new <code>StringHolder</code> with an initial value of
   * <code>null</code> and default subtype of <code>0</code>.
   */
  
  public StringHolder()
    {
    this(null, 0);
    }

  /** Construct a new <code>StringHolder</code> with a specified initial
    * value and default subtype of <code>0</code>.
    *
    * @param value The initial value.
    */
  
  public StringHolder(String value)
    {
    this(value, 0);
    }

  /** Construct a new <code>StringHolder</code> with a specified initial
    * value and subtype.
    *
    * @param value The initial value.
    * @param subtype The subtype for this value.
    */
  
  public StringHolder(String value, int subtype)
    {
    super(subtype);
    
    this.value = value;
    }
  
  /** Set the <code>StringHolder</code>'s value.
    *
    * @param value The new value.
    */
  
  public synchronized final void setValue(String value)
    {
    this.value = value;
    }

  /** Get the <code>StringHolder</code>'s value.
    *
    * @return The current value.
    */
  
  public synchronized final String getValue()
    {
    return(value);
    }

  /** Get a string representation for this object. */
  
  public String toString()
    {
    return(value);
    }

  /** Compare this holder object to another. */
  
  public int compareTo(HolderObject other)
    {
    Collator collator = LocaleManager.getDefaultLocaleManager().getCollator();
    
    String v = ((StringHolder)other).getValue();

    return(collator.compare(value, v));
    }
  
  }

/* end of source file */
