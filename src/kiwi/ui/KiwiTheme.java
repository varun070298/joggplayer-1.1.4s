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
   $Log: KiwiTheme.java,v $
   Revision 1.3  2003/01/19 09:50:53  markl
   Javadoc & comment header updates.

   Revision 1.2  2001/03/12 09:27:57  markl
   Source code and Javadoc cleanup.
   ----------------------------------------------------------------------------
*/

package kiwi.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.metal.*;

/** The Kiwi UI theme.
 *
 * @author Mark Lindner
 */

public final class KiwiTheme extends DefaultMetalTheme
  {
  public static final ColorUIResource paleGreen
    = new ColorUIResource(152, 251, 152);

  public static final ColorUIResource limeGreen
    = new ColorUIResource(50, 205, 50);

  public static final ColorUIResource charteuse3
    = new ColorUIResource(102, 205, 0);
  
  public static final ColorUIResource gold3
    = new ColorUIResource(205, 173, 0);

  public static final ColorUIResource gold1
    = new ColorUIResource(203, 173, 0);
  
  public static final ColorUIResource darkGoldenrod2
    = new ColorUIResource(238, 173, 14);
  
  // greens
  
  protected ColorUIResource getPrimary1()
    {
    return(limeGreen);
    }

  protected ColorUIResource getPrimary2()
    {
    return(paleGreen);
    }

  protected ColorUIResource getPrimary3()
    {
    return(charteuse3);
    }

  // browns
  
  protected ColorUIResource getSecondary1()
    {
    return(darkGoldenrod2);
    }

  protected ColorUIResource getSecondary2()
    {
    return(gold1);
    }

  protected ColorUIResource getSecondary3()
    {
    return(gold3);
    }
  
  }

/* end of source file */
