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
   $Log: HolderObject.java,v $
   Revision 1.3  2003/01/19 09:42:39  markl
   Javadoc & comment header updates.

   Revision 1.2  2001/03/12 02:57:40  markl
   Source code cleanup.

   Revision 1.1  1999/07/16 07:13:06  markl
   Initial revision
   ----------------------------------------------------------------------------
*/

package kiwi.util;

/** An abstract base class for mutable holder objects. This base class declares
 * a comparator method that may be used to determine the relative order of
 * two holder objects, based on the cardinality of their respective values.
 * It also introduces the notion of a <i>subtype</i>, which is an arbitrary
 * integer that may be used to store additional information about the value
 * stored in this holder. One possible use for the subtype field is to store
 * a format type (such as one of the data format constants defined in the
 * <code>kiwi.text.FormatConstants</code> interface); such information can be
 * useful to tree and table cell renderers, for example.
 *
 * @see kiwi.text.FormatConstants
 *
 * @author Mark Lindner
 */

public abstract class HolderObject
  {
  /** The subtype for the value stored by this holder. */
  protected int subtype;

  /** Construct a new <code>HolderObject</code>. */
  
  protected HolderObject()
    {
    this(0);
    }

  /** Construct a new <code>HolderObject</code> with the specified subtype.
   *
   * @param subtype The subtype.
   */
  
  protected HolderObject(int subtype)
    {
    this.subtype = subtype;
    }
  
  /** Compare the value in this <code>HolderObject</code> to the value in
   * another <code>HolderObject</code>. It is assumed that the two values
   * are of the same type (hence that the holders are also of the same type).
   *
   * @param other The <code>HolderObject</code> to compare against.
   * @return <code>-1</code> if this object is "less than" the other object;
   * <code>1</code> if this object is "greater than" the other object, and
   * <code>0</code> if the objects are "equal."
   */
  
  public abstract int compareTo(HolderObject other);

  /** Set the subtype for the value stored by this holder.
   *
   * @param subtype The new subtype.
   */
  
  public void setSubtype(int subtype)
    {
    this.subtype = subtype;
    }

  /** Get the subtype for the value stored by this holder.
   *
   * @return The current subtype. The default subtype is 0.
   */
  
  public int getSubtype()
    {
    return(subtype);
    }
  
  }

/* end of source file */
