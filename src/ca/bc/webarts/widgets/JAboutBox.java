/*
$Source: v:/cvsroot/open/projects/WebARTS/ca/bc/webarts/widgets/JAboutBox.java,v $
$Name:  $

Current File Status:
$Revision: 1.7 $
$Date: 2001/12/16 07:16:32 $
$Locker:  $

Copyright (C) 2001 WebARTS Design, North Vancouver Canada
*/

package ca.bc.webarts.widgets;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.lang.System;
import javax.swing.*;
import javax.swing.event.*;
import ca.bc.webarts.widgets.ImageCanvas;



public class JAboutBox extends JFrame
{
  Toolkit toolkit = Toolkit.getDefaultToolkit();
  ImageCanvas canvas;
  String imageJarFilename_ = "";
  String graphicFilename_ = "";
  String versionStr_ = "";
  static String helpHtmlStr_ = "";
  Image image_ = null;

//	TBGJPanel mainPanel = new TBGJPanel();
  JPanel mainPanel = new JPanel();
  JPanel titlePanel = new JPanel();
  JPanel innerPanel = new JPanel();
  JPanel graphicPanel = new JPanel();
  JPanel textPanel = new JPanel();
  JPanel buttonPanel = new JPanel();
  JPanel topPanel = new JPanel();
  String htmlTitle, tag;
  JButton vmButton, sysButton, dbButton, helpButton, closeButton;
  Color defaultColour = new Color(156,154,206); // this is the Java L&F Purple!
  javax.swing.border.BevelBorder mainBevelBorder =
     new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED);

/* Constructor */
/***************/
public JAboutBox( String parentTitle,
                  String detailedTitle,
                  String versionString)
{
  this(null, parentTitle, detailedTitle, versionString, "No Help Available.");
}


/* Constructor */
/***************/
public JAboutBox( String parentTitle,
                  String detailedTitle )
{
  this(null, parentTitle, detailedTitle, "", "No Help Available." );
}

/* Constructor */
/***************/
public JAboutBox( Image i, String parentTitle, String detailedTitle,
                  String versionString, String helpHtml )
{

    /* get the super class frame to build a frame with a title */
    super(parentTitle);
    versionStr_ = versionString;
    helpHtmlStr_ = helpHtml;
    image_ = i;
        try
        {
          final String jvmVer = System.getProperty("java.version");
          final String JVMvendor = System.getProperty("java.vendor") + " " +
            System.getProperty("java.vendor.url");
          final String OSname = System.getProperty("os.name");
          final String OSarch = System.getProperty("os.arch");
          final String OSver = System.getProperty("os.version");
          final String UserName = System.getProperty("user.name");
          final String UserDir = System.getProperty("user.home");
/*	        final String dbVendor = parentApplet.getdbVendorParam();
          final String dbName = parentApplet.getdbNameParam();
          final String dbUsername = parentApplet.getdbUsernameParam();
          final String dbdbPassword = parentApplet.getdbPasswordParam();
          final String dbServer = parentApplet.getdbServerParam();
          final String dbPort = parentApplet.getdbPortParam();
*/
/*
java.version    Java Runtime Environment version
java.vendor	Java Runtime Environment vendor
java.vendor.url	Java vendor URL
java.home	Java installation directory
java.vm.specification.version	Java Virtual Machine specification version
java.vm.specification.vendor	Java Virtual Machine specification vendor
java.vm.specification.name	Java Virtual Machine specification name
java.vm.version	Java Virtual Machine implementation version
java.vm.vendor	Java Virtual Machine implementation vendor
java.vm.name	Java Virtual Machine implementation name
java.specification.version	Java Runtime Environment specification version
java.specification.vendor	Java Runtime Environment specification vendor
java.specification.name	Java Runtime Environment specification name
java.class.version	Java class format version number
java.class.path	Java class path
os.name	Operating system name
os.arch	Operating system architecture
os.version	Operating system version
file.separator	File separator ("/" on UNIX)
path.separator	Path separator (":" on UNIX)
line.separator	Line separator ("\n" on UNIX)
user.name	User's account name
user.home	User's home directory
user.dir   User's current working directory
*/

      /* set the layout Manager and borders for the JFrame */
      mainPanel.setLayout(new BorderLayout()); //10,10));
      mainPanel.setBackground(defaultColour);
      mainPanel.setBorder(mainBevelBorder);
      buttonPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));


      /* place The Title Label into the app frame */
      htmlTitle = new String("<html><body bgcolor=\"#eeeeee\"><p align=\"center\"><FONT FACE=\"QuillScript\" SIZE=\"+1\">");
      htmlTitle += "Web<I>ARTS</I> Design</FONT></body></html>";
      titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER,10,10));
      titlePanel.add(new JLabel(htmlTitle));

      /* make up the text that will go into the text area */
      tag = new String("<html><body bgcolor=\"#dddddd\"><p align=\"center\">"+
                detailedTitle);
      tag += "<p align=\"center\">" +versionStr_+ "<BR>";
      tag += "<p align=\"center\">Tom B. Gutwin P.Eng.</body></html>";

      /* make the Displayed panels */
//      topPanel.setLayout(new GridLayout(0,2));
      buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER,10,10));
      graphicPanel.setLayout(new BorderLayout(10,10)); //FlowLayout(FlowLayout.CENTER,0,0));
      textPanel.setLayout(new BorderLayout(10,10)); //FlowLayout(FlowLayout.CENTER,0,0));
      innerPanel.setLayout(new BorderLayout(10,10));

      /* fill the text panel */
      textPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
      textPanel.add(new JLabel(tag),"Center");

      /* fill the graphic Panel */
      if(image_ == null)
        image_   = toolkit.getImage("images/DONT_PAN.GIF");
      canvas  = new ImageCanvas(image_);
      graphicPanel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
      graphicPanel.add(canvas,"Center");

      /* create the buttons */
      vmButton = new JButton("Java VM Info.");
      sysButton = new JButton("System Info.");
      dbButton = new JButton("Database Info.");
      helpButton = new JButton("Help");
      closeButton = new JButton("Close");
      Dimension tempSize =new Dimension(85,30);
      Font tempFont = new Font("Dialog",Font.PLAIN,10);
      vmButton.setPreferredSize(tempSize);vmButton.setFont(tempFont);vmButton.setMargin(new Insets(6,6,6,6));
      sysButton.setPreferredSize(tempSize);sysButton.setFont(tempFont);sysButton.setMargin(new Insets(6,6,6,6));
      dbButton.setPreferredSize(tempSize);dbButton.setFont(tempFont);dbButton.setMargin(new Insets(6,6,6,6));
      helpButton.setPreferredSize(tempSize);helpButton.setFont(tempFont);helpButton.setMargin(new Insets(6,6,6,6));
      closeButton.setPreferredSize(tempSize);closeButton.setFont(tempFont);closeButton.setMargin(new Insets(6,6,6,6));

      buttonPanel.add(vmButton);
      buttonPanel.add(sysButton);
//      buttonPanel.add(dbButton);
      buttonPanel.add(closeButton);
      buttonPanel.add(helpButton);

      /*  Add the Button Action Listeners */
      /************************************/
      /* JDK VM Button */
      vmButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent event)
        {
          /* now add the specific stuff for this Menu Item */
          /* open a dialog to find out what version to print the text output*/
          Object [] options = {"Okay"};
          JScrollPane js = new JScrollPane();

          JOptionPane pane = new JOptionPane(
                (Object) new JLabel("<html><body><B><U>Java Vendor:</U> " +
                                    JVMvendor + "<BR><U>Java VM Version:</U> " +
                                    jvmVer+ "<BR><U>JVM Free Memory:</U> " +
                                    java.lang.Runtime.getRuntime().freeMemory() +
                                    "/" +java.lang.Runtime.getRuntime().totalMemory()+
                                    " bytes"+
                                    "</B></body></html>"),
                JOptionPane.INFORMATION_MESSAGE,
                JOptionPane.DEFAULT_OPTION,
                new ImageIcon(),
                options) ;
           JDialog dialog = pane.createDialog(getParent(), "Java VM Information");
           dialog.show();
        }/* action performed */
      });

      /* System Info Button */
      sysButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent event)
        {
          /* now add the specific stuff for this Menu Item */
          Object [] options = {"Okay"};
           JOptionPane pane = new JOptionPane((Object) new JLabel("<html><body><B>" +
                                      "<U>User's account name:</U> " + UserName +
                                      "<BR><U>User Home Directory:</U> "+UserDir +
                                      "<BR><U>Operating system name:</U> " +OSname+
                                      "<BR><U>Operating system version:</U> " +OSver+
                                      "<BR><U>Operating system architecture:</U> " +OSarch+
                                      "</B></body></html>"),
                            JOptionPane.INFORMATION_MESSAGE,
                            JOptionPane.DEFAULT_OPTION,
                            new ImageIcon(),
                            options) ;
           JDialog dialog = pane.createDialog(getParent(), "User System Information");
           dialog.show();
        }/* action performed */
      });

      /* Database Info. Button */
      dbButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent event)
        {
          /* now add the specific stuff for this Menu Item */
          Object [] options = {"Okay"};
//					 JOptionPane pane = new JOptionPane((Object) new JLabel("<html><body><U>Database Vendor:</U> "+ dbVendor +
//																			"<BR><U>Database Name:</U> " +dbName +
//																			"<BR><U>Database Server:</U> " +dbServer +
//																			"<BR><U>Database Server Port:</U> " +dbPort +
//																			"</body></html>"),
//														JOptionPane.INFORMATION_MESSAGE,
//														JOptionPane.DEFAULT_OPTION,
//														new ImageIcon(),
//														options) ;
//					 JDialog dialog = pane.createDialog(getParent(), "Database Information");
//					 dialog.show();
      }/* action performed */
      });

      /* Help Button */
      helpButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent event)
        {
          /* now add the specific stuff for this Menu Item */
            Object [] options = {"Done"};
            JEditorPane htmlPane = new JEditorPane("text/html",
                           "<html><body>" +
                            helpHtmlStr_+
                            "</body></html>");
            htmlPane.setEditable(false);

            JScrollPane htmlScrollPane = new JScrollPane(htmlPane);
            htmlScrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            htmlScrollPane.setPreferredSize(new Dimension(640, 480));
            JOptionPane pane = new JOptionPane(
                            (Object) htmlScrollPane,
                            JOptionPane.INFORMATION_MESSAGE,
                            JOptionPane.DEFAULT_OPTION,
                            new ImageIcon(),
                            options) ;
            JDialog dialog = pane.createDialog(getParent(), "Help");
            dialog.show();
        }/* action performed */
      });

      /* window Close Button */
      closeButton.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent event)
        {
          /* now add the specific stuff for this Menu Item */
            dispose();
        }/* action performed */
      });


      /* Stitch it all together so it is laid out */
      topPanel.add(graphicPanel);
      topPanel.add(textPanel);
      innerPanel.add(topPanel,"Center");
      innerPanel.add(buttonPanel, "South");
      mainPanel.add(titlePanel, "North");
      mainPanel.add(innerPanel, "Center");

      /* place The Main Panels into the app frame */
      getContentPane().add(mainPanel);
      doLayout();

      /* set the size and placement on the screen */
      Dimension overallSize;
      Point location;
      try {
        Dimension myDialogSize = getPreferredSize();
        overallSize = toolkit.getScreenSize();
        location = new Point(0,0);;
        int windowWidth = overallSize.width;
        int	windowHeight = overallSize.height;
        int myWidth = myDialogSize.width;
        int myHeight = myDialogSize.height;
        int newX= location.x +  (windowWidth/2) - myWidth/2;
        int newY=  location.y + (windowHeight/2) - myHeight/2;
        setLocation( newX,newY);
      }
      catch (Exception ex){
        System.out.println("Caught Exception: AboutBox ");
        ex.printStackTrace();
      }


      setSize(500,225);
      setResizable(false);
      setVisible(true);
      pack();
      show();
      toFront();
      validate();

      /* listen for closing */
      addWindowListener(new WindowAdapter()
      {
        public void windowClosing(WindowEvent event)
        {
          dispose();
        }
      }); /* inner WindowAdapter class */
    }
    catch (Throwable t)
    {
            System.out.println("About Box caught an exception: " + t);
            t.printStackTrace();
        }
  }
}
/*
Here is the revision log
------------------------
$Log: JAboutBox.java,v $
Revision 1.7  2001/12/16 07:16:32  tgutwin

Added user.home to the system info that is output when the button
is pressed.

Revision 1.6  2001/12/12 14:37:43  tgutwin

Added the API to send a String of HTML to be used as help Text when the help button is pressed.

Revision 1.5  2001/09/02 03:24:18  tgutwin

Initial Rev of my custom DropDown Boxes.

Revision 1.4  2001/06/16 21:55:15  tgutwin
No Changes

Revision 1.3  2001/06/16 21:47:45  tgutwin
Added CVS Keywords and headers


*/
