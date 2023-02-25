/*
 * ConfigurationLoader.java
 *
 * Created on 28. April 2002, 14:41
 */

package org.jconfig;

import java.util.HashMap;
import java.util.Vector;
/**
 * This interface defines all methods that must be implemented 
 * by all Handler classes.
 *
 * @author  Andreas Mecky andreas.mecky@xcom.de
 * @author Terry Dye terry.dye@xcom.de
 */
public interface ConfigurationHandler {

    /**
     * This method is used to send any parameters from
     * the ConfigurationManager to the particular handler.
     *
     * @param parameters a <code>HashMap</code> that contains all
     * parmaters as a key value pair.
     */
    public void initialize(HashMap parameters);
    
    /**
     * This method should read the configuration and then
     * set the categories and properties.
     * @throws ConfigurationManagerException  */
    public void load() throws ConfigurationManagerException;
    
    /**
     * This method should return a <code>Vector</code> of all
     * categories. The category names must be stored as <code>Strings</code>
     * inside the <code>Vector</code>.
     *
     * @return all categories inside a <code>Vector</code>
     */
    public Vector getCategories();
    
    /**
     * This method should return all properties that was read
     * by the particular handler.<BR />
     * The key for the properties are build like:<BR />
     * category/property name.
     *
     * @return a <code>HashMap</code> with all properties.
     */
    public HashMap getProperties();
    
    /**
     * This method should store all categories and properties.
     * @throws ConfigurationManagerException  */
    public void store() throws ConfigurationManagerException;
}

