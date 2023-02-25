/*
 *  $Source: /cvsroot2/open/projects/WebARTS/ca/bc/webarts/widgets/JCheckDropDown.java,v $
 *  $Name:  $
 *  $Revision: 1.7 $
 *  $Date: 2003/11/02 03:42:29 $
 *  $Locker:  $
 */
/*
 *  JCheckDropDown -- A Widget Class to mimic a toggle drop down box which has
 *  JCheckBoxes inside its popup.
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
import ca.bc.webarts.widgets.PopUpButton;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
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
import java.util.Arrays;
import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JWindow;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import kiwi.util.KiwiUtils;

/**
 * A custom Drop Down box that holds JCheckBox items in its list. It also requires
 * the user to select the button to open and close the popup list.
 *
 * @author    Tom Gutwin P.Eng
 * @created   October 15, 2001
 */
public class JCheckDropDown extends PopUpButton
{

  /**
   * A Class holder for its name (used in Logging).
   */
  private static String className_ = "JCheckDropDown";
  /**
   * The Log Filename.
   */
  private static String logFile_ = "." + File.separator + className_+ "Log.txt";
  /**
   * The Log that will get used.
   */
  protected static Log log_ = Log.createLog(Log.DEBUG, logFile_);
  /**
   * The number of items in the list.
   **/
   private int numItems_ = 0;
  /**
   * The number of items in the list That Are Checked..
   **/
   private int numItemsChecked_ = 0;
  /**
   * A Cache of the Checked items.
   **/
  private Vector checkedItems_ = new Vector();


  /**
   * Constructor that instantiates the popup list using the passed in Array and
   * basic renderer.
   *
   * @param jl  the array of JCheckboxes to add (make sure they are pre
                selected)
   */
  public JCheckDropDown(JCheckBox[] jl)
  {
    super();
    final String methodName = className_ + ": JCheckDropDown(JCheckBox[])";
    //log_ = Log.createLog(Log.FULL, logFile_);
    log_.startMethod(methodName);
    if (jl != null && jl.length > 0)
    {
      numItems_ = jl.length;
      for (int i=0 ;i < numItems_; i++)
        listModel_.addElement(jl[i]);
      popUpList_ = new JList(listModel_);
    }
    else
    {
      log_.minor("JCheckDropDown: The passed in Array is Null" +
        " (or empty), initializing with an empty JList Popup.");
      popUpList_ = new JList(listModel_);
    }
    //setHorizontalTextPosition(LEFT);
    //setHorizontalAlignment(LEFT);
    popUpList_.setSelectionBackground(Color.darkGray);
    popUpList_.setCellRenderer(new CheckBoxRenderer());
    popUpList_.addMouseListener(new ListCheckBoxMouseListsener());

    popUpList_.setVisibleRowCount(6);
    popUpList_.setBorder(null);
    popUpScroller_.getViewport().setView(popUpList_);
    popUpPanel_.add(popUpScroller_);
    popUpWin_.getContentPane().add(popUpPanel_);

    addActionListener(PopUpButtonActionListener_);
    log_.endMethod();
  }


  /**
   * Constructor that instantiates the popup list using the passed in Array and
   * basic renderer.
   *
   * @param items  Description of Parameter
   */
  public JCheckDropDown(String[] items)
  {
    super();
    final String methodName = className_ + ": JCheckDropDown(String[])";
    //log_ = Log.createLog(Log.FULL, logFile_);
    log_.startMethod(methodName);

    JCheckBox[] jl = null;
    if (items != null && items.length > 0)
    {
      numItems_ = items.length;

      /*System.out.println("\n UN-Sorted List:");
      for (int i = 0; i < numItems_; i++)
      {
        System.out.println(" Item "+i+" "+items[i]);
      }
      System.out.println("\n Sorted List:");*/

      Arrays.sort(items, String.CASE_INSENSITIVE_ORDER);

      /*
      for (int i = 0; i < numItems_; i++)
      {
        System.out.println(" Item "+i+" "+items[i]);
      }
      */

      log_.debug("Adding " + numItems_ + " items.");
      jl = new JCheckBox[numItems_];
      for (int i = 0; i < numItems_; i++)
      {
        jl[i] = new JCheckBox(items[i], true);
      }
      numItemsChecked_ = numItems_;
      for (int i = 0; i < numItems_; i++)
      {
        listModel_.addElement(jl[i]);
      }
    }
    else
    {
      log_.minor("JCheckDropDown: The passed in Array is Null" +
        " (or empty), initializing with an empty JList Popup.");
    }
    popUpList_ = new JList(listModel_);

    //setHorizontalTextPosition(LEFT);
    //setHorizontalAlignment(LEFT);
    popUpList_.setSelectionBackground(Color.darkGray);
    popUpList_.setCellRenderer(new CheckBoxRenderer());
    popUpList_.addMouseListener(new ListCheckBoxMouseListsener());

    popUpList_.setVisibleRowCount(6);
    popUpList_.setBorder(null);
    popUpScroller_.getViewport().setView(popUpList_);
    popUpPanel_.add(popUpScroller_);
    popUpWin_.getContentPane().add(popUpPanel_);

    addActionListener(PopUpButtonActionListener_);
    log_.endMethod();
  }


  /**
   * The main test program for the JCheckDropDown class
   *
   * @param args  The command line arguments
   */
  public static void main(String[] args)
  {
    appFrame_ = new JFrame();
    appFrame_.getContentPane().setLayout(new BorderLayout());
    appFrame_.setTitle("JCheckDropDown TEST Window.");
    Arrays.sort(SAMPLE_LIST_DATA, String.CASE_INSENSITIVE_ORDER);
    JCheckBox[] jc = new JCheckBox[SAMPLE_LIST_DATA.length];
    for (int i = 0; i < jc.length; i++)
    {
      jc[i] = new JCheckBox(SAMPLE_LIST_DATA[i], true);
    }
    final JCheckDropDown myTestButton = new JCheckDropDown(jc);
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
   * Gets the ItemChecked attribute of the JCheckDropDown object
   *
   * @param itemNum  Description of Parameter
   * @return         The ItemChecked value
   */
  public boolean isItemChecked(int itemNum)
  {
    final String methodName = className_ + ": isItemChecked(int)";
    log_.startMethod(methodName);
    boolean retVal = false;

    retVal = getItem(itemNum).isSelected();

    log_.debug("Item " + itemNum + " is Checked?? " + retVal);
    log_.endMethod();
    return retVal;
  }


  /**
   * Checks the checkbox for th especified item
   *
   * @param itemNum  Description of Parameter
   * @param checkit or not checkit
   * @return         The ItemChecked value
   */
  public void checkItem(int itemNum, boolean checkIt)
  {
    final String methodName = className_ + ": checkItem(int,"+checkIt+")";
    log_.startMethod(methodName);

    getItemNonLogged(itemNum).setSelected(checkIt);
    if (checkIt)
      numItemsChecked_++;
    else
      numItemsChecked_--;

    log_.endMethod();
  }


  /**
   * Checks ALL the checkboxes with the specified value.
   *
   * @param checkit or not checkit
   * @return         The ItemChecked value
   */
  public void checkAllItems(boolean checkIt)
  {
    final String methodName = className_ + ": checkItem("+checkIt+")";
    log_.startMethod(methodName);

    for (int i = 0; i < numItems_; i++)
    {
      getItemNonLogged(i).setSelected(checkIt);
    }
    if (checkIt)
      numItemsChecked_ = numItems_;
    else
      numItemsChecked_ = 0;

    log_.endMethod();
  }


  /**
   * Gets the Item attribute of the JCheckDropDown object
   *
   * @param itemNum  Description of Parameter
   * @return         The Item value or null if non-existant item
   */
  public JCheckBox getItem(int itemNum)
  {
    final String methodName = className_ + ": getItem(int)";
    log_.startMethod(methodName);

    JCheckBox retVal = null;
    if (numItems_ > 0 && itemNum < numItems_)
    {
      retVal = ((JCheckBox) ((JList) popUpList_).getModel().
      getElementAt(itemNum));
    }
    log_.endMethod();
    return retVal;
  }


  /**
   * Gets the Item attribute of the JCheckDropDown object without logging.
   *
   * @param itemNum  Description of Parameter
   * @return         The Item value
   */
  private JCheckBox getItemNonLogged(int itemNum)
  {
    JCheckBox retVal = ((JCheckBox) ((JList) popUpList_).getModel().
      getElementAt(itemNum));
    return retVal;
  }


  /**
   * Gets the ItemName attribute of the JCheckDropDown object
   *
   * @param itemNum  Description of Parameter
   * @return         The ItemName value
   */
  public String getItemName(int itemNum)
  {
    final String methodName = className_ + ": getItemName(int)";
    log_.startMethod(methodName);

    String retVal = "";
    if (numItems_ > 0 && itemNum < numItems_)
    {
      JCheckBox jc = getItemNonLogged(itemNum);
      if (jc != null)
      {
        retVal = jc.getText();
      }
    }

    log_.endMethod();
    return retVal;
  }


  /**
   * Returns the numItemsChecked_ field.
   *
   * @return   The numItemsChecked_ value
   */
  public int getNumItemsChecked()
  {
    final String methodName = className_ + ": getNumItemsChecked()";
    //log_.startMethod(methodName);
    //log_.endMethod();
    return numItemsChecked_;
  }


  /**
   * Returns the numItems_ field. The number of items in this object.
   *
   * @return   The numItems_ value
   */
  public int getNumItems()
  {
    final String methodName = className_ + ": getNumItems()";
    //log_.startMethod(methodName);
    //log_.endMethod();
    return numItems_;
  }


  /**
   * Iterates through the list checking to see how many of the contained
   * JCheckBoxes are checked.
   *
   * @return   The NumItemsChecked value
   */
  private int reGetNumItemsChecked()
  {
    final String methodName = className_ + ": getNumItemsChecked()";
    log_.startMethod(methodName);
    int retVal = 0;
    for (int i = 0; i < numItems_; i++)
    {
      if (isItemChecked(i))
      {
        retVal++;
      }
    }

    log_.endMethod();
    return retVal;
  }


  /**
   * Gets the RandomCheckedItemNum attribute of the JCheckDropDown object
   *
   * @return   The RandomCheckedItemNum value
   */
  public int getRandomCheckedItemNum()
  {
    final String methodName = className_ + ": getRandomCheckedItem()";
    log_.startMethod(methodName);
    int retVal = -1;
    boolean notDone = true;

    log_.debug(methodName+"numItems_="+numItems_+
      "numItemsChecked_="+numItemsChecked_);
    if (numItemsChecked_ > 0)
    {
      while (notDone)
      {
        retVal = (int) (Math.random() * numItems_);
        // go direct to the getItem method for speed (bypasses logging etc)
        if (getItemNonLogged(retVal).isSelected()) //!isItemChecked(retVal))
        {
          notDone = false;
        }
      }
    }

    log_.endMethod();
    return retVal;
  }


  /**
   * Gets the NextCheckedItemNum attribute of the JCheckDropDown object
   *
   * @return   The NextCheckedItemNum value
   */
  public int getNextCheckedItemNum()
  {
    final String methodName = className_ + ": getNextCheckedItemNum()";
    log_.startMethod(methodName);
    int retVal = getSelectedIndex();
    int numItems = getItemCount();
    boolean firstTimeInLoop = true;

    log_.debug("Current Selected Item Num="+retVal);
    log_.debug("Current Selected Item Name="+getSelectedItemName());
    if (numItemsChecked_ > 0)
    {
      while (firstTimeInLoop || !isItemChecked(retVal))
      {
        firstTimeInLoop = false;
        if (retVal < numItems - 1)
        {
          retVal++;
        }
        else
        {
          retVal = 0;
        }
      }
    }
    log_.debug("NEXT Selected Item Num="+retVal);

    log_.endMethod();
    return retVal;
  }


  /**
   * Gets the SelectedItemName attribute of the JCheckDropDown object
   *
   * @return   The SelectedItemName value
   */
  public String getSelectedItemName()
  {
    final String methodName = className_ + ": getSelectedItemName()";
    log_.startMethod(methodName);
    String retVal = "";
    int sel = getSelectedIndex();
    if (!(sel < 1))
    {
      retVal = ((JCheckBox) ((JList) popUpList_).getModel().
        getElementAt(sel-1)).getText();
    }
    else if (sel==0)
    {
      retVal = ((JCheckBox) ((JList) popUpList_).getModel().
        getElementAt(0)).getText();
    }

    log_.endMethod();
    return retVal;
  }


  /**
   * Gets the Index for the item that matches the name passedin.
   *
   * @param item  Description of Parameter
   * @return      The JCheckBoxIndex value
   */
  public int getJCheckBoxIndex(String item)
  {
    final String methodName = className_ + ": getJCheckBoxIndex(String)";
    log_.startMethod(methodName);

    int index = 0;
    int size = ((JList) popUpList_).getModel().getSize();
    for (int i = 0; index == 0 && i < size; i++)
    {
      JCheckBox jc = (JCheckBox) ((JList) popUpList_).getModel().getElementAt(i);
      if (jc.getText().equals(item))
      {
        index = i;
      }
    }
    log_.endMethod();
    return index;
  }

  /**
   * Sets the SelectedIndex attribute of the PopUpButton object.
   *
   * @param index  The new SelectedIndex value
   */
  public void setSelectedIndex(int index)
  {
    super.setSelectedIndex(index);
    final String methodName = className_ + ": setSelectedIndex("+index+")";
    log_.startMethod(methodName);
    setText(getItemName(index));
    log_.endMethod();
  }


  public void sortDefaultList(DefaultListModel dlm)
  {
    final String methodName = className_ + ": sortDefaultList()";
    log_.startMethod(methodName);

    if (dlm != null)
    {
      try
      {
        int numItems = dlm.getSize();
        JCheckBox[] a = new JCheckBox[numItems];
        JCheckBox tmpCheckBox = null;
        for (int i=0; i < numItems; i++)
        {
            a[i] = tmpCheckBox;
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


  private void sortArray(Collator collator, JCheckBox[] checkedArray)
  {
    final String methodName = className_ + ": sortArray(Collator, JCheckBox[])";
    log_.startMethod(methodName);
    JCheckBox tmp;
    int lengthOfArray = checkedArray.length;
    log_.debug("Sorting " + lengthOfArray  + " items.");
    if (lengthOfArray == 1) return;
    for (int i = 0; i < lengthOfArray; i++)
    {
      for (int j = i + 1; j < lengthOfArray; j++)
      {
        if( collator.compare(checkedArray[i].getText(),
                             checkedArray[j].getText() ) > 0 )
        {
          tmp = checkedArray[i];
          checkedArray[i] = checkedArray[j];
          checkedArray[j] = tmp;
        }
      }
    }
    log_.endMethod();
  }


   /**
   * Adds a feature to the Item attribute of the JCheckDropDown object
   *
   * @param jc  The feature to be added to the Item attribute
   */
  public void addItem(JCheckBox jc)
  {
    final String methodName = className_ + ": addItem(JCheckBox)";
    log_.startMethod(methodName);

    listModel_.addElement(jc);
    //((DefaultListModel)((JList) popUpList_).getModel()).addElement(jc);
    numItems_++;
    if (jc.isSelected())
      numItemsChecked_++;
    //sortDefaultList((DefaultListModel)popUpList_.getModel());
    log_.endMethod();
  }


  /**
   * Removes all Items from the DropDown List.
   */
  public void removeAllItems()
  {
    final String methodName = className_ + ": removeAllItems()";
    log_.startMethod(methodName);

    listModel_.removeAllElements();
    numItems_ = 0;
    numItemsChecked_=0;
    log_.endMethod();
  }


  /**
   * Removes the named item from the list of items.
   *
   * @param item  The name of the item to remove
   */
  public void removeItem(String item)
  {
    final String methodName = className_ + ": removeItem(): "+item;
    log_.startMethod(methodName);

    try
    {
      int index = getJCheckBoxIndex(item);
      JCheckBox jc = getItemNonLogged(index);
      if (jc.isSelected())
        numItemsChecked_++;

      listModel_.remove(index);
      numItems_--;
    }
    catch (java.lang.Exception ex)
    {
      ex.printStackTrace();
      log_.major("Exception Thrown: Delete from PlayList FAILED:"+item, ex);
    }
    log_.endMethod();
  }


  /**
   * Listens for mouse clicks on the listbox. It then decides where the
   * click happened so it can either work the checkbox or select an item.
   *
   * @author    Tom Gutwin P.Eng
   * @created   October 15, 2001
   */
  class ListCheckBoxMouseListsener extends MouseAdapter
  {
    /**
     * Listener for the mouse clickmethod
     *
     * @param e  Description of Parameter
     */
    public void mouseClicked(MouseEvent e)
    {
      Point pnt = e.getPoint();
      double xLocation = pnt.getX();
      int index = ((JList) popUpList_).locationToIndex(pnt);
      int clkCount = e.getClickCount();
      //System.out.println("  !Mouse Click: " + clkCount +
      //  " click on Item " + index);
      //System.out.println("  at point " + pnt);
      JCheckBox jc = (JCheckBox) ((JList) e.getSource()).
        getModel().getElementAt(index);
      if (xLocation < 13)
      {
        if (!jc.isSelected())
        {
          //System.out.print("  CHECK it: ");
          jc.setSelected(true);
          numItemsChecked_++;
        }
        else
        {
          //System.out.print("  UNCHECK it: ");
          jc.setSelected(false);
          numItemsChecked_--;
        }
      }
      else
      {
        //System.out.print("  Select it: ");
        ((JList) popUpList_).setSelectionBackground(Color.red);
        Color selectedBack = ((JList) popUpList_).getSelectionBackground();
        Color selectedFront = ((JList) popUpList_).getSelectionForeground();
        //System.out.println("  back = " + selectedBack + "  front = " + selectedFront);
        jc.setBackground(Color.red);
        jc.repaint();
        //boolean isSelected = popUpList_.getSelectedIndex() == index;
        //popUpList_.clearSelection();
        //if(isSelected)
        //{
        //  System.out.println("DE-Selecting index "+ index);
        //}
        //else
        //{
        //  System.out.println("Selecting index "+ index);
        //  popUpList_.setSelectedIndex(index);
        //}
        setText(getItemName(index));

        // close the window
        togglePopUp();
      }
      popUpList_.repaint();
    }


    /**
     * Listener for a mouse press.
     *
     * @param e  Description of Parameter
     */
    public void mousePressed(MouseEvent e)
    {
      int index = popUpList_.locationToIndex(e.getPoint());
      //System.out.println("  Mouse Press on Item " + index);
    }


    /**
     * Description of the Method
     *
     * @param e  Description of Parameter
     */
    public void mouseEntered(MouseEvent e)
    {
      int index = popUpList_.locationToIndex(e.getPoint());
      //System.out.println("  Mouse Enter Item " + index);
    }
  }


  /**
   * Description of the Class
   *
   * @author    Tom Gutwin P.Eng
   * @created   October 15, 2001
   */
  class ListListener implements ListDataListener
  {
    /**
     * Description of the Method
     *
     * @param e  Description of Parameter
     */
    public void contentsChanged(ListDataEvent e)
    {
      int index0 = e.getIndex0();
      int index1 = e.getIndex1();
      log_.debug("List Item Changed " + index0 + " " + index1);
      popUpList_.repaint();
    }


    /**
     * Description of the Method
     *
     * @param e  Description of Parameter
     */
    public void intervalAdded(ListDataEvent e)
    {
      int index0 = e.getIndex0();
      int index1 = e.getIndex1();
      log_.debug("List intervalAdded " + index0 + " " + index1);
      popUpList_.revalidate();
    }


    /**
     * Description of the Method
     *
     * @param e  Description of Parameter
     */
    public void intervalRemoved(ListDataEvent e)
    {
      int index0 = e.getIndex0();
      int index1 = e.getIndex1();
      log_.debug("List intervalRemoved " + index0 + " " + index1);
      //contentsChanged( e );
    }

  }


  /**
   * Description of the Class
   *
   * @author    Tom Gutwin P.Eng
   * @created   October 15, 2001
   */
  class CheckBoxRenderer extends JCheckBox implements ListCellRenderer
  {
    /**
     * Constructor for the CheckBoxRenderer object
     */
    public CheckBoxRenderer()
    {
      setOpaque(true);
    }


    /**
     * Gets the ListCellRendererComponent attribute of the CheckBoxRenderer object
     *
     * @param list          Description of Parameter
     * @param value         Description of Parameter
     * @param index         Description of Parameter
     * @param isSelected    Description of Parameter
     * @param cellHasFocus  Description of Parameter
     * @return              The ListCellRendererComponent value
     */
    public Component getListCellRendererComponent(
      JList list,
      Object value,  // value to display
    int index,  // cell index
    boolean isSelected,  // is the cell selected
    boolean cellHasFocus)
    { // the list and the cell have the focus

      JCheckBox jc = (JCheckBox) value;
      setText(jc.getText());
      setSelected(jc.isSelected());
      return (Component) this;
    }
  }

}




