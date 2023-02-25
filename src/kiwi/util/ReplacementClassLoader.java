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
   $Log: ReplacementClassLoader.java,v $
   Revision 1.5  2003/01/19 09:42:39  markl
   Javadoc & comment header updates.

   Revision 1.4  2001/08/28 21:37:54  markl
   Fixed call to stream method.

   Revision 1.3  2001/03/12 03:16:49  markl
   *** empty log message ***

   Revision 1.2  1999/01/10 03:55:05  markl
   added GPL header & RCS tag
   ----------------------------------------------------------------------------
*/

package kiwi.util;

import java.util.*;
import java.io.*;

/** A custom class loader that can be used to selectively replace core classes.
  *
  * @author Mark Lindner
  */

public class ReplacementClassLoader extends ClassLoader
  {
  /** The path (relative to the anchor) relative to which classes will be
    * loaded.
    */
  public static final String codebase = "patches";
  private Hashtable replacements, classes;
  private Class clazz;

  /** Construct a new <code>ReplacementClassLoader</code>. The class loader
    * will become active once a class is loaded explicitly via its
    * <code>loadClass()</code> method. All classes loaded by that class will,
    * in turn, be loaded by this class loader.
    *
    * @param replacementList A list of full-qualified names of classes that
    * will be loaded from a local source.
    *
    * @param clazz The class relative to which the <i>patches</i> resource
    * directory is located. This resource directory contains the class
    * hierarchy of replacement classes.
    */

  public ReplacementClassLoader(String replacementList[], Class clazz)
    {
    replacements = new Hashtable();
    classes = new Hashtable();
    this.clazz = clazz;

    for(int i = 0; i < replacementList.length; i++)
      {
      replacements.put(replacementList[i], Void.class);
      }
    }

  /** Load a class.
    *
    * @param name The fully-qualified name of the class to load.
    * @param resolve A flag specifying whether the class should be resolved.
    *
    * @exception java.lang.ClassNotFoundException If the named class could not
    * be found.
    */

  protected Class loadClass(String name, boolean resolve)
    throws ClassNotFoundException
    {
    Class c;
    byte bytecodes[];

    System.out.println("Loading: " + name);

    // already loaded?

    if((c = (Class)classes.get(name)) == null)
      {
      InputStream in = null;

      System.out.println("Checking if " + name + " is patched");
      if(replacements.get(name) != null)
        {
        System.out.println("Patching " + name + "...");
        in = clazz.getResourceAsStream(translateName(name));

        System.out.println("Translated: " + translateName(name));

        if(in == null)
          {
          System.out.println("Can't find it there...trying system!");
          c = findSystemClass(name);
          }
        else
          {
          try
            {
            bytecodes = StreamUtils.readStreamToByteArray(in);
            }
          catch(IOException ex)
            {
            throw(new ClassFormatError(name));
            }
          finally
            {
            try
              {
              in.close();
              }
            catch(IOException ex) {}
            }

          c = defineClass(name, bytecodes, 0, bytecodes.length);
          if(c == null)
            throw new ClassNotFoundException(name);
          }
        }
      else
        c = findSystemClass(name);

      classes.put(name, c);
      }
    if(resolve) resolveClass(c);

    return(c);
    }

  /* translate class name to resource path */

  private String translateName(String name)
    {
    StringBuffer sb = new StringBuffer(100);
    StringTokenizer st = new StringTokenizer(name, ".");
    boolean first = true;

    sb.append(codebase);
    while(st.hasMoreTokens())
      {
      sb.append('/');
      sb.append(st.nextToken());
      }
    sb.append(".txt");
    return(sb.toString());
    }
  
  }

/* end of source file */
