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
   $Log: DateChooser.java,v $
   Revision 1.22  2003/02/06 07:40:31  markl
   Removed references to Metal Look & Feel.

   Revision 1.21  2003/01/19 09:50:04  markl
   Modified mouse event handling so cell is highlighted on mousedown rather
   than mouseup.

   Revision 1.20  2001/06/26 06:17:35  markl
   Updated with new LocaleManager API.

   Revision 1.19  2001/03/12 09:56:54  markl
   KLabel/KLabelArea changes.

   Revision 1.18  2001/03/12 09:27:54  markl
   Source code and Javadoc cleanup.

   Revision 1.17  2000/10/11 10:46:37  markl
   Fixed mouse event handler and added ActionEvent command strings.

   Revision 1.16  2000/05/29 06:08:25  markl
   Removed debug println.

   Revision 1.15  1999/08/30 22:29:25  markl
   Replaced RepeaterButton with KButton, due to synchronization bugs.

   Revision 1.14  1999/08/18 06:13:18  markl
   Fixed a coordinate calculation bug in the mouse event handler.

   Revision 1.13  1999/08/13 07:11:17  markl
   Bug fix to eliminate NullPointerException.

   Revision 1.12  1999/08/05 14:47:17  markl
   Added accessors for highlight color.

   Revision 1.11  1999/08/05 14:37:14  markl
   Allow setSelectedDay() to select a day that is out of the selection range.

   Revision 1.10  1999/08/03 04:49:13  markl
   Removed debug println.

   Revision 1.9  1999/07/26 08:51:18  markl
   Added ActionEvent support.

   Revision 1.8  1999/07/25 13:57:11  markl
   Bug fix.

   Revision 1.7  1999/07/25 13:46:30  markl
   Javadoc fixes.

   Revision 1.6  1999/07/25 13:35:59  markl
   Extensive changes, including fixes to the paint method, added support for
   constrained date selection range, enhanced month/year wrap-around, and
   internationalization enhancements.

   Revision 1.5  1999/06/14 02:10:35  markl
   Added range selection flag

   Revision 1.4  1999/06/08 06:47:06  markl
   Mouseup event bug fix.

   Revision 1.3  1999/05/05 06:08:50  markl
   Added logic for leap years.

   Revision 1.2  1999/02/28 00:25:42  markl
   Changes from David Croy.
   ----------------------------------------------------------------------------
*/

package kiwi.ui;

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

import kiwi.event.*;
import kiwi.util.*;

/** This class represents a date chooser. The chooser allows an arbitrary date
  * to be selected by presenting a calendar with day, month and year selectors.
  * The range of selectable dates may be constrained by supplying a minimum
  * and/or maximum selectable date. The date chooser is fully locale-aware.
  *
  * <p><center>
  * <img src="snapshot/DateChooser.gif"><br>
  * <i>An example DateChooser.</i>
  * </center>
  *
  * @author Mark Lindner
  */

public class DateChooser extends KPanel implements ActionListener
  {
  private KLabel l_date, l_year, l_month;
  private KButton b_lyear, b_ryear, b_lmonth, b_rmonth;
  private CalendarPane cal;
  private SimpleDateFormat datefmt = new SimpleDateFormat("E  d MMM yyyy");
  private Calendar selectedDate = null, minDate = null, maxDate = null;
  private int selectedDay, firstDay, minDay = -1, maxDay = -1;
  private static final int cellSize = 25;
  private static final int[] daysInMonth
    = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
  private static final int[] daysInMonthLeap
    = { 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
  private String months[], labels[] = new String[7];
  private static final Color weekendColor = Color.red.darker();
  private Color highlightColor, disabledColor;
  private boolean clipMin = false, clipMax = false, clipAllMin = false,
    clipAllMax = false;
  private int weekendCols[] = { 0, 0 };
  private ActionSupport asupport;

  /** <i>Date changed</i> event command. */
  public static final String DATE_CHANGE_CMD = "dateChanged"; 
  /** <i>Month changed</i> event command. */
  public static final String MONTH_CHANGE_CMD = "monthChanged"; 
  /** <i>Year changed</i> event command. */
  public static final String YEAR_CHANGE_CMD = "yearChanged";
  
  /** Construct a new <code>DateChooser</code>. The selection will be
   * initialized to the current date.
   */
  
  public DateChooser()
    {
    this(Calendar.getInstance());
    }

  /** Construct a new <code>DateChooser</code> with the specified selected
   * date.
   *
   * @param <code>date</code> The date for the selection.
   */
  
  public DateChooser(Calendar date)
    {
    asupport = new ActionSupport(this);
    
    DateFormatSymbols sym = LocaleManager.getDefaultLocaleManager()
      .getDateFormatSymbols();

    months = sym.getShortMonths();

    String wkd[] = sym.getShortWeekdays();
    
    for(int i = 0; i < 7; i++)
      {
      int l = Math.min(wkd[i + 1].length(), 2);
      labels[i] = wkd[i + 1].substring(0, l);
      }

    // Let's at least make a half-assed attempt at conforming to the PLAF
    // colors.
    
    highlightColor = UIManager.getColor("List.selectionBackground");
    disabledColor = UIManager.getColor("Label.disabledForeground");
    
    setLayout(new BorderLayout(5, 5));

    KPanel top = new KPanel();
    top.setLayout(new BorderLayout(0, 0));

    KPanel p1 = new KPanel();
    p1.setLayout(new FlowLayout(FlowLayout.LEFT));
    top.add("West", p1);
    
    b_lmonth = new KButton(KiwiUtils.getResourceManager()
                           .getIcon("repeater-left.gif"));
    b_lmonth.setMargin(KiwiUtils.emptyInsets);
    b_lmonth.setFocusPainted(false);
    b_lmonth.setOpaque(false);
    b_lmonth.addActionListener(this);
    p1.add(b_lmonth);

    l_month = new KLabel();
    p1.add(l_month);

    b_rmonth = new KButton(KiwiUtils.getResourceManager()
                           .getIcon("repeater-right.gif"));
    b_rmonth.setMargin(KiwiUtils.emptyInsets);
    b_rmonth.setFocusPainted(false);
    b_rmonth.setOpaque(false);
    b_rmonth.addActionListener(this);
    p1.add(b_rmonth);

    KPanel p2 = new KPanel();
    p2.setLayout(new FlowLayout(FlowLayout.LEFT));
    top.add("East" ,p2);
    
    b_lyear = new KButton(KiwiUtils.getResourceManager()
                          .getIcon("repeater-left.gif"));
    b_lyear.setMargin(KiwiUtils.emptyInsets);
    b_lyear.setFocusPainted(false);
    b_lyear.setOpaque(false);
    b_lyear.addActionListener(this);
    p2.add(b_lyear);

    l_year = new KLabel();
    p2.add(l_year);

    b_ryear = new KButton(KiwiUtils.getResourceManager()
                          .getIcon("repeater-right.gif"));
    b_ryear.setMargin(KiwiUtils.emptyInsets);
    b_ryear.setFocusPainted(false);
    b_ryear.setOpaque(false);
    b_ryear.addActionListener(this);
    p2.add(b_ryear);

    add("North", top);

    cal = new CalendarPane();
    cal.setOpaque(false);
    add("Center", cal);

    l_date = new KLabel("Date", SwingConstants.CENTER);
    add("South", l_date);

    Font f = getFont();
    setFont(new Font(f.getName(), Font.BOLD, f.getSize()));

    int fd = date.getFirstDayOfWeek();
    weekendCols[0] = (Calendar.SUNDAY - fd + 7) % 7;
    weekendCols[1] = (Calendar.SATURDAY - fd + 7) % 7;

    setSelectedDate(date);
    }
  
  /** Get a copy of the <code>Calendar</code> object that represents the
   * currently selected date.
   *
   * @return The currently selected date.
   */
  
  public Calendar getSelectedDate()
    {
    return((Calendar)selectedDate.clone());
    }
  
  /**
   * Set the selected date for the chooser.
   *
   * @param date The date to select.
   */

  public void setSelectedDate(Calendar date)
    {
    selectedDate = copyDate(date, selectedDate);
    selectedDay = selectedDate.get(Calendar.DAY_OF_MONTH);

    _refresh();
    }

  /** Set the earliest selectable date for the chooser.
   *
   * @param date The (possibly <code>null</code>) minimum selectable date.
   */
  
  public void setMinimumDate(Calendar date)
    {
    minDate = ((date == null) ? null : copyDate(date, minDate));
    minDay = ((date == null) ? -1 : minDate.get(Calendar.DATE));
    
    _refresh();
    }

  /** Get the earliest selectable date for the chooser.
   *
   * @return The minimum selectable date, or <code>null</code> if there is no
   * minimum date currently set.
   */
  
  public Calendar getMinimumDate()
    {
    return(minDate);
    }

  /** Set the latest selectable date for the chooser.
   *
   * @param date The (possibly <code>null</code>) maximum selectable date.
   */
  
  public void setMaximumDate(Calendar date)
    {
    maxDate = ((date == null) ? null : copyDate(date, maxDate));
    maxDay = ((date == null) ? -1 : maxDate.get(Calendar.DATE));

    _refresh();
    }

  /** Get the latest selectable date for the chooser.
   *
   * @return The maximum selectable date, or <code>null</code> if there is no
   * maximum date currently set.
   */
  
  public Calendar getMaximumDate()
    {
    return(maxDate);
    }
  
  /**
   * Set the format for the textual date display at the bottom of the
   * component.
   *
   * @param <code>format</code> The new date format to use.
   */
  
  public void setDateFormat(SimpleDateFormat format)
    {
    datefmt = format;

    _refresh();
    }

  /** Handle events. This method is public as an implementation side-effect. */
  
  public void actionPerformed(ActionEvent evt)
    {
    Object o = evt.getSource();
    
    if(o == b_lmonth)
      selectedDate.add(Calendar.MONTH, -1);

    else if(o == b_rmonth)
      selectedDate.add(Calendar.MONTH, 1);

    else if(o == b_lyear)
      {
      selectedDate.add(Calendar.YEAR, -1);
      if(minDate != null)
        {
        int m = minDate.get(Calendar.MONTH);
        if(selectedDate.get(Calendar.MONTH) < m)
          selectedDate.set(Calendar.MONTH, m);
        }
      }

    else if(o == b_ryear)
      {
      selectedDate.add(Calendar.YEAR, 1);
      if(maxDate != null)
        {
        int m = maxDate.get(Calendar.MONTH);
        if(selectedDate.get(Calendar.MONTH) > m)
          selectedDate.set(Calendar.MONTH, m);
        }
      }

    selectedDay = 1;        
    selectedDate.set(Calendar.DATE, selectedDay);

    _refresh();

    asupport.fireActionEvent(((o == b_lmonth) || (o == b_rmonth))
                             ? MONTH_CHANGE_CMD : YEAR_CHANGE_CMD);
    }

  /* Determine what day of week the first day of the month falls on. It's too
   * bad we have to resort to this hack; the Java API provides no means of
   * doing this any other way.
   */

  private void _computeFirstDay()
    {
    int d = selectedDate.get(Calendar.DAY_OF_MONTH);
    selectedDate.set(Calendar.DAY_OF_MONTH, 1);
    firstDay = selectedDate.get(Calendar.DAY_OF_WEEK);
    selectedDate.set(Calendar.DAY_OF_MONTH, d);
    }

  /* This method is called whenever the month or year changes. It's job is to
   * repaint the labels and determine whether any selection range limits have
   * been reached.
   */
  
  private void _refresh()
    {
    l_date.setText(datefmt.format(selectedDate.getTime()));
    l_year.setText(String.valueOf(selectedDate.get(Calendar.YEAR)));
    l_month.setText(months[selectedDate.get(Calendar.MONTH)]);

    _computeFirstDay();
    clipMin = clipMax = clipAllMin = clipAllMax = false;

    b_lyear.setEnabled(true);
    b_ryear.setEnabled(true);
    b_lmonth.setEnabled(true);
    b_rmonth.setEnabled(true);
    
    // Disable anything that would cause the date to go out of range. This
    // logic is extremely sensitive so be very careful when making changes.
    // Every condition test in here is necessary, so don't remove anything.

    if(minDate != null)
      {
      int y = selectedDate.get(Calendar.YEAR);
      int y0 = minDate.get(Calendar.YEAR);
      int m = selectedDate.get(Calendar.MONTH);
      int m0 = minDate.get(Calendar.MONTH);

      b_lyear.setEnabled(y > y0);
      if(y == y0)
        {
        b_lmonth.setEnabled(m > m0);

        if(m == m0)
          {
          clipMin = true;
          int d0 = minDate.get(Calendar.DATE);
          
           if(selectedDay < d0)
             selectedDate.set(Calendar.DATE, selectedDay = d0);

           // allow out-of-range selection
           // selectedDate.set(Calendar.DATE, selectedDay);
          }
        }

      clipAllMin = ((m < m0) || (y < y0));
      }

    if(maxDate != null)
      {
      int y = selectedDate.get(Calendar.YEAR);
      int y1 = maxDate.get(Calendar.YEAR);
      int m = selectedDate.get(Calendar.MONTH);
      int m1 = maxDate.get(Calendar.MONTH);

      b_ryear.setEnabled(y < y1);
      if(y == y1)
        {
        b_rmonth.setEnabled(m < m1);
        if(m == m1)
          {
          clipMax = true;
          int d1 = maxDate.get(Calendar.DATE);
           if(selectedDay > d1)
             selectedDate.set(Calendar.DATE, selectedDay = d1);

          // allow out-of-range selection
          // selectedDate.set(Calendar.DATE, selectedDay);          
          }
        }

      clipAllMax = ((m > m1) || (y > y1));
      }

    // repaint the calendar pane
    
    cal.repaint();
    }

  /** Determine if a year is a leap year.
   *
   * @param year The year to check.
   * @return <code>true</code> if the year is a leap year, and
   * <code>false</code> otherwise.
   */
  
  public static boolean isLeapYear(int year)
    {
    return((((year % 4) == 0) && ((year % 100) != 0)) || ((year % 400) == 0));
    }
  
  // inner class to draw the calendar itself

  private class CalendarPane extends JComponent
    {
    private int ww = 0, hh = 0, dp = 0, x0 = 0, y0 = 0;
        
    /** Construct a new <code>CalendarView</code>. */

    CalendarPane()
      {
      addMouseListener(new _MouseListener2());
      }

    /** Paint the component. */

    public void paint(Graphics gc)
      {
      FontMetrics fm = gc.getFontMetrics();
      Insets ins = getInsets();
      int h = fm.getMaxAscent();
      int hd = (cellSize + h) / 2;

      // figure out how many blank spaces there are before first day of month,
      // and calculate coordinates of first drawn cell

      dp = ((firstDay - selectedDate.getFirstDayOfWeek() + 7) % 7);
      int x = dp, y = 0;
      y0 = ((getSize().height - getPreferredSize().height) / 2);
      int yp = y0;
      x0 = ((getSize().width - getPreferredSize().width) / 2);
      int xp = x0;

      // paint the border
      
      paintBorder(gc);

      // set the clip rect to exclude the border & insets
      
      gc.setColor(Color.black);
      gc.clipRect(ins.left, ins.top, (getSize().width - ins.left - ins.right),
                  (getSize().height - ins.top - ins.bottom));
      gc.translate(ins.left, ins.top);

      // draw the weekday headings

      for(int i = 0, ii = selectedDate.getFirstDayOfWeek() - 1; i < 7;
          i++)
        {
        gc.drawString(labels[ii], xp + 5 + i * (cellSize + 2), yp + h);
        if(++ii == 7)
          ii = 0;
        }

      yp += 20;
      xp += dp * (cellSize + 2);

      // find out how many days there are in the current month
      
      int month = DateChooser.this.selectedDate.get(Calendar.MONTH);
      int dmax = (isLeapYear(DateChooser.this.selectedDate.get(Calendar.YEAR))
                  ? daysInMonthLeap[month] : daysInMonth[month]);

      // draw all the day cells
      
      for(int d = 1; d <= dmax; d++)
        {
        // draw the outline of the cell
        
        gc.setColor(Color.lightGray);
        gc.draw3DRect(xp, yp, cellSize, cellSize, true);        

        // if the cell is selected, fill it with the highlight color
        
        if(d == selectedDay)
          {
          gc.setColor(highlightColor);
          gc.fillRect(xp + 1, yp + 1, cellSize - 1, cellSize - 1);
          }

        // set the pen color depending on weekday or weekend, and paint the
        // day number in the cell

        if((clipMin && (d < minDay)) || (clipMax && (d > maxDay))
           || clipAllMin || clipAllMax)
          gc.setColor(disabledColor);
        else
          gc.setColor(((weekendCols[0] == x) || (weekendCols[1] == x))
                      ? weekendColor : Color.black);
        
        String ss = String.valueOf(d);
        int sw = fm.stringWidth(ss);
        gc.drawString(ss, xp - 3 + (cellSize - sw), yp + hd);

        // advance to the next cell position
        
        if(++x == 7)
          {
          x = 0;
          xp = x0;
          y++;
          yp += (cellSize + 2);
          }
        else
          xp += (cellSize + 2);
        }
      }

    /* Get the preferred size of the component. */

    public Dimension getPreferredSize()
      {
      Insets ins = getInsets();
      return(new Dimension((((cellSize + 2) * 7) + ins.left + ins.right),
                           (((cellSize + 2) * 6) + 20) + ins.top
                           + ins.bottom));
      }

    /* Get the minimum size of the component. */

    public Dimension getMinimumSize()
      {
      return(getPreferredSize());
      }
    
    /* mouse listener */
    
    private class _MouseListener2 extends MouseAdapter
      {
      public void mousePressed(MouseEvent evt)
        {
        int d = getDay(evt);
        if(d < 0)
          return;

        selectedDay = d;
        
        selectedDate.set(Calendar.DAY_OF_MONTH, selectedDay);
        _refresh();
        }
      
      public void mouseReleased(MouseEvent evt)
        {
/*        int d = getDay(evt);
        if(d < 0)
          return;

        selectedDay = d;
        
        selectedDate.set(Calendar.DAY_OF_MONTH, selectedDay);
        _refresh(); */

        asupport.fireActionEvent(DATE_CHANGE_CMD);
        }
      }
      
    /* Figure out which day the mouse click is on. */
      
    private int getDay(MouseEvent evt)
      {
      Insets ins = getInsets();

      int x = evt.getX() - ins.left - x0;
      int y = evt.getY() - ins.top - 20 - y0;
      int maxw = (cellSize + 2) * 7;
      int maxh = (cellSize + 2) * 6;
      
      // check if totally out of range.
      
      if((x < 0) || (x > maxw) || (y < 0) || (y > maxh))
        return(-1);

      y /= (cellSize + 2);
      x /= (cellSize + 2);

      int d = (7 * y) + x - (dp - 1);

      if((d < 1) || (d > selectedDate.getMaximum(Calendar.DAY_OF_MONTH)))
        return(-1);

      if((clipMin && (d < minDay)) || (clipMax && (d > maxDay)))
        return(-1);
      
      return(d);
      }
    }

  /* Copy the relevant portions of a date. */
  
  private Calendar copyDate(Calendar source, Calendar dest)
    {
    if(dest == null)
      dest = Calendar.getInstance();
    
    dest.set(Calendar.YEAR, source.get(Calendar.YEAR));
    dest.set(Calendar.MONTH, source.get(Calendar.MONTH));
    dest.set(Calendar.DATE, source.get(Calendar.DATE));

    return(dest);
    }

  /** Add a <code>ActionListener</code> to this component's list of listeners.
    *
    * @param listener The listener to add.
    */  
  
  public void addActionListener(ActionListener listener)
    {
    asupport.addActionListener(listener);
    }

  /** Remove a <code>ActionListener</code> from this component's list of
    * listeners.
    *
    * @param listener The listener to remove.
    */

  public void removeActionListener(ActionListener listener)
    {
    asupport.removeActionListener(listener);
    }

  /** Set the highlight color for this component.
   *
   * @param color The new highlight color.
   */
  
  public void setHighlightColor(Color color)
    {
    highlightColor = color;
    }

  /** Get the highlight color for this component.
   *
   * @return The current highlight color.
   */
  
  public Color getHighlightColor()
    {
    return(highlightColor);
    }
  
  }

/* end of source file */
