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
   $Log: KToggleButton.java,v $
   Revision 1.3  2003/01/19 09:50:53  markl
   Javadoc & comment header updates.

   Revision 1.2  2001/03/12 09:27:57  markl
   Source code and Javadoc cleanup.

   Revision 1.1  2000/10/11 10:45:57  markl
   New class.
   ----------------------------------------------------------------------------
*/

package kiwi.ui;

import javax.swing.*;

import kiwi.util.KiwiUtils;

/** A trivial extension to <code>JToggleButton</code> that performs some simple
 * customizations.
 *
 * @see javax.swing.JToggleButton
 *
 * @author Mark Lindner
 */

public class KToggleButton extends JToggleButton
  {
  
  /** Construct a new <code>KToggleButton</code>. A new, transparent button
   * will be created.
   *
   * @param text The text to display in the button.
   */
  
  public KToggleButton(String text)
    {
    super(text);
    setOpaque(!UIChangeManager.getInstance().getButtonsAreTransparent());
    }

  /** Construct a new <code>KToggleButton</code>. A new, transparent button
   * will be created.
   *
   * @param text The text to display in the button.
   * @param icon The icon to display in the button.
   */

  public KToggleButton(String text, Icon icon)
    {
    super(text, icon);
    setOpaque(!UIChangeManager.getInstance().getButtonsAreTransparent());
    }

  /** Construct a new <code>KToggleButton</code>. A new, transparent button
   * will be created with zero-pixel margins and focus painting turned off.
   *
   * @param icon The icon to display in the button.
   */

  public KToggleButton(Icon icon)
    {
    super(icon);
    setMargin(KiwiUtils.emptyInsets);
    setFocusPainted(false);
    setOpaque(!UIChangeManager.getInstance().getButtonsAreTransparent());
    }

  }

/* end of source file */
