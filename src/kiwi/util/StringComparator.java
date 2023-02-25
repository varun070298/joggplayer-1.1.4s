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
   $Log: StringComparator.java,v $
   Revision 1.3  2003/01/19 09:42:39  markl
   Javadoc & comment header updates.

   Revision 1.2  2001/03/12 03:16:50  markl
   *** empty log message ***

   Revision 1.1  1999/06/08 06:46:24  markl
   Initial revision
   ----------------------------------------------------------------------------
*/

package kiwi.util;

/** A string comparator. This class compares the string representations of
 * two objects.
 *
 * @author Mark Lindner
 */

public class StringComparator implements Comparator
  {
  private boolean caseSensitive;
  
  /** Construct a new case-sensitive <code>StringComparator</code>.
   */
  
  public StringComparator()
    {
    this(true);
    }
  
  /** Construct a new <code>StringComparator</code>.
   *
   * @param caseSensitive A flag specifying whether comparisons will be
   * case-sensitive.
   */
  
  public StringComparator(boolean caseSensitive)
    {
    this.caseSensitive = caseSensitive;
    }

  /** Determine if this comparator is case-sensitive.
   *
   * @return <code>true</code> if the comparator is case-sensitive, and
   * <code>false</code> otherwise.
   */
  
  public boolean isCaseSensitive()
    {
    return(caseSensitive);
    }
  
  /** Compare the string representations of two objects. A
   * <code>toString()</code> is performed on both objects, and then the
   * resulting strings are compared using the <code>compareTo()</code> method
   * of the <code>String</code> class.
   *
   * @param a The first object.
   * @param b The second object.
   * @return <code>0</code> if the objects are equal, <code>-1</code> if
   * <code>a</code> is less than <code>b</code>, and <code>1</code> if
   * <code>a</code> is greater than <code>b</code>.
   */

  public int compare(Object a, Object b)
    {
    if(caseSensitive)
      return(a.toString().compareTo(b.toString()));
    else
      return(a.toString().toLowerCase()
             .compareTo(b.toString().toLowerCase()));
    }
  
  }

/* end of source file */
