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
   $Log: ResourcePool.java,v $
   Revision 1.4  2003/01/19 09:42:39  markl
   Javadoc & comment header updates.

   Revision 1.3  2001/06/26 06:10:40  markl
   Fixed typo in javadoc.

   Revision 1.2  2001/03/12 03:16:50  markl
   *** empty log message ***

   Revision 1.1  1999/04/23 07:33:25  markl
   Initial revision
   ----------------------------------------------------------------------------
*/

package kiwi.util;

import java.util.*;

/** An abstract class that represents a pool of instances of some resource.
 * See <code>TimerPool</code> for an example concrete implementation. Accesses
 * to the pool are threadsafe so there is no possibility of contention for
 * the resource.
 *
 * @see kiwi.util.Resource
 * @see kiwi.util.TimerPool
 *
 * @author Mark Lindner
 */

public abstract class ResourcePool
  {
  private int size;
  private Stack reservedList, availableList;

  /** Construct a new <code>ResourcePool</code> of the given size.
   *
   * @param size The number of instances of a resource to preallocate in this
   * pool.
   */
   
  public ResourcePool(int size)
    {
    this.size = size;
    reservedList = new Stack();
    availableList = new Stack();

    for(int i = 0; i < size; i++)
      availableList.push(constructResource());
    }

  /** Reserve one instance of the resource. If all instances are currently in
   * use, this method blocks until one becomes available.
   *
   * @return An instance of the <code>Resource</code>.
   */
   
  public synchronized Resource reserveResource()
    {
    for(;;)
      {
      if(!availableList.isEmpty())
        break;
      
      try
        {
        System.err.println("All resource instances in use; waiting...");
        wait();
        }
      catch(InterruptedException ex)
        {
        }
      }
    
    Resource resource = (Resource)availableList.pop();
    reservedList.push(resource);
    resource.reserve();

    return(resource);
    }

  /** Release the given resource. If the resource is not currently reserved,
   * this method does nothing. Note that it is the caller's responsibility
   * to pass the correct resource to this method; the method does not check
   * if the calling thread actually has the specified resource reserved.
   *
   * @param resource The <code>Resource</code> to release.
    */
  
  public synchronized void releaseResource(Resource resource)
    {
    if(!(reservedList.contains(resource)))
      throw(new IllegalArgumentException(
        "Resource not managed by this pool!"));
    
    reservedList.removeElement(resource);
    resource.release();
    availableList.push(resource);
    notify();
    }

  /** Construct an instance of the resource that is managed by this pool.
   * The constructor calls this method repeatedly to pre-build the number
   * of instances specified as its argument.
   *
   * @return The newly-constructed <code>Resource</code> instance.
   */
   
  abstract protected Resource constructResource();
  
  /** Get the total number of resource instances in this pool.
   *
   * @return The total number of instances.
   */
  
  public int getTotalResourceCount()
    {
    return(size);
    }

  /** Get the number of resource instances that are currently in use.
   *
   * @return The number of instances that are in use.
   */
   
  public synchronized int getUsedResourceCount()
    {
    return(reservedList.size());
    }

  /** Get the number of resource instances that are currently available.
   *
   * @return The number of instances that are available.
   */

  public synchronized int getAvailableResourceCount()
    {
    return(availableList.size());
    }

  }

/* end of source file */
