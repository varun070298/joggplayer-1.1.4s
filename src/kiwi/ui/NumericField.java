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
   $Log: NumericField.java,v $
   Revision 1.11  2003/01/19 09:50:53  markl
   Javadoc & comment header updates.

   Revision 1.10  2001/03/20 00:54:52  markl
   Fixed deprecated calls.

   Revision 1.9  2001/03/12 09:27:59  markl
   Source code and Javadoc cleanup.

   Revision 1.8  1999/10/05 02:55:26  markl
   Added screenshot

   Revision 1.7  1999/07/29 06:45:19  markl
   Changes to support length constraints.

   Revision 1.6  1999/07/26 08:51:36  markl
   Added getGrouping() and setGrouping() methods.

   Revision 1.5  1999/07/25 13:40:54  markl
   Minor enhancements and bug fixes.

   Revision 1.4  1999/07/19 09:46:35  markl
   More validation fixes.

   Revision 1.3  1999/07/12 08:51:18  markl
   Broke out common functionality.

   Revision 1.2  1999/07/09 04:37:54  markl
   Broke up into two classes.

   Revision 1.1  1999/06/28 08:18:47  markl
   Initial revision
   ----------------------------------------------------------------------------
*/

package kiwi.ui;

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import javax.swing.*;

import kiwi.text.*;
import kiwi.util.*;

/** A subclass of <code>DataField</code> for the input and display of
 * specialized data values, such as currency amounts, percentages, and decimal
 * values.
 *
 * <p><center>
 * <img src="snapshot/NumericField.gif"><br>
 * <i>An example NumericField.</i>
 * </center>
 *
 * @author Mark Lindner
 */

public class NumericField extends DataField
  {
  private static LocaleManager lm = LocaleManager.getDefaultLocaleManager();
  private int type, decimals = 2;
  private double value = 0.0, maxValue, minValue;
  private boolean hasMaxValue = false, hasMinValue = false, grouping = true;

  /** Construct a new <code>NumericField</code> of the specified width and
   * a default type of <code>DECIMAL_FORMAT</code>.
   *
   * @param width The width of the field.
   */
  
  public NumericField(int width)
    {
    this(width, DECIMAL_FORMAT);
    }

  /** Construct a new <code>NumericField</code> of the specified width, for the
   * specified value type.
   *
   * @param width The width of the field.
   * @param type A validation type; one of the format constants defined in the
   * <code>FormatConstants</code> class.
   * @see kiwi.text.FormatConstants
   *
   */
  
  public NumericField(int width, int type)
    {
    super(width);
    
    setType(type);
    setHorizontalAlignment(SwingConstants.RIGHT);
    setFont(KiwiUtils.boldFont);
    }

  /** Set the numeric value to be displayed by this field. The value is
   * formatted as a string, according to the rules of the current locale,
   * and displayed in the field. Invalid input flagging is automatically
   * turned off.
   *
   * @param value The value.
   */
  
  public synchronized void setValue(double value)
    {
    this.value = value;
    String s = "?";

    switch(type)
      {
      case CURRENCY_FORMAT:
        s = lm.formatCurrency(value, decimals, grouping);
        break;

      case PERCENTAGE_FORMAT:
        s = lm.formatPercentage(value, decimals, grouping);
        break;

      case INTEGER_FORMAT:
        s = lm.formatInteger((long)value, grouping);
        break;
        
      case DECIMAL_FORMAT:
      default:
        s = lm.formatDecimal(value, decimals, grouping);
        break;
      }

    setText(s);
    invalid = false;
    paintInvalid(invalid);
    }

  /** Set the numeric value to be displayed by this field.
   *
   * @param value The value. This value is cast internally to a double.
   */

  public void setValue(float value)
    {
    setValue((double)value);
    }

  /** Set the numeric value to be displayed by this field.
   *
   * @param value The value. This value is cast internally to a double.
   */
  
  public void setValue(int value)
    {
    setValue((double)value);
    }

  /** Set the numeric value to be displayed by this field.
   *
   * @param value The value. This value is cast internally to a double.
   */
  
  public void setValue(long value)
    {
    setValue((double)value);
    }

  /** Set the numeric value to be displayed by this field.
   *
   * @param value The value. This value is cast internally to a double.
   */
  
  public void setValue(short value)
    {
    setValue((double)value);
    }
  
  /** Set the validation type for this field.
   *
   * @param type A validation type; one of the format constants defined in the
   * <code>FormatConstants</code> class.
   * @see kiwi.text.FormatConstants
   */

  public void setType(int type)
    {
    this.type = type;

    if(type == INTEGER_FORMAT)
      setDecimals(0);
    else
      setDecimals(2);
    
    validateInput();
    }

  /** Get the validation type for this field.
   *
   * @return The validation type.
   */
  
  public int getType()
    {
    return(type);
    }
  
  /** Get the value from the field. The value returned is the value that
   * was parsed by the last call to <code>validateInput()</code>.
   *
   * @return The parsed value, or 0.0 if the last call to
   * <code>validateInput()</code> resulted in a parsing error, or if there was
   * no previous call to <code>validateInput()</code>.
   * @see #validateInput
   */
  
  public synchronized double getValue()
    {
    String text = getText().trim();

    try
      {
      switch(type)
        {
        case CURRENCY_FORMAT:
          value = lm.parseCurrency(text);
          break;

        case PERCENTAGE_FORMAT:
          value = lm.parsePercentage(text);
          break;

        case INTEGER_FORMAT:
          value = (double)lm.parseInteger(text);
          break;

        case DECIMAL_FORMAT:
        default:
          value = lm.parseDecimal(text);
          break;
        }

      if(hasMinValue && (value < minValue))
        invalid = true;

      if(hasMaxValue && (value > maxValue))
        invalid = true;
      }
    catch(ParseException ex)
      {
      invalid = true;
      }

    return(value);
    }

  /** Validate the input in this field.
   *
   * @return <code>true</code> if the field contains valid input, and
   * <code>false</code> otherwise.
   */
  
  protected boolean checkInput()
    {
    invalid = false;

    double v = getValue();

    if(!invalid)
      setValue(v);

    paintInvalid(invalid);

    return(!invalid);
    }

  /** Set the number of decimals to display to the right of the radix. The
   * default is 2.
   *
   * @param decimals The new decimal count.
   */
  
  public void setDecimals(int decimals)
    {
    if(decimals < 0)
      decimals = 0;
    
    this.decimals = decimals;
    }

  /** Get the number of decimals being displayed to the right of the radix.
   *
   * @return The decimal count.
   */

  public int getDecimals()
    {
    return(decimals);
    }

  /** Set a maximum value constraint. If a value is entered that is greater
   * than the maximum value, the input will not validate.
   *
   * @param value The new maximum value.
   */
  
  public void setMaxValue(double value)
    {
    maxValue = value;
    hasMaxValue = true;
    }

  /** Set a maximum value constraint. If a value is entered that is greater
   * than the maximum value, the input will not validate.
   *
   * @since Kiwi 1.3
   *
   * @param value The new maximum value.
   */
  
  public void setMaxValue(int value)
    {
    setMaxValue((double)value);
    }
  
  /** Clear the maximum value constraint.
   */

  public void clearMaxValue()
    {
    hasMaxValue = false;
    }

  /** Set a minimum value constraint. If a value is entered that is less than
   * the minimum value, the input will not validate.
   *
   * @param value The new minimum value.
   */
  
  public void setMinValue(double value)
    {
    minValue = value;
    hasMinValue = true;
    }

  /** Set a minimum value constraint. If a value is entered that is less than
   * the minimum value, the input will not validate.
   *
   * @since Kiwi 1.3
   *
   * @param value The new minimum value.
   */
  
  public void setMinValue(int value)
    {
    setMinValue((double)value);
    }
  
  /** Clear the minimum value constraint.
   */
  
  public void clearMinValue()
    {
    hasMinValue = false;
    }

  /** Set the grouping mode for this numeric field. If grouping is turned
   * off, values will be formatted without grouping characters separating the
   * thousands. The default mode is on.
   *
   * @param grouping A flag specifying whether grouping should be on
   * (<code>true</code>) or off (<code>false</code>).
   */
   
  public void setGrouping(boolean grouping)
    {
    this.grouping = grouping;
    }

  /** Get the grouping mode for this numeric field.
   *
   * @return The current grouping mode.
   */
    
  public boolean getGrouping()
    {
    return(grouping);
    }

  }

/* end of source file */
