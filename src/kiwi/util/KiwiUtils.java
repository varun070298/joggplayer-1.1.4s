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
   $Log: KiwiUtils.java,v $
   Revision 1.18  2003/11/07 19:17:59  markl
   Added setFonts() and set*LookAndFeel() methods.

   Revision 1.17  2003/01/19 09:42:39  markl
   Javadoc & comment header updates.

   Revision 1.16  2002/03/08 21:36:39  markl
   Fixed NullPointerException in paintImmediately()

   Revision 1.15  2001/08/28 21:37:33  markl
   Split out stream methods into their own class.

   Revision 1.14  2001/03/12 05:43:34  markl
   Javadoc cleanup.

   Revision 1.13  2001/03/12 02:57:40  markl
   Source code cleanup.

   Revision 1.12  2000/10/15 09:33:08  markl
   Minor fixes to window positioning code.

   Revision 1.11  1999/11/15 03:43:39  markl
   Replaced some public members with accessors.

   Revision 1.10  1999/08/05 14:37:45  markl
   Added printWindow() method.

   Revision 1.9  1999/07/19 03:58:56  markl
   Added bounds checking in centerWindow().

   Revision 1.8  1999/06/28 08:19:08  markl
   Added Font constants.

   Revision 1.7  1999/06/14 00:51:41  markl
   Removed second form of paintImmediately() method; it didn't work reliably.

   Revision 1.6  1999/04/25 03:33:14  markl
   Added new form of paintImmediately() for JComponents.

   Revision 1.5  1999/04/19 05:31:42  markl
   Added some useful Insets constants.

   Revision 1.4  1999/03/11 07:51:31  markl
   Added another form of cascadeWindow().

   Revision 1.3  1999/02/28 00:37:23  markl
   Added yet another form of centerWindow().

   Revision 1.2  1999/01/10 03:47:05  markl
   added GPL header & RCS tag
   ----------------------------------------------------------------------------
*/

package kiwi.util;

import java.awt.*;
import java.awt.datatransfer.*;
import java.io.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import kiwi.ui.AboutFrame;

/** This class consists of several convenience routines and constants,
 * all of which are static.
 *
 * @author Mark Lindner
 */

public final class KiwiUtils
  {
  /** The data transfer block size. */
  public static final int blockSize = 4096;

  /** A phantom Frame. */
  private static Frame phantomFrame = null;

  /** Empty insets (zero pixels on all sides). */
  public static final Insets emptyInsets = new Insets(0, 0, 0, 0);

  /** A default Kiwi border (empty, 5 pixels on all sides). */
  public static final EmptyBorder defaultBorder = new EmptyBorder(5, 5, 5, 5);

  /** A default Kiwi border (empty, 0 pixels on all sides). */
  public static final EmptyBorder emptyBorder = new EmptyBorder(0, 0, 0, 0);

  /** Predefined insets for first component on first or subsequent lines.
   * Defines 5 pixels of space to the right and below.
   */
  public static final Insets firstInsets = new Insets(0, 0, 5, 5);

  /** Predefined insets for last component on first or subsequent lines.
   * Defines 5 pixels of space below.
   */
  public static final Insets lastInsets = new Insets(0, 0, 5, 0);

  /** Predefined insets for first component on last line.
   * Defines 5 pixels of space to the right.
   */
  public static final Insets firstBottomInsets = new Insets(0, 0, 0, 5);

  /** Predefined insets for last component on last line.
   * Defines no pixels of space.
   */
  public static final Insets lastBottomInsets = emptyInsets;  
  
  /** The root of the filesystem. */
  public static final File filesystemRoot
    = new File(System.getProperty("file.separator"));

  /** A default bold font. */
  public static final Font boldFont = new Font("Dialog", Font.BOLD, 12);

  /** A default plain font. */
  public static final Font plainFont = new Font("Dialog", Font.PLAIN, 12);

  /** A default italic font. */
  public static final Font italicFont = new Font("Dialog", Font.ITALIC, 12);

  /** An origin point: (0,0) */
  public static final Point origin = new Point(0, 0);
  
  private static Cursor lastCursor
    = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);

  private static Clipboard clipboard = null;

  private static ResourceManager resmgr = null;
  private static AboutFrame aboutFrame = null;
  
  private KiwiUtils() { }

  /** Paint a component immediately. Paints a component immediately (as opposed
    * to queueing a repaint request in the event queue.)
    *
    * @param c The component to repaint.
    */

  public static final void paintImmediately(Component c)
    {
    Graphics gc = c.getGraphics();
    if(gc != null)
      {
      c.paint(gc);
      gc.dispose();
      }
    }
  
  /** Cascade a window off of a parent window. Moves a window a specified
    * number of pixels below and to the right of another window.
    *
    * @param parent The parent window.
    * @param w The window to cascade.
    * @param offset The number of pixels to offset the window by
    * vertically and horizontally.
    */
  
  public static final void cascadeWindow(Window parent, Window w, int offset)
    {
    _cascadeWindow(parent, w, offset, offset);
    }

  /** Cascade a window off of a parent window. Moves a window a specified
    * number of pixels below and to the right of another window.
    *
    * @param parent The parent window.
    * @param w The window to cascade.
    * @param offsetx The number of pixels to offset the window by horizontally.
    * @param offsety The number of pixels to offset the window by vertically.
    */

  public static final void cascadeWindow(Window parent, Window w, int offsetx,
                                         int offsety)
    {
    _cascadeWindow(parent, w, offsetx, offsety);
    }

  /** Cascade a window off of a parent window. Moves a window 40 pixels below
    * and to the right of another window.
    *
    * @param parent The parent window.
    * @param w The window to cascade.
    */

  public static final void cascadeWindow(Window parent, Window w)
    {
    _cascadeWindow(parent, w, 40, 40);
    }

  /** Cascade a window off of a component's parent window.
   *
   * @param w The window to cascade.
   * @param c The component off whose parent window this window should be
   * cascaded. If a window cannot be found in the component hierarchy above
   * <code>c</code>, the window is centered on the screen.
   */
  
  public static final void cascadeWindow(Component c, Window w)
    {
    Window pw = SwingUtilities.windowForComponent(c);

    if(pw == null)
      KiwiUtils.centerWindow(w);
    else
      KiwiUtils.cascadeWindow(pw, w);
    }
  
  /* common window cascading code */

  private static void _cascadeWindow(Window parent, Window w, int offsetx,
                                     int offsety)
    {
    Dimension s_size, w_size;
    Point loc = parent.getLocation();
    loc.translate(offsetx, offsety);
    
    _positionWindow(w, loc.x, loc.y);
    }

  /** Center a window on the screen.
    *
    * @param w The window to center.
    */
  
  public static final void centerWindow(Window w)
    {
    Dimension s_size, w_size;
    int x, y;

    s_size = w.getToolkit().getScreenSize();
    w_size = w.getSize();

    x = (s_size.width - w_size.width) / 2;
    y = (s_size.height - w_size.height) / 2;

    _positionWindow(w, x, y);
    }

  /*
   * Make sure no part of the window is off-screen.
   */

  private static final void _positionWindow(Window w, int x, int y)
    {
    Dimension s_size, w_size;

    s_size = w.getToolkit().getScreenSize();
    w_size = w.getSize();

    int xe = (x + w_size.width) - s_size.width;
    int ye = (y + w_size.height) - s_size.height;
    
    if(xe > 0)
      x -= xe;
    if(ye > 0)
      y -= ye;
    
    w.setLocation(x, y);    
    }
  
  /** Center a window within the bounds of another window.
    *
    * @param w The window to center.
    * @param parent The window to center within.
    */

  public static final void centerWindow(Window parent, Window w)
    {
    Dimension p_size, w_size, s_size;
    int x, y;

    p_size = parent.getSize();
    w_size = w.getSize();
    s_size = w.getToolkit().getScreenSize();
    Point p_loc = parent.getLocationOnScreen();

    x = ((p_size.width - w_size.width) / 2) + p_loc.x;
    y = ((p_size.height - w_size.height) / 2) + p_loc.y;

    // If placing the window at (x,y) would make part if it off-screen,
    // then center it in the middle of the screen instead.
    
    if(((x + w_size.width) > s_size.width) || (x < 0)
       || ((y + w_size.height) > s_size.height) || (y < 0))
       centerWindow(w);
    else
      w.setLocation(x, y);
    }

  /** Center a window within the bounds of a component's parent window.
   *
   * @param w The window to center.
   * @param c The component within whose parent window this window should be
   * centered. If a window cannot be found in the component hierarchy above
   * <code>c</code>, the window is centered on the screen.
   */
  
  public static final void centerWindow(Component c, Window w)
    {
    Window pw = SwingUtilities.windowForComponent(c);

    if(pw == null)
      KiwiUtils.centerWindow(w);
    else
      KiwiUtils.centerWindow(pw, w);
    }

  /** Turn on a busy cursor.
    *
    * @param c The component whose cursor will be changed.
    */
  
  public static final void busyOn(Component c)
    {
    lastCursor = c.getCursor();
    c.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    }

  /** Turn off the busy cursor. The last cursor saved will be restored.
    *
    * @param c The component whose cursor will be changed.
    */

  public static final void busyOff(Component c)
    {
    c.setCursor(lastCursor);
    }

  /** Get the Frame parent of a component. This method searches upward in the
    * component hierarchy, searching for an ancestor that is a Frame.
    */

  public static final Frame getFrameForComponent(Component c)
    {
    while((c = c.getParent()) != null)
      if(c instanceof Frame)
        return((Frame)c);

    return(null);
    }

  /** Print a hardcopy of the contents of a window.
   *
   * @param window The window to print.
   * @param title A title for the print job.
   *
   * @return <code>true</code> if the print job was started, or
   * <code>false</code> if the user cancelled the print dialog.
   */

  public static final boolean printWindow(Window window, String title)
    {
    PrintJob pj = Toolkit.getDefaultToolkit()
      .getPrintJob(getPhantomFrame(), title, null);

    if(pj == null)
      return(false);

    int res = Toolkit.getDefaultToolkit().getScreenResolution();
    // Dimension d = pj.getPageDimension(); // buggy in JDK 1.1.x
    Dimension d = new Dimension((int)(2 * res * 8.5), (int)(res * 11 * 2));
    
    d.width -= (int)(res * 0.75 /*in*/);
    d.height -= (int)(res * 0.75 /*in*/);
    
    Graphics gc = pj.getGraphics();
    window.paint(gc);

    gc.dispose();
    pj.end();
    return(true);
    }

  /** Get a reference to the system clipboard.
   *
   * @return The clipboard.
   */

  public static final Clipboard getClipboard()
    {
    if(clipboard == null)
      clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

    return(clipboard);
    }
  
  /** Copy text to the system clipboard.
    *
    * @param text The text to copy to the clipboard.
    */

  public static synchronized final void setClipboardText(String text)
    {
    StringSelection sel = new StringSelection(text);
    getClipboard().setContents(sel, sel);
    }

  /** Copy text from the system clipboard.
    *
    * @return The text that is in the clipboard, or <code>null</code> if the
    * clipboard is empty or does not contain plain text.
    */

  public static synchronized final String getClipboardText()
    {
    try
      {
      return((String)(getClipboard().getContents(Void.class)
                      .getTransferData(DataFlavor.stringFlavor)));
      }
    catch(Exception ex)
      {
      return(null);
      }
    }

  /** Get a reference to the internal resource manager singleton. The Kiwi
    * Toolkit has a small built-in collection of textures, icons, audio clips,
    * and HTML files.
    */

  public static final ResourceManager getResourceManager()
    {
    if(resmgr == null)
      resmgr = new ResourceManager(kiwi.ResourceAnchor.class);

    return(resmgr);
    }

  /** Suspend the calling thread. Suspends the calling thread, returning
    * immediately if an exception occurs.
    *
    * @param sec The number of seconds to sleep.
    */
  
  public static final void sleep(int sec)
    {
    try
      {
      Thread.currentThread().sleep(sec * 1000);
      }
    catch(InterruptedException ex)
      {
      }
    }

  /** Read all of the data from a stream, writing it to another stream. Reads
    * data from the input stream and writes it to the output stream, until no
    * more data is available.
    *
    * @param input The input stream.
    * @param output The output stream.
    *
    * @exception java.io.IOException If an error occurred while reading from
    * the stream.
    *
    * @deprecated This method has been moved to the StreamUtils class.
    */

  public static final OutputStream readStreamToStream(InputStream input,
                                                      OutputStream output)
    throws IOException
    {
    return(StreamUtils.readStreamToStream(input, output));
    }

  /** Read all of the data from a stream, returning the contents as a
    * <code>String</code>. Note that this method is not unicode-aware.
    *
    * @param input The stream to read from.
    *
    * @return The contents of the stream, as a <code>String</code>.
    *
    * @exception java.io.IOException If an error occurred while reading from
    * the stream.
    *
    * @deprecated This method has been moved to the StreamUtils class.    
    */

  public static final String readStreamToString(InputStream input)
    throws IOException
    {
    return(StreamUtils.readStreamToString(input));
    }

  /** Write a string to a stream. Note that this method is not unicode-aware.
    *
    * @exception java.io.IOException If an error occurred while writing to the
    * stream.
    *
    * @param s The string to write.
    * @param output The stream to write it to.
    *
    * @deprecated This method has been moved to the StreamUtils class.
    */

  public static final void writeStringToStream(String s, OutputStream output)
    throws IOException
    {
    StreamUtils.writeStringToStream(s, output);
    }
  
  /** Read all of the data from a stream, returning the contents as a
    * <code>byte</code> array.
    *
    * @param input The stream to read from.
    *
    * @return The contents of the stream, as a <code>byte</code> array.
    *
    * @exception java.io.IOException If an error occurred while reading from
    * the stream.
    *
    * @deprecated This method has been moved to the StreamUtils class.
    */

  public static final byte[] readStreamToByteArray(InputStream input)
    throws IOException
    {
    return(StreamUtils.readStreamToByteArray(input));
    }

  /** Get a reference to a phantom frame.
   *
   * @return The phantom frame.
   */

  public static final Frame getPhantomFrame()
    {
    if(phantomFrame == null)
      phantomFrame = new Frame();

    return(phantomFrame);
    }
  
  /** Get an instance to a prebuilt about window that describes the Kiwi
    * Toolkit itself.
    */
  
  public static final AboutFrame getKiwiAboutFrame()
    {
    if(aboutFrame == null)
      aboutFrame = new AboutFrame("About Kiwi",
                                  getResourceManager().getURL("kiwi.html"));

    return(aboutFrame);
    }

  /** Recursively delete files in a directory. Deletes all files and
    * subdirectories in the given directory.
    *
    * @param parent The parent (presumed to be a directory) of the files to
    * be deleted. The parent is not deleted.
    *
    * @return The number of files and directories deleted.
    */

  public static final int deleteTree(File parent)
    {
    int ct = 0;
    
    if(!parent.isDirectory()) return(0);
    
    String files[] = parent.list();
    for(int i = 0; i < files.length; i++)
      {
      File f = new File(parent.getAbsolutePath(), files[i]);
      
      if(f.isDirectory())
	ct += deleteTree(f);

      f.delete();
      ct++;
      }
    
    return(ct);
    }

  /**
   * Recursively set the font on a container and all of its descendant
   * components.
   *
   * @param container The container.
   * @param font The new font.
   *
   * @since Kiwi 1.4.3
   */
  
  public static void setFonts(Container container, Font font)
    {
    container.setFont(font);

    Component[] components = container.getComponents();

    for(int i = 0; i < components.length; i++)
      {
      if(Container.class.isInstance(components[i]))
        setFonts((Container)components[i], font);
      else
        components[i].setFont(font);
      }
    }

  /**
   * Set the default cross-platform Look and Feel
   *
   * @since Kiwi 1.4.3
   */

  public static void setDefaultLookAndFeel()
    {
    try
      {
      UIManager.setLookAndFeel(
        UIManager.getCrossPlatformLookAndFeelClassName());
      }
    catch(Exception ex) { }
    }

  /**
   * Set the native (system) Look and Feel
   *
   * @since Kiwi 1.4.3
   */

  public static void setNativeLookAndFeel()
    {
    try
      {
      UIManager.setLookAndFeel(
        UIManager.getSystemLookAndFeelClassName());
      }
    catch(Exception ex) { }
    }
  
  }

/* end of source file */
