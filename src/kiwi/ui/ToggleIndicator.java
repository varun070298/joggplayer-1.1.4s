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
   $Log: ToggleIndicator.java,v $
   Revision 1.5  2003/01/19 09:50:54  markl
   Javadoc & comment header updates.

   Revision 1.4  2001/03/12 09:28:00  markl
   Source code and Javadoc cleanup.

   Revision 1.3  1999/04/25 04:23:55  markl
   Added javadoc screenshot.

   Revision 1.2  1999/04/23 07:40:14  markl
   Added synchronization.

   Revision 1.1  1999/04/23 07:25:31  markl
   Initial revision
   ----------------------------------------------------------------------------
*/

package kiwi.ui;

import java.awt.*;
import javax.swing.*;

import kiwi.util.KiwiUtils;

/** A graphical toggle component. This component displays one of two images,
 * depending on whether it is "toggled" on or off. This component can be used
 * to create LEDs and other types of on-off indicators.
 *
 * <p><center>
 * <img src="snapshot/ToggleIndicator.gif"><br>
 * <i>Some example ToggleIndicators.</i>
 * </center>
 *
 * @author Mark Lindner
 * 
 */

public class ToggleIndicator extends JLabel
  {
  private Icon icon, altIcon;
  private boolean state = false;

  /** Construct a new <code>ToggleIndicator</code> with the specified icons for
   * the "on" and "off" states.
   *
   * @param icon The icon to display when the toggle is off.
   * @param altIcon The icon to display when the toggle is on.
   */
  
  public ToggleIndicator(Icon icon, Icon altIcon)
    {
    this.icon = icon;
    this.altIcon = altIcon;

    setHorizontalAlignment(SwingConstants.CENTER);
    setVerticalAlignment(SwingConstants.CENTER);

    setIcon(icon);
    }

  /** Set the toggle state.
   *
   * @param state <code>true</code> to turn the toggle "on",
   * <code>false</code> to turn it "off." The toggle will be repainted
   * immediately.
   */
  
  public synchronized void setState(boolean state)
    {
    this.state = state;
    
    setIcon(state ? icon : altIcon);
    KiwiUtils.paintImmediately(this);
    }

  /** Get the current state of the toggle.
   *
   * @return The current toggle state.
   */

  public synchronized boolean getState()
    {
    return(state);
    }

  }

/* end of source file */
