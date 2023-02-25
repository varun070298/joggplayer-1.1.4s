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
   $Log: ColoredString.java,v $
   Revision 1.4  2003/01/19 09:50:53  markl
   Javadoc & comment header updates.

   Revision 1.3  2001/03/12 09:27:53  markl
   Source code and Javadoc cleanup.

   Revision 1.2  1999/01/10 01:00:58  markl
   added GPL header & RCS tag
   ----------------------------------------------------------------------------
*/

package kiwi.ui;

import java.awt.Color;

/** A class that represents a colored string.
  *
  * @author Mark Lindner
  */

public class ColoredString 
  {
  private String string = "";
  private Color color = Color.black;

  /** Construct a new colored string.
    *
    * @param string The string.
    * @param color The color.
    */

  public ColoredString(String string, Color color)
    {
    this.string = string;
    this.color = color;
    }

  /** Construct a new colored string. The string is set to "" and the color
    * is set to black.
    */

  public ColoredString()
    {
    }

  /** Get the string.
    *
    * @return The string.
    * @see #setString
    */

  public String getString()
    {
    return(string);
    }

  /** Set the string.
    *
    * @param string The new string.
    * @see #getString
    */

  public void setString(String string)
    {
    this.string = string;;
    }
  
  /** Get the color.
    *
    * @return The color.
    * @see #setColor
    */

  public Color getColor()
    {
    return(color);
    }

  /** Set the color.
    *
    * @param color The new color.
    * @see #getColor
    */

  public void setColor(Color color)
    {
    this.color = color;
    }

  }

/* end of source file */
