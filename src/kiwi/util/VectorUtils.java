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
   $Log: VectorUtils.java,v $
   Revision 1.4  2003/01/19 09:42:39  markl
   Javadoc & comment header updates.

   Revision 1.3  2001/03/12 05:43:34  markl
   Javadoc cleanup.

   Revision 1.2  2001/03/12 03:16:52  markl
   *** empty log message ***

   Revision 1.1  1999/06/08 06:46:44  markl
   Initial revision
   ----------------------------------------------------------------------------
*/

package kiwi.util;

import java.util.*;

/** This is a utility class that provides methods for performing common
 * operations on sorted and unsorted vectors. Some of these operations are
 * taken from set theory, while others are basic list operations.
 *
 * @author Mark Lindner
 */

public class VectorUtils
  {
  private Comparator comparator;
  private MergeSort sorter = new MergeSort();

  /** Construct a new <code>VectorUtils</code> object with the default
   * comparator. The default comparator is the <code>HashCodeComparator</code>.
   *
   * @see kiwi.util.HashCodeComparator
   */
  
  public VectorUtils()
    {
    this(new HashCodeComparator());
    }

  /** Construct a new <code>VectorUtils</code> object with the specified
   * comparator.
   *
   * @param comparator The comparator to use.
   */
  
  public VectorUtils(Comparator comparator)
    {
    this.comparator = comparator;
    }

  /** Set the comparator to be used by the methods in this class.
   *
   * @param comparator The comparator to use.
   */
  
  public synchronized void setComparator(Comparator comparator)
    {
    this.comparator = comparator;
    sorter.setComparator(comparator);
    }

  /** Print the contents of a vector. This method displays a list of all items
   * in a vector. For each item, the class name of the object type is listed,
   * followed by the string representation of the object.
   *
   * @param v The vector to print.
   */
  
  public static void print(Vector v)
    {
    if(v == null)
      {
      System.out.println("(null)");
      return;
      }

    Enumeration e = v.elements();
    while(e.hasMoreElements())
      {
      Object o = e.nextElement();
      System.out.println("[" + StringUtils.getClassName(o.getClass()) + "] "
                         + o.toString());
      }
    }

  /** Search for an object in a vector. If the vector is sorted, a binary
   * search is performed. Otherwise, a linear search is performed.
   *
   * @param v The vector to search.
   * @param o The object to search for.
   * @param sorted A flag specifying whether the vector is sorted.
   * @return The object, if found, and <code>null</code> otherwise.
   */
  
  public Object search(Vector v, Object o, boolean sorted)
    {
    return(sorted ? binarySearch(v, o) : linearSearch(v, o));
    }

  /** Perform a linear search on a vector.
   *
   * @param v The vector to search.
   * @param o The object to search for.
   * @return The object, if found, and <code>null</code> otherwise.
   */
  
  public Object linearSearch(Vector v, Object o)
    {
    Enumeration e = v.elements();
    while(e.hasMoreElements())
      {
      Object obj = e.nextElement();
      if(comparator.compare(o, obj) == 0)
        return(obj);
      }

    return(null);
    }

  /** Perform a binary search on a presumably sorted vector. If the specified
   * vector is not sorted, the results are undefined.
   *
   * @param v The vector to search.
   * @param o The object to search for.
   * @return The object, if found, and <code>null</code> otherwise.
   */
  
  public Object binarySearch(Vector v, Object o)
    {
    int sz = v.size();
    
    if(sz == 0)
      return(null);

    int lower = 0, upper = sz - 1, idx = 0, c;
    Object obj = null;

    while(lower <= upper)
      {
      idx = ((lower + upper) >> 1);
      obj = v.elementAt(idx);
      c = comparator.compare(obj, o);

      if(c == 0)
        return(obj);
      
      if(c < 0)
        lower = idx + 1;
      else
        upper = idx - 1;
      }

    return(null);
    }

  /** Sort a vector. Merge-sorts the given vector, returning a new vector.
   *
   * @param v The vector to sort.
   * @return A vector that includes all items from <code>v</code> in sorted
   * order.
   */
  
  public Vector sort(Vector v)
    {
    int sz = v.size();
    Object a[] = new Object[sz];
    v.copyInto(a);
    sorter.sort(a);

    Vector sorted = new Vector(sz);
    for(int i = 0; i < sz; i++)
      sorted.addElement(a[i]);
    
    return(sorted);
    }
  
  /** Compare two vectors. The comparison is done using the currently set
   * comparator.
   *
   * @param v1 The first vector.
   * @param v2 The second vector.
   * @param sorted A flag specifying whether the vectors are sorted.
   * @return <code>true</code> if the vectors are "equal" and
   * <code>false</code> otherwise.
   */

  public boolean compare(Vector v1, Vector v2, boolean sorted)
    {
    int n = v1.size();
    if(v2.size() != n)
      return(false);

    Enumeration e = v1.elements();
    while(e.hasMoreElements())
      {
      if(search(v2, e.nextElement(), sorted) != null)
        n--;
      }
    
    return(n == 0);
    }

  /** Compute the union of two vectors. The union of two vectors consists of
   * all items unique to one vector and all items unique to the other vector.
   *
   * @param v1 The first vector.
   * @param v2 The second vector.
   * @param sorted A flag specifying whether the vectors are sorted.
   * @return A vector consisting of all items in <code>v1</code> and all
   * items in <code>v2</code> that are not also in <code>v1</code>.
   */

  public Vector union(Vector v1, Vector v2, boolean sorted)
    {
    // create a new vector
    
    Vector v = new Vector();

    // first copy all elements from v1 to v
    
    append(v, v1);

    // then copy all elements from v2 to v that are not already in v
    
    Enumeration e = v2.elements();
    while(e.hasMoreElements())
      {
      Object o = e.nextElement();
      if(search(v, o, sorted) == null)
        v.addElement(o);
      }

    return(v);
    }

  /** Compute the intersection of two vectors. The intersection of two vectors
   * is a vector of elements that appear in both vectors.
   *
   * @param v1 The first vector.
   * @param v2 The second vector.
   * @param sorted A flag specifying whether the vectors are sorted.
   * @return A vector that includes all items that are in both <code>v1</code>
   * and <code>v2</code>.
   */
  
  public Vector intersection(Vector v1, Vector v2, boolean sorted)
    {
    // create a new vector
    
    Vector v = new Vector();

    // copy those elements to v that are in both v1 and v2
    
    Enumeration e = v1.elements();
    while(e.hasMoreElements())
      {
      Object o = e.nextElement();
      if(search(v2, o, sorted) != null)
        v.addElement(o);
      }

    return(v);
    }

  /** Compute the difference between two vectors. The difference between two
   * vectors is a vector consisting of all items in one vector that are not
   * in the other vector.
   *
   * @param v1 The first vector.
   * @param v2 The second vector.
   * @param sorted A flag specifying whether the vectors are sorted.
   * @return A vector that includes all elements from <code>v1</code> that are
   * not in <code>v2</code>.
   */

  public Vector difference(Vector v1, Vector v2, boolean sorted)
    {
    Vector v = new Vector();

    Enumeration e = v1.elements();
    while(e.hasMoreElements())
      {
      Object o = e.nextElement();
      if(search(v2, o, sorted) == null)
        v.addElement(o);
      }

    return(v);
    }

  /** Append one vector to another vector.
   *
   * @param v1 The original vector.
   * @param v2 The vector of items to append to <code>v1</code>.
   * @return <code>v1</code>
   */
  
  public static Vector append(Vector v1, Vector v2)
    {
    if((v2 == null) || (v2.size() == 0))
      return(v1);

    Enumeration e = v2.elements();
    while(e.hasMoreElements())
      v1.addElement(e.nextElement());

    return(v1);
    }
  
  }

/* end of source file */
