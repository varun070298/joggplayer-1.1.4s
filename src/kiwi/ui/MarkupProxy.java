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
   $Log: MarkupProxy.java,v $
   Revision 1.4  2003/01/19 09:50:53  markl
   Javadoc & comment header updates.

   Revision 1.3  2001/03/12 09:27:58  markl
   Source code and Javadoc cleanup.

   Revision 1.2  1999/01/10 02:49:15  markl
   added GPL header & RCS tag
   ----------------------------------------------------------------------------
*/

package kiwi.ui;

import java.awt.Color;
import javax.swing.*;
import javax.swing.border.LineBorder;

import kiwi.text.*;

/** A <code>MarkupProxy</code> is a graphical element that serves as a proxy
  * for a data structure that cannot easily be rendered in-line within a
  * <code>SimpleStyledEditor</code>.
  *
  * @see kiwi.ui.SimpleStyledEditor
  * @see kiwi.ui.MarkupProxyFactory
  *
  * @author Mark Lindner
  */

public abstract class MarkupProxy extends JLabel
  {
  /** The text for this proxy. */
  protected String text;

  /** Construct a new <code>MarkupProxy</code>.
    *
    * @param icon The icon for this proxy.
    * @param text The text to display in this proxy.
    */

  public MarkupProxy(Icon icon, String text)
    {
    setText(text);
    setIcon(icon);
    setBackground(new Color(200, 200, 200));
    setOpaque(true);
    setToolTipText(getDescription());
    setBorder(new LineBorder(Color.black, 1));
    }

  /** Get the textual markup used to encode this proxy. */

  public abstract String getMarkup();

  /** Get a description of this proxy. */

  public abstract String getDescription();

  }

/* end of source file */
