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
   $Log: RawLoggingEndpoint.java,v $
   Revision 1.4  2003/01/19 09:42:39  markl
   Javadoc & comment header updates.

   Revision 1.3  2001/03/12 03:16:49  markl
   *** empty log message ***

   Revision 1.2  1999/01/10 03:55:05  markl
   added GPL header & RCS tag
   ----------------------------------------------------------------------------
*/

package kiwi.util;

import java.io.*;

/** An implementation of <code>LoggingEndpoint</code> for standard error.
  *
  * @author Mark Lindner
  */

public class RawLoggingEndpoint implements LoggingEndpoint
  {
  private static final String types[]
    = {"INFO   ", "STATUS ", "WARNING", "ERROR  " };

  /** Construct a new <code>RawLoggingEndpoint</code>.
    */

  public RawLoggingEndpoint()
    {
    }
  
  /** Write a message to standard error.
    *
    * @param type The message type; one of the constants defined in
    * <code>LoggingEndpoint</code>.
    * @param message The message.
    */

  public void logMessage(int type, String message)
    {
    if((type < 0) || (type > 3)) type = 1;

    System.err.println(types[type] + " - " + message);
    }

  /** Close the logging endpoint. This method is effectively a no-op, since it
    * is usually undesirable to close the standard error stream.
    */

  public void close()
    {
    }
  
  }

/* end of source file */
