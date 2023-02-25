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
   $Log: LocaleData.java,v $
   Revision 1.7  2003/02/06 07:43:44  markl
   fixed javadoc typos

   Revision 1.6  2003/01/19 09:42:39  markl
   Javadoc & comment header updates.

   Revision 1.5  2001/03/18 06:39:47  markl
   No longer relies on PropertyResourceBundle; added new constructor to
   construct from Dictionary.

   Revision 1.4  2001/03/12 02:57:41  markl
   Source code cleanup.

   Revision 1.3  1999/06/28 08:19:19  markl
   Added another form of getMessage().

   Revision 1.2  1999/04/19 05:31:53  markl
   New I18N support.

   Revision 1.1  1999/04/18 10:26:12  markl
   Initial revision
   ----------------------------------------------------------------------------
*/

package kiwi.util;

import java.io.*;
import java.text.*;
import java.util.*;

/** Locale-specific message bundle. This class serves as a lookup dictionary
 * for localized messages, and provides some convenience methods for formatting
 * the messages.
 *
 * @author Mark Lindner
 */

public class LocaleData
  {
  /** The default message list delimiter. */
  public static final String DEFAULT_DELIMITER = ",";

  private final Object unitArray[] = new Object[1];
  private Dictionary source;

  /** Construct a new <code>LocaleData</code> object from the given input
   * stream.
   *
   * @param instream The stream to read the data from.
   * @exception java.io.IOException If an error occurred while reading from
   * the stream.
   */
  
  public LocaleData(InputStream instream) throws IOException
    {
    Properties props = new Properties();
    props.load(instream);

    source = props;
    }

  /** Construct a new <code>LocaleData</code> object from the given
   * dictionary.
   *
   * @param source A dictionary that contains the key/value pairs.
   *
   * @since Kiwi 1.3
   */

  public LocaleData(Dictionary source)
    {
    this.source = source;
    }
  
  /** Get a message for the specified key.
   *
   * @param key The key.
   * @exception kiwi.util.ResourceNotFoundException If the specified key was
   * not found.
   * @return A message for the specified key.
   */
  
  public String getMessage(String key) throws ResourceNotFoundException
    {
    Object o = source.get(key);
    if(o == null)
      throw(new ResourceNotFoundException("Resource not found: " + key));
    
    return((String)o);
    }

  /** Get a message for the specified key, and format the message, substituting
   * the specified arguments for the message's placeholders. Messages may have
   * placeholders of the form {n}, where n is a non-negative integer. For
   * example, the message <tt>"My name is {0}, and I am {1} years old."</tt>
   * and argument list <code>{ "Joe", new Integer(12) }</code> would be
   * formatted as <tt>My name is Joe, and I am 12 years old.</tt>
   *
   * @param key The key.
   * @param args An array of arguments for the message.
   * @exception kiwi.util.ResourceNotFoundException If the specified key was
   * not found.
   * @return A formatted message for the specified key.
   */
  
  public String getMessage(String key, Object args[])
    {
    return(MessageFormat.format(getMessage(key), args));
    }

  /** Get a message for the specified key, and format the message, substituting
   * the specified argument for the message's first placeholder. Messages may
   * have* placeholders of the form {n}, where n is a non-negative integer. For
   * example, the message <tt>"My name is {0}"</tt> and argument
   * <code>"Joe"</code> would be formatted as <tt>My name is Joe.</tt>
   *
   * @param key The key.
   * @param args An array of arguments for the message.
   * @exception kiwi.util.ResourceNotFoundException If the specified key was
   * not found.
   * @return A formatted message for the specified key.
   */
  
  public String getMessage(String key, Object arg)
    {
    // Reuse a single array so we don't waste heap space.
    
    synchronized(unitArray)
      {
      unitArray[0] = arg;
      return(getMessage(key, unitArray));
      }
    }
  
  /** Get a message list for the specified key. Retrieves a message for the
   * specified key, and breaks the message on the default delimiter (",")
   * constructing an array in the process.
   *
   * @param key The key.
   * @exception kiwi.util.ResourceNotFoundException If the specified key was
   * not found.
   * @return An array of messages for the specified key.
   */
  
  public String[] getMessageList(String key) throws ResourceNotFoundException
    {
    return(getMessageList(key, DEFAULT_DELIMITER));
    }

  /** Get a message list for the specified key. Retrieves a message for the
   * specified key, and breaks the message on the specified delimiter
   * constructing an array in the process.
   *
   * @param key The key.
   * @param delimiter The delimiter to use.
   * @exception kiwi.util.ResourceNotoundException If the specified key was
   * not found.
   * @return An array of messages for the specified key.
   */
  
  public String[] getMessageList(String key, String delimiter)
    throws ResourceNotFoundException
    {
    String msg = getMessage(key);
    
    return(StringUtils.split(msg, delimiter));
    }

  /** Determine if a message is defined for the specified key.
   *
   * @param key The key.
   * @return <code>true</code> if the key exists, and <code>false</code>
   * otherwise.
   */
   
  public boolean isMessageDefined(String key)
    {
    return(source.get(key) != null);
    }
  
  }

/* end of source file */
