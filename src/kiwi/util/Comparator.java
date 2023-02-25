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
   $Log: Comparator.java,v $
   Revision 1.3  2003/01/19 09:42:38  markl
   Javadoc & comment header updates.

   Revision 1.2  2001/03/12 02:57:38  markl
   Source code cleanup.

   Revision 1.1  1999/06/08 06:45:34  markl
   Initial revision
   ----------------------------------------------------------------------------
*/

package kiwi.util;

/** An interface describing a comparator--an object that determines the
 * relative order of two objects.
 *
 * @author Mark Lindner
 */

public interface Comparator
  {
  
  /** Compare two objects.
   *
   * @param a The first object.
   * @param b The second object.
   * @return 0 if the objects are equal, -1 if <code>a</code> is less than
   * <code>b</code>, and 1 if <code>a</code> is greater than <code>b</code>.
   */
  
  public int compare(Object a, Object b);
  }

/* end of source file */
