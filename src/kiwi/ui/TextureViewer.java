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
   $Log: TextureViewer.java,v $
   Revision 1.3  2003/01/19 09:48:48  markl
   Give the viewer a decent preferred size.

   Revision 1.2  2001/03/12 09:28:00  markl
   Source code and Javadoc cleanup.

   Revision 1.1  1999/05/10 09:02:10  markl
   Initial revision
   ----------------------------------------------------------------------------
*/

package kiwi.ui;

import java.awt.*;
import javax.swing.*;

/** An implementation of <code>UIElementViewer</code> for previewing textures.
 *
 * @author Mark Lindner
 */

public class TextureViewer extends KPanel implements UIElementViewer
  {
  private static final Dimension preferredSize = new Dimension(150, 150);
  
  /** Construct a new <code>TextureViewer</code>.
   */
  
  public TextureViewer()
    {
    setOpaque(true);
    setTexture(null);
    setPreferredSize(preferredSize);
    }

  /** Get a reference to the viewer component.
   *
   * @return The viewer component.
   */
  
  public JComponent getViewerComponent()
    {
    return(this);
    }

  /** Show the specified element.
   *
   * @param element An object, assumed to be an instance of <code>Image</code>,
   * to display.
   */
  
  public void showElement(UIElement element)
    {
    Object obj = element.getObject();
    
    if(obj instanceof Image)
      setTexture((Image)obj);
    }

  }

/* end of source file */
