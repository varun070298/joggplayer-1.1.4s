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
   $Log: WizardPanelSequence.java,v $
   Revision 1.8  2003/01/19 09:50:54  markl
   Javadoc & comment header updates.

   Revision 1.7  2001/03/12 09:28:02  markl
   Source code and Javadoc cleanup.

   Revision 1.6  2000/08/26 09:04:41  markl
   Changed Wizard*.java APIs to facilitate easier control over forward/
   backward navigation.

   Revision 1.5  1999/08/24 08:30:44  markl
   Fixed some javadoc comments.

   Revision 1.4  1999/03/11 03:59:01  markl
   Minor layout fix.

   Revision 1.3  1999/02/28 11:44:08  markl
   Minor fixes.

   Revision 1.2  1999/01/10 03:05:32  markl
   added GPL header & RCS tag
   ----------------------------------------------------------------------------
*/

package kiwi.ui;

import java.util.*;
import javax.swing.event.*;

import kiwi.event.*;
import kiwi.util.*;

/** This class serves as a factory of <code>WizardPanel</code>s for a
  * <code>WizardView</code>. A <code>WizardPanelSequence</code> maintains a set
  * of <code>WizardPanel</code>s and a <code>Config</code> object. When a
  * <code>WizardPanel</code> is added to the sequence, a reference to the
  * <code>Config</code> object is passed to the panel. The panel may use this
  * <code>Config</code> object to store results from user input or to look up
  * the current or default values for its input fields.
  * <p>
  * Whenever the value of a property is changed or a property is added to or
  * removed from  the <code>Config</code> object, the <code>Config</code>
  * object notifies the <code>WizardPanelSequence</code> via a
  * <code>ChangeEvent</code>. Subclassers may override the
  * <code>stateChanged()</code> method to handle these events.
  * <p>
  * The goal of the <code>Wizard</code> family of classes was to provide a
  * framework for creating wizards that was as flexible as possible, without
  * creating a large amount of classes and interfaces to achieve that goal.
  * Therefore the APIs for these classes may at first appear counterintuitive
  * or inelegant. If they are found to be too cumbersome, they will be changed
  * in a future release of Kiwi.
  *
  * @see kiwi.ui.WizardView
  * @see kiwi.ui.WizardPanel
  * @see kiwi.util.Config
  * @see javax.swing.event.ChangeEvent
  *
  * @author Mark Lindner
  */

public class WizardPanelSequence implements ChangeListener
  {
  /** The list of <code>WizardPanel</code>s. */
  protected Vector panels;
  /** The index of the currently-selected <code>WizardPanel</code>. */
  protected int currentIndex = -1;
  private ChangeSupport support;
  /** The configuration object for this sequence. */
  protected Config config;

  /** Construct a new <code>WizardPanelSequence</code>.
    */
  
  public WizardPanelSequence()
    {
    this(new Config());
    }
  
  /** Construct a new <code>WizardPanelSequence</code> with the given
    * configuration object.
    * 
    * @param config The configuration object to use as a datasource.
    */
  
  public WizardPanelSequence(Config config)
    {
    panels = new Vector();
    support = new ChangeSupport(this);
    this.config = config;
    config.addChangeListener(this);
    }

  /** Add a <code>WizardPanel</code> to this sequence. All panels should be
    * added before the sequence is used to construct a <code>WizardView</code>.
    *
    * @param panel The panel to add.
    * @see #addPanels
    */
  
  public final void addPanel(WizardPanel panel)
    {
    panels.addElement(panel);
    panel.addChangeListener(this);
    panel.setConfig(config);
    }

  /** Add an array of <code>WizardPanel</code>s to this sequence. All panels
    * should be added before the sequence is used to construct a
    * <code>WizardView</code>.
    *
    * @param panels The panels to add.
    * @see #addPanel
    */
  
  public final void addPanels(WizardPanel panels[])
    {
    for(int i = 0; i < panels.length; i++)
      addPanel(panels[i]);
    }

  /** Reset the sequence. Resets the sequence so that the first panel in the
    * sequence becomes the current panel; thus the next call to
    * <code>getNextPanel()</code> will return the first panel in the sequence.
    *
    * @see #getNextPanel
    */
  
  public void reset()
    {
    currentIndex = -1;
    }

  /** Get a reference to this <code>WizardPanelSequence</code>'s
    * <code>Config</code> object.
    *
    * @return The <code>Config</code> object.
    */
  
  public final Config getConfig()
    {
    return(config);
    }

  /** Get the currently displayed panel from this sequence.
   *
   * @return The current panel.
   */

  public WizardPanel getCurrentPanel()
    {
    return((currentIndex < 0) ? null
           : (WizardPanel)panels.elementAt(currentIndex));
    }

  /** Get the next panel from this sequence. The default implementation
    * returns the next panel from the <code>panels</code> vector and
    * increments the <code>currentIndex</code> variable only if the end of
    * the sequence has not been reached <i>and</i> the next panel is
    * reachable (e.g., if <code>canMoveForward()</code> returns
    * <cod>true</code>).
    *
    * @return The next panel to be displayed in the <code>WizardView</code>.
    */
  
  public WizardPanel getNextPanel()
    {
    if(isLastPanel()) return(null);
//    if(!canMoveForward()) return(null);

    return((WizardPanel)panels.elementAt(++currentIndex));
    }

  /** Get the previous panel from this sequence. The default implementation
    * returns the previous panel from the <code>panels</code> vector and
    * decrements the <code>currentIndex</code> variable only if the beginning
    * of the sequence has not been reached <i>and</i> the previous panel is
    * reachable (e.g., if <code>canMoveBackward()</code> returns
    * <code>true</code>).
    *
    * @return The previous panel to be displayed in the
    * <code>WizardView</code>.
    */
  
  public WizardPanel getPreviousPanel()
    {
    if(currentIndex == 0) return(null);
//    if(!canMoveBackward()) return(null);

    return((WizardPanel)panels.elementAt(--currentIndex));
    }

  /** Determine if the current panel is the last panel in the sequence. Once
    * the last panel in a sequence has been reached, th
    * <code>WizardView</code> changes its <i>Next</i> button to a
    * <i>Finish</i> button.
    *
    * @return <code>true</code> if the current panel is the last panel, and
    * <code>false</code> otherwise.
    */
  
  public boolean isLastPanel()
    {
    return(currentIndex == (panels.size() - 1));
    }

  /** Determine if the user is allowed to move to the next panel. The
    * default implementation returns <code>true</code> if the current panel
    * is not the last panel in the sequence <i>and</i> the
    * <code>canMoveForward()</code> method of the current panel
    * returns <code>true</code>. Subclassers may wish to override this
    * method to allow movement forward only if certain conditions are
    * met.
    *
    * @return <code>true</code> if the previous panel is reachable, and
    * <code>false</code> otherwise.
    */
  
  public boolean canMoveForward()
    {
    if(currentIndex > panels.size() - 1)
      return(false);

    WizardPanel p = getCurrentPanel();
    return((p == null) ? false : p.canMoveForward());
    }

  /** Determine if the user is allowed to move to the previous
    * panel. The default implementation returns <code>true</code> if the
    * current panel is not the first panel in the sequence <i>and</i>
    * the <code>canMoveBackward()</code> method of the current panel
    * returns <code>true</code>. Subclassers may wish to override this
    * method to allow movement backward only if certain conditions are
    * met.
    */
  
  public boolean canMoveBackward()
    {
    if(currentIndex <= 0)
      return(false);

    WizardPanel p = getCurrentPanel();
    return((p == null) ? false : p.canMoveBackward());
    }

  /** Handle <code>ChangeEvent</code>s fired by the <code>WizardPanel</code>s
    * that belong to this sequence. The default implementation does nothing;
    * subclassers may wish to add logic to determine (based on the current
    * values of properties in the <code>Config</code> object) whether movement
    * to the next or previous panel is allowed, or to determine which panel
    * will be displayed next, for example.
    *
    * @param evt The event. The source of the event is the
    * <code>WizardPanel</code> that fired it.
    * @see java.util.EventObject#getSource
    */
  
  public void stateChanged(ChangeEvent evt)
    {
    // handle change events here

    fireChangeEvent();
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

  /** Fire a change event. Notify listeners (typically only the
    * <code>WizardView</code> that owns this sequence) that the state of this
    * <code>WizardPanelSequence</code> has changed. Subclassers may wish to
    * call this method from the body of the <code>stateChanged()</code>
    * method if a change in the <code>Config</code> object has changed the
    * state of this <code>WizardPanelSequence</code> in a way that will
    * affect the appearance of the <code>WizardView</code>.
    */
  
  protected void fireChangeEvent()
    {
    support.fireChangeEvent();
    }

  /** Dispose of this object. */
  
  public void dispose()
    {
    config.removeChangeListener(this);
    config = null;
    }

  }

/* end of source file */
