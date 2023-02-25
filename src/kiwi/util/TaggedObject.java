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
   $Log: TaggedObject.java,v $
   Revision 1.5  2003/01/19 09:42:39  markl
   Javadoc & comment header updates.

   Revision 1.4  2001/03/18 06:33:16  markl
   Added toString() method.

   Revision 1.3  2001/03/12 03:16:51  markl
   *** empty log message ***

   Revision 1.2  1999/01/10 03:56:22  markl
   added GPL header & RCS tag
   ----------------------------------------------------------------------------
*/

package kiwi.util;

/** An object-id or object-tag pair. Sometimes it is useful to assign a tag
  * or numeric ID to an object for purposes of identification. Most commonly
  * the identifier is a unique integer, but in some circumstances it is more
  * appropriate to use another object as an identifier. This class allows
  * an object to be associated with either an integer or an arbitrary object.
  *
  * @author Mark Lindner
  */

public class TaggedObject
  {
  private Object obj;
  private Object tag = null;
  private int id = -1;

  /** Construct a new <code>TaggedObject</code> for the given user object
    * and identifier object.
    *
    * @param obj The user object.
    * @param tag The identifier object.
    */
  
  public TaggedObject(Object obj, Object tag)
    {
    this.obj = obj;
    this.tag = tag;
    }

  /** Construct a new <code>TaggedObject</code> for the given user object
    * and numerical ID.
    *
    * @param obj The user object.
    * @param id The numerical ID.
    */
  
  public TaggedObject(Object obj, int id)
    {
    this.obj = obj;
    this.id = id;
    }

  /** Get the user object.
    *
    * @return The user object.
    */
  
  public final Object getObject()
    {
    return(obj);
    }

  /** Get the numerical ID.
    *
    * @return The numerical ID, or <code>-1</code> if there is no numerical ID
    * for this object.
    */
  
  public final int getID()
    {
    return(id);
    }

  /** Get the identifier object.
    *
    * @return The identifier object, or <code>null</code> if there is no
    * identifier object for this object.
    */
  
  public final Object getTag()
    {
    return(tag);
    }

  /** Get a string representation of the tagged object.
   *
   * @since Kiwi 1.3
   */

  public String toString()
    {
    return(obj.toString());
    }
  
  }

/* end of source file */
