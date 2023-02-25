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
   $Log: StateIndicator.java,v $
   Revision 1.1  2003/11/07 19:19:01  markl
   New class.

   ----------------------------------------------------------------------------
*/

package kiwi.ui;

import java.awt.*;
import java.util.*;
import javax.swing.*;

import kiwi.util.KiwiUtils;

/** A graphical state indicator component. This component displays one
 * of a collection of images, depending on what its current "state"
 * is. Each state is indicated by a unique string; the component maintains a
 * mapping of these strings to the corresponding state icons.
 * <p>
 * This component can be used to create status icons and other
 * types of multi-state indicators.
 *
 * @see kiwi.ui.ToggleIndicator
 *
 * @since Kiwi 1.4.3
 *
 * @author Mark Lindner
 */

public class StateIndicator extends JLabel
  {
  private String state = null;
  private Hashtable icons = new Hashtable();
  private Icon defaultIcon;

  /** Construct a new <code>StateIndicator</code>.
   *
   * @param icon The default icon to display when the state is unknown.
   */
  
  public StateIndicator(Icon defaultIcon)
    {
    this.defaultIcon = defaultIcon;

    setHorizontalAlignment(SwingConstants.CENTER);
    setVerticalAlignment(SwingConstants.CENTER);

    setIcon(defaultIcon);
    }

  /**
   * Remove all states from the indicator.
   */

  public void clearStates()
    {
    icons.clear();
    }

  /**
   * Add a new state to the indicator.
   *
   * @param state The name of the state.
   * @param icon The icon to display for this state.
   */

  public void addState(String state, Icon icon)
    {
    icons.put(state, icon);
    }

  /**
   * Remove a state from the indicator.
   *
   * @param state The name of the state.
   */

  public void removeState(String state)
    {
    icons.remove(state);
    }

  /** Set the state of the indicator.
   *
   * @param state The new state for the indicator. The indicator will
   * be repainted immediately. If the specified <code>state</code> is
   * invalid, the state will be set to <code>null</code> and the icon
   * to default icon.
   */
  
  public synchronized void setState(String state)
    {
    Icon icon = (Icon)icons.get(state);
    if(icon == null)
      {
      icon = defaultIcon;
      this.state = null;
      }
    else
      this.state = state;
    
    setIcon(icon);
    KiwiUtils.paintImmediately(this);
    }

  /** Get the current state of the indicator.
   *
   * @return The current state of the indicator, which may be
   * <code>null</code>.
   */

  public synchronized String getState()
    {
    return(state);
    }

  }

/* end of source file */
