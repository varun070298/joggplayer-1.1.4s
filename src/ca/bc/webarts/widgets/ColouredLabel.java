/*
 *  $Source: /cvsroot/open/projects/WebARTS/ca/bc/webarts/widgets/ColouredLabel.java,v $
 *  $Name:  $
 *  Current File Status:   $Revision: 1.4 $
 *  $Date: 2002/04/02 22:10:23 $
 *  $Locker:  $
 *  Copyright (C) 2001 WebARTS Design, North Vancouver Canada
 */
/*
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package ca.bc.webarts.widgets;

import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;

/**
 *  A class to extend JLabel with some added easily settable colour and Font
 *  settings.<P>
 *
 *  It allows easily setting the text colour and the background colour of the
 *  label. <P>
 *
 *  Now you don't have to override paint.
 *
 *@author     unknown
 *@created    April 2, 2002
 */
public class ColouredLabel extends JLabel {
    private final static String DEFAULT_FONT_FACE = "Arial, Helvetica";
    /**
     *  Description of the Field
     */
    protected Color textColour_ = Color.white;
    /**
     *  Description of the Field
     */
    protected Color backColour_ = Color.black;
    /**
     *  Description of the Field
     */
    protected String labelText_ = "";
    /**
     *  Description of the Field
     */
    protected String fontName_ = "default";
    /**
     *  Description of the Field
     */
    protected String[] fontSizes_ = {"+8", "+7", "+6", "+5", "+4", "+3", "+2", "+1",
            "+0", "-1", "-2", "-3", "-4", "-5", "-6", "-7", "-8"};
    /**
     *  Description of the Field
     */
    protected String fontSize_ = "-1";
    /**
     *  Description of the Field
     */
    protected int fontMidpoint_ = 11;
    /**
     *  Description of the Field
     */
    protected String boldOn_ = "";
    /**
     *  Description of the Field
     */
    protected String boldOff_ = "";
    /**
     *  Description of the Field
     */
    protected String italicOn_ = "";
    /**
     *  Description of the Field
     */
    protected String italicOff_ = "";
    /**
     *  Description of the Field
     */
    protected boolean wrapText_ = true;


    /**
     *  Basic constructor to set up the Label with the specified colour.
     *
     *@param  labelText  the text in the label
     */
    public ColouredLabel(String labelText) {
        labelText_ = labelText;
        setText(labelText);
    }


    /**
     *  Constructor to set up the Label with the specified colour.
     *
     *@param  textColour  is the Color to use in the text in the label
     *@param  labelText   the text in the label
     */
    public ColouredLabel(Color textColour, String labelText) {
        labelText_ = labelText;
        textColour_ = textColour;
        setText(labelText);
    }


    /**
     *  Constructor to set up the Label with the specified colours.
     *
     *@param  backColour  is the Color to use in the back of the label
     *@param  textColour  is the Color to use in the text in the label
     *@param  labelText   the text in the label
     */
    public ColouredLabel(Color backColour, Color textColour, String labelText) {
        labelText_ = labelText;
        textColour_ = textColour;
        backColour_ = backColour;
        setText(labelText);
    }


    /**
     *  Constructor to set up the Label with the specified colours.
     *
     *@param  backColour  is the Color to use in the back of the label
     *@param  textColour  is the Color to use in the text in the label
     *@param  newFont     is the Font to use in the text in the label
     *@param  labelText   the text in the label
     */
    public ColouredLabel(Color backColour,
            Color textColour,
            Font newFont,
            String labelText) {
        labelText_ = labelText;
        textColour_ = textColour;
        backColour_ = backColour;
        setFont(newFont);
        setText(labelText);
    }


    /**
     *  Forces the display of the label text to the specified wrapping.
     *
     *@param  wrapText  flags if the text in the label will wrap or not
     */
    public void setWrapText(boolean wrapText) {
        wrapText_ = wrapText;
    }


    /**
     *  Overrides the JLabel setText to add the extra stuff to get the extra
     *  colours and font stuff.
     *
     *@param  newText  the text to go in the label.
     */
    public void setText(String newText) {
        if (wrapText_) {
            newText = Util.tokenReplace(newText, " ", "&nbsp;");
        }
        super.setText("<html><body bgcolor=\"" + getBackColourHtmlString() +
                "\" text=\"" + getTextColourHtmlString() + "\"><Font face=\"" +
                fontName_ + ", " + DEFAULT_FONT_FACE + "\" size=\"" +
                fontSize_ + "\">" +
                boldOn_ + italicOn_ + newText + italicOff_ + boldOff_ +
                "</font></body></html>");
        /*
         *  super.setText("<Font face=\"" +
         *  fontName_ + ", " + DEFAULT_FONT_FACE + "\" size=\"" +
         *  fontSize_ + "color=\""+getTextColourHtmlString()+"\">" +
         *  boldOn_ + italicOn_ + newText + italicOff_ + boldOff_ +
         *  "</font>");
         */
        labelText_ = newText;
    }


    /**
     *  Allows setting of the Font to use in this label. It parses the Face,
     *  Italic and Bold attributes.
     *
     *@param  newFont  the new font to use for this ColouredLabel.
     */
    public void setFont(Font newFont) {
        super.setFont(newFont);
        fontName_ = newFont.getName();
        if (newFont.isBold()) {
            boldOn_ = "<B>";
            boldOff_ = "</B>";
        } else {
            boldOn_ = "";
            boldOff_ = "";
        }
        if (newFont.isItalic()) {
            italicOn_ = "<I>";
            italicOff_ = "</I>";
        } else {
            italicOn_ = "";
            italicOff_ = "";
        }
        if (fontSizes_ != null) {
            // need to check... super calls this before init

            int fontSizeLookup = fontMidpoint_ - newFont.getSize() + fontSizes_.length / 2;
            fontSize_ = fontSizes_[fontSizeLookup];
            coloursChanged();
        }
    }


    /**
     *  A convienience method to directly set the Font using the face name
     *  instead of sending a Font object.
     *
     *@param  fontName  The new fontName value
     */
    public void setFontName(String fontName) {
        fontName_ = fontName;
        coloursChanged();
    }


    /**
     *  Sets the current Text Colour being used in this label.
     *
     *@param  textColour  the current Text Colour to use in this label.
     */
    public void setTextColour(Color textColour) {
        textColour_ = textColour;
        coloursChanged();
    }


    /**
     *  Sets the current background Text Colour being used in this label.
     *
     *@param  backColour  the current background Colour to use in this label.
     */
    public void setBackColour(Color backColour) {
        backColour_ = backColour;
        coloursChanged();
    }


    /**
     *  Get method for the current Text Colour being used in this label.
     *
     *@return    the current Text Colour being used in this label.
     */
    public Color getTextColour() {
        return textColour_;
    }


    /**
     *  Get method for the current Background Colour being used in this label.
     *
     *@return    the current Background Colour being used in this label.
     */
    public Color getBackColour() {
        return backColour_;
    }


    /**
     *  Get the HTML string representing the Current Text Colour.
     *
     *@return    The textColourHtmlString value
     */
    private String getTextColourHtmlString() {
        return getTextColourHtmlString(textColour_);
    }


    /**
     *  Get the HTML string representing the Current Background Colour.
     *
     *@return    The backColourHtmlString value
     */
    private String getBackColourHtmlString() {
        return getTextColourHtmlString(backColour_);
    }


    /**
     *  Get the HTML string representing the specified Color.
     *
     *@param  c  is the Color to retrieve the html string for.
     *@return    The textColourHtmlString value
     */
    public static String getTextColourHtmlString(Color c) {
        String r = "FF";
        String g = "FF";
        String b = "FF";

        if (c != null) {
            r = Integer.toHexString(c.getRed());
            g = Integer.toHexString(c.getGreen());
            b = Integer.toHexString(c.getBlue());
        }
        String retVal = "#" + r + g + b;
        return retVal;
    }


    /**
     *  Refreshes the text string with some new colour values.
     */
    private void coloursChanged() {
        setText(labelText_);
    }
}

/*
 *  Here is the revision log
 *  ------------------------
 *  $Log: ColouredLabel.java,v $
 *  Revision 1.4  2002/04/02 22:10:23  anonymous
 *  Added CVS Headerv And Log.
 *
 */

