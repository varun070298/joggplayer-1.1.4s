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
   $Log: ImageView.java,v $
   Revision 1.7  2003/01/19 09:46:48  markl
   replaced JScrollPane instances with KScrollPane.

   Revision 1.6  2001/03/20 00:54:52  markl
   Fixed deprecated calls.

   Revision 1.5  2001/03/12 09:56:54  markl
   KLabel/KLabelArea changes.

   Revision 1.4  2001/03/12 09:27:56  markl
   Source code and Javadoc cleanup.

   Revision 1.3  1999/04/19 05:59:27  markl
   I18N changes.

   Revision 1.2  1999/01/10 02:04:05  markl
   fixed to do a smooth resize
   ----------------------------------------------------------------------------
*/

package kiwi.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import kiwi.util.*;

/** A general-purpose image viewing component. It displays an image in a scroll
  * pane and provides zooming buttons.
  *
  * <p><center>
  * <img src="snapshot/ImageView.gif"><br>
  * <i>An example ImageView.</i>
  * </center>
  *
  * @see java.awt.Image
  *
  * @author Mark Lindner
  */

public class ImageView extends KPanel
  {
  private KButton b_zoomin, b_zoomout, b_zoomreset;
  private KLabel viewport;
  private int origw, origh;
  private float scale = 1;
  private Image image, curImage = null;
  private KScrollPane scroll;
  private Cursor waitCursor = new Cursor(Cursor.WAIT_CURSOR);
  private Cursor defaultCursor;

  /** Construct a new <code>ImageView</code>.
    *
    * @param comment A comment to display above the image.
    * @param image The image to display.
    */

  public ImageView(String comment, Image image)
    {
    origw = image.getWidth(this);
    origh = image.getHeight(this);
    this.image = image;

    LocaleData loc = LocaleManager.getDefaultLocaleManager()
      .getLocaleData("KiwiDialogs");

    ActionListener actionListener = new ActionListener()
      {
      public void actionPerformed(ActionEvent evt)
        {
        Object o = evt.getSource();
        
        if(o == b_zoomin)
          scaleImage(2.0f);
        else if(o == b_zoomout)
          scaleImage(0.5f);
        else if(o == b_zoomreset)
          scaleImage(1.0f);
        }
      };
    
    defaultCursor = getCursor();

    GridBagConstraints gbc = new GridBagConstraints();

    setBorder(KiwiUtils.defaultBorder);
    setLayout(new BorderLayout(5, 5));

    KPanel p1 = new KPanel();
    GridBagLayout gb = new GridBagLayout();
    p1.setLayout(gb);

    gbc.anchor = gbc.WEST;
    gbc.weightx = 1;
    gbc.weighty = 0;
    gbc.gridwidth = 1;
    gbc.insets = new Insets(0, 1, 0, 1);

    KLabel label = new KLabel(comment);
    label.setFont(new Font("Serif", Font.BOLD, 14));

    p1.add(label, gbc);

    gbc.weightx = 0;

    ResourceManager rm = KiwiUtils.getResourceManager();

    b_zoomin = new KButton(rm.getIcon("zoom-in.gif"));
    b_zoomin.setToolTipText(loc.getMessage("kiwi.tooltip.zoom_in"));
    b_zoomin.addActionListener(actionListener);
    p1.add(b_zoomin, gbc);

    b_zoomout = new KButton(rm.getIcon("zoom-out.gif"));
    b_zoomout.setToolTipText(loc.getMessage("kiwi.tooltip.zoom_out"));
    b_zoomout.addActionListener(actionListener);
    p1.add(b_zoomout, gbc);

    b_zoomreset = new KButton(rm.getIcon("zoom-reset.gif"));
    b_zoomreset.setToolTipText(loc.getMessage("kiwi.tooltip.actual_size"));
    b_zoomreset.addActionListener(actionListener);
    p1.add(b_zoomreset, gbc);

    add("North", p1);

    viewport = new KLabel();
    viewport.setIcon(new ImageIcon(image));
    viewport.setVerticalAlignment(SwingConstants.CENTER);
    viewport.setHorizontalAlignment(SwingConstants.CENTER);

    scroll = new KScrollPane(viewport);
    scroll.setBackground(Color.white);
    add("Center", scroll);
    }

  /** Scale the image by a given scale factor.
    *
    * @param scaleFactor The scale factor.
    */

  public void scaleImage(float scaleFactor)
    {
    if(scaleFactor != 1)
      scale *= scaleFactor;
    else
      scale = 1;

    KiwiUtils.busyOn(this);
    if(curImage != null) curImage.flush();
    curImage = image.getScaledInstance((int)((float)origw * scale),
                                       (int)((float)origh * scale),
                                       Image.SCALE_SMOOTH);
    viewport.setIcon(new ImageIcon(curImage));
    viewport.invalidate();
    validate();
    KiwiUtils.busyOff(this);
    }

  }

/* end of source file */
