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
   $Log: LocaleManager.java,v $
   Revision 1.15  2003/02/06 07:44:42  markl
   Don't change default Locale if we don't need to.

   Revision 1.14  2003/01/19 09:42:20  markl
   Added getShortDateFormat() method.

   Revision 1.13  2002/08/11 09:49:09  markl
   Javadoc correction.

   Revision 1.12  2001/08/28 20:28:21  markl
   Fixes to defer access to Toolkit for situations where no X Display is
   available.

   Revision 1.11  2001/06/26 06:11:01  markl
   Made constructor public.

   Revision 1.10  2001/03/18 06:37:26  markl
   No longer a singleton, added getLocale() method.

   Revision 1.9  2001/03/12 02:57:41  markl
   Source code cleanup.

   Revision 1.8  2000/06/09 01:47:54  markl
   Added Collator instance and getCollator() method.

   Revision 1.7  2000/03/17 10:41:30  markl
   Simple fix to correct timezone in date formats.

   Revision 1.6  1999/07/29 06:45:38  markl
   Fixed a NPE problem.

   Revision 1.5  1999/07/25 13:40:07  markl
   Added getDateFormatSymbols() method.

   Revision 1.4  1999/06/28 08:19:31  markl
   Added many new methods and more sophisticated parsing/formatting logic.

   Revision 1.3  1999/06/08 06:45:57  markl
   Added lots of new methods.

   Revision 1.2  1999/04/19 05:32:13  markl
   New I18N support.

   Revision 1.1  1999/04/18 10:26:20  markl
   Initial revision
   ----------------------------------------------------------------------------
*/

package kiwi.util;

import java.io.*;
import java.text.*;
import java.util.*;

import kiwi.text.*;

/** The Kiwi locale manager. This class
 * retrieves resource bundles from one or more <code>ResourceManager</code>s,
 * and provides convenience methods for formatting various types of data
 * according to the rules of the current locale.
 * <p>
 * In the case of decimal-based values such as percentages and currency
 * amounts, if the corresponding parse fails, this class will resort to
 * parsing the string as a generic decimal value, throwing an exception if
 * that also fails. This allows for the parsing of values that are formatted
 * specifically or generically. For example, the currency value $49.55 will
 * parse to the decimal value <tt>49.55</tt> whether the source string is
 * <tt>"$49.55"</tt> or <tt>"49.55"</tt>.
 *
 * @author Mark Lindner
 */

public class LocaleManager implements FormatConstants
  {
  private static LocaleManager defaultLocaleManager = new LocaleManager();
  private static Collator collator;
  private NumberFormat currencyFormat, percentFormat, decimalFormat,
    integerFormat;
  private Vector resourceManagers;
  private static ParsePosition pos = new ParsePosition(0);
  private DateFormat dateFormat[] = new DateFormat[3];
  private DateFormat timeFormat[] = new DateFormat[3];
  private DateFormat dateTimeFormat[] = new DateFormat[3];  
  private int lengthTypes[] = { DateFormat.SHORT, DateFormat.MEDIUM,
                                DateFormat.LONG };
  private DateFormatSymbols dateFormatSymbols = null;
  
  /** The default number of decimal digits to retain when formatting
   * currency values.
   */

  public static final int DEFAULT_CURRENCY_DECIMALS = 2;

  /** The default number of decimal digits to retain when formatting
   * percentage values.
   */

  public static final int DEFAULT_PERCENTAGE_DECIMALS = 4;

  /** The default number of decimal digits to retain when formatting
   * numeric values.
   */

  public static final int DEFAULT_NUMBER_DECIMALS = 4;

  /** Construct a new <code>LocaleManager</code>.
   *
   * @since Kiwi 1.3
   *
   * @param resourceManager The <code>ResourceManager</code> that will be
   * used to load resource bundles.
   */
  
  public LocaleManager(ResourceManager resourceManager)
    {
    resourceManagers = new Vector();
    resourceManagers.addElement(resourceManager);

    setLocale(Locale.getDefault());
    }

  /** Construct a new <code>LocaleManager</code>.
   *
   * @since Kiwi 1.3
   *
   */
  
 public LocaleManager()
    {
    this(ResourceManager.getKiwiResourceManager());
    }

  /** Get the default locale.
   *
   * @return The current locale for this <code>LocaleManager</code>.
   *
   * @since Kiwi 1.3
   */

  public Locale getLocale()
    {
    return(Locale.getDefault());
    }
  
  /** Set the default locale. This method should be called in place of
   * <code>Locale.setDefault()</code> (though it does call this method itself).
   *
   * @param locale The new locale.
   */
  
  public void setLocale(Locale locale)
    {
    if(! locale.equals(Locale.getDefault()))
      Locale.setDefault(locale);

    for(int i = 0; i < 3; i++)
      {
      dateFormat[i] = DateFormat.getDateInstance(lengthTypes[i]);
      dateFormat[i].setLenient(false);
      dateFormat[i].setTimeZone(TimeZone.getDefault());
      timeFormat[i] = DateFormat.getTimeInstance(lengthTypes[i]);
      timeFormat[i].setLenient(false);
      dateTimeFormat[i] = DateFormat.getDateTimeInstance(lengthTypes[i],
                                                         lengthTypes[i]);
      dateTimeFormat[i].setLenient(false);
      }
    
    currencyFormat = NumberFormat.getCurrencyInstance();
    percentFormat = NumberFormat.getPercentInstance();
    decimalFormat = NumberFormat.getNumberInstance();

    integerFormat = NumberFormat.getNumberInstance();
    integerFormat.setParseIntegerOnly(true);
    integerFormat.setMaximumFractionDigits(0);

    dateFormatSymbols = new DateFormatSymbols(locale);

    collator = Collator.getInstance(locale);
    }

  /** Get a reference to the default <code>LocaleManager</code>.
   *
   * @deprecated Use getDefaultLocaleManager() instead.
   */
  
  public static LocaleManager getLocaleManager()
    {
    return(getDefaultLocaleManager());
    }

  /** Get a reference to the default <code>LocaleManager</code>.
   *
   * @since Kiwi 1.3
   *
   * @return The default <code>LocaleManager</code>.
   */
  
  public static LocaleManager getDefaultLocaleManager()
    {
    return(defaultLocaleManager);
    }
  
  /** Register a <code>ResourceManager</code> with the locale manager. The
   * locale manager searches through all registered resource managers when
   * searching for a resource bundle. The Kiwi internal resource manager is
   * always registered.
   *
   * @param manager The <code>ResourceManager</code> to register.
   */
  
  public void addResourceManager(ResourceManager manager)
    {
    synchronized(resourceManagers)
      {
      // don't allow the same manager to be added twice

      int i = resourceManagers.indexOf(manager);
      if(i < 0)
        resourceManagers.addElement(manager);
      }
    }

  /** Unregister a <code>ResourceManager</code> from the locale manager. The
   * Kiwi internal resource manager is always registered and cannot be removed.
   *
   * @param manager The <code>ResourceManager</code> to unregister.
   */
  
  public void removeResourceManager(ResourceManager manager)
    {
    synchronized(resourceManagers)
      {
      // don't allow the default manager to be removed
      
      int i = resourceManagers.indexOf(manager);
      if(i > 0)
        resourceManagers.removeElement(manager);
      }
    }

  /** Retrieve the named resource bundle. The locale manager queries all
   * resource managers sequentially for the desired resource bundle, starting
   * with the Kiwi internal resource manager, returning as soon as the
   * bundle is found.
   *
   * @exception java.util.ResourceNotFoundException If the specified bundle
   * could not be located by any of the registered resource managers.
   * @return The <code>LocaleData</code> object representing the specified
   * resource bundle.
   */
  
  public LocaleData getLocaleData(String name) throws ResourceNotFoundException
    {
    LocaleData ld = null;

    synchronized(resourceManagers)
      {
      Enumeration e = resourceManagers.elements();

      while(e.hasMoreElements())
        {
        ResourceManager rm = (ResourceManager)e.nextElement();
        try
          {
          ld = rm.getResourceBundle(name);
          }
        catch(ResourceNotFoundException ex)
          {
          }
        }
      }
      
    if(ld != null)
      return(ld);

    throw(new ResourceNotFoundException(name));
    }

  /** Format a date according to the rules of the current locale, with the
   * default (medium) format length.
   *
   * @param date The date to format.
   * @return A string representation of the value.
   */
  
  public String formatDate(Calendar date)
    {
    return(formatDate(date.getTime(), MEDIUM));
    }

  /** Format a date according to the rules of the current locale, with the
   * default (medium) format length.
   *
   * @param date The date to format.
   * @return A string representation of the value.
   */
  
  public String formatDate(Date date)
    {
    return(formatDate(date, MEDIUM));
    }

  /** Format a date according to the rules of the current locale, with the
   * specified format length.
   *
   * @param date The date to format.
   * @param type The format length; one of the symbolic constants
   * <code>SHORT</code>, <code>MEDIUM</code>, or <code>LONG</code>.
   * @return A string representation of the value.
   */
  
  public String formatDate(Date date, int type)
    {
    return(dateFormat[type].format(date));
    }  

  /** Format a time according to the rules of the current locale, with the
   * default (medium) format length.
   *
   * @param date The date to format.
   * @return A string representation of the value.
   */
  
  public String formatTime(Calendar date)
    {
    return(formatTime(date.getTime(), MEDIUM));
    }

  /** Format a time according to the rules of the current locale, with the
   * default (medium) format length.
   *
   * @param date The date to format.
   * @return A string representation of the value.
   */
  
  public String formatTime(Date date)
    {
    return(formatTime(date, MEDIUM));
    }

  /** Format a time according to the rules of the current locale, with the
   * specified format length.
   *
   * @param date The date to format.
   * @param type The format length; one of the symbolic constants
   * <code>SHORT</code>, <code>MEDIUM</code>, or <code>LONG</code>.
   * @return A string representation of the value.
   */
  
  public String formatTime(Date date, int type)
    {
    return(timeFormat[type].format(date));
    }  

  /** Format a date and time according to the rules of the current locale,
   * with the default (medium) format length.
   *
   * @param date The date to format.
   * @return A string representation of the value.
   */
  
  public String formatDateTime(Calendar date)
    {
    return(formatDateTime(date.getTime(), MEDIUM));
    }

  /** Format a date and time according to the rules of the current locale,
   * with the default (medium) format length.
   *
   * @param date The date to format.
   * @return A string representation of the value.
   */
  
  public String formatDateTime(Date date)
    {
    return(formatDateTime(date, MEDIUM));
    }

  /** Format a date and time according to the rules of the current locale,
   * with the specified format length.
   *
   * @param date The date to format.
   * @param type The format length; one of the symbolic constants
   * <code>SHORT</code>, <code>MEDIUM</code>, or <code>LONG</code>.
   * @return A string representation of the value.
   */
  
  public String formatDateTime(Date date, int type)
    {
    return(dateTimeFormat[type].format(date));
    }  

  /** Parse a date value from a string using the rules of the current locale,
   * with the default (medium) format length.
   *
   * @param s The string to parse.
   * @return The resulting <code>Date</code> object.
   * @exception java.text.ParseException If the value could not be parsed.
   */

  public Date parseDate(String s) throws ParseException
    {
    return(parseDate(s, MEDIUM));
    }
  
  /** Parse a date value from a string using the rules of the current locale,
   * with the specified format length.
   *
   * @param s The string to parse.
   * @param type The format length; one of the symbolic constants
   * <code>SHORT</code>, <code>MEDIUM</code>, or <code>LONG</code>.
   * @return The resulting <code>Date</code> object.
   * @exception java.text.ParseException If the value could not be parsed.
   */
  
  public synchronized Date parseDate(String s, int type) throws ParseException
    {
    pos.setIndex(0);
    Date d = dateFormat[type].parse(s, pos);
    trapGarbage(s);
    return(d);
    }

  /** Parse a time value from a string using the rules of the current locale,
   * with the default (medium) format length.
   *
   * @param s The string to parse.
   * @return The resulting <code>Date</code> object.
   * @exception java.text.ParseException If the value could not be parsed.
   */

  public Date parseTime(String s) throws ParseException
    {
    return(parseTime(s, MEDIUM));
    }
  
  /** Parse a time value from a string using the rules of the current locale,
   * with the specified format length.
   *
   * @param s The string to parse.
   * @param type The format length; one of the symbolic constants
   * <code>SHORT</code>, <code>MEDIUM</code>, or <code>LONG</code>.
   * @return The resulting <code>Date</code> object.
   * @exception java.text.ParseException If the value could not be parsed.
   */
  
  public synchronized Date parseTime(String s, int type) throws ParseException
    {
    pos.setIndex(0);
    Date d = timeFormat[type].parse(s, pos);
    trapGarbage(s);
    return(d);
    }

  /** Parse a date and time value from a string using the rules of the current
   * locale, with the default (medium) format length.
   *
   * @param s The string to parse.
   * @return The resulting <code>Date</code> object.
   * @exception java.text.ParseException If the value could not be parsed.
   */

  public Date parseDateTime(String s) throws ParseException
    {
    return(parseDateTime(s, MEDIUM));
    }
  
  /** Parse a date and time value from a string using the rules of the current
   * locale, with the specified format length.
   *
   * @param s The string to parse.
   * @param type The format length; one of the symbolic constants
   * <code>SHORT</code>, <code>MEDIUM</code>, or <code>LONG</code>.
   * @return The resulting <code>Date</code> object.
   * @exception java.text.ParseException If the value could not be parsed.
   */
  
  public synchronized Date parseDateTime(String s, int type)
    throws ParseException
    {
    pos.setIndex(0);
    Date d = dateTimeFormat[type].parse(s, pos);
    trapGarbage(s);
    return(d);
    }
  
  /** Format a currency value according to the rules of the current locale,
   * with <code>DEFAULT_CURRENCY_DECIMALS</code> decimal places retained, and
   * grouping turned off.
   *
   * @param value The currency value to format.
   * @return A string representation of the value.
   */
  
  public String formatCurrency(double value)
    {
    return(formatCurrency(value, DEFAULT_CURRENCY_DECIMALS, false));
    }

  /** Format a currency value according to the rules of the current locale,
   * with the specified number of decimal places retained, and grouping turned
   * off.
   *
   * @param value The currency value to format.
   * @param decimals The number of decimal places to retain.
   * @return A string representation of the value.
   */
  
  public String formatCurrency(double value, int decimals)
    {
    return(formatCurrency(value, decimals, false));
    }

  /** Format a currency value according to the rules of the current locale,
   * with the specified number of decimal places retained, and the specified
   * grouping policy.
   *
   * @param value The currency value to format.
   * @param decimals The number of decimal places to retain.
   * @param grouping A flag specifying whether grouping should be used.
   * @return A string representation of the value.
   */
  
  public synchronized String formatCurrency(double value, int decimals,
                                            boolean grouping)
    {
    synchronized(currencyFormat)
      {
      currencyFormat.setMaximumFractionDigits(decimals);
      currencyFormat.setMinimumFractionDigits(decimals);
      currencyFormat.setGroupingUsed(grouping);
      return(currencyFormat.format(value));
      }
    }
  
  /** Format a currency value according to the rules of the current locale,
   * with <code>DEFAULT_CURRENCY_DECIMALS</code> decimal places retained, and
   * grouping turned off.
   *
   * @param value The currency value to format.
   * @return A string representation of the value.
   */

  public String formatCurrency(float value)
    {
    return(formatCurrency(value, DEFAULT_CURRENCY_DECIMALS, false));
    }

  /** Format a currency value according to the rules of the current locale,
   * with the specified number of decimal places retained, and grouping turned
   * off.
   *
   * @param value The currency value to format.
   * @param decimals The number of decimal places to retain.
   * @return A string representation of the value.
   */
  
  public String formatCurrency(float value, int decimals)
    {
    return(formatCurrency(value, decimals, false));
    }

  /** Format a currency value according to the rules of the current locale,
   * with the specified number of decimal places retained, and the specified
   * grouping policy.
   *
   * @param value The currency value to format.
   * @param decimals The number of decimal places to retain.
   * @param grouping A flag specifying whether grouping should be used.
   * @return A string representation of the value.
   */
  
  public String formatCurrency(float value, int decimals, boolean grouping)
    {
    synchronized(currencyFormat)
      {
      currencyFormat.setMaximumFractionDigits(decimals);
      currencyFormat.setMinimumFractionDigits(decimals);
      currencyFormat.setGroupingUsed(grouping);
      return(currencyFormat.format(value));
      }
    }

  /** Parse a currency value from a string using the rules of the current
   * locale. The input string may include grouping characters.
   *
   * @param s The string to parse.
   * @return The resulting value.
   * @exception java.text.ParseException If the value could not be parsed.
   */
   
  public synchronized double parseCurrency(String s) throws ParseException
    {
    try
      {
      s = s.trim();
      currencyFormat.setGroupingUsed(true);
      pos.setIndex(0);
      Number n = currencyFormat.parse(s, pos);
      trapGarbage(s);
      return((n == null) ? 0 : n.doubleValue());
      }
    catch(ParseException ex)
      {
      return(parseDecimal(s));
      }
    }
  
  /** Format a percentage value according to the rules of the current locale,
   * with <code>DEFAULT_PERCENTAGE_DECIMALS</code> decimal places retained, and
   * grouping turned off.
   *
   * @param value The percentage value to format.
   * @return A string representation of the value.
   */

  public String formatPercentage(double value)
    {
    return(formatPercentage(value, DEFAULT_PERCENTAGE_DECIMALS, false));
    }

  /** Format a percentage value according to the rules of the current locale,
   * with the specified number of decimal places retained, and grouping turned
   * off.
   *
   * @param value The percentage value to format.
   * @param decimals The number of decimal places to retain.
   * @return A string representation of the value.
   */
  
  public String formatPercentage(double value, int decimals)
    {
    return(formatPercentage(value, decimals, false));
    }

  /** Format a percentage value according to the rules of the current locale,
   * with the specified number of decimal places retained, and the specified
   * grouping policy.
   *
   * @param value The percentage value to format.
   * @param decimals The number of decimal places to retain.
   * @param grouping A flag specifying whether grouping should be used.
   * @return A string representation of the value.
   */
  
  public synchronized String formatPercentage(double value, int decimals,
                                              boolean grouping)
    {
    percentFormat.setMinimumFractionDigits(decimals);
    percentFormat.setMaximumFractionDigits(decimals);
    percentFormat.setGroupingUsed(grouping);
    return(percentFormat.format(value));
    }

  /** Format a percentage value according to the rules of the current locale,
   * with <code>DEFAULT_PERCENTAGE_DECIMALS</code> decimal places retained, and
   * grouping turned off.
   *
   * @param value The percentage value to format.
   * @return A string representation of the value.
   */

  public String formatPercentage(float value)
    {
    return(formatPercentage(value, DEFAULT_PERCENTAGE_DECIMALS, false));
    }

  /** Format a percentage value according to the rules of the current locale,
   * with the specified number of decimal places retained, and grouping turned
   * off.
   *
   * @param value The percentage value to format.
   * @param decimals The number of decimal places to retain.
   * @return A string representation of the value.
   */
  
  public String formatPercentage(float value, int decimals)
    {
    return(formatPercentage(value, decimals, false));
    }

  /** Format a percentage value according to the rules of the current locale,
   * with the specified number of decimal places retained, and the specified
   * grouping policy.
   *
   * @param value The percentage value to format.
   * @param decimals The number of decimal places to retain.
   * @param grouping A flag specifying whether grouping should be used.
   * @return A string representation of the value.
   */
  
  public synchronized String formatPercentage(float value, int decimals,
                                              boolean grouping)
    {
    percentFormat.setMinimumFractionDigits(decimals);
    percentFormat.setMaximumFractionDigits(decimals);
    percentFormat.setGroupingUsed(grouping);
    return(percentFormat.format(value));
    }

  /** Parse a percentage value from a string using the rules of the current
   * locale. The input string may include grouping characters.
   *
   * @param s The string to parse.
   * @return The resulting value.
   * @exception java.text.ParseException If the value could not be parsed.
   */
  
  public synchronized double parsePercentage(String s) throws ParseException
    {
    try
      {
      s = s.trim();
      percentFormat.setGroupingUsed(true);
      pos.setIndex(0);
      Number n = percentFormat.parse(s, pos);
      trapGarbage(s);
      return((n == null) ? 0 : n.doubleValue());
      }
    catch(ParseException ex)
      {
      return(parseDecimal(s) / 100);
      }
    }

  /** Format an integer value according to the rules of the current
   * locale, with grouping turned off.
   *
   * @param value The integer value to format.
   * @return A string representation of the value.
   */
  
  public String formatInteger(int value)
    {
    return(formatInteger((long)value));
    }

  /** Format an integer value according to the rules of the current
   * locale, with grouping turned off.
   *
   * @param value The integer value to format.
   * @return A string representation of the value.
   */
  
  public String formatInteger(long value)
    {
    return(formatInteger(value, false));
    }

  /** Format an integer value according to the rules of the current
   * locale, and the specified grouping policy.
   *
   * @param value The integer value to format.
   * @return A string representation of the value.
   */
  
  public String formatInteger(int value, boolean grouping)
    {
    return(formatInteger((long)value, grouping));
    }

  /** Format an integer value according to the rules of the current
   * locale, and the specified grouping policy.
   *
   * @param value The integer value to format.
   * @return A string representation of the value.
   */
  
  public synchronized String formatInteger(long value, boolean grouping)
    {
    integerFormat.setGroupingUsed(grouping);
    return(integerFormat.format(value));
    }

  /** Parse an integer value from a string using the rules of the current
   * locale. The input string may include grouping characters.
   *
   * @param s The string to parse.
   * @return The resulting value.
   * @exception java.text.ParseException If the value could not be parsed.
   */
  
  public synchronized long parseInteger(String s) throws ParseException
    {
    s = s.trim();
    integerFormat.setGroupingUsed(true);
    pos.setIndex(0);
    Number n = integerFormat.parse(s, pos);
    trapGarbage(s);
    return((n == null) ? 0 : n.longValue());
    }
  
  /** Format a floating point value according to the rules of the current
   * locale, with <code>DEFAULT_NUMBER_DECIMALS</code> decimal places
   * retained, and grouping turned off.
   *
   * @param value The floating point value to format.
   * @return A string representation of the value.
   */
  
  public String formatDecimal(double value)
    {
    return(formatDecimal(value, DEFAULT_NUMBER_DECIMALS, false));
    }

  /** Format a floating point value according to the rules of the current
   * locale, with the specified number of decimal places retained, and
   * grouping turned off.
   *
   * @param value The floating point value to format.
   * @param decimals The number of decimal places to retain.
   * @return A string representation of the value.
   */
  
  public String formatDecimal(double value, int decimals)
    {
    return(formatDecimal(value, decimals, false));
    }

  /** Format a floating point value according to the rules of the current
   * locale, with the specified number of decimal places retained, and the
   * specified grouping policy.
   *
   * @param value The floating point value to format.
   * @param decimals The number of decimal places to retain.
   * @param grouping A flag specifying whether grouping should be used.
   * @return A string representation of the value.
   */
  
  public synchronized String formatDecimal(double value, int decimals,
                                           boolean grouping)
    {
    decimalFormat.setMinimumFractionDigits(decimals);
    decimalFormat.setMaximumFractionDigits(decimals);
    decimalFormat.setGroupingUsed(grouping);
    return(decimalFormat.format(value));
    }

  /** Format a floating point value according to the rules of the current
   * locale, with <code>DEFAULT_NUMBER_DECIMALS</code> decimal places
   * retained, and grouping turned off.
   *
   * @param value The floating point value to format.
   * @return A string representation of the value.
   */
  
  public String formatDecimal(float value)
    {
    return(formatDecimal((double)value));
    }

  /** Format a floating point value according to the rules of the current
   * locale, with the specified number of decimal places retained, and
   * grouping turned off.
   *
   * @param value The floating point value to format.
   * @param decimals The number of decimal places to retain.
   * @return A string representation of the value.
   */
  
  public String formatDecimal(float value, int decimals)
    {
    return(formatDecimal((double)value, decimals));
    }

  /** Format a floating point value according to the rules of the current
   * locale, with the specified number of decimal places retained, and the
   * specified grouping policy.
   *
   * @param value The floating point value to format.
   * @param decimals The number of decimal places to retain.
   * @param grouping A flag specifying whether grouping should be used.
   * @return A string representation of the value.
   */
    
  public String formatDecimal(float value, int decimals, boolean grouping)
    {
    return(formatDecimal((double)value, decimals, grouping));
    }

  /** Parse a numeric value from a string using the rules of the current
   * locale. The input string may include grouping characters.
   *
   * @param s The string to parse.
   * @return The resulting value.
   * @exception java.text.ParseException If the value could not be parsed.
   */
  
  public synchronized double parseDecimal(String s) throws ParseException
    {
    s = s.trim();
    decimalFormat.setGroupingUsed(true);
    pos.setIndex(0);
    Number n = decimalFormat.parse(s, pos);
    trapGarbage(s);
    return((n == null) ? 0 : n.doubleValue());
    }

  /** Get an instance of the <code>DateFormatSymbols</code> object for the
   * current locale.
   *
   * @return The <code>DateFormatSymbols</code> object.
   */
  
  public DateFormatSymbols getDateFormatSymbols()
    {
    return(dateFormatSymbols);
    }

  /** Get an instance of the <code>Collator</code> object for the current
   * locale.
   *
   * @return The <code>Collator</code> object.
   */

  public Collator getCollator()
    {
    return(collator);
    }

  /** Get a reference to the short date format object for the current locale.
   *
   * @return The <code>DateFormat</code> object.
   *
   * @since Kiwi 1.4
   */

  public DateFormat getShortDateFormat()
    {
    return(dateFormat[SHORT]);
    }
  
  /*
   */
  
  private void trapGarbage(String s) throws ParseException
    {
    if(pos.getIndex() != s.length())
      throw(new ParseException("Garbage in string " + s, pos.getIndex()));
    }
  
  }

/* end of source file */
