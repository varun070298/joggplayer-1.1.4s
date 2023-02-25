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
   $Log: ToolTipManager.java,v $
   Revision 1.4  2003/01/19 09:50:54  markl
   Javadoc & comment header updates.

   Revision 1.3  2001/03/12 09:28:01  markl
   Source code and Javadoc cleanup.

   Revision 1.2  1999/01/10 03:01:37  markl
   added GPL header & RCS tag
   ----------------------------------------------------------------------------
*/

package kiwi.ui;

import java.util.*;
import java.awt.*;

import kiwi.util.KiwiUtils;

/** A tool tip manager for use with heavyweight as well as lightweight AWT and
  * JFC components.
  *
  * @author Mark Lindner
  *
  * @deprecated Since Kiwi is first and foremost a Swing extension
  * library, AWT-only features such as this are of limited usefulness
  * and will be removed.
  */

public class ToolTipManager
  {
  private Vector tips;
  private Thread thread;
  private int delay = 2000;
  private Color bgcolor = new Color(150, 200, 230), fgcolor = Color.black;
  private Font font = new Font("Dialog", Font.PLAIN, 8);
  private static ToolTipManager manager = new ToolTipManager();

  /* private constructor to create singleton */

  private ToolTipManager()
    {
    tips = new Vector(20, 20);

    thread = new Thread(new Runnable()
      {
      public void run()
	{
	_run();
	}
      });
    thread.start();
    }

  /** Get a reference to the tool tip manager singleton.
    */

  public static ToolTipManager getToolTipManager()
    {
    return(manager);
    }

  /** Set the tool tip font.The default font is 8 point <i>Dialog</i>.
    *
    * @param font The new font to use.
    */

  public void setFont(Font font)
    {
    this.font = font;
    }

  /** Set the tool tip background color. The default background color is a
    * light blue.
    *
    * @param bgcolor The new background color.
    */

  public void setBackground(Color bgcolor)
    {
    this.bgcolor = bgcolor;
    }

  /** Set the tool tip foreground color. The default foreground color is
    * black.
    *
    * @param fgcolor The new foreground color.
    */

  public void setForeground(Color fgcolor)
    {
    this.fgcolor = fgcolor;
    }

  /** Set the tool tip display delay. A tool tip will appear
    * <code>seconds</code> seconds after the mouse pointer has remained
    * motionless within the bounds of the associated component. The default
    * delay is 2 seconds.
    *
    * @exception java.lang.IllegalArgumentException If <code>seconds</code> is
    * not in the range 1 to 20, inclusive.
    */

  public void setDelay(int seconds) throws IllegalArgumentException
    {
    if((seconds < 1) || (seconds > 20))
      throw(new IllegalArgumentException("Delay value out of range."));
    else
      this.delay = seconds * 1000;
    }
  
  /** Add a tool tip. Registers the component <code>c</code> and tool tip text
    * <code>tip</code> with the tool tip manager.
    *
    * @see #removeToolTip
    */

  public void addToolTip(Component c, String tip)
    throws IllegalArgumentException
    {
    if(tip.length() > 40)
      throw(new IllegalArgumentException("Tool tip string too long."));

    ToolTipWindow tw = new ToolTipWindow(c, tip, delay);
    tw.setBackground(bgcolor);
    tw.setForeground(fgcolor);
    tw.setFont(font);
    c.addMouseListener(tw);
    c.addMouseMotionListener(tw);
    tips.addElement(tw);
    }

  /** Remove a tool tip. Unregisters the component <code>c</code> from the tool
    * tip manager.
    *
    * @see #addToolTip
    */

  public void removeToolTip(Component c) throws NoSuchElementException
    {
    Enumeration e = tips.elements();
    while(e.hasMoreElements())
      {
      ToolTipWindow tw = (ToolTipWindow)e.nextElement();
      if(tw.getComponent() == c)
	{
	c.removeMouseListener(tw);
	c.removeMouseMotionListener(tw);
	tips.removeElement(tw);
	return;
	}
      }
    throw(new NoSuchElementException("Component not registered."));
    }
  
  /* Tool tip manager thread body. */

  private void _run()
    {
    Enumeration e;
    long now;
    
    while(!Thread.currentThread().isInterrupted())
      {
      try
        {
        Thread.currentThread().sleep(500);
        }
      catch(InterruptedException ex)
        {
        return;
        }
      
      now = System.currentTimeMillis();
      e = tips.elements();
      while(e.hasMoreElements())
	{
	ToolTipWindow tw = (ToolTipWindow)e.nextElement();
	if(tw.isArmed(now)) tw.setVisible(true);
	}
      }
    }

  /** Dispose of this object. Disassociates this <code>ToolTipManager</code>
    * as a listener from all of the components it is managing, and releases all
    * references to those components. */

  public void dispose()
    {
    thread.interrupt();
    Enumeration e = tips.elements();
    while(e.hasMoreElements())
      {
      ToolTipWindow tw = (ToolTipWindow)e.nextElement();
      Component c = tw.getComponent();
      c.removeMouseListener(tw);
      c.removeMouseMotionListener(tw);
      }
    tips.removeAllElements();
    thread = null;
    }

  }

/* end of source file */
