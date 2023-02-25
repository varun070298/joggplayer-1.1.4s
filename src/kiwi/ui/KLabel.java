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
   $Log: KLabel.java,v $
   Revision 1.9  2003/01/19 09:50:53  markl
   Javadoc & comment header updates.

   Revision 1.8  2001/03/12 09:27:09  markl
   New class.
   ----------------------------------------------------------------------------
*/

package kiwi.ui;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.plaf.ComponentUI;

import kiwi.util.*;

/** A trivial extension to <code>JLabel</code> that performs some simple
 * customizations.
 *
 * @since Kiwi 1.3
 *
 * @author Mark Lindner
 */

public class KLabel extends JLabel
  {
  /** Construct a new <code>KLabel</code>.
   */
  
  public KLabel()
    {
    super();

    _init();
    }

  /** Construct a new <code>KLabel</code> with the specified image.
   *
   * @param image The image.
   */
  
  public KLabel(Icon image)
    {
    super(image);

    _init();
    }

  /** Construct a new <code>KLabel</code> with the specified image and
   * horizontal alignment.
   *
   * @param image The image.
   * @param horizontalAlignment The horizontal alignment.
   */
  
  public KLabel(Icon image, int horizontalAlignment)
    {
    super(image, horizontalAlignment);
    
    _init();
    }

  /** Construct a new <code>KLabel</code> with the specified text.
   *
   * @param text The text.
   */
  
  public KLabel(String text)
    {
    super(text);

    _init();
    }

  /** Construct a new <code>KLabel</code> with the specified text, icon and
   * horizontal alignment.
   *
   * @param text The text.
   * @param icon The icon.
   * @param horizontalAlignment The horizontal alignment.
   */
  
  public KLabel(String text, Icon icon, int horizontalAlignment)
    {
    super(text, icon, horizontalAlignment);

    _init();
    }

  /** Construct a new <code>KLabel</code> with the specified text and
   * horizontal alignment.
   *
   * @param text The text.
   * @param horizontalAlignment The horizontal alignment.
   */
  
  public KLabel(String text, int horizontalAlignment)
    {
    super(text, horizontalAlignment);

    _init();
    }

  /*
   */

  private void _init()
    {
    setOpaque(false);
    setForeground(Color.black);
    }
  
  }

/* end of source file */
