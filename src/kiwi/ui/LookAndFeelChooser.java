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
   $Log: LookAndFeelChooser.java,v $
   Revision 1.3  2003/01/19 09:50:53  markl
   Javadoc & comment header updates.

   Revision 1.2  2001/03/12 09:27:57  markl
   Source code and Javadoc cleanup.

   Revision 1.1  1999/04/23 07:25:38  markl
   Initial revision
   ----------------------------------------------------------------------------
*/

package kiwi.ui;

import java.awt.event.*;
import javax.swing.*;

/** This class represents a combo box for selecting a Look & Feel. It lists
  * all L&Fs installed on the system.
  *
  * @see javax.swing.UIManager
  *
  * @author Mark Lindner
  */

public class LookAndFeelChooser extends JComboBox
  {
  private boolean liveUpdate = false;
  private String plafClasses[], plafNames[];

  /** Construct a new <code>LookAndFeelChooser</code>.
   */
  
  public LookAndFeelChooser()
    {
    UIManager.LookAndFeelInfo plafs[] = UIManager.getInstalledLookAndFeels();

    for(int j = 0; j < plafs.length; ++j)
      addItem(new UIManager.LookAndFeelInfo(plafs[j].getName(),
                                            plafs[j].getClassName())
              {
              public String toString()
                {
                return(getName());
                } 
              });
    
    String curplaf = UIManager.getLookAndFeel().getName();

    for(int i = 0; i < plafs.length; i++)
      {
      if(curplaf.equals(plafs[i].getName()))
        {
        setSelectedIndex(i);
        break;
        } 
      }
    }

  /** Get the currently selected Look & Feel.
   *
   * @return The <code>LookAndFeelInfo</code> object corresponding to the
   * currently-selected L&F.
   */
  
  public String getLookAndFeel()
    {
    return(((UIManager.LookAndFeelInfo)getSelectedItem()).getClassName());
    }  

  }

/* end of source file */
