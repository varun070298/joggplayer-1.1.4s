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
   $Log: ListConsumer.java,v $
   Revision 1.4  2003/01/19 09:42:39  markl
   Javadoc & comment header updates.

   Revision 1.3  2001/03/12 02:57:41  markl
   Source code cleanup.

   Revision 1.2  1999/01/10 03:47:05  markl
   added GPL header & RCS tag
   ----------------------------------------------------------------------------
*/

package kiwi.util;

/**
  * An interface for receiving a list of objects from another object.
  *
  * @author Mark Lindner
  */

public interface ListConsumer
  {
  
  /** Receive the next item from the source.
    *
    * @param item The item.
    */

  public void addItem(Object item);
  }

/* end of source file */
