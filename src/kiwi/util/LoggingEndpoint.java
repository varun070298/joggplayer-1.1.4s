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
   $Log: LoggingEndpoint.java,v $
   Revision 1.5  2003/01/19 09:42:39  markl
   Javadoc & comment header updates.

   Revision 1.4  2001/09/26 07:05:36  markl
   Version fixes, email address fixes.

   Revision 1.3  2001/03/12 05:43:34  markl
   Javadoc cleanup.

   Revision 1.2  1999/01/10 03:47:05  markl
   added GPL header & RCS tag
   ----------------------------------------------------------------------------
*/

package kiwi.util;

import java.io.IOException;

/** Logging endpoint interface. A logging endpoint accepts messages and writes
  * them to a file, a graphical console window, or some other type of data
  * sink.
  *
  * @author Mark Lindner
  */

public interface LoggingEndpoint
  {

  /** Informational message type. */
  public static final int INFO = 0;

  /** Status message type. */
  public static final int STATUS = 1;

  /** Warning message type. */
  public static final int WARNING = 2;

  /** Error condition message type. */
  public static final int ERROR = 3;

  /** Accept a new message. Writes the message to the data sink.
    *
    * @param type The message type; one of the static constants defined above.
    * @param message The message.
    */

  public void logMessage(int type, String message);

  /** Close the logging endpoint. The logging endpoint is closed. Once a
    * logging endpoint is closed, it cannot accept any more messages.
    */

  public void close();
  }

/* end of source file */
