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
   $Log: StringUtils.java,v $
   Revision 1.5  2003/01/19 09:42:39  markl
   Javadoc & comment header updates.

   Revision 1.4  2001/03/12 03:16:51  markl
   *** empty log message ***

   Revision 1.3  1999/06/08 06:46:33  markl
   Added getClassName() method.

   Revision 1.2  1999/01/10 03:56:22  markl
   added GPL header & RCS tag
   ----------------------------------------------------------------------------
*/

package kiwi.util;

import java.util.*;
import java.io.*;

/** A class of miscellaneous string utilities. All of the methods in this class
  * are static.
  *
  * @author Mark Lindner
  */

public final class StringUtils
  {

  private StringUtils() { }

  /** Left justify a string, wrapping words as necessary.
    *
    * @param text The text to justify.
    * @param cols The number of columns to use.
    */

  public final static String justify(String text, int cols)
    {
    StringTokenizer st = new StringTokenizer(text, "\t \f\n", true);
    StringBuffer buf = new StringBuffer(500);
    int ww, lw = 0;
    boolean sawLF = false, first = true;

    while(st.hasMoreTokens())
      {
      String tok = st.nextToken();

      if(tok.equals("\n"))
        {
        if(cols == 0) continue;

        if(sawLF)
          {
          buf.append('\n');
          buf.append('\n');
          lw = 0;
          first = true;
          sawLF = false;
          }
        else sawLF = true;
        }

      else if((tok.equals(" ")) || (tok.equals("\t")) || (tok.equals("\f")))
        {
        sawLF = false;
        continue;
        }
      else
        {
        sawLF = false;
        ww = tok.length();
        if(!first) ww++;
        if((lw + ww) > cols)
          {
          buf.append('\n');
          first = true;
          lw = 0;
          }

        if(!first) buf.append(' ');
        buf.append(tok);
        lw += ww;
        first = false;
        }
      }

    String r = buf.toString();
    buf = null;
    return(r);
    }

  /** Determine if a string consists solely of alphanumeric characters.
    *
    * @param text The string to test.
    *
    * @return <code>true</code> if the string contains only alphanumeric
    * characters, and <code>false</code> otherwise.
    */

  public final static boolean isAlphaNumeric(String text)
    {
    for(int i = 0; i < text.length(); i++)
      if(!Character.isLetterOrDigit(text.charAt(i)))
        return(false);

    return(true);
    }

  /** Split a string into a series of tokens based on the given delimiter.
    *
    * @param s The string to split.
    * @param delimiter A string consisting of characters that should be treated
    * as delimiters.
    * @return An array of tokens.
    * @see #join
    */

  public final static String[] split(String s, String delimiter)
    {
    Vector v = new Vector();
    StringTokenizer st = new StringTokenizer(s, delimiter);
    while(st.hasMoreTokens())
      v.addElement(st.nextToken());

    String array[] = new String[v.size()];
    v.copyInto(array);

    return(array);
    }

  /** Join an array of strings into a single string of tokens using the given
    * delimiter.
    *
    * @param array The tokens to join.
    * @param delimiter A string to insert between adjacent tokens.
    * @return The resulting string.
    * @see #split
    */
    
  public final static String join(String array[], String delimiter)
    {
    StringBuffer sb = new StringBuffer();
    for(int i = 0; i < array.length; i++)
      {
      if(i > 0) sb.append(delimiter);
      sb.append(array[i]);
      }

    return(sb.toString());
    }

  /** Get the name of a class; this method returns the last component of the
   * fully qualified name of the given class. For example, 'String' is returned
   * for the class <b>java.lang.String</b>.
   *
   * @param clazz The class.
   * @return The name of the class.
   */
  
  public final static String getClassName(Class clazz)
    {
    String s = clazz.getName();

    int idx = s.lastIndexOf('.');

    return(idx < 0 ? s : s.substring(++idx));
    }
  
  }

/* end of source file */
