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
   $Log: VectorSequence.java,v $
   Revision 1.4  2003/01/19 09:42:39  markl
   Javadoc & comment header updates.

   Revision 1.3  2001/03/12 03:16:51  markl
   *** empty log message ***

   Revision 1.2  1999/01/10 03:56:22  markl
   added GPL header & RCS tag
   ----------------------------------------------------------------------------
*/

package kiwi.util;

import java.util.*;

/**
  * An implementation of <code>Sequence</code> for wrapping
  * <code>Vector</code>s.
  *
  * @author Mark Lindner
  */

public class VectorSequence implements Sequence
  {
  private Vector vector;
  private int offset = 0;

  /** Construct a new <code>VectorSequence</code>.
    *
    * @param vector The <code>Vector</code> to wrap.
    */

  public VectorSequence(Vector vector)
    {
    this.vector = vector;
    }

  /** Check if the sequence has more elements.
    *
    * @return <code>true</code> if there are more elements, and
    * <code>false</code> otherwise.
    */

  public boolean hasMoreElements()
    {
    return(offset < vector.size());
    }

  /** Get the next element in the sequence.
    *
    * @return The next element in the sequence, or <code>null</code> if the
    * end has been reached.
    */

  public Object nextElement()
    {
    if(offset >= vector.size())
      throw(new NoSuchElementException());
    else
      return(vector.elementAt(offset++));
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
    return(vector.size());
    }
  
  }

/* end of source file */
