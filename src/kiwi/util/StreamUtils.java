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
   $Log: StreamUtils.java,v $
   Revision 1.2  2003/01/19 09:42:39  markl
   Javadoc & comment header updates.

   Revision 1.1  2001/08/28 21:37:33  markl
   Split out stream methods into their own class.
   ----------------------------------------------------------------------------
*/

package kiwi.util;

import java.io.*;

/** This class consists of several convenience routines for reading and
 * writing streams. The methods are all static.
 *
 * @since Kiwi 1.3.1
 *
 * @author Mark Lindner
 */

public final class StreamUtils
  {
  /** The data transfer block size. */
  public static final int blockSize = 4096;

  /** Read all of the data from a stream, writing it to another stream. Reads
    * data from the input stream and writes it to the output stream, until no
    * more data is available.
    *
    * @param input The input stream.
    * @param output The output stream.
    *
    * @exception java.io.IOException If an error occurred while reading from
    * the stream.
    */

  public static final OutputStream readStreamToStream(InputStream input,
                                                      OutputStream output)
    throws IOException
    {
    byte buf[] = new byte[blockSize];
    int b;
      
    while((b = input.read(buf)) > 0)
      output.write(buf, 0, b);
      
    return(output);
    }

  /** Read all of the data from a stream, returning the contents as a
    * <code>String</code>. Note that this method is not unicode-aware.
    *
    * @param input The stream to read from.
    *
    * @return The contents of the stream, as a <code>String</code>.
    *
    * @exception java.io.IOException If an error occurred while reading from
    * the stream.
    */

  public static final String readStreamToString(InputStream input)
    throws IOException
    {
    return(readStream(input).toString());
    }

  /** Write a string to a stream. Note that this method is not unicode-aware.
    *
    * @exception java.io.IOException If an error occurred while writing to the
    * stream.
    *
    * @param s The string to write.
    * @param output The stream to write it to.
    */

  public static final void writeStringToStream(String s, OutputStream output)
    throws IOException
    {
    InputStream input = new ByteArrayInputStream(s.getBytes());

    readStreamToStream(input, output);
    }
  
  /** Read all of the data from a stream, returning the contents as a
    * <code>byte</code> array.
    *
    * @param input The stream to read from.
    *
    * @return The contents of the stream, as a <code>byte</code> array.
    *
    * @exception java.io.IOException If an error occurred while reading from
    * the stream.
    */

  public static final byte[] readStreamToByteArray(InputStream input)
    throws IOException
    {
    return(readStream(input).toByteArray());
    }

  /* Fully read an <code>InputStream</code>, writing the data read to a
   * <code>ByteArrayOutputStream. Returns a reference to the resulting stream.
   */

  private static final ByteArrayOutputStream readStream(InputStream input)
    throws IOException
    {
    ByteArrayOutputStream output = new ByteArrayOutputStream(blockSize);
    
    readStreamToStream(input, output);
    
    return(output);
    }
  
  }

/* end of source file */
