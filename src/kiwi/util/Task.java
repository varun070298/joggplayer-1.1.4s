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
   $Log: Task.java,v $
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

/** This class represents an asynchronous task whose progress can be tracked
  * by a <code>ProgressObserver</code>.
  *
  * @author Mark Lindner
  *
  * @see kiwi.util.ProgressObserver
  * @see kiwi.ui.dialog.ProgressDialog
  * @see java.lang.Runnable
  */

public abstract class Task implements Runnable
  {
  private Vector observers;

  /** Construct a new <code>Task</code>. */
  
  public Task()
    {
    observers = new Vector();
    }

  /** Run the task. This method is the body of the thread for this task. */
  
  public abstract void run();

  /** Add a progress observer to this task's list of observers. Observers are
    * notified by the task of progress made.
    *
    * @param observer The observer to add.
    */
   
  public final void addProgressObserver(ProgressObserver observer)
    {
    observers.addElement(observer);
    }

  /** Remove a progress observer from this task's list of observers.
    *
    * @param observer The observer to remove.
    */
  
  public final void removeProgressObserver(ProgressObserver observer)
    {
    observers.removeElement(observer);
    }

  /** Notify all observers about the percentage of the task completed.
    *
    * @param percent The percentage of the task completed, an integer value
    * between 0 and 100 inclusive. Values outside of this range are silently
    * clipped.
    */
  
  protected final void notifyObservers(int percent)
    {
    if(percent < 0) percent = 0;
    else if(percent > 100) percent = 100;
    
    Enumeration e = observers.elements();
    while(e.hasMoreElements())
      ((ProgressObserver)e.nextElement()).setProgress(percent);
    }
  
  }

/* end of source file */
