/* ****************************************************************************/
/*                              Splash                                        */
/*                          - Tom B. Gutwin -                                 */
/*  En Extended Frame to act as a splash pane that comes up before the app    */
/* ************************************************************************** */
/*  This program requires JVM 1.2 or JVM 1.1  SWING NOT REQUIRED              */
/*  If building with the OS/2 JDK - at least 1.1.4 with the 1.1.4 fixes       */
/* ****************************************************************************/

/*
$Copyright $

$Header: v:/cvsroot/open/projects/WebARTS/ca/bc/webarts/widgets/Splash.java,v 1.4 2002/04/04 23:02:50 anonymous Exp $

$Log: Splash.java,v $
Revision 1.4  2002/04/04 23:02:50  anonymous
Commented out some debug statements. Added some extra exception handling.

Revision 1.3  2001/10/28 06:48:28  tgutwin

Added some Constructors that take an image (so this class doesn't have to go looking for the file.

Revision 1.2  2001/09/02 03:24:30  tgutwin

Initial Rev of my custom DropDown Boxes.

Revision 1.1.1.1  2001/03/03 23:36:06  tgutwin
Imported Sources


*/

package ca.bc.webarts.widgets;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.lang.*;
import java.awt.Dimension;
import ca.bc.webarts.widgets.ImageCanvas;


/**
 * Splash is a Application Opening splash screen.
 * It provides an expandable API for its use as a generic Splash, allowing
 * the user to specify the graphic to use as well as the message to put up.
 */
public class Splash extends Frame implements Runnable{
  Toolkit toolkit = Toolkit.getDefaultToolkit();
  Window  window;
  Image   image = null;
  static String 	imgName = "."+File.separator+
                            "images"+File.separator+"dont_pan.gif";
  static int		mSec = 3000;
  static final int DEFAULT_SLEEP_TIME = 2000;
  static Panel	messageArea;
  boolean splashFinished = false;

  static public void main(String[] args)
  {
    System.out.println("Splashing "+imgName);
    if (args.length > 0)
      imgName = args[0];
    if (args.length > 1)
      mSec = (Integer.valueOf(args[1])).intValue();
    Frame frame = new Splash(imgName, mSec);
    System.exit(0);
  }

  public Splash()
  {
    mSec = DEFAULT_SLEEP_TIME;
    (new Thread(this)).start();
  }

  public Splash(String imageName)
  {
    mSec = DEFAULT_SLEEP_TIME;
    imgName = imageName;
    (new Thread(this)).start();
  }

  public Splash(Image img)
  {
    mSec = DEFAULT_SLEEP_TIME;
    image = img;
    imgName = "defaultImageName";
    (new Thread(this)).start();
  }

  public Splash(String imageName,int time)
  {
    System.out.println("Splashing "+imgName);
    imgName = imageName;
    mSec=time;
    (new Thread(this)).start();
  }

  public Splash(Image img,int time)
  {
    image = img;
    imgName = "defaultImageName";
    if (img==null)
    {
      System.out.println("Splash Image is NULL");
    }
    mSec=time;
    (new Thread(this)).start();
  }

  public Splash(String imageName, int time, Panel messagePanel)
  {
    imgName = imageName;
    mSec=time;
    messageArea = messagePanel;
    (new Thread(this)).start();
  }

  public Splash(Image img,int time, Panel messagePanel)
  {
    image = img;
    mSec=time;
    messageArea = messagePanel;
    (new Thread(this)).start();
  }

  public Splash(String imageName, Panel messagePanel)
  {
    imgName = imageName;
    mSec=DEFAULT_SLEEP_TIME;
    messageArea = messagePanel;
    (new Thread(this)).start();
  }

  public void finishSplash()
  {
    splashFinished = true;
  }

  public void	indicateUpdate()
  {
    /* do something to the splash screen to show that there
      has been some progress*/
  }

  public void run()
  {
    Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
    /*if (image == null)
      System.out.println("Splashing "+imgName);
    else
        System.out.println("Splashing a passed-in image.");
*/
    ImageCanvas canvas = null;

    window  = new Window(this);
    if (image == null)
      image   = toolkit.getImage(imgName); // "images/DONT_PAN.GIF");
    try
    {
      canvas  = new ImageCanvas(image);
    }
    catch (java.lang.NullPointerException nullPtr)
    {
        nullPtr.printStackTrace();
      //canvas  = new ImageCanvas(new Image());
    }
    window.setLayout (new BorderLayout() );

    if (canvas != null )
      window.add("Center", canvas);

    if (messageArea !=null )
      window.add("South",messageArea );

    Dimension   scrnSize  = toolkit.getScreenSize();
    int         imgWidth  = image.getWidth(this),
                imgHeight = image.getHeight(this),
                panelHeight = messageArea.getPreferredSize().height * 3 + 3;

    window.setLocation(scrnSize.width/2  - (imgWidth/2),
                       scrnSize.height/2 - ((imgHeight+panelHeight)/2));
    window.setSize(imgWidth,imgHeight+panelHeight);
    window.pack();
    window.show();
    window.toFront();
    window.setVisible(true);
    this.repaint();
    //System.out.println("Splashing "+imgName);
    while(!splashFinished)
    {
      try
      {
        Thread.currentThread().sleep(mSec);
        finishSplash();
      }
      catch(Exception e) { e.printStackTrace(); }
    }

    window.dispose();
  }
}


