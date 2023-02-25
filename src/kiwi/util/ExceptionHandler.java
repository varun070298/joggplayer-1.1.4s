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
   $Log: ExceptionHandler.java,v $
   Revision 1.3  2003/01/19 09:42:39  markl
   Javadoc & comment header updates.

   Revision 1.2  2001/03/12 02:57:39  markl
   Source code cleanup.

   Revision 1.1  1999/07/25 13:40:17  markl
   Initial revision
   ----------------------------------------------------------------------------
*/

package kiwi.util;

/** An interface for delivering an exception to a handler without using the
 * <i>try / catch</i> programming construct. Sometimes an exception may be
 * thrown within a method that cannot itself throw an exception, for example if
 * the method is an implementation of an interface and that interface does not
 * define a <i>throws</i> clause for that method's signature.
 * <p>
 * This interface may be used to deliver caught Exceptions to an appropriate
 * handler, perhaps in a different object altogether.
 *
 * @author Mark Lindner
 */

public interface ExceptionHandler
  {
  /** Handle an exception.
   *
   * @param ex The exception that was raised.
   */
  
  public void exceptionRaised(Exception ex);
  }

/* end of source file */
