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
   $Log: KPanel.java,v $
   Revision 1.6  2003/01/19 09:50:53  markl
   Javadoc & comment header updates.

   Revision 1.5  2001/03/12 09:27:56  markl
   Source code and Javadoc cleanup.

   Revision 1.4  1999/04/26 09:27:15  markl
   Added support for solid backgrounds.

   Revision 1.3  1999/04/25 04:12:41  markl
   Changes to support solid backgrounds in addition to textures.

   Revision 1.2  1999/01/10 02:25:57  markl
   added GPL header & RCS tag
   ----------------------------------------------------------------------------
*/

package kiwi.ui;

import java.awt.*;
import javax.swing.*;

/** An extension of <code>JPanel</code> that provides some additional
  * functionality including support for background texture tiling and
  * named-component lookup. Lightweight components nested within the
  * <code>KPanel</code> should be transparent and unbuffered; the tiled
  * background will not show through heavyweight components.
  * <p>
  * Note that images with transparent portions should <i>not</i> be used with
  * <code>KPanel</code>s.
  * <p>
  * A <code>KPanel</code> will always be transparent if a background
  * texture has not been specified. Therefore <code>KPanel</code>s may be
  * safely nested if only the outermost instance has a texture applied. It is
  * recommended that <code>KPanel</code>s always be used in place of Swing
  * <code>JPanel</code>s.
  * <p>
  * In most cases it is convenient to use a <code>KFrame</code> to provide a
  * textured background for an entire window.
  * <p>
  * If a solid color background is desired in place of a texture, then the
  * outermost <code>KPanel</code> should be made opaque via the call
  * <code>setOpaque(true)</code>, and the texture should be turned off via a
  * call to <code>setTexture(null)</code>. The background color can be set
  * using <code>setBackground()</code> as usual.
  *
  * @see kiwi.ui.KFrame
  * 
  * @author Mark Lindner
  */

public class KPanel extends JPanel
  {
  private Image image = null;

  /** Construct a new <code>KPanel</code>. The newly created
    * <code>KPanel</code> will be transparent.
    */

  public KPanel()
    {
    super(false);
    super.setOpaque(false);
    }

  /** Construct a new <code>KPanel</code>. Creates a new
    * <code>KPanel</code> with the specified layout manager. The newly
    * created <code>KPanel</code> will be transparent.
    *
    * @param lm The layout manager to use for the panel.
    */

  public KPanel(LayoutManager lm)
    {
    super(lm, false);
    super.setOpaque(false);
    }

  /** Construct a new <code>KPanel</code>. Creates a new
    * <code>KPanel</code> with the specified layout manager and
    * background image.
    *
    * @param lm The layout manager to use for the panel.
    * @param image The image with which the background of the panel will be
    * tiled.
    */

  public KPanel(LayoutManager lm, Image image)
    {
    super(lm, false);

    this.image = image;
    super.setOpaque(false);
    }

  /** Construct a new <code>KPanel</code>. Creates a new
    * <code>KPanel</code> with a <code>BorderLayout</code> and the
    * specified background image.
    *
    * @param image The image with which the background of the panel will be
    * tiled.
    */

  public KPanel(Image image)
    {
    super(new BorderLayout(0, 0), true);

    this.image = image;
    super.setOpaque(false);
    }

  /** Paint the component. Tiles the component with the background image,
    * if one has been provided.
    */

  public void paintComponent(Graphics gc)
    {
    if(image == null)
      {
      super.paintComponent(gc);
      }
    else
      {
      Dimension size = new Dimension();
      getSize(size);
      int ih = image.getHeight(null);
      int iw = image.getWidth(null);

      int vc = (size.height / ih) + 1;
      int hc = (size.width / iw) + 1;

      for(int y = 0; y < vc; y++)
	for(int x = 0; x < hc; x++)
	  gc.drawImage(image, x * iw, y * ih, null);
      }
    }

  /** Set the background texture.
    *
    * @param image The image to use as the background texture for the panel.
    */

  public void setTexture(Image image)
    {
    this.image = image;
    repaint();
    }

  /** Search for a component by name. Components can be named using the
    * <code>setName()</code> method of <code>Component</code>. This is a useful
    * way of identifying a component when comparison by reference is not
    * possible. This method searches this <code>KPanel</code>'s component
    * hierarchy for a component with the given name.
    *
    * @param name The name of the component to search for.
    * @return The matching component, or <code>null</code> if there is no
    * component in the component hierarchy with the given name.
    * @see java.awt.Component#setName
    * @see java.awt.Component#getName
    */
    
  public Component getComponentByName(String name)
    {
    return(findComponent(this, name));
    }

  private Component findComponent(Container cont, String name)
    {
    int ct = cont.getComponentCount();

    for(int i = 0; i < ct; i++)
      {
      Component subc = cont.getComponent(i);
      if(subc.getName().equals(name))
        return(subc);
      }

    // none of them matched, so recurse

    for(int i = 0; i < ct; i++)
      {
      Component c = cont.getComponent(i);

      if(c instanceof Container)
        {
        Component subc = findComponent((Container)c, name);
        if(subc != null)
          return(subc);
        }
      }
    
    return(null);
    }

  /** Set the focus order for this component. The focus order specifies the
   * order in which child components will receive focus when the user presses
   * the tab key to move between input components.
   *
   * @param order The component array representing the desired focus order.
   */
  
  public void setFocusOrder(JComponent order[])
    {
    int i;
    
    for(i = 0; i < (order.length - 1); i++)
      order[i].setNextFocusableComponent(order[i + 1]);

    order[i].setNextFocusableComponent(order[0]);
    }

  }

/* end of source file */
