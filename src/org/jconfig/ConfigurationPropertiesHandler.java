/*
 * ConfigurationPropertiesLoader.java
 *
 * Created on 28. April 2002, 14:59
 */

package org.jconfig;

import java.io.*;
import java.util.Properties;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;
/**
 * This class implements the methods for handling a properties file.
 * It can read a properties file or save one or all categories to
 * a properties file.
 *
 * @author  Andreas Mecky andreas.mecky@xcom.de
 * @author Terry Dye terry.dye@xcom.de
 */
public class ConfigurationPropertiesHandler implements ConfigurationHandler {

    // separator for category and property for HashMap
    private static final char C_SEP   = 127; 
    // prefix for category names
		private static final char C_PFIX  =  64; 
    private HashMap initValues;
    private HashMap props;
    /** Creates a new instance of ConfigurationPropertiesLoader */
    public ConfigurationPropertiesHandler() {
        props = new HashMap();
    }
    
    /**
     * This method save the incoming parameters for
     * later use.
     *
     * @param parameters  a <code>HashMap</code> with all parameters
     * as key value pairs.
     */    
    public void initialize(HashMap parameters) {
        initValues = parameters;
    }
    
    /**
     * This is the <code>load</code> method that will be
     * called by the <code>ConfigurationManager</code>.
     *
     * @throws ConfigurationManagerException  if some error will occur
     */    
    public void load() throws ConfigurationManagerException {
        // only call loadPorperties when we have an entry in the initValues
        if ( initValues.containsKey("File") ) {
            loadProperties((File)initValues.get("File"));
        }
    }
    /**
     *  This method will store all categories and properties to a
     *  Java-properties file. All properties will be in the category "general".
     *  It will override an existing configuration.
     *
     *@param  file                            the file
     *@throws  ConfigurationManagerException  if the file cannot be processed
     */
    public void loadProperties(File file) throws ConfigurationManagerException {        
        try {
            FileInputStream input = new FileInputStream(file);
            Properties myProperties = new Properties();
            myProperties.load(input);
            Enumeration propertyNames = myProperties.propertyNames();            
            while (propertyNames.hasMoreElements()) {
                String name = (String) propertyNames.nextElement();
                String value = myProperties.getProperty(name);
                props.put(C_PFIX+"general"+C_SEP+name, value);                
            }
        } catch (Exception e) {
            throw new ConfigurationManagerException("The file cannot be loaded");
        }
    }

    /**
     * This method returns all properties.
     *
     * @return  a <code>HashMap</code> with all properties
     */    
    public HashMap getProperties() {        
        return props;
    }
    
    /**
     * This method returns all categories. In this particular
     * case it returns only one category "general".
     *
     * @return  a <code>Vector</code> with all categories as <code>String</code>
     */    
    public Vector getCategories() {
        Vector categories = new Vector();
        // there is only the general category
        categories.add("general"); 
        return categories;
    }
    
    /**
     * This method stores one or all categories as a properties file.
     * It is called by the <code>ConfigurationManager</code>. The
     * actual store method it determined by the type that is
     * passed as parameter. If the type is single then only the
     * specified category is saved.
     *
     * @throws ConfigurationManagerException if the file cannot be saved
     */    
    public void store() throws ConfigurationManagerException {
        String type = (String)initValues.get("Type");
        if ( type.equals("Single") ) {
            storeProperties((File)initValues.get("File"),(String)initValues.get("Category"),(Hashtable)initValues.get("Properties"));
        }
        else {
            storeProperties((File)initValues.get("File"),(HashMap)initValues.get("Data"));
        }
    }
    
    /**
     * This method will generate a Java-properties file. All categories will be
     * saved as comments and only the properties are written to the file. The
     * output is category.property=value
     *
     * @param data a <code>HashMap</code> with all categories and properties
     * @param file the file
     * @throws ConfigurationManagerException if the file cannot be processed 
     */
    public void storeProperties(File file,HashMap data) throws ConfigurationManagerException {
        // this will store the properties for a certain category
        Hashtable props = new Hashtable();
        String currentCategory = new String();
        Iterator it = data.keySet().iterator();
        try {
            // let's open the file
            FileWriter fw = new FileWriter(file);
            // and write the header
            fw.write("#\n");
            fw.write("# automatically generated properties file\n");
            fw.write("#\n");
            // now we walk through all categories
            while ( it.hasNext() ) {
                currentCategory = (String)it.next();
                // write the category as comment
                fw.write("#\n");
                fw.write("# category: " + currentCategory + "\n");
                fw.write("#\n");
                // now we get all properties for this category
                props = (Hashtable)data.get(currentCategory);
                Enumeration lprops = props.keys();
                // here we walk through the properties
                while (lprops.hasMoreElements()) {
                    String currentKey = (String) lprops.nextElement();
                    // and write it to the file
                    // form category.property=value
                    fw.write(currentCategory + "." + currentKey + "=" + (String) props.get(currentKey) + "\n");
                }
            }
            // close the file because we are done
            fw.close();
        } catch (Exception e) {
            throw new ConfigurationManagerException("The file cannot be saved");
        }
    }

    /**
     * This method will write a Java-properties file and fill in the properties
     * for the particular category. The category will be saved as comment and
     * only the properties are written to the file. The output is
     * property=value. The category is not included in the property name.
     *
     * @param file      the file 
     * @param category  the category that will be saved
     * @param props     a <code>Hashtable</code> with all properties for this category
     *
     * @throws ConfigurationManagerException if the file cannot be processed 
     */
    public void storeProperties(File file, String category,Hashtable props) throws ConfigurationManagerException {
        // this will store the properties for a certain category     
        try {
            // let's open the file
            FileWriter fw = new FileWriter(file);
            // and write the header
            fw.write("#\n");
            fw.write("# automatically generated properties file");
            fw.write("#\n");
            fw.write("#\n");
            fw.write("# category: " + category + "\n");
            fw.write("#\n");
            // now we get all properties for this category
            Enumeration myEnum = props.keys();     
            // here we walk through the properties
            while ( myEnum.hasMoreElements() ) {
                String currentKey = (String) myEnum.nextElement();
                // and write it to the file
                // form category.property=value
                fw.write(currentKey + "=" + (String) props.get(currentKey) + "\n");
            }
            // close the file because we are done
            fw.close();
        } catch (Exception e) {
            throw new ConfigurationManagerException("The file cannot be saved");
        }
    }    
}
