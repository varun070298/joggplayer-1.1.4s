/*
 *  NativeAppLauncher.java
 *  $Source: /cvsroot2/open/projects/WebARTS/ca/bc/webarts/tools/StreamGobbler.java,v $
 *  $Revision: 1.1 $  $Date: 2002/10/17 21:24:16 $  $Locker:  $
 *  Copyright 2003 (C) WebARTS Design.   All rights reserved.
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package ca.bc.webarts.tools;
import java.io.*;


/**
 *  A simple class to take an input stream and dump it out to the commandline
 * with a prepended string. It is designed to run in its own thread so it can
 * pump the data silently in the background.
 *
 * @author    TGutwin
 */
public class StreamGobbler extends Thread
{
  /**  Flag so we can signal the control/signal the threads end. **/
  public boolean finishedGobbling_ = false;

  /**
    * The class InputStream that gets echoed to the designated output stream
    * (default=System.out).
    **/
  InputStream is_ = null;

  /**  The location of any output. **/
  PrintStream ps_ = System.out;

  /**  The location of any output. **/
  OutputStream os_ = System.out;

  /**  The string to get prepended to the output from the InputStream. **/
  String type_ = "";

  /** signals if we are gobbling input instead of the default outputstreams. **/
  boolean inputGobbler_ = false;

  /** the lin sep chars **/
  String lineSep_ = System.getProperty("line.separator");

  /** the input from in stream **/
  String input_ = null;

  /** the thread aborted flag. **/
  boolean aborted_ = false;

  /** a control flag that turns on/off the capturing of the input stream **/
  boolean capture_ = false;

  /**
   *  The buffer used to store the input stream data IFF the capture has been
   *  turned on with the capture_ flag.
   **/
  StringBuffer captureBuffer_ = new StringBuffer();


  /**
   *  A Simple Constructor for the StreamGobbler object WITHOUT assigning a
   *  stream to gobble. YOU HAVE TO ASSIGN THE InputStream before using this object.
   *  It that ends up using
   *  System.out as the Output PrintStream. So it simply dumps the monitored
   *  InputStream to System.out.
   *
   */
  public StreamGobbler()
  {

  }


  /**
   *  A Simple Constructor for the StreamGobbler object that ends up using
   *  System.out as the Output PrintStream. So it simply dumps the monitored
   *  InputStream to System.out.
   *
   * @param  inputstream  The stream to watch for input.
   * @param  string       The string to get prepended to the output from the
   *                      InputStream before it gets pumped into the output
   *                      PrintStream.
   */
  public StreamGobbler(InputStream inputstream, String string)
  {
    is_ = inputstream;
    if (string != null)
    {
      type_ = string;
    }
  }


  /**
   *  Constructor for the StreamGobbler object that requires all parameters to
   *  be spec'd.
   *
   * @param  inputstream  The stream to watch for input.
   * @param  string       The string to get prepended to the output from the
   *                      InputStream before it gets pumped into the output
   *                      PrintStream.
   * @param  os           An output PrintStream to send the data to.
   *
   */
  public StreamGobbler(InputStream inputstream, String string, PrintStream os)
  {
    is_ = inputstream;
    if (os != null)
      ps_ = os;
    else
      capture_ = true;
    if (string != null)
    {
      type_ = string;
    }
  }


  /**
   *  Constructor for the StreamGobbler object that requires all parameters to
   *  be spec'd.
   *
   * @param  inputstream  The stream to watch for input.
   * @param  ps           An output PrintStream to send the data to, If
   *                      this is null it simply captures the output.
   *
   */
  public StreamGobbler(InputStream inputstream, PrintStream ps)
  {
    is_ = inputstream;
    if (ps != null)
      ps_ = ps;
    else
      capture_ = true;
  }


  /**  Main processing method for the StreamGobbler object */
  public void run()
  {
    if (!inputGobbler_)
    {
      if (type_ != null)
      {
        try
        {
          InputStreamReader inputstreamreader
               = new InputStreamReader(is_);
          if (inputstreamreader != null)
          {
            // set up the output stream
            BufferedReader bufferedreader
                 = new BufferedReader(inputstreamreader);
            if (bufferedreader != null)
            {
              Object object = null;
              String string;
              int retry = 0;
              while (retry < 10)
              {
                try
                {
                  while (!finishedGobbling_ &&
                          (string = bufferedreader.readLine()) != null )
                  {
                    if (capture_)
                    {
                      captureBuffer_.append(string);
                      captureBuffer_.append(lineSep_);
                    }
                    if (ps_ !=null)
                    {
                      ps_.print(type_ );
                      ps_.println(string);
                    }
                  }
                }
                catch (IOException ioEx)
                {
                  ioEx.printStackTrace();
                  //finishedGobbling_ = true;
                }

                retry++;
              }
              //ps_.println(type_ + "> stream now done ("+retry+").");
              bufferedreader.close();
            }
            inputstreamreader.close();
            Object object = null;
          }
          Object object = null;
          finishedGobbling_ = true;
        }
        catch (Exception exception)
        {
          exception.printStackTrace();
          finishedGobbling_ = true;
        }
      }
    }
    else
    {
      // we are gobbling and simply dumping the inputstream to the outputstream
      try
      {
        if (os_ !=null) // then transfer the stream to the output
        {
          BufferedWriter out = new BufferedWriter(
            new OutputStreamWriter(os_));
          if(input_ != null)
          {
            for(int i = 0; i < input_.length(); i++)
            {
              char ch = input_.charAt(i);
              if(ch == '\n')
                out.write(lineSep_);
              else
                out.write(ch);
            }
          }
          out.close();
        }

      }
      catch(Exception e)
      {
        if(!aborted_)
        {
          System.out.println(e.toString());
        }
      }
      finally
      {
        //threadDone();
      }
    }
  }


  public void abort()
  {
    aborted_ = true;
    ps_.close();
  }


  /**
    * Set Method for class field 'type_' which sets the string to prepend to the output.
    *
    * @param is the value to set this class field to.
    *
    **/
  public  void setPrependString(String prepend)
  {
    if (prepend != null)
      this.type_ = prepend;
    else
      this.type_ = "";
  }  // setPrependString Method


  /**
    * Get Method for class field 'type_' which is the string to prepend to the output.
    *
    * @return String - The value the class field 'type_'.
    *
    **/
  public String getPrependString()
  {
    return type_;
  }  // getIs_ Method


  /**
    * Set Method for class field 'is_'.
    *
    * @param is is the value to set this class field to.
    *
    **/
  public  void setInputStream(InputStream is)
  {
    this.is_ = is;
  }  // setIs_ Method


  /**
    * Get Method for class field 'is_'.
    *
    * @return InputStream - The value the class field 'is_'.
    *
    **/
  public InputStream getInputStream()
  {
    return is_;
  }  // getIs_ Method


    /**
    * Set Method for class field 'ps_'.
    *
    * @param ps_ is the value to set this class field to.
    *
    **/
  public  void setOs_(PrintStream ps_)
  {
    this.ps_ = ps_;
  }  // setOs_ Method


  /**
    * Get Method for class field 'ps_'.
    *
    * @return PrintStream - The value the class field 'ps_'.
    *
    **/
  public PrintStream getOs_()
  {
    return ps_;
  }  // getOs_ Method


  /**
    * Gets the inputStreams captured data.
    *
    * @return StringBuffer - The value the class field 'captureBuffer_'.
    *
    **/
  public String getCapturedOutput()
  {
    return captureBuffer_.toString();
  }  // getCaptureBuffer_ Method


  /**
    * Set Method for class field 'capture_'. It turns on or off this classes
    * ability to capture and internally stor the InputStream data.
    *
    * @param capture_ is the value to set this class field to.
    *
    **/
  public  void setCapture(boolean capture)
  {
    this.capture_ = capture;
  }  // setCapture_ Method


  /**
    * Get Method for class field 'capture_'.
    *
    * @return boolean - The value the class field 'capture_'.
    *
    **/
  public boolean getCapture()
  {
    return capture_;
  }  // getCapture_ Method

}
