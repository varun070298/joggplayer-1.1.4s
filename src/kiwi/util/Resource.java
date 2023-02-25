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
   $Log: Resource.java,v $
   Revision 1.4  2003/01/19 09:42:39  markl
   Javadoc & comment header updates.

   Revision 1.3  2001/03/12 03:16:49  markl
   *** empty log message ***

   Revision 1.2  1999/04/23 07:32:57  markl
   Removed unneeded method.

   Revision 1.1  1999/04/19 05:32:24  markl
   Initial revision
   ----------------------------------------------------------------------------
*/

package kiwi.util;

/** An interface that describes a <i>resource</i>. A resource is an exclusive
 * entity that may be in use by one thread at a time; a thread must reserve a
 *  resource from a resource pool, use the resource, and then release the
 * resource so that it may be used by another thread.
 *
 * @see kiwi.util.ResourcePool
 * 
 * @author Mark Lindner
 */

public interface Resource
  {
  /** Reserve the resource. */
  
  public void reserve();

  /** Release the resource. */
  
  public void release();
  }

/* end of source file */
