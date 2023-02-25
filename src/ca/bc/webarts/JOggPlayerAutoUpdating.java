/*
 *  $Source: f:/cvsroot2/open/projects/jOggPlayer/ca/bc/webarts/JOggPlayerAutoUpdating.java,v $
 *  $Name:  $
 *  $Revision: 1.4 $
 *  $Date: 2002/07/18 01:11:07 $
 *  $Locker:  $
 */
/*
 *  JOggPlayerAutoUpdating - AutoUpdating GUI Pure Java Ogg Vorbis player.
 *
 *  Copyright (C) 2001 WebARTS Design, North Vancouver Canada
 *  http://www.webarts.bc.ca/jOggPlayer
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

package ca.bc.webarts;

class JOggPlayerAutoUpdating extends ca.bc.webarts.tools.AutoUpdateApp
{


   /**
   * Basic constructor overs super to set the name and properties file for the
   * application.  The entry point for this app
   * app is the <A HREF="#main(java.lang.String[])">main( String[] )</A> or
   * <A HREF="#runApp(java.util.Properties)">runApp(Properties)</A>methods.
   *
   * @see #main
   * @see #runApp
   **/
  private JOggPlayerAutoUpdating()
  {
    appName_ = "AutoUpdatingJOggPlayer";
    appPropertyFilename_ = appName_ + ".prop";
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
  public static void main(String [] args)
  {
    JOggPlayerAutoUpdating me = new JOggPlayerAutoUpdating();
    System.out.println("Starting The AutoUpdateApp "+me.appName_);
    if (args.length < 1)
      me.runApp(me.loadAutoAppPropertiesFile());
    else
      System.out.println("JOggPlayerAutoUpdating ERROR - incorrect number of" +
                         "commandline args:\nUSAGE: java -jar " + me.appName_ +
                         ".jar ");

  }
}
