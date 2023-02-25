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
   $Log: StackView.java,v $
   Revision 1.4  2003/01/19 09:50:54  markl
   Javadoc & comment header updates.

   Revision 1.3  2001/03/12 09:28:00  markl
   Source code and Javadoc cleanup.

   Revision 1.2  1999/01/10 03:00:07  markl
   added GPL header & RCS tag
   ----------------------------------------------------------------------------
*/

package kiwi.ui;

import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

import kiwi.ui.model.*;

/** A component that displays the contents of a <code>Stack</code> data
  * structure. This is an MVC class that uses a <code>StackModel</code> as its
  * data model.
  *
  * @see kiwi.ui.model.StackModel
  * @see java.util.Stack
  *
  * @author Mark Lindner
  */

public class StackView extends JList
  {
  private StackModel model;

  /** Construct a new <code>StackView</code> with a default stack model.
    */

  public StackView()
    {
    this(new DefaultStackModel());
    }

  /** Construct a new <code>StackView</code> with the given stack model.
    *
    * @param model The <code>StackModel</code> to use.
    */

  public StackView(StackModel model)
    {
    super.setModel(model);
    this.model = model;
    }

  /** Get the model used by this <code>StackView</code>. */

  public StackModel getStackModel()
    {
    return(model);
    }

  /** Push a new item on the stack.
    *
    * @param obj The object to push on the stack.
    */

  public void push(Object obj)
    {
    model.push(obj);
    }

  /** Pop an item off the stack. Pops the top item off the stack.
    *
    * @return The popped item.
    *
    * @exception java.util.EmptyStackException If the stack is empty.
    */

  public Object pop() throws EmptyStackException
    {
    return(model.pop());
    }

  /** Drop the top item off the stack. Pops and discards the top item off the
    * stack.
    *
    * @exception java.util.EmptyStackException If the stack is empty.
    */

  public void drop() throws EmptyStackException
    {
    model.drop();
    }

  /** Peek at the top item on the stack.
    *
    * @return The top item on the stack. The item is not removed from the
    * stack.
    */

  public Object peek()
    {
    return(model.peek());
    }

  /** Swap the positions of the top two items on the stack. If there is only
    * one item in the stack, this method has no effect.
    *
    * @exception java.util.EmptyStackException If the stack is empty.
    */

  public void swap() throws EmptyStackException
    {
    model.swap();
    }

  /** Return the depth of the stack.
    *
    * @return The number of items on the stack.
    */

  public int getDepth()
    {
    return(model.getDepth());
    }

  /** Check if the stack is empty.
    *
    * @return <code>true</code> if the stack is empty, <code>false</code>
    * otherwise.
    */

  public boolean isEmpty()
    {
    return(model.isEmpty());
    }

  /** Remove an item from the stack. Removes the item at the specified index
    * from the stack. Index position 0 refers to the top of the stack.
    *
    * @param index The index of the item to remove.
    *
    * @return The removed item.
    *
    * @exception java.lang.ArrayIndexOutOfBoundsException If <code>index</code>
    * is out of range.
    */

  public Object pick(int index) throws ArrayIndexOutOfBoundsException
    {
    return(model.pick(index));
    }

  /** Append an item to the stack.
    *
    * @param obj The item to add to the bottom of the stack.
    */

  public void append(Object obj)
    {
    model.append(obj);
    }

  /** Replace the top item on the stack. The top item on the stack is replaced
    * with the item <code>obj</code>. If the stack is empty, the item is merely
    * pushed on the stack.
    *
    * @param obj The new item.
    */

  public void replace(Object obj)
    {
    try
      {
      pop();
      }
    catch(EmptyStackException ex) {}

    push(obj);
    }

  }

/* end of source file */
