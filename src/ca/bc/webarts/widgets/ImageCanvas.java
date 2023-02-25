/*
$Source: v:/cvsroot/open/projects/WebARTS/ca/bc/webarts/widgets/ImageCanvas.java,v $
$Name:  $

$Revision: 1.4 $
$Date: 2002/04/04 22:53:09 $
$Locker:  $
*/

/* This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package ca.bc.webarts.widgets;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;

/**
 *  A nice canvas that holds an Image in its center.
 *
 * @author     tgutwin
 * @created    October 27, 2001
 */
public class ImageCanvas extends Canvas
{
  /**
   *  Description of the Field
   *
   * @since
   */
  private Image          image;
  /**
   *  Description of the Field
   *
   * @since
   */
  private int            imageHeight = 0;
  /**
   *  Description of the Field
   *
   * @since
   */
  private int            imageWidth = 0;


  /**
   *  Constructor for the ImageCanvas object
   *
   * @param  image  The Image that will get displayed
   * @since
   */
  public ImageCanvas(Image image)
  {
    MediaTracker mt = new MediaTracker(this);
    mt.addImage(image, 0);

    try
    {
      mt.waitForID(0);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

    this.image = image;
    imageHeight = image.getHeight(this);
    imageWidth = image.getWidth(this);
  }


  /**
   *  Gets the PreferredSize attribute of the ImageCanvas object
   *
   * @return    The PreferredSize value
   * @since
   */
  public Dimension getPreferredSize()
  {
    return new Dimension(imageWidth, imageHeight);
  }


  /**
   *  Description of the Method
   *
   * @param  g  Description of Parameter
   * @since
   */
  public void paint(Graphics g)
  {
    g.drawImage(image,
        (getWidth() - imageWidth) / 2,
        (getHeight() - imageHeight) / 2,
        this);
  }


  /**
   *  Description of the Method
   *
   * @param  g  Description of Parameter
   * @since
   */
  public void update(Graphics g)
  {
    paint(g);
  }
}

