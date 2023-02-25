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
   $Log: SimpleStyledEditor.java,v $
   Revision 1.4  2003/01/19 09:50:53  markl
   Javadoc & comment header updates.

   Revision 1.3  2001/03/12 09:28:00  markl
   Source code and Javadoc cleanup.

   Revision 1.2  1999/01/10 03:00:07  markl
   added GPL header & RCS tag
   ----------------------------------------------------------------------------
*/

package kiwi.ui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Enumeration;
import javax.swing.*;
import javax.swing.text.*;

import kiwi.text.*;

/** A simple text editor that supports styles like bold, italic, and underline,
  * and that is able to render embedded markup using proxies.
  *
  * <p><center>
  * <img src="snapshot/SimpleStyledEditor.gif"><br>
  * <i>An example SimpleStyledEditor.</i>
  * </center>
  *
  * @see kiwi.ui.SimpleEditor
  *
  * @author Mark Lindner
  */

public class SimpleStyledEditor extends JTextPane
  {
  private SimpleAttributeSet a_italic, a_plain, a_bold, a_underline;
  private DefaultStyledDocument doc;
  private StringBuffer buffer;
  private boolean italicf, boldf, underlinef, plainf;
  private MarkupProxyFactory proxyFactory;

  /** Construct a new <code>SimpleStyledEditor</code>.
    *
    * @param proxyFactory The <code>MarkupProxyFactory</code> that will provide
    * proxies for rendering markup content.
    */

  public SimpleStyledEditor(MarkupProxyFactory proxyFactory)
    {
    super();

    StyleContext sc = new StyleContext();
    Style def = sc.getStyle(StyleContext.DEFAULT_STYLE);
    StyleConstants.setFontFamily(def, "Serif");
    StyleConstants.setFontSize(def, 14);
    doc = new DefaultStyledDocument(sc);
    setDocument(doc);
		
    a_italic = new SimpleAttributeSet();
    StyleConstants.setItalic(a_italic, true);

    a_bold = new SimpleAttributeSet();
    StyleConstants.setBold(a_bold, true);

    a_underline = new SimpleAttributeSet();
    StyleConstants.setUnderline(a_underline, true);

    a_plain = new SimpleAttributeSet();

    this.proxyFactory = proxyFactory;
    }

  /** Set the text to be displayed in the editor.
    *
    * @param text The text to display.
    * @see #getText
    */

  public synchronized void setText(String text)
    {
    new TextReader(text);
    }

  /** Get the text currently displayed in the editor.
    *
    * @return The text currently displayed in the editor.
    * @see #setText
    **/

  public synchronized String getText()
    {
    buffer = new StringBuffer();
    italicf = boldf = underlinef = false;
    plainf = true;

    Element e[] = doc.getRootElements();
    for(int i = 0; i < e.length; i++)
      decode(e[i]);

    if(boldf) buffer.append("</b>");
    if(italicf) buffer.append("</i>");
    if(underlinef) buffer.append("</u>");

    return(buffer.toString());
    }

  // --- decoder

  private void decode(Element el)
    {
    // decode the element

    AttributeSet attrs = el.getAttributes();
    Enumeration e = attrs.getAttributeNames();

    if(el.isLeaf() && el.getName().equals("component"))
      {
      while(e.hasMoreElements())
	{
	Object name = e.nextElement();
	Object value = attrs.getAttribute(name);

	if(name == StyleConstants.ComponentAttribute)
	  buffer.append(((MarkupProxy)(StyleConstants.getComponent(attrs)))
			.getMarkup());
	}
      }

    else if(el.isLeaf() && el.getName().equals("content"))
      {
      boolean cboldf = false, citalicf = false, cunderlinef = false;

      while(e.hasMoreElements())
	{
	Object name = e.nextElement();
	Object value = attrs.getAttribute(name);

	if((name == StyleConstants.Bold) && ((Boolean)value).booleanValue())
	  cboldf = true;
	else if((name == StyleConstants.Italic)
		&& ((Boolean)value).booleanValue())
	  citalicf = true;
	else if((name == StyleConstants.Underline)
		&& ((Boolean)value).booleanValue())
	  cunderlinef = true;
	}

      if(boldf && !cboldf)
	buffer.append("</b>");
      else if(!boldf && cboldf)
	buffer.append("<b>");
      else if(italicf && !citalicf)
	buffer.append("</i>");
      else if(!italicf && citalicf)
	buffer.append("<i>");
      else if(underlinef && !cunderlinef)
	buffer.append("</u>");
      else if(!underlinef && cunderlinef)
	buffer.append("<u>");
		
      boldf = cboldf;
      italicf = citalicf;
      underlinef = cunderlinef;

      int start = el.getStartOffset();
      int end = el.getEndOffset();
      try
	{
	buffer.append(escapeText(doc.getText(start, (end - start))));
	}
      catch(Exception ex){}	
      }
    else
      {
      if(el.getName().equals("paragraph"))
	{
	while(e.hasMoreElements())
	  {
	  Object name = e.nextElement();
	  Object value = attrs.getAttribute(name);

	  /* ignore these for the time being */
	  }
	}

      // decode the element's children

      int ct = el.getElementCount();
      for(int i = 0; i < ct; i++)
	{
	Element ee = el.getElement(i);
	decode(ee);
	}
      }
    }

  // --- public style/content modification methods

  /** Insert text at the current insertion point.
    *
    * @param text The text to insert.
    */

  public synchronized void insertText(String text)
    {
    String t = escapeText(text);
    replaceSelection(t);
    }

  /** Insert a markup proxy at the current insertion point.
    *
    * @param proxy The proxy to insert.
    */

  public synchronized void insertMarkupProxy(MarkupProxy proxy)
    {
    insertComponent((Component)proxy);
    }

  /** Italicize the currently-selected content. */

  public synchronized void setItalic()
    {
    setCharacterAttributes(a_italic, false);
    }

  /** Boldify the currently-selected content. */

  public synchronized void setBold()
    {
    setCharacterAttributes(a_bold, false);
    }

  /** Underline the currently-selected content. */

  public synchronized void setUnderline()
    {
    setCharacterAttributes(a_underline, false);
    }

  /** Remove all style attributes from the currently-selected content. */

  public synchronized void setPlain()
    {
    setCharacterAttributes(a_plain, true);
    }

  /* escape < and > */

  private String escapeText(String s)
    {
    StringBuffer sb = new StringBuffer();

    for(int i = 0; i < s.length(); i++)
      {
      char c = s.charAt(i);

      if(c == '<')
	sb.append("<lt>");
      else if(c == '>')
	sb.append("<gt>");
      else
	sb.append(c);
      }
    return(sb.toString());	
    }

  // sgml consumer

  private class TextReader implements SGMLConsumer
    {
    private String lastString = null;
    private boolean italicf = false, boldf = false, underlinef = false;

    TextReader(String text)
      {
      SGMLParser parser = new SGMLParser(new StringReader(text), this);
      try
	{
	doc.remove(0, doc.getLength());
	parser.parse();

	if(lastString != null)
	  insertString();
	}
      catch(Exception ex) {}
      }

    public void processString(String s)
      {
      if(lastString != null)
	insertString();
      lastString = s;
      }

    private void insertString()
      {
      insertString(lastString);
      }

    private void insertString(String s)
      {
      SimpleAttributeSet attrs = new SimpleAttributeSet();

      if(italicf)
	StyleConstants.setItalic(attrs, true);

      if(boldf)
	StyleConstants.setBold(attrs, true);

      if(underlinef)
	StyleConstants.setUnderline(attrs, true);

      try
	{
	doc.insertString(doc.getLength(), s, attrs);
	}
      catch(/*BadLocation*/Exception ex)
	{
	ex.printStackTrace();
	}
      }

    public boolean processElement(SGMLElement e)
      {
      // dump out last string, if any

      if(lastString != null)
	insertString();

      // adjust style state
			
      String tag = e.getTag();
      if(tag.equals("b"))
	boldf = !e.isEnd();

      else if(tag.equals("i"))
	italicf = !e.isEnd();

      else if(tag.equals("u"))
	underlinef = !e.isEnd();

      else if(tag.equals("lt"))
	insertString("<");

      else if(tag.equals("gt"))
	insertString(">");

      else
	{
	MarkupProxy pxy = proxyFactory.getMarkupProxy(e);
	if(pxy != null)
	  {
	  SimpleAttributeSet attrs2 = new SimpleAttributeSet();
	  StyleConstants.setComponent(attrs2, pxy);
	  try
	    {
	    doc.insertString(doc.getLength(), " ", attrs2);
	    }
	  catch(BadLocationException ex) {}
	  }
	}
      lastString = null;
      return(false);
      }
    }

  }

/* end of source file */
