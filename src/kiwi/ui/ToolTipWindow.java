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
   $Log: ToolTipWindow.java,v $
   Revision 1.4  2003/01/19 09:50:54  markl
   Javadoc & comment header updates.

   Revision 1.3  2001/03/12 09:28:01  markl
   Source code and Javadoc cleanup.

   Revision 1.2  1999/01/10 03:01:37  markl
   added GPL header & RCS tag
   ----------------------------------------------------------------------------
*/

package kiwi.ui;

import java.awt.*;
import java.awt.event.*;

import kiwi.util.*;

/* ToolTipWindow - a tool tip component */

class ToolTipWindow extends Window
  implements MouseListener, MouseMotionListener
  {
  private String tip;
  private Component c;
  private boolean armed = false;
  private long timestamp = System.currentTimeMillis(), delay;
  private Point drawLoc = null;

  /*
   */
  
  ToolTipWindow(Component c, String tip, long delay)
    {
    super(KiwiUtils.getFrameForComponent(c));
    this.tip = tip;
    this.c = c;
    this.delay = (long)delay;
    }

  /*
   */
  
  synchronized boolean isArmed(long now)
    {
    return(armed && ((now - timestamp) >= delay));
    }

  /*
   */
  
  public Dimension getPreferredSize()
    {
    FontMetrics fm = getGraphics().getFontMetrics();

    return(new Dimension(fm.stringWidth(tip) + 6, fm.getHeight() + 4));
    }

  /*
   */
  
  public void paint(Graphics gc)
    {
    Dimension size = getSize();
		
    gc.drawRect(0, 0, size.width - 1, size.height - 1);
    gc.drawString(tip, 2, gc.getFontMetrics().getAscent() + 2);
    }

  /*
   */
  
  public void setVisible(boolean flag)
    {
    if(flag)
      {
      armed = false;
      setLocation(drawLoc.x - 15, drawLoc.y + 20);
      pack();
      }
    super.setVisible(flag);
    }

  /*
   */
  
  public Component getComponent()
    {
    return(c);
    }

  /*
   */
  
  public synchronized void mouseClicked(MouseEvent ev)
    {
    if(isShowing()) dispose();
    armed = false;
    }

  /*
   */
  
  public synchronized void mouseEntered(MouseEvent ev)
    {
    Point p = ev.getPoint();
    drawLoc = c.getLocationOnScreen();
    drawLoc.translate(p.x, p.y);
    timestamp = System.currentTimeMillis();
    armed = true;
    }

  /*
   */
  
  public synchronized void mouseDragged(MouseEvent ev)
    {
    }

  /*
   */
  
  public synchronized void mouseMoved(MouseEvent ev)
    {
    Point p = ev.getPoint();
    drawLoc = c.getLocationOnScreen();
    drawLoc.translate(p.x, p.y);
    }

  /*
   */
  
  public synchronized void mouseExited(MouseEvent ev)
    {
    if(isShowing()) dispose();
    armed = false;
    }

  /*
   */
  
  public synchronized void mousePressed(MouseEvent ev)
    {
    if(isShowing()) dispose();
    armed = false;
    }

  /*
   */
  
  public synchronized void mouseReleased(MouseEvent ev)
    {
    if(isShowing()) dispose();
    armed = false;
    }

  }

/* end of source file */
