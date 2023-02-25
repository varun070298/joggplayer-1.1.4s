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
   $Log: AudioClipViewer.java,v $
   Revision 1.3  2003/01/19 09:50:52  markl
   Javadoc & comment header updates.

   Revision 1.2  2001/03/12 09:27:52  markl
   Source code and Javadoc cleanup.

   Revision 1.1  1999/06/03 06:50:40  markl
   Initial revision
   ----------------------------------------------------------------------------
*/

package kiwi.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import kiwi.util.*;

/** An implementation of <code>UIElementViewer</code> for previewing
 * audio clips. The viewer consists of <i>Play</i> and <i>Stop</i> buttons,
 * for playing and stopping the audio clip, respectively.
 *
 * @author Mark Lindner
 */

public class AudioClipViewer extends KPanel implements UIElementViewer,
  ActionListener
  {
  private KButton b_play, b_stop;
  private AudioClip clip = null;

  /** Construct a new <code>AudioClipViewer</code>.
   */
  
  public AudioClipViewer()
    {
    setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

    ResourceManager rm = KiwiUtils.getResourceManager();
    
    b_play = new KButton(rm.getIcon("play.gif"));
    b_play.addActionListener(this);
    add(b_play);

    b_stop = new KButton(rm.getIcon("stop.gif"));
    b_stop.addActionListener(this);
    add(b_stop);   
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
   * @param element An object, assumed to be an instance of
   * <code>AudioClip</code>, to display.
   */
  
  public void showElement(UIElement element)
    {
    Object obj = element.getObject();
    
    if(obj instanceof AudioClip)
      {
      if(clip != null)
        clip.stop();
      
      clip = (AudioClip)obj;
      }
    }

  /** This method is public as an implementation side-effect.
   */
  
  public void actionPerformed(ActionEvent evt)
    {
    if(clip == null)
      return;
    
    Object o = evt.getSource();
    
    if(o == b_play)
      {
      clip.play();
      }
    else if(o == b_stop)
      {
      clip.stop();
      }
    }

  }

/* end of source file */
