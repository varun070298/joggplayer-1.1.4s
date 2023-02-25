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
   $Log: CommandDispatcher.java,v $
   Revision 1.4  2003/01/19 09:42:38  markl
   Javadoc & comment header updates.

   Revision 1.3  2001/03/12 02:57:38  markl
   Source code cleanup.

   Revision 1.2  1999/01/10 03:41:46  markl
   added GPL header & RCS tag
   ----------------------------------------------------------------------------
*/

package kiwi.util;

import java.util.*;
import java.lang.reflect.*;

/** A class that makes use of the reflection API to implement a generic,
  * reusable, and flexible command processor.
  * <p>
  * The <code>CommandDispatcher</code> analyzes a helper class (which
  * implements the <code>CommandProcessor</code> interface), searching for
  * methods whose names begin with the prefix <code>cmd_</code>. This
  * information is used to build a command dictionary that maps command names
  * to their associated processing methods.
  * <p>
  * The general idea is that the <code>CommandDispatcher</code> will be fed
  * command strings that consist of a command name followed by a whitespace-
  * separated list of arguments. This string is tokenized into words. The first
  * word is the command name and is used to look up an associated method in the
  * command dictionary. For example, if the command is <i>hello</i>, the
  * associated method in the helper class would be <code>cmd_hello()</code>. If
  * a matching method is not found, the helper object is messaged with an
  * <code>unknownCommandError()</code>.
  * <p>
  * If the command is found, then the remaining tokens are counted to determine
  * the number of arguments that the command is being invoked with. Some
  * commands may have more than one form, or may have optional arguments. In
  * this case, the associated command processing method in the helper class may
  * be overloaded to handle all of the separate (legal) forms. The argument
  * count is used to select an appropriate command handler. Note that no two
  * command processing methods of the same name may have the same number of
  * arguments, as this would produce a parsing ambiguity. Thus it is not valid
  * to have <code>void cmd_hello(String s, int i)</code> and
  * <code>void cmd_hello(int a, int b)</code>. If an appriate method cannot be
  * found, the helper object is messaged with an
  * <code>argumentCountError()</code>.
  * <p>
  * The arguments to the command (which are string tokens) are cast to the
  * appropriate data types for the method. Thus if the command processor method
  * for <i>hello</i> is declared as <code>void cmd_hello(String s, int i)
  * </code>, then the first argument is left as a string and the second
  * argument is converted to an <code>Integer</code> object. The following
  * object types are supported for arguments: <code>String</code>,
  * <code>int</code>, <code>Integer</code>, <code>boolean</code>,
  * <code>Boolean</code>, <code>short</code>, <code>Short</code>,
  * <code>long</code>, <code>Long</code>, <code>float</code>,
  * <code>Float</code>, <code>double</code>, <code>Double</code>. If an error
  * occurrs during argument conversion, the helper object is messaged with an
  * <code>arugmentFormatError()</code>.
  * <p>
  * Once argument conversion is complete, the command processor method in the
  * helper object is invoked with the argument list. If an error occurrs during
  * the invocation, the helper object is messaged with an
  * <code>invocationError()</code>.
  *
  * @author Mark Lindner
  */

public class CommandDispatcher
  {
  private CommandProcessor processor;
  private Hashtable cmds;
  private static final Class argtypes[]
    = { Integer.class, Float.class, Double.class, Short.class, Long.class,
	java.lang.String.class, Boolean.class };
  private static final Class argtypes2[]
    = { int.class, float.class, double.class, short.class, Long.class,
	java.lang.String.class, boolean.class };
  private static final Class stringConstructor[] = { java.lang.String.class };

  /** Construct a new <code>CommandDispatcher</code> for the given helper
    * class.
    *
    * @param processor The helper class that serves as the command processor
    * and error handler.
    */

  public CommandDispatcher(CommandProcessor processor)
    {
    this.processor = processor;
    cmds = new Hashtable();

    // analyze the processor class and build a command dictionary

    Method methods[] = processor.getClass().getMethods();

      NEXTCMD:
    for(int i = 0; i < methods.length; i++)
      {
      String nm = methods[i].getName();

      // ignore methods whose names don't begin with "cmd_"

      if(!(nm.startsWith("cmd_"))) continue;

      // check all the method's parameter types to ensure that they are of
      // supported types; ignore methods that don't pass this test

      Class args[] = methods[i].getParameterTypes();
      for(int j = 0; j < args.length; j++)
        if(!isSupported(args[j])) continue NEXTCMD;

      // strip off the "cmd_" prefix and try to look up the vector for this
      // method name in the hash table

      String c = nm.substring(4);
      Vector v = (Vector)cmds.get(c);

      // if this is the first method of this name that we've encountered,
      // create the vector in the hash table

      if(v == null)
        {
        v = new Vector();
        cmds.put(c, v);
        }

      // add the method object to the vector

      v.addElement(methods[i]);
      }
    }

  /** Dispatch a command string to the appropriate processing method.
    *
    * @param s The command string to be parsed and passed to its associated
    * command processing method.
    */

  public void dispatch(String s)
    {
    StringTokenizer st = new StringTokenizer(s);

    // if there are no tokens on the line, ignore the line

    if(!st.hasMoreTokens()) return;

    // pull the first token off the string; this is the command line
    // try to find the vector for this command in the hashtable

    String cmd = st.nextToken();
    Vector v = (Vector)cmds.get(cmd);
    if(v == null)
      {
      // if we can't find one, message an error & abort

      processor.unknownCommandError(cmd);
      return;
      }

    // enumerate through the vector, examining each overloaded method in turn

    Enumeration e = v.elements();
    Method m = null;
    Class params[] = null;
    boolean found = false;
    Constructor cons = null;

    while(e.hasMoreElements())
      {
      m = (Method)e.nextElement();
      params = m.getParameterTypes();

      // if the parameter count matches the number of tokens remaining
      // (arguments) then we've found our method

      if(params.length == st.countTokens())
        {
        found = true;
        break;
        }
      }

    if(!found)
      {
      // if we didn't find a method, message an error & abort

      processor.argumentCountError(cmd);
      return;
      }

    // construct the argument list to pass to the method

    Object args[] = new Object[params.length];

    for(int i = 0; i < params.length; i++)
      {
      // get a wrapper class for the nth parameter, and try to locate a
      // constructor for the wrapper class that takes only a single string as
      // an argument

      Class clazz = wrapperClass(params[i]);
      String arg = st.nextToken();

      try
        {
        cons = clazz.getConstructor(stringConstructor);
        }
      catch(NoSuchMethodException ex)
        {
        // this shouldn't happen, but if it does, we should abort

        processor.argumentFormatError(cmd, arg);
        return;
        }

      // instantiate the class with the next token as the argument

      try
        {
        args[i] = cons.newInstance(new Object[] { arg });
        }
      catch(Exception ex)
        {
        // if we get an instantiation error, then the format of the argument is
        // incorrect

        processor.argumentFormatError(cmd, arg);
        return;
        }
      }

    // now invoke the method with the argument list, messaging an error and
    // aborting if we get an exception

    try
      {
      m.invoke(processor, args);
      }
    catch(Exception ex)
      {
      processor.invocationError(cmd, ex);
      return;
      }
    }
  
  /* Look up the wrapper class for a primitive type class; if we can't find a
   * wrapper, just return the class that was passed in.
   */

  private Class wrapperClass(Class clazz)
    {
    for(int i = 0; i < argtypes.length; i++)
      if(clazz.equals(argtypes2[i]))
        return(argtypes[i]);

    return(clazz);
    }
  
  /* Determine if the specified class is a supported argument type.
   */

  private boolean isSupported(Class type)
    {
    for(int i = 0; i < argtypes.length; i++)
      if(type.equals(argtypes[i]) || type.equals(argtypes2[i]))
        return(true);

    return(false);
    }
  
  }

/* end of source file */
