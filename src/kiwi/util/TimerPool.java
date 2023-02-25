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
   $Log: TimerPool.java,v $
   Revision 1.3  2003/01/19 09:42:39  markl
   Javadoc & comment header updates.

   Revision 1.2  2001/03/12 03:16:51  markl
   *** empty log message ***

   Revision 1.1  1999/04/23 07:31:08  markl
   Initial revision
   ----------------------------------------------------------------------------
*/

package kiwi.util;

/** A concrete implementation of <code>ResourcePool</code> for managing a
 * pool of <code>IntervalTimer</code>s.
 *
 * @author Mark Lindner
 */

public class TimerPool extends ResourcePool
  {
  /** Construct a new <code>TimerPool</code> of the specified size.
   *
   * @param size The number of <code>IntervalTimer</code>s to preallocate.
   */

  public TimerPool(int size)
    {
    super(size);
    }

  /** Construct a new <code>IntervalTimer</code>.
   *
   * @return The new <code>IntervalTimer</code>.
   */
   
  protected Resource constructResource()
    {
    return(new IntervalTimer());
    }
  
  /** Reserve a timer from the pool. If all timers are currently in use, the
   * method blocks until one becomes available.
   *
   * @return An <code>IntervalTimer</code> instance.
   */

  public IntervalTimer reserveTimer()
    {
    IntervalTimer timer = (IntervalTimer)reserveResource();
    return(timer);
    }
  
  /** Release a timer back into the pool.
   *
   * @param timer The timer to release.
   */
   
  public void releaseTimer(IntervalTimer timer)
    {
    releaseResource(timer);
    }
  
  }

/* end of source file */
