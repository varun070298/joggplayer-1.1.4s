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
   $Log: ComponentPrinter.java,v $
   Revision 1.3  2003/01/19 09:42:38  markl
   Javadoc & comment header updates.

   Revision 1.2  2002/08/11 09:57:21  markl
   Version number change.

   Revision 1.1  2002/08/11 09:48:38  markl
   New class.

   ----------------------------------------------------------------------------
*/

package kiwi.util;

import java.awt.*;
import java.awt.print.*;
import javax.swing.*;

/** A class for printing arbitrary components. To print a given component,
 * simply use the class as follows:
 *
 * <pre>
 * ComponentPrinter cp = new ComponentPrinter(someComponent);
 * cp.print();
 * </pre>
 *
 * The <code>print()</code> method may be invoked multiple times.
 *
 * @author <a href="http://www.apl.jhu.edu/~hall/java/">Marty Hall</a>
 * @author John N. Kostaras
 * @author Mark Lindner
 * @since Kiwi 1.3.4
 */

public class ComponentPrinter implements Printable
  {
  private Component component;

  /** Construct a new <code>ComponentPrinter</code> for printing the specified
   * component.
   *
   * @param component The component to be printed.
   */
  
  public ComponentPrinter(Component component)
    {
    this.component = component;
    }

  /** Specify a different component to be printed.
   *
   * @param component The new component.
   */

  public void setComponent(Component component)
    {
    this.component = component;
    }

  /** Print the component.
   *
   * @throws java.awt.print.PrinterException If an error occurred during
   * printing.
   */
  
  public void print() throws PrinterException
    {
    PrinterJob printJob = PrinterJob.getPrinterJob();
    PageFormat pageformat = printJob.pageDialog(printJob.defaultPage());
    printJob.setPrintable(this);
    if(printJob.printDialog())
      printJob.print();
    }
  
  /** Implementation of the <code>Printable</code> interface; this method
   *  should not be called directly.
   */
  
  public int print(Graphics g, PageFormat pageFormat, int pageIndex)
    {
    if(pageIndex != 0)
      return(NO_SUCH_PAGE);
    
    RepaintManager repmgr = RepaintManager.currentManager(component);
    
    Graphics2D g2d = (Graphics2D)g;
    g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
    boolean flag = repmgr.isDoubleBufferingEnabled();
    repmgr.setDoubleBufferingEnabled(false);
    component.paint(g2d);
    repmgr.setDoubleBufferingEnabled(flag);
    
    return(PAGE_EXISTS);
    }
    
  }

/* end of source file */
