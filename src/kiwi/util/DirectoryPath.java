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
   $Log: DirectoryPath.java,v $
   Revision 1.4  2003/01/19 09:42:38  markl
   Javadoc & comment header updates.

   Revision 1.3  2001/03/12 02:57:39  markl
   Source code cleanup.

   Revision 1.2  1999/01/10 03:47:05  markl
   added GPL header & RCS tag
   ----------------------------------------------------------------------------
*/

package kiwi.util;

import java.util.*;

/** A convenience class for maintaining a directory path (that is, an ordered
  * list of directories).
  *
  * @author Mark Lindner
  */

public class DirectoryPath
  {
  private String psep;
  private Vector _dirs;

  /** Construct a new, empty <code>DirectoryPath</code>. */
      
  public DirectoryPath()
    {
    this(null);
    }

  /** Construct a new <code>DirectoryPath</code> for the given directories.
    *
    * @param dirs An array of directory names.
    */
  
  public DirectoryPath(String dirs[])
    {
    psep = System.getProperty("path.separator");
    _dirs = new Vector();
    
    if(dirs != null)
      for(int i = 0; i < dirs.length; i++)
        _dirs.addElement(dirs[i]);
    }

  /** Prepend a directory to the beginning of the path.
    *
    * @param dir The directory to add.
    */

  public synchronized void prepend(String dir)
    {
    _dirs.insertElementAt(dir, 0);
    }

  /** Prepend a list directories to the beginning of the path. The order of
    * the directories is preserved.
    *
    * @param dirs The directories to add.
    */
  
  public synchronized void prepend(String dirs[])
    {
    for(int i = 0; i < dirs.length; i++)
      _dirs.insertElementAt(dirs[i], i);
    }
  
  /** Append a directory to the end of the path.
    *
    * @param dir The directory to add.
    */
  
  public synchronized void append(String dir)
    {
    _dirs.addElement(dir);
    }

  /** Append a list directories to the end of the path. The order of the
    * directories is preserved.
    *
    * @param dirs The directories to add.
    */

  public synchronized void append(String dirs[])
    {
    for(int i = 0; i < dirs.length; i++)
      _dirs.addElement(dirs[i]);
    }
  
  /** Get the list of directories for this path.
    *
    * @return An array of directory names.
    */
  
  public synchronized String[] getDirectories()
    {
    String s[] = new String[_dirs.size()];

    _dirs.copyInto(s);
    
    return(s);
    }

  /** Convert this path to a string, using the appropriate path separator for
    * this platform.
    */
  
  public String toString()
    {
    StringBuffer sb = new StringBuffer();

    Enumeration e = _dirs.elements();
    boolean first = true;
    while(e.hasMoreElements())
      {
      String s = (String)e.nextElement();
      if(!first) sb.append(psep);
      first = false;
      sb.append(s);
      }

    return(sb.toString());
    }
  
  }

/* end of source file */
