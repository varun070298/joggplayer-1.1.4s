/*
 *  JOrbisComment -- pure Java Ogg Vorbis Comment Editor
 *
 *  Copyright (C) 2000 ymnk, JCraft,Inc.
 *
 *  Written by: 2000 ymnk<ymnk@jcraft.com>
 *
 *  Many thanks to
 *  Monty <monty@xiph.org> and
 *  The XIPHOPHORUS Company http://www.xiph.org/ .
 *  JOrbis has been based on their awesome works, Vorbis codec and
 *  JOrbisPlayer depends on JOrbis.
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package ca.bc.webarts.tools;

import ca.bc.webarts.widgets.Util;
import com.jcraft.jogg.*;
import com.jcraft.jorbis.*;
import java.io.*;
import java.util.*;


/**
 *  A class that reads and writes comments in Ogg Vorbis Files. It uses jcrafts
 *  jorbis impl.
 *
 * @author
 */
public class JOrbisComment
{

  /**  Description of the Field */
  private static int CHUNKSIZE = 4096;
  /**  Description of the Field */
  State state = null;
  /**  Description of the Field */
  File inFile = null;
  InputStream vorbisStream_ = null;


  /**  Constructor for the JOrbisComment object */
  public JOrbisComment()
  {
    super();
    State foo = new State();
    this.state = state;
  }


  /**
   *  Constructor for the JOrbisComment object
   *
   * @param  state  Description of the Parameter
   */
  public JOrbisComment(State state)
  {
    super();
    this.state = state;
  }


  /**
   *  Constructor for the JOrbisComment object
   *
   * @param  inFile  inits and reads the passed file
   */
  public JOrbisComment(File inFile)
  {
    super();
    State foo = new State();
    this.state = foo;
    this.inFile = inFile;
    readInFile();
  }


  /**
   *  Constructor for the JOrbisComment object
   *
   * @param  vorbisStream  inits and reads the passed inStream
   */
  public JOrbisComment(InputStream vorbisStream)
  {
    super();
    State foo = new State();
    this.state = foo;
    this.vorbisStream_ = vorbisStream_;
  }


  /**
   *  The main program for the JOrbisComment class.
   *
   * @param  arg  The command line arguments
   */
  public static void main(String[] arg)
  {
    if (arg.length > 0)
    {
      String input = arg[0];
      String output = arg[0];
      //System.out.println("arg="+arg.length );
      InputStream in = null;
      try
      {
        in = new FileInputStream(input);
      }
      catch (Exception e)
      {
        System.out.println(e);
      }
      State foo = new State();
      JOrbisComment jorbiscomment = new JOrbisComment(foo);
      jorbiscomment.read(in);
      System.out.println(foo.vc);

      // foo.vc.add("TEST=TESTTEST");
      // foo.vc.add_tag("TITLE", "demodemo");
      //System.out.println(foo.vc.query("TEST"));
      //System.out.println(foo.vc.query("TITLE"));
      //System.out.println(foo.vc.query("ARTIST"));

      // parse any passed comments and add to this
      if (arg.length > 2)
      {
        for (int pair = 1; pair < arg.length; pair+=2)
        {
          // an ogg comment was given on the commandline so add them tothe file
          jorbiscomment.setComment(arg[pair], arg[pair+1]);
          System.out.println("added ogg comment: " +
            arg[pair] + "=" + arg[pair+1]);
        }
      }

      if (arg.length > 1)
      {
        try
        {
          // persist any changes back to the file NOTE you could persist this to any other file as well.
          OutputStream out = new FileOutputStream(output);
          jorbiscomment.write(out);
          out.close();
        }
        catch (Exception e)
        {
          e.printStackTrace();
          System.out.println(e);
        }
      }
    }
    else
    {
      System.out.println(usage());
    }
  }


  public static String usage()
  {
    String retVal = "JOrbisComment - A Java query and writer for Ogg Vorbis File comments.";
    retVal+= Util.SYSTEM_LINE_SEPERATOR;
    retVal+= "> java ca.bc.webarts.tools.JOrbisComment [inputFile] [commentFiled commentValue]...";
    retVal+= Util.SYSTEM_LINE_SEPERATOR;
    retVal+= "   inputFile - is the ogg file to read(and writeiff comments are specified)";
    retVal+= Util.SYSTEM_LINE_SEPERATOR;
    retVal+= "   comments are specified in name value pairs";
    retVal+= Util.SYSTEM_LINE_SEPERATOR;
    retVal+= "   If only the inputFile is specified, then it is read and the comments are printed on the commandline.";
    retVal+= Util.SYSTEM_LINE_SEPERATOR;
    return retVal;
  }


  public boolean setArtist(String value)
  {
    return setComment("ARTIST",value);
  }


  public boolean setAlbum(String value)
  {
    return setComment("ALBUM",value);
  }


  public boolean setTitle(String value)
  {
    return setComment("TITLE",value);
  }


  public boolean setTracknumber(String value)
  {
    return setComment("TRACKNUMBER",value);
  }


  public boolean setTrackTotal(String value)
  {
    return setComment("TRACKTOTAL",value);
  }


  public boolean setGenre(String value)
  {
    return setComment("GENRE",value);
  }


  /**
   *  Sets a comment attribute of the JOrbisComment object BUT does not write to file.
   *
   * @param  field  ThSystem.out.printlne new comment value
   * @param  value  The new comment value
   * @return        Description of the Return Value
   */
  public boolean setComment(String field, String value)
  {
    boolean retVal = true;
    try
    {
      if (value==null) value="";
      this.state.vc.add_tag(field, value);
    }
    catch (Exception e)
    {
      e.printStackTrace();
      System.out.println("Can't add comment: " + field + "=" + value);
      retVal = false;
    }
    return retVal;
  }


  /**
   *  Gets the comment attribute of the JOrbisComment object. It is case
   *  sensitive - it will search for the exact match of the passed field.
   *
   * @param  field  Description of the Parameter
   * @return        The comment value
   */
  public String getComment(String field)
  {
    return (this.state.vc!=null?this.state.vc.query(field):"");

  }


  /**
   *  Gets the comment attribute of the JOrbisComment object. NOTE: that this method
   *  looks for the AllCaps version of this tag 1st, then the allLower version
   *
   * @param  smart  flag if the Uppercase should be prioritized.
   * @param  field  Description of the Parameter
   * @return        The comment value, or an empty string if not found.
   */
  public String getComment(String field, boolean smart)
  {
    String retVal = "";
    if (smart)
    {
      String caps = getComment(field.toUpperCase());
      String sml = getComment(field.toLowerCase());
      retVal = (caps!=null?caps:(sml!=null?sml:""));
    }
    else
      retVal = getComment(field);

    return retVal;
  }


  /**
   * Gets the artist attribute of the JOrbisComment object.
   * NOTE: It calls the smart upparcase prioritized getComment method.
   *
   * @return    The artist value
   */
  public String getArtist()
  {
    return getComment("artist",true);
  }


  /**
   *  Gets the album attribute of the JOrbisComment object.
   * NOTE: It calls the smart upparcase prioritized getComment method.
   *
   * @return    The album value
   */
  public String getAlbum()
  {
    return getComment("album",true);
  }


  /**
   *  Gets the title attribute of the JOrbisComment object. NOTE: that this method
   *  looks for the AllCaps version of this tag 1st, then the allLower version
   *
   * @return    The TITLE comment value, or an empty string if not found.
   */
  public String getTitle()
  {
    return getComment("title",true);
  }


  /**
   *  Gets the tracknumber attribute of the JOrbisComment object.
   * NOTE: It calls the smart upparcase prioritized getComment method.
   *
   * @return    The tracknumber value
   */
  public String getTracknumber()
  {
    return getComment("tracknumber",true);
  }


  /**
   *  Gets the tracktotal attribute of the JOrbisComment object.
   * NOTE: It calls the smart upparcase prioritized getComment method.
   *
   * @return    The tracktotal value
   */
  public String getTrackTotal()
  {
    return getComment("tracktotal",true);
  }


  /**
   *  Gets the genre attribute of the JOrbisComment object.
   * NOTE: It calls the smart upparcase prioritized getComment method.
   *
   * @return    The genre value
   */
  public String getGenre()
  {
    return getComment("genre",true);
  }


  /**
   *  Gets the date attribute of the JOrbisComment object.
   * NOTE: It calls the smart upparcase prioritized getComment method.
   *
   * @return    The date value
   */
  public String getDate()
  {
    return getComment("date",true);
  }


  /**
   *  Gets the MusicBrainz DiscID attribute of the JOrbisComment object.
   * NOTE: It calls the smart upparcase prioritized getComment method.
   *
   * @return    The MUSICBRAINZ_DISCID value
   */
  public String getMusicBrainzDiscID()
  {
    return getComment("MUSICBRAINZ_DISCID",true);
  }


  /**
   *  Reads from this classes inputstream  - vorbisStream_
   *
   */
  public void read(){ read(vorbisStream_);}


  /**
   *  Reads from the classes inFile
   *
   */
  public void readInFile()
  {
    InputStream in = null;
    try
    {
      in = new FileInputStream(this.inFile.toString());
      if (in != null)
      {
        this.read(in);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }


  /**
   *  Description of the Method
   *
   * @param  in  Description of the Parameter
   */
  public void read(InputStream in)
  {
    this.state.in = in;

    Page og = new Page();

    int index;
    byte[] buffer;
    int bytes = 0;

    state.oy = new SyncState();
    state.oy.init();

    index = state.oy.buffer(CHUNKSIZE);
    buffer = state.oy.data;
    try
    {
      bytes = this.state.in.read(buffer, index, CHUNKSIZE);
    }
    catch (Exception e)
    {
      //System.err.println("vorbisStream is "+(in==null?"":"NOT ")+" null");
      //System.err.println("buffer is "+(buffer==null?"":"NOT ")+" null");
      //System.err.println(e);
      //e.printStackTrace();
      return;
    }
    state.oy.wrote(bytes);

    if (state.oy.pageout(og) != 1)
    {
      if (bytes < CHUNKSIZE)
      {
        System.err.println("Input truncated or empty.");
      }
      else
      {
        System.err.println("Input is not an Ogg bitstream.");
      }
      // goto err;
      return;
    }
    state.serial = og.serialno();
    state.os = new StreamState();
    state.os.init(state.serial);
    //  os.reset();

    state.vi = new Info();
    state.vi.init();

    state.vc = new Comment();
    state.vc.init();

    if (state.os.pagein(og) < 0)
    {
      System.err.println("Error reading first page of Ogg bitstream data.");
      // goto err
      return;
    }

    Packet header_main = new Packet();

    if (state.os.packetout(header_main) != 1)
    {
      System.err.println("Error reading initial header packet.");
      // goto err
      return;
    }

    if (state.vi.synthesis_headerin(state.vc, header_main) < 0)
    {
      System.err.println("This Ogg bitstream does not contain Vorbis data.");
      // goto err
      return;
    }

    state.mainlen = header_main.bytes;
    state.mainbuf = new byte[state.mainlen];
    System.arraycopy(header_main.packet_base, header_main.packet,
        state.mainbuf, 0, state.mainlen);

    int i = 0;
    Packet header;
    Packet header_comments = new Packet();
    Packet header_codebooks = new Packet();

    header = header_comments;
    while (i < 2)
    {
      while (i < 2)
      {
        int result = state.oy.pageout(og);
        if (result == 0)
        {
          break;
        }
        /*
         *  Too little data so far
         */
        else if (result == 1)
        {
          state.os.pagein(og);
          while (i < 2)
          {
            result = state.os.packetout(header);
            if (result == 0)
            {
              break;
            }
            if (result == -1)
            {
              System.out.println("Corrupt secondary header.");
              //goto err;
              return;
            }
            state.vi.synthesis_headerin(state.vc, header);
            if (i == 1)
            {
              state.booklen = header.bytes;
              state.bookbuf = new byte[state.booklen];
              System.arraycopy(header.packet_base, header.packet,
                  state.bookbuf, 0, header.bytes);
            }
            i++;
            header = header_codebooks;
          }
        }
      }

      index = state.oy.buffer(CHUNKSIZE);
      buffer = state.oy.data;
      try
      {
        bytes = state.in.read(buffer, index, CHUNKSIZE);
      }
      catch (Exception e)
      {
        System.err.println(e);
        return;
      }

      if (bytes == 0 && i < 2)
      {
        System.out.println("EOF before end of vorbis headers.");
        // goto err;
        return;
      }
      state.oy.wrote(bytes);
    }

    //System.out.println(state.vi);
  }


  /**
   *  Writes (persists) the JOrbisComment object comments to the original file.
   *
   * @return      Description of the Return Value
   */
  int write()
  {
    int retVal = -1;
    try
    {
      // persist any changes back to the file NOTE you could persist this to any other file as well.
      OutputStream out = new FileOutputStream(this.inFile);
      retVal = write(out);
      out.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
      System.out.println(e);
    }
    return retVal;
  }


  /**
   *  Writes (persists) the JOrbisComment object comments to the outputstream.
   *
   * @param  out  Description of the Parameter
   * @return      Description of the Return Value
   */
  int write(OutputStream out)
  {
    StreamState streamout = new StreamState();
    Packet header_main = new Packet();
    Packet header_comments = new Packet();
    Packet header_codebooks = new Packet();

    Page ogout = new Page();

    Packet op = new Packet();
    long granpos = 0;

    int result;

    int index;
    byte[] buffer;

    int bytes;

    int eosin = 0;
    int needflush = 0;
    int needout = 0;

    header_main.bytes = state.mainlen;
    header_main.packet_base = state.mainbuf;
    header_main.packet = 0;
    header_main.b_o_s = 1;
    header_main.e_o_s = 0;
    header_main.granulepos = 0;

    header_codebooks.bytes = state.booklen;
    header_codebooks.packet_base = state.bookbuf;
    header_codebooks.packet = 0;
    header_codebooks.b_o_s = 0;
    header_codebooks.e_o_s = 0;
    header_codebooks.granulepos = 0;

    streamout.init(state.serial);

    state.vc.header_out(header_comments);

    streamout.packetin(header_main);
    streamout.packetin(header_comments);
    streamout.packetin(header_codebooks);

//System.out.println("%1");

    while ((result = streamout.flush(ogout)) != 0)
    {
//System.out.println("result="+result);
      try
      {
        out.write(ogout.header_base, ogout.header, ogout.header_len);
        out.flush();
      }
      catch (Exception e)
      {
        //goto cleanup;
        break;
      }
      try
      {
        out.write(ogout.body_base, ogout.body, ogout.body_len);
        out.flush();
      }
      catch (Exception e)
      {
        //goto cleanup;
        break;
      }
    }

//System.out.println("%2");

    while (state.fetch_next_packet(op) != 0)
    {
      int size = state.blocksize(op);
      granpos += size;
//System.out.println("#1");
      if (needflush != 0)
      {
//System.out.println("##1");
        if (streamout.flush(ogout) != 0)
        {
          try
          {
            out.write(ogout.header_base, ogout.header, ogout.header_len);
            out.flush();
          }
          catch (Exception e)
          {
            e.printStackTrace();
            //goto cleanup;
            return -1;
          }
          try
          {
            out.write(ogout.body_base, ogout.body, ogout.body_len);
            out.flush();
          }
          catch (Exception e)
          {
            e.printStackTrace();
//System.out.println("ogout.body_base.length="+ogout.body_base.length+
//                   ", ogout.body="+ogout.body+
//                   ", ogout.body_len="+ogout.body_len);
            //goto cleanup;
            return -1;
          }
        }
      }
//System.out.println("%2 eosin="+eosin);
      else if (needout != 0)
      {
//System.out.println("##2");
        if (streamout.pageout(ogout) != 0)
        {
          try
          {
            out.write(ogout.header_base, ogout.header, ogout.header_len);
            out.flush();
          }
          catch (Exception e)
          {
            e.printStackTrace();
            //goto cleanup;
            return -1;
          }
          try
          {
            out.write(ogout.body_base, ogout.body, ogout.body_len);
            out.flush();
          }
          catch (Exception e)
          {
            e.printStackTrace();
//System.out.println("ogout.body_base.length="+ogout.body_base.length+
//                   ", ogout.body="+ogout.body+
//                   ", ogout.body_len="+ogout.body_len);
            //goto cleanup;
            return -1;
          }
        }
      }

//System.out.println("#2");

      needflush = needout = 0;

      if (op.granulepos == -1)
      {
        op.granulepos = granpos;
        streamout.packetin(op);
      }
      else
      {
        if (granpos > op.granulepos)
        {
          granpos = op.granulepos;
          streamout.packetin(op);
          needflush = 1;
        }
        else
        {
          streamout.packetin(op);
          needout = 1;
        }
      }
//System.out.println("#3");
    }

//System.out.println("%3");

    streamout.e_o_s = 1;
    while (streamout.flush(ogout) != 0)
    {
      try
      {
        out.write(ogout.header_base, ogout.header, ogout.header_len);
        out.flush();
      }
      catch (Exception e)
      {
        e.printStackTrace();
        //goto cleanup;
        return -1;
      }
      try
      {
        out.write(ogout.body_base, ogout.body, ogout.body_len);
        out.flush();
      }
      catch (Exception e)
      {
        e.printStackTrace();
//System.out.println("ogout.body_base.length="+ogout.body_base.length+
//                   ", ogout.body="+ogout.body+
//                   ", ogout.body_len="+ogout.body_len);
        //goto cleanup;
        return -1;
      }
    }

//System.out.println("%4");

    state.vi.clear();
//System.out.println("%3 eosin="+eosin);

//System.out.println("%5");

    eosin = 0;
    /*
     *  clear it, because not all paths to here do
     */
    while (eosin == 0)
    {
      /*
       *  We reached eos, not eof
       */
      /*
       *  We copy the rest of the stream (other logical streams)
       *  through, a page at a time.
       */
      while (true)
      {
        result = state.oy.pageout(ogout);
//System.out.println(" result4="+result);
        if (result == 0)
        {
          break;
        }
        if (result < 0)
        {
          System.out.println("Corrupt or missing data, continuing...");
        }
        else
        {
          /*
           *  Don't bother going through the rest, we can just
           *  write the page out now
           */
          try
          {
            out.write(ogout.header_base, ogout.header, ogout.header_len);
            out.flush();
          }
          catch (Exception e)
          {
            //goto cleanup;
            return -1;
          }
          try
          {
            out.write(ogout.body_base, ogout.body, ogout.body_len);
            out.flush();
          }
          catch (Exception e)
          {
            //goto cleanup;
            return -1;
          }
        }
      }

      index = state.oy.buffer(CHUNKSIZE);
      buffer = state.oy.data;
      try
      {
        bytes = state.in.read(buffer, index, CHUNKSIZE);
      }
      catch (Exception e)
      {
        System.err.println(e);
        return -1;
      }
//System.out.println("bytes="+bytes);
      state.oy.wrote(bytes);

      if (bytes == 0 || bytes == -1)
      {
        eosin = 1;
        break;
      }
    }

    /*
     *  cleanup:
     *  ogg_stream_clear(&streamout);
     *  ogg_packet_clear(&header_comments);
     *  free(state->mainbuf);
     *  free(state->bookbuf);
     *  jorbiscomment_clear_internals(state);
     *  if(!eosin)
     *  {
     *  state->lasterror =
     *  "Error writing stream to output. "
     *  "Output stream may be corrupted or truncated.";
     *  return -1;
     *  }
     *  return 0;
     *  }
     */
    return 0;
  }
}


/**
 *  Description of the Class
 *
 * @author    tgutwin
 */
class State
{


  /**  Description of the Field */
  private static int CHUNKSIZE = 4096;
  /**  Description of the Field */
  SyncState oy;
  /**  Description of the Field */
  StreamState os;
  /**  Description of the Field */
  Comment vc;
  /**  Description of the Field */
  Info vi;

  /**  Description of the Field */
  InputStream in;
  /**  Description of the Field */
  int serial;
  /**  Description of the Field */
  byte[] mainbuf;
  /**  Description of the Field */
  byte[] bookbuf;
  /**  Description of the Field */
  int mainlen;
  /**  Description of the Field */
  int booklen;
  /**  Description of the Field */
  String lasterror;

  /**  Description of the Field */
  int prevW;

  /**  Description of the Field */
  Page og = new Page();


  /**
   *  Description of the Method
   *
   * @param  p  Description of the Parameter
   * @return    Description of the Return Value
   */
  int blocksize(Packet p)
  {
    int _this = vi.blocksize(p);
    int ret = (_this + prevW) / 4;

    if (prevW == 0)
    {
      prevW = _this;
      return 0;
    }

    prevW = _this;
    return ret;
  }


  /**
   *  Description of the Method
   *
   * @param  p  Description of the Parameter
   * @return    Description of the Return Value
   */
  int fetch_next_packet(Packet p)
  {
    int result;
    byte[] buffer;
    int index;
    int bytes;

    result = os.packetout(p);

    if (result > 0)
    {
      return 1;
    }

    while (oy.pageout(og) <= 0)
    {
      index = oy.buffer(CHUNKSIZE);
      buffer = oy.data;
      try
      {
        bytes = in.read(buffer, index, CHUNKSIZE);
      }
      catch (Exception e)
      {
        System.err.println(e);
        return 0;
      }
      if (bytes > 0)
      {
        oy.wrote(bytes);
      }
      if (bytes == 0 || bytes == -1)
      {
        return 0;
      }
    }
    os.pagein(og);

    return fetch_next_packet(p);
  }
}

