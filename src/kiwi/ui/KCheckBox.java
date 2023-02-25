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
   $Log: KCheckBox.java,v $
   Revision 1.1  2003/11/07 19:11:05  markl
   new class

   ----------------------------------------------------------------------------
*/

package kiwi.ui;

import javax.swing.*;

import kiwi.util.KiwiUtils;

/** A trivial extension to <code>JCheckBox</code> that performs some simple
 * customizations.
 *
 * @see javax.swing.JCheckBox
 *
 * @author Mark Lindner
 *
 * @since Kiwi 1.4.3
 */

public class KCheckBox extends JCheckBox
  {

  /** Construct a new <code>KCheckBox</code>. A new, transparent checkbox
   * will be created.
   *
   * @param text The text to display for the checkbox.
   */

  public KCheckBox(String text)
    {
    super(text);
    
    setOpaque(!UIChangeManager.getInstance().getButtonsAreTransparent());
    }

  /** Construct a new <code>KCheckBox</code>. A new, transparent checkbox
   * will be created.
   *
   * @param text The text to display for the checkbox.
   * @param icon The icon to display for the checkbox.
   */

  public KCheckBox(String text, Icon icon)
    {
    super(text, icon);

    setOpaque(!UIChangeManager.getInstance().getButtonsAreTransparent());
    }

  /** Construct a new <code>KCheckBox</code>. A new, transparent checkbox
   * will be created.
   *
   * @param icon The icon to display in the button.
   */

  public KCheckBox(Icon icon)
    {
    super(icon);

    setOpaque(!UIChangeManager.getInstance().getButtonsAreTransparent());
    }

  }

/* end of source file */
