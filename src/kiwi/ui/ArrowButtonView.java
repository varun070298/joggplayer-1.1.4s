/* ----------------------------------------------------------------------------
   The Kiwi Toolkit
   Copyright (C) 1998-2003 Mark A. Lindner

   This file is part of Kiwi.
   
   This library is free software; you can redistribute it and/or
   modify it under the terms of the GNU Library General Public
   License as published by the Free Software Foundation; either
   version 2 of the License, or (at your option) any later version.

   This library is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   Library General Public License for more details.

   You should have received a copy of the GNU Library General Public
   License along with this library; if not, write to the Free
   Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 
   The author may be contacted at:
   
   mark_a_lindner@yahoo.com
   ----------------------------------------------------------------------------
   $Log: ArrowButtonView.java,v $
   Revision 1.4  2003/01/19 09:50:52  markl
   Javadoc & comment header updates.

   Revision 1.3  2001/03/12 09:27:51  markl
   Source code and Javadoc cleanup.

   Revision 1.2  1999/01/10 01:00:58  markl
   added GPL header & RCS tag
   ----------------------------------------------------------------------------
*/

package kiwi.ui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

import kiwi.event.*;
import kiwi.util.*;

/** This class represents a panel of VCR-style buttons, including <i>First</i>,
  * <i>Previous</i>, <i>Next</i>, and <i>Last</i> buttons. These buttons
  * represent a bounded range of items that may be traversed.
  * <p>
  * The component maintains the current position within the list being
  * traversed. This position may be retrieved at any time via a call
  * to the <code>getPosition()</code> method. Additionally, an
  * <code>ActionEvent</code> is generated each time one of the four
  * buttons is pressed; one of the command strings "first", "prev",
  * "next", or "last" is passed as the argument of the
  * <code>ActionEvent</code> to specify which button was pressed.
  * <p>
  * The <i>First</i> and <i>Previous</i> buttons are dimmed when the component
  * is at the "beginning" of the range, and the <i>Next</i> and <i>Last</i>
  * buttons are dimmed when the component is at the "end" of the range.
  *
  * <p><center>
  * <img src="snapshot/ArrowButtonView.gif"><br>
  * <i>An example ArrowButtonView.</i>
  * </center>
  *
  * @author Mark Lindner
  */

public class ArrowButtonView extends KPanel
  {
  private static final int FIRST = 0, PREV = 1, NEXT = 2, LAST = 3;
  private static final String commands[] = { "first", "prev", "next", "last" };
  private KButton b_first, b_prev, b_next, b_last;
  private int range, pos = 0, action;
  private _ActionListener actionListener;
  private ActionSupport support;
  
  /** Construct a new <code>ArrowButtonView</code>.
    *
    * @param range The number of items being traversed.
    *
    * @exception java.lang.IllegalArgumentException If <code>range</code> is
    * less than 0.
    */

  public ArrowButtonView(int range) throws IllegalArgumentException
    {
    super();

    if(range < 0)
      throw(new IllegalArgumentException("Range must be nonnegative."));

    this.range = range;

    setLayout(new GridLayout(1, 4, 0, 0));

    support = new ActionSupport(this);
    actionListener = new _ActionListener();
    
    ResourceManager rm = KiwiUtils.getResourceManager();

    b_first = new KButton(rm.getIcon("firstp.gif"));
    b_first.addActionListener(actionListener);
    add(b_first);

    b_prev = new KButton(rm.getIcon("leftp.gif"));
    b_prev.addActionListener(actionListener);
    add(b_prev);

    b_next = new KButton(rm.getIcon("rightp.gif"));
    b_next.addActionListener(actionListener);
    add(b_next);

    b_last = new KButton(rm.getIcon("lastp.gif"));
    b_last.addActionListener(actionListener);
    add(b_last);

    resetButtons();
    }

  /* reset button states */

  private void resetButtons()
    {
    b_first.setEnabled(true);
    b_prev.setEnabled(true);
    b_next.setEnabled(true);
    b_last.setEnabled(true);

    if(pos == 0)
      {
      b_first.setEnabled(false);
      b_prev.setEnabled(false);
      }

    if(pos == (range - 1))
      {
      b_next.setEnabled(false);
      b_last.setEnabled(false);
      }
    }

  /** Get the traversal position. Returns the current traversal position, which
    * will be an integer in the range [0, <code>getRange() - 1</code>],
    * inclusive.
    *
    * @return The current position.
    */

  public int getPosition()
    {
    return(pos);
    }

  /** Get the traversal range. Returns the number of items being traversed by
    * this component.
    *
    * @return The range.
    */

  public int getRange()
    {
    return(range);
    }

  /** Add an <code>ActionListener</code> to this component's list of listeners.
    *
    * @param listener The listener to add.
    */

  public void addActionListener(ActionListener listener)
    {
    support.addActionListener(listener);
    }

  /** Remove an <code>ActionListener</code> from this component's list of
    * listeners.
    *
    * @param listener The listener to remove.
    */
  
  public void removeActionListener(ActionListener listener)
    {
    support.removeActionListener(listener);
    }

  /* Handle events. */

  private class _ActionListener implements ActionListener
    {
    public void actionPerformed(ActionEvent ev)
      {
      Object o = ev.getSource();
      
      if(o == b_first)
        {
        pos = 0;
        resetButtons();
        support.fireActionEvent(commands[FIRST]);
        }
      else if(o == b_prev)
        {
        if(pos > 0) pos--;
        resetButtons();
        support.fireActionEvent(commands[PREV]);
        }
      else if(o == b_next)
        {
        if(pos < (range - 1)) pos++;
        resetButtons();
        support.fireActionEvent(commands[NEXT]);
        }
      else if(o == b_last)
        {
        pos = range - 1;
        resetButtons();
        support.fireActionEvent(commands[LAST]);
        }
      }
    }

  }

/* end of source file */
