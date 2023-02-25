/*
 * ConfigurationFileLoader.java
 *
 * Created on 28. April 2002, 14:45
 */

package org.jconfig;

import java.io.*;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Iterator;
import java.util.Enumeration;
import java.util.Properties;
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import org.w3c.dom.*;
/**
 *
 * @author  Andreas Mecky andreas.mecky@xcom.de
 * @author Terry Dye terry.dye@xcom.de
 */
public class ConfigurationFileHandler implements ConfigurationHandler {

    // separator for category and property for HashMap
    private static final char C_SEP   = 127; 
    // prefix for category names
		private static final char C_PFIX  =  64; 
    private boolean validate = false;
    private HashMap initValues;
    private HashMap props;
    private Vector categories;
    /** Creates a new instance of ConfigurationFileLoader */
    public ConfigurationFileHandler() {
    }
    
    /**
     * @param parameters  */    
    public void initialize(HashMap parameters) {
        initValues = parameters;
        props = new HashMap();
        categories = new Vector();
    }
    
    public void setValidation(boolean validate) {
        this.validate = validate;
    }
    
    /**
     * @throws ConfigurationManagerException  */    
    public synchronized void load() throws ConfigurationManagerException {        
        if ( initValues.get("File") != null ) {
            load((File)initValues.get("File"));
        }
        else if ( initValues.get("InputStream") != null ) {
            load((InputStream)initValues.get("InputStream"));
        }
        else if ( initValues.get("URL") != null ) {
            load((String)initValues.get("URL"));
        }
    }
    
    /**
     * @param file
     * @throws ConfigurationManagerException  */    
    public synchronized void load(File file) throws ConfigurationManagerException {        
        if ( file == null ) {
            return;
        }        
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        // set the validation flag (true if it should be validated)
        dbf.setValidating(validate);
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException pce) {
            throw new ConfigurationManagerException("The parser cannot create a new document builder: " + pce.getMessage());
        }
        // if we have to validate then we need to define an error handler here
        if (validate) {
            try {
                // Set an ErrorHandler before parsing
                OutputStreamWriter errorWriter =
                        new OutputStreamWriter(System.err, "UTF-8");
                db.setErrorHandler(
                        new MyErrorHandler(new PrintWriter(errorWriter, true)));
            } catch (Exception e) {
                throw new ConfigurationManagerException("The parser cannot set up the error handler");
            }
        }
        Document doc = null;
        try {
            doc = db.parse(file);
        } catch (SAXException se) {
            throw new ConfigurationManagerException("The parser cannot parse the file: " + se.getMessage());
        } catch (IOException ioe) {
            throw new ConfigurationManagerException("The parser cannot open the file: " + ioe.getMessage());
        }
        processProperties(doc);

    }
                
    /**
     *  Gets the variables attribute of the ConfigurationManager object
     *
     *@param  doc  Description of the Parameter
     *@return      The variables value
     */
    private Hashtable getVariables(Document doc) {
        Hashtable vars = null;
        NodeList nl = doc.getElementsByTagName("variables");
        for (int i = 0; i < nl.getLength(); i++) {
            Node n = nl.item(i);
            vars = new Hashtable();
            for (Node child = n.getFirstChild(); child != null; child = child.getNextSibling()) {
                // get all vraibles
                if (child.getNodeName().equals("variable")) {
                    NamedNodeMap myAtt = child.getAttributes();
                    Node myNode = myAtt.getNamedItem("name");
                    String name = myNode.getNodeValue();
                    myNode = myAtt.getNamedItem("value");
                    String value = myNode.getNodeValue();
                    if (name != null && value != null) {
                        // we store the name as ${name} so we have
                        // it directly the way it is used
                        String rname = "${" + name + "}";
                        vars.put(rname, value);
                    }
                }
            }
        }
        return vars;
    }


    /**
     *  Description of the Method
     *
     *@param  vars  Description of the Parameter
     *@param  line  Description of the Parameter
     *@return       Description of the Return Value
     */
    private String replaceVariables(Hashtable vars, String line) {
        if (vars != null) {
            Enumeration keys = vars.keys();
            int pos = 0;
            // walk through all variables
            while (keys.hasMoreElements()) {
                String currentKey = (String) keys.nextElement();
                String value = (String) vars.get((String) currentKey);
                pos = line.indexOf(currentKey);
                // check if we have found a variable
                if (pos != -1) {
                    // cut the line into 2 pieces and put in the
                    // value of the variable
                    String firstPart = line.substring(0, pos);
                    String secondPart = line.substring(pos + currentKey.length());
                    line = firstPart + value + secondPart;
                }
            }
        }
        return line;
    }


    /**
     *  Description of the Method
     *
     *@param  doc  Description of the Parameter
     */
    private void processProperties(Document doc) {        
        String currentCategory;
        Hashtable myvars = getVariables(doc);
        // first we get all nodes where the element is category
        NodeList nl = doc.getElementsByTagName("category");
        for (int i = 0; i < nl.getLength(); i++) {
            // now we get every node from the list
            Node n = nl.item(i);
            // and get the name attribute for this category
            NamedNodeMap curAtt = n.getAttributes();
            Node curNode = curAtt.getNamedItem("name");
            currentCategory = curNode.getNodeValue();
            categories.add(currentCategory);            
            // now we process all children for this category
            for (Node child = n.getFirstChild(); child != null; child = child.getNextSibling()) {
                if (child.getNodeName().equals("property")) {
                    // we have found a property element and now we grab the name and value
                    // attributes
                    NamedNodeMap myAtt = child.getAttributes();
                    Node myNode = myAtt.getNamedItem("name");
                    String name = myNode.getNodeValue();
                    myNode = myAtt.getNamedItem("value");
                    String value = myNode.getNodeValue();
                    // if we have both then lets store it
                    if (name != null && value != null) {
                        // the key is always category/name
                        // e.g. general/congig_file                        
                        props.put(C_PFIX+currentCategory+C_SEP+name,replaceVariables(myvars, value));
                    }
                }
            }
        }
    }

    /**
     *  This method will read in a file and generate the properties
     *
     *@param  is                              The inputstream
     *@throws  ConfigurationManagerException  if the file cannot be processed
     */
    public synchronized void load(InputStream is) throws ConfigurationManagerException {            
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        // set the validation flag (true if it should be validated)
        dbf.setValidating(validate);
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException pce) {
            throw new ConfigurationManagerException("The parser cannot create a new document builder: " + pce.getMessage());
        }
        // if we have to validate then we need to define an error handler here
        if (validate) {
            try {
                // Set an ErrorHandler before parsing
                OutputStreamWriter errorWriter =
                        new OutputStreamWriter(System.err, "UTF-8");
                db.setErrorHandler(
                        new MyErrorHandler(new PrintWriter(errorWriter, true)));
            } catch (Exception e) {
                throw new ConfigurationManagerException("The parser cannot set up the error handler");
            }
        }
        Document doc = null;
        try {
            doc = db.parse(is);
        } catch (SAXException se) {
            throw new ConfigurationManagerException("The parser cannot parse the file: " + se.getMessage());
        } catch (IOException ioe) {
            throw new ConfigurationManagerException("The parser cannot open the file: " + ioe.getMessage());
        }
        processProperties(doc);

    }


    /**
     *  This method will read the content from an URL and generate the
     *  properties. If you have the need to use a proxy server, create
     *  jconfig.properties file and place it inside your system path.
     *  <BR />
     *  jconfig.properties example:<BR />
     *  # jconfig.properties file<BR />
     *  http.proxyHost=proxy.server.url<BR />
     *  http.proxyPort=3128
     *
     *@param  theURL                          Description of the Parameter
     *@throws  ConfigurationManagerException  if the file cannot be processed
     */
    public synchronized void load(String theURL) throws ConfigurationManagerException {        
        java.net.URL jcfURL = null;
        InputStream is = null;
        try {
            // get a jconfig.properties in classpath, if it exists
            ClassLoader cl = this.getClass().getClassLoader();
            InputStream jcf = cl.getResourceAsStream( "jconfig.properties" );            
            // it is possible that the jconfig.properties does not exist, we get null
            if ( jcf != null ) {
                Properties jcfProperties = new Properties();
                jcfProperties.load( jcf );
            
                // load what is set in system
                Properties prop = System.getProperties(); 
                // if we see http.proxyHost and/or http.proxyPort inside
                // the jconfig.properties, we can set the System.properties
                // for use by the URLConnection object
                if ( jcfProperties.getProperty( "http.proxyHost" ) != null )
                    prop.put( "http.proxyHost", jcfProperties.getProperty( "http.proxyHost" ) );
                if ( jcfProperties.getProperty( "http.proxyPort" ) != null )
                    prop.put( "http.proxyPort", jcfProperties.getProperty( "http.proxyPort" ) );
            }
            // prepare URL, open the connection and grab stream for parsing
            jcfURL = new java.net.URL( theURL );
            java.net.URLConnection con = jcfURL.openConnection();
            is = con.getInputStream();
        }
        catch ( Exception e ) {
            throw new ConfigurationManagerException ( "Problem with URL handling/connection/validating: "
                + e.getMessage() );
        }
	
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(validate);
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException pce) {
            throw new ConfigurationManagerException("The parser cannot create a new document builder: " + pce.getMessage());
        }
        // if we have to validate then we need to define an error handler here
        if (validate) {
            try {
                // Set an ErrorHandler before parsing
                OutputStreamWriter errorWriter =
                        new OutputStreamWriter(System.err, "UTF-8");
                db.setErrorHandler(
                        new MyErrorHandler(new PrintWriter(errorWriter, true)));
            } catch (Exception e) {
                throw new ConfigurationManagerException("The parser cannot set up the error handler");
            }
        }
        Document doc = null;
        try {
            doc = db.parse(is);
        } catch (SAXException se) {
            throw new ConfigurationManagerException("The parser cannot parse the XML: " + se.getMessage());
        } catch (IOException ioe) {
            throw new ConfigurationManagerException("The parser cannot open the file: " + ioe.getMessage());
        }
        processProperties(doc);

    }

    /**
     *  This method will save the current categories and their properties to a
     *  file
     *
     *@param  file                               the file that will be generated
     *@exception  ConfigurationManagerException  Description of the Exception
     */
    public void store(File file,HashMap data) throws ConfigurationManagerException {
        // this will store the properties for a certain category             
        Iterator it = data.keySet().iterator();
        try {
            // let's open the file
            FileWriter fw = new FileWriter(file);
            // and write the header
            fw.write("<?xml version=\"1.0\" ?>\n");
            fw.write("<properties>\n");
            // now we walk through all categories
            while ( it.hasNext() ) {
                String currentCategory = (String)it.next();
                fw.write("  <category name='" + currentCategory + "'>\n");
                // now we get all properties for this category
                Hashtable props = (Hashtable)data.get(currentCategory);                
                Enumeration lprops = props.keys();
                // here we walk through the properties
                while (lprops.hasMoreElements()) {
                    String currentKey = (String) lprops.nextElement();
                    // and write it to the file
                    fw.write("    <property name='" + currentKey + "' value='" + (String) props.get(currentKey) + "'/>\n");
                }
                fw.write("  </category>\n");
            }
            fw.write("</properties>\n");
            // close the file because we are done
            fw.close();
        } catch (Exception e) {
            throw new ConfigurationManagerException("The file cannot be saved");
        }         
    }

    /**
     * This method should return a <code>Vector</code> of all
     * categories. The category names must be stored as <code>Strings</code>
     * inside the <code>Vector</code>.
     *
     * @return all categories inside a <code>Vector</code>
     */
    public Vector getCategories() {
        return categories;
    }    
    
    /**
     * This method should return all properties that was read
     * by the particular handler.<BR />
     * The key for the properties are build like:<BR />
     * category/property name.
     *
     * @return a <code>HashMap</code> with all properties.
     */
    public HashMap getProperties() {
        return props;
    }
    
    /**
     * This method should store all categories and properties.
     */
    public void store() throws ConfigurationManagerException {
        store((File)initValues.get("File"),(HashMap)initValues.get("Data"));
    }
    
    //
    // This is an inner class
    //
    // Error handler to report errors and warnings
    //
    /**
     *  Description of the Class
     *
     *@author     andreas
     *@created    27. Januar 2002
     */
    private static class MyErrorHandler implements ErrorHandler {

        private PrintWriter out;


        /**
         *  Constructor for the MyErrorHandler object
         *
         *@param  out  Description of the Parameter
         */
        MyErrorHandler(PrintWriter out) {
            this.out = out;
        }


        /**
         *  Returns a string describing parse exception details
         *
         *@param  spe  Description of the Parameter
         *@return      The parseExceptionInfo value
         */
        private String getParseExceptionInfo(SAXParseException spe) {
            String systemId = spe.getSystemId();
            if (systemId == null) {
                systemId = "null";
            }
            String info = "URI=" + systemId +
                    " Line=" + spe.getLineNumber() +
                    ": " + spe.getMessage();
            return info;
        }


        // The following methods are standard SAX ErrorHandler methods.
        // See SAX documentation for more info.

        /**
         *  Description of the Method
         *
         *@param  spe               Description of the Parameter
         *@exception  SAXException  Description of the Exception
         */
        public void warning(SAXParseException spe) throws SAXException {
            out.println("Warning: " + getParseExceptionInfo(spe));
        }


        /**
         *  Description of the Method
         *
         *@param  spe               Description of the Parameter
         *@exception  SAXException  Description of the Exception
         */
        public void error(SAXParseException spe) throws SAXException {
            String message = "Error: " + getParseExceptionInfo(spe);
            throw new SAXException(message);
        }


        /**
         *  Description of the Method
         *
         *@param  spe               Description of the Parameter
         *@exception  SAXException  Description of the Exception
         */
        public void fatalError(SAXParseException spe) throws SAXException {
            String message = "Fatal Error: " + getParseExceptionInfo(spe);
            throw new SAXException(message);
        }
    }

}
