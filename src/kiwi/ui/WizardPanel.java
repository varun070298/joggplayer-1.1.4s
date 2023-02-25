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
   $Log: WizardPanel.java,v $
   Revision 1.9  2003/01/19 09:50:54  markl
   Javadoc & comment header updates.

   Revision 1.8  2001/03/12 09:56:54  markl
   KLabel/KLabelArea changes.

   Revision 1.7  2001/03/12 09:28:02  markl
   Source code and Javadoc cleanup.

   Revision 1.6  2000/08/26 09:04:41  markl
   Changed Wizard*.java APIs to facilitate easier control over forward/
   backward navigation.

   Revision 1.5  1999/02/28 11:44:08  markl
   Minor fixes.

   Revision 1.4  1999/02/28 00:27:35  markl
   Fixed a layout problem by adding a call to validate().

   Revision 1.3  1999/02/27 14:43:59  markl
   softened etched border

   Revision 1.2  1999/01/10 03:05:32  markl
   added GPL header & RCS tag
   ----------------------------------------------------------------------------
*/

package kiwi.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import kiwi.event.*;
import kiwi.util.*;

/** This class represents a single user interface panel for a
  * <code>WizardView</code> component. Subclassers should not construct the
  * panel's user interface in the constructor, but rather, should provide an
  * implementation for <code>buildUI()</code> that fills this purpose.
  * <p>
  * The goal of the <code>Wizard</code> family of classes was to provide a
  * framework for creating wizards that was as flexible as possible, without
  * creating a large amount of classes and interfaces to achieve that goal.
  * Therefore the APIs for these classes may at first appear counterintuitive
  * or inelegant. If they are found to be too cumbersome, they will be changed
  * in a future release of Kiwi.
  *
  * @author Mark Lindner
  *
  * @see kiwi.ui.WizardView
  * @see kiwi.ui.WizardPanelSequence
  */

public abstract class WizardPanel extends KPanel
  {
  private ChangeSupport support;
  private KLabel l_title;
  /** The configuration object for the <code>WizardPanelSequence</code> that
    * owns this <code>WizardPanel</code>.
    */
  protected Config config;
  private static Font defaultFont = new Font("Dialog", Font.BOLD, 14);

  /** Construct a new <code>WizardPanel</code>.
    */
  
  public WizardPanel()
    {
    support = new ChangeSupport(this);
    setOpaque(false);
    
    setLayout(new BorderLayout(5, 5));

    KPanel p_top = new KPanel();
    p_top.setLayout(new BorderLayout(5, 5));

    l_title = new KLabel("Untitled");
    p_top.add("Center", l_title);

    add("North", p_top);

    KPanel p_center = new KPanel();
    p_center.setBorder(
      new CompoundBorder(new SoftBevelBorder(BevelBorder.LOWERED),
                         KiwiUtils.defaultBorder));
    p_center.setLayout(new GridLayout(1, 0));
    
    p_center.add(buildUI());
    
    add("Center", p_center);
    }

  /** Build the user interface for this <code>WizardPanel</code>. This method
    * must build and return the component that will be displayed in this
    * <code>WizardPanel</code>. Typically the implementor will instantiate
    * a container such as <code>KPanel</code>, add interface components to it,
    * and then return that container.
    *
    * @return The component that will be displayed in this panel.
    */
  
  protected abstract Component buildUI();

  /** Synchronize this <code>WizardPanel</code>'s user interface. This method
    * is called by the <code>WizardView</code> immediately before this panel is
    * made visible to the user. This allows the implementor to update the
    * state of the components that make up this panel's interface, perhaps
    * based on the current values of the <code>WizardPanelSequence</code>'s
    * <code>Config</code> properties.
    */
  
  public abstract void syncUI();

  /** Synchronize this <code>WizardPanel</code>'s data. This method is called
    * by the <code>WizardView</code> immediately after this panel is made
    * invisible (such as when the user moves to the next or previous panel).
    * This allows the implementor to update the <code>Config</code> properties
    * based on the values entered in the panel's user interface.
    */
  
  public abstract void syncData();

  /** Set the title for this <code>WizardPanel</code>. The title is displayed
    * at the top of the panel.
    *
    * @param title The new title, or <code>null</code> if no title is needed.
    */
  
  protected final void setTitle(String title)
    {
    l_title.setText(title);
    }
  
  /* attach a reference to the global configuration object */
  
  final void setConfig(Config config)
    {
    this.config = config;
    }

  /** Begin focus in this component. This method is called by the
    * <code>WizardView</code> immediately after this panel is made visible to
    * the user. It allows the panel to give input focus to the appropriate
    * component in its user interface. The default implementation requests
    * focus for the panel's first child component.
    */
  
  public void beginFocus()
    {
    if(getComponentCount() > 0)
      getComponent(0).requestFocus();
    }

  /** Add a <code>ChangeListener</code> to this object's list of listeners.
    *
    * @param listener The listener to add.
    */  
  
  public void addChangeListener(ChangeListener listener)
    {
    support.addChangeListener(listener);
    }

  /** Remove a <code>ChangeListener</code> from this object's list of
    * listeners.
    *
    * @param listener The listener to remove.
    */
  
  public void removeChangeListener(ChangeListener listener)
    {
    support.removeChangeListener(listener);
    }

  /** Fire a change event. <code>WizardPanel</code>s should fire
    * <code>ChangeEvent</code>s whenever a change in their internal state
    * would affect the appearance of the <code>WizardView</code>. For example,
    * the user may complete data entry in a panel, which should undim the
    * <code>WizardView</code>'s <i>Next</i> button to allow the user to proceed
    * to the next panel.
    */
  
  protected void fireChangeEvent()
    {
    support.fireChangeEvent();
    }

  /** Determine if the user can move forward to the next panel.
   *
   * @return <code>true</code> if the next panel can be shown, and
   * <code>false</code> otherwise. The default implementation returns
   * <code>true</code>.
   */

  public boolean canMoveForward()
    {
    return(true);
    }

  /** Determine if the user can move backward to the previous panel.
   *
   * @return <code>true</code> if the previous panel can be shown, and
   * <code>false</code> otherwise. The default implementation returns
   * <code>true</code>.
   */

  public boolean canMoveBackward()
    {
    return(true);
    }

  }

/* end of source file */
