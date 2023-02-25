/* $Source: /cvsroot2/open/projects/WebARTS/ca/bc/webarts/tools/AutoUpdateApp.java,v $
 *  $Revision: 1.4 $  $Date: 2002/10/22 08:59:36 $  $Locker:  $
 *  Copyright 2001-2002 (C) WebARTS Design.   All rights reserved.
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
/*
Here is the revision log
------------------------
$Log: AutoUpdateApp.java,v $
Revision 1.4  2002/10/22 08:59:36  tgutwin
Many JavaDoc Comments changes.
Removed the StreamGobbler class... it now lives on its own in tools.
small change to the exec call.

Revision 1.3  2001/08/04 18:54:49  tgutwin

Added the ability to read properties file from a file in the current dir.
There are 3 places this app now looks for the prop file
1) in the jar in the curr dir
2) in the jar that is found in the classpath
3) in the prop file in the curr dir.


*/

package ca.bc.webarts.tools;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.lang.Runtime;
import java.lang.Process;
import java.lang.SecurityException;
import java.lang.String;
import java.util.jar.JarFile;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

// not necessary but saves headaches later if classes move
import ca.bc.webarts.tools.StreamGobbler;

import ca.bc.webarts.widgets.Util;

/**
 * This is a self standing Java application that acts as a middleware
 * application executor that lets you put this class and a properties file on a
 * client and be able to execute anything you want and have it update the app
 * automatically from a specified URL.
 * A <A HREF="#runApp(java.util.Properties)">static method</A> is also
 * available to access the functionality from within existing code.
 * <P>It executes applications... both native or Java as
 * specified in a Properties file that gets passed to it (via a filename on the
 * commandline). All system output from the executed app is echoed to the
 * System.out (both regular and error output).
 * <P> Its main purpose is to provide a single entrypoint to
 * execute applications that can be dynamically specified at runtime. This
 * allows a single deployment of this class onto a client and then tell this
 * class what to run and where to download an update from if needed.
 * <p>If you want this class to have a the name of your app; just extend this
 * class and name it what you want... like the JOggPlayeAutoUpdate.java file.
 * <P><B><U>Features:</U></B><UL><LI>executes Java<SUP><SMALL>TM</SMALL></SUP>
 * Class files
 * <LI>executes Java<SUP><SMALL>TM</SMALL></SUP> Jar files
 * <LI>executes native executable files
 * <LI>automatically retrieves the required files at a specified URL
 * <LI>automatically checks for newer updated files at a specified URL
 * <LI>automatically downmloads/updates any required files
 * <LI>can run as an application or from existing code via the
 * <A HREF="#runApp(java.util.Properties)">static runApp method</A></UL>
 * <P><B><U>Usage:</U></B><BR><code>java AutoUpdateApp</code> &lt;
 * <code>propertiesFilename</code>&gt; OR<BR>
 * <code>java -jar AutoUpdateApp.jar</code>
 * &lt;<code>propertiesFilename</code>&gt;<P>
 * The <B>properties file</B> describes the app to
 * execute. It has both required and optional entries as follows:
 * <UL>
 * <LI><B>Required Entries</B></LI>
 * <UL>
 * <LI><I><U>appType</U></I> - a String specifying the type of app that will
 * be executed
 * <BR>(NATIVE_APP = native; JAVA_APP = java;
*  APPLET_APP = applet; HTTPURL_APP = httpUrl)
 * </LI>
 * <LI><I><U>remoteAppURL</U></I> - a string specifying URL for the application
 * to launch<BR>
 * (for example: file://usr/bin/telnet.sh or file:/c:/winnt/wordpad.exe)
 * </LI>
 * </UL><BR>
 * <LI><B>Optional Entries</B></LI>
 * <UL><LI><I><U>remoteAppAutoDownload</U></I> - a string specifying
 * the flag (boolean string value) that specifies if this app should query the
 * download server (see property below) for updated app files.</LI>
 * <LI><I><U>remoteAppDownloadURL</U></I> - a string specifying
 * the URL of the download for the Remote Launch APP.  If the filename portion
 * of this URL does not match the filename portion of the remoteAppURL this app
 * will assume it is an archive file (ie Jar file) that will be downloaded and
 * will save it as a separate file.  In addition, if the appType is JAVA_APP,
 * this archive name will be added to the front of the classpath so the new
 * files in the archive will be used first.</LI>
 * <LI><I><U>remoteAppJvmParameters</U></I> - a string specifying any parameters
 * that should go directly to the JVM when executing a Java App. For Example:
 * -Xmx64m -Xms8m</LI>
 * <LI><I><U>remoteAppJvmClasspath</U></I> - a string specifying any additional
 * paths to add to the <I>front</I> of the classpath when executing a Java App.
 * The path entries in this string <B>must</B> be delimited by ";" because a
 * ":" might have to used in a DOS drive specification.</LI>
 * </UL><BR>
 * <LI><B>All extra entries</B> in the properties file will be passed directly
 * to the application as commandline parameters.</LI>
 * </UL><HR>
 * <P>An Example Properties file might look like:<BR>
 * <PRE>
 * # NATIVE Remote Application Description property file
 * appType=0
 * remoteAppURL=file:/c:/progra~1/tn.bat
 * #remoteAppURL=c:\\progra~1\\tn.bat
 * remoteAppDownloadURL=
 * remoteAppAutoDownload=false
 * telnetHost=10.0.0.22 2020
 * </PRE><P>
 * OR<BR>
 * <PRE>
 * # Java Remote Application Description property file
 * appType=1
 * remoteAppURL=SimpleExample
 * remoteAppDownloadURL=http://somesite.com/updates/MySimpleExample.jar
 * remoteAppAutoDownload=false
 * remoteAppJvmParameters=-Xmx64m
 * extraParm=myParm1
 * anotherExtraParm=myParm2
 * </PRE>
 * OR<BR>
 * <PRE>
 * # Java Remote Application Description property file
 * appType=1
 * remoteAppURL=TelnetApp.jar
 * remoteAppDownloadURL=http://somesite.com/updates/TelnetApp.jar
 * remoteAppAutoDownload=true
 * remoteAppJvmClasspath=/usr/local/extraclasses.jar;/usr/moreextraclasses.jar
 * telnetHost=10.0.0.22 2020
 * </PRE>
 *
 * <p>Author: <a href="mailto:tgutwin@webarts.bc.ca">Tom Gutwin P.Eng.</a>
 * <br>Copyright (C) 2002-2003 <a href="http://www.webarts.bc.ca">Web<i>ARTS</i>
 * Design</a>, North Vancouver Canada. All Rights Reserved.</p>
 * @author     <a href="mailto:tgutwin@webarts.bc.ca">Tom Gutwin P.Eng.</a>
 **/
public class AutoUpdateApp
{
  /** This var should be defined by an extending class (not required)**/
   protected static String appName_ = "";

  /** The default property filename to use. **/
   protected static String appPropertyFilename_ = appName_ + ".prop";


  /** this VM classpath **/
  static final String CLASSPATH = System.getProperty("java.class.path");

  /** Class Constant specifying that the app to run is a native application **/
  static public final String NATIVE_APP = "native";

  /** Class Constant specifying that the app to run is a Java application **/
  static public final String JAVA_APP = "java";

  /** Class Constant specifying that the app to run is a Java Applet **/
  static public final String APPLET_APP = "applet";

  /**
   * Class Constant specifying that the app to run is a URL Locationto view
   * using the native webbrowser.
   **/
  public static final String HTTPURL_APP = "httpUrl";

  /**
   * Remote Launch Property Key specifying the remote lauch app type.<P>This
   * is a required property in the supplied properties file.
   **/
  public static final String RMT_APP_TYPE         = "appType";

  /**
   * Remote Launch Property Key specifying the remote lauch
   * URL for the RemoteLaunchOperation.HTTPURL_APP app type.<P>This
   * is a required property in the supplied properties file.
   **/
  public static final String RMT_APP_URL          = "remoteAppURL";

  /**
   * Remote Launch Property Key specifying the URL of the download for the
   * Remote Launch APP.<P>This is an optional property in the supplied
   * properties file.
   **/
  public static final String RMT_APP_DOWNLOADURL  = "remoteAppDownloadURL";

  /**
   * Remote Launch Property Key specifying the flag (boolean string value) that
   * specifies if the RemoteLaunchOperation Operation should query the
   * download server for updated app files.<P>This is an optional property in
   * the supplied properties file. (default=true)
   **/
  public static final String RMT_APP_AUTODOWNLOAD = "remoteAppAutoDownload";

  /**
   * Remote Launch Property Key  specifying any parameters that should go to the
   * JVM when executing a Java App.<P>This is an optional property in
   * the supplied properties file. (default="")
   **/
  public static final String RMT_APP_JVM_PARAMETERS = "remoteAppJvmParameters";

  /**
   * Remote Launch Property Key specifying any parameters that should be added
   * to the <B>FRONT</B> of the classpath when executing a Java App.<P>This is
   * an optional property in the supplied properties file. (default="")<P>A
   * StringTokenizer is used to parse this property value and usessemi colons or
   * colons as delimiters. <B>Note</B> that spaces are NOT used as delimeters so
   * they can be used in the paths.
   **/
  public static final String RMT_APP_JVM_CLASSPATH = "remoteAppJvmClasspath";

  /**
    *  A helper Vector that keeps a table of the Defined Properties.
    **/
  private static Vector definedPropertyKeys = new Vector();

  /** A holder for the NA Clients System File Separator. **/
  private static final String SYSTEM_FILE_SEPERATOR = File.separator;

  /**
   * A holder for the directory location to save downloaded apps.<P>
   * Default = <code>./RemoteApps/</code> (or the DOS Equiv.
   * <code>.\RemoteApps\</code>).
   **/
  private static final String DEFAULT_SAVE_LOCATION = "." +
    SYSTEM_FILE_SEPERATOR + "RemoteApps" + SYSTEM_FILE_SEPERATOR;

  /**
   * Remote Launch Parm specifying the remote lauch app type (required).
   **/
  private static String appType_ = JAVA_APP;

  /**
   * Remote Launch Parm specifying the remote lauched application in the form of
   * a URL.<P>It gets assigned from the required property RMT_APP_URL in the
   * supplied properties file.
   **/
  private static URL remoteAppURL_ = null;

  /** The system dependant file path to execute. **/
  private static String filePath_ = "";

  /** The system dependant file path for the downloaded archive file.**/
  private static String archiveFilePath_ = "";

  /**
   * Remote Launch Parm specifying the URL of the download for the Remote Launch
   * APP.
   **/
  private static URL remoteAppDownloadURL_ = null;

  /**
   * Remote Launch Parm specifying the flag (boolean string val) that specifies
   * if the RemoteLaunchOperation Operation should query the
   * download server for updated app files.<P>(default=true)
   **/
  private static boolean remoteAppAutoDownload_ = true;

  /**
   * Remote Launch Parm specifying the any extra commandline parms to send
   * directly to the jvm.<P>(default="")
   **/
  private static String jvmParameters_ = "";

  /**
   * Remote Launch Parm specifying the any extra classpath parms to put at the
   * front of the classpath when the calling the jvm.<P>(default="")<P>A
   * StringTokenizer is used to parse this property value. So commas or a space
   * delimited list of entries will work.
   **/
  private static String jvmClasspath_ = "";

  /**
   * A class var to track the progress of the various validation checks that
   * are performed.
   **/
  private static boolean soFarSoGood_ = true;

  /**
   * A class var to indicate if the download URL is pointing to an archive file
   * (ie jar file) instead of the actual executable file.<p>This field gets
   * used by the updateAppFromServer method to determine where to save the file.
   * </p>
   *
   * @see #updateAppFromServer
   **/
  private static boolean archiveDownload_ = false;

  /**
   * A class var to record and error message to print out if any of the
   * validation checks on the properties fail.
   **/
  private static String errorMsg_ = "";

  /**
   * A table of strings holding any extra parameter properties that were passed
   * in the properties file. Extra means anything that do not match any of the
   * Keys specified above.
   **/
   private static Vector cmdLineParms_ = new Vector();


   /**
   * Basic constructor for the application.  It is empty. The entry for this
   * app is the <A HREF="#main(java.lang.String[])">main( String[] )</A> or
   * <A HREF="#runApp(java.util.Properties)">runApp(Properties)</A>methods.
   *
   * @see #main
   * @see #runApp
   **/
  protected AutoUpdateApp()
  {
    // not implemented
  }


  /**
   * The entry point for running an app defined by the passed in properties.
   * <P><b>This method blocks until the Executed app is complete</b></p>
   *
   * @param props the Properties defining the app to run as defined in the main
   * class description.
   *
   * @return the return code from the exec'd application, -1 if the app did not
   * get executed or the app returned a -1.
   *
   * @see #main
   **/
  public static int runApp(Properties props)
  {
    int retVal = -1;
     System.out.println("++ Initializing AutoUpdateApp");
    // init the class vars from the Properties file
    soFarSoGood_ = initialize(props);

    // required parameters are all set up??
    if (soFarSoGood_)
    {
      // required parms are supplied ... carry on the validation
      System.out.println("++ Ensuring the Remote app Is Available");

      // validate that the app is available
      soFarSoGood_ = ensureTheAppIsAvailable(filePath_);
    }

    // check for updates
    if (soFarSoGood_ && remoteAppAutoDownload_ && remoteAppDownloadURL_ != null)
    {
      // the return from this DOES not affect soFarSoGood_ because we already
      // confirmed that the app is available.
      System.out.println("++ Checking for an update.");
      checkAndUpdateApp(remoteAppDownloadURL_);
    }

    // execute
    if (soFarSoGood_)
    {
      // This call blocks until the Executed app is complete
      System.out.println("++ Executing the app");
      retVal = execute();
      System.out.println("++ Application Completed: Return code = " + retVal);
    }

    if (!soFarSoGood_)
      System.out.println(errorMsg_);

    return retVal;
  }


  /**
   * The main entry for this app. It performs all the calls to validate,
   * download and then execute the executable requested in the Properties file
   * that is passed in as this apps single commandline parameter.
   *
   * @param args are the commandline parameters.This app expects ONLY 1
   *             parameter... the absolute file path to the properties file
   *             containing all the info for the Remote App to execute.
   *
   * @see #runApp
   **/
  public static void main (String [] args)
  {
    if (args.length < 1)
      runApp(loadPropertiesFile(appPropertyFilename_));
    else
      System.out.println("AutoUpdateApp ERROR - incorrect number of" +
                         "commandline args:\nUSAGE: java -jar " + appName_ +
                         ".jar ");

    //System.exit(0);
  }


  /**
   * Initializes the apps vars (based on the passed in properties file)
   * and gets ready to start.
   *
   * @return true if everything went okay and we can now start
   **/
  private static boolean initialize(Properties props)
  {
    boolean retVal = true;
    // Validate that we got a properties file args.length ==1
    if (props == null)
      retVal = false;
    else
    {
      // set up our helper vector to save us time on compares later on
      definedPropertyKeys.add(RMT_APP_TYPE);
      definedPropertyKeys.add(RMT_APP_URL);
      definedPropertyKeys.add(RMT_APP_DOWNLOADURL);
      definedPropertyKeys.add(RMT_APP_AUTODOWNLOAD);
      definedPropertyKeys.add(RMT_APP_JVM_PARAMETERS);
      definedPropertyKeys.add(RMT_APP_JVM_CLASSPATH);
      // parse out the needed properties into the class variables
      retVal = parseProperties(props);
    }
    return retVal;
  }


  /**
   * This helper method checks that the specified file exists, if not it
   * downloads it from the download URL.
   *
   * @return true if everything went okay and we have the file in place.
   **/
  private static boolean ensureTheAppIsAvailable(String filePath)
  {
    boolean retVal = true;
    File executableFile = null;

    if (filePath == null)
      filePath = "";

    try
    {
      System.out.println("We have the Parms: Remote App URL = "
        + filePath);
      if (appType_ == JAVA_APP &&
        !(filePath.trim().toLowerCase().endsWith(".jar")) &&
        !(filePath.trim().toLowerCase().endsWith(".class")))
        executableFile = new File(filePath + ".class");
      else
        executableFile = new File(filePath);

      // now get the downloadUrl filename (if it exists)
      String downloadFilename = "";
      if (remoteAppDownloadURL_ != null)
      {
        downloadFilename = remoteAppDownloadURL_.getFile();
        downloadFilename = DEFAULT_SAVE_LOCATION +
          downloadFilename.substring(downloadFilename.lastIndexOf("/")+1);
      }

      if (executableFile.exists())
      {
        if (!executableFile.canRead())
        {
          errorMsg_ = "ERROR: Cannot read " + executableFile.getAbsolutePath();
          retVal = false;
        }
        else
          System.out.println("Executable Exists");
      }
      else
      {
        // do the initial download/install
        if (!(new File(downloadFilename)).exists())
        {
          System.out.println("Remote Launch Executable Does NOT Exist, " +
                             "attempting retrieval");
          retVal = updateAppFromServer(remoteAppDownloadURL_);
        }
      }
    }
    catch (SecurityException securityEx)
    {
      errorMsg_ = "ERROR: A Java Security Manager is in use and is " +
        "restricting execution of the " + filePath +
        " application.";
      retVal = false;
    }
    // catch anything else here
    catch (Exception otherEx)
    {
      errorMsg_ = "ERROR: An unforseen " + otherEx.getMessage() + "exception " +
                  "occured while asserting the " + filePath +
                  " application was available.\nIt will NOT execute.";
      retVal = false;
    }

    return retVal;
  }


  /**
   * Searches the classpath for the specified filename and then returns the full
   * path that is used for it.
   *
   * @return the path description of the passed filename as found in the
   *         classpath.
   **/
  private static String getFilePathFromClasspath(String filename)
  {
    String retVal = "";
    String pathSep = System.getProperty("path.separator");

    int fileIndex = CLASSPATH.indexOf(filename);
    //System.out.println("Searching the Classpath for " + filename + " " +fileIndex);
    if (fileIndex>=0)
    {
      int startSpot = 0;
      int nextSpot = CLASSPATH.indexOf(pathSep);
      // the fileName is in the classpath
      // System.out.print("Start Looking at ("+startSpot+","+nextSpot+")");
      while ((nextSpot < fileIndex) && nextSpot != -1)
      {
        startSpot = nextSpot;
        nextSpot = CLASSPATH.indexOf(pathSep,startSpot+1);
        //System.out.print(", ("+startSpot+","+nextSpot+")");
      }
      if (startSpot <= 0) startSpot = -1;
      if (nextSpot <= 0) nextSpot = CLASSPATH.length() ;
      retVal = CLASSPATH.substring(startSpot+1, nextSpot);
    }

    return retVal;
  }


  /**
   * Converts the path component of a URL to the native relative file path.
   *
   * @param url the url to use to convert.
   *
   * @return the converted path string. It will return a "" if the URL does not
   *         include a filepath portion.
   **/
  public static String convertSystemDependantPath(URL url)
  {
    String retVal = "";

    // First check if we need to do the '/' conversion
    if (url != null && SYSTEM_FILE_SEPERATOR.equals("\\"))
    {
      // yup... we have to switch 'em
      // gotta use getFile instead of getPath which is a jdk 1.3 method
      String tempPath = url.getFile();
      for ( int i = 0; i < tempPath.length();i++)
      {
        if (tempPath.charAt(i) != '/' )
        {
          retVal += tempPath.substring(i,i+1);
        }
        else
        {
          if (i != 0)
            retVal += "\\";
        }
      }
    }
    else if (url != null)
      // It is a Un*x path (no conversion needed)
      retVal = url.getFile().substring(1);

    return retVal;
  }


  /**
   * Determines the app type that has been assigned to the class variables and
   * calls the correct helper method that Executes the class defined remote app.
   * It Expects that all the class vars that desribe the app are in place and
   * valid.
   *
   **/
  private static int execute()
  {
    int retVal = -1;

    if(appType_.equals(NATIVE_APP))
    {
      retVal = executeNativeApp(filePath_,
                                cmdLineParms_);
    }
    else if(appType_.equals(JAVA_APP))
    {
        // first we have to build the JVM parameters Vector
        Vector jvmParmVector = null;
        if (!"".equals(jvmParameters_))
        {
          jvmParmVector = new Vector();
          StringTokenizer s = new StringTokenizer(jvmParameters_);
          while (s.hasMoreTokens())
          {
            jvmParmVector.add(s.nextToken());
          }
        }
        retVal = executeJavaApp(filePath_,
                                jvmParmVector,
                                cmdLineParms_);
    }
    else if(appType_.equals(APPLET_APP))
    {
      // not implemented
      errorMsg_ = "Applet type remote apps not yet supported";
      soFarSoGood_ = false;
    }
    else if(appType_.equals(HTTPURL_APP))
    {
      // not implemented
      errorMsg_ = "HTTP URL type remote apps not yet supported";
      soFarSoGood_ = false;
    }

    return retVal;
  }


  /**
   * Parses a String representation of a URL or absolute file path and converts
   * it into a URL.<P>This method is a bit forgiving... if the string is not
   * a correctlty formatted http, ftp, or file URL it will assume it is a file
   * URL and create the returned URL as such.
   *
   * @param value is the String to convert into a URL.
   *
   * @return the Valid URL for the Executable or null if this method failed to
   *         convert the passed in string.
   *
   * @throws MalformedURLException if the passed in String is not a URL
   **/
  private static URL parseAppExecutableUrl(String value)
     throws MalformedURLException
  {
    URL retVal = null;
    if (value.toLowerCase().startsWith("http:/") ||
        value.toLowerCase().startsWith("ftp:/")  ||
        value.toLowerCase().startsWith("file:/"))
    {
      // the following throws a MalformedURLException if value is bad
      retVal = new URL(value);
    }
    else // assume it is a file path
    {
      try
      {
        String tempPath = value; // Assume Un*x ... no replacement needed
        if (SYSTEM_FILE_SEPERATOR.equals("\\"))
        {
          /* Replace the file separators with the forward slash */
          tempPath = ""; // start from scratch
          for ( int i = 0; i < value.length(); i++)
          {
            if (value.charAt(i) != SYSTEM_FILE_SEPERATOR.charAt(0) )
            {
              tempPath += value.substring(i,i+1);
            }
            else
            {
              tempPath += "/";
            }
          }
        }
        retVal = new URL("file:/" + tempPath);
      }
      catch (NullPointerException badUrl)
      {
        throw new MalformedURLException(
          "Can't parse the location URL from the file path provided.");
      }
    }
    return retVal;
  }


  /**
   * Checks the specified remoteAppDownloadURL_ to see if there is a newer
   * version of the remoteAppURL_ <B>AND </B>then goes out and updates the app
   * if a newer one is available.
   *
   * @param urlToGet is the place to go to get the updated app.
   *
   * @return true if there is a newer version available and it was download
   *              successfully.
   *
   * @see #remoteAppDownloadURL_
   * @see #remoteAppURL_
   **/
  private static boolean checkAndUpdateApp(URL urlToGet)
  {
    boolean retVal = false;
    if (checkForUpdatedApp())
      retVal = updateAppFromServer(urlToGet);

    return retVal;
  }


  /**
   * Checks the specified remoteAppDownloadURL_ to see if there is a newer
   * version of the remoteAppURL_.
   *
   * @return true if there is a newer version available
   *
   * @see #checkAndUpdateApp
   * @see #remoteAppDownloadURL_
   * @see #remoteAppURL_
   **/
  private static boolean checkForUpdatedApp()
  {
    boolean retVal = false;
    if (filePath_ == null)
      filePath_ = "";

    try
    {
      File executableFile = null;

      if (appType_ == JAVA_APP &&
        !(filePath_.trim().toLowerCase().endsWith(".jar")) &&
        !(filePath_.trim().toLowerCase().endsWith(".class")))
        executableFile = new File(filePath_ + ".class");
      else
        executableFile = new File(filePath_);

      if (executableFile.exists())
      {
        if (executableFile.canRead())
        {
          long localModTime = executableFile.lastModified();
          URLConnection remoteConn =
            (URLConnection) remoteAppDownloadURL_.openConnection();
          if (remoteConn.getLastModified() > localModTime)
            retVal = true;
        }
      }
      else
        // check the download file
      {
        if (remoteAppDownloadURL_ != null)
        {
          // Parse out the filename portion of the download url
          String downloadFilename = remoteAppDownloadURL_.getFile();
          downloadFilename = DEFAULT_SAVE_LOCATION +
            downloadFilename.substring(downloadFilename.lastIndexOf("/")+1);

          // now create a file Object based on the above info so we can check
          // dates
          File localDownloadFile = new File(downloadFilename);
          if (localDownloadFile.exists())
          {
            if (localDownloadFile.canRead())
            {
              long localModTime = localDownloadFile.lastModified();
              URLConnection remoteConn =
                (URLConnection) remoteAppDownloadURL_.openConnection();
              if (remoteConn.getLastModified() > localModTime)
              {
                retVal = true;
              }
            }
          }
        }
      }
    }
    catch (IOException ioEx)
    {
      System.out.println("Cannot check for updates at this time: " +
        "The remote download URL specified " + remoteAppDownloadURL_ +
        "is not permitting a connection.\nUsing current local file.");
    }
    return retVal;
  }


  /**
   * Downloads the requested URL and replaces any existing version.<P>The place
   * for saving this downloaded file is determined based on the archiveDownload_
   * flag.  If archiveDownload_is true then dowloaded file is saved as a new
   * /updated file with its own filename. This is done when an archive file is
   * used as the download that is not the same as the executable name found in
   * the remoteAppURL_ field. For example: if remoteAppURL_ points to
   * ca.bc.webarts.SomeClass and the remoteAppDownloadURL_ points to
   * SomeClass.jar we need to save the downloaded file to SomeClass.jar and then
   * assume the commandline parameters will take care of the running/classpath
   * issues.
   *
   * @param urlToGet is the place to go to get the updated app.
   *
   * @return true if successfully updated
   *
   * @see #archiveDownload_
   **/
  private static boolean updateAppFromServer(URL urlToGet)
  {
    boolean retVal = false;

    if (urlToGet != null)
    {
      String filename = urlToGet.getFile();

      // get the filename of the download
      String remoteAppName = filename.substring(filename.lastIndexOf("/")+1);

      // check if the remote download filename is the same as our Executable
      // file name (we need this if the exe/class file is in an Jar archive etc
      if (archiveDownload_)
      {
        // write to a new file
        filename = DEFAULT_SAVE_LOCATION + remoteAppName;
        archiveFilePath_ = filename;
      }
      else
        // if so just overwrite the exe/class file
        filename = filePath_;

      //go get it
      try
      {
        System.out.print("Downloading an update for: " +
                           filename + " <");
        URLConnection connection =
          (URLConnection) urlToGet.openConnection();
        System.out.println(connection.getContentType() + "> ("+
                           connection.getContentLength() + ")");
        System.out.println("  FROM: " + urlToGet);

        // Store the file locally
        InputStream inUrl = null;
        if (urlToGet.getProtocol().equals("http"))
          inUrl = ((HttpURLConnection) connection).getInputStream();
        else
          inUrl = connection.getInputStream();
        new File(DEFAULT_SAVE_LOCATION).mkdir(); // ensures the dir exests
        FileOutputStream ostream = new FileOutputStream(filename);

        // start transfering
        byte [] bytesRead= new byte[1024];
        int numBytes = inUrl.read(bytesRead);
        int status = 0;
        System.out.print("..");
        while (numBytes != -1)
        {
          // write what we read
          ostream.write(bytesRead, 0, numBytes);

          // update our status indicator
          status += numBytes;
          if (status % 1024 == 0)
            System.out.print(".");

          // go back for more
          numBytes = inUrl.read(bytesRead);
        }
        System.out.println(".");
        ostream.close();
        retVal = true;
      }
      catch (NullPointerException nullEx)
      {
        System.out.println("Cannot update" + filename + " at this time: ");
        System.out.println("Reason: Cannot get the content from " +
                           urlToGet.toString());
         System.out.println("Using current local file.");
      }
      catch (MalformedURLException badUrlEx)
      {
        System.out.println("Cannot update" + filename + " at this time: ");
        System.out.println("Reason: Cannot get the URL for " +
                           filename);
        System.out.println("Using current local file.");
      }
      catch (SecurityException secEx)
      {
        System.out.println("Cannot update" + filename + " at this time: ");
        System.out.println("Reason: Cannot create the file due to security " +
                           "reasons.");
        System.out.println("Using current local file.");
      }
      catch (FileNotFoundException fnfEx)
      {
        System.out.println("Cannot update" + filename + " at this time: ");
        System.out.println("Reason: Cannot write the File.");
        System.out.println("Using current local file.");
      }
      catch (IOException ioEx)
      {
        System.out.println("Cannot update" + filename + " at this time: ");
        System.out.println("Reason: The remote download URL specified " +
                           urlToGet + "is not permitting a connection.");
        System.out.println("Using current local file.");
      }
    }

    return retVal;
  }


  /**
   * Executes the Specified Native OS application with the provided commandline
   * parameters. This method blocks until the Executed app is complete. It also
   * cleans up its sub-process and garbage collects.
   * <br><b><u>
   * NOTE:</b></u> This method uses a vector for the appParams and DOES NOT guaranty
   * parameter order.  Please use the
   * <a href="#executeNativeApp(java.lang.String, java.lang.String[])">
   * executeNativeApp(String executableLocation, String [] cmdParms)</a> method\
   * which uses an array for storage/retreival of the app parms.
   *
   * @param executableLocation is the path to the executable to run
   *
   * @param appParms and extra commandline parameters to tag onto the end of the
   *                 commandline that gets executed.
   *
   * @return returns the return code from the executed app/process
   *
   * @see #executeNativeApp(java.lang.String, java.lang.String[])
   * @see StreamGobbler
   * @see java.lang.Runtime#getRuntime
   **/
  protected static int executeNativeApp(String executableLocation,
                                      Vector appParms)
  {
    int cmdParms = 0;
    if (appParms != null)
      cmdParms = appParms.size();
    String [] cmds = new String [cmdParms+1];

    for (int i=1; i <= cmdParms; i++)
    {
      cmds[i] = (String) appParms.elementAt(i-1);
    }
    return executeNativeApp(executableLocation, cmds);
  }


  /**
   * Executes the Specified Native OS application with the provided commandline
   * parameters. This method blocks until the Executed app is complete. It also
   * cleans up its sub-process and garbage collects. This method sends app
   * output to std out.
   *
   * @param executableLocation is the path to the executable to run
   *
   * @param cmdParms and extra commandline parameters to tag onto the end of the
   *                 commandline that gets executed.
   *
   * @return returns the return code from the executed app/process
   *
   * @see StreamGobbler
   * @see java.lang.Runtime#getRuntime
   **/
  public static int executeNativeApp(String executableLocation,
                                      String [] cmdParms)
  {
    return executeNativeApp(executableLocation, cmdParms, null);
  }


  /**
   * Executes the Specified Native OS application with the provided commandline
   * parameters. This method blocks until the Executed app is complete. It also
   * cleans up its sub-process and garbage collects.
   *
   * @param executableLocation is the path to the executable to run
   *
   * @param cmdParms and extra commandline parameters to tag onto the end of the
   *                 commandline that gets executed.
   *
   * @param outputFilename a filename to send the output to.
   *
   * @return returns the return code from the executed app/process
   *
   * @see StreamGobbler
   * @see java.lang.Runtime#getRuntime
   **/
  public static int executeNativeApp(String executableLocation,
                                      String [] cmdParms,
                                      String outputFilename)
  {
    int retVal = -1;
    //int cmdParms = 0;
    //if (appParms != null)
    //  cmdParms = appParms.size();
    String [] cmds = new String [cmdParms.length+1];
    cmds[0] = executableLocation;

    System.out.print("\nParms: ");
    for (int i=1; i <= cmdParms.length; i++)
    {
      cmds[i] = (String) cmdParms[i-1];
      System.out.print(" " + cmds[i]);
    }
    System.out.println("");

    // Start the app... This starts a new process
    try
    {
      File appDirfile = Util.getFileDir(executableLocation);
      System.out.println("Executable Dir : "+ appDirfile.getAbsolutePath());
      Runtime runtime = Runtime.getRuntime();
      System.out.println("Executing: "+ cmds[0]);
      Process p = runtime.exec(cmds, null, appDirfile);

      // capture any output we get
      // any error message?
      StreamGobbler errorGobbler = new
        StreamGobbler(p.getErrorStream(), "APP ERR:");

      // redirect any output?
      FileOutputStream pumpedOutputFile = null;
      if (outputFilename != null && !outputFilename.equals(""))
      {
        pumpedOutputFile = new FileOutputStream(outputFilename);
        System.out.println("Output Redirected to file: "+ outputFilename);
      }
      StreamGobbler outputGobbler = new
        StreamGobbler(p.getInputStream(), "",
                      new PrintStream(pumpedOutputFile));
      outputGobbler.setCapture(false);

        // kick them off
      errorGobbler.start();
      outputGobbler.start();

      // now wait for the process to end
      try
      {
        p.waitFor();
      }
      catch (InterruptedException intEx)
      {
        // no biggie
        System.out.print("Process "+ cmds[0] +" Interupted?");
      }
      retVal = p.exitValue();
      errorGobbler.finishedGobbling_ = true;
      outputGobbler.finishedGobbling_ = true;
      p.destroy();
      p = null;
      runtime.gc();
    }
    catch (IOException ioEx)
    {
      errorMsg_ = "ERROR: An IO exception occured while attempting " +
        "execution of the " + executableLocation + " application.";
      soFarSoGood_ = false;
    }
    catch (SecurityException securityEx)
    {
      errorMsg_ = "ERROR: A Java Security Manager is in use and is " +
        "restricting execution of the " + executableLocation + " application.";
      soFarSoGood_ = false;
    }
    catch (Exception ex)
    {
      errorMsg_ = "ERROR: A Java Exception Occured" +
        "restricting execution of the " + executableLocation + " application.";
      soFarSoGood_ = false;
    }

    return retVal;
  }


  /**
   * Executes the Specified Java appilcation with the provided commandline
   * parameters.
   *
   * @param classLocation is the path to the executable to run
   *
   * @param appParms and extra commandline parameters to tag onto the end of the
   *                 commandline that gets executed.
   * @return returns the return code from the executed app/process
   **/
  protected static int executeJavaApp(String classLocation, Vector appParms)
  {
    return executeJavaApp(classLocation, null, appParms);
  }


  /**
   * Executes the Specified Java appilcation with the provided JVM parameters
   * and executableApp commandline parameters.
   *
   * @param classLocation is the path to the executable to run
   *
   * @param jvmParms holds any cmdline parms to send directly to the jvm
   *
   * @param appParms and extra commandline parameters to tag onto the end of the
   *                 commandline that gets executed.
   * @return returns the return code from the executed app/process
   **/
  protected static int executeJavaApp(String classLocation,
                                    Vector jvmParms,
                                    Vector appParms)
  {
    int retVal = -1;
    int cmdParms = 0;
    if (appParms != null)
      cmdParms = appParms.size();
    Vector v = new Vector();
    int vCounter = 0;

    // add the JVM parms first
    if (jvmParms != null)
    {
      int jvmParmSize = jvmParms.size();
      // add all the JVM parameters
      for (int i=0; i< jvmParmSize; i++)
      {
        v.add(vCounter++,(String)jvmParms.elementAt(i));
      }
    }

    // Add the classpath parm (if it is not empty)
    if (archiveDownload_)
      jvmClasspath_ = archiveFilePath_+ File.pathSeparator + jvmClasspath_;

    if (!"".equals(jvmClasspath_))
    {
      v.add(vCounter++,"-cp");
      v.add(vCounter++,jvmClasspath_);
    }

    // verify if the java app is a class or jar file
    // if it is a jar we have to provide the extra -jar item on the cmdline
    // this assumes the jar is set up to be autoexecuting jar
    if (classLocation.trim().toLowerCase().endsWith("jar"))
      v.add(vCounter++,"-jar");
    v.add(vCounter++,classLocation);

    // add all the extra parameters following the class/jar file
    for (int i=0; i < cmdParms; i++)
    {
      v.add(vCounter++,(String)appParms.elementAt(i));
    }

    /*
    cmdParms = v.size();
    System.out.print("Parms: ");
    for (int i=1; i <= cmdParms; i++)
    {
      System.out.print(" " + (String) v.elementAt(i-1));
    }
    System.out.println("");
    */

    String javaHome = System.getProperty("java.home");
    appParms.add(classLocation);
    retVal = executeNativeApp(javaHome + SYSTEM_FILE_SEPERATOR +"bin" +
                              SYSTEM_FILE_SEPERATOR + "java", v);
    return retVal;
  }


  /**
   * Parses the passed in Properties and pulls out all the name/value pairs
   * and assigns them to the class vars as needed.
   *
   * @param props the Properties to parse
   *
   * @return true if the method was able to successfully assign all class vars
   *
   **/
  private static boolean parseProperties(Properties props)
  {
    boolean retVal = true;

    // grab out the name/value pairs
    // app type
    if (props.containsKey(RMT_APP_TYPE))
    {
      appType_ = props.getProperty(RMT_APP_TYPE);
    }
    else
    {
      errorMsg_ = "Can't parse the required parameter: application type.";
      retVal = false;
    }

    // app locator
    if (retVal && props.containsKey(RMT_APP_URL))
    {
      try
      {
        remoteAppURL_ = parseAppExecutableUrl(props.getProperty(RMT_APP_URL));
        filePath_ = convertSystemDependantPath(remoteAppURL_);
      }
      catch (MalformedURLException badUrl)
      {
        errorMsg_ = "Can't parse the location URL for the application " +
          "to execute";
        retVal = false;
      }
    }
    else
    {
      errorMsg_ = "Can't parse the required parameter: application location.";
      retVal = false;
    }

    String tempVal = null;
    // optional autodownload flag
    if (retVal && props.containsKey(RMT_APP_AUTODOWNLOAD))
    {
      tempVal = (String) props.get(RMT_APP_AUTODOWNLOAD);
      if (tempVal != null)
        remoteAppAutoDownload_ = (
          "false".equals(tempVal.trim().toLowerCase())?
          false:
          true);
    }

    // optional download url
    if (retVal &&
        props.containsKey(RMT_APP_DOWNLOADURL))
    {
      try
      {
        remoteAppDownloadURL_ =
          new URL((String) props.get(RMT_APP_DOWNLOADURL));

        // check and set the archiveDownload_ indicator
        String remoteFilename = remoteAppDownloadURL_.getFile();

        // get our path for saving set up
        String remoteAppName =
          remoteFilename.substring(remoteFilename.lastIndexOf("/")+1);
        String localAppName =
          filePath_.substring(filePath_.lastIndexOf(SYSTEM_FILE_SEPERATOR)+1);

        // check if the remote download filename is the same as our Executable
        // file name. Set archiveDownload_if the exe/class file is in
        // an archive (ie Jar or maybe a zip)
        // this gets used be the Auto Download feature (updateAppFromServer)
        if (remoteAppName != null && !remoteAppName.equals(localAppName))
        {
          // if so just overwrite the exe/class file
          archiveDownload_ = true;
          archiveFilePath_ = DEFAULT_SAVE_LOCATION + remoteAppName;
        }
        else
          filePath_ = DEFAULT_SAVE_LOCATION + remoteAppName;
      }
      catch (MalformedURLException badUrl)
      {
        // This is Okay because this Property is an optional one.
        // so just inform the user
        System.out.println("WARNING: Can't parse the supplied location URL " +
                           "to download the application to execute.");
        System.out.println("Using current local file.");
      }
    }

    // optional jvm parms string
    if (retVal && props.containsKey(RMT_APP_JVM_PARAMETERS))
    {
      jvmParameters_ = props.getProperty(RMT_APP_JVM_PARAMETERS);
    }

    // optional jvm classpath string
    if (retVal && props.containsKey(RMT_APP_JVM_CLASSPATH))
    {
      StringTokenizer st = new StringTokenizer(
        props.getProperty(RMT_APP_JVM_CLASSPATH), ";");
      while (st.hasMoreTokens())
      {
        // add them to the FRONT of any existing jvmClasspath_
        jvmClasspath_ = st.nextToken() +
          (jvmClasspath_==""?"":File.pathSeparator + jvmClasspath_);
      }
    }

    // grab out all the rest
    for (Enumeration myEnum = props.keys(); myEnum.hasMoreElements();)
    {
      String currentPropKey = (String) myEnum.nextElement();
      if (currentPropKey != null &&
          !definedPropertyKeys.contains(currentPropKey))
      {
        // tokenize any extra parms if it can
        tempVal = props.getProperty(currentPropKey);
        StringTokenizer st = new StringTokenizer(tempVal);
        while (st.hasMoreTokens())
        {
          cmdLineParms_.add(st.nextToken());
        }
      }
    }

    return retVal;
  }


  /**
   * Loads the AutoDownload app information from the properties file so this
   * wrapped app can execute. The properties file is encased in the root dir of
   * this apps jar file.
   *
   * @return the parsed Properties contained in the file specified by the passed
   *         in appPropertyFilename_, null if unable to get the contained
   *         Properties
   **/
  protected static Properties loadAutoAppPropertiesFile()
  {
    Properties props = null;
    boolean errorOccured = true;
    InputStream in = null;
    JarEntry autoAppPropsJarEntry = null;
    String currDir = System.getProperty("user.dir");
    String thisJarFile = currDir + SYSTEM_FILE_SEPERATOR + appName_+".jar";
    String jarInClasspath = getFilePathFromClasspath(appName_+".jar");
    String fileInCurrDir = currDir + SYSTEM_FILE_SEPERATOR +
                           appPropertyFilename_;

    try
    {
      // first check the current directory for a jar file with this appsname.jar
      File tempFile = new File(thisJarFile);
      if (tempFile == null || !tempFile.exists() || !tempFile.canRead())
      {
        // if no jar in the current dir then going looking in the classpath
        tempFile = new File(jarInClasspath);
        if (tempFile == null || !tempFile.exists() || !tempFile.canRead())
        {
          // Last resort look for an unzipped set of files
          tempFile = new File(fileInCurrDir);
          if (tempFile == null || !tempFile.exists() || !tempFile.canRead())
          {
            // Not Found Anywhere
            thisJarFile = null;
          }
          else
          {
            // Its an Unzipped Archive
            // So load the props right here (leave errorOccured = true
            try
            {
              in = new FileInputStream(tempFile);
              props = new Properties();
              props.load(in);
              errorOccured = false;
            }
            catch (FileNotFoundException fEx)
            {
              System.out.println("No Application Properties File " +
                                 "FileNotFoundException " + appPropertyFilename_);
            }
            catch (IOException ioEx)
            {
              System.out.println("IO Error Reading Properties File " +
                                 "FileNotFoundException " + appPropertyFilename_);
            }
          }
        }
        else
        {
          // Its in The Classpath
          thisJarFile = jarInClasspath;
          errorOccured = false;
        }
      }
      else
        // Its in the Jar
        errorOccured = false;
    }
    catch (NullPointerException fEx)
    {
      thisJarFile = null;
    }

    try
    {
      if (!errorOccured) // if in jar or jar in classpath
      {
        System.out.println(" +++ Looking for Jar File \n" + thisJarFile);
        JarFile jar = new JarFile(thisJarFile, false);
        autoAppPropsJarEntry = jar.getJarEntry(appPropertyFilename_);
        System.out.println("appPropertyFilename_="+appPropertyFilename_);
        System.out.println("autoAppPropsJarEntry="+
                           (autoAppPropsJarEntry==null?"null":"valid"));
        in = jar.getInputStream(autoAppPropsJarEntry);
        props = new Properties();
        props.load(in);
        errorOccured = false;
      }
    }
    catch (NullPointerException fEx)
    {
      System.out.println("No Application Properties JarEntry " +
                         "NullPointerException " + appPropertyFilename_);
    }
    catch (FileNotFoundException fEx)
    {
      System.out.println("No Application Properties JarEntry " +
                         "FileNotFoundException " + appPropertyFilename_);
    }
    catch (IOException ioEx)
    {
      System.out.println("IO Error Reading Properties JarEntry " +
                         "FileNotFoundException " + appPropertyFilename_);
    }
    catch (SecurityException secEx)
    {
      System.out.println("Jar SecurityException " + thisJarFile);
    }

    return props;
  }


  /**
   * Loads the passed Properties file and returns a Properties object.
   *
   * @param propFilename the filename for the Properties file to parse
   *
   * @return the parsed Properties contained in the file specified by the passed
   *         in propFilename, null if unable to get the contained Properties
   *
   **/
  protected static Properties loadPropertiesFile(String propFilename)
  {
    Properties props = new Properties();
    try
    {
      // validate that the properties file exists
      FileInputStream propFileIn = new FileInputStream(propFilename);

      // Load the Properties File
      props.load(propFileIn);

    }
    catch (FileNotFoundException fnfEx)
    {
      // the path is null
      props = null;
      errorMsg_ = "Cannot obtain the application path (File Not Found).";
    }
    catch (IOException ioEx)
    {
      // the path is null
      props = null;
      errorMsg_ = "Cannot obtain the application path (I/O Error).";
    }
    return props;
  }
}
