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
   $Log: WizardView.java,v $
   Revision 1.12  2003/01/19 09:50:54  markl
   Javadoc & comment header updates.

   Revision 1.11  2001/03/20 00:54:53  markl
   Fixed deprecated calls.

   Revision 1.10  2001/03/12 09:56:54  markl
   KLabel/KLabelArea changes.

   Revision 1.9  2001/03/12 09:28:02  markl
   Source code and Javadoc cleanup.

   Revision 1.8  2000/10/11 10:46:17  markl
   Fixes to better support enabling/disabling navigation.

   Revision 1.7  2000/08/26 09:04:41  markl
   Changed Wizard*.java APIs to facilitate easier control over forward/
   backward navigation.

   Revision 1.6  1999/11/19 06:23:53  markl
   Added addButton() and removeButton() methods.

   Revision 1.5  1999/04/19 05:59:47  markl
   I18N changes.

   Revision 1.4  1999/02/28 11:44:08  markl
   Minor fixes.

   Revision 1.3  1999/02/28 00:27:35  markl
   Added a updateData() method. This gives subclasses a chance to store their
   values in the config object.

   Revision 1.2  1999/01/10 03:05:32  markl
   added GPL header & RCS tag
   ----------------------------------------------------------------------------
*/

package kiwi.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import kiwi.event.*;
import kiwi.util.*;

/** A wizard-style component. <code>WizardView</code> essentially displays a
  * sequence of panels (or "cards") to the user;  each panel typically
  * contains messages and/or input elements. A <code>WizardPanelSequence</code>
  * object functions as the source of these panels, and determines the order
  * in which the panels are presented to the user, and the conditions under
  * which forward and backward movement is allowed between consecutive panels.
  * <p>
  * This component is arranged as follows. The leftmost portion of the
  * component is used to display an image (which for best results should be
  * transparent). Animated GIFs are acceptable. The bottom portion of the
  * component displays the <i>Back</i>, <i>Next</i>, and <i>Cancel</i>
  * buttons. The remaining space is occupied by the current
  * <code>WizardPanel</code> provided by the <code>WizardPanelSequence</code>
  * object.
  * <p>
  * The <code>WizardPanelSequence</code> determines when the user may move to
  * the next or previous panel. Whenever these conditions change, the
  * <code>WizardPanelSequence</code> fires a <code>ChangeEvent</code> to
  * notify the <code>WizardView</code>, which responds by dimming or
  * undimming the <i>Next</i> and <i>Back</i> buttons, as appropriate. When
  * the final panel in the sequence is reached, the <i>Next</i> button changes
  * to a <i>Finish</i> button.
  * <p>
  * If the <i>Cancel</i> button is pressed, an <code>ActionEvent</code> is
  * fired with an action command of "cancel". If the <i>Finish</i> button is
  * pressed, an <code>ActionEvent</code> is fired with an action command of
  * "finish".
  * <p>
  * The goal of the <code>Wizard</code> family of classes was to provide a
  * framework for creating wizards that was as flexible as possible, without
  * creating a large amount of classes and interfaces to achieve that goal.
  * Therefore the APIs for these classes may at first appear counterintuitive
  * or inelegant. If they are found to be too cumbersome, they will be changed
  * in a future release of Kiwi.
  *
  * <p><center>
  * <img src="snapshot/WizardView.gif"><br>
  * <i>An example WizardView.</i>
  * </center>
  *
  * @see javax.swing.event.ChangeEvent
  * @see kiwi.ui.WizardPanelSequence
  *
  * @author Mark Lindner
  */

public class WizardView extends KPanel
  {
  private KButton b_prev, b_next, b_cancel;
  private KLabel iconLabel;
  private WizardPanelSequence sequence;
  private KPanel content;
  private WizardPanel curPanel = null;
  private int pos = 0, count = 0;
  private Icon i_next;
  private boolean finish = false;
  private ActionSupport support;
  private String s_next, s_back, s_finish, s_cancel;
  private _ActionListener actionListener;
  private ButtonPanel p_buttons;

  /** Construct a new <code>WizardView</code>.
   *
   * @param sequence The <code>WizardPanelSequence</code> that will provide
   * <code>WizardPanel</code>s for the wizard.
   */
  
  public WizardView(WizardPanelSequence sequence)
    {
    this.sequence = sequence;

    sequence.addChangeListener(new ChangeListener()
                               {
                               public void stateChanged(ChangeEvent evt)
                                 {
                                 refresh();
                                 }
                               });

    LocaleData loc = LocaleManager.getDefaultLocaleManager()
      .getLocaleData("KiwiDialogs");

    s_next = loc.getMessage("kiwi.button.next");
    s_back = loc.getMessage("kiwi.button.back");
    s_finish = loc.getMessage("kiwi.button.finish");
    s_cancel = loc.getMessage("kiwi.button.cancel");
    
    support = new ActionSupport(this);
    
    setLayout(new BorderLayout(5, 5));

    content = new KPanel();
    content.setLayout(new GridLayout(1, 0));

    iconLabel = new KLabel(KiwiUtils.getResourceManager()
			   .getIcon("wizard.gif"));
    add("West", iconLabel);

    add("Center", content);

    Insets margin = new Insets(1, 5, 1, 5);
    
    p_buttons = new ButtonPanel();

    actionListener = new _ActionListener();

    b_prev = new KButton(s_back, KiwiUtils.getResourceManager()
			 .getIcon("left.gif"));
    b_prev.setFocusPainted(false);
    b_prev.addActionListener(actionListener);
    b_prev.setEnabled(false);
    b_prev.setMargin(margin);
    p_buttons.addButton(b_prev);

    i_next = KiwiUtils.getResourceManager().getIcon("right.gif");
    
    b_next = new KButton("");
    b_next.setHorizontalTextPosition(SwingConstants.LEFT);
    b_next.setFocusPainted(false);
    b_next.addActionListener(actionListener);
    b_next.setMargin(margin);
    p_buttons.addButton(b_next);

    b_cancel = new KButton(s_cancel);
    b_cancel.setFocusPainted(false);
    b_cancel.addActionListener(actionListener);
    b_cancel.setMargin(margin);
    p_buttons.addButton(b_cancel);

    KPanel p_bottom = new KPanel();
    p_bottom.setLayout(new BorderLayout(5, 5));

    p_bottom.add("Center", new JSeparator());
    p_bottom.add("South", p_buttons);
    
    add("South", p_bottom);

    reset();
    }

  /** Get a reference to the <i>Cancel</i> button.
    *
    * @return The <i>Cancel</i> button.
    */
  
  public JButton getCancelButton()
    {
    return(b_cancel);
    }

  /** Get a reference to the <i>Finish</i> button.
    *
    * @return The <i>Finish</i> button.
    */
  
  public JButton getFinishButton()
    {
    return(b_next);
    }

  /** Add a button to the <code>WizardView</code> at the specified position.
   *
   * @param button The button to add.
   * @param pos The position at which to add the button. The value 0 denotes
   * the first position, and -1 denotes the last position.
   * @exception java.lang.IllegalArgumentException If the value of
   * <code>pos</code> is invalid.
   */

  public void addButton(JButton button, int pos)
    throws IllegalArgumentException
    {
    p_buttons.addButton(button, pos);
    }

  /** Remove a button from the specified position in the
    * <code>ButtonPanel</code>.
    *
    * @param pos The position of the button to remove, where 0 denotes the
    * first position.
    * @exception java.lang.IllegalArgumentException If an attempt is made
    * to remove one of the predefined wizard buttons.
    */
  
  public void removeButton(int pos) throws IllegalArgumentException
    {
    JButton b = (JButton)p_buttons.getButton(pos);
    if((b == b_cancel) || (b == b_prev) || (b == b_next))
      throw(new IllegalArgumentException("Can't remove predefined buttons."));
    else
      p_buttons.removeButton(pos);
    }

  /** Set the component's icon. Animated and/or transparent GIF images add a
    * professional touch when used with <code>WizardView</code>s.
    *
    * @param icon The new icon to use, or <code>null</code> if no icon is
    * needed.
    */

  public void setIcon(Icon icon)
    {
    iconLabel.setIcon(icon);
    }
  
  /* show a panel */
  
  private void showPanel(WizardPanel panel)
    {
    if(curPanel != null)
      {
      content.remove(curPanel);
      curPanel.syncData();
      }
    
    content.add(curPanel = panel);
    content.validate();
    content.repaint();
    curPanel.syncUI();
    curPanel.beginFocus();
    refresh();
    }

  /** Reset the <code>WizardView</code>. Resets the component so that the first
    * panel is displayed. This method also calls the
    * <code>WizardPanelSequence</code>'s <code>reset()</code> method.
    *
    * @see kiwi.ui.WizardPanelSequence#reset
    */
  
  public void reset()
    {
    sequence.reset();
    b_next.setText(s_next);
    b_next.setIcon(i_next);
    showPanel(sequence.getNextPanel());
    }

  /* refresh the buttons based on what the sequence tells us */
  
  private void refresh()
    {
    finish = sequence.isLastPanel();
    // if(!finish)
    b_next.setEnabled(sequence.canMoveForward());
    b_prev.setEnabled(sequence.canMoveBackward());

    if(!finish)
      {
      b_next.setText(s_next);
      b_next.setIcon(i_next);
      }
    else
      {
      b_next.setText(s_finish);
      b_next.setIcon(null);
      }
    }

  /** Add an <code>ActionListener</code> to this component's list of listeners.
    *
    * @param listener The listener to add.
    */

  public void addActionListener(ActionListener listener)
    {
    support.addActionListener(listener);
    }

  /** Add an <code>ActionListener</code> to this component's list of listeners.
    *
    * @param listener The listener to add.
    */

  public void removeActionListener(ActionListener listener)
    {
    support.removeActionListener(listener);
    }

  /* Handle events. */

  private class _ActionListener implements ActionListener
    {
    public void actionPerformed(ActionEvent evt)
      {
      Object o = evt.getSource();

      if(o == b_next)
        {
        if(finish)
          {
          curPanel.syncData();
          support.fireActionEvent("finish");
          }
        else
          showPanel(sequence.getNextPanel());
        }

      else if(o == b_prev)
        showPanel(sequence.getPreviousPanel());

      else if(o == b_cancel)
        support.fireActionEvent("cancel");
      }
    }

  }

/* end of source file */
