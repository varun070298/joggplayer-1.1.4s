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
   $Log: Config.java,v $
   Revision 1.7  2003/01/19 09:42:38  markl
   Javadoc & comment header updates.

   Revision 1.6  2002/08/11 09:49:44  markl
   Typo fix.

   Revision 1.5  2001/10/31 23:08:05  markl
   Added new constructor.

   Revision 1.4  2001/03/12 02:57:38  markl
   Source code cleanup.

   Revision 1.3  1999/04/19 05:31:25  markl
   Added support for Fonts.

   Revision 1.2  1999/01/10 03:41:46  markl
   added GPL header & RCS tag
   ----------------------------------------------------------------------------
*/

package kiwi.util;

import java.awt.Color;
import java.awt.Font;
import java.beans.*;
import java.io.*;
import java.util.*;
import javax.swing.event.*;

import kiwi.event.*;
import kiwi.text.*;

/** Configuration object. This class extends
  * <code>Properties</code>, adding convenience methods for storing and
  * retrieving properties as strings, integers, booleans, and
  * <code>Color</code>s. All values are stored internally as strings, so that
  * persisting the object will produce a human-readable and
  * -modifiable file.
  * <p>
  * Whenever the contents of the <code>Config</code> object change, a
  * <code>ChangeEvent</code> is fired. Also, when a specific property in the
  * object changes, a <code>PropertyChangeEvent</code> is fired.
  *
  * @see java.util.Properties
  * @see kiwi.io.ConfigFile
  * @see javax.swing.event.ChangeEvent
  * @see java.beans.PropertyChangeEvent
  *
  * @author Mark Lindner
  */

public class Config extends Properties implements PropertyChangeSource
  {
  private static final String DEFAULT_DESCRIPTION = "Configuration Parameters";
  /** The description for this set of configuration parameters. */
  protected String description = DEFAULT_DESCRIPTION;
  /** The support object for firing <code>ChangeEvent</code>s when the object
    * changes.
    */
  protected ChangeSupport support;
  /** The support object for firing <coode>PropertyChangeEvent</code>s when
   * a property changes.
   */
  protected PropertyChangeSupport psupport;
  
  /** Construct a new <code>Config</code> with a default description.
    */

  public Config()
    {
    this(DEFAULT_DESCRIPTION);
    }

  /** Construct a new <code>Config</code> object.
    *
    * @param description The description of the configuration parameters that
    * will be stored in this object (one line of text).
    */

  public Config(String description)
    {
    this.description = description;
    support = new ChangeSupport(this);
    psupport = new PropertyChangeSupport(this);
    }

  /** Construct a new <code>Config</code> object from a <code>Properties</code>
   * list.
   *
   * @since Kiwi 1.3.2
   */

  public Config(Properties properties, String description)
    {
    this(description);

    Enumeration e = properties.propertyNames();
    while(e.hasMoreElements())
      {
      String key = (String)e.nextElement();
      put(key, properties.get(key));
      }
    }

  /** Get the description for this set of configuration parameters.
    *
    * @return The description.
    * @see #setDescription
    */
  
  public String getDescription()
    {
    return(description);
    }

  /** Set the description for this set of configuration parameters.
    *
    * @param description The new description, or <code>null</code> if a default
    * description should be used.
    * @see #getDescription
    */
  
  public void setDescription(String description)
    {
    this.description = (description == null ? DEFAULT_DESCRIPTION
                        : description);
    }
  
  /** Look up a <code>String</code> property.
    *
    * @param key The name of the property.
    * @return The property's value, as a <code>String</code>, or
    * <code>null</code> if a property with the specified name does not exist.
    * @see #putString
    */

  public String getString(String key)
    {
    return((String)get(key));
    }

  /** Look up a <code>String</code> property.
    *
    * @param key The name of the property.
    * @param defaultValue The default value to return.
    * @return The property's value, as a <code>String</code>, or
    * <code>defaultValue</code> if a property with the specified name does not
    * exist.
    * @see #putString
    */

  public String getString(String key, String defaultValue)
    {
    String s = (String)get((Object)key);
    if(s == null) s = defaultValue;

    return(s);
    }

  /** Store a <code>String</code> property.
    *
    * @param key The name of the property.
    * @param value The value of the property.
    * @return The old value associated with this key, or <code>null</code> if
    * there was no previous value.
    * @see #getString
    */

  public String putString(String key, String value)
    {
    String old = getString(key);
    put(key, value);
    
    return(old);
    }

  /** Look up an integer property.
    *
    * @param key The name of the property.
    * @return The property's value, as an <code>int</code>, or <code>0</code>
    * if a property with the specified name does not exist.
    * @see #putInt
    */

  public int getInt(String key)
    {
    return(getInt(key, 0));
    }

  /** Look up an integer property.
    *
    * @param key The name of the property.
    * @param defaultValue The default value to return.
    * @return The property's value, as an <code>String</code>, or
    * <code>defaultValue</code> if a property with the specified name does not
    * exist.
    * @see #putInt
    */

  public int getInt(String key, int defaultValue)
    {
    String s = (String)get(key);
    if(s == null) return(defaultValue);

    return(Integer.parseInt(s));
    }

  /** Store an integer property.
    *
    * @param key The name of the property.
    * @param value The value of the property.
    * @return The old value associated with this key, or 0 if there
    * was no previous value.
    * @see #getInt
    */

  public int putInt(String key, int value)
    {
    int old = getInt(key);
    String s = String.valueOf(value);
    put(key, s);
    
    return(old);
    }

  /** Look up an boolean property.
    *
    * @param key The name of the property.
    * @return The property's value, as a <code>boolean</code>. Returns
    * <code>false</code> if a property with the specified name does not exist.
    * @see #putBoolean
    */

  public boolean getBoolean(String key)
    {
    return(getBoolean(key, false));
    }
  
  /** Look up a boolean property.
    *
    * @param key The name of the property.
    * @param defaultValue The default value to return.
    * @return The property's value, as a <code>boolean</code>, or
    * <code>defaultValue</code> if a property with the specified name does not
    * exist.
    * @see #putBoolean
    */

  public boolean getBoolean(String key, boolean defaultValue)
    {
    String s = (String)get(key);
    if(s == null) return(defaultValue);

    return(Boolean.valueOf(s).booleanValue());
    }

  /** Store a boolean property.
    *
    * @param key The name of the property.
    * @param value The value of the property.
    * @return The old value associated with this key, or <code>false</code> if
    * there was no previous value.
    * @see #getBoolean
    */

  public boolean putBoolean(String key, boolean value)
    {
    boolean old = getBoolean(key);
    String s = String.valueOf(value);
    put(key, s);

    return(old);
    }

  /** Look up a <code>Color</code> property.
    *
    * @param key The name of the property.
    *
    * @return The property's value, as a <code>Color</code>. Returns
    * <code>null</code> if a property with the specified name does not exist,
    * or is not a properly formatted color specification.
    * @see #putColor
    */
  
  public Color getColor(String key)
    {
    return(getColor(key, null));
    }
  
  /** Look up a <code>Color</code> property.
    *
    * @param key The name of the property.
    * @param defaultValue The default value to return.
    * @return The property's value, as a <code>Color</code>, or
    * <code>defaultValue</code> if a property with the specified name does not
    * exist.
    * @see #putColor
    */

  public Color getColor(String key, Color defaultValue)
    {
    String s = (String)get(key);
    if(s == null) return(defaultValue);

    try
      {
      return(ColorFormatter.parse(s));
      }
    catch(ParsingException ex)
      {
      return(null);
      }
    }
  
  /** Store a <code>Color</code> property.
    *
    * @param key The name of the property.
    * @param value The value of the property.
    * @return The old value associated with this key, or <code>null</code> if
    * there was no previous value.
    * @see #getColor
    */

  public Color putColor(String key, Color value)
    {
    Color old = getColor(key);
    String s = ColorFormatter.format(value);
    put(key, s);

    return(old);
    }

  /** Look up a <code>Font</code> property.
    *
    * @param key The name of the property.
    *
    * @return The property's value, as a <code>Font</code>. Returns
    * <code>null</code> if a property with the specified name does not exist,
    * or is not a properly formatted font specification.
    * @see #putFont
    */

  public Font getFont(String key)
    {
    return(getFont(key, null));
    }

  /** Look up a <code>Font</code> property.
    *
    * @param key The name of the property.
    * @param defaultValue The default value to return.
    * @return The property's value, as a <code>Font</code>, or
    * <code>defaultValue</code> if a property with the specified name does not
    * exist.
    * @see #putFont
    */
  
  public Font getFont(String key, Font defaultValue)
    {
    String s = (String)get(key);
    if(s == null) return(defaultValue);

    try
      {
      return(FontFormatter.parse(s));
      }
    catch(ParsingException ex)
      {
      return(null);
      }
    }

  /** Store a <code>Font</code> property.
    *
    * @param key The name of the property.
    * @param value The value of the property.
    * @return The old value associated with this key, or <code>null</code> if
    * there was no previous value.
    * @see #getFont
    */
  
  public Font putFont(String key, Font value)
    {
    Font old = getFont(key);
    String s = FontFormatter.format(value);
    put(key, s);

    return(old);
    }
  
  /** Store an arbitrary property.
    *
    * @param key The object that identifies the property.
    * @param value The value of the property.
    * @return The old value associated with this key, or <code>null</code> if
    * there was no previous value.
    */
  
  public Object put(Object key, Object value)
    {
    Object o = super.put(key, value);
    support.fireChangeEvent();
    psupport.firePropertyChange(key.toString(), o, value);
    
    return(o);
    }

  /** Remove a property. Removes the property for the given key.
   *
   * @param key The object that identifies the property.
   * @return The value associated with this key, or <code>null</code> if there
   * was no property with the given key in this object.
   * @see #clear
   */

  public Object remove(Object key)
    {
    Object o = super.remove(key);
    if(o != null)
      {
      support.fireChangeEvent();
      psupport.firePropertyChange(key.toString(), o, null);
      }
    
    return(o);
    }

  /** Remove all properties. Removes all properties from this object.
    */

  public void clear()
    {
    if(size() > 0)
      {
      super.clear();
      support.fireChangeEvent();
      }
    }
  
  /** Get a list of properties.
    *
    * @return A list of the property names as an <code>Enumeration</code>.
    */

  public Enumeration list()
    {
    return(keys());
    }

  /** Add a <code>ChangeListener</code> to this object's list of listeners.
    *
    * @param listener The listener to add.
    */  
  
  public void addChangeListener(ChangeListener listener)
    {
    support.addChangeListener(listener);
    }

  /** Remove a <code>ChangeListener</code> from this object's list of
    * listeners.
    *
    * @param listener The listener to remove.
    */

  public void removeChangeListener(ChangeListener listener)
    {
    support.removeChangeListener(listener);
    }

  /** Add a <code>PropertyChangeListener</code> to this object's list of
   * listeners.
   *
   * @since Kiwi 1.3
   *
   * @param listener The listener to add.
   */

  public void addPropertyChangeListener(PropertyChangeListener listener)
    {
    psupport.addPropertyChangeListener(listener);
    }

  /** Remove a <code>PropertyChangeListener</code> from this object's list of
   * listeners.
   *
   * @since Kiwi 1.3
   *
   * @param listener The listener to remove.
   */

  public void removePropertyChangeListener(PropertyChangeListener listener)
    {
    psupport.removePropertyChangeListener(listener);
    }
  
  }

/* end of source file */
