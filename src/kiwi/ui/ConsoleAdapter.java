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
   $Log: ConsoleAdapter.java,v $
   Revision 1.4  2003/01/19 09:50:23  markl
   Replaced deprecated OutputLoop calls.

   Revision 1.3  2001/03/12 09:27:53  markl
   Source code and Javadoc cleanup.

   Revision 1.2  1999/01/10 01:00:58  markl
   added GPL header & RCS tag
   ----------------------------------------------------------------------------
*/

package kiwi.ui;

import java.io.*;

import kiwi.io.*;
import kiwi.util.*;

/** Adapter for using a logging endpoint with the standard output stream.
  *
  * This class allows an arbitrary logging endpoint to be connected (via an
  * <code>OutputLoop</code>) to the standard output stream,
  * <code>System.out</code>. The class starts a separate thread which reads
  * messages from the pipe and writes them to the logging endpoint. Message
  * severity is specified using a message prefix; "warning:", "status:",
  * "info:", or "error:". The default severity is <code>STATUS</code>. For
  * example:
  * <p>
  * <code>System.out.println("info:Program started.");</code>
  * <p>
  * will log the message "Program started." as an <code>INFO</code> message.
  *
  * @see kiwi.ui.ConsoleFrame
  * @see kiwi.io.OutputLoop
  *
  * @author Mark Lindner
  */

public class ConsoleAdapter
  {
  private LoggingEndpoint log;
  private OutputLoop pipe;
  private BufferedReader reader;
  private Thread thread;

  /** Construct a new <code>ConsoleAdapter</code> for the specified logging
    * endpoint.
    *
    * @param log The <code>LoggingEndpoint</code> to use.
    * @exception java.io.IOException If the output loop could not be created.
    */

  public ConsoleAdapter(LoggingEndpoint log) throws IOException
    {
    this.log = log;

    pipe = new OutputLoop();
    pipe.setActive(true);

    reader = new BufferedReader(new InputStreamReader(pipe.getInputStream()));

    Runnable r = new Runnable()
      {
      public void run()
	{
	_run();
	}
      };

    thread = new Thread(r);
    thread.start();
    }

  /* thread body */

  private void _run()
    {
    String s;
    int type;

    try
      {
      while((s = reader.readLine()) != null)
	{
	type = log.STATUS;

	if(s.startsWith("status:"))
	  {
	  s = s.substring(7);
	  type = log.STATUS;
	  }
	else if(s.startsWith("error:"))
	  {
	  s = s.substring(6);
	  type = log.ERROR;
	  }
	else if(s.startsWith("info:"))
	  {
	  s = s.substring(5);
	  type = log.INFO;
	  }
	else if(s.startsWith("warning:"))
	  {
	  s = s.substring(8);
	  type = log.WARNING;
	  }

	log.logMessage(type, s);
	}
      }
    catch(IOException ex)
      {
      log.close();
      }
    }

  }

/* end of source file */
