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
   $Log: LoggingMux.java,v $
   Revision 1.4  2003/01/19 09:42:39  markl
   Javadoc & comment header updates.

   Revision 1.3  2001/03/12 02:57:41  markl
   Source code cleanup.

   Revision 1.2  1999/02/09 05:07:22  markl
   minor fixes

   Revision 1.1  1999/02/02 07:35:43  markl
   Initial revision
   ----------------------------------------------------------------------------
*/

package kiwi.util;

import java.util.*;

/** A logging multiplexor. This class manages a set of
  * <code>LoggingEndpoint</code>s and itself implements the
  * <code>LoggingEndpoint</code> interface. It may be use to direct logging
  * messages to several endpoints simultaneously. For example, an application
  * may send messages to both a console and a file.
  *
  * @see kiwi.util.LoggingEndpoint
  *
  * @author Mark Lindner
  */

public class LoggingMux implements LoggingEndpoint
  {
  private Vector v;

  /** Construct a new <code>LoggingMux</code>.
    */
  
  public LoggingMux()
    {
    v = new Vector();
    }

  /** Log a message to all endpoints in this set.
    */
  
  public void logMessage(int type, String message)
    {
    Enumeration e = v.elements();
    while(e.hasMoreElements())
      ((LoggingEndpoint)e.nextElement()).logMessage(type, message);
    }

  /** Close this set of endpoints. Equivalent to <code>close(false)</code>.
    */
  
  public void close()
    {
    close(false);
    }
  
  /** Close this set of endpoints.
    *
    * @param closeEndpoints If <code>true</code>, in addition to removing every
    * <code>LoggingEndpoint</code> from its list, the <code>LoggingMux</code>
    * closes each <code>LoggingEndpoint</code> explicitly via a call to its
    * <code>close()</code> method.
    */
  
  public void close(boolean closeEndpoints)
    {
    if(closeEndpoints)
      {
      Enumeration e = v.elements();
      while(e.hasMoreElements())
        ((LoggingEndpoint)e.nextElement()).close();
      }
    
    removeAllLoggingEndpoints();
    }

  /** Add a <code>LoggingEndpoint</code> to the set.
    *
    * @param endpoint The <code>LoggingEndpoint</code> to add.
    */
  
  public void addLoggingEndpoint(LoggingEndpoint endpoint)
    {
    v.addElement(endpoint);
    }

  /** Remove a <code>LoggingEndpoint</code> from the set.
    *
    * @param endpoint The <code>LoggingEndpoint</code> to remove.
    */
  
  public void removeLoggingEndpoint(LoggingEndpoint endpoint)
    {
    v.removeElement(endpoint);
    }

  /** Remove all <code>LoggingEndpoint</code>s from the set.
    */
  
  public void removeAllLoggingEndpoints()
    {
    v.removeAllElements();
    }
  
  }

/* end of source file */
