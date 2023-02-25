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
   $Log: ArraySequence.java,v $
   Revision 1.5  2003/01/19 09:42:38  markl
   Javadoc & comment header updates.

   Revision 1.4  2001/03/12 05:43:34  markl
   Javadoc cleanup.

   Revision 1.3  2001/03/12 02:57:37  markl
   Source code cleanup.

   Revision 1.2  1999/01/10 03:41:46  markl
   added GPL header & RCS tag
   ----------------------------------------------------------------------------
*/

package kiwi.util;

import java.util.*;

/** An implementation of <code>Sequence</code> for wrapping arrays.
  *
  * @author Mark Lindner
  */

public class ArraySequence implements Sequence
  {
  private Object array[];
  private int offset = 0;

  /** Construct a new <code>ArraySequence</code>.
    *
    * @param array The array to wrap.
    */

  public ArraySequence(Object array[])
    {
    this.array = array;
    }

  /** Check if the sequence has more elements.
    *
    * @return <code>true</b> if there are more elements, and <code>false</code>
    * otherwise.
    */

  public boolean hasMoreElements()
    {
    return(offset < array.length);
    }

  /** Get the next element in the sequence.
    *
    * @return The next element in the sequence, or <code>null</code> if the
    * end has been reached.
    */

  public Object nextElement()
    {
    if(offset >= array.length)
      throw(new NoSuchElementException());
    else
      return(array[offset++]);
    }

  /** Rewind the sequence to the beginning. */

  public void rewind()
    {
    offset = 0;
    }

  /** Get the size of the sequence.
    *
    * @return The number of items in the sequence.
    */

  public int getSize()
    {
    return(array.length);
    }
  
  }

/* end of source file */
