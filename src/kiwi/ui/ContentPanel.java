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
   $Log: ContentPanel.java,v $
   Revision 1.6  2003/01/19 09:50:53  markl
   Javadoc & comment header updates.

   Revision 1.5  2001/03/12 09:27:53  markl
   Source code and Javadoc cleanup.

   Revision 1.4  1999/10/05 02:50:51  markl
   Javadoc fix for constructor.

   Revision 1.3  1999/08/13 07:10:49  markl
   Implemented some of the abstract methods.

   Revision 1.2  1999/05/10 09:24:09  markl
   Added methods.

   Revision 1.1  1999/02/28 00:26:24  markl
   Initial revision
   ----------------------------------------------------------------------------
*/

package kiwi.ui;

import kiwi.db.*;

/** This class defines methods common to components that display (and allow
  * the editing of) a piece of data, which is represented by a
  * <code>DomainObject</code>. Typically, the component is filled with
  * editing fields and other input components that correspond to fields in
  * the domain object. Data is synchronized between the domain object and these
  * fields via calls to the methods <code>setData()</code> and
  * <code>syncData()</code>.
  *
  * @author Mark Lindner
  */

public abstract class ContentPanel extends KPanel
  {
  /** Construct a new <code>ContentPanel</code>.
   */
  
  public void ContentPanel()
    {
    }
  
  /** Synchronize a <code>DomainObject</code> with the
   *  <code>ContentPanel</code>. Subclassers should define this method to
   *  copy the appropriate values from the domain object to the corresponding
   *  input elements in the interface. The reference to the domain object must
   *  also be saved so that its state may be updated in the
   *  <code>syncData()</code> method.
   *
   * @param data The <code>DomainObject</code> to associate with this
   * <code>ContentPanel</code>.
   */
  
  public abstract void setData(DomainObject data);

  /** Synchronize data entered into the <code>ContentPanel</code> with the
   * <code>DomainObject</code> that was provided with the most recent call
   * to <code>setData()</code>. Subclassers should define this method to
   * copy the appropriate values from the input elements in the interface to
   * the corresponding fields in the domain object.
   * <p>
   * Data validation is performed by a <code>DomainObject</code>'s
   * <i>setter</i> methods. Invalid input is flagged by raising a
   * <code>AccessorException</code>, which must be caught by the
   * <code>syncData()</code> method. The message in the exception is a human-
   * readable phrase that describes the problem with the input; this message
   * may be displayed in a warning dialog box to notify the user of the
   * erroneous input.
   *
   * @return <code>true</code> if no data validation errors occurred, and
   * <code>false</code> otherwise.
   */
  
  public abstract boolean syncData();

  /** Set the editable property for this <code>ContentPanel</code>. Some
   * interfaces have both an editable (<i>edit</i>) and a non-editable
   * (<i>view</i>) mode, but some subclasses may not implement both; in this
   * case the method will be a <i>no-op</i>.
   *
   * @param editable A flag specifying whether the component will be editable.
   */
  
  public abstract void setEditable(boolean editable);

  /** Activate this content panel. The meaning of <i>activate</i> is
   * context-specific. If this is one of a sequence of panels in a tabbed
   * pane, for example, this method might be called when the user selects the
   * corresponding tab to make this panel visible.
   *
   * @return A flag specifying whether this panel may be activated. The default
   * implementation returns <code>true</code>.
   */
  
  public boolean activate()
    {
    return(true);
    }

  /** Deactivate this content panel. The meaning of <i>deactivate</i> is
   * context-specific. If this is one of a sequence of panels in a tabbed
   * pane,  for example, this method might be called when the user selects
   * another tab to make this panel invisible.
   *
   * @return A flag specifying whether this panel may be deactivated. The
   * default implementation returns <code>true</code>.
   */
  
  public boolean deactivate()
    {
    return(true);
    }

  /** Get the title for this panel. The usage of this method will vary; if this
   * is one of a sequence of panels in a tabbed pane, for example, this method
   * might return the string that should appear in the tab corresponding to
   * this panel.
   *
   * @return The title for the panel. The default implementation returns an
   * empty string.
   */
  
  public String getTitle()
    {
    return("");
    }

  }

/* end of source file */
