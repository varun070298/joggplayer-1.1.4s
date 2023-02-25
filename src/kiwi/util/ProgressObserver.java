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
   $Log: ProgressObserver.java,v $
   Revision 1.5  2003/01/19 09:42:39  markl
   Javadoc & comment header updates.

   Revision 1.4  2002/08/11 09:49:19  markl
   Javadoc fixes.

   Revision 1.3  2001/03/12 03:16:49  markl
   *** empty log message ***

   Revision 1.2  1999/01/10 03:55:05  markl
   added GPL header & RCS tag
   ----------------------------------------------------------------------------
*/

package kiwi.util;

/** This class defines the behavior of an object that wishes to receive
  * periodic updates on the progress of a lengthy task.
  *
  * @author Mark Lindner
  */

public interface ProgressObserver 
  {
  /** Set the progress amount to <code>progress</code>, which is a value
    * between 0 and 100. (Out of range values should be silently clipped.)
    *
    * @param progress The percentage of the task completed.
    */

  public void setProgress(int progress);
  }

/* end of source file */
