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
   $Log: DateChooserField.java,v $
   Revision 1.1  2003/01/19 09:37:59  markl
   New class.

   ----------------------------------------------------------------------------
*/

package kiwi.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.text.*;

import kiwi.event.*;
import kiwi.util.*;

/** A date entry component which consists of a combination of a
 * <code>DateField</code> and a <code>DateChooser</code> in a popup.
 * The popup is activated by clicking on the button to the right of the input
 * field.
 *
 * <p><center>
 * <img src="snapshot/DateChooserField.gif"><br>
 * <i>An example DateChooserField.</i>
 * <p>
 * <img src="snapshot/DateChooserField_popup.gif"><br>
 * <i>An example DateChooserField with the popup activated.</i>
 * </center>
 *
 * @author Mark Lindner
 *
 * @since Kiwi 1.4
 *
 * @see kiwi.ui.DateField
 * @see kiwi.ui.DateChooser
 * @see kiwi.ui.dialog.DateChooserDialog
 */

public class DateChooserField extends KPanel
  {
  private SimpleDateFormat dateFormat;
  /** */
  protected DateField t_date;
  /** */
  protected JButton b_chooser;
  private JPopupMenu menu;
  /** */
  protected DateChooser chooser;

  /** Constructs a <code>DateChooserField</code> object using the
   * default date and format.
   */

  public DateChooserField()
    {
    this(null);
    }

  /**
   */

  public DateChooserField(int width)
    {
    this(width, null);
    }

  /**
   */
  
  public DateChooserField(String format)
    {
    this(10, format);
    }
  
  /**
   */

  public DateChooserField(int width, String format)
    {  
    setLayout(new BorderLayout(2, 2));

    LocaleManager locmgr = LocaleManager.getDefaultLocaleManager();
    LocaleData loc = locmgr.getLocaleData("KiwiDialogs");
    
    t_date = new DateField(width, format);
    t_date.setOpaque(false);
    add("Center", t_date);
    
    b_chooser = new KButton(KiwiUtils.getResourceManager()
                            .getIcon("date.gif"));
    b_chooser.setToolTipText(loc.getMessage("kiwi.tooltip.select_date"));
    add("East", b_chooser);
    
    menu = new JPopupMenu();
    menu.setOpaque(false);
    chooser = new DateChooser();
    chooser.setOpaque(false);

    KPanel p_popup = new KPanel(UIChangeManager.getInstance()
                                .getDefaultTexture());
    p_popup.setLayout(new GridLayout(1, 0));
    p_popup.setBorder(KiwiUtils.defaultBorder);
    p_popup.add(chooser);
    menu.add(p_popup);

    b_chooser.addActionListener(new ActionListener()
        {
        public void actionPerformed(ActionEvent evt)
          {
          Date d = t_date.getDate();
          if(d != null)
            {
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            chooser.setSelectedDate(c);
            }
          
          menu.show(b_chooser, 0, 0);
          }
        });
    
    chooser.addActionListener(new ActionListener()
        {
        public void actionPerformed(ActionEvent evt)
          {
          if(evt.getActionCommand().equals(DateChooser.DATE_CHANGE_CMD))
            {
            Date d = chooser.getSelectedDate().getTime();
            t_date.setDate(d);
            menu.setVisible(false);
            }
          }
        });
    }
  
  /**
   */
  
  public Date getDate()
    {
    return(t_date.getDate());
    }

  /**
   */

  public void setDate(Date date)
    {
    t_date.setDate(date);
    }
  
  }

/* end of source file */
