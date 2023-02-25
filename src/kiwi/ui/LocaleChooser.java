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
   $Log: LocaleChooser.java,v $
   Revision 1.3  2003/01/19 09:50:53  markl
   Javadoc & comment header updates.

   Revision 1.2  2001/03/12 09:27:57  markl
   Source code and Javadoc cleanup.

   Revision 1.1  1999/04/23 07:26:46  markl
   Initial revision
   ----------------------------------------------------------------------------
*/

package kiwi.ui;

import java.awt.*;
import java.util.*;
import javax.swing.*;

/** This class represents a combo box for selecting a locale. Locales are
  * presented in their own specific localized form.
  *
  * @author Mark Lindner
  */

public class LocaleChooser extends JComboBox
  {
  private JComboBox c_country, c_language;
  private Vector supportedLocales = new Vector();

  /** Construct a new <code>LocaleChooser</code>.
   *
   * @param localeList A string containing a comma-separated list of locales,
   * each of which is of the form
   * "&lt;language&gt;:&lt;country&gt;[:&lt;variant&gt;]". For example:
   * "en:US,fr:FR".
   *
   * @param localizeDisplay If <code>true</code>, each locale entry is
   * localized to itself. Otherwise, all entries are displayed in the current
   * locale.
   */
   
  public LocaleChooser(String localeList, boolean localizeDisplay)
    {
    StringTokenizer st = new StringTokenizer(localeList, ",");
    while(st.hasMoreTokens())
      {
      String s = st.nextToken();
      StringTokenizer st2 = new StringTokenizer(s, ":");
      if(st2.countTokens() < 2) continue;
      String language = st2.nextToken();
      String country = st2.nextToken();
      String variant = (st2.hasMoreTokens() ? st2.nextToken() : null);
      Locale l;
      if(variant != null)
        l = new Locale(language, country, variant);
      else
        l = new Locale(language, country);
      supportedLocales.addElement(l);

      String lang = (localizeDisplay ? l.getDisplayLanguage(l)
                     : l.getDisplayLanguage());
      if(lang.length() > 0)
        {
        String first = lang.substring(0, 1);
        first = (localizeDisplay ? first.toUpperCase(l)
                 : first.toUpperCase());
        lang = first + lang.substring(1);
        }

      String item = lang + " - " + (localizeDisplay ? l.getDisplayCountry(l)
                                    : l.getDisplayCountry());
      addItem(item);
      }
    }

  /** Get the currently selected locale.
   *
   * @return A <code>Locale</code> object corresponding to the currently-
   * selected locale, or <code>null</code> if there is no selection.
   */
   
  public Locale getSelectedLocale()
    {
    int x = getSelectedIndex();
    if(x < 0) return(null);
    return((Locale)(supportedLocales.elementAt(x)));
    }

  }

/* end of source file */
