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
   $Log: MarkupProxyFactory.java,v $
   Revision 1.4  2003/01/19 09:50:53  markl
   Javadoc & comment header updates.

   Revision 1.3  2001/03/12 09:27:58  markl
   Source code and Javadoc cleanup.

   Revision 1.2  1999/01/10 02:49:15  markl
   added GPL header & RCS tag
   ----------------------------------------------------------------------------
*/

package kiwi.ui;

import kiwi.text.*;

/** This class defines a markup proxy; an object that generates
  * <code>MarkupProxy</code> objects appropriate for rendering given
  * <code>SGMLElement</code> objects.
  *
  * @author Mark Lindner
  */

public interface MarkupProxyFactory
  {

  /** Return a <code>MarkupProxy</code> appropriate for rendering an element.
    *
    * @param element The <code>SGMLElement</code> to create a proxy for.
    */
  
  public MarkupProxy getMarkupProxy(SGMLElement element);

  }

/* end of source file */
