/*
 *  $Source: /cvsroot2/open/projects/jOggPlayer/ca/bc/webarts/widgets/VorbisInfo.java,v $
 *  $Name:  $
 *  $Revision: 491 $
 *  $Date: 2008-10-13 18:01:39 -0700 (Mon, 13 Oct 2008) $
 *  $Locker:  $
 */
/**
 *  A utility for reading information and comments from the header packets of an
 *  Ogg Vorbis stream. <tt> <p>
 *
 *  Copyright (C) 2001 Matthew Elder </p> <p>
 *
 *  This library is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU Library General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or (at your
 *  option) any later version. </p> <p>
 *
 *  This library is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *  FITNESS FOR A PARTICULAR PURPOSE. See the GNU Library General Public License
 *  for more details. </p> <p>
 *
 *  You should have received a copy of the GNU Library General Public License
 *  along with this library; if not, write to the Free Software Foundation,
 *  Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA. </p> </tt>
 *
 * @author     Matthew M. Elder
 * @version    0.1
 */

package ca.bc.webarts.widgets;

import java.io.*;
import java.util.*;


/**
 *  This is a class to encapsulate the Vorbis comments associated in a Ogg Vorbis file.
 *
 * @author    Matthew Elder
 */
public class VorbisInfo
{

  // stuff from comment header
  /**  Description of the Field */
  String vendor;
  /**  The number of comments. */
  int comments = 0;
  /**  The comments name value pairs in a Hashtable. */
  Hashtable comment;

  // stuff from info header
  /**  Info Header channels. */
  private int channels;
  /**  Info Header  bit rate. */
  private long rate = 0;
  /**  Description of the Field */
  private long bitrate_upper;
  /**  Description of the Field */
  private long bitrate_nominal;
  /**  Description of the Field */
  private long bitrate_lower;

  // for doing checksum
  /**  for doing checksum. */
  private long[] crc_lookup = new long[256];
  /**  Description of the Field */
  private int crc_ready = 0;

  // for reading data
  /**  Description of the Field */
  private byte[] header;
  /**  Description of the Field */
  private byte[] packet;


  /**
   *  Initializes Ogg Vorbis information based on the header from a stream.
   *
   * @param  s                an Ogg Vorbis data stream
   * @exception  IOException  Description of the Exception
   */
  public VorbisInfo( InputStream s )
    throws IOException
  {

    /*
     *  initialize the crc_lookup table
     */
    for ( int i = 0; i < 256; i++ )
    {
      crc_lookup[i] = _ogg_crc_entry( i );
    }

    fetch_header_and_packet( s );
    interpret_header_packet();

    fetch_header_and_packet( s );
    interpret_header_packet();
  }

  //tester main
  /**
   *  The main program for the VorbisInfo class
   *
   * @param  args           The command line arguments
   * @exception  Exception  Description of the Exception
   */
  public static void main( String[] args )
    throws Exception
  {
    if ( args.length != 1 )
    {
      System.err.println( "usage:\tjava VorbisInfo <ogg vorbis file>" );
      System.exit( 1 );
    }
    BufferedInputStream b =
        new BufferedInputStream( new FileInputStream( args[0] ) );
    VorbisInfo vi = new VorbisInfo( b );
    System.out.println( "\n" + vi );

    b.close();
  }


  /**
   *  Returns the number of channels in the bitstream.
   *
   * @return    the number of channels in the bitstream.
   */
  public int getChannels()
  {
    return channels;
  }


  /**
   *  Returns the rate of the stream in Hz.
   *
   * @return    the rate of the stream in Hz.
   */
  public long getRate()
  {
    return rate;
  }


  /**
   *  Returns the <em>average</em> bitrate of the stream in kbps.
   *
   * @return    the <em>average</em> bitrate of the stream in kbps.
   */
  public long getBitrate()
  {
    return bitrate_nominal;
  }


  /**
   *  Returns a <code>Vector</code> containing values from the comment header.
   *  Vorbis comments take the form: <blockquote><tt>FIELD=SOME STRING VALUE.
   *  </tt></blockquote> Since there is no requirement for FIELD to be unique
   *  there may be multiple values for one field. <code>getComments</code>
   *  returns a <code>Vector</code> of <code>String</code> for all of the
   *  strings associated with a field. <br>
   *  Note: <code>field</code> is case insensitive.<br>
   *
   *
   * @param  field  a case insensitive string for a field name in an Ogg Vorbis
   *      comment header
   * @return        a Vector of strings containing field values from an Ogg
   *      Vorbis comment header
   */
  public Vector getComments( String field )
  {
    Vector retVal = null;
    if (field != null && comment !=null)
    {
      retVal = (Vector)comment.get( field.toLowerCase() );
    }
    return retVal;
  }


  /**
   *  Returns a <code>Set</code> of comment field names in no particular order.
   *
   * @return    a <code>Set</code> of comment field names in no particular
   *      order.
   */
  public Set getFields()
  {
    return comment.keySet();
  }


  /**
   *  Prints out the information from an Ogg Vorbis stream in a nice,
   *  humanly-readable format.
   *
   * @return    A string representation of the object.
   */
  public String toString()
  {

    String str = "";

    str += channels + " channels at " + rate + "Hz\n";
    str += bitrate_nominal / 1000 + "kbps (average bitrate)\n";

    Iterator fields = comment.keySet().iterator();
    while ( fields.hasNext() )
    {
      String name = (String)fields.next();
      Vector values = (Vector)comment.get( name );
      Iterator vi = values.iterator();
      str += name + "=";
      boolean dumb = false;
      while ( vi.hasNext() )
      {
        if ( dumb )
        {
          str += ", ";
        }
        str += vi.next();
        dumb = true;
      }
      str += "\n";
    }

    return str;
  }


  /**
   *  Description of the Method
   *
   * @param  s                Description of the Parameter
   * @exception  IOException  Description of the Exception
   */
  private void fetch_header_and_packet( InputStream s )
    throws IOException
  {

    // read in the minimal packet header
    byte[] head = new byte[27];
    int bytes = s.read( head );
    //System.err.println("\nDEBUG: bytes = "+bytes);
    if ( bytes < 27 )
    {
      throw new IOException( "Not enough bytes in header" );
    }

    if ( !"OggS".equals( new String( head, 0, 4 ) ) )
    {
      throw new IOException( "Not a valid Ogg Vorbis file" );
    }

    int headerbytes = ( touint( head[26] ) ) + 27;
    //System.err.println("DEBUG: headerbytes = "+headerbytes);

    // get the rest of the header
    byte[] head_rest = new byte[touint( head[26] )];// :-) that's a pun
    bytes += s.read( head_rest );
    //System.err.println("DEBUG: bytes = "+bytes);
    header = new byte[headerbytes];
    Arrays.fill( header, (byte)0 );

    // copy the whole header into header
    System.arraycopy( head, 0, header, 0, 27 );
    System.arraycopy( head_rest, 0, header, 27, headerbytes - 27 );

    if ( bytes < headerbytes )
    {
      String error =
          "Error reading vorbis file: " +
          "Not enough bytes for header + seg table";
      throw new IOException( error );
    }

    int bodybytes = 0;
    for ( int i = 0; i < header[26]; i++ )
    {
      bodybytes += touint( header[27 + i] );
    }
    //System.err.println("DEBUG: bodybytes = "+bodybytes);

    packet = new byte[bodybytes];
    Arrays.fill( packet, (byte)0 );
    bytes += s.read( packet );
    //System.err.println("DEBUG: bytes = "+bytes);

    if ( bytes < headerbytes + bodybytes )
    {
      String error =
          "Error reading vorbis file: " +
          "Not enough bytes for header + body";
      throw new IOException( error );
    }

    byte[] oldsum = new byte[4];
    System.arraycopy( header, 22, oldsum, 0, 4 );// read existing checksum
    Arrays.fill( header, 22, 22 + 4, (byte)0 );// clear for calculation of checksum

    byte[] newsum = checksum();
    if ( !( new String( oldsum ) ).equals( new String( newsum ) ) )
    {
      System.err.println( "checksum failed" );
      System.err.println( "old checksum: " +
          oldsum[0] + "|" + oldsum[1] + "|" + oldsum[2] + "|" + oldsum[3] );
      System.err.println( "new checksum: " +
          newsum[0] + "|" + newsum[1] + "|" + newsum[2] + "|" + newsum[3] );
    }

  }


  /**
   *  Description of the Method
   *
   * @exception  IOException  Description of the Exception
   */
  private void interpret_header_packet()
    throws IOException
  {
    byte packet_type = packet[0];
    switch ( packet_type )
    {
        case 1:
          //System.err.println("DEBUG: got header packet");
          if ( rate != 0 )
          {
            throw new IOException( "Invalid vorbis file: info already fetched" );
          }
          fetch_info_info();
          break;
        case 3:
          //System.err.println("DEBUG: got comment packet");
          if ( rate == 0 )
          {
            throw new IOException( "Invalid vorbis file: header not complete" );
          }
          fetch_comment_info();
          break;
        case 5:
          throw new IOException( "Invalid vorbis file: header not complete" );
        default:
          throw new IOException( "Invalid vorbis file: bad packet header" );
    }
  }


  /**
   *  pull the fields from the info header
   *
   * @exception  IOException  Description of the Exception
   */
  private void fetch_info_info()
    throws IOException
  {

    // keep track of location in packet
    int dataptr = 1;// should have already read packet[0] for packet type

    String str = new String( packet, dataptr, 6 );
    dataptr += 6;
    if ( !"vorbis".equals( str ) )
    {
      throw new IOException( "Not a vorbis header" );
    }
    dataptr += 4;// skip version (4 bytes)

    channels = packet[dataptr++];// 1 byte

    rate = toulong( read32( packet, dataptr ) );
    dataptr += 4;// just read 4 bytes

    bitrate_upper = toulong( read32( packet, dataptr ) );
    dataptr += 4;// just read 4 bytes

    bitrate_nominal = toulong( read32( packet, dataptr ) );
    dataptr += 4;// just read 4 bytes

    bitrate_lower = toulong( read32( packet, dataptr ) );
    dataptr += 4;// just read 4 bytes

    dataptr++;// skip block sizes (4 bits each for a total of 1 byte)

    byte eop = packet[dataptr++];
    if ( eop != 1 )
    {
      throw new IOException( "End of packet expected but not found" );
    }
  }


  /**
   *  Description of the Method
   *
   * @exception  IOException  Description of the Exception
   */
  private void fetch_comment_info()
    throws IOException
  {
    int dataptr = 1;

    String str = new String( packet, dataptr, 6 );
    dataptr += 6;
    if ( !"vorbis".equals( str ) )
    {
      throw new IOException( "Not a vorbis header" );
    }

    comment = new Hashtable();

    long len = toulong( read32( packet, dataptr ) );
    int lenInt = Long.valueOf(len).intValue();
    //System.err.println("DEBUG: vendor string length = "+len);
    dataptr += 4;

    /*
     *  FIXME: Casting len to int here means possible loss of data.
     *  I don't know who would have a comment header big
     *  enough to cause this, but the spec says this is
     *  an unsigned 32 bit int.
     *  Damn java for not having unsigned ints (or it should
     *  at least allow the use of 64 bit longs more frequently)
     *  If I get a chance i'll write a wrapper for this(maybe).
     */
    vendor = new String( packet, dataptr, lenInt );
    dataptr += lenInt;

    // FIXME: similar problem to the vendor string length above
    comments = Long.valueOf(toulong( read32( packet, dataptr ) )).intValue();
    //System.out.println( "DEBUG: number of comments: "+comments);
    dataptr += 4;
    int validComment = -1;

    for ( int i = 0; i < comments; i++ )
    {

      // read comment
      len = toulong( read32( packet, dataptr ) );
      //System.out.println( "DEBUG: length of current comment: "+len);
      if (len>0)
      {
        lenInt = Long.valueOf(len).intValue();
        dataptr += 4;
        // FIXME: same problem as vendor string
        String cmnt = new String( packet, dataptr, lenInt );
        dataptr += lenInt;

        // parse and store

        validComment = cmnt.indexOf( '=' );
        if (validComment >0)
        {
          String name = cmnt.substring( 0, validComment );
          String value = cmnt.substring( validComment + 1 );
          if ( comment.containsKey( name ) )
          {
            Vector tmp = (Vector)comment.get( name.toLowerCase() );
            tmp.add( value );
          }
          else
          {
            Vector tmp = new Vector();
            tmp.add( value );
            comment.put( name.toLowerCase(), tmp );
          }
        }
      }
    }
  }


  /**
   *  Description of the Method
   *
   * @param  data  Description of the Parameter
   * @param  ptr   Description of the Parameter
   * @return       Description of the Return Value
   */
  private int read32( byte[] data, int ptr )
  {
    int val = 0;
    val = ( touint( data[ptr] ) & 0x000000ff );
    val |= ( ( touint( data[ptr + 1] ) << 8 ) & 0x0000ff00 );
    val |= ( ( touint( data[ptr + 2] ) << 16 ) & 0x00ff0000 );
    val |= ( ( touint( data[ptr + 3] ) << 24 ) & 0xff000000 );
    return val;
  }


  /**
   *  Description of the Method
   *
   * @return    Description of the Return Value
   */
  private byte[] checksum()
  {
    long crc_reg = 0;

    for ( int i = 0; i < header.length; i++ )
    {
      int tmp = (int)( ( ( crc_reg >>> 24 ) & 0xff ) ^ touint( header[i] ) );
      crc_reg = ( crc_reg << 8 ) ^ crc_lookup[tmp];
      crc_reg &= 0xffffffff;
    }
    for ( int i = 0; i < packet.length; i++ )
    {
      int tmp = (int)( ( ( crc_reg >>> 24 ) & 0xff ) ^ touint( packet[i] ) );
      crc_reg = ( crc_reg << 8 ) ^ crc_lookup[tmp];
      crc_reg &= 0xffffffff;
    }

    byte[] sum = new byte[4];
    sum[0] = (byte)( crc_reg & 0xffL );
    sum[1] = (byte)( ( crc_reg >>> 8 ) & 0xffL );
    sum[2] = (byte)( ( crc_reg >>> 16 ) & 0xffL );
    sum[3] = (byte)( ( crc_reg >>> 24 ) & 0xffL );

    return sum;
  }


  /**
   *  Description of the Method
   *
   * @param  index  Description of the Parameter
   * @return        Description of the Return Value
   */
  private long _ogg_crc_entry( long index )
  {
    long r;

    r = index << 24;
    for ( int i = 0; i < 8; i++ )
    {
      if ( ( r & 0x80000000L ) != 0 )
      {
        r = ( r << 1 ) ^ 0x04c11db7L;
      }
      else
      {
        r <<= 1;
      }
    }
    return ( r & 0xffffffff );
  }


  /**
   *  Description of the Method
   *
   * @param  n  Description of the Parameter
   * @return    Description of the Return Value
   */
  private long toulong( int n )
  {
    return ( n & 0xffffffffL );
  }


  /**
   *  Description of the Method
   *
   * @param  n  Description of the Parameter
   * @return    Description of the Return Value
   */
  private int touint( byte n )
  {
    return ( n & 0xff );
  }

}

