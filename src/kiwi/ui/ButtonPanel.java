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
   $Log: ButtonPanel.java,v $
   Revision 1.12  2003/01/19 09:50:52  markl
   Javadoc & comment header updates.

   Revision 1.11  2001/03/12 09:27:52  markl
   Source code and Javadoc cleanup.

   Revision 1.10  2000/10/11 10:45:47  markl
   Fixes to support AbstractButtons.

   Revision 1.9  1999/11/19 06:23:41  markl
   Added getButton() method.

   Revision 1.8  1999/08/04 08:09:27  markl
   Rewrote to use a custom layout manager.

   Revision 1.7  1999/07/30 03:59:18  markl
   Bug fix.

   Revision 1.6  1999/07/29 04:34:33  markl
   Added some more sophisticated layout logic.

   Revision 1.5  1999/02/28 00:27:05  markl
   Minor fixes.

   Revision 1.4  1999/02/27 12:08:17  markl
   Minor fixes.

   Revision 1.3  1999/02/27 12:06:07  markl
   Added alignment parameter.

   Revision 1.2  1999/01/10 01:00:58  markl
   added GPL header & RCS tag
   ----------------------------------------------------------------------------
*/

package kiwi.ui;

import java.awt.*;
import javax.swing.*;

/** This class is a simple extension of <code>KPanel</code> that arranges
  * buttons in a row, in such a way that the buttons are all of equal size,
  * and justified flush with the right or left edge of the panel. Many Kiwi
  * dialogs and frames provide buttons in their lower-right areas or toolbars
  * in their upper-left areas that are positioned in just this way; this
  * class eliminates the need to perform the layout explicitly in code each
  * time this effect is desired.
  * <p>
  * The row of buttons is anchored at either the left or the right edge of
  * the panel. If the sum of the preferred widths of the buttons exceeds the
  * available horizontal space, the buttons are compressed horizontally to
  * fit.
  *
  * @see kiwi.ui.KPanel
  *
  * @author Mark Lindner
  */

public class ButtonPanel extends KPanel
  {
  private static final int LEFT = 0, RIGHT = 1;
  private KPanel p_buttons;
  /** The default horizontal spacing. */
  public static final int DEFAULT_SPACING = 5;

  /** Construct a new <code>ButtonPanel</code> with the default horizontal
    * spacing and right alignment.
    */
  
  public ButtonPanel()
    {
    this(SwingConstants.RIGHT, DEFAULT_SPACING);
    }

  /** Construct a new <code>ButtonPanel</code> with default horizontal
    * spacing and the given alignment.
    *
    * @param alignment The alignment of the buttons within their containing
    * panel; one of <code>SwingConstants.LEFT</code> or
    * <code>SwingConstants.RIGHT</code>.
    */
  
  public ButtonPanel(int alignment)
    {
    this(alignment, DEFAULT_SPACING);
    }
  
  /** Construct a new <code>ButtonPanel</code> with the given horizontal
    * spacing and alignment.
    *
    * @param spacing The size of the gap (in pixels) to place between buttons
    * horizontally.
    * @param alignment The alignment of the buttons within their containing
    * panel; one of <code>SwingConstants.LEFT</code> or
    * <code>SwingConstants.RIGHT</code>.
    */
  
  public ButtonPanel(int alignment, int spacing)
    {
    setOpaque(false);

    int anchor = ((alignment == SwingConstants.RIGHT) ? RIGHT : LEFT);
    setLayout(new AnchorLayout(anchor));

    p_buttons = new KPanel();
    p_buttons.setLayout(new GridLayout(1, 0, spacing, 0));
    
    add(p_buttons);
    }

  /** Add a button to the <code>ButtonPanel</code>.
    *
    * @param button The button to add.
    * @see #removeButton
    */
  
  public void addButton(AbstractButton button)
    {
    p_buttons.add(button);
    }

  /** Add a button to the <code>ButtonPanel</code> at the specified position.
    *
    * @param button The button to add.
    * @param pos The position at which to add the button. The value 0 denotes
    * the first position, and -1 denotes the last position.
    * @exception java.lang.IllegalArgumentException If the value of
    * <code>pos</code> is invalid.
    */
  
  public void addButton(AbstractButton button, int pos)
    throws IllegalArgumentException
    {
    p_buttons.add(button, pos);
    }

  /** Get a reference to the button at the specified position in the
   * <code>ButtonPanel</code>.
   *
   * @param pos The position of the button to retrieve.
   * @return The button.
   */
  
  public AbstractButton getButton(int pos)
    {
    return((AbstractButton)p_buttons.getComponent(pos));
    }
  
  /** Remove a button from the <code>ButtonPanel</code>.
    *
    * @param button The button to remove.
    * @see #addButton
    */
  
  public void removeButton(AbstractButton button)
    {
    p_buttons.remove(button);
    }

  /** Remove a button from the specified position in the
    * <code>ButtonPanel</code>.
    *
    * @param pos The position of the button to remove, where 0 denotes the
    * first position.
    */
  
  public void removeButton(int pos)
    {
    p_buttons.remove(pos);
    }

  /** Get the number of buttons in this <code>ButtonPanel</code>.
    *
    * @return The number of buttons.
    */

  public int getButtonCount()
    {
    return(p_buttons.getComponentCount());
    }

  /* The custom layout manager for this component. Normally I would frown upon
   * writing a layout manager, but in this case, none of the existing layout
   * managers did the job, and there was no other elegant and foolproof way to
   * achieve this effect.
   *
   * This layout manager can only handle one child component in a container.
   * It anchors the child either to the right or the left edge of its parent.
   * The child's size is set to its preferred size, if the parent is big
   * enough. Otherwise, the child's size is set to the size of the parent.
   */

  private class AnchorLayout implements LayoutManager2
    {
    private int anchor;
    private Component c = null;
    private Dimension noSize = new Dimension(0, 0);

    public AnchorLayout(int anchor)
      {
      this.anchor = anchor;
      }
    
    public float getLayoutAlignmentX(Container cont)
      {
      return(0.5f);
      }

    public float getLayoutAlignmentY(Container cont)
      {
      return(0.5f);
      }
    
    public void addLayoutComponent(String name, Component comp)
      {
      if(c != null)
        return;

      c = comp;
      }

    public void addLayoutComponent(Component comp, Object constraints)
      {
      addLayoutComponent((String)null, comp);
      }

    public void removeLayoutComponent(Component comp)
      {
      if(comp == c)
        c = null;
      }

    private Dimension layoutSize(Dimension size, Insets insets)
      {
      if(c == null)
        return(noSize);
      
      return(new Dimension((insets.left + size.width + insets.right),
                           (insets.top + size.height + insets.bottom)));
    }
    
    public Dimension maximumLayoutSize(Container cont)
      {
      return(c.getMaximumSize());
      }

    public void invalidateLayout(Container cont)
      {
      // nothing to do
      }

    // The minimum layout size is based on the component's minimum size.
    
    public Dimension minimumLayoutSize(Container cont)
      {
      return(layoutSize(c.getMinimumSize(), cont.getInsets()));
      }

    // The preferred layout size is based on the component's preferred size.
    
    public Dimension preferredLayoutSize(Container cont)
      {
      return(layoutSize(c.getPreferredSize(), cont.getInsets()));
      }

    // Lay out the container.
    
    public void layoutContainer(Container cont)
      {
      if(c == null)
        return;

      Dimension size = cont.getSize();
      Insets insets = cont.getInsets();

      int w = size.width - (insets.left + insets.right);
      int h = size.height - (insets.top + insets.bottom);
      Dimension p = c.getPreferredSize();

      int cw = p.width;
      
      if(cw > w)
        {
        cw = w;
        c.setSize(cw, h);
        }
      else
        c.setSize(p);

      int offset = ((anchor == LEFT) ? 0 : (w - cw));
      
      c.setLocation(insets.left + offset, insets.top);
      }
    }  

  }

/* end of source file */
