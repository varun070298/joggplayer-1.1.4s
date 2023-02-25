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
   $Log: CommandProcessor.java,v $
   Revision 1.4  2003/01/19 09:42:38  markl
   Javadoc & comment header updates.

   Revision 1.3  2001/03/12 02:57:38  markl
   Source code cleanup.

   Revision 1.2  1999/01/10 03:41:46  markl
   added GPL header & RCS tag
   ----------------------------------------------------------------------------
*/

package kiwi.util;

/** Helper class for the <code>CommandDispatcher</code>.
  * <code>CommandProcessor</code> is a minimal interface that declares error
  * handling methods for the <code>CommandDispatcher</code>; however, the class
  * that implements this interface should also define all of the actual command
  * processing methods; reflection is used to analyze this class and compile a
  * command dictionary.
  *
  * @see kiwi.util.CommandDispatcher
  *
  * @author Mark Lindner
  */

public interface CommandProcessor
  {

  /** Invocation error handler. This method is called by the command dispatcher
    * when an error occurrs while invoking a command processor method.
    *
    * @param cmd The command that was being processed.
    * @param ex The exception that was thrown.
    */

  public void invocationError(String cmd, Exception ex);

  /** Argument count error handler. This method is called by the command
    * dispatcher when a command is invoked with an incorrect number of
    * arguments.
    *
    * @param cmd The command that was being processed.
    */

  public void argumentCountError(String cmd);

  /** Argument format error handler. This method is called by the command
    * dispatcher when a command is invoked with one or more incorrect argument
    * types.
    *
    * @param cmd The command that was being processed.
    * @param arg The offending argument.
    */

  public void argumentFormatError(String cmd, String arg);

  /** Unknown command error handler. This method is called by the command
    * dispatcher when an unknown command is received.
    *
    * @param cmd The offending command.
    */

  public void unknownCommandError(String cmd);
  }

/* end of source file */
