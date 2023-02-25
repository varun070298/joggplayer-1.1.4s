/*
 *  $Source: /cvsroot2/open/projects/WebARTS/ca/bc/webarts/widgets/Util.java,v $
 *  $Name:  $
 *
 *  $Revision: 1.21 $
 *  $Date: 2005-04-10 11:53:16 -0700 (Sun, 10 Apr 2005) $
 *  $Locker:  $
 *
 *
 *  Written by Tom Gutwin - WebARTS Design.
 *  Copyright (C) 2001 WebARTS Design, North Vancouver Canada
 *  http://www.webarts.bc.ca
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
package ca.bc.webarts.widgets;

import ca.bc.webarts.tools.Log;
import ca.bc.webarts.tools.StreamGobbler;

import gnu.regexp.RE;
import gnu.regexp.REException;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import java.io.*;

import org.apache.tools.ant.util.FileUtils;
import org.apache.tools.ant.taskdefs.Replace;


/**
 *  A set of useful utility methods/functions for reuse.
 *
 * @author     Tom Gutwin P.Eng
 */
public class Util
{
  /**  A holder for this clients System File Separator.  */
  public final static String SYSTEM_FILE_SEPERATOR = File.separator;

  /**  A holder for this clients System line termination separator.  */
  public final static String SYSTEM_LINE_SEPERATOR =
                                           System.getProperty("line.separator");

  /**
   *  A constant to specify which dialog to open with the chooseFileDialog.
   */
  public final static short OPEN_DIALOG = 0;

  /**
   *  A constant to specify which dialog to open with the chooseFileDialog.
   */
  public final static short SAVE_DIALOG = 1;

  /**  The VM classpath (used in some methods)..  */
  public static String CLASSPATH = System.getProperty("class.path");

  /**  The users home ditrectory.  */
  public static String USERHOME = System.getProperty("user.home");

  /**  The users pwd ditrectory.  */
  public static String USERDIR = System.getProperty("user.dir");

  /**  A holder This classes name (used when logging).  */
  private static String CLASSNAME = "ca.bc.webarts.widgets.Util";

  /**
   *  the Metal LookAndFeel CONSTANT.
   */
   public static final short METAL_LOOKANDFEEL = 0;

  /**
   *  the Windoze LookAndFeel CONSTANT.
   */
   public static final short WINDOZE_LOOKANDFEEL = 1;

  /**
   *  the Motif LookAndFeel CONSTANT.
   */
   public static final short MOTIF_LOOKANDFEEL = 2;

  /**
   *  the Mac LookAndFeel CONSTANT.
   */
   public static final short MAC_LOOKANDFEEL = 3;

  /**
   *  the Default LookAndFeel CONSTANT. DEFAULT_LOOKANDFEEL = METAL_LOOKANDFEEL
   */
   public static final short DEFAULT_LOOKANDFEEL = METAL_LOOKANDFEEL;

  /**
   *  the Mac LookAndFeel class name
   */
  public static String macClassName =
      "com.sun.java.swing.plaf.mac.MacLookAndFeel";
  /**
   *  the Java LookAndFeel class name
   */
  public static String metalClassName =
      "javax.swing.plaf.metal.MetalLookAndFeel";
  /**
   *  the motif LookAndFeel class name
   */
  public static String motifClassName =
      "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
  /**
   *  the Windoze LookAndFeel class name
   */
  public static String windowsClassName =
      "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";

  /**  Gets a Log instance in case any calling classes are using it.  */
  private static Log log_ = Log.getInstance();

  /**  Class flag signifying if the initUtil method has been called  */
  private static boolean initUtil = false;

  /**  Class flag for the threadWatchdog method to enable the calling user
   *   to stop the watch.  */
  public static boolean watchdogReset = false;


  /**  Inits the log and some other vars.*/
  private static synchronized void initUtil()
  {
    final String methodName = CLASSNAME + ": initUtil()";

    if (!initUtil || log_ == null)
    {
      //System.out.println("Into the init block");
      log_ = Log.getInstance();
      if (log_ != null)
      {
        log_.setLogLevel(Log.MAJOR);
        log_.startMethod(methodName);
      }
      CLASSPATH = System.getProperty("java.class.path");
      //System.out.println("In "+methodName);
      initUtil = true;
      if (log_ != null)
      {
        log_.endMethod();
      }

    }
  }


  /**
   *  Ensures that a folder exists.
   *  <TABLEWIDTH="100%" BORDER="0" CELLSPACING="1" CELLPADDING="2">
   *
   *    <TR>
   *
   *      <TDCOLSPAN="2">
   *        <H2>Ensures that a folder exists</H2>
   *      </TD>
   *
   *    </TR>
   *
   *    <TR>
   *
   *      <TDCOLSPAN="2">
   *        &nbsp;<BR>
   *        <B>Description:</B> <BR>
   *        use it like this: <br>
   *        ensureFolderExists(new File(fileName).getParentFile());
   *      </TD>
   *
   *    </TR>
   *
   *  </TABLE>
   *
   *
   * @param  folder  The File object to check.
   */
  public static void ensureFolderExists(File folder)
  {
    //System.out.println(" 1 Making Dir: "+folder.getAbsolutePath());
    if ((folder != null) && !(folder.isDirectory() ))
    {
      //System.out.println(" 2 Making Dir: "+folder.getAbsolutePath());
      ensureFolderExists(folder.getParentFile());
      //System.out.print(" 3 Making Dir: "+folder.getAbsolutePath());
      boolean suc = folder.mkdir();
      //System.out.println(" -->"+(suc?"Successful":"Failed"));
    }
  }


  public static void ensureFolderExists(String foldername)
  {
    if (foldername != null && !foldername.equals(""))
    {
      File folder = new File(foldername);
      ensureFolderExists(folder);
    }
  }


  /**
   * Moves the specified file to the specified directory. It will create
   * the target dir if it does not exist.
   *
   * @param filename is the absoulte pathname of the file to move.
   * @param foldername is the directory to move it to.
   **/
  public static boolean moveFile(String filename, String foldername)
  {
    boolean retVal = false;
    if (foldername != null && !foldername.equals("") &&
        filename != null && !filename.equals(""))
    {
      File fromFile = new File(filename);
      if (fromFile != null && fromFile.exists())
      {
        File folder = new File(foldername);
        ensureFolderExists(folder);
        File toFile = new File(foldername+File.separator+fromFile.getName());
        retVal = fromFile.renameTo(toFile);
        if (!retVal)
        {
          // Try Native move call
          String [] ss = {filename,foldername};
          String ret = executeNativeApp("/bin/mv",ss,false);
          System.out.println(" Native mv: "+ ret);
        }
      }
    }
    return retVal;
  }


  /**
   *  A simple byte to Hex String converter.
   *
   * @param  b  The byte to convert
   * @return    The resultant Hex String
   */
  public static String toHEXString(byte b)
  {
    return ("" + "0123456789ABCDEF".charAt(0xf & b >> 4) +
        "0123456789ABCDEF".charAt(b & 0xF));
  }


  /**
   *  SetsIcon for the passed in Frame
   *
   * @param  imageFilename  - is the filename for the new icon
   * @param  appFrame       The new iconForApp value
   */
  public static void setIconForApp(JFrame appFrame, String imageFilename)
  {
    setIconForApp(appFrame, Util.class.getResource(imageFilename));
  }
  // end of setIconForApp


  /**
   *  SetsIcon for the passed in Frame
   *
   * @param  imageUrl  - is the URL for the new icon
   * @param  appFrame  The new iconForApp value
   */
  public static void setIconForApp(JFrame appFrame, URL imageUrl)
  {
    ImageIcon image = new ImageIcon(imageUrl);
    appFrame.setIconImage(image.getImage());
    image = null;
  }
  // end of setIconForApp


  /**
   *  Creates a timestamp for the current date. It is in the form of 'year +
   *  "-" + month "-" + date "-" + hour "-" + seconds<br>
   *  For example:  2003-245
   *
   * @return    The CurrentDateTime value.
   */
  public static String createCurrentDateTime()
  {
    Calendar calendar_ = Calendar.getInstance();

    return ""+ calendar_.get(Calendar.YEAR) + "-" +
                calendar_.get(Calendar.MONTH)+ "-" +
                calendar_.get(Calendar.DATE)+ "-" +
                calendar_.get(Calendar.	HOUR_OF_DAY )+ "-" +
                calendar_.get(Calendar.MINUTE);
  }


  /**
   *  Creates a timestamp for the current date. It is in the form of 'year +
   *  "-" + dayNum <br>
   *  For example:  2003-245
   *
   * @return    The CurrentTimeStamp value.
   */
  public static String createCurrentDateStamp()
  {
    Calendar calendar_ = Calendar.getInstance();

    return ""+ calendar_.get(Calendar.YEAR) + "-" +
           calendar_.get(Calendar.DAY_OF_YEAR);
  }


  /**
   *  Creates a timestamp for the current date and time. It is in the form of 'year +
   *  "-" + dayNum + "-" + hour + "-" + min + "-" + sec + "-" + millis'.<br>
   *  For example:  2003-245-14-34-52-224
   *
   * @return    The CurrentTimeStamp value.
   */
  public static String createCurrentDateTimeStamp()
  {
    Calendar calendar_ = Calendar.getInstance();

    return ""+ calendar_.get(Calendar.YEAR) + "-" +
           calendar_.get(Calendar.DAY_OF_YEAR) + "-" +createCurrentTimeStamp();
  }


  /**
   *  Creates a timestamp for the current time in the form of 'hour + "-" + min
   *  + "-" + sec + "-" + millis'.
   *
   * @return    The CurrentTimeStamp value.
   */
  public static String createCurrentTimeStamp()
  {
    String value = "";
    Calendar calendar_ = Calendar.getInstance();
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
    int currHr = calendar_.get(calendar_.HOUR_OF_DAY);
    String hour = String.valueOf(currHr);
    if (currHr < 10)
    {
      hour = "0" + currHr;
    }

    return (String) hour + "-" + min + "-" + sec + "-" + millis;
  }


  /**
   *  Creates a Timer timestamp from the current time to the passed startTime
   *  in the form of <BR>
   * 'hour + "-" + min + "-" + sec + "-" + millis'.
   *
   * THIS METHOD IS NOT COMPLETED. IT DOES NOT WORK YET.
   *
   * @return    The Timer Timestamp value.
   */
  public static String createTimerTimeStamp(int startTimeMillis)
  {
    String value = "";
    Calendar calendar_ = Calendar.getInstance();
    int currMillis = calendar_.get(calendar_.MILLISECOND) - startTimeMillis;
    String millis = String.valueOf(currMillis);
    int currSec = calendar_.get(calendar_.SECOND);
    String sec = String.valueOf(currSec);
    int currMin = calendar_.get(calendar_.MINUTE);
    String min = String.valueOf(currMin);
    int currHr = calendar_.get(calendar_.HOUR_OF_DAY);
    String hour = String.valueOf(currHr);
     if (currMillis < 10)
    {
      millis = "00" + currMillis;
    }
    else if (currMillis < 100)
    {
      millis = "0" + currMillis;
    }
   if (currSec < 10)
    {
      sec = "0" + currSec;
    }
    if (currMin < 10)
    {
      min = "0" + currMin;
    }
    if (currHr < 10)
    {
      hour = "0" + currHr;
    }

    return (String) hour + "-" + min + "-" + sec + "-" + millis;
  }


  /**
   *  A simple String token replacement routine. Replaces all occurences of the
   *  token parameter with the replacement value in the passed in sentence
   *  parameter.
   *
   * @param  sentence     The String to perform the token replacement on
   * @param  token        the token String to seartch for and replace
   * @param  replacement  the tokens replacement value
   * @return              The new token replaced string
   */
  public static String tokenReplace(String sentence,
      String token,
      String replacement)
  {
    initUtil();
    final String methodName = CLASSNAME + ": tokenReplace(" +
        sentence + " " + token + " " + replacement;
    if (log_ != null)
    {
      log_.startMethod(methodName);
    }
    int a = 0;
    String retVal = "";
    while ((a = sentence.indexOf(token)) > -1)
    {
      retVal += sentence.substring(0, a) + replacement;
      sentence = sentence.substring(a + token.length());
      // System.out.println(wort);
    }
    // if a > -1
    retVal += sentence;
    if (log_ != null)
    {
      log_.endMethod();
    }
    return retVal;
  }


  /**
   *  Recursively deletes all the files in the spec'd directory and
   *  subdirectories, and then removes the dir.
   *
   * @param  fileName  The filename of the directory to delete.
   */
  public static boolean removeDir(String fileName)
  {
    initUtil();
    boolean retVal = false;
    File dirToDel = initDirFile(fileName);
    if (dirToDel != null)
    {
      String[] files = dirToDel.list();
      File tempFile;

      //loop through all the files in the dir DELETING them
      for (int i = 0; i < files.length; i++)
      {
        tempFile = new File(fileName + File.separator + files[i]);
        if (tempFile.isDirectory())
        {
          // recurse on the subdirectory
          removeDir(fileName + File.separator + files[i]);
        }
        else
        {
          // Delete all the files
          tempFile.delete();
        }
      }
      retVal = dirToDel.delete();
    }
    return retVal;
  }


  /**
   *  Deletes a file.
   *
   * @param  fileName  The filename to delete.
   */
  public static boolean removeFile(String fileName)
  {
    initUtil();
    boolean retVal = false;
    if (fileName != null)
    {
      File tempFile = new File(fileName);
      if (tempFile != null && tempFile.exists())
        retVal = tempFile.delete();
    }
    return retVal;
  }


  /**
   *  Deletes all files in the spec'd dir that are older than the spec'd
   *  number of days.
   *
   * @param  dirName  The directory to parse for files to delete.
   * @param  numDays   The age of the files to delete.
   * @param  recurse   Do the removal recursively.
   */
  public static boolean removeOldFiles(String dirName,
                                       int numDays,
                                       boolean recurse)
  {
    initUtil();
    boolean retVal = false;
    final String methodName = CLASSNAME + ": removeOldFiles()";
    if (log_ != null)
    {
      log_.startMethod(methodName);
    }
    File dirFile = new File(dirName);

    if (dirFile.isDirectory() && dirFile.canRead())
    {
      String[] waste = dirFile.list();
      File tempFile = null;
      for (int i = 0; i < waste.length; i++)
      {
        tempFile = new File(dirName + File.separator + waste[i]);
        if (tempFile != null && !tempFile.isDirectory())
        {
          // if file is older than numDays... delete it
          long fileAge = tempFile.lastModified();
          Calendar rightNow = Calendar.getInstance();
          long rightNowInMillis = rightNow.getTimeInMillis();
          long numDaysInMillis = (new java.lang.Integer(numDays)).longValue() *
                                 24l * 60l * 60l * 1000l;
          if (rightNowInMillis - fileAge > numDaysInMillis)
          {
            //delete the file
            System.out.println("Deleting file "+ tempFile.getPath());
            tempFile.delete();
          }
        }
        else if (tempFile != null && recurse)
        {
          removeOldFiles(dirName + File.separator + waste[i], numDays, recurse);
        }
      }
    }

    if (log_ != null)
    {
      log_.endMethod();
    }
    return retVal;
  }


  /**
   *  Obtains the bytes of data contained in all files in this dir
   *  (and subdirs).
   *
   * @param  fileName  The filename of the directory to delete.
   *
   * @return the size of the dir in bytes.
   */
  public static long dirSize(String fileName, boolean recurseSubdirs)
  {
    initUtil();
    long retVal = 0L;
    File dirToCalc = initDirFile(fileName);
    if (dirToCalc != null)
    {
      String[] files = dirToCalc.list();
      File tempFile;

      //loop through all the files in the dir
      if (files != null)
      {
        for (int i = 0; i < files.length; i++)
        {
          tempFile = new File(fileName + File.separator + files[i]);
          if (tempFile.isDirectory() && recurseSubdirs)
          {
            // recurse on the subdirectory
            retVal += dirSize(fileName + File.separator + files[i], true);
          }
          else
          {
            // add its size
            retVal += tempFile.length();
          }
        }
      }
      else
        log_.minor("Problem reading Directory "+fileName);
    }
    return retVal;
  }


  /**
   * Helper method to copy a file
   *
   * @param from the from filename
   * @param to the to filename
   **/
  public static void copyFile(String from, String to) throws IOException
  {
    File newFile = new File(to);
    if (newFile != null && newFile.exists())
      newFile.delete();
    (FileUtils.newFileUtils()).copyFile(from, to);
  }


  /**
   *  Token replace method for a given filename and then saves the file with the
   *  swapped tokens. If you don't want to overwrite the original file make a
   *  copy 1st and then call this method on the copy.
   *
   * @param  file     The file(name) to act on
   * @param  token    The search token (that will get replaced)
   * @param  value    The replacement string.
   */
  public static void tokenReplaceInFile(String file, String token, String value)
  {
    Replace replace = new Replace();
    replace.setProject(new org.apache.tools.ant.Project());
    replace.setFile(new File(file));
    replace.setToken(token);
    replace.setValue(value);
    replace.execute();
  }// -- tokenReplaceInFile()


  /**
   * Abstracts the reading of a file and returns the contents as a String.
   *
   * @param fileName is the file naem to read into a String
   * @return the Text file contents as a String
   **/
  public static String readFileToString(String fileName)
  {
    String stringLine;
    BufferedReader in;
    StringBuffer stringOut = new StringBuffer();
    try
    {
      in = new BufferedReader(new FileReader(fileName));
      while ((stringLine = in.readLine()) != null)
      {
        stringOut.append(stringLine);
        stringOut.append(SYSTEM_LINE_SEPERATOR);
      }
    }
    catch (FileNotFoundException fnfEx)
    {
      System.out.println("Cannot find file: "+fileName);
    }
    catch (IOException ioEx)
    {
      System.out.println("Error Reading File to String: "+fileName);
    }

    return stringOut.toString();
  }


      /**
   *  Searches the provided file for the search String.
   *
   * @param  searchStr      The searchString/regexp to search for.
   * @param  filename       The filename to search through.
   * @param  verbose        true will produce output to System.out just like the
   *                        UNIX find cmd
   *
   * @return an array of the matched lines
   */
  public static String [] searchInFile(String searchStr, String filename,
                                        boolean verbose)
  {
    initUtil();
    Vector retVal = new Vector();
    BufferedReader in;
    RE expression = null;
    String s;
    int regExFlags = RE.REG_ICASE;
    int numExtraBeforeLines = 2;
    String [] extraBeforeLines = new String [numExtraBeforeLines];
    int currLineNumber = 0;
    for (int i=0; i< numExtraBeforeLines ; i++)
      extraBeforeLines[i] = "";

    File rootFile = new File(filename);

    if (rootFile != null && !rootFile.isDirectory())
    {
      if(rootFile.canRead())
      {
        try
        {
          in = new BufferedReader(new FileReader(filename));
          currLineNumber = 0;
          while ((s = in.readLine()) != null)
          {
            currLineNumber++;
            expression = new RE(searchStr, regExFlags);
            if (expression.getMatch(s)!=null)
            {
              if ( verbose )
              {
                System.out.println("FILE: "+filename);
                for (int i=0; i< numExtraBeforeLines ; i++)
                  System.out.println("  "+currLineNumber+extraBeforeLines[i]);
                System.out.println("  "+currLineNumber + "  "+s);
                System.out.println();
              }
              retVal.addElement(s);
            }
            for (int i=0; i< numExtraBeforeLines-1 ; i++)
              extraBeforeLines[i] = extraBeforeLines[i+1];
            extraBeforeLines[numExtraBeforeLines-1] = s;
          }
          in.close();
        }
        catch (REException reEx)
        {
          reEx.printStackTrace();
        }
        catch (FileNotFoundException fEx)
        {
          fEx.printStackTrace();
        }
        catch (IOException ioEx)
        {
          ioEx.printStackTrace();
        }

      }
    }
    String [] rets = new String [retVal.size()];

    for (int i=0; i < retVal.size(); i++)
    {
      rets[i] = (String) retVal.elementAt(i);
    }
    return rets;
  }


  /**
   *  Searches the provided file/directory (and subdirs) for the search String.
   *
   * @param  searchStr      The searchString/regexp to search for.
   * @param  filename       The filename/directoryName to search through.
   * @param  recurseSubdirs Flags if the sub-dirs are searched.
   * @param  fileRegExp     Flags if 'fileName' should be regarded as a regular
   *                        expression.
   * @param  verbose        true will produce output to System.out just like the
   *                        UNIX find cmd
   *
   * @return a hash of the full cannoical paths/and a Vector of the lineNumbers
   *                          in that file
   */
  public static Hashtable searchInFile(String searchStr, String filename,
                              boolean recurseSubdirs, boolean fileRegExp,
                              boolean verbose)
  {
    initUtil();
    Hashtable retVal = new Hashtable();
    Vector fileLineNumbers = new Vector();
    BufferedReader in;
    RE expression = null;
    String s;
    int regExFlags = RE.REG_ICASE;
    int numExtraBeforeLines = 2;
    String [] extraBeforeLines = new String [numExtraBeforeLines];
    String currFilename;
    int currLineNumber = 0;
    for (int i=0; i< numExtraBeforeLines ; i++)
      extraBeforeLines[i] = "";

    File rootFile = new File(filename);

    if (rootFile != null && !rootFile.isDirectory())
    {
      if(rootFile.canRead())
      {
        currFilename = filename;
        try
        {
          in = new BufferedReader(new FileReader(currFilename));
          fileLineNumbers.clear();
          currLineNumber = 0;
          while ((s = in.readLine()) != null)
          {
            currLineNumber++;
            expression = new RE(searchStr, regExFlags);
            if (expression.getMatch(s)!=null)
            {
              if ( verbose )
              {
                System.out.println("FILE: "+currFilename);
                for (int i=0; i< numExtraBeforeLines ; i++)
                  System.out.println("  "+currLineNumber+extraBeforeLines[i]);
                System.out.println("  "+currLineNumber + "  "+s);
                System.out.println();
              }
              fileLineNumbers.add(new Integer(currLineNumber));
            }
            for (int i=0; i< numExtraBeforeLines-1 ; i++)
              extraBeforeLines[i] = extraBeforeLines[i+1];
            extraBeforeLines[numExtraBeforeLines-1] = s;
          }
          in.close();
          if (fileLineNumbers.size() > 0)
            retVal.put(currFilename, fileLineNumbers.clone());
        }
        catch (REException reEx)
        {
          reEx.printStackTrace();
        }
        catch (FileNotFoundException fEx)
        {
          fEx.printStackTrace();
        }
        catch (IOException ioEx)
        {
          ioEx.printStackTrace();
        }
      }
    }
    else
    {
      // it is a directory so recurse on files if recursing flag is set
      if (recurseSubdirs)
      {
        String[] files = rootFile.list();
        File tempFile;
        //if ( verbose )
        //{
        //  System.out.println("Recursing into "+filename);
        //}

        //loop through all the files in the dir
        Hashtable recursedHash = null;
        for (int i = 0; files != null && i < files.length; i++)
        {
          recursedHash = null;
          recursedHash = searchInFile(searchStr,
                                      filename + File.separator + files[i],
                                      recurseSubdirs, fileRegExp, verbose);
          if (recursedHash != null ) retVal.putAll(recursedHash);
        }
      }
    }

    return retVal;
  }


  /**
   *  Searches the provided directory (and subdirs) for the requested file.
   *
   * @param  fileName       The filename/regexp to search for.
   * @param  directory      The filename of the directory to delete.
   * @param  recurseSubdirs Flags if the sub-dirs are searched.
   * @param  regExp         Flags if 'fileName' should be regarded as a regular
   *                        expression.
   * @param  verbose        true will produce output to System.out just like the
   *                        UNIX find cmd
   *
   * @return the full cannoical paths to the found files.
   */
  public static Vector findFile(String fileName, String directory,
                              boolean recurseSubdirs, boolean regExp, boolean verbose)
  {
    initUtil();
    final String methodName = CLASSNAME + ": findFile(" +
        fileName + " " + directory + " " + recurseSubdirs+ " " + regExp+ " " + verbose+ " )";
    if (log_ != null) log_.startMethod(methodName);
    Vector retVal = new Vector();
    File dirToSearch = initDirFile(directory);

    if (dirToSearch != null)
    {
      String[] files = dirToSearch.list();
      File tempFile;
      if ( files!= null) log_.debug("Searching "+files.length +" files&dirs");

      //loop through all the files in the dir
      for (int i = 0; files != null && i < files.length; i++)
      {
        tempFile = new File(directory + File.separator + files[i]);
        if (tempFile.isDirectory() && recurseSubdirs)
        {
          // recurse on the subdirectory
          retVal.addAll(findFile(fileName, directory + File.separator + files[i],
                             recurseSubdirs, regExp, verbose));
        }
        else if (!tempFile.isDirectory())
        {
          // does it match
          String absPath = tempFile.getAbsolutePath();
          log_.debug("Searching "+absPath);

          if (regExp)
          {
            try
            {
              RE expression = new RE(fileName);
              if (expression.isMatch(files[i]))
              {
                retVal.add(absPath);
                if (verbose) System.out.println(absPath);
              }
            }
            catch (REException reExp)
            {
              reExp.printStackTrace();
              break;
            }
          }
          else if (files[i].trim().indexOf(fileName) >= 0)
          {
            retVal.add(absPath);
            if (verbose)System.out.println(absPath);
          }
        }
      }
    }
    if (log_ != null) log_.endMethod(methodName);
    return retVal;
  }


  /**
   *  Initisalizes File only if it is directory.It is represented by the passed
   *  in String.
   *
   * @param  s  the directory name to init as a File
   * @return    An instatiated File object if the passed string is a dir, null
   *      if not
   */
  public static File initDirFile(String s)
  {
    initUtil();
    File retVal = null;
    File dirFile = new File(s);
    if (dirFile.isDirectory() && dirFile.canRead())
    {
      retVal = dirFile;
    }
    return retVal;
  }


/**
 * Renames files to a more URL friendly format.
 * <P>It goes through each file in the specified directory and renames them
 * using the spacesToCapsInFileName method of renaming.
 *
 * @see #spacesToCapsInFileName(String)
 * @see #dirFile
 **/
public static void spacesToCapsInDir(String dirName)
{
  spacesToCapsInDir(dirName, true);
}


/**
 * Renames files to a more URL friendly format.
 * <P>It goes through each file in the specified directory and renames them
 * using the spacesToCapsInFileName method of renaming.
 *
 * @see #spacesToCapsInFileName(String)
 * @see #dirFile
 **/
public static void spacesToCapsInDir(String dirName, boolean doUnderscores)
{
  File dirFile = initDirFile(dirName);
  String newDirName = dirName;
  boolean succeeded = false;
  // check and rename the initial dir
  if (dirFile.toString().indexOf(" ") != -1 ||
      dirFile.toString().indexOf("_") != -1)
  {
    newDirName = spacesToCapsInString(dirFile.toString(), doUnderscores);
    System.out.println("Renaming "+dirFile.toString()+" -> "+newDirName);
    //System.out.println("       "+dirFile.getAbsolutePath()+"   "+spacesToCapsInString(dirFile.toString(), true));
    succeeded = dirFile.renameTo(new File(newDirName));
  }
  if (succeeded)
  {
    succeeded = false;
    dirFile = initDirFile(newDirName);
    String [] fileName = dirFile.list();
    System.out.print("Formatting dir="+dirFile.toString());
    System.out.println("   It has "+fileName.length + " files.");
    if (dirFile != null &&
        dirFile.isDirectory() &&
        dirFile.canRead() &&
        newDirName  != null )
    {
      File tempFile;
      for (int i=0;i< fileName.length;i++)
      {
        tempFile = new File(newDirName+File.separator+fileName[i]);

        // do a depth first file rename of the subdir files
        if (tempFile != null && tempFile.isDirectory())
          spacesToCapsInDir(tempFile.toString(), doUnderscores);
        String newName = spacesToCapsInString(fileName[i], doUnderscores);
        //System.out.println(fileName[i] +" .. " +newName);
        try
        {
          if (newName !="" && fileName[i].length() != newName.length())
          {
            //System.out.println(i+".1) "+tempFile.toString());
            //System.out.print(i+".2) "+newDirName+File.separator+newName);
            succeeded = tempFile.renameTo(
              new File(newDirName+File.separator+newName));
            if (succeeded)
              System.out.println("  succeeded.");
            else
              System.out.println("");
            File tempFile2 = new File(newDirName+File.separator+fileName[i]);
            if (tempFile2.exists() &&
                tempFile.exists() &&
                !newName.toUpperCase().equals(fileName[i].toUpperCase()))
            {
              tempFile2.delete();
            }
          }
        }
        catch (SecurityException ex)
        {
          System.err.println("SecurityException " + ex);
        }
        catch (NullPointerException ex)
        {
          System.err.println("NullPointerException " + ex);
        }
      } //loop
    }
  }
  else
  {
    System.err.println("Could not rename "+dirName +" to "+newDirName);
  }
}


/**
 * Removes any space chars ' ' from a filename and Capitalizes the next char.
 * <P><B>NOTE:</B> The file is expected to be in
 * the current working directorydirectory
 *
 * @param fName a string representing the file to perform the action
 * @return the new filename representation
 **/
public static String spacesToCapsInFileName(String fName)
{
  String newName = "";
  File tempFile;
  if (fName != null){
    tempFile = new File(USERDIR+File.separator+fName);
    if(!tempFile.isDirectory())
    {
      newName = spacesToCapsInString(fName);
    }
  }
  return newName;
}


/**
 * Removes any space chars ' ' from a String and Capitalizes the next char.
 *
 * @param fName a string  perform the action
 * @return the new representation
 **/
public static String spacesToCapsInString(String fName)
{
  return spacesToCapsInString(fName, false);
}


/**
 * Removes any space chars ' ' from a String and Capitalizes the next char.
 *
 * @param fName a string  perform the action
 * @param includeUnderscoreflag if  '_' are also removed
 * @return the new representation
 **/
public static String spacesToCapsInString(String fName, boolean includeUnderscore)
{
  initUtil();
  String newName = "";
  boolean space = false;
  for (int j=0; j< fName.length(); j++)
  {
    char newChar = fName.charAt(j);
    if (newChar == ' ' || (newChar == '_' && includeUnderscore))
    {
      // set the space flag so the next char can be cap'd
      space = true;
    }
    else
    {
      if (space) // was the last char a space???
      {
        newName += String.valueOf(Character.toUpperCase(newChar));
      }
      else
      {
        newName += String.valueOf(newChar);
      }
      space = false;
    }
  }
  return newName;
}


/**
 * Takes a string and adds a space in front of any capitalized char.
 * Basically the reverse of the method spacesToCapsInString.
 *
 * @param fName a string  perform the action
 * @return the new representation
 **/
public static String capsToSpacesInString(String fName)
{
  initUtil();
  StringBuffer newName = new StringBuffer();
  char newChar;
  for (int j=0; j< fName.length(); j++)
  {
    newChar = fName.charAt(j);
    if (Character.isLetter(newChar) && Character.isUpperCase(newChar))
    {
      newName.append(" ");
    }
      newName.append(String.valueOf(newChar));
  }
  return newName.toString();
}


/**
 * Takes a string and adds a space in front of any capitalized char.
 * Basically the reverse of the method spacesToCapsInString.
 *
 * @param fName a string  perform the action
 * @return the new representation
 **/
public static String capsToUndersInString(String fName)
{
  initUtil();
  StringBuffer newName = new StringBuffer();
  char newChar;
  for (int j=0; j< fName.length(); j++)
  {
    newChar = fName.charAt(j);
    if (Character.isLetter(newChar) && Character.isUpperCase(newChar))
    {
      newName.append("_");
    }
      newName.append(String.valueOf(newChar));
  }
  return newName.toString();
}


  /**
   *  Searches the classpath for the specified filename and then returns the
   *  full path that is used for it.
   *
   * @param  filename  to look for in the classpath
   * @param  fallBack  if the file is not found in classpath
   * @return           the path description of the passed filename as found in
   *      the classpath.
   */
  public static String getFilePathFromClasspath(String filename, String fallBack)
  {
    initUtil();
    final String methodName = CLASSNAME +
        ": getFilePathFromClasspath(String, String)";
    if (log_ != null)
    {
      log_.startMethod(methodName);
    }
    String retVal = getFilePathFromClasspath(filename);
    //System.out.println("File In Classpath="+retVal);
    if (retVal == null || retVal.equals(""))
    {
      retVal = fallBack;
    }
    if (log_ != null)
    {
      log_.endMethod();
    }
    //System.out.println("File In Classpath="+retVal);
    return retVal;
  }


  /**
   *  Searches the classpath for the specified filename and then returns the
   *  full path that is used for it.
   *
   * @param  filename  the filename to look for
   * @return           the path description of the passed filename as found in
   *      the classpath.
   */
  public static String getFilePathFromClasspath(String filename)
  {
    initUtil();
    final String methodName = CLASSNAME + ": getFilePathFromClasspath";
    if (log_ != null)
    {
      log_.startMethod(methodName);
    }
    String retVal = "";
    String pathSep = System.getProperty("path.separator");
    if (CLASSPATH == null)
    {
      return "";
    }
    int fileIndex = CLASSPATH.indexOf(filename);
    if (log_ != null)
    {
      log_.debug("Searching the Classpath for " + filename +
          " " + fileIndex);
    }
    if (fileIndex >= 0)
    {
      int startSpot = 0;
      int nextSpot = CLASSPATH.indexOf(pathSep);
      // the fileName is in the classpath
      if (log_ != null)
      {
        log_.debug("Start Looking at (" + startSpot + "," + nextSpot + ")");
      }
      while ((nextSpot < fileIndex) && nextSpot != -1)
      {
        startSpot = nextSpot;
        nextSpot = CLASSPATH.indexOf(pathSep, startSpot + 1);
        if (log_ != null)
        {
          log_.debug(", (" + startSpot + "," + nextSpot + ")");
        }
      }
      if (startSpot <= 0)
      {
        startSpot = -1;
      }
      if (nextSpot <= 0)
      {
        nextSpot = CLASSPATH.length();
      }
      retVal = CLASSPATH.substring(startSpot + 1, nextSpot);
    }
    else
        if (log_ != null)
    {
      log_.minor(filename + " cannot be found in classpath.");
    }
    if (log_ != null)
    {
      log_.endMethod();
    }
    return retVal;
  }


  /**
   *  This method seeks out and returns the Parent Frame for a given component.
   *
   * @param  c  The Component to be used as the basis of the frame search
   * @return    Frame The Frame that eventually holds the giuven Component.
   */
  public static Frame getAncestorFrame(Component c) {
    Frame retVal = null;
    while ((c = c.getParent()) != null)
      if (c instanceof Frame) {
        retVal = (Frame) c;
      }
    return retVal;
  }


  /**
   *  Calls the User Interface Manager and sets the look and feel setting based
   *  on the parameter it is passed.
   *
   * @param  i   The new TheLookAndFeel value
   */
  public static void setTheLookAndFeel(short i, Component comp) {
    /* Force SwingSet to come up in the Cross Platform L&F */
    try {
      switch ((int)i){
        case METAL_LOOKANDFEEL:
          // If you want the Java L&F
          UIManager.setLookAndFeel(metalClassName);
          log_.debug("Setting the Java L&F");
          break;
        case WINDOZE_LOOKANDFEEL:
          // If you want the System L&F
          UIManager.setLookAndFeel(windowsClassName);//UIManager.getSystemLookAndFeelClassName());
          log_.debug("Setting the System L&F");
          break;
        case MOTIF_LOOKANDFEEL:
          // If you want the Motif L&F
          UIManager.setLookAndFeel(motifClassName);
          log_.debug("Setting the Motif L&F");
          break;
        case MAC_LOOKANDFEEL:
          // If you want the Mac L&F
          UIManager.setLookAndFeel(macClassName);
          log_.debug("Setting the Mac L&F");
          break;
        default:
          // defaults to Java L&F
          UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
          log_.debug("Setting the Java L&F");
      }
      SwingUtilities.updateComponentTreeUI(Util.getAncestorFrame(comp));
    }
    catch (Exception exc)
    {
      System.err.println("Error loading L&F: " + exc);
    }
  }


  /**
   *  Loads the requested Image filename from the specified jarfile.
   *
   * @param  imageFilename  the filename in the jarfile to use as the basis for
   *      the Image to retrieve.
   * @param  jarFilename    the jar file to look through for the image.
   * @return                Image the Image loaded from the filename spec'd as
   *      an imput param, It returns null if it cannot retrieve the Image.
   */
  public static Image loadImage(String imageFilename, String jarFilename)
  {
    initUtil();
    final String methodName = CLASSNAME + ": loadImage(String, String)";
    if (log_ != null)
    {
      log_.startMethod(methodName);
    }
    if (log_ != null)
    {
      log_.debug("Loading Imagename " + imageFilename);
    }
    Image retVal = (new ImageIcon(Util.class.getResource("/" + imageFilename))).
        getImage();
    /*
     *  String iconJarFileName = jarFilename; //getFilePathFromClasspath(jarFilename);
     *  if(log_!=null) log_.debug("Loading Imagename "+imageFilename+" from Jarfile "+jarFilename);
     *  InputStream in = null;
     *  JarEntry tempJarEntry = null;
     *  byte [] imageBytes;
     *  long imageFileSize = 0l;
     *  int bytesRead = 0;
     *  try
     *  {
     *  JarFile jar = new JarFile(iconJarFileName, false);
     *  tempJarEntry = jar.getJarEntry(imageFilename);
     *  in = jar.getInputStream(tempJarEntry);
     *  imageFileSize = tempJarEntry.getSize();
     *  imageBytes = new byte[(int)imageFileSize];
     *  for (int j=0;j<imageFileSize;j++)
     *  imageBytes[j] = new Integer(in.read()).byteValue();
     *  if(log_!=null) log_.debug("Loaded "+imageFileSize+" Bytes.");
     *  retVal = (Toolkit.getDefaultToolkit()).createImage(imageBytes);
     *  }
     *  catch (NullPointerException fEx)
     *  {
     *  System.out.println("No graphicJarEntry NullPointerException " +
     *  imageFilename);
     *  }
     *  catch (FileNotFoundException fEx)
     *  {
     *  System.out.println("graphic FileNotFoundException " +imageFilename);
     *  }
     *  catch (IOException ioEx)
     *  {
     *  System.out.println("graphic FileNotFoundException " +imageFilename);
     *  }
     *  catch (SecurityException secEx)
     *  {
     *  System.out.println("graphic SecurityException " +imageFilename);
     *  }
     */
    if (log_ != null)
    {
      log_.endMethod();
    }
    return retVal;
  }


  /**
   *  Loads the requested Image filename from the classpath.
   *
   * @param  imageFilename  the filename in the jarfile to use as the basis for
   *      the Image to retrieve.
   * @return                Image the Image loaded from the filename spec'd as
   *      an imput param, It returns null if it cannot retrieve the Image.
   */
  public static Image loadImage(String imageFilename)
  {
    initUtil();
    final String methodName = CLASSNAME + ": loadImage()";
    if (log_ != null)
    {
      log_.startMethod(methodName);
    }
    if (log_ != null)
    {
      log_.debug("Loading Imagename " + imageFilename);
    }
    //System.out.println("Loading Imagename "+imageFilename);
    URL resourceUrl = Util.class.getResource(imageFilename);
    Image retVal = null;
    if (resourceUrl != null)
    {
      retVal = (new ImageIcon(resourceUrl)).getImage();
    }
    else
        if (log_ != null)
    {
      log_.minor("DID NOT Got the Resource URL for :" + imageFilename);
    }

    if (retVal == null)
    {
      if (log_ != null)
      {
        log_.minor("Could NOT create Image for " + imageFilename);
      }
    }
    if (log_ != null)
    {
      log_.endMethod();
    }
    return retVal;
  }


  /**
   *  A method to simply abstract the Try/Catch required to put the current
   *  thread to sleep for the specified time in ms.
   *
   * @param  waitTime  the sleep time in milli seconds (ms).
   * @return           boolean value specifying if the sleep completed (true) or
   *      was interupted (false).
   */
  public static boolean sleep(long waitTime)
  {
    final String methodName = CLASSNAME + ": sleep(Long)";
    if (log_ != null)
    {
      log_.startMethod(methodName);
    }
    boolean retVal = true;
    /*
     *  BLOCK for the spec'd time
     */
    try
    {
      Thread.sleep(waitTime);
    }
    catch (InterruptedException iex)
    {
      retVal = false;
    }
    if (log_ != null)
    {
      log_.endMethod();
    }
    return retVal;
  }


  /**
   *  Encapsulates the showing of the FileChooser dialog and returns a String
   *  representing the absolute path. <P>
   *
   *  NO file filters will be used and NO directory selection is allowed.
   *
   * @param  parent     the owner of this dialog (generally just send this)
   * @param  startPath  the dir to start the file chooser dialog from
   * @return            the absolute path of the chosen file.
   */
  public static String chooseAFilename(Component parent, String startPath)
  {
    initUtil();
    final String methodName = CLASSNAME + ": chooseAFilename(String)";
    if (log_ != null)
    {
      log_.startMethod(methodName);
    }
    if (log_ != null)
    {
      log_.endMethod();
    }
    return chooseAFilename(parent, startPath, null, false);
  }


  /**
   *  Encapsulates the showing of the FileChooser dialog and returns a String
   *  representing the absolute path.<P>
   *
   *  This method requires all the config parms specified by the
   *  ExampleFileFilter.
   *
   * @param  parent        the owner of this dialog (generally just send this)
   * @param  startPath     the dir to start the file chooser dialog from
   * @param  filters       an array holding the Filters to use in the dialog
   * @param  filesAndDirs  specs if the user will be allowed to choose both
   *      files or dirs.
   * @return               the absolute path of the chosen file.
   */
  public static String chooseAFilename(Component parent,
      String startPath,
      ExampleFileFilter[] filters,
      boolean filesAndDirs
      )
  {
    return chooseAFilename(parent, startPath, filters,
        filesAndDirs, Util.OPEN_DIALOG);
  }


  /**
   *  Encapsulates the showing of the FileChooser dialog and returns a String
   *  representing the absolute path.<P>
   *
   *  This method requires all the config parms specified by the
   *  ExampleFileFilter.
   *
   * @param  parent        the owner of this dialog (generally just send this)
   * @param  startPath     the dir to start the file chooser dialog from
   * @param  filters       an array holding the Filters to use in the dialog
   * @param  filesAndDirs  specs if the user will be allowed to choose both
   *      files or dirs.
   * @param  openOrClose   Description of the Parameter
   * @return               the absolute path of the chosen file.
   */
  public static String chooseAFilename(Component parent,
      String startPath,
      ExampleFileFilter[] filters,
      boolean filesAndDirs,
      short openOrClose)
  {
    initUtil();
    final String methodName = CLASSNAME +
        ": chooseAFilename(String,ExampleFileFilter[],boolean)";
    if (log_ != null)
    {
      log_.startMethod(methodName);
    }
    String retVal = "";
    JFileChooser chooser = new JFileChooser();
    if (filters != null && filters.length > 0)
    {
      for (int i = 0; i < filters.length; i++)
      {
        chooser.addChoosableFileFilter(filters[i]);
      }
      chooser.setFileFilter(filters[0]);
    }
    if (filesAndDirs)
    {
      chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    }
    if (openOrClose == SAVE_DIALOG)
    {
      if (chooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION)
      {
        retVal = chooser.getSelectedFile().getAbsolutePath();
        System.out.println("You chose this file: " + retVal);
      }
    }
    else
    {
      if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION)
      {
        retVal = chooser.getSelectedFile().getAbsolutePath();
        System.out.println("You chose this file: " + retVal);
      }
    }
    if (log_ != null)
    {
      log_.endMethod();
    }
    return retVal;
  }


  /**
   *  Converts an Absolute path to a relative path from the VM Working Dir.
   *
   * @param  absPath  is the File to use to to convert
   * @return null if can't be done, otherwise it returns the relative path.
   */
  public static String absoluteToPwdRelativePath(File absPath)
  {
    String retVal = null;
    if (absPath !=null)
      retVal = absoluteToPwdRelativePath(absPath.getAbsolutePath());

     return retVal;
  }


  /**
   *  Converts an Absolute path to a relative path from the VM Working Dir.
   *
   * @param  absPath  is the absolute path  to convert
   * @return null if can't be done, otherwise it returns the relative path.
   */
  public static String absoluteToPwdRelativePath(String absPath)
  {
    initUtil();
    final String methodName = CLASSNAME + ": getFileDir(String)";
    if (log_ != null)
    {
      log_.startMethod(methodName);
    }
    String retVal = null;

    String pwd = System.getProperty("user.dir");
    if (absPath != null && absPath.indexOf(pwd) != -1)
    {
      // Found It
      retVal = absPath.substring(pwd.length()+1);
    }

    if (log_ != null)
    {
      log_.endMethod();
    }
    return retVal;
  }


  /**
   *  Removes a '/..' from the filename string and returns the corrected dir
   *  name reference. For Example :   pass it "/a/b/c/../d" it returns "/a/b/d".
   *
   * @param  path  is the absolute path  to convert
   * @return null if can't be done, otherwise it returns the corrected path.
   */
  public static String removeParentRelativeReference(String path)
  {
    initUtil();
    final String methodName = CLASSNAME + ": removeParentRelativeReference(String)";
    if (log_ != null)
    {
      log_.startMethod(methodName);
    }
    String retVal = path;
    String parentRefString = SYSTEM_FILE_SEPERATOR+"..";
    int spot = -1;
    int removeSpot = -1;

    //System.out.println("Path = "+path);
    //System.out.println("parentRef = "+parentRefString);
    if (path != null)
    {
      spot = retVal.indexOf(parentRefString);
      while (spot != -1)
      {
        // Found It
        removeSpot = retVal.substring(0,spot-1).lastIndexOf(SYSTEM_FILE_SEPERATOR);
        retVal = retVal.substring(0,removeSpot)+retVal.substring(spot+3);

        //System.out.println("spot = "+spot);
        //System.out.println("removeSpot = "+removeSpot);
        //System.out.println("retVal = "+retVal);

        spot = retVal.indexOf(parentRefString);
      }
    }

    if (log_ != null)
    {
      log_.endMethod();
    }
    return retVal;
  }


  /**
   *  Creates a File representing the Dir for the specified filename string.
   *
   * @param  fileString  is the filename to get the dir for
   */
  public static File getFileDir(String fileString)
  {
    initUtil();
    final String methodName = CLASSNAME + ": getFileDir(String)";
    if (log_ != null)
    {
      log_.startMethod(methodName);
    }
    File retVal = null;
    if (fileString != null &&
        !fileString.equals(""))
    {
      int endSpot = fileString.lastIndexOf(File.separator);
      if(endSpot >=0)
      {
        retVal = new File(fileString.substring(0,endSpot));
      }
      else
      {
        retVal = new File(".");
      }
    }

    if (log_ != null)
    {
      log_.endMethod();
    }
    return retVal;
  }


  /**
   *  Creates a URL for the specified filename string.
   *
   * @param  fileString  is the filename to turn into a file://URL
   * @return             the converted file://URL
   */
  public static URL getFileBaseURL(String fileString)
  {
    initUtil();
    final String methodName = CLASSNAME + ": getFileBaseURL(String)";
    if (log_ != null)
    {
      log_.startMethod(methodName);
    }
    /*
     *  because this is an application the codebase does not work
     */
    /*
     *  we have to create the codeBase URL from the userDir
     */
    URL convertedURL = null;
    int i = 0;
    String tempPath = null;
    try
    {
      // 1st see if the string is already a URL
      convertedURL = new URL(fileString);
    }
    catch (MalformedURLException ex)
    {
      try
      {
        try
        {
          /*
           *  1st test if this is a DOS/OS/2 style path with a drive letter
           */
          if (fileString.charAt(1) == ':')
          {
            tempPath = fileString.substring(0, 1);
            tempPath += ":";
            /*
             *  Replace the file separators with the forward slash
             */
            for (i = 2; i <= fileString.length() - 1; i++)
            {
              if (fileString.charAt(i) != SYSTEM_FILE_SEPERATOR.charAt(0))
              {
                tempPath += fileString.substring(i, i + 1);
              }
              else
              {
                tempPath += "/";
              }
            }
            //System.out.print("Appears to be a DOS or OS/2 filesystem - ");
            convertedURL = new URL("FILE:////" + tempPath);
            //System.out.println(""+ convertedURL);
          }
          else
          {
            /*
             *  2nd test if this is a UN*x style path without a drive letter
             */
            if (fileString.charAt(0) == '/')
            {
              //System.out.println("Appears to be a Unix filesystem");
              //convertedURL = new URL("file://" + fileString);
              convertedURL = new URL("file","", fileString);
            }
            else
            {
              //System.out.println("Appears to be a relative filesystem");
              convertedURL = new URL("file://./");
              convertedURL = new URL("file","", fileString);
            }
          }
          //  System.out.println("APP convertedURL=" + convertedURL.toString());
        }
        catch (java.net.MalformedURLException t)
        {
          System.out.println("caught java.net.MalformedURLException: " + t +
              "Cannot retrieve User Directory");
          t.printStackTrace();
        }
      }
      catch (StringIndexOutOfBoundsException t)
      {
        System.out.println("caught StringIndexOutOfBoundsException: " + t +
            "Cannot retrieve User Directory");
        t.printStackTrace();
      }
    }
    if (log_ != null)
    {
      log_.endMethod();
    }
    return convertedURL;
  }


  /**
   *  Creates a URL[] for the given Strings.
   *
   * @param  fileStrings  Description of Parameter
   * @return              The FileBaseURLs value
   */
  public static URL[] getFileBaseURLs(String[] fileStrings)
  {
    final String methodName = CLASSNAME + ": getFileBaseURLs(String[])";
    initUtil();
    if (log_ != null)
    {
      log_.startMethod(methodName);
    }
    URL[] retVal = null;
    if (fileStrings != null)
    {
      int size = fileStrings.length;
      //      System.out.println("Into getFileBaseURLs " + size);
      retVal = new URL[size];
      for (int i = 0; i < size; i++)
      {
        retVal[i] = getFileBaseURL(fileStrings[i]);
      }
    }
    if (log_ != null)
    {
      log_.endMethod();
    }

    return retVal;
  }


  /**
   *  gets the current VMs localhost address.
   *
   * @return    the current VMs localhost address, Null if it can't be gotten.
   */
  public static InetAddress getLocalhostName()
  {
    initUtil();
    final String methodName = CLASSNAME + ": getLocalhostrName()";
    if (log_ != null)
    {
      log_.startMethod(methodName);
    }
    InetAddress retVal = null;
    try
    {
      retVal = InetAddress.getLocalHost();
    }
    catch (UnknownHostException unknownEx)
    {
      log_.minor("Cant't get the hostname address.", unknownEx);
    }
    if (log_ != null)
    {
      log_.endMethod();
    }
    return retVal;
  }


  /**
   * Executes the Specified Native OS application with the provided commandline
   * parameters. This method blocks until the Executed app is complete. It also
   * cleans up its sub-process and garbage collects.
   *
   * @param executableLocation is the path to the executable to run
   * @param cmds and extra commandline parameters to tag onto the
   *               end of the commandline that gets executed.
   * @param verboseOutput flag for optionaly printing extra application state output.
   *
   * @return returns the captured output from the executed app/process
   *
   * @see StreamGobbler
   * @see java.lang.Runtime#getRuntime
   **/
  public static String executeNativeApp(String executableLocation,
                                          String[] cmds,
                                          boolean verboseOutput)
  {
    final String methodName = CLASSNAME + ": executeNativeApp(String,String[],boolean)";
    initUtil();
    if (log_ != null)
    {
      log_.startMethod(methodName);
    }
    String retVal = "";

    StreamGobbler inputGobbler = new StreamGobbler();
    inputGobbler.setCapture(true);
    executeNativeApp(executableLocation,cmds,null,inputGobbler,verboseOutput);
    log_.debug("App Execution Complete");
    retVal = inputGobbler.getCapturedOutput();

    if (log_ != null)
    {
      log_.endMethod();
    }
    return retVal;
  }


  /**
   * Executes the Specified Native OS application with the provided commandline
   * parameters. This method blocks until the Executed app is complete. It also
   * cleans up its sub-process and garbage collects.
   *
   * @param executableLocation is the path to the executable to run
   * @param cmds and extra commandline parameters to tag onto the
   *               end of the commandline that gets executed.
   * @param outFilename an outputFile to dump the exe output (If this is null
   *                    it sends the output to System.out).
   * @param verboseOutput flag for optionaly printing extra applicvation state output.
   *
   * @return returns the return code from the executed app/process
   *
   * @see StreamGobbler
   * @see java.lang.Runtime#getRuntime
   **/
  public static int executeNativeApp(String executableLocation,
                      String[] cmds,
                      String outFilename,
                      boolean verboseOutput)
  {
    return executeNativeApp(executableLocation,
                             cmds,
                             outFilename,
                             null,
                             verboseOutput);
  }


  /**
   * Executes the Specified Native OS application with the provided commandline
   * parameters. This method blocks until the Executed app is complete. It also
   * cleans up its sub-process and garbage collects.
   *
   * @param executableLocation is the path to the executable to run
   * @param appParms and extra commandline parameters to tag onto the
   *               end of the commandline that gets executed.
   * @param outFilename an outputFile to dump the exe output (If this is null
   *                    it sends the output to System.out).
   * @param inputGobbler is the StreamGobbler object to use in gobbling the Commands InputStream.
   * @param verboseOutput flag for optionaly printing extra applicvation state output.
   *
   * @return returns the return code from the executed app/process
   *
   * @see StreamGobbler
   * @see java.lang.Runtime#getRuntime
   **/
  public static int executeNativeApp(String executableLocation,
                      Vector appParms,
                      String outFilename,
                      StreamGobbler inputGobbler,
                      boolean verboseOutput)
  {
    int retVal = -1;
    int cmdParms = 0;
    if (appParms != null)
      cmdParms = appParms.size();
    String [] cmds = new String [cmdParms];
    //cmds[0] = executableLocation;

    if (verboseOutput) System.out.print("Executing: "+ executableLocation);
    for (int i=0; i < cmdParms; i++)
    {
      if (verboseOutput) System.out.print(" " + i + ")");
      if (verboseOutput) System.out.print(" " + (String)appParms.elementAt(i));
      cmds[i] = (String) appParms.elementAt(i);
      if (verboseOutput) System.out.print(" " + cmds[i]);
    }
    if (verboseOutput) System.out.println("");

    return executeNativeApp(executableLocation,
                            cmds,
                            outFilename,
                            inputGobbler,
                            verboseOutput);
  }


  /**
   * Executes the Specified Native OS application with the provided commandline
   * parameters. This method blocks until the Executed app is complete. It also
   * cleans up its sub-process and garbage collects.
   *
   * @param executableLocation is the path to the executable to run
   * @param cmds and extra commandline parameters to tag onto the
   *               end of the commandline that gets executed.
   * @param outFilename an outputFile to dump the exe output (If this is null
   *                    it sends the output to System.out).
   * @param inputGobbler is the StreamGobbler object to use in gobbling the Commands InputStream.
   * @param verboseOutput flag for optionaly printing extra applicvation state output.
   *
   * @return returns the return code from the executed app/process
   *
   * @see StreamGobbler
   * @see java.lang.Runtime#getRuntime
   **/
  public static int executeNativeApp(String executableLocation,
                      String[] cmds,
                      String outFilename,
                      StreamGobbler inputGobbler,
                      boolean verboseOutput)
  {
    int retVal = -1;

    // Start the app... This starts a new process
    try
    {
      // set up the arrays to send to exec.
      //String[] prefix = new String[]{};
      String[] prefix = new String[]{executableLocation};
      //String[] prefix = new String[]{"cmd.exe", "/U", "/E:OFF", "/C"};
      //String[] prefix = new String[]{"start", "/I", "/B"};
      String[] actualArgs = new String[prefix.length + cmds.length];
      System.arraycopy(prefix, 0, actualArgs, 0, prefix.length);
      System.arraycopy(cmds, 0, actualArgs, prefix.length, cmds.length);

      // collect and init the environment
      ProcessRunner osSupport = ProcessRunner.getProcessRunner();
      Hashtable variables = osSupport.getEnvironmentVariables();
      String [] env = new String[variables.size()];
      if(osSupport.supportsEnvironmentVariables())
      {
        int counter = 0;
        Enumeration keys = variables.keys();
        while(keys.hasMoreElements())
        {
          Object key = keys.nextElement();
          env[counter++]= (key + "=" + variables.get(key));
        }
      }

      //for (int i=0; i < env.length; i++)
      //  System.out.println(env[i]);
      //System.out.println("");
      if (verboseOutput)
      {
        for (int i=0; i < actualArgs.length; i++)
          System.out.print(actualArgs[i] + " ");
        System.out.println("");
      }

      // get set up to get any output?
      FileOutputStream pumpedOutputFile = null;
      PrintStream pumpedPrintStream = System.out;
      if (outFilename != null && !outFilename.equals(""))
      {
        pumpedOutputFile = new FileOutputStream(outFilename);
        pumpedPrintStream = new PrintStream(pumpedOutputFile);
      }
      //set up the Gobbler
      if (inputGobbler == null) inputGobbler = new StreamGobbler();
      inputGobbler.setPrependString("");
      inputGobbler.setOs_(pumpedPrintStream);
      StreamGobbler errorGobbler = new StreamGobbler();
      errorGobbler.setPrependString("Error> ");

      // get the system call process set up and running
      Runtime runtime = Runtime.getRuntime();
      //Process p = runtime.exec(actualArgs, env, new File(".\\."));
      /*System.out.println("***Calling runtimeexec "+actualArgs[0]+","+
                          (actualArgs.length>1?actualArgs[1]+",":"")+
                          (actualArgs.length>2?actualArgs[2]+",":"")+
                          (actualArgs.length>3?actualArgs[3]+",":""));*/
      Process p = runtime.exec(actualArgs, null);

      // capture any input we get
      // any error message?
      //StreamGobbler outputGobbler = new StreamGobbler(p.getOutputStream(), "Out> ");

      // capture any output we get
      // any error message?
      inputGobbler.setInputStream(p.getInputStream());
      errorGobbler.setInputStream(p.getErrorStream());

      // kick them off
      inputGobbler.start();
      errorGobbler.start();
      //outputGobbler.start();

      // now wait for the process to end
      try
      {
        //sleep(20000);
        p.waitFor();
        if (verboseOutput) System.out.println("Process Done Executing.");
        //if (verboseOutput) System.out.println(outputGobbler.getCapturedOutput());
      }
      catch (Exception intEx)
      {
        if (verboseOutput) System.out.print("APP Interupted?");
      }
      retVal = p.exitValue();
      if (verboseOutput) System.out.println("APP return value:"+retVal);
      errorGobbler.finishedGobbling_ = true;
      //outputGobbler.finishedGobbling_ = true;
      inputGobbler.finishedGobbling_ = true;
      if (pumpedOutputFile != null)
      {
        pumpedOutputFile.flush();
        pumpedOutputFile.close();
      }
      p.destroy();
      p = null;
      runtime.gc();
    }
    catch (IOException ioEx)
    {
      System.out.println("ERROR: An IO exception occured while attempting " +
        "execution of the " + executableLocation + " application.");
        ioEx.printStackTrace();
    }
    catch (SecurityException securityEx)
    {
      System.out.println("ERROR: A Java Security Manager is in use and is " +
        "restricting execution of the " + executableLocation + " application.");
    }
    catch (Exception ex)
    {
      System.out.println("ERROR: A Java Exception Occured " +
        "restricting execution of the " + executableLocation + " application.");
      ex.printStackTrace();
    }

    return retVal;
  }


  /**
   * A Thread Watchdog that watches the passed in thread and Interupts it if it
   * has not finished by the requested time.
   *
   * @param watchThread is the thread to watch
   * @param timeToTerminate is the time to wait befor Interupting
   * @exception SecurityException If the interrupt is not allowed on the thread
   *
   * @return true or false to specify if this watchdog had to interupt.
   **/
  public static boolean threadWatchdog(final Thread watchThread,
                                          final long timeToTerminate)
                                          throws SecurityException
  {
    boolean retVal = false;

    // inline Thread to do the watching
    Thread watchdogThread = new Thread (){
      Thread threadToWatch = null;
      long timeWatched = 0;
      int sleepTime = 500;

      public void run()
      {
        watchdogReset = false;
        while (timeWatched < timeToTerminate &&
                watchThread.isAlive() &&
                !watchThread.isInterrupted() &&
                !watchdogReset)
        {
          timeWatched += sleepTime;
          try
          {
            sleep(sleepTime);
          }
          catch (InterruptedException iex)
          {
          }
        }

        if (timeWatched >= timeToTerminate)
        {
          // this throws a security exception If the interrupt is not allowed
          watchThread.interrupt();
        }
      }
    };

    watchdogThread.start();

   return retVal;
  }


  /**
   * Executes the Specified Native OS application with the provided commandline
   * parameters. This method blocks until the Executed app is complete. It also
   * cleans up its sub-process and garbage collects. This method defaults to
   * print System.out statements to describe the state of execution.  If you
   * don't want anything but the executed application output call the
   * overridden method with the verbose flag.
   *
   * @param executableLocation is the path to the executable to run
   *
   * @param appParms and extra commandline parameters to tag onto the
   *               end of the commandline that gets executed.
   *
   * @param outFilename an outputFile to dump the exe output.
   *
   * @return returns the return code from the executed app/process
   *
   * @see StreamGobbler
   * @see java.lang.Runtime#getRuntime
   **/
  public static int executeNativeApp(String executableLocation,
                      Vector appParms,
                      String outFilename)
  {
    return executeNativeApp(executableLocation, appParms, outFilename, true);
  }


  /**
   * Executes the Specified Native OS application with the provided commandline
   * parameters. This method blocks until the Executed app is complete. It also
   * cleans up its sub-process and garbage collects.
   *
   * @param executableLocation is the path to the executable to run
   * @param appParms and extra commandline parameters to tag onto the
   *               end of the commandline that gets executed.
   * @param outFilename an outputFile to dump the exe output (This can be null)
   * @param verboseOutput flag for optionaly printing extra applicvation state output.
   *
   * @return returns the return code from the executed app/process
   *
   * @see StreamGobbler
   * @see java.lang.Runtime#getRuntime
   **/
  public static int executeNativeApp(String executableLocation,
                      Vector appParms,
                      String outFilename,
                      boolean verboseOutput)
  {
    int retVal = -1;
    int cmdParms = 0;
    if (appParms != null)
      cmdParms = appParms.size();
    String [] cmds = new String [cmdParms];
    //cmds[0] = executableLocation;

    if (verboseOutput) System.out.print("Executing: "+ executableLocation);
    for (int i=0; i < cmdParms; i++)
    {
      if (verboseOutput) System.out.print(" " + i + ")");
      if (verboseOutput) System.out.print(" " + (String)appParms.elementAt(i));
      cmds[i] = (String) appParms.elementAt(i);
      if (verboseOutput) System.out.print(" " + cmds[i]);
    }
    if (verboseOutput) System.out.println("");

    return executeNativeApp(executableLocation,
                            cmds,
                            outFilename,
                            verboseOutput);
  }


  /**
   * Executes the Specified Native OS application with the provided commandline
   * parameters. This method blocks until the Executed app is complete. It also
   * cleans up its sub-process and garbage collects.
   *
   * @param executableLocation is the path to the executable to run
   * @param appParms and extra commandline parameters to tag onto the
   *               end of the commandline that gets executed.
   * @param verboseOutput flag for optionaly printing extra applicvation state output.
   *
   * @return returns the captured results stream from the executed app/process
   *
   * @see StreamGobbler
   * @see java.lang.Runtime#getRuntime
   **/
  public static String executeNativeApp(String executableLocation,
                      Vector appParms,
                      boolean verboseOutput)
  {
    int cmdParms = 0;
    if (appParms != null)
      cmdParms = appParms.size();
    String [] cmds = new String [cmdParms];
    //cmds[0] = executableLocation;

    if (verboseOutput) System.out.print("Executing: "+ executableLocation);
    for (int i=0; i < cmdParms; i++)
    {
      if (verboseOutput) System.out.print(" " + i + ")");
      if (verboseOutput) System.out.print(" " + (String)appParms.elementAt(i));
      cmds[i] = (String) appParms.elementAt(i);
      if (verboseOutput) System.out.print(" " + cmds[i]);
    }
    if (verboseOutput) System.out.println("");

    return executeNativeApp(executableLocation,
                            cmds,
                            verboseOutput);
  }


    /**
   *  gets the current date and time.
   *
   * @return    the current Date.
   */
  public static Date getCurrentDate()
  {
    initUtil();
    final String methodName = CLASSNAME + ": getCurrentDate()";
    if (log_ != null)
    {
      log_.startMethod(methodName);
    }
    Date retVal = new Date();
    if (log_ != null)
    {
      log_.endMethod();
    }
    return retVal;
  }


  /**
   *  Counts the files in a dir (optionally recurses sub dirs)
   *
   * @param  dirName
   * @param  recurse
   * @return          the number of files (not counting the directories)
   */
  public static long countFilesInDir(String dirName,
      boolean recurse)
  {
    initUtil();
    final String methodName = CLASSNAME + ": countFilesInDir()";
    if (log_ != null)
    {
      log_.startMethod(methodName);
    }
    long retVal = 0L;
    File dirFile = new File(dirName);

    if (dirFile.isDirectory() && dirFile.canRead())
    {
      String[] waste = dirFile.list();
      File tempFile = null;
      for (int i = 0; i < waste.length; i++)
      {
        tempFile = new File(dirName + File.separator + waste[i]);
        if (tempFile != null && !tempFile.isDirectory())
        {
          retVal++;
        }
        else if (tempFile != null && recurse)
        {
          retVal +=
              countFilesInDir(dirName + File.separator + waste[i], true);
        }
      }
    }

    if (log_ != null)
    {
      log_.endMethod();
    }
    return retVal;
  }

  public static void main(String [] args)
  {
    final String methodName = CLASSNAME + ": main()";
    initUtil();
    if (log_ != null)
    {
      log_.setLogLevel(Log.MAJOR);
      log_.startMethod(methodName);
    }
    log_.debug("args: ");
    for (int i=1; i < args.length; i++) log_.debug(""+i+" "+args[i]);

    StringBuffer helpMsg = new StringBuffer(SYSTEM_LINE_SEPERATOR);
    helpMsg.append("---  WebARTS Util Class  --------------------------------");
    helpMsg.append("----------------------");
    helpMsg.append(SYSTEM_LINE_SEPERATOR);
    helpMsg.append("WebARTS Util Class");
    helpMsg.append(SYSTEM_LINE_SEPERATOR);
    helpMsg.append("SYNTAX:");
    helpMsg.append(SYSTEM_LINE_SEPERATOR);
    helpMsg.append("   java ca.bc.webarts.widgets.Util methodName methodArgs");
    helpMsg.append(SYSTEM_LINE_SEPERATOR);
    helpMsg.append(SYSTEM_LINE_SEPERATOR);
    helpMsg.append("Available Methods:");
    helpMsg.append(SYSTEM_LINE_SEPERATOR);
    helpMsg.append("-->   removeDir fullDirectoryName fullDirectoryName2 ...");
    helpMsg.append(SYSTEM_LINE_SEPERATOR);
    helpMsg.append("-->   createCurrentTimeStamp");
    helpMsg.append(SYSTEM_LINE_SEPERATOR);
    helpMsg.append("-->   createCurrentDateTimeStamp");
    helpMsg.append(SYSTEM_LINE_SEPERATOR);
    helpMsg.append("-->   capsToSpacesInString StringToConvert");
    helpMsg.append(SYSTEM_LINE_SEPERATOR);
    helpMsg.append("-->   spacesToCapsInString  StringToConvert");
    helpMsg.append(SYSTEM_LINE_SEPERATOR);
    helpMsg.append("-->   spacesToCapsInDir  directoryName");
    helpMsg.append(SYSTEM_LINE_SEPERATOR);
    helpMsg.append("-->   removeParentRelativeReference  pathName");
    helpMsg.append(SYSTEM_LINE_SEPERATOR);
    helpMsg.append("-->   countFilesInDir  fullDirectoryName");
    helpMsg.append(SYSTEM_LINE_SEPERATOR);
    helpMsg.append("-->   dirSize  fullDirectoryName");
    helpMsg.append(SYSTEM_LINE_SEPERATOR);
    helpMsg.append("-->   removeOldFiles fullDirectoryName daysOld");
    helpMsg.append(SYSTEM_LINE_SEPERATOR);
    helpMsg.append("-->   sleep ms");
    helpMsg.append(SYSTEM_LINE_SEPERATOR);
    helpMsg.append("-->   executeNativeApp executableLocation [parms] ");
    helpMsg.append(SYSTEM_LINE_SEPERATOR);
    helpMsg.append("-->   findFile fileToFind [dirToStart] [recurseSubDirs] [regExp] ");
    helpMsg.append(SYSTEM_LINE_SEPERATOR);
    helpMsg.append("-->   searchInFile file/dirToSearch searchStr");
    helpMsg.append(SYSTEM_LINE_SEPERATOR);
    helpMsg.append("-->   tokenReplaceInFile file/dirToSearch tokenStr replaceStr ");
    helpMsg.append(SYSTEM_LINE_SEPERATOR);
    helpMsg.append("---------------------------------------------------------");
    helpMsg.append("----------------------");
    helpMsg.append(SYSTEM_LINE_SEPERATOR);

    if (args ==null || args.length<1)
      System.out.println(helpMsg.toString());
    else
    {
      if (args[0].equals("removeDir"))
      {
        if (args.length >1)
        {
          File tmpDirFile = null;
          for (int i=1; i < args.length; i++)
          {
            if ((tmpDirFile = initDirFile(args[i])) != null)
            {
              if (removeDir(args[i]))
                System.out.println("Directory Removed: "+args[i]);
              else
                System.out.println("Directory NOT Removed successfully: "+args[i]);
            }
            else
            {
              helpMsg.append("ERROR: Cannot find the Directory named:");
              helpMsg.append(SYSTEM_LINE_SEPERATOR);
              helpMsg.append(args[i]);
              System.out.println(helpMsg.toString());
            }
          }
        }
        else
        {
          helpMsg.append("ERROR: The removeDir method requires at least 1");
          helpMsg.append(" directory name to operate.");
          System.out.println(helpMsg.toString());
        }
      }
      else if (args[0].equals("countFilesInDir"))
      {
        if (args.length >1)
        {
          File tmpDirFile = null;
          for (int i=1; i < args.length; i++)
          {
            if ((tmpDirFile = initDirFile(args[i])) != null)
            {
              long retVal = countFilesInDir(args[i], true);
              System.out.println("Number Of Files = " + retVal);
            }
            else
            {
              helpMsg.append("ERROR: Cannot find the Directory named:");
              helpMsg.append(SYSTEM_LINE_SEPERATOR);
              helpMsg.append(args[i]);
              System.out.println(helpMsg.toString());
            }
          }
        }
        else
        {
          helpMsg.append("ERROR: The countFilesInDir method requires 1");
          helpMsg.append(" directory name to operate.");
          System.out.println(helpMsg.toString());
        }
      }
      else if (args[0].equals("countFilesInDir"))
      {
        if (args.length >1)
        {
          File tmpDirFile = null;
          for (int i=1; i < args.length; i++)
          {
            if ((tmpDirFile = initDirFile(args[i])) != null)
            {
              long retVal = countFilesInDir(args[i], true);
              System.out.println("Number Of Files = " + retVal);
            }
            else
            {
              helpMsg.append("ERROR: Cannot find the Directory named:");
              helpMsg.append(SYSTEM_LINE_SEPERATOR);
              helpMsg.append(args[i]);
              System.out.println(helpMsg.toString());
            }
          }
        }
        else
        {
          helpMsg.append("ERROR: The countFilesInDir method requires 1");
          helpMsg.append(" directory name to operate.");
          System.out.println(helpMsg.toString());
        }
      }
      else if (args[0].equals("tokenReplaceInFile"))
      {
        if (args.length == 4)
        {
          File rootFile = new File(args[1]);
          if (rootFile != null && !rootFile.isDirectory() && rootFile.canRead())
          {
            tokenReplaceInFile(args[1], args[2], args[3]);
          }
          else
          {
            helpMsg.append("ERROR: Cannot find the file named:");
            helpMsg.append(SYSTEM_LINE_SEPERATOR);
            helpMsg.append(args[1]);
            System.out.println(helpMsg.toString());
          }
        }
        else
        {
          helpMsg.append("ERROR: The tokenReplaceInFile method requires");
          helpMsg.append(" 3 parameters:\n a file name to operate on,\n");
          helpMsg.append("the the token string to search for in the file, and \n");
          helpMsg.append("the replacement string");
          System.out.println(helpMsg.toString());
        }
      }
      else if (args[0].equals("dirSize"))
      {
        if (args.length >1)
        {
          File tmpDirFile = null;
          for (int i=1; i < args.length; i++)
          {
            if ((tmpDirFile = initDirFile(args[i])) != null)
            {
              long retVal = dirSize(args[i], true);
              DecimalFormat decForm = new DecimalFormat("##0.000E0");
              String formattedRetVal = decForm.format(retVal);
              if ((formattedRetVal.substring(formattedRetVal.length()-1)).
                  equals("9"))
                formattedRetVal =
                  formattedRetVal.substring(0,formattedRetVal.length()-2)+" Gb";
              else if ((formattedRetVal.substring(formattedRetVal.length()-1)).
                  equals("6"))
                formattedRetVal =
                  formattedRetVal.substring(0,formattedRetVal.length()-2)+" Mb";
              else if ((formattedRetVal.substring(formattedRetVal.length()-1)).
                  equals("3"))
                formattedRetVal =
                  formattedRetVal.substring(0,formattedRetVal.length()-2)+" kb";

              //formattedRetVal = decForm.format(retVal);
              System.out.println("Directory size = " + formattedRetVal);

            }
            else
            {
              helpMsg.append("ERROR: Cannot find the Directory named:");
              helpMsg.append(SYSTEM_LINE_SEPERATOR);
              helpMsg.append(args[i]);
              System.out.println(helpMsg.toString());
            }
          }
        }
        else
        {
          helpMsg.append("ERROR: The dirSize method requires 1");
          helpMsg.append(" directory name to operate.");
          System.out.println(helpMsg.toString());
        }
      }
      else if (args[0].equals("capsToSpacesInString"))
      {
        if (args.length >1)
        {
          StringBuffer result = new StringBuffer();
          for (int i=1; i< args.length; i++)
          {
            result.append(capsToSpacesInString(args[i]));
            result.append(" ");
          }
          System.out.println(result.toString().trim());
        }
        else
        {
          helpMsg.append("ERROR: The capsToSpacesInString method requires");
          helpMsg.append(" at least 1 String Argument to operate.");
          System.out.println(helpMsg.toString());
        }
      }
      else if (args[0].equals("spacesToCapsInString"))
      {
        if (args.length >1)
        {
          StringBuffer result = new StringBuffer();
          for (int i=1; i< args.length; i++)
          {
            result.append(args[i]);
            result.append(" ");
          }
          System.out.println(spacesToCapsInString(result.toString().trim()));
        }
        else
        {
          helpMsg.append("ERROR: The spacesToCapsInString method requires");
          helpMsg.append(" at least 1 String Argument to operate.");
          System.out.println(helpMsg.toString());
        }
      }
      else if (args[0].equals("spacesToCapsInDir"))
      {
        if (args.length >1)
        {
          for (int i=1; i< args.length; i++)
          {
            spacesToCapsInDir(args[i].toString().trim());
          }
        }
        else
        {
          helpMsg.append("ERROR: The spacesToCapsInFilenames method requires");
          helpMsg.append(" at least 1 String Argument to specify the dir to parse for filenames.");
          System.out.println(helpMsg.toString());
        }
      }
      else if (args[0].equals("removeParentRelativeReference"))
      {
        if (args.length >1)
        {
          for (int i=1; i< args.length; i++)
          {
            System.out.println(removeParentRelativeReference(args[i].trim()));
          }
        }
        else
        {
          helpMsg.append("ERROR: The removeParentRelativeReference method requires");
          helpMsg.append(" at least 1 path String Argument to operate.");
          System.out.println(helpMsg.toString());
        }
      }
      else if (args[0].equals("sleep"))
      {
        if (args.length >1)
        {
          sleep(Integer.parseInt(args[1]));
          //System.out.println(sleep(Integer.parseInt(args[1])));
        }
        else
        {
          helpMsg.append("ERROR: The sleep method requires");
          helpMsg.append(" the sleep time specified in ms.");
          System.out.println(helpMsg.toString());
        }
      }
      else if (args[0].equals("executeNativeApp"))
      {
        if (args.length >1)
        {
          Vector cmdArgs = new Vector();
          for (int i=2; i< args.length; i++)
          {
            cmdArgs.add(args[i]);
          }
          System.out.println("\nexecuteNativeApp: Output Sent to std out.");
          boolean verboseScreenOutput = true;
          //executeNativeApp(args[1], cmdArgs, "appOut.txt", verboseScreenOutput);
          System.out.println(executeNativeApp(args[1], cmdArgs, verboseScreenOutput));
          //System.out.println(sleep(Integer.parseInt(args[1])));
        }
        else
        {
          helpMsg.append("ERROR: The executeNativeApp method requires");
          helpMsg.append(" at least a name of an executable to run.");
          System.out.println(helpMsg.toString());
        }
      }
      else if (args[0].startsWith("findFile"))
      {
        if (args.length >1 && args.length <6)
        {
          boolean recurseSubdirs = true;
          boolean regExp = false;
          boolean verbose = true;
          String directory = System.getProperty("user.dir");
          String searchForStr = args[1];
          if (args.length < 3)
          {
            recurseSubdirs = true;
            regExp = false;
            verbose = true;
            directory = System.getProperty("user.dir");
            searchForStr = args[1];
          }
          else if (args.length <4)
          {
            recurseSubdirs = true;
            regExp = false;
            verbose = true;
            directory = args[2];
            searchForStr = args[1];
          }
          else if (args.length <5 )
          {
            recurseSubdirs = Boolean.valueOf(args[3]).booleanValue();
            regExp = false;
            verbose = true;
            directory = args[2];
            searchForStr = args[1];
          }
          else if (args.length <6 )
          {
            recurseSubdirs = Boolean.valueOf(args[3]).booleanValue();
            regExp = Boolean.valueOf(args[4]).booleanValue();
            verbose = true;
            directory = args[2];
            searchForStr = args[1];
          }

          StringBuffer summary = new StringBuffer();
          summary.append((recurseSubdirs?"Recursive search for\n  ":"Search for\n  "));
          summary.append((regExp?"regular expression '":""));
          summary.append(searchForStr);
          summary.append((regExp?"'\n":"\n"));
          summary.append("starting in directory:\n  ");
          summary.append(directory);
          //System.out.println(summary.toString());
          findFile(searchForStr, directory, recurseSubdirs, regExp, verbose );
        }
        else
        {
          helpMsg.append("ERROR: The findFiles method requires");
          helpMsg.append(" at least a filename to find from the recursed current dir.\n\n");
          helpMsg.append("       You can optionally specify the following:\n");
          helpMsg.append("       findFile filename [directoryToStart] [recurse] [regExp]\n");
          helpMsg.append("       directoryToStart is the directory to start looking\n");
          helpMsg.append("       recurse (true or false) to specify if the ");
          helpMsg.append("sub-dirs should be recursed\n");
          helpMsg.append("       regExp  (true or false) to specify if the ");
          helpMsg.append("filename parameter should be trerated as a regular expression\n");
          System.out.println(helpMsg.toString());
        }
      }
      else if (args[0].equals("searchInFile"))
      {
        if (args.length > 2)
        {
          boolean recurseSubdirs = false;
          boolean fileRegExp = false;
          boolean verbose = true;
          String fileName = args[1];
          String searchStr = args[2];

          recurseSubdirs = true;
          fileRegExp = false;
          verbose = true;
          fileName = args[1];
          StringBuffer sb = new StringBuffer(args[2]);
          for (int i = 3; i < args.length; i++)
          {
            sb.append(" ");
            sb.append(args[i]);
          }
          searchStr = sb.toString();
          /*
          else if (args.length <5 )
          {
            recurseSubdirs = Boolean.valueOf(args[3]).booleanValue();
            regExp = false;
            verbose = true;
            directory = args[2];
            searchForStr = args[1];
          }
          else if (args.length <6 )
          {
            recurseSubdirs = Boolean.valueOf(args[3]).booleanValue();
            regExp = Boolean.valueOf(args[4]).booleanValue();
            verbose = true;
            directory = args[2];
            searchForStr = args[1];
          }
          */
          File rootFile = new File(fileName);

          StringBuffer summary = new StringBuffer();
          summary.append((recurseSubdirs?"Recursive search for \"":"Search for \""));
          summary.append(searchStr);
          summary.append("\" starting in file:  ");
          summary.append(fileName);
          if (rootFile != null && !rootFile.isDirectory())
          {
            if(rootFile.canRead())
            {
              String [] ret = searchInFile(searchStr, fileName, true);
              summary.append("\nReturned Lines:");
              for (int i = 0; i < ret.length; i++)
              {
                summary.append(ret[i]);
                summary.append("\n");
              }
            }
          }
          else
          {
            Hashtable retVal =
              searchInFile(searchStr, fileName, recurseSubdirs, fileRegExp, verbose);
            if (retVal != null)
            {
              String currFilename = "";
              Vector lineNums = null;
              summary.append("\nReturned Hashtable:");
              for (Enumeration e = retVal.keys() ; e.hasMoreElements() ;)
              {
                currFilename = (String) e.nextElement();
                summary.append("\n   ");
                summary.append(currFilename);
                summary.append(" -     ");
                lineNums = (Vector) retVal.get(currFilename);
                for (int i = 0; i < lineNums.size(); i++)
                {
                  summary.append(((Integer)lineNums.get(i)).toString());
                  if ( (i + 1) < lineNums.size()) summary.append(", ");
                }
              }
            }
          }
          System.out.println(summary.toString());
        }
        else
        {
          // print searchInFile ERROR msg
        }
      }
      else if (args[0].equals("removeOldFiles"))
      {
        if (args.length >2)
        {
          removeOldFiles(args[1], Integer.parseInt(args[2]),
            (args.length>3?(args[3]!=null&&args[3].equals(true)?true:false):false));
        }
        else
        {
          helpMsg.append("ERROR: The removeOldFiles method requires 1");
          helpMsg.append(" directory name, 1 number for thee days old and an optional true/false to recurse");
          System.out.println(helpMsg.toString());
        }
      }
      else if (args[0].equals("createCurrentTimeStamp"))
      {
        System.out.println(createCurrentTimeStamp());
      }
      else if (args[0].equals("createCurrentDateTimeStamp"))
      {
        System.out.println(createCurrentDateTimeStamp());
      }
      else
      {
        System.out.println("Unknown Command: "+args[0]);
      }

    }

  }
}
