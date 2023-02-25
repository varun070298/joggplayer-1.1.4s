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
   $Log: Assert.java,v $
   Revision 1.6  2003/01/19 09:42:38  markl
   Javadoc & comment header updates.

   Revision 1.5  2002/08/11 09:42:10  markl
   Renamed assert() method to _assert(), since in JDK 1.4 assert is a
   keyword.

   Revision 1.4  2001/03/12 05:43:34  markl
   Javadoc cleanup.

   Revision 1.3  2001/03/12 02:57:37  markl
   Source code cleanup.

   Revision 1.2  1999/01/10 03:41:46  markl
   added GPL header & RCS tag
   ----------------------------------------------------------------------------
*/

package kiwi.util;

/** Assertion support. An assertion is basically a test of a boolean
  * expression. If the expression evaluates to <code>false</code>, the
  * assertion fails, and a runtime exception is thrown. An example of an
  * assertion is:
  * <p>
  * <pre>
  * Assert._assert(window != null);
  * window.setVisible(true);
  * </pre>
  *
  * @author Mark Lindner
  */

public class Assert
  {
  /*
   */
  
  private Assert() { }

  /** Test an assertion.
    *
    * @param expr The value of the expression to test.
    *
    * @exception kiwi.util.AssertionException If the assertion fails.
    */

  public static void _assert(boolean expr) throws AssertionException
    {
    if(!expr)
      throw(new AssertionException("Assertion failed."));
    }
  
  }

/* end of source file */
