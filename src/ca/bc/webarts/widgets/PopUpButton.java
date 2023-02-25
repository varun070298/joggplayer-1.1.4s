/*
 *  $Source: /cvsroot2/open/projects/WebARTS/ca/bc/webarts/widgets/PopUpButton.java,v $
 *  $Name:  $
 *  $Revision: 1.6 $
 *  $Date: 2003/11/02 03:40:38 $
 *  $Locker:  $
 */
/*
 *  PopUpButton -- A Widget Class to mimic a drop down box which allows
 *  any JComponent inside its popup.
 *
 *  Copyright (C) 2001 WebARTS Design, North Vancouver Canada
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
package ca.bc.webarts.widgets;

import ca.bc.webarts.tools.Log;
import ca.bc.webarts.widgets.Util;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.text.Collator;
import java.net.URL;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JWindow;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;

import kiwi.util.KiwiUtils;

/**
 * A custom Drop Down box that requires the user to select the button to open and
 * close the popup list.
 *
 * @author    Tom Gutwin P.Eng
 * @created   October 15, 2001
 */
public class PopUpButton extends JButton
{

  /**
   * a test JFrame.
   */
  protected static JFrame appFrame_ = null;

  /**
   * The Log Filename.
   */
  protected static String logFile_ = "." + File.separator + "PopUpLog.txt";
  /**
   * The Log that will get used.
   */
  protected static Log log_ = Log.createLog(Log.DEBUG, logFile_);
  /**
   * A Class holder for its name (used in Logging).
   */
  private static String className_ = "PopUpButton";

  /**
   * Sample Test Data to put in the JList.
   */
  protected final static String[] SAMPLE_LIST_DATA =
    {"Item0", "Item1", "Item12", "Item3", "Item8", "Item4", "Item5", "Item6",
     "Item7", "Item11", "Item11", "Item0"};
  /**
   * The list model used by this class.
   */
  protected DefaultListModel listModel_ = new DefaultListModel();

  /**
   * The JFrame that the popUpPanel_/PopUpList will live in.
   */
  protected JWindow popUpWin_ = new JWindow();

  /**
   * The JPanel that the popUpScroller_ will live in.
   */
  protected JPanel popUpPanel_ = new JPanel();

  /**
   * The JScrollPane that the PopUpList will live in.
   */
  protected JScrollPane popUpScroller_ = new JScrollPane();

  /**
   * Flags if the popup list is showing.
   */
  protected boolean popUpIsShowing_ = false;

  /**
   * The position on the screen to place the popup.
   */
  protected Point popUpPosition_ = new Point(25, 25);

  /**
   * The Property Listener for this class.
   */
  protected PropertyChangeListener PopUpButtonPropListener_ =
    new PropertyChangeListener()
    {
      public void propertyChange(PropertyChangeEvent e)
      {
        PopUpButton me = (PopUpButton) e.getSource();
        //System.out.println("PropChange Event from: " + e.getPropertyName());
        me.showPopUp(false);
      }
    };

  /**
   * The Action Listener for this class.
   */
  protected ActionListener PopUpButtonActionListener_ =
    new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        PopUpButton me = (PopUpButton) e.getSource();
        //System.out.println("PopUpButton Action Event from: " +
        //  e.getActionCommand());
        popUpPanel_.setSize(getWidth(),popUpPanel_.getHeight());
        me.togglePopUp();
      }
    };

  /**
   * The List of items that goes in the PopUp.
   */
  JList popUpList_ = null;



  /**
   * Basic Constructor that instantiates with an empty popup list using the basic
   * renderer. Sets the text allignment to LEFT and adds a metal Icon.
   */
  protected PopUpButton()
  {
    final String methodName = className_ + ": PopUpButton()";
    log_.startMethod(methodName);

    setHorizontalTextPosition(LEFT);
    setHorizontalAlignment(RIGHT);
    StringBuffer fileName = new StringBuffer();
    fileName.append("ca");
    fileName.append(File.separator);
    fileName.append("bc");
    fileName.append(File.separator);
    fileName.append("webarts");
    fileName.append(File.separator);
    fileName.append("widgets");
    fileName.append(File.separator);
    fileName.append("");
    fileName.append("DropDownButtonIcon.jpg");
    //URL imgUrl = this.getClass().getResource(fileName.toString(0));
    //backgroundTexture_ = Util.loadImage(fileName, "jOggPlayer.jar");
    log_.debug("Retrieving "+fileName.toString());
    String jarFilename = "";//Util.tokenReplace(codeBase_.getFile(),"/",File.separator)+
    //   File.separator+"jOggPlayer.jar";
    try
    {
      //setIcon(new ImageIcon(Util.loadImage(fileName.toString(),jarFilename)));
      setIcon(new ImageIcon(
        PopUpButton.class.getResource("/ca/bc/webarts/widgets/DropDownButtonIcon.jpg")));
    }
    catch(Exception crap)
    {
      log_.minor("Exception retrieving "+fileName+" from "+jarFilename,crap);
    }
    setMargin(new Insets(0,0,0,0));

    log_.endMethod();
  }


  /**
   * Constructor that instantiates the popup list using the passed in JList and
   * basic renderer.
   *
   * @param jl  The JList to iunstantiate with.
   */
  public PopUpButton(JList jl)
  {
    final String methodName = className_ + ": PopUpButton(JList)";
    //log_ = Log.createLog(Log.FULL, logFile_);
    log_.startMethod(methodName);
    if (jl != null)
    {
      popUpList_ = jl;
      sortDefaultList((DefaultListModel)popUpList_.getModel());
    }
    else
    {
      System.err.println("PopUpButton: The passed in JList is Null, " +
        "initializing with an empty JList Popup.");
      popUpList_ = new JList(listModel_);
    }
    setHorizontalTextPosition(LEFT);
    setHorizontalAlignment(RIGHT);
    setIcon(new ImageIcon(this.getClass().getResource(
      "DropDownButtonIcon.jpg")));
    setMargin(new Insets(1,1,1,1));
    popUpList_.addMouseListener(new ListMouseListener());
    popUpList_.setVisibleRowCount(6);
    popUpScroller_.getViewport().setView(popUpList_);
    popUpPanel_.add(popUpScroller_);
    //popUpPanel_.setSize(getWidth());
    popUpWin_.getContentPane().add(popUpPanel_);

    addPropertyChangeListener(PopUpButtonPropListener_);
    addActionListener(PopUpButtonActionListener_);
    log_.endMethod();
  }


  /**
   * Constructor that instantiates the popup list using the passed in Array and
   * basic renderer.
   *
   * @param jl  An array of objects to instantiate the popup list with.
   */
  public PopUpButton(Object[] jl)
  {
    final String methodName = className_ + ": PopUpButton(Object[])";
    //log_ = Log.createLog(Log.FULL, logFile_);
    log_.startMethod(methodName);
    if (jl != null && jl.length > 0)
    {
      for (int i = 0; i < jl.length; i++)
      {
        ((DefaultListModel) listModel_).addElement(jl[i]);
      }
      popUpList_ = new JList(listModel_);
      sortDefaultList((DefaultListModel)popUpList_.getModel());
    }
    else
    {
      log_.minor("PopUpButton: The passed in Array is Null" +
        " (or empty), initializing with an empty JList Popup.");
      popUpList_ = new JList(listModel_);
    }
    setHorizontalTextPosition(LEFT);
    setHorizontalAlignment(RIGHT);
    setIcon(new ImageIcon(this.getClass().getResource(
      "DropDownButtonIcon.jpg")));
    setMargin(new Insets(1,1,1,1));
    popUpList_.addMouseListener(new ListMouseListener());
    popUpList_.setVisibleRowCount(6);
    popUpList_.setBorder(null);
    popUpScroller_.getViewport().setView(popUpList_);
    popUpPanel_.add(popUpScroller_);
    //popUpPanel_.setWidth(getWidth());
    popUpWin_.getContentPane().add(popUpPanel_);

    addPropertyChangeListener(PopUpButtonPropListener_);
    addActionListener(PopUpButtonActionListener_);
    log_.endMethod();
  }


  /**
   * Constructor that instantiates the popup list using the passed in Vector and
   * basic renderer.
   *
   * @param jlv  A Vector of objects to instantiate the popup list with.
   */
  public PopUpButton(Vector jlv)
  {
    final String methodName = className_ + ": PopUpButton(Vector)";
    //log_ = Log.createLog(Log.FULL, logFile_);
    log_.startMethod(methodName);
    if (jlv != null && jlv.size() > 0)
    {
      for (int i = 0; i < jlv.size(); i++)
      {
        ((DefaultListModel) listModel_).addElement(jlv.get(i));
      }
      popUpList_ = new JList(listModel_);
      sortDefaultList((DefaultListModel)popUpList_.getModel());
    }
    else
    {
      log_.minor("PopUpButton: The passed in Vector is Null" +
        " (or empty), initializing with an empty JList Popup.");
      popUpList_ = new JList(listModel_);
    }
    setHorizontalTextPosition(LEFT);
    setHorizontalAlignment(RIGHT);
    setIcon(new ImageIcon(this.getClass().getResource(
      "DropDownButtonIcon.jpg")));
    setMargin(new Insets(1,1,1,1));
    popUpList_.addMouseListener(new ListMouseListener());
    popUpList_.setVisibleRowCount(6);
    popUpScroller_.getViewport().setView(popUpList_);
    popUpPanel_.add(popUpScroller_);
    //popUpPanel_.setWidth(getWidth());
    popUpWin_.getContentPane().add(popUpPanel_);

    addPropertyChangeListener(PopUpButtonPropListener_);
    addActionListener(PopUpButtonActionListener_);
    log_.endMethod();
  }


  /**
   * The main program for the PopUpButton class
   *
   * @param args  The command line arguments
   */
  public static void main(String[] args)
  {
    appFrame_ = new JFrame();
    appFrame_.getContentPane().setLayout(new BorderLayout());
    appFrame_.setTitle("PopUpButton TEST Window.");
    final PopUpButton myTestButton = new PopUpButton(SAMPLE_LIST_DATA);
    appFrame_.addWindowListener(
      new WindowAdapter()
      {
        public void windowClosing(WindowEvent e)
        {
          myTestButton.getLog().close();
          System.exit(0);
        }
      });
    myTestButton.setText("Press Me");
    appFrame_.getContentPane().add(myTestButton);
    appFrame_.pack();
    KiwiUtils.centerWindow(appFrame_);
    appFrame_.toFront();
    appFrame_.setVisible(true);
    appFrame_.repaint();
  }


  /**
   * Sets the SelectedIndex attribute of the PopUpButton object.
   *
   * @param index  The new SelectedIndex value
   */
  public void setSelectedIndex(int index)
  {
    if (index < 0)
      index = ((DefaultListModel)((JList) popUpList_).getModel()).getSize() -1;
    if (index >= ((DefaultListModel)((JList) popUpList_).getModel()).getSize())
      index = 0;
    final String methodName = className_ + ": setSelectedIndex("+index+")";
    log_.startMethod(methodName);

    popUpList_.setSelectedIndex(index);
    log_.endMethod();
  }


  /**
   * Gets the SelectedItem attribute of the PopUpButton object
   *
   * @return   The SelectedItem value
   */
  public Object getSelectedItem()
  {
    final String methodName = className_ + ": getSelectedItem()";
    log_.startMethod(methodName);
    log_.endMethod();
    return (Object) ((DefaultListModel)((JList) popUpList_).getModel()).
      getElementAt(getSelectedIndex());
  }


  /**
   * Gets the SelectedIndex attribute of the PopUpButton object
   *
   * @return   The SelectedIndex value
   */
  public int getSelectedIndex()
  {
    final String methodName = className_ + ": getSelectedIndex()";
    log_.startMethod(methodName);
    int num = popUpList_.getSelectedIndex();
    log_.debug("\nSelected Item num = "+num);
    log_.endMethod();
    return num;
  }


  /**
   * Gets the ItemCount attribute of the PopUpButton object
   *
   * @return   The ItemCount value
   */
  public int getItemCount()
  {
    final String methodName = className_ + ": getSelectedIndex()";
    log_.startMethod(methodName);
    int num = ((DefaultListModel)((JList) popUpList_).getModel()).getSize();
    log_.debug("ItemCount = " + num);
    log_.endMethod();
    return num;
  }


  /**
   * Gets the Log object that the PopUpButton is using.
   *
   * @return   The Log .
   */
  Log getLog()
  {
    return log_;
  }

  protected void sortDefaultList(DefaultListModel dlm)
  {
    final String methodName = className_ + ": sortDefaultList()";
    log_.startMethod(methodName);

    if (dlm != null)
    {
      try
      {
        int numItems = dlm.getSize();
        String[] a = new String[numItems];
        for (int i=0; i < numItems; i++)
        {
          a[i] = (String) dlm.getElementAt(i);
        }
        sortArray(Collator.getInstance(),a);
        // Locale loc = Locale.FRENCH;
        // sortArray(Collator.getInstance(loc), (String[])a);
        for (int i=0; i < numItems; i++)
        {
          dlm.setElementAt(a[i], i);
        }
      }
      catch (Exception ex)
      {
        log_.minor("Threw a:",ex);
      }
    }
    log_.endMethod();
  }


  protected void sortArray(Collator collator, String[] strArray)
  {
    final String methodName = className_ + ": sortArray(Collator, String[])";
    log_.startMethod(methodName);
    String tmp;
    int lengthOfArray = strArray.length;
    log_.debug("Sorting " + lengthOfArray  + " items.");
    if (lengthOfArray == 1) return;
    for (int i = 0; i < lengthOfArray; i++)
    {
      for (int j = i + 1; j < lengthOfArray; j++)
      {
        if( collator.compare(strArray[i], strArray[j] ) > 0 )
        {
          tmp = strArray[i];
          strArray[i] = strArray[j];
          strArray[j] = tmp;
        }
      }
    }
    log_.endMethod();
  }

  /**
   * Toggles the popup to show or not show depending on if it is currently shown.
   */
  protected void togglePopUp()
  {
    final String methodName = className_ + ": togglePopUp";
    log_.startMethod(methodName);
    if (popUpIsShowing_)
    {
      popUpIsShowing_ = showPopUp(false);
    }
    else
    {
      popUpIsShowing_ = showPopUp(true);
    }
    log_.endMethod();
  }


  /**
   * Shows or Removes the JList Popup depending on the passed parm.
   *
   * @param showIt  flag to show or remove the popup.
   * @return        Description of the Returned Value
   */
  boolean showPopUp(boolean showIt)
  {
    final String methodName = className_ + ": showPopUp " + showIt;
    log_.startMethod(methodName);
    /*
     *  int frameX = appFrame_.getX();
     *  int frameY = appFrame_.getY();
     *  int framedX = appFrame_.getHeight();
     *  int framedY = appFrame_.getWidth();
     */
    int buttonX = getX();
    int buttonY = getY();
    int buttondY = getHeight();
    int buttondX = getWidth();
    /*
     *  int frameBorderWidth = (framedY - buttondY) / 2;
     *  int frameTitleBarHeight = framedX - buttondX - frameBorderWidth;
     *  log_.debug("Window Sizes:");
     *  log_.debug("  AppFrame  X=" + frameX);
     *  log_.debug("  AppFrame  Y=" + frameY);
     *  log_.debug("  AppFrame dX=" + framedX);
     *  log_.debug("  AppFrame dY=" + framedY);
     *  log_.debug("  Border   dX=" + frameBorderWidth);
     *  log_.debug("  TitleBar dY=" + frameTitleBarHeight);
     *  popUpPosition_.setLocation(frameX+frameBorderWidth,
     *  frameY+buttonY+buttondX+frameTitleBarHeight);
     */
    JFrame jf = ((JFrame) KiwiUtils.getFrameForComponent(this));
    Container parent = this.getParent();
    Container parent2 = null;
    Container parent3 = null;
    int parentX = 0;
    int parentY = 0;
    int parent2X = 0;
    int parent2Y = 0;
    int parent3X = 0;
    int parent3Y = 0;
    int jfX = 0;
    int jfY = 0;
    int jfdX = 0;
    int jfdY = 0;
    // Test the screen size to see if the pop up will fit below the button
    // if not put it above the button
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int compSize = 0;
    if (jf != null)
    {
      jfX = jf.getX();
      jfY = jf.getY();
      jfdX = jf.getWidth();
      jfdY = jf.getHeight();
      compSize += jfY;
      /*
      log_.debug("  JFrame    X=" + jfX);
      log_.debug("  JFrame    Y=" + jfY);
      log_.debug("  JFrame    dX=" + jfdX);
      log_.debug("  JFrame    dY=" + jfdY);*/
    }
    if (parent != null)
    {
      parentX = parent.getX();
      parentY = parent.getY();
      parent2 = parent.getParent();
      compSize += parentY;
      /*
      log_.debug("  Parent    X=" + parentX);
      log_.debug("  Parent    Y=" + parentY);*/
    }
    if (parent2 != null)
    {
      parent2X = parent2.getX();
      parent2Y = parent2.getY();
      parent3 = parent2.getParent();
      compSize += parent2Y;
      /*
      log_.debug("  Parent2    X=" + parent2X);
      log_.debug("  Parent2    Y=" + parent2Y);*/
    }
    if (parent3 != null)
    {
      parent3X = parent3.getX();
      parent3Y = parent3.getY();
      compSize += parent3Y;
      /*
      log_.debug("  Parent3    X=" + parent3X);
      log_.debug("  Parent3    Y=" + parent3Y);*/
    }
    compSize +=157;
    compSize +=2*buttondY;
      /*
    log_.debug("  Button    X=" + buttonX);
    log_.debug("  Button    Y=" + buttonY);
    log_.debug("  Button   dX=" + buttondX);
    log_.debug("  Button   dY=" + buttondY);
    log_.debug("  Screen   dY=" + screenSize.height);
    log_.debug("  PopWinHeight" + popUpWin_.getHeight());
    log_.debug("Comparing Screen Height " + screenSize.height + " to " +
      compSize);*/
    if (screenSize.height > compSize )
    {
      //above
      log_.debug("Placing Popup below button");
      popUpPosition_.setLocation(jfX + buttonX + parentX,
                                 jfY+parentY+parent2Y+ 2*buttondY);//jfY + buttonY + parentY + 2 * buttondY);
    }
    else
    {
      //below
      log_.debug("Placing Popup above button");
      popUpPosition_.setLocation(jfX + buttonX + parentX,
                                 jfY+parentY+parent2Y+buttondY-157);
    }

    //popUpWin_.setSize(buttondX,buttondY*4);
    popUpWin_.pack();
    popUpWin_.setLocation(popUpPosition_);
    int selectIndex = popUpList_.getSelectedIndex();
    if (selectIndex >= 0)
    {
      popUpList_.ensureIndexIsVisible(selectIndex);
    }
    popUpWin_.setVisible(showIt);
    if (showIt)
    {
      popUpWin_.show();
      popUpWin_.toFront();
      //popUpWin_.setSize(buttondX,buttondY*4);
    }
    log_.debug("new PopUp Position = " + popUpPosition_.getX() + ", " +
      popUpPosition_.getY());
    log_.debug("new PopUp Size x,y = " + popUpWin_.getWidth() + ", " +
      popUpWin_.getHeight());

    log_.endMethod();
    return showIt;
  }


  /**
   * A private mouse listener for the popup list.NOTHING really implemented yet
   (other than som terminal output debugging).
   *
   * @author    Tom Gutwin P.Eng
   * @created   October 15, 2001
   */
  class ListMouseListener extends MouseAdapter
  {
    /**
     * Mouse Clicked Listener method.
     *
     * @param e  The actual MouseEvent
     */
    public void mouseClicked(MouseEvent e)
    {
      int index = popUpList_.locationToIndex(e.getPoint());
      //System.out.println("  Mouse Click: " + e.getClickCount() +
       // " click on Item " + index);
    }


    /**
     * Mouse Pressed Listener method.
     *
     * @param e  The actual MouseEvent
     */
    public void mousePressed(MouseEvent e)
    {
      int index = popUpList_.locationToIndex(e.getPoint());
      //System.out.println("  Mouse Press on Item " + index);
    }


    /**
     * Mouse Entered Listener method.
     *
     * @param e  The actual MouseEvent
     */
    public void mouseEntered(MouseEvent e)
    {
      int index = popUpList_.locationToIndex(e.getPoint());
      //System.out.println("  Mouse Enter Item " + index);
    }
  }
}




