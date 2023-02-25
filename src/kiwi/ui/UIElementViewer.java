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
   $Log: UIElementViewer.java,v $
   Revision 1.3  2003/01/19 09:50:54  markl
   Javadoc & comment header updates.

   Revision 1.2  2001/03/12 09:28:01  markl
   Source code and Javadoc cleanup.

   Revision 1.1  1999/05/10 09:14:39  markl
   Initial revision
   ----------------------------------------------------------------------------
*/

package kiwi.ui;

import javax.swing.*;

/** An interface that describes the behavior of a viewer for
 * <code>UIElement</code>s.
 *
 * @see kiwi.ui.UIElementChooser
 *
 * @author Mark Lindner
 */

public interface UIElementViewer
  {
  /** Get a reference to the viewer component.
   *
   * @return The viewer component itself.
   */
  
  public JComponent getViewerComponent();

  /** Display the given element in the viewer.
   *
   * @param element The element to display.
   *
   */
  
  public void showElement(UIElement element);
  }

/* end of source file */
