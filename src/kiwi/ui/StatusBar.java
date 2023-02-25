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
   $Log: StatusBar.java,v $
   Revision 1.10  2003/01/19 09:50:54  markl
   Javadoc & comment header updates.

   Revision 1.9  2001/03/12 09:28:00  markl
   Source code and Javadoc cleanup.

   Revision 1.8  1999/06/28 08:17:08  markl
   Fixed font.

   Revision 1.7  1999/04/25 04:13:08  markl
   Fixed opacity bug.

   Revision 1.6  1999/04/23 07:40:24  markl
   Added synchronization, fixed doc comment.

   Revision 1.5  1999/04/23 07:25:45  markl
   Fixed layout problem, added support for adding additional components to the
   status bar.

   Revision 1.4  1999/04/19 05:30:45  markl
   Added support for expiring messages.

   Revision 1.3  1999/02/09 05:31:32  markl
   Used softer border; only created one instance of it.

   Revision 1.2  1999/01/10 03:00:07  markl
   added GPL header & RCS tag
   ----------------------------------------------------------------------------
*/

package kiwi.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import kiwi.util.*;

/** This class represents a status bar that includes a message area and an
  * optional progress meter (see <code>Thermometer</code>). Status bars are
  * typically placed at the bottom of an application window. Status messages
  * disappear after a fixed number of seconds, unless they are specified as
  * non-expiring; the delay is adjustable.
  *
  * <p><center>
  * <img src="snapshot/StatusBar.gif"><br>
  * <i>An example StatusBar.</i>
  * </center>
  *
  * @see kiwi.ui.Thermometer
  *
  * @author Mark Lindner
  */

public class StatusBar extends KPanel implements ProgressObserver,
  ActionListener
  {
  private JTextField label = null;
  private Thermometer meter;
  private boolean labelOnly;
  private static final Border border
    = new SoftBevelBorder(SoftBevelBorder.LOWERED);
  private Timer timer;
  private GridBagConstraints gbc;
  private boolean busy = false;
  private static final Insets insets = new Insets(0, 2, 0, 0);
  
  /** Construct a new <code>StatusBar</code>. Constructs a new status bar
   * without a progress meter.
   */

  public StatusBar()
    {
    this(false);
    }

  /** Construct a new <code>StatusBar</code>.
   *
   * @param showMeter A flag specifying whether the status bar should include
   * a progress meter.
   */

  public StatusBar(boolean showMeter)
    {
    timer = new Timer(10000, this);
    
    GridBagLayout gb = new GridBagLayout();
    setLayout(gb);

    gbc = new GridBagConstraints();
    gbc.weighty = gbc.weightx = 1;
    gbc.fill = gbc.BOTH;
    gbc.ipady = 0;
    gbc.ipadx = 2;

    label = new JTextField();
    label.setFont(KiwiUtils.boldFont);
    label.setHighlighter(null);
    label.setEditable(false);
    label.setForeground(Color.black);
    label.setOpaque(false);
    label.setBorder(border);
    add(label, gbc);

    gbc.ipadx = 0;
    gbc.fill = gbc.VERTICAL;
    gbc.weightx = 0;
    gbc.insets = insets;
    
    if(showMeter)
      addStatusComponent(meter = new Thermometer());

    // super.setOpaque(true);
    }

  /** Remove a component from the status bar. The specified component will be
   * removed from the status bar. The text status field and the progress meter
   * (if present) cannot be removed.
   *
   * @param c The component to remove.
   * @exception java.lang.IllegalArgumentException If an attempt was made to
   * remove the text status field or tje progress meter.
   */
  
  public void removeStatusComponent(JComponent c)
    {
    if((c == label) || ((meter != null) && (c == meter)))
      throw(new IllegalArgumentException("Cannot remove label or meter."));
    
    remove(c);
    }

  /** Add a component to the status bar. The new component will be fitted with
   * a beveled border and made transparent to match the other components in the
   * status bar, and will be added at the right end of the status bar.
   *
   * @param c The component to add.
   */
  
  public void addStatusComponent(JComponent c)
    {
    c.setBorder(border);
    c.setOpaque(false);
    add(c, gbc);
    }
  
  /** Start or stop the slider. Starts or stops the progress meter's slider.
   * If this status bar was created without a progress meter, this method will
   * have no effect.
   *
   * @param flag A flag specifying whether the slider should be started or
   * stopped.
   */

  public synchronized void setBusy(boolean flag)
    {
    if(flag == busy) return;
    
    if(!labelOnly)
      {
      if(flag)
        meter.start();
      else
        meter.stop();

      busy = flag;
      }
    }

  /** Determine if the meter slider is currently active.
   *
   * @return <code>true</code> if the slider is active, and <code>false</code>
   * otherwise.
   */
  
  public synchronized boolean isBusy()
    {
    return(busy);
    }
  
  /** Set the message font. Sets the font for the text in the status bar.
   *
   * @param font The new font.
   */

  public void setFont(Font font)
    {
    if(label != null)
      label.setFont(font);
    }

  /** Set the text color. Sets the color of the text displayed in the status
   * bar.
   *
   * @param color The text new color.
   */

  public void setTextColor(Color color)
    {
    label.setForeground(color);
    }

  /** Set the meter color.
   *
   * @param color The new forground color for the thermometer.
   */

  public void setMeterColor(Color color)
    {
    meter.setForeground(color);
    }

  /** Set a percentage on the meter. The <code>percent</code> value, which must
   * be between 0 and 100 inclusive, specifies how much of the meter should be
   * filled in with the foreground color.
   *
   * @param percent The percentage, a value between 0 and 100 inclusive. If
   * the value is out of range, it is clipped.
   */

  public void setProgress(int percent)
    {
    meter.setPercent(percent);
    }

  /** Set the text to be displayed in the status bar. The status bar will be
   * cleared when the delay expires.
   *
   * @param text The text to display in the status bar.
   */

  public void setText(String text)
    {
    setText(text, true);
    }
  
  /** Set the text to be displayed in the status bar.
   *
   * @param text The text to display in the status bar.
   * @param expires A flag specifying whether the message "expires." If
   * <code>true</code>, the status bar will be cleared after the message has
   * been in the status bar for a specified number of seconds. The default
   * delay is 10 seconds, but can be adjusted via the <code>setDelay()</code>
   * method.
   *
   * @see #setDelay
   */
  
  public synchronized void setText(String text, boolean expires)
    {
    if(text == null) text = "";
    label.setText(text);

    if(expires)
      {
      if(timer.isRunning())
        timer.restart();
      else
        timer.start();
      }
    else
      {
      if(timer.isRunning())
        timer.stop();
      }
    }

  /** Set the delay on status bar messages.
   *
   * @param seconds The number of seconds before a message disappears from the
   * status bar.
   */
  
  public synchronized void setDelay(int seconds)
    {
    timer.setDelay(seconds * 1000);
    }
  
  /** This method is public as an implementation side-effect. */

  public void actionPerformed(ActionEvent evt)
    {
    setText(null);
    }

  }

/* end of source file */
