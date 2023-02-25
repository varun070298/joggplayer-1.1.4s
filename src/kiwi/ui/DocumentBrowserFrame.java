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
   $Log: DocumentBrowserFrame.java,v $
   Revision 1.8  2003/01/19 09:50:53  markl
   Javadoc & comment header updates.

   Revision 1.7  2001/06/26 06:17:15  markl
   Updated with new LocaleManager API.

   Revision 1.6  2001/03/12 09:56:54  markl
   KLabel/KLabelArea changes.

   Revision 1.5  2001/03/12 09:27:54  markl
   Source code and Javadoc cleanup.

   Revision 1.4  2000/07/15 02:18:37  markl
   Fixed typo.

   Revision 1.3  1999/04/19 05:59:19  markl
   I18N changes.

   Revision 1.2  1999/01/10 02:05:37  markl
   added GPL header & RCS tag
   ----------------------------------------------------------------------------
*/

package kiwi.ui;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import kiwi.ui.model.*;
import kiwi.util.*;

/** This class represents a document browser window. It displays a
  * <code>DocumentBrowserView</code> in a dedicated frame and handles all
  * window-related events.
  *
  * <p><center>
  * <img src="snapshot/DocumentBrowserFrame.gif"><br>
  * <i>An example DocumentBrowserFrame.</i>
  * </center>
  *
  * @author Mark Lindner
  */

public class DocumentBrowserFrame extends KFrame
  {
  private KButton b_close;
  private DocumentDataSource dataSource;
  private DocumentBrowserView browser;

  /** Construct a new <code>DocumentBrowserFrame</code>.
    *
    * @param title The window title.
    * @param comment A comment string for the top portion of the window.
    * @param dataSource The data source for the browser.
    */

  public DocumentBrowserFrame(String title, String comment,
			      DocumentDataSource dataSource)
    {
    super(title);

    this.dataSource = dataSource;

    LocaleData loc = LocaleManager.getDefaultLocaleManager()
      .getLocaleData("KiwiDialogs");
    
    KPanel panel = getMainContainer();

    panel.setBorder(KiwiUtils.defaultBorder);
    panel.setLayout(new BorderLayout(5, 5));

    KLabel l = new KLabel(comment);
    panel.add("North", l);

    StaticTreeModel model = new StaticTreeModel(dataSource);
    panel.add("Center", browser = new DocumentBrowserView(model));

    ButtonPanel buttons = new ButtonPanel();

    b_close = new KButton(loc.getMessage("kiwi.button.close"));
    b_close.addActionListener(new ActionListener()
      {
      public void actionPerformed(ActionEvent evt)
	{
	_hide();
	}
      });
    buttons.addButton(b_close);
    
    panel.add("South", buttons);

    addWindowListener(new WindowAdapter()
      {
      public void windowClosing(WindowEvent evt)
	{
	_hide();
	}
      });
    
    pack();
    }

  /* hide the window */

  private void _hide()
    {
    setVisible(false);
    dispose();
    }

  }

/* end of source file */
