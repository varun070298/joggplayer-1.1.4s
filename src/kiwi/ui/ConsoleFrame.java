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
   $Log: ConsoleFrame.java,v $
   Revision 1.8  2003/01/19 09:46:47  markl
   replaced JScrollPane instances with KScrollPane.

   Revision 1.7  2002/08/11 09:50:30  markl
   Javadoc fix.

   Revision 1.6  2001/03/20 00:54:52  markl
   Fixed deprecated calls.

   Revision 1.5  2001/03/12 09:27:53  markl
   Source code and Javadoc cleanup.

   Revision 1.4  1999/04/19 05:59:04  markl
   I18N changes.

   Revision 1.3  1999/02/02 07:56:30  markl
   Fixed the close button to only hide the frame, not dispose it.

   Revision 1.2  1999/01/10 01:00:58  markl
   added GPL header & RCS tag
   ----------------------------------------------------------------------------
*/

package kiwi.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import kiwi.util.*;

/** A GUI console window. This class implements the 
  * <code>LoggingEndpoint</code> interface and as such can be used as the
  * destination of log messages sent using that interface.
  *
  * <p><center>
  * <img src="snapshot/ConsoleFrame.gif"><br>
  * <i>An example ConsoleFrame.</i>
  * </center>
  *
  * @author Mark Lindner
  */

public class ConsoleFrame extends KFrame implements LoggingEndpoint
  {
  private KButton b_clear, b_dismiss;
  private ScrollbackView buffer;
  private ColoredString item;

  /** The message type-to-color mapping. */
  protected Color types[] = { Color.green, Color.yellow, Color.orange,
                              Color.red };

  /** Construct a new <code>ConsoleFrame</code> with a default title.
    *
    */
  
  public ConsoleFrame()
    {
    this("");
    }
  
  /** Construct a new <code>ConsoleFrame</code>.
    *
    * @param title The title for the console window.
    */
  
  public ConsoleFrame(String title)
    {
    super(title);

    ActionListener actionListener = new ActionListener()
      {
      public void actionPerformed(ActionEvent evt)
        {
        Object o = evt.getSource();
        
        if(o == b_clear)
          buffer.clear();
        else if(o == b_dismiss)
          setVisible(false);
        }
      };

    LocaleData loc = LocaleManager.getDefaultLocaleManager()
      .getLocaleData("KiwiDialogs");
    
    KPanel main = getMainContainer();

    main.setLayout(new BorderLayout(5, 5));
    main.setBorder(KiwiUtils.defaultBorder);

    item = new ColoredString("", Color.green);

    // list

    buffer = new ScrollbackView();
    KScrollPane sp = new KScrollPane();
    sp.getViewport().setView(buffer);
    sp.setBackground(Color.black);

    buffer.setCellRenderer(new ColoredCellRenderer());
    buffer.setBackground(Color.black);

    main.add("Center", sp);

    // buttons

    ButtonPanel buttons = new ButtonPanel();

    b_clear = new KButton(loc.getMessage("kiwi.button.clear"));
    b_clear.addActionListener(actionListener);
    buttons.addButton(b_clear);

    b_dismiss = new KButton(loc.getMessage("kiwi.button.dismiss"));
    b_dismiss.addActionListener(actionListener);
    buttons.addButton(b_dismiss);

    main.add("South", buttons);

    if(getTitle().length() == 0)
      setTitle(loc.getMessage("kiwi.dialog.title.console"));
    
    pack();
    }

  /** Set the number of save lines.
    *
    * @param lines The number of lines of text that are saved by the console.
    */

  public void setSaveLines(int lines)
    {
    buffer.setSaveLines(lines);
    }

  /** Get the number of save lines.
    *
    * @return The number of lines of text that are saved by the console.
    */

  public int getSaveLines()
    {
    return(buffer.getSaveLines());
    }

  /** Log a message to the console.
    *
    * @param type The message type
    * @param message The message proper.
    *
    * @see kiwi.util.LoggingEndpoint
    */

  public void logMessage(int type, String message)
    {
    buffer.addItem(new ColoredString(message, types[type]));
    }

  /** Close the console.
    *
    * @see kiwi.util.LoggingEndpoint
    */

  public void close()
    {
    setVisible(false);
    dispose();
    }

  }

/* end of source file */
