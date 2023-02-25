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
   $Log: ResourceLoader.java,v $
   Revision 1.6  2003/01/19 09:42:39  markl
   Javadoc & comment header updates.

   Revision 1.5  2001/10/31 23:14:04  markl
   Documentation fix.

   Revision 1.4  2001/03/12 03:16:50  markl
   *** empty log message ***

   Revision 1.3  1999/05/03 09:35:12  markl
   Fixed typo in javadoc.

   Revision 1.2  1999/01/10 03:55:05  markl
   added GPL header & RCS tag
   ----------------------------------------------------------------------------
*/

package kiwi.util;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.*;
import java.util.Properties;
import java.net.URL;

import kiwi.ui.AudioClip;

/** A utility class containing methods for retrieving application resources;
  * these resources typically reside within a JAR file among the classes that
  * make up an application. The location of a resource is specified as a path
  * relative to the location of a class within the application's class
  * hierarchy. This "anchor class" is specified in the constructor.
  * <p>
  * Resources may be retrieved as byte arrays, as <code>String</code>s, as
  * <code>InputStream</code>s, as <code>AudioClip</code>s, as
  * <code>Image</code>s, or as <code>Properties</code> objects.
  * <p>
  * See <code>ResourceManager</code> for a higher-level interface.
  *
  * @see kiwi.util.ResourceManager
  * 
  * @author Mark Lindner
  */

public class ResourceLoader
  {
  private ResourceDecoder decoder;

  /** The class object associated with this resource loader. */
  protected Class clazz = null;

  /** Construct a new <code>ResourceLoader</code>. A new resource loader is
    * created with a default input buffer size.
    */

  public ResourceLoader(Class clazz)
    {
    this.clazz = clazz;

    decoder = new ResourceDecoder();
    }

  /** Retrieve a resource as a URL.
    *
    * @param path The location of the resource.
    *
    * @return A <code>URL</code> reference to the resource.
    */

  public URL getResourceAsURL(String path)
    {
    return(clazz.getResource(path));
    }

  /** Retrieve a resource as a stream.
    *
    * @param path The location of the resource.
    *
    * @return An <code>InputStream</code> from which the resource data may be
    * read.
    *
    * @exception java.io.IOException If the resource was not found.
    */

  public InputStream getResourceAsStream(String path) throws IOException
    {
    InputStream is = clazz.getResourceAsStream(path);

    if(is == null)
      throw(new IOException("Resource not found"));

    return(is);
    }

  /** Retrieve a resource as a <code>String</code>. Retrieves the specified
    * resource, returning its data as a <code>String</code>. It is assumed that
    * the resource contains printable text.
    *
    * @param path The location of the resource.
    *
    * @exception java.io.IOException If an error occurred while reading the
    * resource's data.
    */

  public final String getResourceAsString(String path) throws IOException
    {
    InputStream is = getResourceAsStream(path);
    String s = decoder.decodeString(is);
    is.close();

    return(s);
    }

  /** Retrieve a resource as an <code>AudioClip</code>. Retrieves the specified
    * resource, returning its data as an <code>AudioClip</code>. It is assumed
    * that the resource contains valid audio data.
    *
    * @param path The location of the resource.
    */

  public final AudioClip getResourceAsAudioClip(String path)
    {
    AudioClip clip = null;

    try
      {
      InputStream is = getResourceAsStream(path);
      clip = decoder.decodeAudioClip(is);
      }
    catch(IOException ex) {}

    return(clip);
    }

  /** Retrieve a resource as an <code>Image</code>. Retrieves the specified
    * resource, returning its data as an <code>Image</code>. It is assumed that
    * the resource contains valid image data.
    *
    * @param path The location of the resource.
    */

  public synchronized final Image getResourceAsImage(String path)
    {
    Toolkit tk = Toolkit.getDefaultToolkit();
    Image im = null;

    try
      {
      InputStream is = getResourceAsStream(path);
      im = decoder.decodeImage(is);
      }
    catch(IOException ex) {  }

    return(im);
    }

  /** Retrieve a resource as a <code>Properties</code> object. Retrieves the
    * specified resource, returning its data as a <code>Properties</code>
    * object. It is assumed that the resource is a properly-formatted property
    * list.
    *
    * @param path The location of the resource.
    *
    * @exception java.io.IOException If an error occurred while reading the
    * resource's data.
    */

  public final Properties getResourceAsProperties(String path)
    throws IOException
    {
    InputStream is = getResourceAsStream(path);
    Properties prop = decoder.decodeProperties(is);
    is.close();

    return(prop);
    }

  }

/* end of source file */
