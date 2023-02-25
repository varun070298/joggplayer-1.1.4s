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
   $Log: ColorSwatch.java,v $
   Revision 1.3  2003/01/19 09:50:52  markl
   Javadoc & comment header updates.

   Revision 1.2  2001/03/12 09:27:52  markl
   Source code and Javadoc cleanup.

   Revision 1.1  2000/10/11 10:52:35  markl
   New class.
   ----------------------------------------------------------------------------
*/

package kiwi.ui;

import java.awt.*;
import javax.swing.*;

/** A simple component that renders a color swatch--a filled rectangle with
 * a thin black border.
 *
 * @author Mark Lindner
 */

public class ColorSwatch implements Icon
  {
  private Color color;
  private int w, h;
  /** The default swatch width. */
  public static final int DEFAULT_WIDTH = 50;
  /** The default swatch height. */
  public static final int DEFAULT_HEIGHT = 15;
  /** The default swatch color. */
  public static final Color DEFAULT_COLOR = Color.gray;

  /** Construct a new <code>ColorSwatch</code> with a default color, width,
   * and height.
   */

  public ColorSwatch()
    {
    this(DEFAULT_COLOR, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

  /** Construct a new <code>ColorSwatch</code> with the specified color and
   * geometry.
   *
   * @param color The color for the swatch.
   * @param width The width, in pixels.
   * @param height The height, in pixels.
   */

  public ColorSwatch(Color color, int width, int height)
    {
    this.color = color;
    this.w = width;
    this.h = height;
    }

  /** Get the color of this swatch.
   *
   * @return The current color of the swatch.
   */

  public Color getColor()
    {
    return(color);
    }

  /** Set the color of this swatch.
   *
   * @param color The new color for the swatch.
   */

  public void setColor(Color color)
    {
    this.color = color;
    }

  /** Get the width of the swatch.
   *
   * @return The width, in pixels.
   */

  public int getIconWidth()
    {
    return(w);
    }

  /** Get the height of the swatch.
   *
   * @return The height, in pixels.
   */
  
  public int getIconHeight()
    {
    return(h);
    }

  /** Paint the swatch (as an icon).
   *
   * @param c The component to paint the swatch in.
   * @param gc The graphics context.
   * @param x The x-coordinate.
   * @param y The y-coordinate.
   */

  public void paintIcon(Component c, Graphics gc, int x, int y)
    {
    gc.setColor(Color.black);
    gc.drawRect(x, y, w - 1, h - 1);
    gc.setColor(color);
    gc.fillRect(x + 1, y + 1, w - 2, h - 2);
    }

  }

/* end of source file */
