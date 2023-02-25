/*
 *  $Source: /cvsroot2/open/projects/jOggPlayer/ca/bc/webarts/jOggPlayer.java,v $
 *  $Name:  $
 *  $Revision: 1.30 $
 *  $Date: 2008-10-13 18:01:39 -0700 (Mon, 13 Oct 2008) $
 *  $Locker:  $
 */
/*
 *  jOggPlayer -- GUI Enhanced Pure Java Ogg Vorbis player.
 *
 *  Graphical and User enhancements Written by Tom Gutwin - WebARTS Design.
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
/*
 *  Original Java Vorbis Ogg Audio/Decoder/Player code Written by:
 *  2000 ymnk<ymnk@jcraft.com>
 *  Copyright (C) 2000 ymnk, JCraft,Inc.
 *  http://www.jcraft.com
 *
 *  Many thanks to
 *  Monty <monty@xiph.org> and
 *  The XIPHOPHORUS Company http://www.xiph.org/ .
 *  JOrbis has been based on their awesome works, Vorbis codec and
 *  JOrbisPlayer depends on JOrbis.
 *
 */
/*
 *  on with the show
 */
package ca.bc.webarts;

import java.applet.Applet;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.Window;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;

import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.Properties;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;

import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JWindow;
import javax.swing.ToolTipManager;

import ca.bc.webarts.tools.Log;

import ca.bc.webarts.widgets.ColouredLabel;
import ca.bc.webarts.widgets.ExampleFileFilter;
import ca.bc.webarts.widgets.FirewallAuthenticator;
import ca.bc.webarts.widgets.JAboutBox;
import ca.bc.webarts.widgets.JCheckDropDown;
import ca.bc.webarts.widgets.PlayList;
import ca.bc.webarts.widgets.Splash;
import ca.bc.webarts.widgets.Util;

import ca.bc.webarts.widgets.dnd.FileDrop;

import ca.bc.webarts.widgets.dnd.FileDrop.Listener;

//import calpa.html.*;

import com.jcraft.jogg.Packet;
import com.jcraft.jogg.Page;
import com.jcraft.jogg.StreamState;
import com.jcraft.jogg.SyncState;

import com.jcraft.jorbis.Block;
import com.jcraft.jorbis.Comment;
import com.jcraft.jorbis.DspState;
import com.jcraft.jorbis.Info;

import kiwi.ui.KButton;
import kiwi.ui.KPanel;

import kiwi.util.KiwiUtils;

import org.jconfig.ConfigurationManager;

import net.roarsoftware.lastfm.Event;
import net.roarsoftware.lastfm.Geo;
import net.roarsoftware.lastfm.scrobble.ResponseStatus;
import net.roarsoftware.lastfm.scrobble.Scrobbler;
import net.roarsoftware.lastfm.scrobble.Source;
import net.roarsoftware.lastfm.PaginatedResult;

/**
 * An Open Source Platform independant Graphical Vorbis Ogg Player!<P>
 *
 * You can now listen to your Oggs using an app that is NOT bloated. In fact this
 * app/let can run on a web page (it is under 50 kB).<BR clear=left>
 * <IMG SRC="http://jOggPlayer.webarts.bc.ca/jOggPlayer.png" alt="ScreenShot"><BR>
 * <P>
 *
 * It provides the usual basics needed to enjoy your files:
 * <UL>
 *   <LI> plays files from your hard drive or streams from a URL
 *   <LI> individual file play or multiple file looping play
 *   <LI> sequential or random play
 *   <LI> in applet volume control that goes to 11!
 *   <li> visual song progrees indicator
 *   <li> displays ogg file information comments ( song name, Artist etc.)
 *   <li> unlimitted size playlist that can transparently include files and/or
 *   URLs
 *   <LI> user selectable directory recursion on file load
 *   <li> can load a file, URL or a whole directory of files on startup
 *   <li> PLUS it sounds great!
 * </UL>
 * <A HREF="http://www.webarts.bc.ca/jOggPlayer">Visit the Homepage</A> for latest
 * news and updates. <P>
 *
 * Based on the Great work done by the <A HREF="http://www.jcraft.com/jorbis/index.html">
 * jCraft crew</A> to build jOrbis - the Java implementation of the Vorbis Ogg
 * Codec by <A HREF="http://www.xiph.org/"> The XIPHOPHORUS Company</A> .<P>
 *
 * All the aforementioned work has been released under an OpenSource GNU General
 * Public License.<BR>
 * <HR> <P>
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later version.
 * <P>
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * <P>
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 675 Mass
 * Ave, Cambridge, MA 02139, USA.
 *
 * @author    Tom Gutwin P.Eng
 * @created   November 19, 2001
 */
public class jOggPlayer extends JApplet
   implements ActionListener, Runnable
{
  // Constants
  /**
   * Constant holding the users file seperator. ("/" or "\")
   */
  private final static String SYSTEM_FILE_SEPERATOR = File.separator;

  /**
   * The application name string
   **/
  private final static String APP_NAME = "jOggPlayer";

  /**
   * A Class holder for its name (used in Logging).
   **/
  private static String className_ = APP_NAME;
  /**
   * The Log Filename.
   **/
  private static String logFile_ = "." + File.separator + className_ + "Log.txt";

  /**
   * The X amount the current window was just dragged.
   **/
  private int XDifference;
  /** The Y amount the current window was just dragged. **/
  private int YDifference;
  /** the x position of the mini window. **/
  private static int xpos = -1;
  /** the y position of the mini window. **/
  private static int ypos = -1;
  /** the x position of the app window. **/
  private static int appXpos = -1;
  /** the y position of the app window. **/
  private static int appYpos = -1;

  /**
   * The dir where the images are located.
   */
  final static String IMAGE_DIR = "." + SYSTEM_FILE_SEPERATOR +
    "images" + SYSTEM_FILE_SEPERATOR;

  /**
   * Class holder for the users dir.
   */
  final static String USER_DIR = System.getProperty("user.home") +
    SYSTEM_FILE_SEPERATOR;
  /**
   * Class holder for the classpath.
   */
  static String CLASSPATH = System.getProperty("java.class.path");
  /**
   * The jOggPlayer persistant config data file.
   */
  private static String configFileName_ = USER_DIR+"."+APP_NAME+".xml";

  /**
   * The version string for this release.
   */
  final static String VERSION = "1.1.6 ($Revision: 1.30 $)";
  /**
   * The Java2 Graphics environment (getrs some platform local data).
   */
  final static GraphicsEnvironment USER_GRAPHIC_ENV =
    GraphicsEnvironment.getLocalGraphicsEnvironment();

  // Variables
  /**
   * The codebase dir when running as an application.
   */
  static URL appCodeBase_ = null;
  /**
   * The codebase dir when running as an applet.
   */
  static URL codeBase_ = null;
  /**
   * The commandline args that get passed to the app.
   */
  static String[] initArgs_ = null;
  /**
   * The instantied class.
   */
  static jOggPlayer applicationPlayer = null;
  /**
   * Description of the Field
   */
  static JarFile iconJar = null;

  // Playlist Variables
  /**
   * Description of the Field
   */
  static String playListDir_ = USER_DIR;

  // Colours
  /**
   * The colour used for all panels except the display area.
   */
  static Color mainBackColour_ = new Color(100, 180, 245);
  /**
   * The colour used to back the display area.
   */
  static Color displayBackColour_ = Color.black;
  /**
   * The display area text colour.
   */
  static Color displayTextColour_ = mainBackColour_.brighter();

  // GUI Componenet Setting Variables
  /**
   * The dismiss delay time for the song info Dispay tooltip.
   */
   static int songInfoDisplayDismissDelay_ = 8000;
  /**
   * Description of the Field
   */
  final static int maxVolScale_ = 10;
  /**
   * Description of the Field
   */
  static int left_vol_scale = maxVolScale_;
  /**
   * Description of the Field
   */
  static int right_vol_scale = left_vol_scale;
  /**
   * Description of the Field
   */
  static Dimension frameInitSize_ = null;
  /**
   * Description of the Field
   */
  static Dimension frameCurrentSize_ = null;
  /**
   * Description of the Field
   */
  static JCheckBox checkBoxRecursePlaylist =
    new JCheckBox("Recurse Directories", true);
  /**
   * Description of the Field
   */
  static JFrame appFrame_ = null;
  /**
   * Flag to show the playlist on startup, It gets read from the config file.
   **/
  static boolean showPlaylistOnStart_ = true;
  /**
   * Flag to specify if the playlist gets auto started on startup.
   **/
  static boolean autoPlayOnStart_ = true;
  /**
   * Flag to show the mini view on startup, It gets read from the config file.
   **/
  static boolean showMiniViewOnStart_  = false;
  /**
   * The Volume Slider.
   */
  static KButton muteButton_ = new KButton("");
  /** The last volume before mute.**/
  static int mutedVolume_ = 0;
  /**
   * The Volume Slider.
   */
  static JSlider volumeSlider_ = new JSlider(JSlider.VERTICAL,
    0,
    maxVolScale_ + 1,
    maxVolScale_);

  /** Flag to control use of the proxy. **/
  static private boolean areWeUsingProxy_ = true;
  /** Flag to control use of the proxy with authentication. **/
  static private boolean useProxyAuthentication_ = true;
  /** Username for the proxy authentication. **/
  static private String proxyUsername_ = "";
  /** Password for the proxy authentication. **/
  static private String proxyPassword_ = "";
  /** Host for the proxy. **/
  static private String proxyHost_ = "";
  /** Port for the proxy . **/
  static private String proxyPort_ = "";

  static String helpHtmlStr_ =
          "<HR><U>Installation</U><BR><HR>Installing is easy.<BR> Double click "+
          "the downloaded jOggPlayer-install.jar file.<BR>"+
          "OR<BR>Create a new directory" +
          " and unzip the ZIP archive into it (ie. d:\\apps\\jOggPlayer).<BR>"+
          "It is now installed.</p><BR>"+

          "<BR><BR><HR><U>Starting jOggPlayer</U><BR><HR>There are a couple of ways to start " +
          "jOggPlayer<OL><LI>Double click on jOggPlayer.jar. <BR>This works " +
          "\"out of the box\" with the windoze and Mac OS X Java VMs (UN*X " +
          "and OS/2 if you have<BR>jar files associated with the java executable).</LI>" +
          "<LI>Double Click / run one of the included jOggPlayer script files " +
          "(Un*x Shell jOggPlayer.sh, Windoze<BR>Batch jOggPlayer.bat, Windoze NT " +
          "Batch jOggPlayer_Winnt.bat, OS/2 jOggPlayer.cmd).<BR>They " +
          "should run without modifications.These allow the added feature" +
          " of letting *you* add<BR>commandline parameters; such as specifying " +
          "some ogg files or URLs to load on startup,<BR>or a directory " +
          "to search and recurse to load <B>ALL</B> ogg files.</LI>" +
          "<LI>behind a firewall... use the above script files" +
          "and add the required information that I have<BR>outlined in the usage " +
          "below. (email me if you have problems)" +
          "</LI><LI>directly run it as specified in the usage below.</LI></OL></P>" +

          "<BR><BR><HR><U>Running jOggPlayer</U><BR><HR>The most important thing to remember " +
          "is that a<BR><B>Right click (Mac: ctrl click) brings up the menu for " +
          "all features.</B><BR>Most things are straight forward. A few items I " +
          "should point out:<UL><LI>commandline parameters - you can add " +
          "filenames, URLs or directory names to load on startup</LI>" +
          "<LI>jOggPlayer now remembers your last window " +
          "location,<BR>the view size (mini view or normal view), the background " +
          "colours</LI>" +
          "<LI>config items are saved in a XML file called " +
          "'.jOggPlayer.xml' found<BR>in your user home dir. It is text readable " +
          "and easy to edit (if you want)</LI>"+
          "<LI>if you save a playlist file " +
          "called jOggPlaylist.pls to your user dir,it will auto load it on startup" +
          "</LI>"+
          "<LI>Moving the Mini-View... The title area can be clicked and dragged." +
          "</LI>"+
          "<LI>Drag &amp; Drop is available... drag &amp; drop an ogg file to "+
          "the Playlist dropdown box." +
          "</LI>"+
          "</UL></P>\n" +
          "<BR><BR><HR><U>Usage:</U><BR><HR>\n" +
          "<PRE>\n" +
          "Usage:\n" +
          "  java -classpath \"PATH_TO\\jOggPlayer.jar\" ca.bc.webarts.jOggPlayer " +
          "[file location]\n" +
          "     OR\n" +
          "  java -jar .\\jOggPlayer.jar  [file location]\n" +
          "\n" +
          "     where [file location] can be:\n" +
          "                                  a filename\n" +
          "                                  a URL to a ogg file\n" +
          "                                  a directory name to recurse " +
          "for ogg files\n\n" +
          "If you are behind a firewall and need to get access to files outside \n" +
          "the proxy you have to set some environment vars before you run the " +
          "jOggPlayer:\n" +
          "set proxyHost=myproxy.com\n" +
          "set proxyPort=8080\n" +
          "set noProxy=*.webarts.ca\n" +
          "set proxySet=true\n\n" +

          "OR on the commandline\n" +
          "Usage:\n" +
          "java -DproxySet=true -DproxyHost=YourProxyServerHostname \n"+
          "     -DproxyPort=YourProxyServerPort  \n"+
          "     -DproxyUser=YourProxyUsernameIFNEEDED  \n"+
          "     -DproxyPassword=YourProxyUserPasswordIFNEEDED  \n"+
          "     -DnonProxyHosts=CommaSeperatedlistOfNonProxiedSites  \n"+
          "     ca.bc.webarts.jOggPlayer [file/URL location]\n"
          +"</PRE>\n";

  /** The help Text Label html String **/
  static ColouredLabel helpLabel_ = new ColouredLabel(Color.lightGray,
                                                       Color.black,
                                                       helpHtmlStr_);

  /**
   * The artist Coloured text label.
   */
  static ColouredLabel artistLabel = new ColouredLabel(displayBackColour_,
    displayTextColour_,
    "Artist: ");
  /**
   * Description of the Field
   */
  static ColouredLabel titleLabel = new ColouredLabel(displayBackColour_,
    displayTextColour_,
    "Song: ");
  /**
   * Description of the Field
   */
  static ColouredLabel timeLabel = new ColouredLabel(displayBackColour_,
    displayTextColour_,
    "Time: ");
  /**
   * Description of the Field
   */
  static ColouredLabel artistLabelValue = new ColouredLabel(displayBackColour_,
    displayTextColour_,
    "None Selected");
  /**
   * Description of the Field
   */
  static ColouredLabel titleLabelValue = new ColouredLabel(displayBackColour_,
    displayTextColour_,
    "None Selected");
  /**
   * Description of the Field
   */
  static ColouredLabel timeLabelValue = new ColouredLabel(displayBackColour_,
    displayTextColour_,
    "00:00");
  /**
   * Description of the Field
   */
  static ColouredLabel miniTimeLabelValue =
    new ColouredLabel(displayBackColour_,
    displayTextColour_,
    "00:00");
  /**
   * Description of the Field
   */
  static KPanel headerPanel = new KPanel();
  /**
   * Description of the Field
   */
  static KPanel bodyPanel = new KPanel();
  /**
   * Description of the Field
   */
  static KPanel dragableMiniViewPanel = new KPanel();
  /**
   * Description of the Field
   */
  static KPanel miniViewPanel = new KPanel();
  /**
   * Description of the Field
   */
  static KPanel miniViewWindowControlPanel = new KPanel();
  /**
   * Description of the Field
   */
  static KPanel leftPanel = new KPanel();
  /**
   * Description of the Field
   */
  static KPanel innerPanel = new KPanel();
  /**
   * Description of the Field
   */
  static KPanel rightPanel = new KPanel();
  /**
   * Description of the Field
   */
  static KPanel innerHeaderPanel = new KPanel();
  /**
   * The panel used to hold all the display components.
   */
  static JPanel displayPanel = new JPanel();

  /**
   * Description of the Field
   */
  static KPanel controlPanel = new KPanel();
  /**
   * Description of the Field
   */
  static KPanel innerFooterPanel = new KPanel();
  /**
   * Description of the Field
   */
  static JPanel innerDisplayPanel = new JPanel();
  /**
   * Description of the Field
   */
  static JPanel innerLeftDisplayPanel = new JPanel();
  /**
   * Description of the Field
   */
  static JPanel innerRightDisplayPanel = new JPanel();
  /**
   * Description of the Field
   */
  static KPanel innerPlaylistPanel = new KPanel();
  /**
   * Description of the Field
   */
  static KPanel checkPanel = new KPanel();
  /**
   * Description of the Field
   */
  static KPanel buttonPanel = new KPanel();
  /**
   * Description of the Field
   */
  static KPanel miniButtonPanel = new KPanel();

  /**
   * Description of the Field
   */
  static JPopupMenu mainMenu = new JPopupMenu(APP_NAME + " Main Menu");
  /**
   * Description of the Field
   */
  static JPopupMenu playlistMenu = new JPopupMenu(APP_NAME + " Playlist Menu");
  /**
   * Description of the Field
   */
  static JPopupMenu displayMenu = new JPopupMenu(APP_NAME + " Display Area Menu");
  /**
   * Popup menu Mouse Listener.
   */
  static MouseListener popupListener_ =
    new MouseAdapter()
    {

      private void maybeShowPopup(MouseEvent e)
      {
        if (e.isPopupTrigger())
        {
          mainMenu.show(e.getComponent(), e.getX(), e.getY());
        }
      }


      public void mousePressed(MouseEvent e)
      {
        maybeShowPopup(e);
      }


      public void mouseReleased(MouseEvent e)
      {
        maybeShowPopup(e);
      }
    };
  /**
   * The File Load Play Buffer.
   */
  static int bufferMultiple_ = 4;
  /**
   * The File Load Play Buffer.
   */
  static int bufferSize_ = bufferMultiple_ * 256 * 2;
  /**
   * Description of the Field
   */
  static int convsize = bufferSize_ * 2;
  /**
   * Description of the Field
   */
  static byte[] convbuffer = new byte[convsize];
  // Flags
  /**
   * Description of the Field
   */
  static boolean running_as_applet = true;
  /**
   * Description of the Field
   */
  boolean playListShowing_ = false;
  /**
   * Description of the Field
   */
  static boolean miniViewShowing_ = false;
  /**
   * flag to control the players thread.  When this goes false this classes run method/thread ends.
   */
  boolean playing_ = false;
  /**
   * Description of the Field
   */
  boolean loopPaused_ = false;
  /**
   * Description of the Field
   */
  boolean timerRunning_ = false;
  /**
   * Description of the Field
   */
  boolean logVolumeScale_ = true;
  /**
   * Description of the Field
   */
  boolean showButtonBorders_ = false;
  /**
   * Description of the Field
   */
  boolean showTextures_ = false;
  /**
   * Small flag when a songs are looping.
   */
  boolean looping_ = false;

  //Class Threads
  /**
   * Description of the Field
   */
  Thread playerThread_ = null;
  /**
   * Description of the Field
   */
  Thread looperThread_ = null;
  /**
   * Description of the Field
   */
  double currentVolumeMultiplier_ = 1.0;
  /**
   * Description of the Field
   */
  long songStartTime_ = 0l;
  /**
   * Description of the Field
   */
  Date date = new Date();
  /**
   * Description of the Field
   */
  int progressCount_ = 0;
  /**
   * Description of the Field
   */
  String playListFilename_ = "file:///"+playListDir_ + "jOggPlaylist.pls";
  /**
   * Description of the Field
   */
  PlayList playList = null;
  /**
   * Description of the Field
   */
  Color playlistBackColour_ = new Color(190, 215, 235);
  /**
   * Description of the Field
   */
  Color buttonBackColour_ = mainBackColour_;
  /**
   * Description of the Field
   */
  Color volumeBackColour_ = new Color(224, 224, 255);
  /**
   * Description of the Field
   */
  private static final String[] DEFAULT_TEXTURE_NAMES = {"aquaMarble.gif",
    "bluesatin.jpg",
    "brownBricks.gif",
    "greenMarble.jpg"};

  /**
   * Description of the Field
   */
  String[] availableTextureNames_ = DEFAULT_TEXTURE_NAMES;

  //Fonts
  /**
   * Description of the Field
   */
  Font displayFont_ = new Font("default", Font.PLAIN, 10);
  /**
   * Description of the Field
   */
  Font displayFontBold_ = new Font("default", Font.BOLD, 10);
  /**
   * Description of the Field
   */
  int currVolSetting_ = maxVolScale_;
  /**
   * Description of the Field
   */
  int playlistHeight_ = 55;
  /**
   * Description of the Field
   */
  int innerLeftDisplayPanelHeight_ = 52;
  /**
   * Description of the Field
   */
  int innerLeftDisplayPanelWidth_ = 45;
  /**
   * Description of the Field
   */
  int innerRightDisplayPanelHeight_ = innerLeftDisplayPanelHeight_;
  /**
   * Description of the Field
   */
  int innerDisplayPanelHeight_ = innerLeftDisplayPanelHeight_;
  /**
   * Description of the Field
   */
  int innerDisplayPanelWidth_ = 240;
  /**
   * Description of the Field
   */
  int songProgressHeight_ = 8;
  /**
   * Description of the Field
   */
  int controlHeight_ = 64;
  /**
   * Description of the Field
   */
  int volumeWidth_ = 50;
  /**
   * Description of the Field
   */
  int displayHeight_ = innerDisplayPanelHeight_ + songProgressHeight_;
  /**
   * Description of the Field
   */
  int displayWidth_ = innerDisplayPanelWidth_;

  // Gui Components
  /**
   * Description of the Field
   */
  Image aboutGraphic_ = null;
  /**
   * Description of the Field
   */
  Image backgroundTexture_ = null;
  /**
   * Description of the Field
   */
  ImageIcon iconMiniView_ = new ImageIcon();
  /**
   * Description of the Field
   */
  ImageIcon iconMiniClose_ = new ImageIcon();
  /**
   * Description of the Field
   */
  ImageIcon iconDelete_ = new ImageIcon();
  /**
   * Description of the Field
   */
  ImageIcon iconAdd_ = new ImageIcon();
  /**
   * Description of the Field
   */
  ImageIcon iconAddUrl_ = new ImageIcon();
  /**
   * Description of the Field
   */
  ImageIcon iconRefresh_ = new ImageIcon();
  /**
   * Description of the Field
   */
  ImageIcon iconPlay_ = new ImageIcon();
  /**
   * Description of the Field
   */
  ImageIcon iconPause_ = new ImageIcon();
  /**
   * Description of the Field
   */
  ImageIcon iconStop_ = new ImageIcon();
  /**
   * Description of the Field
   */
  ImageIcon iconForward_ = new ImageIcon();
  /**
   * Description of the Field
   */
  ImageIcon iconBack_ = new ImageIcon();
  /**
   * Description of the Field
   */
  ImageIcon iconMiniPlay_ = new ImageIcon();
  /**
   * Description of the Field
   */
  ImageIcon iconMiniPause_ = new ImageIcon();
  /**
   * Description of the Field
   */
  ImageIcon iconMiniStop_ = new ImageIcon();
  /**
   * Description of the Field
   */
  ImageIcon iconMiniForward_ = new ImageIcon();
  /**
   * Icon For going back a song.
   */
  ImageIcon iconMiniBack_ = new ImageIcon();
  /**
   * Unmuted Icon
   */
  ImageIcon iconVolOn_ = new ImageIcon();
  /**
   * Muted Icon
   */
  ImageIcon iconVolOff_ = new ImageIcon();
  /**
   * Description of the Field
   */
  ImageIcon iconLoop_ = new ImageIcon();
  /**
   * Description of the Field
   */
  ImageIcon iconSelectedLoop = new ImageIcon();
  /**
   * Description of the Field
   */
  String backgroundTextureFilename_ = "/images/textures/blueVelvet.jpg";
  /**
   * Description of the Field
   */
  KPanel panel = new KPanel();
  /**
   * Description of the Field
   */
  KButton miniDragButton = new KButton("");
  /**
   * Description of the Field
   */
  ColouredLabel miniDragLabel = new ColouredLabel(
    displayBackColour_,
    displayTextColour_,
    new Font("Arial", Font.PLAIN, 9),
    "jOggPlayer MiniView");
  /**
   * Description of the Field
   */
  KButton buttonDeleteFromPlaylist = new KButton("");
  /**
   * Description of the Field
   */
  KButton buttonAddToPlaylist = new KButton("");
  /**
   * Description of the Field
   */
  KButton buttonAddUrlToPlaylist = new KButton("");
  /**
   * Description of the Field
   */
  KButton buttonRefreshPlaylist = new KButton("");
  /**
   * Description of the Field
   */
  KButton button = new KButton("");
  /**
   * Description of the Field
   */
  KButton buttonPause = new KButton("");
  /**
   * Description of the Field
   */
  KButton buttonLast = new KButton("");
  /**
   * Description of the Field
   */
  KButton buttonNext = new KButton("");
  /**
   * Description of the Field
   */
  KButton miniButtonPlay = new KButton("");
  /**
   * Description of the Field
   */
  KButton miniButtonPause = new KButton("");
  /**
   * Description of the Field
   */
  KButton miniButtonLast = new KButton("");
  /**
   * Description of the Field
   */
  KButton miniButtonNext = new KButton("");
  /**
   * Description of the Field
   */
  KButton miniButtonView_ = new KButton("");
  /**
   * Description of the Field
   */
  KButton miniButtonClose_ = new KButton("");
  /**
   * Description of the Field
   */
  JCheckBox checkBoxLoop = new JCheckBox("Loop");
  /**
   * Description of the Field
   */
  JCheckBox checkBoxRandom = new JCheckBox("Shuffle", true);
  /**
   * Description of the Field
   */
  JCheckBox checkBoxPlaylist = new JCheckBox("Playlist", false);
  /**
   * Description of the Field
   */
  JCheckBox checkBoxMiniView = new JCheckBox("Mini", false);
  /**
   * Description of the Field
   */
  JProgressBar songProgress = new JProgressBar();

  // Ogg File Variables
  /**
   * Description of the Field
   */
  InputStream oggBitStream_ = null;
  /**
   * Description of the Field
   */
  byte[] buffer = null;
  /**
   * Description of the Field
   */
  int bytes = 0;
  /**
   * Description of the Field
   */
  int rate = 0;
  /**
   * Description of the Field
   */
  int channels = 0;
  /**
   * Description of the Field
   */
  SourceDataLine outputLine = null;
  /**
   * Description of the Field
   */
  Vector songComments_ = new Vector();

  /** Last.FM Scrobbler Object **/
  String lastFM_key = "93df6c849563eef04d7d48db5950d0c7";
  String lastFM_userId = "";
  String lastFM_userPass = "";
  Date lastFM_songStartTime_ = null;
  ResponseStatus resp_ = null ;
  boolean lastFMConnected_  = false;
  Scrobbler sc_ = null ;


  /**
   * Listener For the select all as favourite Songs menuitem. *
   */
  ActionListener selectAllAsFavouriteSongActionListener =
    new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        playListComboBox.checkAllItems(true);
        playList.selectAllSongs();
      }
    };

  /**
   * Listener For the select all as favourite Songs menuitem. *
   */
  ActionListener deselectAllAsFavouriteSongActionListener =
    new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        playListComboBox.checkAllItems(false);
        playList.deSelectAllSongs();
      }
    };

  /**
   * This Listener gets the first event when the mini view title bar is
   * pressed so it can remember the original location of mini window.
   * This is in anticipation of a window drag
   *
   */
  MouseAdapter miniViewMouseListener =
    new MouseAdapter()
    {
      /**
       * Description of the Method
       *
       * @param e  Description of Parameter
       * @since
       */
      public void mousePressed(MouseEvent e)
      {
        XDifference = e.getX();
        YDifference = e.getY();
      }
    };

  /**
   * This Listener watches/listens where the mini window is being dragged to
   * so it can be repainted on the screen because the miniwindow does NOT
   * have a Java created titlebar so I have to mimic the window move.
   *
   */
  MouseMotionAdapter dragMiniViewMotionListener =
    new MouseMotionAdapter()
    {
      /**
       * Handles a Mouse drag
       *
       * @param e  Description of Parameter
       * @since
       */
      public void mouseDragged(MouseEvent e)
      {
        popUpWin_.setLocation(xpos + (e.getX() - XDifference),
          ypos + (e.getY() - YDifference));
        xpos = xpos + (e.getX() - XDifference);
        ypos = ypos + (e.getY() - YDifference);
      }
    };

  /**
   * Listener For the Delete Selected Song Playlist Button. *
   */
  ActionListener deleteSelectedSongActionListener =
    new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        System.out.println("Into Delete From Playlist Action");
        String selection =
          (String) ((JCheckBox) playListComboBox.getSelectedItem()).getText();
        int sel = playListComboBox.getSelectedIndex();
        if (playList.removeSong(selection))
        {
          try
          {
            playListComboBox.removeItem(selection);
          }
          catch (Exception ex)
          {
            log_.major("Exception Thrown: Delete from PlayList FAILED:" +
              selection, ex);
          }
        }
        else
        {
          log_.minor("Delete from PlayList FAILED:" + selection);
        }

        //now set the index to the song following the one deleted
        // which now has the same index number
        if (sel < playListComboBox.getNumItems())
          playListComboBox.setSelectedIndex(sel);
        else
          playListComboBox.setSelectedIndex(0);
        //playListComboBox.setText(playListComboBox.getSelectedItemName());

        playListComboBox.validate();
      }
    };

  /**
   * Listener For the Save Favourites to a file Button. *
   */
  ActionListener saveFavouritesActionListener =
    new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        System.out.println("IntoSave Favourites Action");
        ExampleFileFilter filter[] = {new ExampleFileFilter(),
          new ExampleFileFilter()};
        filter[0].addExtension("pls");
        filter[0].setDescription("Playlist Files");
        filter[1].addExtension("m3u");
        filter[1].setDescription("Winamp Playlist Files");

        String tempName = Util.chooseAFilename((Component) e.getSource(),
          playListDir_, filter, true, Util.SAVE_DIALOG);
        if (tempName != null && tempName != "")
        {
          /* 1st transfer the jComboBox Selected items to the Playlist */
          /* because the Playlist takkes care of the saving */
          playList.deSelectAllSongs();
          for (int nextIndex= 0; nextIndex < playList.size(); nextIndex++)
          {
            if (playListComboBox.isItemChecked(nextIndex))
              playList.selectSong(nextIndex,true);
          }

          playListFilename_ = tempName;
          playList.saveFavourites(playListFilename_);
        }
      }
    };

  /**
   * Listener For the Save Playlist to a file Button. *
   */
  ActionListener savePlaylistActionListener =
    new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        System.out.println("IntoSave Playlist Action");
        ExampleFileFilter filter[] = {new ExampleFileFilter(),
          new ExampleFileFilter()};
        filter[0].addExtension("pls");
        filter[0].setDescription("Playlist Files");
        filter[1].addExtension("m3u");
        filter[1].setDescription("Winamp Playlist Files");

        String tempName = Util.chooseAFilename((Component) e.getSource(),
          playListDir_, filter, true, Util.SAVE_DIALOG);
        if (tempName != null && tempName != "")
        {
          playListFilename_ = tempName;
          playList.savePlaylistFile(playListFilename_);
        }
      }
    };

  /**
   * Listener For the Delete ALL Songs from the Playlist Button. *
   */
  ActionListener deleteAllActionListener =
    new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        System.out.println("Into Delete ALL From Playlist Action");
        playList = new PlayList();
        playListComboBox.removeAllItems();
        playListComboBox.setText("Empty Playlist");
        playListComboBox.validate();
      }
    };

  /**
   * Listener For the Add a Song to the Playlist Button. *
   */
  ActionListener addSongActionListener =
    new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        System.out.println("Into Add To Playlist Action");
        ExampleFileFilter filter[] = {new ExampleFileFilter()};
        filter[0].addExtension("ogg");
        filter[0].setDescription("Ogg Song Files");

        Vector newFiles = playList.getPlaylistVector(
          Util.chooseAFilename((Component) e.getSource(), playListDir_,
          filter, true));
        String tempEntry = "";
        String s = "";
        for (int i = 0; i < newFiles.size(); i++)
        {
          tempEntry = (String) newFiles.get(i);
          if (!playList.contains(tempEntry))
          {
            // Add it to our playlist Object
            s = playList.addSong(Util.getFileBaseURL(tempEntry));

            // Now add it to the gui component
            JCheckBox chBox = new JCheckBox(s, true);
            playListComboBox.addItem(chBox);
          }
        }
          // get the index of the 1st added songname
          if (newFiles.size() >0)
          {
            int sel = playListComboBox.getJCheckBoxIndex(
              (String) newFiles.get(0));

            // Mark it as selected & update the main box
            playListComboBox.setSelectedIndex(sel);
          }
        //playListComboBox.setText(s);
        playListComboBox.validate();
      }
    };

  /**
   * Listener For the add ALL Songs from PlaylistFile Button. *
   */
  ActionListener addFromPlaylistActionListener =
    new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        System.out.println("Into Add From Playlist Action");
        ExampleFileFilter filter[] = {new ExampleFileFilter(),
          new ExampleFileFilter()};
        filter[0].addExtension("pls");
        filter[0].setDescription("Playlist Files");
        filter[1].addExtension("m3u");
        filter[1].setDescription("Winamp Playlist Files");

        String tempName = Util.chooseAFilename((Component) e.getSource(),
          playListDir_, filter, true);
        if (tempName != null && tempName != "")
        {
          URL playlistURL = null;
          playListFilename_ = tempName;
          if (running_as_applet)
          {
            try
            {
              playlistURL = new URL(getCodeBase(), playListFilename_);
            }
            catch (MalformedURLException ee)
            {
              System.out.println("The Playlist Filename has some errors. " +
                "Please check the filename:\n" + playListFilename_);
            }
          }
          else
          {
            System.out.println("Opening playlist File" +
              playListFilename_);
            playlistURL = Util.getFileBaseURL(playListFilename_);
          }
          playList.loadPlaylist(playlistURL);
          playListComboBox.removeAllItems();
          String[] playListNames = playList.getSongFileNames();
          for (int i = 0; i < playListNames.length; i++)
          {
            playListComboBox.addItem(new JCheckBox(playListNames[i], true));
          }
          // get the index of the 1st added songname
          if (playListNames.length >0)
          {
            int sel = playListComboBox.getJCheckBoxIndex(
              playListNames[0]);

            // Mark it as selected & update the main box
            playListComboBox.setSelectedIndex(sel);
          }
          //playListComboBox.setText(s);
          playListComboBox.validate();
        }
      }
    };

  /**
   * Listener For the Add a Song from a URL Button. *
   */
  ActionListener addURLActionListener =
    new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        boolean validUrl = false;
        boolean cancelled = false;
        System.out.println("Into Add URL To Playlist Action");
        String tempEntry = "";

        tempEntry = JOptionPane.showInputDialog(getParent(),
          new ColouredLabel(Color.lightGray, Color.blue,
          "Please Enter the URL to add"),
          "Add URL to Playlist",
          JOptionPane.QUESTION_MESSAGE);

        System.out.println("Adding URL " + tempEntry);
        if (tempEntry != null && tempEntry != "")
        {
          if (tempEntry.endsWith("m3u") || tempEntry.endsWith("pls"))
          {
            URL playlistURL = null;
            try
            {
              playlistURL = new URL(tempEntry);
            }
            catch (MalformedURLException ee)
            {
              System.out.println("The URL cannot be read as a Playlist. " +
                "Please check the URL:\n" + tempEntry);
            }
            playList.loadPlaylist(playlistURL);
            playListComboBox.removeAllItems();
            String[] playListNames = playList.getSongFileNames();
            for (int i = 0; i < playListNames.length; i++)
            {
              playListComboBox.addItem(new JCheckBox(playListNames[i], true));
            }
            // get the index of the 1st added songname
            if (playListNames.length >0)
            {
              int sel = playListComboBox.getJCheckBoxIndex(
                playListNames[0]);

              // Mark it as selected & update the main box
              playListComboBox.setSelectedIndex(sel);
            }
          }
          else
          {
            URL newUrl = Util.getFileBaseURL(tempEntry);
            if (newUrl != null)
            {
              if (!playList.contains(tempEntry))
              {
                String s = playList.addSong(Util.getFileBaseURL(tempEntry));
                playListComboBox.addItem(new JCheckBox(s, true));
                // get the index of the added songname
                int sel = playListComboBox.getJCheckBoxIndex(s);

                // Mark it as selected & update the main box
                playListComboBox.setSelectedIndex(sel);
              }
              else
              {
                // get the index of the added songname
                int sel = playList.findSong(newUrl);

                // Mark it as selected & update the main box
                playListComboBox.setSelectedIndex(sel);
              }
            }
          }
          playListComboBox.validate();
        }
      }
    };

  /**
   * Runs the Looping of Songs in its own thread. It simply watches the playerThread_
   * to see when it goes null and then increments the currentItemIndex and trys
   * to play the next song via tryPlay(...).
   */
  Runnable loopingRunnable_ =
    new Runnable()
    {
      int watchSleepTime = 1000;


      public void run()
      {
        final int numSongs = playListComboBox.getNumItems();
        String item;
        looping_ = true;
        int currentItemIndex;
        while (looping_ &&
               numSongs > 0 &&
               playListComboBox.getNumItemsChecked() > 0)
        {
          if (!playing_)
          {
            System.out.println("Getting indices for next looped song");
            if (checkBoxRandom.isSelected())
            {
              currentItemIndex = playListComboBox.getRandomCheckedItemNum();
            }
            else
            {
              currentItemIndex = playListComboBox.getNextCheckedItemNum();
              /*if (currentItemIndex < numSongs-1)
              {
                currentItemIndex++;
              }
              else
              {
                currentItemIndex = 0;
              }*/
            }
            if (currentItemIndex != -1)
            {
              playListComboBox.setSelectedIndex(currentItemIndex);
              item = (String) (((JCheckBox) playListComboBox.getSelectedItem()).getText());
              System.out.println("Trying to Play Next Song...");
              tryPlay(playList.getURL(item));
            }
            else
              System.out.println("NO Checked Songs to play.");
          }
          Util.sleep(watchSleepTime);
        }
      }
    };

    /**
     * The thread that watches the trime the song has been playing.
     * Handles the Timer display in its own Thread.<P>
     *
     * Why not use the Timer class in the JDK??? Because it is since JDK 1.3.
     */
    class TimerThread extends Thread
    {
      int min = 0;
      int sec = 0;
      long pausedTime = 0l;
      int pausedMin = 0;
      int pausedSec = 0;
      Date runningDate;
      long runningTime = 0l;
      int playTime = 0;
      long startPauseTime = 0l;
      long lastPauseTime = 0l;
      long newPauseTime = 0l;
      long historyPauseTime = 0l;
      int diffDate = 0;
      String timeString = "00:00";

      public int getPlayTime()
      {
        return playTime;
      }

      public void run()
      {
        date = new Date();
        timeLabelValue.setText("00:00");
        timeLabelValue.validate();
        timerRunning_ = true;
        while (playing_ && timerRunning_)
        {
          Util.sleep(490);
          runningDate = new Date();
          runningTime = runningDate.getTime();
          diffDate = (int) ((runningTime - date.getTime()));
          if (loopPaused_)
          {
            pausedTime = runningTime - startPauseTime;
            newPauseTime = runningTime - lastPauseTime;
            historyPauseTime += newPauseTime;
            pausedMin = (int) historyPauseTime / 60000;
            pausedSec = (int) (historyPauseTime / 1000 - pausedMin * 60);

            // Flash Tne Paused Message
            if (((int) (historyPauseTime / 1000)) % 2 == 0)
            {
              timeLabelValue.setText(timeString + " (PAUSED)");
              miniTimeLabelValue.setText(timeString);
              if (miniViewShowing_)
              {
                miniTimeLabelValue.validate();
              }
              else
              {
                timeLabelValue.validate();
              }
              buttonPause.setBackground(buttonBackColour_.brighter());
              buttonPause.validate();
            }
            else
            {
              timeLabelValue.setText(timeString);
              miniTimeLabelValue.setText(timeString);
              if (miniViewShowing_)
              {
                miniTimeLabelValue.validate();
              }
              else
              {
                timeLabelValue.validate();
              }
              buttonPause.setBackground(buttonBackColour_);
              buttonPause.validate();
            }
            lastPauseTime = runningTime;

          }
          else
          {
            playTime = diffDate - (int)historyPauseTime;
            min = (int) (playTime / 60000);
            sec = ((int) (playTime / 1000)) - min * 60;
            timeString = (min < 9 ? "0" : "") + min + ":" + (sec < 10 ? "0" : "") + sec;
            timeLabelValue.setText(timeString + " &nbsp; (" + progressCount_ / 1000 +
              "kB)");
            miniTimeLabelValue.setText(timeString);
            if (miniViewShowing_)
            {
              miniTimeLabelValue.validate();
            }
            else
            {
              timeLabelValue.validate();
            }
            startPauseTime = runningTime;
            lastPauseTime = runningTime;
          }
        }
      }
    };
    TimerThread timeWatcherThread_ = new TimerThread();

  /**
   * Description of the Field
   */
  JCheckDropDown playListComboBox;
  /**
   * Description of the Field
   */
  SyncState oggSyncState_;
  /**
   * Description of the Field
   */
  StreamState oggStreamState_;
  /**
   * Description of the Field
   */
  Page oggPage_;
  /**
   * Description of the Field
   */
  Packet oggPacket_;
  /**
   * Description of the Field
   */
  Info vorbisInfo;
  /**
   * Description of the Field
   */
  Comment vorbisComment;
  /**
   * Description of the Field
   */
  DspState vorbisDspState;
  /**
   * Description of the Field
   */
  Block vorbisBlock;
  /**
   * Description of the Field
   */
  int format;
  /**
   * Description of the Field
   */
  int frameSizeInBytes;
  /**
   * Description of the Field
   */
  int bufferLengthInBytes;
  /**
   * The Log that will get used.
   */
  protected static Log log_ = Log.createLog(Log.MINOR, logFile_);
  /**
   * The JWindow for the miniView.
   */
  protected static JWindow popUpWin_ = new JWindow();

  private ConfigurationManager cfgmgr_ = ConfigurationManager.getInstance();

  /**
   * Basic constructor for this Object. It is initated with no songs in the playlist.
   */
  public jOggPlayer()
  {
    //    System.out.println("Constructor1");
    final String methodName = className_ + ": Constructor()";
    log_ = Log.createLog(Log.DEBUG);
    log_.startMethod(methodName);

    /* register with last.fm  */
    if (lastFM_userId!=null && !lastFM_userId.equals(""))
      sc_ = handshakeWithLastfm(lastFM_userId);

    log_.endMethod();
  }


  /**
   * Constructor for this Object to initated the playlist with the file/songname
   * args that are passed to it, if the passed args is empty or values are invalid
   * - the default playlistfile is loaded.
   *
   * @param args  is a set of ogg file names to initially load (it can be a directory
   *      name)
   */
  public jOggPlayer(String[] args)
  {
    final String methodName = className_ + ": Constructor(String[])";
    log_ = Log.createLog(Log.DEBUG);
    log_.startMethod(methodName);

    /* register with last.fm  */
    if (lastFM_userId!=null && !lastFM_userId.equals(""))
      sc_ = handshakeWithLastfm(lastFM_userId);

    initArgs_ = args;
    log_.endMethod();
  }


  /** Registers and handshakes with last.fm
   * @return Scrobbler the registered Scrobbler OR null if failed.
  **/
  public Scrobbler handshakeWithLastfm(String userId)
  {
    Scrobbler retVal = null;
    if (userId!=null && !userId.equals(""))
    {
      String lastFM_clientId = "jog"; //"jOggPlayer";
      String lastFM_clientVersion = "1.1.6";
      //ResponseStatus resp = null ;

      retVal = Scrobbler.newScrobbler(lastFM_clientId, lastFM_clientVersion,  userId) ;
      if (retVal !=null) System.out.println("Scrobbling for Last.fm user "+userId);

      /* Handshake with lastfm */
      try
      {
        resp_ = retVal.handshake(lastFM_userPass);
        if (resp_ !=null && resp_.ok())
        {
          lastFMConnected_  = true;
          log_.debug("Handshake Succeeded for user: "+userId);
          System.out.println("Handshake Succeeded for user: "+userId);
        }
        else
        {
          if (resp_ != null)
          {
            lastFMConnected_  = false;
            if (log_!= null)
              log_.minor("Handshake Failed for user: "+userId + " - ("+
                      getLastfmErrorType(resp_.getStatus()) +") "+resp_.getMessage());
            System.out.println("Handshake Failed for user: "+userId + " - ("+
                      getLastfmErrorType(resp_.getStatus()) +") "+resp_.getMessage());
          }
        }
      }
      catch (IOException ioEx)
      {
        if (log_!= null) log_.minor("Error returned from last.fm handshake: ",ioEx);
      }
    }

    return retVal;
  }


  /** A small Lookup method to translate the Last.fm ResponseStatus code into a String
   * @return string describing the responseCode
  **/
  public static String getLastfmErrorType(int err)
  {
    String retVal = "";
    switch (err)
    {
      case ResponseStatus.BADAUTH:
        retVal = "BADAUTH";
        break;
      case ResponseStatus.BADSESSION:
        retVal = "BADSESSION";
        break;
      case ResponseStatus.BADTIME:
        retVal = "BADTIME";
        break;
      case ResponseStatus.BANNED:
        retVal = "BANNED";
        break;
      case ResponseStatus.FAILED:
        retVal = "FAILED";
        break;
      case ResponseStatus.OK:
        retVal = "OK";
    }
    return retVal;
  }


  public URL getCodeBase()
  {
    URL retVal = codeBase_;
    if (retVal == null)
      retVal = super.getCodeBase();
    return retVal;
  }


  /**
   * Sets all the Various colours to their current variable values.
   */
  private void setColours()
  {
    final String methodName = className_ + ": setColours()";
    log_.startMethod(methodName);
    int slight = 25;
    int slightlyBrighterRed = (mainBackColour_.getRed() < (256 - slight) ?
      mainBackColour_.getRed() + slight :
      255);
    int slightlyBrighterGreen = (mainBackColour_.getGreen() < (256 - slight) ?
      mainBackColour_.getGreen() + slight :
      255);
    int slightlyBrighterBlue = (mainBackColour_.getBlue() < (256 - slight) ?
      mainBackColour_.getBlue() + slight :
      255);
    displayTextColour_ = new Color(slightlyBrighterRed,
      slightlyBrighterGreen,
      slightlyBrighterBlue);
    playlistBackColour_ = mainBackColour_.brighter();
    volumeBackColour_ = displayTextColour_;
    buttonBackColour_ = mainBackColour_;
    backgroundTexture_ = Util.loadImage(backgroundTextureFilename_);

    if (!running_as_applet) appFrame_.setBackground(mainBackColour_);
    displayPanel.setBackground(displayBackColour_);
    songProgress.setBackground(displayBackColour_);
    innerDisplayPanel.setBackground(displayBackColour_);
    innerLeftDisplayPanel.setBackground(displayBackColour_);
    innerRightDisplayPanel.setBackground(displayBackColour_);
    artistLabel.setBackground(displayBackColour_);
    artistLabelValue.setBackground(displayBackColour_);
    titleLabel.setBackground(displayBackColour_);
    titleLabelValue.setBackground(displayBackColour_);
    timeLabel.setBackground(displayBackColour_);
    timeLabelValue.setBackground(displayBackColour_);
    miniTimeLabelValue.setBackground(displayBackColour_);
    innerFooterPanel.setBackground(mainBackColour_);
    buttonPanel.setBackground(buttonBackColour_);
    miniButtonPanel.setBackground(buttonBackColour_);
    miniViewWindowControlPanel.setBackground(displayBackColour_);
    controlPanel.setBackground(buttonBackColour_);
    checkPanel.setBackground(buttonBackColour_);
    checkBoxLoop.setBackground(buttonBackColour_);
    checkBoxRandom.setBackground(buttonBackColour_);
    checkBoxPlaylist.setBackground(buttonBackColour_);
    checkBoxMiniView.setBackground(buttonBackColour_);
    volumeSlider_.setBackground(volumeBackColour_);
    panel.setBackground(buttonBackColour_);
    innerPanel.setBackground(buttonBackColour_);
    songProgress.setBackground(displayBackColour_);
    songProgress.setForeground(mainBackColour_.darker());
    innerPlaylistPanel.setBackground(playlistBackColour_);
    innerPlaylistPanel.setOpaque(true);
    innerPlaylistPanel.setTexture(null);
    if (showTextures_)
    {
      //      buttonPanel.setTexture(backgroundTexture_);
      controlPanel.setTexture(backgroundTexture_);
      //      checkPanel.setTexture(backgroundTexture_);
      buttonPanel.setOpaque(false);
      miniButtonPanel.setOpaque(false);
      miniViewWindowControlPanel.setOpaque(false);
      controlPanel.setOpaque(false);
      checkPanel.setOpaque(false);
    }
    else
    {
      buttonPanel.setOpaque(true);
      miniButtonPanel.setOpaque(true);
      controlPanel.setOpaque(true);
      checkPanel.setOpaque(true);
      buttonPanel.setTexture(null);
      miniButtonPanel.setTexture(null);
      miniViewWindowControlPanel.setTexture(null);
      controlPanel.setTexture(null);
      checkPanel.setTexture(null);
    }

    artistLabel.setTextColour(displayTextColour_);
    titleLabel.setTextColour(displayTextColour_);
    timeLabel.setTextColour(displayTextColour_);
    artistLabelValue.setTextColour(displayTextColour_);
    titleLabelValue.setTextColour(displayTextColour_);
    timeLabelValue.setTextColour(displayTextColour_);
    miniTimeLabelValue.setTextColour(displayTextColour_);
    miniDragLabel.setTextColour(displayTextColour_);
    artistLabel.setBackColour(displayBackColour_);
    titleLabel.setBackColour(displayBackColour_);
    timeLabel.setBackColour(displayBackColour_);
    artistLabelValue.setBackColour(displayBackColour_);
    titleLabelValue.setBackColour(displayBackColour_);
    timeLabelValue.setBackColour(displayBackColour_);
    miniTimeLabelValue.setBackColour(displayBackColour_);

    button.setBorderPainted(showButtonBorders_);
    buttonPause.setBorderPainted(showButtonBorders_);
    buttonLast.setBorderPainted(showButtonBorders_);
    buttonNext.setBorderPainted(showButtonBorders_);
    miniButtonPlay.setBorderPainted(showButtonBorders_);
    miniButtonPause.setBorderPainted(showButtonBorders_);
    miniButtonLast.setBorderPainted(showButtonBorders_);
    miniButtonNext.setBorderPainted(showButtonBorders_);

    checkBoxRecursePlaylist.setBackground(playlistBackColour_);
    buttonDeleteFromPlaylist.setBorderPainted(showButtonBorders_);
    buttonAddToPlaylist.setBorderPainted(showButtonBorders_);
    buttonAddUrlToPlaylist.setBorderPainted(showButtonBorders_);
    buttonRefreshPlaylist.setBorderPainted(showButtonBorders_);
    log_.endMethod();
  }


  /**
   * Changes all the Labels in the Display area to the ne font. It re validates
   * the display as well.
   *
   * @param newFont  The new DisplayFont value
   */
  private void setDisplayFont(Font newFont)
  {
    final String methodName = className_ + ": setDisplayFont(Font)";
    log_.startMethod(methodName);
    displayFont_ = newFont;
    displayFontBold_ = newFont.deriveFont(Font.BOLD);
    artistLabel.setFont(newFont);
    titleLabel.setFont(newFont);
    timeLabel.setFont(newFont);
    artistLabelValue.setFont(newFont);
    titleLabelValue.setFont(newFont);
    timeLabelValue.setFont(newFont);
    miniTimeLabelValue.setFont(newFont);
    displayPanel.validate();
    log_.endMethod();
  }


  /**
   * Gets the OutputLine attribute of the jOggPlayer object
   *
   * @param channels  Description of Parameter
   * @param rate      Description of Parameter
   * @return          The OutputLine value
   */
  private SourceDataLine getOutputLine(int channels, int rate)
  {
    final String methodName = className_ + ": getOutputLine()";
    log_.startMethod(methodName);
    if (outputLine != null || this.rate != rate || this.channels != channels)
    {
      if (outputLine != null)
      {
        try
        {
          outputLine.drain();
        }
        catch (NullPointerException outputLineEx)
        {
          log_.minor("SourceDataLine NullPointerException" ,outputLineEx);
        }
        try
        {
          outputLine.stop();
        }
        catch (Exception outputLineEx)
        {
          log_.minor("SourceDataLine.stop Exception" ,outputLineEx);
        }
        try
        {
          outputLine.close();
        }
        catch (Exception outputLineEx)
        {
          log_.minor("SourceDataLine.close Exception" ,outputLineEx);
        }
      }
      init_audio(channels, rate);
      outputLine.start();
    }
    log_.endMethod();

    return outputLine;
  }


  /**
   * Loads the names of the textures available in this classes jar file.
   *
   * @return   an array of strings containing the relative paths of all textures
   *      within the jar file.
   */
  private String[] loadTextureNames()
  {
    final String methodName = className_ + ": loadTextureNames()";
    log_.startMethod(methodName);
    String[] retVal = null;
    if (running_as_applet)
      log_.debug("looking for the jOggPlayer.jar file from the " +
      "following classpath:\n" + CLASSPATH);
    String defaultJarFileName =
      Util.tokenReplace(codeBase_.getFile(),"/",File.separator)+
      File.separator+"jOggPlayer.jar";
    String jarFileName = null;
    if (defaultJarFileName != null && !defaultJarFileName.equals(""))
    {
      log_.debug("defaultJarName: "+defaultJarFileName);
      //System.out.println("defaultJarName: "+defaultJarFileName);
      jarFileName = Util.getFilePathFromClasspath("jOggPlayer.jar",
        defaultJarFileName);
    }
    else
      jarFileName = Util.getFilePathFromClasspath("jOggPlayer.jar");
    String name = null;
    try
    {
      JarFile jar = new JarFile(jarFileName, false);
      log_.debug("Jar: "+jarFileName);
      //System.out.println("Jar: "+jarFileName);
      Vector v = new Vector();
      for (Enumeration enumeration = jar.entries(); enumeration.hasMoreElements(); )
      {
        name = ((JarEntry) enumeration.nextElement()).getName();
        //System.out.print(" "+name);
        if (name.startsWith("images/textures/"))
        {
          v.add(name.substring(16));
          //System.out.print(".");
        }
      }
      //System.out.print("\n");
      Object[] o = v.toArray();
      retVal = new String[o.length];
      for (int i=0; i<o.length; i++)
        retVal[i] = (String)o[i];
    }
    catch (NullPointerException fEx)
    {
      log_.major("Jar NullPointerException " + name,fEx);
      //System.out.println("Jar NullPointerException " + name);
    }
    catch (FileNotFoundException fEx)
    {
      log_.major("Jar FileNotFoundException " + name, fEx);
      //System.out.println("Jar FileNotFoundException " + name);
    }
    catch (IOException ioEx)
    {
      log_.major("Jar IOException ",ioEx);
      //System.out.println("Jar IOException ");
    }
    catch (SecurityException secEx)
    {
      log_.major("Jar SecurityException " + name, secEx);
      //System.out.println("Jar SecurityException " + name);
    }
    log_.endMethod();

    return retVal;
  }


  /**
   * Loads / initializes the class vars for all required icons and images this
   * app/let uses.<P>
   *
   *
   * <UL>
   *   <LI> iconDelete_
   *   <LI> iconAdd_
   *   <LI> iconAddUrl_
   *   <LI> iconRefresh_
   *   <LI> iconPlay_
   *   <LI> iconPause_
   *   <LI> iconStop_
   *   <LI> iconForward_
   *   <LI> iconBack_
   *   <LI> iconLoop_
   *   <LI> aboutGraphic_
   * </UL>
   *
   */
  private void loadIconImages()
  {
    final String methodName = className_ + ": loadIconImages()";
    log_.startMethod(methodName);
    String aboutImageFilename = "/images/dont_pan.jpg";
    String miniViewFilename = "/images/miniViewButtonImage.jpg";
    String miniCloseFilename = "/images/miniCloseButtonImage.jpg";
    //String miniCloseFilename = "miniViewButtonImage.jpg";
    //String miniViewFilename = "miniViewButtonImage.jpg";
    String currImgName = aboutImageFilename;
    ImageIcon[] icons = {iconDelete_, iconAdd_, iconAddUrl_, iconRefresh_,
      iconPlay_, iconPause_, iconStop_, iconForward_, iconBack_,
      iconMiniPlay_, iconMiniPause_, iconMiniStop_,
      iconMiniForward_, iconMiniBack_, iconLoop_,
      iconSelectedLoop, iconVolOn_, iconVolOff_};
    String[] iconFileName = {"/org/javalobby/icons/16x16/DeleteDocument.gif",
      "/org/javalobby/icons/16x16/Open.gif",
      "/org/javalobby/icons/16x16/New.gif",
      "/org/javalobby/icons/16x16/NewFolder.gif",
      "/org/javalobby/icons/20x20/VCRPlay.gif",
      "/org/javalobby/icons/20x20/VCRPause.gif",
      "/org/javalobby/icons/20x20/VCRStop.gif",
      "/org/javalobby/icons/20x20/VCRForward.gif",
      "/org/javalobby/icons/20x20/VCRBack.gif",
      "/org/javalobby/icons/16x16/VCRPlay.gif",
      "/org/javalobby/icons/16x16/VCRPause.gif",
      "/org/javalobby/icons/16x16/VCRStop.gif",
      "/org/javalobby/icons/16x16/VCRForward.gif",
      "/org/javalobby/icons/16x16/VCRBack.gif",
      "/org/javalobby/icons/20x20/RotCWDown.gif",
      "/org/javalobby/icons/20x20/RotCWUp.gif",
      "/org/javalobby/icons/20x20/volOn.gif",
      "/org/javalobby/icons/20x20/volOff.gif"
      };
    String[] iconDesc = {"Remove", "Add", "Add URL", "Refresh",
      "Play", "Pause", "Stop", "Forward", "Back",
      "Play", "Pause", "Stop", "Forward", "Back",
      "Not Loop", "Looping","Mute","Un-Mute"};

    // first get the about box graphic
    aboutGraphic_ = Util.loadImage(currImgName);

    currImgName = miniCloseFilename;
    iconMiniClose_ =
      new ImageIcon(Util.loadImage(currImgName), "Close jOggPlayer");

    currImgName = miniViewFilename;
    iconMiniView_ =
      new ImageIcon(Util.loadImage(currImgName), "Toggle Mini View");

    ImageIcon tempIcon;
    // now get the button image icons
    for (int i = 0; i < icons.length; i++)
    {
      tempIcon =
        new ImageIcon(Util.loadImage(iconFileName[i]), iconDesc[i]);
      switch (i)
      {
        case 0:
          iconDelete_ = tempIcon;
          //new ImageIcon(iconBytes, iconDesc[i]);
          break;
        case 1:
          iconAdd_ = tempIcon;
          //new ImageIcon(iconBytes, iconDesc[i]);
          break;
        case 2:
          iconAddUrl_ = tempIcon;
          //new ImageIcon(iconBytes, iconDesc[i]);
          break;
        case 3:
          iconRefresh_ = tempIcon;
          //new ImageIcon(iconBytes, iconDesc[i]);
          break;
        case 4:
          iconPlay_ = tempIcon;
          //new ImageIcon(iconBytes, iconDesc[i]);
          break;
        case 5:
          iconPause_ = tempIcon;
          //new ImageIcon(iconBytes, iconDesc[i]);
          break;
        case 6:
          iconStop_ = tempIcon;
          //new ImageIcon(iconBytes, iconDesc[i]);
          break;
        case 7:
          iconForward_ = tempIcon;
          //new ImageIcon(iconBytes, iconDesc[i]);
          break;
        case 8:
          iconBack_ = tempIcon;
          //new ImageIcon(iconBytes, iconDesc[i]);
          break;
        case 9:
          iconMiniPlay_ = tempIcon;
          //new ImageIcon(iconBytes, iconDesc[i]);
          break;
        case 10:
          iconMiniPause_ = tempIcon;
          //new ImageIcon(iconBytes, iconDesc[i]);
          break;
        case 11:
          iconMiniStop_ = tempIcon;
          //new ImageIcon(iconBytes, iconDesc[i]);
          break;
        case 12:
          iconMiniForward_ = tempIcon;
          //new ImageIcon(iconBytes, iconDesc[i]);
          break;
        case 13:
          iconMiniBack_ = tempIcon;
          //new ImageIcon(iconBytes, iconDesc[i]);
          break;
        case 14:
          iconLoop_ = tempIcon;
          //new ImageIcon(iconBytes, iconDesc[i]);
          break;
        case 15:
          iconLoop_ = tempIcon;
          //new ImageIcon(iconBytes, iconDesc[i]);
          break;
        case 16:
          iconVolOn_ = tempIcon;
          //new ImageIcon(iconBytes, iconDesc[i]);
          break;
        case 17:
          iconVolOff_ = tempIcon;
          //new ImageIcon(iconBytes, iconDesc[i]);
          break;
      }
    }

    log_.endMethod();
  }


  /**
   * Initializes all the jOrbis and jOgg vars that are used for song playback.
   */
  private void init_jorbis()
  {
    final String methodName = className_ + ": init_jorbis()";
    log_.startMethod(methodName);
    oggSyncState_ = new SyncState();
    oggStreamState_ = new StreamState();
    oggPage_ = new Page();
    oggPacket_ = new Packet();
    vorbisInfo = new Info();
    vorbisComment = new Comment();
    vorbisDspState = new DspState();
    vorbisBlock = new Block(vorbisDspState);
    buffer = null;
    bytes = 0;
    oggSyncState_.init();
    log_.endMethod();
  }


  /**
   * Description of the Method
   *
   * @param channels  Description of Parameter
   * @param rate      Description of Parameter
   */
  private void init_audio(int channels, int rate)
  {
    final String methodName = className_ + ": init_audio()";
    log_.startMethod(methodName);
    try
    {
      AudioFormat audioFormat = new AudioFormat((float) rate,
        16,
        channels,
        true,  // PCM_Signed
        false // littleEndian
      );
      DataLine.Info info = new DataLine.Info(SourceDataLine.class,
        audioFormat,
        AudioSystem.NOT_SPECIFIED);
      if (!AudioSystem.isLineSupported(info))
      {
        System.out.println("Line " + info + " not supported.");
        return;
      }
      try
      {
        outputLine = (SourceDataLine) AudioSystem.getLine(info);
        outputLine.open(audioFormat);
      }
      catch (LineUnavailableException ex)
      {
        System.out.println("Unable to open the sourceDataLine: " + ex);
        System.out.println("  Type: "+info);
        System.out.println("  For: "+outputLine);
        System.out.println("  Supported Mixers: ");
        Mixer.Info [] mixInfo = AudioSystem.getMixerInfo();
        for (int i=0; i< mixInfo.length; i++)
        {
          System.out.println("    - "+mixInfo[i]);
        }
        System.out.println("\n  Supported Types: ");
        AudioFileFormat.Type [] aTypes = AudioSystem.getAudioFileTypes();
        for (int i=0; i< aTypes.length; i++)
        {
          System.out.println("    - "+aTypes[i]);
        }
        ex.printStackTrace();
        return;
      }
      catch (IllegalArgumentException ex)
      {
        System.out.println("Illegal Argument: " + ex);
        return;
      }
      frameSizeInBytes = audioFormat.getFrameSize();
      int bufferLengthInFrames = outputLine.getBufferSize() / frameSizeInBytes / 2;
      bufferLengthInBytes = bufferLengthInFrames * frameSizeInBytes;
      this.rate = rate;
      this.channels = channels;
    }
    catch (Exception ee)
    {
      System.out.println(ee);
    }
    log_.endMethod();
  }


  /**
   * Reads from the oggBitStream_ a specified number of Bytes(bufferSize_) worth
   * sarting at index and puts them in the specified buffer[].
   *
   * @param buffer
   * @param index
   * @param bufferSize_
   * @return             the number of bytes read or -1 if error.
   */
  private int readFromStream(byte[] buffer, int index, int bufferSize_)
  {
    final String methodName = className_ + ": readFromStream(byte[],int,int)";
    //log_.startMethod(methodName);
    int bytes = 0;
    try
    {
      bytes = oggBitStream_.read(buffer, index, bufferSize_);
      progressCount_ += bytes;
    }
    catch (Exception e)
    {
      System.out.println("Cannot Read Selected Song");
      log_.minor("Cannot Read Selected Song - index=" + index);
      bytes = -1;
    }
    //log_.endMethod();
    return bytes;
  }



  /**
   * Helper method to encapsulate the starting of the timeWatcherRunnable_.*
   */
  private void startTimer()
  {
    final String methodName = className_ + ": startTimer()";
    log_.startMethod(methodName);
    if (timeWatcherThread_ == null)
    {
      timeWatcherThread_ = new TimerThread();
      timeWatcherThread_.start();
    }
    else if (!timeWatcherThread_.isAlive())
    {
      timerRunning_ = false;
      timeWatcherThread_ = new TimerThread();
      timeWatcherThread_.start();
    }
    else if (!timeWatcherThread_.isAlive())
    {
      timeWatcherThread_ = new TimerThread();
      timeWatcherThread_.start();
    }
    log_.endMethod();
  }


  /**
   * Attempts to Play the Ogg File At the Specified URL. It checks the URL , updates
   * the buttons and then starts the playerThread.
   *
   * @param url  the url for the song to play
   */
  private void tryPlay(URL url)
  {
    final String methodName = className_ + ": tryPlay(URL)";
    log_.startMethod(methodName);
    Util.sleep(1000);
    oggBitStream_ = null;

    try
    {
      URLConnection urlc = url.openConnection();
      songProgress.setMaximum((urlc.getContentLength() > 0 ?
        urlc.getContentLength() : 0));
      log_.debug("Getting the Ogg Bitstream for URL="+url.toString());
      oggBitStream_ = urlc.getInputStream();
    }
    catch (Exception ee)
    {
      log_.minor("Can't get a connection to the song file",ee);
      System.err.println(ee);
    }
    if (oggBitStream_ == null)
    {
      log_.minor("Can't get a connection to the song file");
      System.out.println("Nope, No Go on the Stream");
    }
    else
    {
      log_.debug("Ogg Bitstream created: " + url.toString());
      if (!this.running_as_applet)
        appFrame_.setTitle(APP_NAME + " - " + url.toString());
      playerThread_ = new Thread(this);
      button.setIcon(iconStop_);
      miniButtonPlay.setIcon(iconMiniStop_);
      if (miniViewShowing_)
      {
        buttonPause.setEnabled(true);
      }
      else
      {
        miniButtonPause.setEnabled(true);
      }
      playing_ = true;
      playerThread_.start();
    }
    Util.sleep(1000);
    log_.endMethod();
  }


  /**
   * Attempts to Play the Ogg File At the Specified URL String. It checks the URL,
   * updates the buttons and then starts the playerThread.
   *
   * @param item  Description of Parameter
   */
  private void tryPlay(String item)
  {
    final String methodName = className_ + ": tryPlay(String)";
    log_.startMethod(methodName);
    Util.sleep(1000);
    oggBitStream_ = null;

    try
    {
      URL url = null;
      if (running_as_applet)
      {
        url = new URL(getCodeBase(), item);
      }
      else
      {
        url = new URL(item);
      }
      URLConnection urlc = url.openConnection();
      songProgress.setMaximum((urlc.getContentLength() > 0 ?
        urlc.getContentLength() : 0));
      oggBitStream_ = urlc.getInputStream();
    }
    catch (Exception ee)
    {
      if (running_as_applet)
      {
        System.err.println(ee);
      }
    }
    if (oggBitStream_ == null && !running_as_applet)
    {
      System.out.println("Trying to open file " + item);
      try
      {
        oggBitStream_ = new FileInputStream(item);
        songProgress.setMaximum((int) (new File(item)).length());
        System.out.println("Progress Max=" + songProgress.getMaximum());
      }
      catch (Exception ee)
      {
        System.err.println(ee);
      }
    }
    if (oggBitStream_ == null)
    {
      System.out.println("Nope, No Go on the Stream");
      System.out.println("Clean up Song.");
      timerRunning_ = false;
      try
      {
        if (oggBitStream_ != null)
        {
          oggBitStream_.close();
        }
        //playerThread_ = null;
        playing_ = false;
      }
      catch (Exception e)
      {
      }
      button.setIcon(iconPlay_);
      miniButtonPlay.setIcon(iconMiniPlay_);
      if (miniViewShowing_)
      {
        buttonPause.setEnabled(false);
      }
      else
      {
        miniButtonPause.setEnabled(false);
      }
    }
    else
    {
      System.out.println("Select: " + item);
      appFrame_.setTitle(APP_NAME + " - " + item);
      playerThread_ = new Thread(this);
      button.setIcon(iconStop_);
      miniButtonPlay.setIcon(iconMiniStop_);
      if (miniViewShowing_)
      {
        buttonPause.setEnabled(true);
      }
      else
      {
        miniButtonPause.setEnabled(true);
      }
      playing_ = true;
      playerThread_.start();
    }
    Util.sleep(1000);
    log_.endMethod();
  }


  /**
   * Updates the tooltip popup text for the Display area with the latest song info.
   */
  private void updateDisplayPopup()
  {
    final String methodName = className_ + ": updateDisplayPopup()";
    log_.startMethod(methodName);
    String newString = "<html><body><font face=\"Arial, Helvetica\" size=\"-2\">";
    for (int i = 0; i < songComments_.size(); i++)
    {
      newString += songComments_.get(i);
      if (i+1 < songComments_.size())
        newString += "<br>";
    }
    newString += "</font>";
//    newString += "<br>";
//    newString += playList.getSongInfo(playListComboBox.getSelectedIndex());
    newString += "</body></html>";
    displayPanel.setToolTipText(newString);
    miniTimeLabelValue.setToolTipText(newString);
    miniDragButton.setToolTipText(newString);
    log_.endMethod();
  }


  /**
   * Gets the Menus setup.
   */
  private void initMenus()
  {
    final String methodName = className_ + ": initMenus()";
    log_.startMethod(methodName);

    JMenuItem miniView = new JMenuItem("View Size Toggle");
    miniView.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          if (e.getActionCommand().equals("View Size Toggle"))
          {
            if (miniViewShowing_)
            {
              showMiniView(false);
            }
            else
            {
              showMiniView(true);
            }
          }
        }
      }
      );

    JMenu playlistMenu = new JMenu("Playlist Admin");
    JMenu favouritesMenu = new JMenu("Favourites");
    JMenuItem deselectAllFavs = new JMenuItem("De-Select All Favourite Checks");
    JMenuItem selectAllFavs = new JMenuItem("Select All Songs as Favourites");
    JMenuItem addUrl = new JMenuItem("Add From URL...");
    JMenuItem addFile = new JMenuItem("Add From File...");
    JMenuItem addPlaylistFile = new JMenuItem("Add From Playlist File...");
    JMenuItem savePlaylist = new JMenuItem("Save Playlist File...");
    JMenuItem saveFavourites = new JMenuItem("Save Favourites Playlist File...");
    JMenuItem deleteSelected = new JMenuItem("Delete Selected Song");
    JMenuItem clearAll = new JMenuItem("Delete ALL");
    favouritesMenu.add(selectAllFavs);
    favouritesMenu.add(deselectAllFavs);
    playlistMenu.add(favouritesMenu);
    playlistMenu.addSeparator();
    playlistMenu.add(addUrl);
    playlistMenu.add(addFile);
    playlistMenu.add(addPlaylistFile);
    playlistMenu.addSeparator();
    playlistMenu.add(savePlaylist);
    playlistMenu.add(saveFavourites);
    playlistMenu.addSeparator();
    playlistMenu.add(deleteSelected);
    playlistMenu.add(clearAll);
    selectAllFavs.addActionListener(selectAllAsFavouriteSongActionListener);
    deselectAllFavs.addActionListener(deselectAllAsFavouriteSongActionListener);
    deleteSelected.addActionListener(deleteSelectedSongActionListener);
    clearAll.addActionListener(deleteAllActionListener);
    addFile.addActionListener(addSongActionListener);
    addUrl.addActionListener(addURLActionListener);
    addPlaylistFile.addActionListener(addFromPlaylistActionListener);
    savePlaylist.addActionListener(savePlaylistActionListener);
    saveFavourites.addActionListener(saveFavouritesActionListener);

    JMenuItem lastfmUserMenu = new JMenuItem("Last.fm UserID ...");
    lastfmUserMenu.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          String result = JOptionPane.showInputDialog("Last.fm Username?",lastFM_userId);

          if(result != null)
          {
            lastFM_userId = result;
            log_.debug("Last.fm userID is now="+lastFM_userId);
          }
          result = JOptionPane.showInputDialog("Last.fm Password?",lastFM_userPass);

          if(result != null)
          {
            lastFM_userPass = result;
            log_.debug("Last.fm userPassword is now="+lastFM_userPass);
          }
        }
      }
      );

    JMenuItem bufferSizeMenu = new JMenuItem("Set Vorbis Buffer Size...");
    bufferSizeMenu.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          Object options[] = new Object[6];
          options[0] = "256";
          options[1] = "512";
          options[2] = "1024";
          options[3] = "2048";
          options[4] = "4096";
          options[5] = "8192";

          Object result = JOptionPane.showInputDialog(getParent(),
            null, "Vorbis buffer size (Bytes)",
            JOptionPane.QUESTION_MESSAGE,
            null, options, options[2]);

          if(result != null)
          {
            bufferMultiple_ = (Integer.parseInt((String)result))/256;
            bufferSize_ = bufferMultiple_ * 256 * 2;
            log_.debug("BufferMultiple is now="+bufferMultiple_);
          }
        }
      }
      );

    JMenuItem debugLevelMenu = new JMenuItem("Set Debug Logging Level...");
    debugLevelMenu.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          Object options[] = new Object[6];
          options[0] = "NONE";
          options[1] = "QUIET";
          options[2] = "MINOR";
          options[3] = "MAJOR";
          options[4] = "DEBUG";
          options[5] = "FULL";

          Object result = JOptionPane.showInputDialog(getParent(),
            null, "Debug Logging Level",
            JOptionPane.QUESTION_MESSAGE,
            null, options, options[0]);

          if(result != null)
          {
            short level = -1;
            if ("NONE".equals(result)) level = Log.NONE;
            else if ("DEBUG".equals(result)) level = Log.DEBUG;
            else if ("MINOR".equals(result)) level = Log.MINOR;
            else if ("MAJOR".equals(result)) level = Log.MAJOR;
            else if ("FULL".equals(result)) level = Log.FULL;
            else if ("QUIET".equals(result)) level = Log.QUIET;
            if (level != -1)
            {
              log_.debug("Setting Log Level to "+level);
              log_.setLogLevel(level);
            }
            log_.debug("Logging Level is now="+log_.getLogLevel());
          }
        }
      }
      );

    JMenuItem autoPlayMenu = new JMenuItem("Auto Start Song Play On Startup...");
    autoPlayMenu.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          Object options[] = new Object[2];
          options[0] = "Yes";
          options[1] = "No";

          Object result = JOptionPane.showInputDialog(getParent(),
            null, "Auto Play on startup?",
            JOptionPane.QUESTION_MESSAGE,
            null, options, options[0]);

          if(result != null)
          {
            if ("Yes".equals(result)) autoPlayOnStart_ = true;
            else if ("No".equals(result)) autoPlayOnStart_ = false;
          }
        }
      }
      );

    JMenu fontChoiceMenu = new JMenu("Set Display Font");
    String[] availableFontNames =
      USER_GRAPHIC_ENV.getAvailableFontFamilyNames();
    for (int i = 0; i < availableFontNames.length; i++)
    {
      JMenu fontChoice = new JMenu(availableFontNames[i]);
      JMenuItem size8 = new JMenuItem("8");
      JMenuItem size9 = new JMenuItem("9");
      JMenuItem size10 = new JMenuItem("10");
      JMenuItem size11 = new JMenuItem("11");
      JMenuItem size12 = new JMenuItem("12");
      JMenuItem size13 = new JMenuItem("13");
      JMenuItem size14 = new JMenuItem("14");
      if (!availableFontNames[i].startsWith("V") &&
        !availableFontNames[i].startsWith(".") &&
        !availableFontNames[i].endsWith("1.1"))
      {
        size8.addActionListener(
          new ActionListener()
          {
            public void actionPerformed(ActionEvent e)
            {
              JMenu myParent = (JMenu)
                ((JPopupMenu)
                ((JMenuItem) e.getSource()).getParent()).getInvoker();
              int mySize = Integer.parseInt(e.getActionCommand());
              setDisplayFont(new Font(myParent.getText(),
                Font.PLAIN,
                mySize));
            }
          }
          );
        size9.addActionListener(
          new ActionListener()
          {
            public void actionPerformed(ActionEvent e)
            {
              JMenu myParent = (JMenu)
                ((JPopupMenu)
                ((JMenuItem) e.getSource()).getParent()).getInvoker();
              int mySize = Integer.parseInt(e.getActionCommand());
              setDisplayFont(new Font(myParent.getText(),
                Font.PLAIN,
                mySize));
            }
          }
          );
        size10.addActionListener(
          new ActionListener()
          {
            public void actionPerformed(ActionEvent e)
            {
              JMenu myParent = (JMenu)
                ((JPopupMenu)
                ((JMenuItem) e.getSource()).getParent()).getInvoker();
              int mySize = Integer.parseInt(e.getActionCommand());
              setDisplayFont(new Font(myParent.getText(),
                Font.PLAIN,
                mySize));
              System.out.println("Setting FONT = " + myParent.getText() + "  " +
                mySize);
            }
          }
          );
        size11.addActionListener(
          new ActionListener()
          {
            public void actionPerformed(ActionEvent e)
            {
              JMenu myParent = (JMenu)
                ((JPopupMenu)
                ((JMenuItem) e.getSource()).getParent()).getInvoker();
              int mySize = Integer.parseInt(e.getActionCommand());
              setDisplayFont(new Font(myParent.getText(),
                Font.PLAIN,
                mySize));
            }
          }
          );
        size12.addActionListener(
          new ActionListener()
          {
            public void actionPerformed(ActionEvent e)
            {
              JMenu myParent = (JMenu)
                ((JPopupMenu)
                ((JMenuItem) e.getSource()).getParent()).getInvoker();
              int mySize = Integer.parseInt(e.getActionCommand());
              setDisplayFont(new Font(myParent.getText(),
                Font.PLAIN,
                mySize));
            }
          }
          );
        size13.addActionListener(
          new ActionListener()
          {
            public void actionPerformed(ActionEvent e)
            {
              JMenu myParent = (JMenu)
                ((JPopupMenu)
                ((JMenuItem) e.getSource()).getParent()).getInvoker();
              int mySize = Integer.parseInt(e.getActionCommand());
              setDisplayFont(new Font(myParent.getText(),
                Font.PLAIN,
                mySize));
            }
          }
          );
        size14.addActionListener(
          new ActionListener()
          {
            public void actionPerformed(ActionEvent e)
            {
              JMenu myParent = (JMenu)
                ((JPopupMenu)
                ((JMenuItem) e.getSource()).getParent()).getInvoker();
              int mySize = Integer.parseInt(e.getActionCommand());
              setDisplayFont(new Font(myParent.getText(),
                Font.PLAIN,
                mySize));
            }
          }
          );
        fontChoice.add(size8);
        fontChoice.add(size9);
        fontChoice.add(size10);
        fontChoice.add(size11);
        fontChoice.add(size12);
        fontChoice.add(size13);
        fontChoice.add(size14);
        fontChoiceMenu.add(fontChoice);
      }
    }

    JMenuItem backColourChange = new JMenuItem("Set Background Colour...");
    backColourChange.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          if (e.getActionCommand().equals("Set Background Colour..."))
          {
            Color newColour = JColorChooser.showDialog(appFrame_,
              "New Background Colour",
              mainBackColour_);
            mainBackColour_ = newColour;
            showTextures_ = false;
            setColours();
            appFrame_.validate();
          }
        }
      }
      );

    JMenu textureChoiceMenu = new JMenu("Set Background Texture");
    if (availableTextureNames_==null)
      availableTextureNames_ = DEFAULT_TEXTURE_NAMES;
    for (int i = 0; i < availableTextureNames_.length; i++)
    {
      JMenuItem textureChoice = new JMenuItem(availableTextureNames_[i]);
      textureChoice.addActionListener(
        new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
            backgroundTextureFilename_ = "/images/textures/" +
              e.getActionCommand();
            System.out.println("Setting Background to : "+
              backgroundTextureFilename_);
            showTextures_ = true;
            setColours();
            //           controlPanel.invalidate();
            appFrame_.paintComponents(appFrame_.getGraphics());
          }
        }
        );
      textureChoiceMenu.add(textureChoice);
    }

    JMenuItem aboutBox = new JMenuItem("About jOggPlayer...");
    aboutBox.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          if (e.getActionCommand().equals("About jOggPlayer..."))
          {
            JAboutBox a = new JAboutBox(aboutGraphic_,
              "About jOggPlayer",
              "<B>jOggPlayer</B><BR>GUI Enhanced Pure Java Ogg Vorbis player.",
              VERSION, helpHtmlStr_);
          }
        }
      }
      );

    JMenuItem helpWindow = new JMenuItem("Help...");
    helpWindow.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          if (e.getActionCommand().equals("Help..."))
          {
            Object [] options = {"Done"};
            JEditorPane htmlPane = new JEditorPane("text/html",
                           "<html><body><A name=\"Top\"></A>" +
                            jOggPlayer.helpHtmlStr_+
                            "</body></html>");
            htmlPane.setEditable(false);

            //CalHTMLPane calPane = new CalHTMLPane();

            JScrollPane htmlScrollPane = new JScrollPane();
            htmlScrollPane.getViewport().add(htmlPane);
            htmlScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            htmlScrollPane.setPreferredSize(new Dimension(640, 480));
            // ensure the top of the help info is visible
            htmlPane.scrollRectToVisible(new Rectangle(0,0));
            htmlScrollPane.getViewport().scrollRectToVisible(new Rectangle(0,0));

            JOptionPane pane = new JOptionPane(
                            (Object) htmlScrollPane,
                            JOptionPane.INFORMATION_MESSAGE,
                            JOptionPane.DEFAULT_OPTION,
                            new ImageIcon(),
                            options) ;
            JDialog dialog = pane.createDialog(getParent(), "jOggPlayer Help");
            dialog.show();
            // ensure the top of the help info is visible
            //htmlPane.scrollRectToVisible(
            //  new Rectangle(0,0));
            /*calPane.showHTMLDocument(
                           "<html><body><A name=\"Top\"></A>" +
                            jOggPlayer.helpHtmlStr_+
                            "</body></html>");
            calPane.scrollToReference("Top",null);
            calPane.requestFocus();*/
          }
        }
      }
      );

    JMenu optionsMenu = new JMenu("Options");

    mainMenu.add(playlistMenu);
    mainMenu.addSeparator();
    if(!this.running_as_applet)
    {
      mainMenu.add(miniView);
    }
    optionsMenu.add(bufferSizeMenu);
    optionsMenu.add(debugLevelMenu);
    optionsMenu.add(autoPlayMenu);
    optionsMenu.add(lastfmUserMenu);
    optionsMenu.addSeparator();
    optionsMenu.add(fontChoiceMenu);
    optionsMenu.add(textureChoiceMenu);
    optionsMenu.add(backColourChange);
    mainMenu.add(optionsMenu);
    mainMenu.addSeparator();
    mainMenu.add(helpWindow);
    mainMenu.add(aboutBox);
    log_.endMethod();
  }


  /**
   * Switches the Playlist panel on/off.
   *
   * @param showIt  specifies whether to view or not.
   */
  private void showPlaylistPanel(boolean showIt)
  {
    final String methodName = className_ + ": showPlaylistPanel(" + showIt + ")";
    log_.startMethod(methodName);
    if (showIt)
    {
      log_.debug("PlayListcomboBox has " + playListComboBox.getItemCount() +
        " songs.");
      String tempTitle = playListComboBox.getSelectedItemName();
      if (tempTitle == null || tempTitle.equals("") )
      {
        tempTitle = "Empty Playlist";
      }
      //else
        //System.out.println("PlayListcomboBox has " + tempTitle +
          //" selected.");
      playListComboBox.setText(tempTitle);
      frameCurrentSize_ = appFrame_.getSize();
      Dimension innerFooterSize = innerFooterPanel.getPreferredSize();
      playListShowing_ = true;
      appFrame_.setResizable(true);
      panel.add(innerFooterPanel, BorderLayout.SOUTH);
      appFrame_.setSize((int) (frameCurrentSize_.getWidth()),
        (int) (frameCurrentSize_.getHeight()
         + innerFooterSize.getHeight()));
      innerFooterPanel.invalidate();
      appFrame_.validate();
      //appFrame_.setResizable(false);
    }
    else
    {
      Dimension innerFooterSize = innerFooterPanel.getPreferredSize();
      frameCurrentSize_ = appFrame_.getSize();
      playListShowing_ = false;
      appFrame_.setResizable(true);
      panel.remove(innerFooterPanel);
      appFrame_.setSize((int) (frameCurrentSize_.getWidth()),
        (int) (frameCurrentSize_.getHeight()
         - innerFooterSize.getHeight()));
      appFrame_.validate();
      //appFrame_.setResizable(false);
    }
    log_.endMethod();
  }


  /**
   * Switches to or from the mini view.
   *
   * @param showIt  specifies whether to go to mini view or not.
   */
  private static void showMiniView(boolean showIt)
  {
    final String methodName = className_ + ": showMiniView(" + showIt + ")";
    log_.startMethod(methodName);
    /*
     *  Show or Hide the playlist
     */
    if (showIt)
    {
      appFrame_.setVisible(false);
      popUpWin_.pack();
      popUpWin_.setLocation(xpos, ypos);
      popUpWin_.setVisible(true);
      popUpWin_.toFront();
      miniViewShowing_ = true;
    }
    else
    {
      popUpWin_.setVisible(false);
      appFrame_.setVisible(true);
      appFrame_.toFront();
      miniViewShowing_ = false;
    }
    log_.endMethod();
  }


  /**
   * Gets the GUI setup *
   */
  private void initUI()
  {
    final String methodName = className_ + ": initUI()";
    log_.startMethod(methodName);
    initMenus();
    FileDrop.Listener fDropListener =
      new FileDrop.Listener()
      {
        public void filesDropped(File[] files)
        {
          // handle file drop
          System.out.println("Dropped " + files.length + " Files");
          String tempEntry = null;
          for (int i = 0; i < files.length; i++)
          {
            tempEntry = files[i].getAbsolutePath();
            if (!playList.contains(tempEntry))
            {
              String s = playList.addSong(Util.getFileBaseURL(tempEntry));
              JCheckBox chBox = new JCheckBox(s, true);

              playListComboBox.addItem(chBox);
            }
          }
          playListComboBox.validate();
        } // end filesDropped
      };

    panel.setLayout(new BorderLayout(0, 0));
    //popUpWin_.getContentPane().setLayout(
    //  new BoxLayout(popUpWin_,BoxLayout.X_AXIS));
    headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));
    bodyPanel.setLayout(new BorderLayout(0, 0));
    dragableMiniViewPanel.setLayout(new BorderLayout(0, 0));
    miniViewPanel.setLayout(new BoxLayout(miniViewPanel, BoxLayout.X_AXIS));
    /* we don't want the mini Label wrapping, so BoxLayout*/
    miniDragLabel.setLayout(new BoxLayout(miniDragLabel, BoxLayout.X_AXIS));
    leftPanel.setLayout(new BorderLayout(0, 0));
    innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
    miniViewWindowControlPanel.setLayout(new BorderLayout(0, 0));
    //new BoxLayout(miniViewWindowControlPanel, BoxLayout.X_AXIS));
    //rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
    innerHeaderPanel.setLayout(new BoxLayout(innerHeaderPanel,
      BoxLayout.X_AXIS));
    displayPanel.setLayout(new BorderLayout(0, 0));
    innerDisplayPanel.setLayout(new BorderLayout(0, 0));
    controlPanel.setLayout(new BorderLayout(0, 0));
    innerFooterPanel.setLayout(new BorderLayout());

    //checkPanel.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));//BoxLayout(checkPanel, BoxLayout.X_AXIS));
    checkPanel.setLayout(new BoxLayout(checkPanel, BoxLayout.X_AXIS));
    innerLeftDisplayPanel.setLayout(new GridLayout(0, 1));
    innerRightDisplayPanel.setLayout(new GridLayout(0, 1));
    /*
     *  innerPlaylistPanel.setLayout(new BoxLayout(innerPlaylistPanel,
     *  BoxLayout.X_AXIS));
     */
    displayPanel.setPreferredSize(new Dimension(displayWidth_, displayHeight_));
    innerDisplayPanel.setPreferredSize(
      new Dimension(innerDisplayPanelWidth_,
      innerDisplayPanelHeight_));
    innerLeftDisplayPanel.setPreferredSize(
      new Dimension(innerLeftDisplayPanelWidth_,
      displayHeight_ - songProgressHeight_));
    //innerRightDisplayPanel.setPreferredSize(
    //new Dimension(displayWidth_ - innerLeftDisplayPanelWidth_,
    //displayHeight_));
    //displayPanel.setBorder(BorderFactory.createBevelBorder((BevelBorder.LOWERED)));
    artistLabel.setFont(displayFontBold_);
    titleLabel.setFont(displayFontBold_);
    timeLabel.setFont(displayFontBold_);
    artistLabelValue.setFont(displayFont_);
    titleLabelValue.setFont(displayFont_);
    timeLabelValue.setFont(displayFont_);
    miniTimeLabelValue.setFont(displayFont_);

    artistLabel.setVerticalAlignment(ColouredLabel.TOP);
    titleLabel.setVerticalAlignment(ColouredLabel.TOP);
    timeLabel.setVerticalAlignment(ColouredLabel.TOP);
    artistLabelValue.setVerticalAlignment(ColouredLabel.TOP);
    titleLabelValue.setVerticalAlignment(ColouredLabel.TOP);
    timeLabelValue.setVerticalAlignment(ColouredLabel.TOP);
    miniTimeLabelValue.setVerticalAlignment(ColouredLabel.TOP);
    miniDragLabel.setVerticalAlignment(ColouredLabel.BOTTOM);

    popUpWin_.getContentPane().setBackground(displayBackColour_);

    playListComboBox = new JCheckDropDown(playList.getSongFileNames());
    String currTitle = null;
    if (playListComboBox.getNumItems() > 0)
    {
      playListComboBox.setSelectedIndex(0);
    }
    else
    {
      playListComboBox.setText("Empty Playlist");
    }

    songProgress.setPreferredSize(
      new Dimension(displayWidth_ - 20,
      songProgressHeight_));
    //songProgress.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
    volumeSlider_.setToolTipText("Volume: " + maxVolScale_);

    volumeSlider_.setBorder(BorderFactory.createBevelBorder(
      BevelBorder.LOWERED));
    volumeSlider_.setPaintLabels(true);
    volumeSlider_.setPaintTicks(true);
    volumeSlider_.setMajorTickSpacing(maxVolScale_ / 2);
    volumeSlider_.setMinorTickSpacing(maxVolScale_ / 10);
    volumeSlider_.setSnapToTicks(true);
    volumeSlider_.setPreferredSize(
      new Dimension(volumeWidth_, displayHeight_ + controlHeight_-20));
    controlPanel.setPreferredSize(
      new Dimension(displayWidth_, controlHeight_));
    //innerPlaylistPanel.setPreferredSize(
    //           new Dimension(displayWidth_ + volumeWidth_, playlistHeight_));
    //button = new KButton("");
    button.setIcon(iconPlay_);
    button.setPreferredSize(
      new Dimension(iconPlay_.getIconWidth() + 5, iconPlay_.getIconHeight() + 5));
    button.setToolTipText("Play Current Song In Playlist");
    button.addActionListener(this);
    button.setBorder(BorderFactory.createBevelBorder(
      BevelBorder.RAISED));

    miniButtonPlay.setIcon(iconMiniPlay_);
    miniButtonPlay.setPreferredSize(
      new Dimension(iconMiniPlay_.getIconWidth(), iconMiniPlay_.getIconHeight()));
    miniButtonPlay.setToolTipText("Play Current Song In Playlist");
    miniButtonPlay.addActionListener(this);
    miniButtonPlay.setBorder(BorderFactory.createBevelBorder(
      BevelBorder.RAISED));

    muteButton_.setToolTipText("Mute The Volume.");
    muteButton_.setIcon(iconVolOn_);
    //muteButton_.setBorder(new javax.swing.border.EmptyBorder(0,0,0,0));
    muteButton_.setBorder(BorderFactory.createBevelBorder(
      BevelBorder.LOWERED));

    buttonLast.addActionListener(this);
    buttonLast.setIcon(iconBack_);
    buttonLast.setPreferredSize(
      new Dimension(iconBack_.getIconWidth() + 5, iconBack_.getIconHeight() + 5));
    buttonLast.setToolTipText("Play Previous Song In Playlist");
    buttonLast.setBorder(BorderFactory.createBevelBorder(
      BevelBorder.RAISED));

    miniButtonLast.addActionListener(this);
    miniButtonLast.setIcon(iconMiniBack_);
    miniButtonLast.setPreferredSize(
      new Dimension(iconMiniBack_.getIconWidth(), iconMiniBack_.getIconHeight()));
    miniButtonLast.setToolTipText("Play Previous Song In Playlist");
    miniButtonLast.setBorder(BorderFactory.createBevelBorder(
      BevelBorder.RAISED));

    buttonNext.addActionListener(this);
    buttonNext.setIcon(iconForward_);
    buttonNext.setPreferredSize(
      new Dimension(iconForward_.getIconWidth() + 5, iconForward_.getIconHeight() + 5));
    buttonNext.setToolTipText("Play Next Song In Playlist");
    buttonNext.setBorder(BorderFactory.createBevelBorder(
      BevelBorder.RAISED));

    miniButtonNext.addActionListener(this);
    miniButtonNext.setIcon(iconMiniForward_);
    miniButtonNext.setPreferredSize(
      new Dimension(iconMiniForward_.getIconWidth(), iconMiniForward_.getIconHeight()));
    miniButtonNext.setToolTipText("Play Next Song In Playlist");
    miniButtonNext.setBorder(BorderFactory.createBevelBorder(
      BevelBorder.RAISED));

    buttonPause.addActionListener(this);
    buttonPause.setIcon(iconPause_);
    buttonPause.setPreferredSize(
      new Dimension(iconPause_.getIconWidth() + 5, iconPause_.getIconHeight() + 5));
    buttonPause.setToolTipText("Pause Play");
    buttonPause.setEnabled(false);
    buttonPause.setBorder(BorderFactory.createBevelBorder(
      BevelBorder.RAISED));

    miniButtonPause.addActionListener(this);
    miniButtonPause.setIcon(iconMiniPause_);
    miniButtonPause.setPreferredSize(
      new Dimension(iconMiniPause_.getIconWidth(), iconMiniPause_.getIconHeight()));
    miniButtonPause.setToolTipText("Pause Play");
    miniButtonPause.setEnabled(false);
    miniButtonPause.setBorder(BorderFactory.createBevelBorder(
      BevelBorder.RAISED));

    buttonAddUrlToPlaylist.setIcon(iconAddUrl_);
    buttonAddUrlToPlaylist.setToolTipText("Add A URL Song To The Playlist");
    buttonAddUrlToPlaylist.setPreferredSize(
      new Dimension(iconAddUrl_.getIconWidth() + 5, iconAddUrl_.getIconHeight() + 5));
    buttonAddUrlToPlaylist.setBorder(BorderFactory.createBevelBorder(
      BevelBorder.RAISED));

    buttonAddToPlaylist.setIcon(iconAdd_);
    buttonAddToPlaylist.setToolTipText("Add A Song File To The Playlist");
    buttonAddToPlaylist.setPreferredSize(
      new Dimension(iconAdd_.getIconWidth() + 5, iconAdd_.getIconHeight() + 5));
    buttonAddToPlaylist.setBorder(BorderFactory.createBevelBorder(
      BevelBorder.RAISED));

    buttonDeleteFromPlaylist.setIcon(iconDelete_);
    buttonDeleteFromPlaylist.setToolTipText("Remove A Song From The Playlist");
    buttonDeleteFromPlaylist.setPreferredSize(
      new Dimension(iconDelete_.getIconWidth() + 5, iconDelete_.getIconHeight() + 5));
    buttonDeleteFromPlaylist.setBorder(BorderFactory.createBevelBorder(
      BevelBorder.RAISED));

    buttonRefreshPlaylist.setIcon(iconRefresh_);

    buttonRefreshPlaylist.setToolTipText("Refresh the Playlist from the Directory");
    buttonRefreshPlaylist.setPreferredSize(
      new Dimension(iconRefresh_.getIconWidth() + 5, iconRefresh_.getIconHeight() + 5));
    buttonRefreshPlaylist.setBorder(BorderFactory.createBevelBorder(
      BevelBorder.RAISED));

    miniButtonView_.addActionListener(this);
    miniButtonView_.setIcon(iconMiniView_);
    miniButtonView_.setToolTipText("Toggle Mini View");
    miniButtonView_.setPreferredSize(new Dimension(10, 10));

    miniButtonClose_.addActionListener(this);
    miniButtonClose_.setIcon(iconMiniClose_);
    miniButtonClose_.setToolTipText("Close jOggPlayer");
    miniButtonClose_.setPreferredSize(
      new Dimension(iconMiniClose_.getIconWidth(), iconMiniClose_.getIconHeight()));

    //miniDragButton.addActionListener(dragMiniViewMotionListener);

    checkBoxLoop.setSelected(true);
    checkBoxLoop.setBorderPainted(false);
    checkBoxLoop.setContentAreaFilled(false);
    checkBoxLoop.setToolTipText("Automatically proceed to next song.");

    checkBoxRandom.setBorderPainted(false);
    checkBoxRandom.setContentAreaFilled(false);
    checkBoxRandom.setToolTipText("Play the playlist songs in a random order.");

    checkBoxMiniView.setBorderPainted(false);
    checkBoxMiniView.setContentAreaFilled(false);
    checkBoxMiniView.setToolTipText("Show the MiniView.");

    checkBoxPlaylist.setBorderPainted(false);
    checkBoxPlaylist.setContentAreaFilled(false);
    checkBoxPlaylist.setToolTipText("Show the playlist panel.");
    checkBoxRecursePlaylist.setToolTipText("Recurse all subdirectories of the " +
      "selected directory to find Oggs.");

    //miniDragLabel.
    //miniDragButton.

    buttonDeleteFromPlaylist.addActionListener(
      deleteSelectedSongActionListener);
    buttonAddToPlaylist.addActionListener(addSongActionListener);
    buttonAddUrlToPlaylist.addActionListener(addURLActionListener);

    buttonRefreshPlaylist.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          System.out.println("Into Refresh Playlist Action");
          String[] playListFileNames = PlayList.getPlaylistArray(initArgs_);
          playList = new PlayList(Util.getFileBaseURLs(playListFileNames));
          playList.sortSongs();
          playListComboBox.removeAllItems();
          String[] playListNames = playList.getSongFileNames();
          for (int i = 0; i < playListNames.length; i++)
          {
            playListComboBox.addItem(new JCheckBox(playListNames[i], true));
          }
          playListComboBox.validate();
        }
      }
      );

    muteButton_.addActionListener(
      new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          if (mutedVolume_ != 0)
          {
            volumeSlider_.setValue(mutedVolume_);
            mutedVolume_ = 0;
            volumeSlider_.setEnabled(true);
            muteButton_.setToolTipText("Mute The Volume.");
            muteButton_.setIcon(iconVolOn_);
          }
          else
          {
            mutedVolume_ = volumeSlider_.getValue();
            volumeSlider_.setValue(0);
            volumeSlider_.setEnabled(false);
            muteButton_.setToolTipText("Un-Mute The Volume.");
            muteButton_.setIcon(iconVolOff_);
          }
        }
      }
    );

    volumeSlider_.addChangeListener(
      new ChangeListener()
      {
        public void stateChanged(ChangeEvent e)
        {
          /*
           *  volume Adjust
           */
          currVolSetting_ = volumeSlider_.getValue();
          currentVolumeMultiplier_ = volumeMultiplier(currVolSetting_);
          if (currVolSetting_ == 11)
          {
            volumeSlider_.setToolTipText("<HTML><BODY>" +
              "Volume: This Player Goes To <B>11</B>" +
              "</body></html>");
          }
          else
          {
            volumeSlider_.setToolTipText("<HTML><BODY>" +
              "Volume: " + currVolSetting_ +
              "</body></html>");
          }
        }
      });

    checkBoxPlaylist.addChangeListener(
      new ChangeListener()
      {
        public void stateChanged(ChangeEvent e)
        {
          /*
           *  Show or Hide the playlist
           */
          if (checkBoxPlaylist.isSelected() && !playListShowing_)
          {
            showPlaylistPanel(true);
          }
          else if (!checkBoxPlaylist.isSelected() && playListShowing_)
          {
            showPlaylistPanel(false);
          }
        }
      });

    checkBoxMiniView.addChangeListener(
      new ChangeListener()
      {
        public void stateChanged(ChangeEvent e)
        {
          /*
           *  Show or Hide the miniView
           */
          if (checkBoxMiniView.isSelected() && !miniViewShowing_)
          {
            showMiniView(true);
            checkBoxMiniView.setSelected(false);
          }
          //else if ( checkBoxMiniView.isSelected() && miniViewShowing_ )
          //{
          //  showMiniView(false);
          //  checkBoxMiniView.setSelected(false);
          //}
        }
      });

    MouseAdapter myButtonMouseAdapter =
      new MouseAdapter()
      {
        public void mouseEntered(MouseEvent e)
        {
          JButton but = (JButton) e.getSource();
          if (but.isEnabled())
          {
            but.setBorderPainted(true);
            but.validate();
          }
        }

        public void mouseExited(MouseEvent e)
        {
          JButton but = (JButton) e.getSource();
          if (but.isEnabled())
          {
            but.setBorderPainted(false);
            but.validate();
          }
        }

        public void mousePressed(MouseEvent e)
        {
          JButton but = (JButton) e.getSource();
          if (but.isEnabled())
          {
            but.setBorder(BorderFactory.createBevelBorder(
              BevelBorder.LOWERED));
            but.validate();
          }
        }

        public void mouseReleased(MouseEvent e)
        {
          JButton but = (JButton) e.getSource();
          if (but.isEnabled())
          {
            but.setBorder(BorderFactory.createBevelBorder(
              BevelBorder.RAISED));
            but.validate();
          }
        }
      };

    buttonRefreshPlaylist.addMouseListener(myButtonMouseAdapter);
    buttonAddUrlToPlaylist.addMouseListener(myButtonMouseAdapter);
    buttonAddToPlaylist.addMouseListener(myButtonMouseAdapter);
    buttonDeleteFromPlaylist.addMouseListener(myButtonMouseAdapter);
    button.addMouseListener(myButtonMouseAdapter);
    buttonPause.addMouseListener(myButtonMouseAdapter);
    buttonNext.addMouseListener(myButtonMouseAdapter);
    buttonLast.addMouseListener(myButtonMouseAdapter);
    miniButtonPlay.addMouseListener(myButtonMouseAdapter);
    miniButtonPause.addMouseListener(myButtonMouseAdapter);
    miniButtonNext.addMouseListener(myButtonMouseAdapter);
    miniButtonLast.addMouseListener(myButtonMouseAdapter);

    /*
     *  panel is the Main Holder Panel
     */
    /*
     *  It gets Split into 3 components Top, Body, Bottom
     */
    /*
     *  (bottom is for mini-layout
     */
    panel.add(headerPanel, BorderLayout.NORTH);
    panel.add(bodyPanel, BorderLayout.CENTER);
    //panel.add(miniViewPanel, BorderLayout.SOUTH); //holds the mini-view layout
    // not initially shown

    /*
     *  The Body holds the volume and the main area
     */
    bodyPanel.add(leftPanel, BorderLayout.WEST);
    bodyPanel.add(innerPanel, BorderLayout.CENTER);
    //    bodyPanel.add(rightPanel);

    leftPanel.add(volumeSlider_, BorderLayout.CENTER);
    leftPanel.add(muteButton_, BorderLayout.SOUTH);

    /*
     *  innerPanel holds the display area and the controls
     */
    innerPanel.add(innerHeaderPanel);
    innerPanel.add(displayPanel);
    innerPanel.add(controlPanel);

    /*
     *  The Following holds the Playlist and is controlled by a checkbox
     */
    //    innerPanel.add(Box.createVerticalGlue());
    //    innerPanel.add(innerFooterPanel);

    displayPanel.add(innerDisplayPanel, BorderLayout.CENTER);
    displayPanel.add(songProgress, BorderLayout.SOUTH);
    displayPanel.addMouseListener(popupListener_);

    controlPanel.add(buttonPanel, BorderLayout.CENTER);
    controlPanel.add(checkPanel, BorderLayout.SOUTH);

    innerDisplayPanel.add(innerLeftDisplayPanel, BorderLayout.WEST);
    innerDisplayPanel.add(innerRightDisplayPanel, BorderLayout.CENTER);
    //innerDisplayPanel.add(Box.createGlue());


    innerLeftDisplayPanel.add(artistLabel);
    innerLeftDisplayPanel.add(titleLabel);
    //innerLeftDisplayPanel.add(Box.createVerticalStrut(5));
    innerLeftDisplayPanel.add(timeLabel);

    innerRightDisplayPanel.add(artistLabelValue);
    innerRightDisplayPanel.add(titleLabelValue);
    //innerRightDisplayPanel.add(Box.createVerticalStrut(5));
    innerRightDisplayPanel.add(timeLabelValue);

    /*
     *  Button Panel uses the default FlowLayout
     */
    buttonPanel.add(buttonLast);
    buttonPanel.add(buttonPause);
    buttonPanel.add(button);
    buttonPanel.add(buttonNext);

    /*
     *  MINI Button Panel uses the default FlowLayout
     */
    miniButtonPanel.add(miniButtonLast);
    miniButtonPanel.add(miniButtonPause);
    miniButtonPanel.add(miniButtonPlay);
    miniButtonPanel.add(miniButtonNext);
    //log_.debug("Adding checkBoxMiniView");
    //buttonPanel.add(checkBoxMiniView);

    /*
     *  this uses a FlowLayout so everything stays in a line
     */

    checkPanel.add(checkBoxLoop);
    checkPanel.add(checkBoxRandom);
    checkPanel.add(checkBoxPlaylist);
    if(!this.running_as_applet) checkPanel.add(checkBoxMiniView);

    /*
     *  this is the playlist panel area
     */
    KPanel tempP = new KPanel(new BorderLayout(0, 0));
    tempP.add(playListComboBox, BorderLayout.NORTH);
    innerFooterPanel.add(tempP, BorderLayout.SOUTH);
    innerFooterPanel.add(innerPlaylistPanel, BorderLayout.NORTH);
    innerFooterPanel.addMouseListener(popupListener_);
    tempP.addMouseListener(popupListener_);

    innerPlaylistPanel.add(checkBoxRecursePlaylist);
    innerPlaylistPanel.add(buttonDeleteFromPlaylist);
    innerPlaylistPanel.add(buttonAddToPlaylist);
    innerPlaylistPanel.add(buttonAddUrlToPlaylist);
    innerPlaylistPanel.add(buttonRefreshPlaylist);

    /*
     * Mini View Window dressing
     */
    miniDragButton.setMaximumSize(new Dimension(500, 16));
    miniDragButton.setBorder(BorderFactory.createRaisedBevelBorder());
    //new emptyBorder(0,0,0,0));
    //miniDragButton.setBorder(new EmptyBorder(0,0,0,0));
    miniDragButton.setBorderPainted(true);
    miniDragButton.add(miniDragLabel);
    //miniDragButton.setPreferredSize(new Dimension(64,16));
    miniButtonView_.setPreferredSize(new Dimension(16, 16));
    miniButtonClose_.setPreferredSize(new Dimension(16, 16));
    miniButtonView_.setBorder(new EmptyBorder(0,0,0,0));
    miniButtonClose_.setBorder(new EmptyBorder(0,0,0,0));
    miniButtonView_.setBorderPainted(false);
    miniButtonClose_.setBorderPainted(false);
    KPanel miniViewClosePanel = new KPanel();
    miniViewClosePanel.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
      //new BoxLayout(miniViewClosePanel, BoxLayout.X_AXIS));
    miniViewClosePanel.setBorder(new EmptyBorder(0,0,0,0));
    miniViewClosePanel.add(miniButtonView_);
    miniViewClosePanel.add(miniButtonClose_);
    miniViewWindowControlPanel.setBorder(new EmptyBorder(0,0,0,0));
    miniViewWindowControlPanel.add(miniDragButton, BorderLayout.CENTER);
    miniViewWindowControlPanel.add(miniViewClosePanel, BorderLayout.EAST);
    miniViewPanel.add(miniTimeLabelValue);
    miniViewPanel.add(miniButtonPanel);
    //miniViewPanel.add(miniViewWindowControlPanel);
    miniViewPanel.setDoubleBuffered(true);
    miniDragButton.setDoubleBuffered(true);
    dragableMiniViewPanel.setDoubleBuffered(true);
    miniTimeLabelValue.setDoubleBuffered(true);
    miniViewPanel.setDoubleBuffered(true);
    miniViewWindowControlPanel.setDoubleBuffered(true);
    miniButtonClose_.setDoubleBuffered(true);
    miniButtonView_.setDoubleBuffered(true);
    miniTimeLabelValue.addMouseListener(popupListener_);
    miniViewPanel.addMouseListener(popupListener_);
    miniDragButton.addMouseListener(miniViewMouseListener);
    miniDragButton.addMouseMotionListener(dragMiniViewMotionListener);
    dragableMiniViewPanel.add(miniViewWindowControlPanel, BorderLayout.CENTER);
    dragableMiniViewPanel.add(miniViewPanel, BorderLayout.SOUTH);
    popUpWin_.getContentPane().add(dragableMiniViewPanel);

    // Add a listener to the display area so we can deleay the tool tip
    // dismiss delay time
    MouseAdapter myDisplayMouseAdapter =
      new MouseAdapter()
      {
        int prevDismissDelay = 200;
        ToolTipManager toolTipMgr = ToolTipManager.sharedInstance();
        public void mouseEntered(MouseEvent e)
        {
          // last lets slow down the ToolTipText dismissDelay
          // so the user can read it
          prevDismissDelay = toolTipMgr.getDismissDelay();
          toolTipMgr.setDismissDelay(songInfoDisplayDismissDelay_);
        }

        public void mouseExited(MouseEvent e)
        {
          // now put it back to the normal delay for all the other components
          toolTipMgr.setDismissDelay(prevDismissDelay);
        }
      };
    displayPanel.addMouseListener(myDisplayMouseAdapter);
    miniTimeLabelValue.addMouseListener(myDisplayMouseAdapter);
    miniDragButton.addMouseListener(myDisplayMouseAdapter);

    new FileDrop(playListComboBox, fDropListener);

    if(!running_as_applet && showPlaylistOnStart_)
    {
      showPlaylistPanel(true);
      checkBoxPlaylist.setSelected(true);
    }
    else
    {
      checkBoxPlaylist.setSelected(false);
    }
    setColours();

    log_.endMethod();
  }


  private void autoPlay()
  {
    if (!playList.isEmpty())
    {
      int currentItemIndex = playListComboBox.getSelectedIndex();
      int numSongs = playListComboBox.getItemCount();
      if (currentItemIndex < 0)
        currentItemIndex = 0;
      button.setIcon(iconStop_);
      buttonPause.setEnabled(true);
      button.setToolTipText("Stop Current Song");
      miniButtonPlay.setIcon(iconMiniStop_);
      miniButtonPause.setEnabled(true);
      miniButtonPlay.setToolTipText("Stop Current Song");
      log_.debug("\nAuto Play Starting");
      System.out.println("Auto Play Starting");
      if (checkBoxLoop.isSelected())
      {
        log_.debug("\nLooping Songs");
        System.out.println("\nLooping "+numSongs+" Songs");
        //checkBoxLoop.setEnabled(false);
        looping_ = true;
        if (currentItemIndex > 0)
        {
          currentItemIndex--;
        }
        else
        {
          currentItemIndex = numSongs - 1;
        }
        playListComboBox.setSelectedIndex(currentItemIndex);
        String item = (String) (((JCheckBox) playListComboBox.
          getSelectedItem()).getText());
        //log_.debug("\nPlay Button on item " +currentItemIndex + " " +item);
        playing_ = false; //playerThread_ = null;
        looperThread_ = new Thread(loopingRunnable_);
        looperThread_.start();
      }
      else
      {
        JCheckBox selectedCheckbox = (JCheckBox)playListComboBox.getSelectedItem();
        if (selectedCheckbox != null)
        {
          String item = (String) (selectedCheckbox.getText());
          System.out.println("Trying to Auto Play Song"+item);
          tryPlay(playList.getURL(item));
        }
      }
    }
  }

  /**
   *  Does the gruntwork to get the proxy properties set.
   **/

  public void initProxy()
  {
    // Set up the proxy settings if needed
    Properties sysProps = System.getProperties();
    String tmpUseproxy  = sysProps.getProperty("proxySet");
    if (tmpUseproxy != null && !tmpUseproxy.equals(""))
      areWeUsingProxy_ = (tmpUseproxy.toLowerCase().equals("true")?true:false);

    if (areWeUsingProxy_)
    {
      System.out.println("Initializing the proxy settings");
      // check if the user had something defined in the Sys Props via the cmdline.
      String tmpUser  = sysProps.getProperty("http.proxyUser");
      String tmpPass  = sysProps.getProperty("http.proxyPassword");
      String tmpHost  = sysProps.getProperty("http.proxyHost");
      String tmpPort  = sysProps.getProperty("http.proxyPort");

      if (tmpUser != null && !tmpUser.equals(""))
        proxyUsername_ = tmpUser;
      if (tmpPass != null && !tmpPass.equals(""))
        proxyPassword_ = tmpPass;
      if (tmpHost != null && !tmpHost.equals(""))
        proxyHost_ = tmpHost;
      if (tmpPort != null && !tmpPort.equals(""))
        proxyPort_ = tmpHost;

      if (useProxyAuthentication_)
      {
        System.out.println("Authenticating the proxy.");
        Authenticator.setDefault(new FirewallAuthenticator(proxyUsername_,
                                                           proxyPassword_));
      }
      sysProps.put("proxySet", "true");
      sysProps.put("http.proxyHost", proxyHost_);
      sysProps.put("http.proxyPort", proxyPort_);
      sysProps.put("noProxy", "");
    }

  }


  /**
   * The main program for the jOggPlayer class
   *
   * @param arg  The command line arguments
   */
  public static void main(String[] arg)
  {
    System.out.println("Starting jOggPlayer with logging going to:" +logFile_);
    log_ = Log.createLog(Log.MINOR, logFile_);
    String splashGraphic = "/images/dont_pan.jpg";
    Image splashImage = Util.loadImage(splashGraphic);
    Panel p = new Panel();
    /*
     *  p.setBorder(new CompoundBorder(
     *  new MatteBorder(1,1,1,1,Color.black),
     *  new EmptyBorder(12,12,12,12)));
     */
    p.setBackground(new Color(210, 210, 210));
    Label l = new Label("Please wait while loading.");
    p.add(l);
    Splash frame = new Splash(splashImage, 5000, p);
    //Splash splash = new Splash(splashGraphic, 5000, (Panel)p);

    appCodeBase_ = Util.getFileBaseURL(USER_DIR);
    appFrame_ = new JFrame();
    Util.setIconForApp(appFrame_,"/images/jOggPlayerIcon.jpg");
    appFrame_.getContentPane().setLayout(new BorderLayout());
    appFrame_.setTitle(APP_NAME + " - No Songs Currently Playing");
//    appFrame_.setIconImage(new ImageIcon(IMAGE_DIR+"Applet24.gif","jOggPlayer Player");
    //Add listener to components that can bring up popup menus.
    //appFrame_.addMouseListener(popupListener_);
    applicationPlayer = new jOggPlayer(arg);
    applicationPlayer.running_as_applet = false;
    running_as_applet = false;
    applicationPlayer.init();
    applicationPlayer.start();
    appFrame_.addWindowListener(
      new WindowAdapter()
      {
        public void windowClosing(WindowEvent e)
        {
          applicationPlayer.stop();
          applicationPlayer.log_.close();
          applicationPlayer = null;
          System.exit(0);
        }
      });

    appFrame_.getContentPane().add(applicationPlayer.panel);
    appFrame_.pack();
    if(xpos == -1)
    {
      KiwiUtils.centerWindow(appFrame_);
      appXpos = appFrame_.getX();
      appYpos = appFrame_.getY();
    }
    else
    {
      appFrame_.setLocation(appXpos,appYpos);
    }
    if (showMiniViewOnStart_)
      showMiniView(true);
    else
    {
      appFrame_.toFront();
      frame.finishSplash();
      appFrame_.setVisible(true);
      frameInitSize_ = appFrame_.getSize();
      frameCurrentSize_ = frameInitSize_;
      //appFrame_.setResizable(false);
      appFrame_.repaint();
    }


    // now start a song if requested to AutoStart
    if (autoPlayOnStart_)
    {
      applicationPlayer.autoPlay();
    }
    //log_.endMethod();
  }


  /**
   * A helper method that initializes the required class vars. This gets called
   * by this class running as an applet.
   */
  public void init()
  {
    final String methodName = className_ + ": init";
    log_.startMethod(methodName);
    System.out.println("Init...");

    String s = null;
    String cbStr = null;
    initProxy();

    // Load applet parms
    if(running_as_applet)
    {
      log_.debug("Loading applet parms:");
      s = getParameter("jOggPlayer.playlist");
      cbStr = getParameter("jOggPlayer.codebase");
      log_.debug("Playlist:"+s);
      log_.debug("codebase:"+cbStr);
    }
    else
    {
      cbStr = "file:////" + USER_DIR;
    }
    // Load up the config parms from the ini file
    // first of all we load an existing file
    File configFile = new File(configFileName_);
    if (configFile.exists())
    {
      // load the file
      try
      {
        cfgmgr_.load(configFile);
        loadConfigs();
      }
      catch (Exception e)
      {
        // ups, something went wrong and we have to stop here
        log_.minor("Can't Load config file.",e);
      }
    }
    else
    {
      // Create a new Config file
      createConfigs();
    }

    if (cbStr != null)
      try
      {
       codeBase_ = new URL(cbStr);
      }
      catch (MalformedURLException ee)
      {
        log_.minor("codeBase_ crap out", ee);
      }
    if (s != null)
    {
      playListFilename_ = s;
    }

    String[] playListFileNames = PlayList.getPlaylistArray(initArgs_);
    System.out.print("Loading Playlist with "+playListFileNames.length+" songs...");
    playList = new PlayList(Util.getFileBaseURLs(playListFileNames));
    playList.sortSongs();
    System.out.println(" DONE!");
    if (playList.isEmpty())
    {
      URL playlistURL = null;
      if (running_as_applet)
      {
        URL cb = null;
        try
        {
          cb = getCodeBase();
          playlistURL = new URL(playListFilename_);
        }
        catch (MalformedURLException ee)
        {
          log_.minor("The Playlist Filename has some errors. " +
            "Please check the filename:\n" + playListFilename_, ee);
        }
        catch (Exception ex)
        {
          log_.minor("Error Obtaining codebase()",ex);
        }
      }
      else
      {
        System.out.println("Opening playlist File" +
          playListFilename_);
        playlistURL = Util.getFileBaseURL(playListFilename_);
      }
      if (playlistURL != null)
        playList.loadPlaylist(playlistURL);
    }
    if (loadTextureNames() != null)
      availableTextureNames_ = loadTextureNames();
    loadIconImages();
    initUI();
    panel.addMouseListener(popupListener_);
    getContentPane().add(panel);

    /* Now handshake with last.fm */
    if (lastFM_userId!=null && !lastFM_userId.equals(""))
      sc_ = handshakeWithLastfm(lastFM_userId);

    log_.endMethod();
  }


  /**
   * The applet start *
   */
  public void start()
  {
    final String methodName = className_ + ": start";
    log_.startMethod(methodName);
    System.out.println("Start...");
    setVisible(true);
    repaint();

    log_.endMethod();
  }

  /**
   * The Runnable to do the actual playing of the song. All the rest of this class
   * is fluff to get to this point [:)] . This code was developed by the jCraft
   * group.
   *
   */
  public void run()
  {
    final String methodName = className_ + ": run()";
    log_.startMethod(methodName);
    init_jorbis();
    Thread me = Thread.currentThread();
    progressCount_ = 0;
    int index = 0;
    boolean streamStillHasData = true;
    while (playing_ && streamStillHasData)
    {
      int eos = 0;
      System.out.println("Init file...");

      index = oggSyncState_.buffer(bufferSize_);
      buffer = oggSyncState_.data;
      bytes = readFromStream(buffer, index, bufferSize_);
      if (bytes == -1)
      {
        streamStillHasData = false;
        System.out.println("Cannot get any data from selected Ogg bitstream.");
        log_.minor("Cannot get any data from selected Ogg bitstream. ");
        break;
      }
      oggSyncState_.wrote(bytes);
      if (oggSyncState_.pageout(oggPage_) != 1)
      {
        streamStillHasData = false;
        if (bytes < bufferSize_)
        {
          log_.debug("No more data");
          break;
        }
        log_.minor("Input does not appear to be an Ogg bitstream.");
        System.out.println("Input does not appear to be an Ogg bitstream.");
        break;
      }
      oggStreamState_.init(oggPage_.serialno());
      vorbisInfo.init();
      vorbisComment.init();
      if (oggStreamState_.pagein(oggPage_) < 0)
      {
        // error; stream version mismatch perhaps
        log_.minor("Error reading first page of Ogg bitstream data.");
        System.out.println("Error reading first page of Ogg bitstream data.");
        break;
      }
      int tmpInt = oggStreamState_.packetout(oggPacket_);
      if (tmpInt != 1)
      {
        // no page? must not be vorbis
        log_.minor("Error reading initial header packet. ("+tmpInt + ")");
        System.out.println("Error reading initial header packet. ("+tmpInt + ")");
        break;
      }
      tmpInt = vorbisInfo.synthesis_headerin(vorbisComment, oggPacket_);
      if (tmpInt < 0)
      {
        // error case; not a vorbis header
        log_.minor("This Ogg bitstream does not contain Vorbis audio data. ("+tmpInt + ")");
        System.out.println("This Ogg bitstream does not contain Vorbis audio data. ("+tmpInt + ")");
        break;
      }
      System.out.println("Valid Ogg bitstream.");
      log_.debug("Valid Ogg bitstream.");

      int i = 0;
      while (i < 2)
      {
        while (i < 2)
        {
          int result = oggSyncState_.pageout(oggPage_);
          if (result == 0)
          {
            break;
          } // Need more data
          if (result == 1)
          {
            oggStreamState_.pagein(oggPage_);
            while (i < 2)
            {
              result = oggStreamState_.packetout(oggPacket_);
              if (result == 0)
              {
                break;
              }
              if (result == -1)
              {
                log_.minor("Corrupt secondary header.  Exiting.");
                return;
              }
              vorbisInfo.synthesis_headerin(vorbisComment, oggPacket_);
              i++;
            }
          }
        }
        index = oggSyncState_.buffer(bufferSize_);
        buffer = oggSyncState_.data;
        bytes = readFromStream(buffer, index, bufferSize_);
        if (bytes == -1)
        {
          break;
        }
        if (bytes == 0 && i < 2)
        {
          log_.minor("End of file before finding all Vorbis  headers!");
          return;
        }
        oggSyncState_.wrote(bytes);
      }

      byte[][] ptr = vorbisComment.user_comments;
      String currComment = "";
      int sel = playListComboBox.getSelectedIndex();
      String tmpUrlStr = playList.getSongUrl(sel).toString();
      int endIndx = tmpUrlStr.toString().lastIndexOf('/');
      int startIndx = tmpUrlStr.toString().indexOf('/')+1;
      String urlBase = tmpUrlStr.substring(startIndx,endIndx);
      artistLabelValue.setText(urlBase);
      String selSongName = playList.getSongName(sel);
      titleLabelValue.setText(selSongName);
      miniDragLabel.setText(selSongName);
      songComments_.clear();
      for (int j = 0; j < ptr.length; j++)
      {
        if (ptr[j] == null)
        {
          break;
        }
        currComment = (new String(ptr[j], 0, ptr[j].length - 1)).trim();
        songComments_.add(currComment);

        String tmpStr = playList.getSongArtist(sel);
        if (!tmpStr.equals("")) artistLabelValue.setText(tmpStr);
        tmpStr = playList.getSongTitle(sel);
        if (!tmpStr.equals(""))
        {
          titleLabelValue.setText(tmpStr);
          miniDragLabel.setText(tmpStr);
        }

        if (currComment.toUpperCase().startsWith("ARTIST"))
        {
          artistLabelValue.setText(currComment.substring(7));
          playList.setArtist(sel,currComment.substring(7));
        }
        else if (currComment.toUpperCase().startsWith("TITLE"))
        {
          titleLabelValue.setText(currComment.substring(6));
          miniDragLabel.setText(currComment.substring(6));
          playList.setTitle(sel,currComment.substring(7));
        }
        //System.out.println("Comment: " + currComment);
      }
      displayPanel.validate();
      //currComment = "Bitstream: " + vorbisInfo.channels + " channel," + vorbisInfo.rate + "Hz";
      currComment = "Bitstream: " + playList.getSongChannels(sel) + " channel," +
                     playList.getSongBitrate(sel) + "Hz";
      songComments_.add(currComment);
      //System.out.println(currComment);
      currComment = "Encoded by: " + new String(vorbisComment.vendor, 0,
        vorbisComment.vendor.length - 1);
      songComments_.add(currComment);
      //System.out.println(currComment);
      updateDisplayPopup();
      convsize = bufferSize_ / vorbisInfo.channels;
      vorbisDspState.synthesis_init(vorbisInfo);
      vorbisBlock.init(vorbisDspState);
      double[][][] _pcm = new double[1][][];
      float[][][] _pcmf = new float[1][][];
      int[] _index = new int[vorbisInfo.channels];
      getOutputLine(vorbisInfo.channels, vorbisInfo.rate);
      startTimer();

      /* got all the headers and comments so start Playing the song */
      lastFM_songStartTime_=new Date();
      if (lastFMConnected_)
      {
        try
        {
        /*  Tell last.fm Whats currently Playing */
        System.out.println("Sending Playing Song Info to Last.fm: "+playList.getSongArtist(sel)+" "+
                               playList.getSongTitle(sel)+" "+
                               playList.getSongAlbum(sel)+" 0 "+
                               playList.getTrackNumber(sel));
        resp_ = sc_.nowPlaying(playList.getSongArtist(sel),
                               playList.getSongTitle(sel),
                               playList.getSongAlbum(sel),
                               0,
                               playList.getTrackNumber(sel));
        if (resp_ !=null && resp_.ok())
        {
          log_.debug("Sending nowPlaying Succeeded for user: "+lastFM_userId);
          System.out.println("Sending nowPlaying Succeeded for user: "+lastFM_userId);
        }
        else
        {
          if (resp_ != null)
          {
            log_.minor("Sending nowPlaying Failed for user: "+lastFM_userId + " - ("+
                      getLastfmErrorType(resp_.getStatus()) +") "+resp_.getMessage());
            System.out.println("Re-Sending nowPlaying Failed for user: "+lastFM_userId + " - ("+
                      getLastfmErrorType(resp_.getStatus()) +") "+resp_.getMessage());
            sc_ = handshakeWithLastfm(lastFM_userId);
            System.out.println("Re-Sending Playing Song Info to Last.fm: "+playList.getSongArtist(sel)+" "+
                                   playList.getSongTitle(sel)+" "+
                                   playList.getSongAlbum(sel)+" 0 "+
                                   playList.getTrackNumber(sel));
            resp_ = sc_.nowPlaying(playList.getSongArtist(sel),
                                   playList.getSongTitle(sel),
                                   playList.getSongAlbum(sel),
                                   0,
                                   playList.getTrackNumber(sel));
            if (resp_ !=null && resp_.ok())
            {
              log_.debug("Re-Sending nowPlaying Succeeded for user: "+lastFM_userId);
              System.out.println("Re-Sending nowPlaying Succeeded for user: "+lastFM_userId);
            }
          }
        }

        }
        catch (IOException ioEx)
        {
          log_.minor("Could NOT send now playing to last.fm",ioEx);
        }
      }

      while (eos == 0)
      {
        while (eos == 0)
        {
          if (!playing_ || playerThread_ == null)
          {
            System.err.println("bye.");
            timerRunning_ = false;
            try
            {
              //outputLine.drain();
              //outputLine.stop();
              //outputLine.close();
              oggBitStream_.close();
            }
            catch (Exception ee)
            {
            }
            button.setIcon(iconPlay_);
            miniButtonPlay.setIcon(iconMiniPlay_);
            if (miniViewShowing_)
            {
              buttonPause.setEnabled(false);
            }
            else
            {
              miniButtonPause.setEnabled(false);
            }

            /* Last.fm Song Submit */
            if (lastFMConnected_)
            {
              try
              {
                //int playTime = (new Long(((new Date()).getTime() - lastFM_songStartTime_.getTime())/1000)).intValue();
                int playTime =  timeWatcherThread_.getPlayTime()/1000;
                if (playTime > 30 && ( songProgress.getPercentComplete() >0.50 || playTime >240))
                {
                  System.out.println("Submitting: "+ playList.getSongArtist(sel)+" "+
                                     playList.getSongTitle(sel)+" "+playList.getSongAlbum(sel)+
                                     " played for:"+playTime+
                                     "sec. at " + lastFM_songStartTime_.getTime()/1000);
                  resp_ = sc_.submit(playList.getSongArtist(sel),
                                     playList.getSongTitle(sel),
                                     playList.getSongAlbum(sel),
                                     playTime,
                                     playList.getTrackNumber(sel),
                                     Source.USER,
                                     lastFM_songStartTime_.getTime()/1000);
                  if (resp_!=null && resp_.ok())
                  {
                    System.out.println("Song Sumbitted");
                  }
                  else
                  {
                    if (resp_ != null)
                    {
                      System.out.println("Sumbmit Failed --> (" +getLastfmErrorType(resp_.getStatus()) +") "+resp_.getMessage());
                      sc_ = handshakeWithLastfm(lastFM_userId);
                      System.out.println("Re-Submitting: "+ playList.getSongArtist(sel)+" "+
                                         playList.getSongTitle(sel)+" "+playList.getSongAlbum(sel)+
                                         " played for:"+playTime+
                                         "sec. at " + lastFM_songStartTime_.getTime()/1000);
                      resp_ = sc_.submit(playList.getSongArtist(sel),
                                         playList.getSongTitle(sel),
                                         playList.getSongAlbum(sel),
                                         playTime,
                                         playList.getTrackNumber(sel),
                                         Source.USER,
                                         lastFM_songStartTime_.getTime()/1000);
                      if (resp_!=null && resp_.ok())
                      {
                        System.out.println("Song Sumbitted");
                      }
                    }
                  }
                }
                else
                {
                  System.out.println("Song NOT submitted because it was too short or did not play long enough");
                  log_.debug("Song NOT submitted because it was too short or did not play long enough");
                }
              }
              catch (IOException ioEx)
              {
                log_.minor("Last.fm submit failed --> (" +getLastfmErrorType(resp_.getStatus()) +") "+resp_.getMessage() ,ioEx);
              }
            }
            /* Last.fm Song Submit */

            return;
          }
          int result = oggSyncState_.pageout(oggPage_);
          if (result == 0)
          {
            break;
          } // need more data
          if (result == -1)
          { // missing or corrupt data at this page position
            System.err.println("Corrupt or missing data in bitstream; " +
              "continuing...");
          }
          else
          {
            oggStreamState_.pagein(oggPage_);

            /* This is the endless Play song loop */
            /* ---------------------------------- */
            while (playing_)
            {
              result = oggStreamState_.packetout(oggPacket_);
              if (result == 0)
              {
                break;
              } // need more data
              if (result == -1)
              { // missing or corrupt data at this page position
                // no reason to complain; already complained above
              }
              else
              {
                // we have a packet.  Decode it
                int samples;
                if (vorbisBlock.synthesis(oggPacket_) == 0)
                { // test for success!
                  vorbisDspState.synthesis_blockin(vorbisBlock);
                }
                while ((samples = vorbisDspState.synthesis_pcmout(_pcmf, _index)) > 0)
                {
                  double[][] pcm = _pcm[0];
                  float[][] pcmf = _pcmf[0];
                  boolean clipflag = false;
                  int bout = (samples < convsize ? samples : convsize);
                  double fVal = 0.0;
                  // convert doubles to 16 bit signed ints (host order) and
                  // interleave
                  for (i = 0; i < vorbisInfo.channels; i++)
                  {
                    int pointer = i * 2;
                    //int ptr=i;
                    int mono = _index[i];
                    for (int j = 0; j < bout; j++)
                    {
                      fVal = (float) pcmf[i][mono + j] * 32767.;
                      /*
                       *  volume Adjust
                       */
                      fVal = fVal * currentVolumeMultiplier_;
                      int val = (int) (fVal);
                      if (val > 32767)
                      {
                        val = 32767;
                        clipflag = true;
                      }
                      if (val < -32768)
                      {
                        val = -32768;
                        clipflag = true;
                      }
                      if (val < 0)
                      {
                        val = val | 0x8000;
                      }
                      convbuffer[pointer] = (byte) (val);
                      convbuffer[pointer + 1] = (byte) (val >>> 8);
                      pointer += 2 * (vorbisInfo.channels);
                    }
                  }
                  outputLine.write(convbuffer, 0, 2 * vorbisInfo.channels * bout);
                  vorbisDspState.synthesis_read(bout);
                }
                //Update the Progress Bar
                songProgress.setValue(progressCount_);
                while (loopPaused_)
                {
                  Util.sleep(200);
                }
              }
            }
            if (oggPage_.eos() != 0)
            {
              eos = 1;
            }
          }
        }
        if (eos == 0)
        {
          index = oggSyncState_.buffer(bufferSize_);
          buffer = oggSyncState_.data;
          bytes = readFromStream(buffer, index, bufferSize_);
          if (bytes == -1)
          {
            System.out.println("Ogg Stream empty.");
            log_.debug("Ogg Stream empty.");
            streamStillHasData = false;
            eos = 1;
            //break;
          }
          else
          {
            oggSyncState_.wrote(bytes);
            if (bytes == 0)
            {
              log_.debug("No data left in Ogg Stream.");
              eos = 1;
            }
          }
        }
      }
      oggStreamState_.clear();
      vorbisBlock.clear();
      vorbisDspState.clear();
      vorbisInfo.clear();
    }

            /* Last.fm Song Submit */
            if (lastFMConnected_)
            {
              int sel = playListComboBox.getSelectedIndex();
              try
              {
                //int playTime = (new Long(((new Date()).getTime() - lastFM_songStartTime_.getTime())/1000)).intValue();
                int playTime =  timeWatcherThread_.getPlayTime()/1000;
                if (playTime > 30 && ( songProgress.getPercentComplete() >0.50 || playTime >240))
                {
                  System.out.println("Submitting: "+ playList.getSongArtist(sel)+" "+
                                     playList.getSongTitle(sel)+" "+playList.getSongAlbum(sel)+
                                     " played for:"+playTime+
                                     "sec. at " + lastFM_songStartTime_.getTime()/1000);
                  resp_ = sc_.submit(playList.getSongArtist(sel),
                                     playList.getSongTitle(sel),
                                     playList.getSongAlbum(sel),
                                     playTime,
                                     playList.getTrackNumber(sel),
                                     Source.USER,
                                     lastFM_songStartTime_.getTime()/1000);
                  if (resp_!=null && resp_.ok())
                  {
                    System.out.println("Song Sumbitted");
                  }
                  else
                  {
                    if (resp_ != null)
                    {
                      System.out.println("Sumbmit Failed --> (" +getLastfmErrorType(resp_.getStatus()) +") "+resp_.getMessage());
                      sc_ = handshakeWithLastfm(lastFM_userId);
                      System.out.println("Re-Submitting: "+ playList.getSongArtist(sel)+" "+
                                         playList.getSongTitle(sel)+" "+playList.getSongAlbum(sel)+
                                         " played for:"+playTime+
                                         "sec. at " + lastFM_songStartTime_.getTime()/1000);
                      resp_ = sc_.submit(playList.getSongArtist(sel),
                                         playList.getSongTitle(sel),
                                         playList.getSongAlbum(sel),
                                         playTime,
                                         playList.getTrackNumber(sel),
                                         Source.USER,
                                         lastFM_songStartTime_.getTime()/1000);
                      if (resp_!=null && resp_.ok())
                      {
                        System.out.println("Song Sumbitted");
                      }
                    }
                  }
                }
                else
                {
                  System.out.println("Song NOT submitted because it was too short or did not play long enough");
                  log_.debug("Song NOT submitted because it was too short or did not play long enough");
                }
              }
              catch (IOException ioEx)
              {
                log_.minor("Last.fm submit failed --> (" +getLastfmErrorType(resp_.getStatus()) +") "+resp_.getMessage() ,ioEx);
              }
            }
            /* Last.fm Song Submit */

    oggSyncState_.clear();
    log_.debug("Done Song.");
    System.out.println("Done Song.");
    timerRunning_ = false;
    try
    {
      if (oggBitStream_ != null)
      {
        oggBitStream_.close();
      }
      playing_ = false; //playerThread_ = null;
    }
    catch (Exception e)
    {
    }
    button.setIcon(iconPlay_);
    miniButtonPlay.setIcon(iconMiniPlay_);
    if (miniViewShowing_)
    {
      buttonPause.setEnabled(false);
    }
    else
    {
      miniButtonPause.setEnabled(false);
    }
    log_.endMethod();
  }


  /**
   * Stops the current playing oggStream in the playerStream_.
   */
  public void stop()
  {
    final String methodName = className_ + ": stop()";
    log_.startMethod(methodName);
    if (!playing_)
    {
      try
      {
        outputLine.drain();
        outputLine.stop();
        outputLine.close();
        if (oggBitStream_ != null)
        {
          oggBitStream_.close();
        }
      }
      catch (Exception e)
      {
      }
    }
    playing_ = false; //playerThread_ = null;
    if (!running_as_applet)
    {
      if (miniViewShowing_)
      {
        xpos = popUpWin_.getX();
        ypos = popUpWin_.getY();
      }
      else
      {
        appXpos = appFrame_.getX();
        appYpos = appFrame_.getY();
      }
      storeConfigs();
      // last step is to save it as file
      //
      File file = new File(configFileName_);
      try
      {
        log_.debug("Storing config: "+configFileName_);
        cfgmgr_.store(file);
      }
      catch (Exception e)
      {
        // we have created it but cannot save it. That's bad
        // and let us see why....
        log_.minor("Did Not store config: "+configFileName_,e);
      }
    }
    log_.endMethod();
  }


  /**
   * Loads all the storable data from the config manager.
   **/
  private void loadConfigs()
  {
    final String methodName = className_ + ": loadConfigs()";
    log_.startMethod(methodName);

    // get and set the general options
    // ==============================
    currVolSetting_ = Integer.parseInt(cfgmgr_.getProperty("lastVolume"));
    bufferMultiple_ = Integer.parseInt(cfgmgr_.getProperty("bufferMultiple"));
    volumeSlider_.setValue(currVolSetting_);
    currentVolumeMultiplier_ = volumeMultiplier(currVolSetting_);
    xpos = Integer.parseInt(cfgmgr_.getProperty("lastMiniXPostion"));
    ypos = Integer.parseInt(cfgmgr_.getProperty("lastMiniYPostion"));
    appXpos = Integer.parseInt(cfgmgr_.getProperty("lastNormalXPostion"));
    appYpos = Integer.parseInt(cfgmgr_.getProperty("lastNormalYPostion"));
    showMiniViewOnStart_ = (cfgmgr_.getProperty("lastViewSize").
      trim().equals("mini")?true:false);
    showPlaylistOnStart_ = (cfgmgr_.getProperty("lastPlaylistShowing").
      trim().equals("true")?true:false);
    autoPlayOnStart_ = (cfgmgr_.getProperty("autoPlayingOnStart").
      trim().equals("true")?true:false);
    lastFM_userId =  cfgmgr_.getProperty("lastFM_userId","general");
    lastFM_userPass =  cfgmgr_.getProperty("lastFM_userPass","general");

    // get and set the logging level
    // ==============================
    String logLevel = cfgmgr_.getProperty("level","DEBUG","logging");
    short level = -1;
    if ("NONE".equals(logLevel)) level = Log.NONE;
    else if ("DEBUG".equals(logLevel)) level = Log.DEBUG;
    else if ("MINOR".equals(logLevel)) level = Log.MINOR;
    else if ("MAJOR".equals(logLevel)) level = Log.MAJOR;
    else if ("FULL".equals(logLevel)) level = Log.FULL;
    else if ("QUIET".equals(logLevel)) level = Log.QUIET;
    if (level != -1)
    {
      log_.debug("Setting Log Level to "+logLevel);
      log_.setLogLevel(level);
    }

    // get and set the back colours
    // ============================
    boolean useLastBackColour = (cfgmgr_.
      getProperty("useLastBackColour","true","colours").
      trim().equals("true")?true:false);
    if(useLastBackColour)
    {
      log_.debug("setting Back Colours");
      String red = cfgmgr_.getProperty("lastBackColourRed","100","colours");
      String green = cfgmgr_.getProperty("lastBackColourGreen","180","colours");
      String blue = cfgmgr_.getProperty("lastBackColourBlue","245","colours");
      log_.debug("Back Colour="+red+" " +green+" " +blue);
      mainBackColour_ = new Color(
        Integer.parseInt(red),
        Integer.parseInt(green),
        Integer.parseInt(blue));
      setColours();
    }

    log_.endMethod();
  }


  /**
   * Puts all the storable data in to the config manager in prep for saving.
   **/
  private void storeConfigs()
  {
    final String methodName = className_ + ": storeConfigs()";
    log_.startMethod(methodName);

    if (!this.running_as_applet)
    {
      cfgmgr_.setProperty("lastNormalXPostion",
        String.valueOf(appFrame_.getX()));
      cfgmgr_.setProperty("lastNormalYPostion",
        String.valueOf(appFrame_.getY()));
      cfgmgr_.setProperty("lastMiniXPostion",
        String.valueOf(popUpWin_.getX()));
      cfgmgr_.setProperty("lastMiniYPostion",
        String.valueOf(popUpWin_.getY()));
    }
    cfgmgr_.setProperty("lastLooping",
      String.valueOf(checkBoxLoop.isSelected()));
    cfgmgr_.setProperty("lastRandom",
      String.valueOf(checkBoxRandom.isSelected()));
    cfgmgr_.setProperty("lastPlaylistShowing",
     String.valueOf(checkBoxPlaylist.isSelected()));
    cfgmgr_.setProperty("lastViewSize",(miniViewShowing_?"mini":"normal"));
    cfgmgr_.setProperty("lastVolume",String.valueOf(currVolSetting_));
    cfgmgr_.setProperty("useLastViewSize","true");
    cfgmgr_.setProperty("useLastPosition","true");
    cfgmgr_.setProperty("useLastVolume","true");
    cfgmgr_.setProperty("useLastLooping","true");
    cfgmgr_.setProperty("useLastRandom","true");
    cfgmgr_.setProperty("useLastPlaylistShowing",
                        (showPlaylistOnStart_?"true":"false"));
    cfgmgr_.setProperty("autoPlayingOnStart",
                        (autoPlayOnStart_?"true":"false"));
    cfgmgr_.setProperty("bufferMultiple",String.valueOf(bufferMultiple_));
    cfgmgr_.setProperty("lastFM_userId",lastFM_userId,"general");
    cfgmgr_.setProperty("lastFM_userPass",lastFM_userPass,"general");

    //
    // and now we add some properties to the category "logging"
    //
    cfgmgr_.setProperty("enabled","true","logging");
    cfgmgr_.setProperty("file",logFile_,"logging");
    cfgmgr_.setProperty("level",log_.getLogLevelString(),"logging");
    cfgmgr_.setProperty("lastFont",displayFont_.getName(),"fonts");
    cfgmgr_.setProperty("lastFontAttributes","","fonts");
    cfgmgr_.setProperty("useLastFont","true","fonts");
    cfgmgr_.setProperty("lastBackColourRed",
      String.valueOf(mainBackColour_.getRed()),"colours");
    cfgmgr_.setProperty("lastBackColourGreen",
      String.valueOf(mainBackColour_.getGreen()),"colours");
    cfgmgr_.setProperty("lastBackColourBlue",
      String.valueOf(mainBackColour_.getBlue()),"colours");
    cfgmgr_.setProperty("useLastBackColour","true","colours");


    log_.endMethod();
  }


  /**
   * Creates all the storable data in to the config manager.
   **/
  private void createConfigs()
  {
    final String methodName = className_ + ": createConfigs()";
    log_.startMethod(methodName);
    // We are going to create a XML file now. There is now an empty
    // configuration available
    //
    // We create a second category. Note that there is already
    // the category "general" created
    //
    cfgmgr_.addCategory("logging");
    cfgmgr_.addCategory("fonts");
    cfgmgr_.addCategory("colours");
    cfgmgr_.addCategory("quickButtons");
    storeConfigs();

      log_.endMethod();
}


  /**
   * Converts a linear mapped value to its Log equivalent. For use in the Volume
   * Scale.
   *
   * @param currSetting  is the linear value to convert. Acceptable values: 0 =
   *      mute, 10 = full, 11 is for fun.
   * @return             the Log value (Log currSetting)
   */
  public double volumeMultiplier(int currSetting)
  {
    final String methodName = className_ + ": volumeMultiplier()";
    log_.startMethod(methodName);
    double retVal = 0.0;
    double[] logLookup = {0.0000, 0.0400, 0.3010, 0.4771, 0.6021, 0.6990,
      0.7781, 0.8451, 0.9031, 0.9542, 1.0000, 1.1000};
    double[] inverseLogLookup = {0.0000, 1 - logLookup[9], 1 - logLookup[8],
      1 - logLookup[7], 1 - logLookup[6], 1 - logLookup[5],
      1 - logLookup[4],
      1 - logLookup[3], 1 - logLookup[2], 1 - logLookup[1],
      1.0000, 1.1000};

    if (currSetting > 10)
    {
      retVal = 1.1;
    }
    else if (currSetting > 0)
    {
      if (logVolumeScale_)
      {
        retVal = inverseLogLookup[currSetting];
      }
      else
      {
        retVal = (double) currSetting / 10;
      }
    }

    log_.endMethod();

    return retVal;
  }


  /**
   * Handles all the Actions originating from the Control Buttons. *
   *
   * @param e  Description of Parameter
   */
  public void actionPerformed(ActionEvent e)
  {
    final String methodName = className_ + ": actionPerformed()";
    log_.startMethod(methodName);
    log_.startMethod("Action Source = " + e.getSource());

    ImageIcon buttonIcon = (ImageIcon) ((JButton) (e.getSource())).getIcon();
    String command = "";
    if (buttonIcon != null)
    {
      command = buttonIcon.getDescription();
    }
    try
    {
      int currentItemIndex = playListComboBox.getSelectedIndex();
      log_.debug("\nCurrent Selected Index = "+currentItemIndex);
      int numSongs = playListComboBox.getItemCount();
      if (currentItemIndex < 0)
        currentItemIndex = 0;
      /* Play Button Was Pressed */
      /* *********************** */
      if (command.trim().equals("Play") && !playing_)
      {
        button.setIcon(iconStop_);
        buttonPause.setEnabled(true);
        button.setToolTipText("Stop Current Song");
        miniButtonPlay.setIcon(iconMiniStop_);
        miniButtonPause.setEnabled(true);
        miniButtonPlay.setToolTipText("Stop Current Song");
        log_.debug("\nPlay Button Pressed");
        System.out.println("Play Button");
        if (checkBoxLoop.isSelected())
        {
          log_.debug("\nLooping Songs");
          System.out.println("\nLooping "+numSongs+" Songs");
          //checkBoxLoop.setEnabled(false);
          looping_ = true;
          if (currentItemIndex > 0)
          {
            currentItemIndex--;
          }
          else
          {
            currentItemIndex = numSongs - 1;
          }
          playListComboBox.setSelectedIndex(currentItemIndex);
          String item = (String) (((JCheckBox) playListComboBox.
            getSelectedItem()).getText());
          log_.debug("\nPlay Button on item " +currentItemIndex + " " +item);
          playing_ = false; //playerThread_ = null;
          looperThread_ = new Thread(loopingRunnable_);
          looperThread_.start();
        }
        else
        {
          String item = (String) (((JCheckBox) playListComboBox.
            getSelectedItem()).getText());
          //        buttonLoop.setEnabled(true);
          tryPlay(playList.getURL(item));
        }
      }
      /* Stop Button Was Pressed */
      /* *********************** */
      else if (command.trim().equals("Stop"))// && playerThread_ != null)
      {
        System.out.println("Stop Button");
        loopPaused_ = false;
        playing_ = false;  // this stops this threads run method
        looping_ = false; // this stops the looping thread
        //checkBoxLoop.setEnabled(true);
        button.setToolTipText("Play Current Song In Playlist");
        button.setIcon(iconPlay_);
        buttonLast.setEnabled(true);
        buttonNext.setEnabled(true);
        buttonPause.setEnabled(false);
        miniButtonPlay.setToolTipText("Play Current Song In Playlist");
        miniButtonPlay.setIcon(iconMiniPlay_);
        miniButtonLast.setEnabled(true);
        miniButtonNext.setEnabled(true);
        miniButtonPause.setEnabled(false);
        Util.sleep(500);
        timeLabelValue.setText("00:00");
        miniTimeLabelValue.setText("00:00");
        appFrame_.setTitle(APP_NAME + " - No Songs Currently Playing");
      }
      /* Back Button Was Pressed */
      /* *********************** */
      else if (command.trim().equals("Back"))
      {
        System.out.println("Back Button");
        if (currentItemIndex > 0)
        {
          currentItemIndex--;
        }
        else
        {
          currentItemIndex = numSongs - 1;
        }
        if (looperThread_ != null && looperThread_.isAlive())
        {
          if (currentItemIndex > 0)
          {
            currentItemIndex--;
          }
          else
          {
            currentItemIndex = numSongs - 1;
          }
          playListComboBox.setSelectedIndex(currentItemIndex);
          playing_ = false; //playerThread_ = null;
          stop();
        }
        else
        {
          if (checkBoxRandom.isSelected())
          {
            currentItemIndex = (int) (Math.random() * playListComboBox.getItemCount());
          }
          playListComboBox.setSelectedIndex(currentItemIndex);
          playing_ = false; //playerThread_ = null;
          stop();
          String item = (String) (((JCheckBox) playListComboBox.getSelectedItem()).getText());
          tryPlay(playList.getURL(item));
        }
      }
      else if (command.trim().equals("Forward"))
      {
        if (looperThread_ != null)// && looperThread_.isAlive())
        {
          System.out.println("Next Song In Loop Button");
          playing_ = false; //playerThread_ = null;
          stop();
        }
        else
        {
          System.out.println("Start On Next Song Button");
          playing_ = false; //playerThread_ = null;
          stop();
          Util.sleep(1000);
          if (checkBoxRandom.isSelected())
          {
            System.out.println("...Random");
            currentItemIndex =
              (int) (Math.random() * playListComboBox.getItemCount());
          }
          else if (currentItemIndex < numSongs && currentItemIndex >= 0)
          {
            currentItemIndex++;
          }
          else
          {
            currentItemIndex = 0;
          }
          playListComboBox.setSelectedIndex(currentItemIndex);
          String item = (String) (((JCheckBox) playListComboBox.
            getSelectedItem()).getText());
          tryPlay(playList.getURL(item));
        }
      }
      else if (command.trim().equals("Pause"))
      {
        System.out.println("Pause Button");
        if (timerRunning_ && !loopPaused_)
        {
          loopPaused_ = true;
          buttonLast.setEnabled(false);
          buttonNext.setEnabled(false);
          miniButtonLast.setEnabled(false);
          miniButtonNext.setEnabled(false);
        }
        else
        {
          loopPaused_ = false;
          buttonPause.setBackground(buttonBackColour_);
          buttonLast.setEnabled(true);
          buttonNext.setEnabled(true);
          miniButtonPause.setBackground(buttonBackColour_);
          miniButtonLast.setEnabled(true);
          miniButtonNext.setEnabled(true);
        }
      }
      else if (command.trim().equals("Toggle Mini View"))
      {
        System.out.println("Toggle Mini View");
        if (miniViewShowing_)
        {
          showMiniView(false);
        }
        else
        {
          showMiniView(true);
        }
      }
      else if (command.trim().equals("Close jOggPlayer"))
      {
        System.out.println("Close App");
        playing_ = false;
        stop();
        log_.close();
        System.exit(0);
      }
      else
      {
        log_.debug("LeftOver ActionListner");
      }
    }
    catch (Exception ex)
    {
      log_.major("Action Listener Exception", ex);
    }
    log_.endMethod();
  }
}

/*
 *  Here is the revision log
 *  ------------------------
 *  $Log: jOggPlayer.java,v $
 *  Revision 1.30  2004/11/14 19:31:43  tgutwin
 *  Bug Fix: The last Volume level is now used properly on statup.
 *
 *  Revision 1.28  2004/11/13 23:06:20  tgutwin
 *  Implemented the Authenticating Proxy.
 *  Started using the PlayList get methods to obtain the Vorbis comments instead of parsing them myself.
 *
 *  Revision 1.27  2003/05/01 05:42:36  tgutwin
 *  Added Javadoc comments dor describing Proxy usage requiring a username/Pass authentication.
 *  Also fixed the background/texture bug. (it couldn't find the image files).
 *
 *  Revision 1.26  2003/04/10 18:03:30  tgutwin
 *  Added code to delay the dismiss of the display Song Info tooltip so the user can have more time to read the song info.
 *
 *  Revision 1.25  2002/04/24 05:21:48  tgutwin
 *  Moved the Auto Start code into the main(). And Upped the version to 1.1.4.
 *
 *  Revision 1.24  2002/04/23 21:40:18  tgutwin
 *  Added the Auto Play on startup feature.
 *
 *  Revision 1.23  2002/04/11 23:13:25  anonymous
 *  Standardized the string needed for Util.loadImage. Also removed unneeded debug output to console. Added the capability to load a Playlist via the Load from URL dialog.
 *
 *  Revision 1.22  2002/04/06 06:27:32  anonymous
 *  Upped the version to 1.1.3
 *
 *  Revision 1.21  2002/04/06 06:13:58  anonymous
 *  changed the default loag level to MINOR in prep for 1.1.3 release
 *
 *  Revision 1.20  2002/04/05 23:08:44  anonymous
 *  Bug fixes when 1 or two items are in the list.
 *
 *  Revision 1.19  2002/04/04 23:05:56  anonymous
 *  Some Log Changes.
 *
 *  Revision 1.18  2002/01/28 06:33:13  tgutwin
 *
 *  Extra patch with the looping counters.
 *
 *  Revision 1.17  2002/01/27 23:01:57  tgutwin
 *
 *  Cleaned up the next checked item bug that was screwing up the
 *  looping and playing of checked items.
 *  Upped the version Num to 1.1.2
 *
 *  Revision 1.16  2002/01/02 05:52:52  root
 *
 *  Version 1.1.0 release.
 *
 *  Revision 1.15  2001/12/17 07:34:45  tgutwin
 *
 *  Bug Fix bufferMultiple config item.
 *
 *  Revision 1.14  2001/12/17 06:59:37  tgutwin
 *
 *  Added a user settable Buffer size dialog.
 *
 *  Revision 1.13  2001/12/16 07:23:55  tgutwin
 *
 *  Added Items to the help text.
 *  Loads the last colours from the config xml doc.
 *  Added a Mute Button.
 *  Favourite spelling.
 *  Added an option menu item and moved items into it.
 *  Playlist area now follows any background colour changes.
 *  Added the menu to the display area.
 *  Playlist file chooser now can have either a save/open dialog.
 *
 *  Revision 1.12  2001/12/02 07:40:40  tgutwin
 *
 *  Fixed the VERSION String to be 1.1 pre 4. For release.
 *
 *  Revision 1.11  2001/12/02 07:32:34  tgutwin
 *
 *  One last applet check.
 *
 *  Revision 1.9  2001/11/21 03:54:10  tgutwin
 *
 *  Added a draggable title button to the mini view.
 *
 *  Revision 1.8  2001/11/17 03:49:10  tgutwin
 *  Added a little area to drag the miniview.
 *  Revision 1.7  2001/11/17 01:56:17  tgutwin
 *  Upped the version and added a draggable area holder for the mini-view.
 *  Revision 1.6  2001/11/16 05:51:47  tgutwin
 *  Mini View 90% complete.
 *  Revision 1.5  2001/08/12 06:04:18  tgutwin
 *  Moved PlayList into ca.bc.webarts.widgets.
 *  Added a bunch of JavaDocs.
 *  Renamed Many vars to be more descriptive.
 *  General Code Cleanup.
 *  NO NEW FEATURES.
 *  Revision 1.4  2001/08/03 00:00:09  tgutwin
 *  Improved the Layout Managers
 *  Revision 1.3  2001/06/16 21:40:03  tgutwin
 *  Added CVS Keywords and headers
 *  Revision 1.2  2001/06/16 21:35:11  tgutwin
 *  Added CVS Keywords and headers
 */

