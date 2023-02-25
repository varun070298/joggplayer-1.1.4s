/*
 *  $Source: v:/cvsroot/open/projects/WebARTS/ca/bc/webarts/tools/Log.java,v $
 *  $Revision: 1.10 $  $Date: 2002/04/04 23:04:26 $  $Locker:  $
 *  Copyright (C) 2001 WebARTS Design,
 *  North Vancouver Canada. All Rights Reserved.
 *
 *  Written by Tom Gutwin - WebARTS Design.
 *  http://www..webarts.bc.ca
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
/*
Here is the revision log
------------------------
$Log: Log.java,v $
Revision 1.10  2002/04/04 23:04:26  anonymous
Added a few extra null checks to ensure a Log gets created.

Revision 1.9  2002/04/03 00:01:42  anonymous
Another fix to the logLevel check.

Revision 1.8  2002/04/02 23:31:36  anonymous
Fixed the logLevel_ compare on the debug methods.

Revision 1.7  2002/03/14 23:09:52  tgutwin

Added/Updated Many Comments.
Added set and get logLeve and LogLevelString.
logEntry is now private.
Added the checking for logLevel on log methods.

Revision 1.6  2001/12/04 06:43:49  tgutwin

Added a null check into the log entry method.

Revision 1.5  2001/11/21 03:59:11  tgutwin

Added a get instance() method that ssimply returns the log_.
It is to be used when you don't want to create a log.

Revision 1.4  2001/10/08 20:28:26  tgutwin

Made the Log Static ( only one instance Used. Plus added an initial std out message as to where the log is going.

Revision 1.3  2001/10/07 23:36:33  tgutwin

Created a single Log Output stream that different sources are now mapped into.
ie ... FileStream StdOut, or pip Stream.

*/

package ca.bc.webarts.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.Exception;
import java.util.Calendar;
import java.util.Stack;

/**
 *  A Simple Log class that can be set to output to a file, the Console or any
 *  specified OutputStream. It provides a few simple log entry methods with
 *  method entry/exit tracking.
 *
 * @author     Tom Gutwin P.Eng
 * @created    September 17, 2001
 */
public class Log
{
  private static boolean instantiated_ = false;
  private static Log log_ = null;
  /**
   *  Constant Specifying NO logging will occur.
   */
  public final static short NONE = 0;
  /**
   *  Constant Specifying minimal logging
   *  will occur (only Method entry and exit).
   */
  public final static short QUIET = 1;
  /**
   *  Constant Specifying only Major Log events logging will occur.
   */
  public final static short MAJOR = 2;
  /**
   *  Constant Specifying Major and Minor Log events logging will occur.
   */
  public final static short MINOR = 3;
  /**
   *  Constant Specifying Major Minor and Debug Log events logging will occur.
   */
  public final static short DEBUG = 4;
  /**
   *  Constant Specifying ALL log event logging will occur.
   */
  public final static short FULL = 5;

  /**
   *  Constant Specifying that the logging output will go to System.out.
   */
  public final static short CONSOLE = 50;
  /**
   *  Constant Specifying that the logging output will go to a spec'd file.
   */
  public final static short FILE = 51;
  /**
   *  Constant Specifying that the logging output will go to a
   *  spec'd OutputStream.
   */
  public final static short PIPE = 52;

  /**
   *  The Standard OutputStream.
   */
  private final static PrintStream STD_OUT = System.out;

  /**
   *  Description of the Field
   */
  static private String    logFileName_ = "logfile.txt";
  /**
   *  Description of the Field
   */
  static private String    msgSpacing_ = "";
  /**
   *  Description of the Field
   */
  static private OutputStream userOut_ = STD_OUT;
  /**
   *  The stream that ALL logging ends upo getting directed to.
   */
  static private PrintStream logOut_ = null;
  /**
   *  Description of the Field
   */
  static private File      logFile_ = null;
  /**
   *  Description of the Field
   */
  static private FileWriter logFileWriter_ = null;
  /**
   *  Description of the Field
   */
  static private short     logLevel_ = MAJOR;
  /**
   *  Description of the Field
   */
  static private short     outSpot_ = CONSOLE;
  /**
   *  Description of the Field
   */
  static private Calendar  calendar_ = Calendar.getInstance();

  /**
   *  A stack of the method call history.
   */
  static private Stack     currentMethod_ = new Stack();


  /**
   *  Default Constructor for the Log object. The ouput location is Console and
   *  the log level is Major.
   */
  private Log()
  {
    outSpot_ = CONSOLE;
    logLevel_ = MAJOR;
    instantiated_ = true;
    log_ = this;
    initLogOut();
  }


  /**
   *  Constructor for the Log object that sends the output to the Console at the
   *  specified Log Level.
   *
   * @param  logLevel  The desired Logging level (must be one of the Constants
   *                   spec'd in this class).
   */
  private Log(short logLevel)
  {
    outSpot_ = CONSOLE;
    logLevel_ = logLevel;
    instantiated_ = true;
    log_ = this;
    initLogOut();
  }


  /**
   *  Constructor for the Log object that sends the output to an OutputStream
   *  Pipe at the specified Log Level.
   *
   * @param  logLevel  The desired Logging level (must be one of the Constants
   *                   spec'd in this class).
   * @param  o         The OutputStream to Pipe the logging to.
   */
  private Log(short logLevel, OutputStream o)
  {
    outSpot_ = PIPE;
    logLevel_ = logLevel;
    userOut_ = o;
    instantiated_ = true;
    log_ = this;
    initLogOut();
  }


  /**
   *  Constructor for the Log object that sends the output to a File
   *  at the specified Log Level.
   *
   * @param  logLevel  The desired Logging level (must be one of the Constants
   *                   spec'd in this class).
   * @param  logFile   The File to send the Logging to.
   */
  private Log(short logLevel, File logFile)
  {
    outSpot_ = FILE;
    logLevel_ = logLevel;
    if (!setLogFile(logFile))
    {
      outSpot_ = CONSOLE;
      initLogOut();
      System.out.println("Error Creating Logfile.");
    }
    else
    {
      instantiated_ = true;
      log_ = this;
      initLogOut();
      //System.out.println("Creating Log: "+logFileName_+" at level "+
        //getLogLevelString());
    }
  }


  /**
   *  Constructor for the Log object that sends the output to a File with the
   *  spec'd filename at the specified Log Level.
   *
   * @param  logLevel  The desired Logging level (must be one of the Constants
   *                   spec'd in this class).
   * @param  logName   The filename for the File to send Logging output to.
   */
  private Log(short logLevel, String logName)
  {
    outSpot_ = FILE;
    logLevel_ = logLevel;

    if (!setLogFile(logName))
    {
      outSpot_ = CONSOLE;
      initLogOut();
      System.out.println("Error Creating Logfile.");
    }
    else
    {
      instantiated_ = true;
      log_ = this;
      initLogOut();
      //System.out.println("In Log(): Creating Log: "+logFileName_+" at level "+
        //getLogLevelString());
    }
  }


  /**
   * Singleton method to get the log instance going to a File.
   **/
  public static Log createLog(short logLevel, File logFile)
  {
    Log retVal = null;
    if (instantiated_ && log_ != null)
    {
      retVal = log_;
    }
    else
    {
      retVal = new Log(logLevel, logFile);
      log_ = retVal;
      logEntry("Log.createLog", "Creating Log: "+logFileName_+" at level "+
        getLogLevelString());
//System.out.println("Log.createLog(short logLevel, File): File="+logFile.toString());
    }
    return retVal;
  }


  /**
   * Singleton method to get the log instance going to an OutputStream.
   **/
  public static Log createLog(short logLevel, OutputStream stream)
  {
    Log retVal = null;
    if (instantiated_ && log_ != null)
    {
      retVal = log_;
    }
    else
    {
      retVal = new Log(logLevel, stream);
      log_ = retVal;
      logEntry("Log.createLog", "Creating Log: "+logFileName_+" at level "+
        getLogLevelString());
//System.out.println("Log.createLog(short logLevel, OutputStream): OutputStream="+stream);
    }
    return retVal;
  }


  /**
   * Singleton method to get the log instance going to a File.
   **/
  public static Log createLog(short logLevel, String fName)
  {
    //System.out.println("Into createLog");
    //System.out.println("instantiated_: "+instantiated_);
    //System.out.println("log_ != null: "+(log_ != null));
    Log retVal = null;
    if (instantiated_ && log_ != null)
      retVal = log_;
    else
    {
      retVal = new Log(logLevel, fName);
      log_ = retVal;
      logEntry("Log.createLog", "Creating Log: "+fName+" at level "+
        getLogLevelString());
      //System.out.println("Creating Log: "+fName+" at level "+
        //getLogLevelString());
    }
    return retVal;
  }


  /**
   * Singleton method to get the log instance going to stdout.
   **/
  public static Log createLog(short logLevel)
  {
    Log retVal = null;
    if (instantiated_ && log_ != null)
      retVal = log_;
    else
    {
      retVal = new Log(logLevel);
      log_ = retVal;
      logEntry("Log.createLog", "Creating Log: "+logFileName_+" at level "+
        getLogLevelString());
//System.out.println("Log.createLog(short logLevel)");
    }
    return retVal;
  }

  /**
   * Singleton method to get the log instance NULL if not init
   * yet with a createLog call.
   **/
  public static Log getInstance()
  {
    return log_;
  }


  public static synchronized boolean alreadyInstantiated()
  {
    return instantiated_;
  }


  /**
   * Sets the current Logging Level.
   *
   * @return true if it was set false if NOT set due to level out out bounds.
   **/
  public static synchronized boolean setLogLevel(short level)
  {
    boolean retVal = false;
    if (level >= NONE && level <= FULL)
    {
      retVal = true;
      logLevel_ = level;
    }
    return retVal;
  }


  public static short getLogLevel()
  {
    return logLevel_;
  }


  public static String getLogLevelString()
  {
    String retVal = "";
    switch (logLevel_)
    {
      case NONE:
        retVal="NONE";
        break;
      case QUIET:
        retVal="QUIET";
        break;
      case MAJOR:
        retVal="MAJOR";
        break;
      case MINOR:
        retVal="MINOR";
        break;
      case DEBUG:
        retVal="DEBUG";
        break;
      case FULL:
        retVal="FULL";
        break;
    }
    return retVal;
  }


  /**
   *  Creates a File object based on the spec'd filename and then inits the
   *  logFile_ attribute.
   *
   * @param  fName  The filename to use for the new logFile_.
   * @return        true if successfully created and init.
   */
  static public boolean setLogFile(String fName)
  {
    boolean retVal = true;
    String newFname = "";
    String tempStr = "";
    for (int i = 0; i < fName.length(); i++)
    {
      tempStr = String.valueOf(fName.charAt(i));
      if (!(" ".equals(tempStr)) && !(":".equals(tempStr)))
      {
        newFname += tempStr;
      }
      else
      {
        newFname += "_";
      }
    }
    try
    {
      logFile_ = new File(newFname);
      logFileName_ = newFname;
    }
    catch (NullPointerException nullEx)
    {
      STD_OUT.println("Error Creating Logfile1: \n  " + nullEx.getMessage());
      retVal = false;
    }

    return retVal;
  }


  /**
   *  Sets the logFile_ attribute to the spec'd File.
   *
   * @param  logFile  The File object to setr to the logFile_.
   * @return          true if successfully init.
   */
  static public boolean setLogFile(File logFile)
  {
    boolean retVal = true;

    if (logFile != null && logFile.exists() && logFile.canRead())
    {
      logFile_ = logFile;
      logFileName_ = logFile.getName();
    }
    else
    {
      STD_OUT.println("Error Creating Logfile from Specified FILE. ");
      retVal = false;
    }

    return retVal;
  }


  /**
   *  Creates a timestamp for the current time in the form of
   *  'hour + "-" + min + "-" + sec + "-" + millis'.
   *
   * @return    The CurrentTimeStamp value.
   */
  static public String createCurrentTimeStamp()
  {
    String value = "";
    calendar_ = Calendar.getInstance();
    int currMillis = calendar_.get(calendar_.MILLISECOND);
    String millis = String.valueOf(currMillis);
    if (currMillis < 10)
    {
      millis = "00" + currMillis;
    }
    else if (currMillis < 100)
    {
      millis = "0" + currMillis;
    }
    int currSec = calendar_.get(calendar_.SECOND);
    String sec = String.valueOf(currSec);
    if (currSec < 10)
    {
      sec = "0" + currSec;
    }
    int currMin = calendar_.get(calendar_.MINUTE);
    String min = String.valueOf(currMin);
    if (currMin < 10)
    {
      min = "0" + currMin;
    }
    int currHr = calendar_.get(calendar_.HOUR);
    String hour = String.valueOf(currHr);
    if (currHr < 10)
    {
      hour = "0" + currHr;
    }

    return (String)hour + "-" + min + "-" + sec + "-" + millis;
  }


  /**
   *  Cleanup method that closes any open Files.
   */
  static public void close()
  {
    try
    {
      if (logFileWriter_ != null)
      {
        STD_OUT.println("Closing Logfile: " + logFile_.getAbsolutePath());
        logFileWriter_.close();
      }
    }
    catch (IOException ioEx)
    {
      STD_OUT.println("Error Closing Logfile: \n  " + ioEx.getMessage());
    }
  }


  /**
   *  Generic uncategorized log entry.
   *
   * @param  value  The String to dump into the Log.
   */
  static private String getCurrentMethodName()
  {
    String retVal = "!";
    if (!currentMethod_.empty())
    {
      retVal = (String) currentMethod_.peek();
    }
    return retVal;
  }



  /**
   *  Generic uncategorized log entry.
   *
   * @param  value  The String to dump into the Log.
   */
  static private void logEntry(String value)
  {
    if (logOut_ != null && msgSpacing_!=null && !msgSpacing_.equals("") )
    {
      value = createCurrentTimeStamp() + ":" + msgSpacing_ + value;

      /* if (outSpot_ == FILE)
      {
        value += "\n";
      }
      */
      logOut_.println(value);
    }
  }


  /**
   *  Generic uncategorized log entry.
   *
   * @param  value  The String to dump into the Log.
   */
  static private void logEntry(String value, Exception ex)
  {
    if (logOut_ != null && msgSpacing_!=null && !msgSpacing_.equals("") )
    {
      value = createCurrentTimeStamp() + ":"+logLevel_ +" "+ msgSpacing_ + value;

     /* if (outSpot_ == FILE)
      {
        value += "\n";
      }
  */
      logOut_.println(value);
      ex.printStackTrace(logOut_);
    }
  }


  /**
   *  A log entry method
   *
   * @param  methodName  Description of Parameter
   * @param  value       The String to dump into the Log.
   */
  static private void logEntry(String methodName, String value)
  {
    logEntry(value);
  }


  /**
   *  Logs an entry into a method with a simple log message. It also keeps
   *  track of the entry so it can manage tabbing/spacing of log entries.
   *
   * @param  methodName  The method being entered.
   */
  static public void startMethod(String methodName)
  {
    if (logLevel_>QUIET)
    {
      currentMethod_.push(methodName);
      logEntry("Entering: " + methodName);
      msgSpacing_ += "  ";
    }
  }


  /**
   *  Logs the exit from a method with a simple log message. It also keeps
   *  track of the exit so it can manage tabbing/spacing of log entries.
   *
   * @param  methodName  The method being exited.
   */
  static public void endMethod(String methodName)
  {
    if (logLevel_>QUIET)
    {
      if (currentMethod_.size() > 0)
      {
        currentMethod_.pop();
        if (msgSpacing_.length() > 1)
        {
          msgSpacing_ = msgSpacing_.substring(0, msgSpacing_.length() - 2);
        }
      }
      logEntry("Exiting : " + methodName);
    }
  }


  /**
   *  Logs the exit from the last entered method with a simple log message.
   *  It also keeps track of the exit so it can manage tabbing/spacing
   *  of log entries.
   */
  static public void endMethod()
  {
    if (logLevel_>QUIET)
    {
      if (msgSpacing_.length() > 1)
      {
        msgSpacing_ = msgSpacing_.substring(0, msgSpacing_.length() - 2);
      }
      if (currentMethod_.size() > 0)
        logEntry("Exiting : " + currentMethod_.pop());
      else
        logEntry("Exiting : " );
    }
  }


  /**
   *  Logs a Major Log event with the spec'd method name prefix.
   *
   * @param  methodName  The methodname to prefix the log message with.
   * @param  value       The String to dump into the Log.
   */
  static public void major(String methodName, String value)
  {
    if (logLevel_>=MAJOR)
      logEntry("Major: " + methodName + ": " + value);
  }


  /**
   *  Logs a Minor level message prefixed with the spec'd methodname.
   *
   * @param  methodName  The methodname to prefix the log message with.
   * @param  value       The String to dump into the Log.
   */
  static public void minor(String methodName, String value)
  {
    if (logLevel_>=MINOR)
      logEntry("Minor: " + methodName + ": " + value);
  }


  /**
   *  Logs a Debug level message prefixed with the spec'd methodname.
   *
   * @param  methodName  The methodname to prefix the log message with.
   * @param  value       The String to dump into the Log.
   */
  static public void debug(String methodName, String value)
  {
    if (logLevel_>=DEBUG)
      logEntry("--> Debug: " + methodName + ": " + value);
  }


  /**
   *  Logs a Debug level message prefixed with the current methodname.
   *
   * @param  value  The String to dump into the Log.
   */
  static public void debug(String value)
  {
    if (logLevel_>=DEBUG)
      logEntry("--> Debug: " + getCurrentMethodName() + ": " + value);
  }


  /**
   *  Logs a Major level message prefixed with the current methodname.
   *
   * @param  value  The String to dump into the Log.
   */
  static public void major(String value)
  {
    if (logLevel_>=MAJOR)
    logEntry("Major: " + getCurrentMethodName() + ": " + value);
  }


  /**
   *  Logs a Major level message prefixed with the current methodname.
   *
   * @param  value  The String to dump into the Log.
   * @param  ex  An exception that will dump its stacktrace into the log.
   */
  static public void major(String value, Exception ex)
  {
    if (logLevel_>=MAJOR)
    logEntry("Major: " + getCurrentMethodName() + ": " + value, ex);
  }


  /**
   *  Logs a minor level message prefixed with the current methodname.
   *
   * @param  value  The String to dump into the Log.
   */
  static public void minor(String value)
  {
    if (logLevel_>=MINOR)
    logEntry("Minor: " + getCurrentMethodName() + ": " + value);
  }


  /**
   *  Logs a minor level message prefixed with the current methodname.
   *
   * @param  value  The String to dump into the Log.
   * @param  ex  An exception that will dump its stacktrace into the log.
   */
  static public void minor(String value, Exception ex)
  {
    if (logLevel_>=MINOR)
      logEntry("Minor: " + getCurrentMethodName() + ": " + value, ex);
  }


  /**
   *  Initializes the logOut_ Stream based on the outSpot specifired by the user.
   */
  static private void initLogOut()
  {
    switch (outSpot_)
    {
      case FILE:
        if (logFile_ != null)
        {
          try
          {
            logOut_ = new PrintStream(new FileOutputStream(logFile_), true);
          }
          catch (IOException ioEx)
          {
            STD_OUT.println("Error Creating Logfile:\n  " + ioEx.getMessage());
            outSpot_ = CONSOLE;
            initLogOut();
          }
        }
        else
        {
          outSpot_ = CONSOLE;
          initLogOut();
        }
        break;
      case PIPE:
        logOut_ = new PrintStream(userOut_, true);
        break;
      default: // default is CONSOLE
        logOut_ = new PrintStream(STD_OUT, true);
    }
  }
}

