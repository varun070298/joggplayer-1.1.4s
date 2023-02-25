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
   $Log: Thermometer.java,v $
   Revision 1.5  2003/01/19 09:50:54  markl
   Javadoc & comment header updates.

   Revision 1.4  2001/03/12 09:28:00  markl
   Source code and Javadoc cleanup.

   Revision 1.3  1999/11/15 03:30:13  markl
   Minor fix to eliminate transient NullPointerException in _run() method.

   Revision 1.2  1999/01/10 03:00:07  markl
   added GPL header & RCS tag
   ----------------------------------------------------------------------------
*/

package kiwi.ui;

import java.awt.*;

import kiwi.util.*;

/** This class represents a combination progress meter / busy slider.
  *
  * @author Mark Lindner
  */

public class Thermometer extends KPanel
  {
  private int percent = 0, pos = 0, pwidth = 100, pheight = 10, barw = 20;
  private Thread thread = null;
  private static final Color defaultColor = new Color(99, 99, 206);
  
  /** Construct a new <code>Thermometer</code> with a default foreground color
    * (a dark blue).
    */

  public Thermometer()
    {
    this(defaultColor);
    }

  /** Construct a new <code>Thermometer</code> with the given foreground color.
    *
    * @param color The foreground color.
    */
  
  public Thermometer(Color color)
    {
    setForeground(color);
    }
  
  /** Set a percentage on the meter. The percentage value, which must
    * be between 0 and 100 inclusive, specifies how much of the meter should
    * be filled in with the foreground color.
    * 
    * @param percent The new percentage value. If the value is out of range,
    * it is clipped.
    */

  public synchronized void setPercent(int percent)
    {
    if(percent < 0) percent = 0;
    if(percent > 100) percent = 100;

    pos = 0;
    this.percent = percent;
    repaint();
    }

  /** Set the size of the component. */

  public void setSize(int w, int h)
    {
    pwidth = 100;
    pheight = h;
    barw = w / 5;
    super.setSize(w, h);
    }

  /** Get the preferred size of the component. The preferred size is always
    * 100 pixels for width and the current height.
    */
  
  public Dimension getPreferredSize()
    {
    return(new Dimension(pwidth, pheight));
    }

  /** Get the minimum size of the component. Returns the preferred size of the
    * component.
    */
  
  public Dimension getMinimumSize()
    {
    return(getPreferredSize());
    }

  /** Paint the component. */

  public void paint(Graphics gc)
    {
    Insets ins = getInsets();

    super.paint(gc);
    Dimension d = getSize();
    int w = d.width * percent / 100;
    int p = d.width * pos / 100;

    gc.setColor(getForeground());
    gc.fillRect(p + ins.left, ins.top, w - (ins.left + ins.right),
		d.height - (ins.top + ins.bottom));
    }

  /* Body of the update thread. */

  private void _run()
    {
    int i, bw;

    setPercent(20);

    try
      {
      while((thread != null) && !thread.isInterrupted())
        {
        bw = barw;

        for(pos = 0; pos < 80; pos += 2)
          {
          repaint();
          Thread.currentThread().sleep(30);
          }
        for(pos = 80; pos > 0; pos -= 2)
          {
          repaint();
          Thread.currentThread().sleep(30);
          }
        }
      }
    catch(InterruptedException ex)
      {
      // done
      }
    }

  /** Start the busy slider. The busy slider is a rectangular segment, 20%
    * of the width of the meter that slides back and forth along the length of
    * the meter. A dedicated thread is created to update this slider.
    */

  public synchronized void start()
    {
    thread = new Thread(new Runnable()
                        {
                        public void run()
                          {
                          _run();
                          }
                        });
    thread.start();
    }

  /** Stop the busy slider. Stops the slider and clears the meter. The
    * dedicated update thread is interrupted and released.
    */

  public synchronized void stop()
    {
    thread.interrupt();
    setPercent(0);
    thread = null;
    }

  }

/* end of source file */
