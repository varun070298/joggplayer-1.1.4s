jOggPlayer - Copyright (C) 2001,2002 WebARTS Design, North Vancouver Canada

README
version 1.1.4 - $Date: 2002/04/26 16:30:41 $

$Source: v:/cvsroot2/open/projects/jOggPlayer/readme.txt,v $
$Name:  $
$Revision: 1.8 $
$Date: 2002/04/26 16:30:41 $
$Locker:  $


* Contents
****************************************************************************

- About jOggPlayer
- Requirements
- jOggPlayer on the Internet
- Reporting bugs
- Suggesting features
- Building from source (this is new)
- Documentation
- Extra OS/2 script files
- Libraries
- ToDo list / Version History
- Credits
- GNU General Public License


* About jOggPlayer
****************************************************************************
An open source platform independant Graphical Ogg Vorbis Player!
Developed using Java, so if your system has a Java VM you can use this app.

It provides the usual basics needed to enjoy your files:
 - plays files from your hard drive or streams from a URL
 - individual file play or multiple file looping play
 - sequential or random play
 - in applet volume control that goes to 11!
 - visual song progrees indicator
 - displays ogg file information comments ( song name, Artist etc.)
 - unlimitted size playlist that can transparently include files and/or URLs
 - favorite song selection in the playlist
 - user selectable directory recursion on file load
 - can load a file, URL or a whole directory of files on startup
 - Drag and drop files
 - mini-view layout
 - byte counter shows up on time display
 - can run as an applet
 - more
 - PLUS it sounds great!

It is FREE. Available under the GNU General Public License.



* Requirements
****************************************************************************
   Java 1.3 runtime environment (or the Java1.3 Plugin)
or Java 1.2 with the java sound extension.
or Java 1.1.8 with the JFC extension and the Java sound extension.
**** Java 1.1. is broken right now

The Java sound extension is available as a separate download that has now
been included in the Java Media Framework API.
  Java Sound API Homepage
   - http://java.sun.com/products/java-media/sound/index.html

  Java Media Framework API Homepage
   - http://java.sun.com/products/java-media/jmf/

The JFC extension (Swing) is also available as a separate download.
(if you are using Java 1.1)
 - http://java.sun.com/products/jfc


* Installation
****************************************************************************
If you are seeing this readme file.... you probably have done enough to
have this app installed.
- If you downloaded the zip archive ... simply unzip it in the dir you want
jOggPlayer to live.
- if you downloaded the installer... double click the installer jar file.


* jOggPlayer on the Internet
****************************************************************************
Homepage:  http://jOggPlayer.webarts.bc.ca


* Support / Reporting Bugs / Suggesting features
****************************************************************************
Please send an email to support@webarts.bc.ca
OR
the preferred way is to join the jOggPlayer Mailing list
jOggPlayer-users-subscribe@yahoogroups.com


* Building from source
****************************************************************************
The source code for the jOggPlayer is included with the distribution in the
src subdirectory.

The required helper library/classes that jOggPlayer uses
(jConfig, kiwi toolkit, jOrbis) are also included.

You will need Jakarta Ant to build this app - http://jakarta.apache.org

The build file is in the src directory and the default target
builds a new jOggPlayer Jar file in the build subdirectory.
All you have to do is cd to src and run ant from that dir.

**** Please note ... this is not the same ant build file I use.
                     Mine is a bit more detailed. So it may have some bugs.
**ALSO** I don't include all the files from the kiwi toolkit to save space.
So if you want to build those files (which the supplied ant build file does NOT)
go and get the full kiwi distribution.


* Documentation
****************************************************************************
Try the help inside the app.
Join the jOggPlayer Mailing list jOggPlayer-users-subscribe@yahoogroups.com
It has screenshots, archives, downloads....

Right Click (ctrl click) to bring up the menu with all the commands.
If you can't get something working ... email me.

Usage:
     java -jar jOggPlayer.jar [file/URL location]

       OR
     java -classpath ./jOggPlayer.jar ca.bc.webarts.jOggPlayer [file/URL location]

       OR if running the Auto-Update Version
     java -jar AutoUpdatingJOggPlayer.jar [file/URL location]

     where [file location] can be:
                                  a filename
                                  a URL to a ogg file
                                  a directory name to recurse for ogg files


* Extra OS/2 script files
****************************************************************************
I have made a few extra cmd files for OS/2 users to get things going as
easily as possible.

- create_os2_desktop_joggplayer_object.cmd
- java2.cmd
- jOggPlayer.ico
- jOggPlayerJava2.cmd

- create_os2_desktop_joggplayer_object.cmd
  Its name says it all

- java2.cmd
  COPY THIS INTO YOUR PATH SOMEWHERE
  It sets up the Warp environmnet to use the java13 runtime
  instead of a default 1.1.8.
  This is used when you have the 1.3 JVM installed but left the
  1.1.8 JVM as the default some of the base OS/2 Java Apps work.
  And now you want to run your applications with the 1.3 runtime.
  Test it by typing --->   java2.cmd -version

- jOggPlayer.ico
  OS/2 Icon

- jOggPlayerJava2.cmd
  Starts jOggPlayer using the Java13 VM using the above java2.cmd script



* Libraries
****************************************************************************
  jOggPlayer uses
   - the Java Ogg Vorbis decoder built by JCraft
   - the jConfig library found at http://jconfig.sourceforge.net
   - the Kiwi toolkit
   - the IzPack installer was used.
  (see credits below)


* ToDo List / Version List
****************************************************************************
For the Latest info see http://jOggPlayer.webarts.bc.ca

  ToDo
    - more help info
    - graphical VU level meter.
    - faster audio response when control buttons are pressed.
      (on a button press -cut off audio before the buffer is emptied).
    - improved ogg comment listing (rather than just a tooltip).
    - more timer display options.
    - other stuff.

  Current CVS Code - unreleased
    - Added an option to have jOggPlayer auto start playing the selected song
      when jOggPlayer starts. This allows you to load a file on the commandline
      and have it start playing automatically.
      (useful if jOgg is a helper app for m3u and ogg files in your browser.)
    - fixed a bug when you delete an entry in the playlist ... the looping does
      not wrap from the last to first song
    - Not all possible fonts were showing up in the font list
    - if Add From URL is chosen and a pls or m3u playlist file url is entered
      jOggPlayer now realizes it and loads the songs from within that playlist.
    - removed unnecessary debug output from console.
    - the logging level can now be set via a new options menu item
    - added some help info
    - new FAQ on website

  Version 1.1.3 - released April 8, 2002
    - Fixed bugs that were screwing up the favourites items and playlist.
    - bug fixes when looping was selected and there were only 1 or 2 files in
      the playlist
    - the playlist drop-down now shows the correct title when a new song is
      added and when the list is empty
    - fixed a bug when loading songs from a playlist file
    - the default playlist file that is looked for at startup is now
      "jOggPlaylist.pls"
    - an ant build file is now included so you can build from source!!!

  Version 1.1.2 - released Feb 6, 2002
    - Fixed a few bugs that had broke the Play/ Forward/ back buttons
      as well as the favourites items.

  Version 1.1.1 - released Jan 6, 2002
    - updated jOrbis to 0.0.11

  Version 1.1.0 - released Jan 2, 2002
    - an installer is now used to install

  Version 1.1 Release Candidate 1 (December 16, 2001)

    - more configurable items saved on close and loaded on open
    - Mute Button
    - Options Menu Item with all config items inside
    - User settable Vorbis file read / play buffer
    - Added the popup menu to the display and playlist areas
    - Playlist colour now follows users selected colour
    - Added more help info (Drag & Drop, Mini View )

  Version 1.1 Pre 5 - released December 14th

    - MAJOR BUG FIX which hung Macs and Unix/Linux (endless loop)
    - more configurable items saved on close (last view, playlist open/closed)
    - basic help info

  Version 1.1 Pre4 - released Dec 1, 2001
    - more mini-view fixes.
    - save state into a config file (basics window locations, colours, fonts
      saved) More stuff to save on this item


  Version 1.1 Pre3 - released November 15, 2001
    - mini-view layout (I still have features to add to this).
    - byte counter shows up on time display.
    - new playlist menu item to clear or set all songs as favourite.

    Version 1.10 pre release 2
    - Playlist dropdown bug fixes.

  Version 1.10 pre release 1
    - more JavaDocs
    - some code cleanup ( standard var naming etc. )
    - user selected songs to play out of playlist.
      (checkboxes in the playlist combobox).
    - Improved Layout Management (you can now resize the app)
    - drag and drop of files into playlist
    - favorites style playlist (checkboxes in the drop down list)
      (still a bit buggy, I am  working on it)

  Version 1.01
    - Improved Layout Management
    - Improved Web Site
    - Separate the Playlist into its own class to keep things
       more object oriented.

  Version 1.0
    - Playlist save/load
    - textured backgrounds (still buggy)
    - font selection options
    - Web Site with links to tools, info etc.

  Version 0.9b ( private beta test release )
    - plays Vorbis ogg files
    - plays Vorbis ogg streams from a URL
    - individual file play or multiple file looping play
    - sequential or shuffle/random play
    - in app/let volume control
    - visual song progrees indicator
    - displays ogg file information comments ( song name, Artist etc.)
      Tooltip popup over display area
    - unlimitted size playlist
    - Playlist can transparently include files and/or URLs
    - Playlist can add a user selectable directory with recursive file load
    - Playlist can add a user entered URL
    - can load a file, URL or a whole directory of files on startup


* Credits
****************************************************************************
    The Xiphophorus Company for creating Ogg Vorbis.
    ------------------------------------------------
    Ogg Vorbis is a fully Open, non-proprietary, patent-and-royalty-free,
    general-purpose compressed audio format for high quality (44.1-48.0kHz,
    16+ bit, polyphonic) audio and music at fixed and variable bitrates
    from 16 to 128 kbps/channel. This places Vorbis in the same class as audio
    representations including MPEG-1 audio layer 3, MPEG-4 audio (AAC
    and TwinVQ), and PAC.

    Vorbis is the first of a planned family of Ogg multimedia coding formats
    being developed as part of Xiphophorus's Ogg multimedia  project.

    See them on the net at http://xiph.org/ogg/vorbis/


    JCraft Inc. for building jOrbis!
    --------------------------------
    JOrbis is a pure Java Ogg Vorbis decoder.
    JOrbis accepts Ogg Vorbis bitstreams and decodes them to raw PCM.

    Way to go JCraft!

    See them on the net at http://www.jcraft.com/jorbis/index.html


    Icons  Copyright(C) 1998  by  Dean S. Jones
    dean@gallant.com www.gallant.com/icons.htm
    -------------------------------------------


    jConfig team
    --------------------------------
    Thanks for building a XML based application config file library.
    It was exactly what I needed!
    http://jconfig.sourceforge.net



    IzPack installer
    --------------------------------
    Thanks for building a XML based installer.
    It was exactly what I needed!
    http://www.izforge.com



    WebARTS Design encapsulated the jOrbis decoder into
    the jOggPlayer graphical player.
    ---------------------------------------------------
    You are reading about it!
    See Us on the net at http://www.webarts.bc.ca/jOggPlayer


* GNU GENERAL PUBLIC LICENSE
****************************************************************************
See the included License.txt
