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
   $Log: KDesktopPane.java,v $
   Revision 1.4  2003/01/19 09:50:53  markl
   Javadoc & comment header updates.

   Revision 1.3  2001/03/12 09:27:56  markl
   Source code and Javadoc cleanup.

   Revision 1.2  1999/01/10 02:25:57  markl
   added GPL header & RCS tag
   ----------------------------------------------------------------------------
*/

package kiwi.ui;

import java.awt.*;
import javax.swing.*;

/** A trivial extension of <code>JDesktopPane</code> that supports an opacity
  * setting.
  *
  * @author Mark Lindner
  */

public class KDesktopPane extends JDesktopPane
  {
  private boolean opaque = true;

  /** Construct a new <code>KDesktopPane</code>. */
  
  public KDesktopPane()
    {
    super();
    }

  /** Paint the component. Delegates to superclass if the component is
    * opaque.
    */
  
  protected void paintComponent(Graphics gc)
    {
    if(opaque)
      super.paintComponent(gc);
    }

  /** Get the opacity state of this component.
    *
    * @return <code>true</code> if this component is opaque, and
    * <code>false</code> otherwise.
    */
  
  public boolean isOpaque()
    {
    return(opaque);
    }

  /** Set the opacity state of this component.
    *
    * @param opaque A flag specifying whether this component will be opaque.
    */
  
  public void setOpaque(boolean opaque)
    {
    this.opaque = opaque;
    repaint();
    }

  }

/* end of source file */
