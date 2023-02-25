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
   $Log: MergeSort.java,v $
   Revision 1.5  2003/01/19 09:42:39  markl
   Javadoc & comment header updates.

   Revision 1.4  2001/03/12 03:16:48  markl
   *** empty log message ***

   Revision 1.3  1999/06/08 06:46:08  markl
   Modified to use Comparators.

   Revision 1.2  1999/01/10 03:47:05  markl
   added GPL header & RCS tag
   ----------------------------------------------------------------------------
*/

package kiwi.util;

/*
 * Copyright 1997, 1998 by Sun Microsystems, Inc.,
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Sun Microsystems, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Sun.
 */

/**
  * An implementation of MergeSort, which can be subclassed to provide a
  * comparator.
  *
  * @author Scott Violet
  */

public class MergeSort
  {
  private Object toSort[], swapSpace[];
  private Comparator comparator;
  private static final Comparator defaultComparator
    = new StringComparator();
  
  /** Construct a new <code>MergeSort</code> that will use a
   * <code>StringComparator</code>.
   */
  
  public MergeSort()
    {
    this(defaultComparator);
    }
  
  /** Construct a new <code>MergeSort</code> that will use the specified
   * comparator.
   *
   * @param comparator The comparator to use.
   */
  
  public MergeSort(Comparator comparator)
    {
    this.comparator = comparator;
    }

  /** Set the comparator for sorting.
   *
   * @param comparator The comparator to use.
   */
  
  public synchronized void setComparator(Comparator comparator)
    {
    this.comparator = comparator;
    }
  
  /** Sort an array of objects.
    *
    * @param array The array to sort.
    */
  
  public synchronized void sort(Object array[])
    {
    if((array != null) && (array.length > 1))
      {
      int maxLength;

      maxLength = array.length;
      swapSpace = new Object[maxLength];
      toSort = array;
      mergeSort(0, maxLength - 1);
      swapSpace = null;
      toSort = null;
      }
    }

  private void mergeSort(int begin, int end)
    {
    if(begin != end)
      {
      int mid = (begin + end) / 2;
      mergeSort(begin, mid);
      mergeSort(mid + 1, end);
      merge(begin, mid, end);
      }
    }

  private void merge(int begin, int middle, int end)
    {
    int firstHalf, secondHalf, count;

    firstHalf = count = begin;
    secondHalf = middle + 1;

    while((firstHalf <= middle) && (secondHalf <= end))
      {
      if(comparator.compare(toSort[secondHalf], toSort[firstHalf]) < 0)
        swapSpace[count++] = toSort[secondHalf++];
      else
        swapSpace[count++] = toSort[firstHalf++];
      }

    if(firstHalf <= middle)
      {
      while(firstHalf <= middle)
        swapSpace[count++] = toSort[firstHalf++];
      }
    else
      {
      while(secondHalf <= end)
        swapSpace[count++] = toSort[secondHalf++];
      }

    for(count = begin; count <= end; count++)
      toSort[count] = swapSpace[count];
    }

  }

/* end of source file */
