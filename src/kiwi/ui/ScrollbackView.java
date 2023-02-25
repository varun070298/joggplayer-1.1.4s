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
   $Log: ScrollbackView.java,v $
   Revision 1.8  2003/02/06 07:41:37  markl
   corrected array bounds exception

   Revision 1.7  2003/01/19 09:50:53  markl
   Javadoc & comment header updates.

   Revision 1.6  2001/08/31 22:09:07  markl
   Completed implementation of FastListModel

   Revision 1.5  2001/06/26 06:18:45  markl
   Added (currently unused) FastListModel.

   Revision 1.4  2001/03/12 09:27:59  markl
   Source code and Javadoc cleanup.

   Revision 1.3  2000/10/11 10:52:52  markl
   Added mutator.

   Revision 1.2  1999/01/10 02:56:27  markl
   added GPL header & RCS tag
   ----------------------------------------------------------------------------
*/

package kiwi.ui;

import java.awt.Color;
import java.util.*;
import javax.swing.*;

import kiwi.util.ListConsumer;

/** This class represents a visual <i>scrollback</i> buffer. Lines of text may
  * be added to the buffer, which always displays the most recently-added
  * lines. Older lines may be viewed by scrolling back to them using the
  * scrollbar.
  * <p>
  * Once the buffer is full, the oldest lines are discarded as new lines
  * are added.
  *
  * @author Mark Lindner
  */

public class ScrollbackView extends JList implements ListConsumer
  {
  private int saveLines = 100;
  private FastListModel model;
  private boolean autoScrollsOnAdd = true;

  /** Construct a new <code>ScrollBackView</code>.
    *
    * @param rows The height for the component, in rows.
    */

  public ScrollbackView()
    {
    model = new FastListModel();
    setModel(model);
    }

  /** Clear the scrollback. Removes all rows from the buffer. */

  public void clear()
    {
    model.removeAllElements();
    }

  /** Set the buffer size.
   *
   * @param lines The maximum number of lines to save in the buffer.
   * @see #getSaveLines
    */

  public void setSaveLines(int lines)
    {
    if(lines > 0)
      saveLines = lines;
    }

  /** Get the buffer size.
    *
    * @return The maximum number of lines that this buffer will save.
    * @see #setSaveLines
    */

  public int getSaveLines()
    {
    return(saveLines);
    }

  /** Add an item to the buffer. Adds the specified item to the end of the
    * buffer. If the buffer was full, the oldest line is discarded. If
    * necessary, the buffer is scrolled to make the new item visible.
    *
    * @param item The item to add.
    */

  public void addItem(Object item)
    {
    if(model.getSize() >= saveLines)
      model.removeRange(0, (model.getSize() - saveLines));
    model.addElement(item);
    if(autoScrollsOnAdd)
      ensureIndexIsVisible(model.getSize() - 1);
    }

  /**
   */
  
  public void addItems(Vector items)
    {
    int t = model.getSize() + items.size();
    
    if(t > saveLines)
      model.removeRange(0, t - saveLines);
    model.addElements(items);

    if(autoScrollsOnAdd)
      ensureIndexIsVisible(model.getSize() - 1);
    }

  /** Specify whether the buffer should automatically scroll to the bottom
   * when a new item is added.
   *
   * @param flag A flag specifying whether autoscrolling should be enabled or
   * disabled.
   */

  public void setAutoScrollsOnAdd(boolean flag)
    {
    autoScrollsOnAdd = flag;
    }

  /**
   */

  private class FastListModel extends AbstractListModel
    {
    private Vector data = new Vector();


    public int getSize()
      {
      return(data.size());
      }
    
    public void addElement(Object o)
      {
      int x = data.size();

      data.addElement(o);
      fireIntervalAdded(this, x, x);
      }
    
    public void addElements(Vector v)
      {
      int sz = v.size();
      int x = data.size();
      for(int i = 0; i < sz; i++)
        data.addElement(v.elementAt(i));
          
      fireIntervalAdded(this,  x, x + sz - 1);
      }

    public void removeRange(int fromIndex, int toIndex)
      {
      for(int i = toIndex; i >= fromIndex; i--)
        data.removeElementAt(fromIndex);

      fireIntervalRemoved(this, fromIndex, toIndex);
      }

    public void removeAllElements()
      {
      int sz = data.size();
      data.removeAllElements();

      if(sz > 0)
        fireIntervalRemoved(this, 0, --sz);
      }

    public Object getElementAt(int index)
      {
      return(data.elementAt(index));
      }

    }

  }

/* end of source file */
