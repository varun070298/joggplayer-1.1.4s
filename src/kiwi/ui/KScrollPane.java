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
   $Log: KScrollPane.java,v $
   Revision 1.3  2003/01/19 09:50:53  markl
   Javadoc & comment header updates.

   Revision 1.2  2002/03/08 22:48:09  markl
   Bug fix.

   Revision 1.1  2001/06/26 06:08:06  markl
   New class.
   ----------------------------------------------------------------------------
*/

package kiwi.ui;

import java.awt.*;
import javax.swing.*;

/** A trivial extension of <code>JScrollPane</code> that renders its contents
 * with a transparent background.
 *
 * @author Mark Lindner
 */

public class KScrollPane extends JScrollPane
  {
  /** Construct a new <code>KScrollPane</code>. */
  
  public KScrollPane()
    {
    super();

    _init();
    }

  /** Construct a new <code>KScrollPane</code> for the given component.
   *
   * @param view The component to display in the scroll pane.
   */

  public KScrollPane(Component view)
    {
    super(view);
    
    _init();
    }
  
  /* common initialization */
  
  private void _init()
    {
    setBackground(Color.white);
    getViewport().setBackground(Color.white);
    
    setOpaque(false);
    getViewport().setOpaque(false);

    JScrollBar sb = getHorizontalScrollBar();
    if(sb != null)
      sb.setOpaque(false);

    sb = getVerticalScrollBar();
    if(sb != null)
    sb.setOpaque(false);
    }
  
  }

/* end of source file */
