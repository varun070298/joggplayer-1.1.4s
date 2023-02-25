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
   $Log: Sequence.java,v $
   Revision 1.4  2003/01/19 09:42:39  markl
   Javadoc & comment header updates.

   Revision 1.3  2001/03/12 03:16:50  markl
   *** empty log message ***

   Revision 1.2  1999/01/10 03:56:22  markl
   added GPL header & RCS tag
   ----------------------------------------------------------------------------
*/

package kiwi.util;

import java.util.Enumeration;

/** An extension of <code>Enumeration</code> that allows rewinding to the first
  * component and retrieval of the number of items in the collection. This
  * interface can be useful in situations where an immutable collection of
  * (possibly mutable) objects must be passed between objects, and where the
  * sequential access provided by <code>Enumeration</code> is not sufficiently
  * flexible.
  *
  * @author Mark Lindner
  */

public interface Sequence extends Enumeration
  {
  /** Rewind the sequence back to the first item. */
  
  public void rewind();

  /** Get the length of the sequence.
    *
    * @return The number of items in the sequence.
    */

  public int getSize();
  }

/* end of source file */

