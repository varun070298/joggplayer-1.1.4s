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
   $Log: ParsingException.java,v $
   Revision 1.5  2003/01/19 09:34:27  markl
   Javadoc & comment header updates.

   Revision 1.4  2001/03/12 02:18:28  markl
   Source code cleanup.

   Revision 1.3  1999/06/30 08:17:32  markl
   Minor fixes to message formatting.

   Revision 1.2  1999/01/10 03:37:18  markl
   added GPL header & RCS tag
   ----------------------------------------------------------------------------
*/

package kiwi.text;

/** General-purpose parsing exception.
  *
  * @author Mark Lindner
  */

public class ParsingException extends Exception
  {
  private int line = -1;
  private String message = "";

  /** Construct a new <code>ParsingException</code>.
    *
    * @param message The exception message.
    */

  public ParsingException(String message)
    {
    this(message, -1);
    }

  /** Construct a new <code>ParsingException</code>.
    *
    * @param message The exception message.
    * @param line The line number in the input where the exception occurred.
    */

  public ParsingException(String message, int line)
    {
    super(message);
    this.message = message;
    this.line = line;
    }

  /** Get the line number of this exception. If no line number is available,
    * this method returns -1.
    */

  public int getLine()
    {
    return(line);
    }

  /** Get the message of this exception. */

  public String getMessage()
    {
    return(message);
    }

  /** Convert the parsing exception to a string that contains the message and
    * line number.
    */

  public String toString()
    {
    String msg = getMessage();
    if(line >= 0)
      msg += (" on line " + line);
    
    return(msg);
    }

  }

/* end of source file */
