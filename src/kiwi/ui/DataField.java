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
   $Log: DataField.java,v $
   Revision 1.8  2003/01/19 09:50:53  markl
   Javadoc & comment header updates.

   Revision 1.7  2001/03/12 09:27:53  markl
   Source code and Javadoc cleanup.

   Revision 1.6  1999/10/05 03:10:48  markl
   Documentation correction.

   Revision 1.5  1999/10/04 06:22:55  markl
   Fixed bugs, implemented checkInput() and made class concrete.

   Revision 1.4  1999/07/29 06:45:19  markl
   Changes to support length constraints.

   Revision 1.3  1999/07/19 09:46:24  markl
   More validation fixes.

   Revision 1.2  1999/07/19 03:59:11  markl
   Fixed deadlock problem.

   Revision 1.1  1999/07/09 04:37:54  markl
   Initial revision
   ----------------------------------------------------------------------------
*/

package kiwi.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

import kiwi.event.*;
import kiwi.ui.model.*;
import kiwi.text.*;

/** A class that implements basic functionality for a text field
 * that places constraints on its input.
 * <code>DataField</code> validates its input when it loses or gains focus,
 * generates an action event, or when messaged with the
 * <code>validateInput()</code> method. The no-op method
 * <code>checkInput()</code> must be overridden by subclassers to perform
 * the actual data validation.
 * <p>
 * Invalid input is flagged by repainting the contents of the field in red. If
 * a key is typed into a field so highlighted, the text reverts back to black
 * (non-flagged). Validation is not performed whenever the contents of the
 * field change, as the necessary parsing is an expensive operation.
 *
 * @author Mark Lindner
 */

public class DataField extends JTextField implements FormatConstants
  {
  private ChangeSupport csupport;
  private _DocumentListener documentListener = null;
  private boolean inputRequired = false;
  /** A state flag for representing validation state. */
  protected boolean invalid = false;
  private boolean adjusting = false;

  /** Construct a new <code>DataField</code>.
   */
  
  public DataField()
    {
    super();
    _init();
    }

  /** Construct a new <code>DataField</code> with the specified width.
   */
  
  public DataField(int width)
    {
    super(width);
    _init();
    }

  /* initialization */
  
  private void _init()
    {
    documentListener = new _DocumentListener();
    csupport = new ChangeSupport(this);
    KDocument doc = new KDocument();
    setDocument(doc);
    doc.addDocumentListener(documentListener);

    addKeyListener(new KeyAdapter()
                   {
                   public void keyTyped(KeyEvent evt)
                     {
                     // we don't want to validate after each keypress; too
                     // expensive
                     
                     if(invalid)
                       {
                       invalid = false;
                       paintInvalid(invalid);
                       }
                     }
                   });

    addFocusListener(new FocusAdapter()
                     {
                     public void focusLost(FocusEvent evt)
                       {
                       validateInput();
                       }

                     public void focusGained(FocusEvent evt)
                       {
                       validateInput();
                       }
                     });

    addActionListener(new ActionListener()
                      {
                      public void actionPerformed(ActionEvent evt)
                        {
                        validateInput();
                        }
                      });    
    }

  /*
   */
  
  public void setDocument(Document doc)
    {
    super.setDocument(doc);

    Document oldDoc = getDocument();

    if((documentListener != null) && (oldDoc != null))
      {
      oldDoc.removeDocumentListener(documentListener);
      doc.addDocumentListener(documentListener);
      }
    }

  /** Add a <code>ChangeListener</code> to this component's list of listeners.
   * <code>ChangeEvent</code>s are fired when this text field's document model
   * changes.
   *
   * @param listener The listener to add.
   */
  
  public void addChangeListener(ChangeListener listener)
    {
    csupport.addChangeListener(listener);
    }

  /** Add a <code>ChangeListener</code> to this component's list of listeners.
   * <code>ChangeEvent</code>s are fired when this text field's document model
   * changes.
   *
   * @param listener The listener to add.
   */
  
  public void removeChangeListener(ChangeListener listener)
    {
    csupport.removeChangeListener(listener);
    }

  /* document listener */
  
  private class _DocumentListener implements DocumentListener
    {
    public void changedUpdate(DocumentEvent evt)
      {
      _fireChange();
      }

    public void insertUpdate(DocumentEvent evt)
      {
      _fireChange();
      }

    public void removeUpdate(DocumentEvent evt)
      {
      _fireChange();
      }
    }

  /* Delay-fire a change event, but only if the current DocumentEvent is not
   * the result of a call to setText().
   */

  private void _fireChange()
    {
    if(adjusting)
      return;

    SwingUtilities.invokeLater(new Runnable()
                               {
                               public void run()
                                 {
                                 csupport.fireChangeEvent();
                                 }
                               });
    }

  /** Set the text to be displayed by this field. A <code>ChangeEvent</code>
   * will <i>not</i> be fired when the data in the field is modified via this
   * call.
   *
   * @param text The text to set.
   */
  
  public final synchronized void setText(String text)
    {
    adjusting = true;
    super.setText(text);
    adjusting = false;
    }
  
  /** Paint the necessary decorations for the field to denote invalid (or
   * valid) input. The default implementation sets the text color to red
   * if the input is invalid and black otherwise. This method may be
   * overridden by subclassers who wish to customize the method of visual
   * feedback.
   *
   * @param invalid A flag specifying whether the input in the field is
   * currently valid or invalid.
   */

  protected void paintInvalid(boolean invalid)
    {
    setForeground(invalid ? Color.red : Color.black);
    }

  /** Set the editable state of this field.
   *
   * @param flag A flag specifying whether this field should be editable.
   * Non-editable fields are made transparent.
   */
  
  public void setEditable(boolean flag)
    {
    super.setEditable(flag);
    setOpaque(flag);
  }

  /** Specify whether an input is required in this field. If no input is
   * required, the <code>validateInput()</code> method will return
   * <code>true</code> if the field is left empty; otherwise it will return
   * <code>false</code>.
   *
   * @param flag The flag.
   * @see #validateInput
   * @see #isInputRequired
   */

  public void setInputRequired(boolean flag)
    {
    inputRequired = flag;
    }

  /** Determine if input is required in this field.
   *
   * @return <code>true</code> if input is required in this field, and
   * <code>false</code>
   * otherwise.
   */
  
  public boolean isInputRequired()
    {
    return(inputRequired);
    }
  
  /** Validate the input in this field.
   *
   * @return <code>true</code> if the field contains valid input or if the
   * field contains no input and input is not required, and <code>false</code>
   * otherwise.
   */

  public final boolean validateInput()
    {
    String s = getText().trim();

    if(s.length() == 0)
      return(!isInputRequired());

    return(checkInput());
    }

  /** Determine if the given input is valid for this field. The default
   * implementation returns <code>true</code>.
   *
   * @return <code>true</code> if the input is valid, and <code>false</code>
   * otherwise.
   */
  
  protected boolean checkInput()
    {
    return(true);
    }

  /** Set the maximum number of characters that may be entered into this field.
   * This method will have no effect if the document has been changed from
   * a <code>KDocument</code> via a call to <code>setDocument()</code>.
   *
   * @param length The new maximum length, or <code>KDocument.NO_LIMIT</code>
   * for unlimited length.
   * @see kiwi.ui.model.KDocument
   */

  public void setMaximumLength(int length)
    {
    Document d = getDocument();
    if(d instanceof KDocument)
      ((KDocument)d).setMaximumLength(length);
    }

  /** Get the maxmium number of characters that may be entered into this field.
   *
   * @return The maximum length, or <code>KDocument.NO_LIMIT</code> if there
   * is no limit.
   */
  
  public int getMaximumLength()
    {
    Document d = getDocument();
    if(d instanceof KDocument)
      return(((KDocument)d).getMaximumLength());
    else
      return(KDocument.NO_LIMIT);
    }
  
  }

/* end of source file */
