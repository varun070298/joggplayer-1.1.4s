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
   $Log: BooleanHolder.java,v $
   Revision 1.7  2003/01/19 09:42:38  markl
   Javadoc & comment header updates.

   Revision 1.6  2001/03/12 02:57:37  markl
   Source code cleanup.

   Revision 1.5  1999/07/25 13:39:39  markl
   Added a constructor that accepts a subtype.

   Revision 1.4  1999/07/16 07:12:00  markl
   Modified to subclass HolderObject.

   Revision 1.3  1999/02/09 05:02:56  markl
   added toString() method

   Revision 1.2  1999/01/10 03:41:46  markl
   added GPL header & RCS tag
   ----------------------------------------------------------------------------
*/

package kiwi.util;

/** A mutable holder for a <code>boolean</code> value.
  *
  * @author Mark Lindner
  */

public class BooleanHolder extends HolderObject
  {
  /** The current value. */
  protected boolean value;

  /** Construct a new <code>BooleanHolder</code> with an initial value of
    * <code>false</code> and default subtype of 0.
    */
  
  public BooleanHolder()
    {
    this(false, 0);
    }

  /** Construct a new <code>BooleanHolder</code> with a specified initial
    * value and default subtype of 0.
    *
    * @param value The initial value.
    */
  
  public BooleanHolder(boolean value)
    {
    this(value, 0);
    }
  
  /** Construct a new <code>BooleanHolder</code> with a specified initial
    * value and subtype.
    *
    * @param value The initial value.
    * @param subtype The subtype for this value.
    */
  
  public BooleanHolder(boolean value, int subtype)
    {
    super(subtype);
    
    this.value = value;
    }

  /** Set the <code>BooleanHolder</code>'s value.
    *
    * @param value The new value.
    */
  
  public synchronized final void setValue(boolean value)
    {
    this.value = value;
    }

  /** Get the <code>BooleanHolder</code>'s value.
    *
    * @return The current value.
    */
  
  public synchronized final boolean getValue()
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
    boolean v = ((BooleanHolder)other).getValue();

    return((value == v) ? 0 : (value ? 1 : -1));
    }
  
  }

/* end of source file */
