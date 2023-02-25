package org.jconfig;

import java.io.*;
import java.util.*;

import org.jconfig.event.*;
/**
 *  The ConfigurationManager is the do-it-all-in-one utility. The
 *  ConfigurationManager allows you to store properties inside an XML file or a
 *  properties file. The properties can be retrieved later for use in your
 *  applications. <P/>
 *
 *  Typical usage: <P/>
 *
 *  <CODE>
 * // <BR />
 *  // first we get an existing property from the category &quot;general&quot;
 *  <BR />
 *  //<BR />
 *  String debug_file = cfgmgr.getProperty(&quot;debug_file&quot;);<BR />
 *  System.out.println(&quot;The debug_file is:&quot; + debug_file);<BR />
 *  //<BR />
 *  // now we try to get a non-existing property from the category
 *  &quot;general&quot;<BR />
 *  //<BR />
 *  String neProp = cfgmgr.getProperty(&quot;hello&quot;,&quot;my
 *  default&quot;);<BR />
 *  System.out.println(&quot;The value is:&quot; + neProp );<BR />
 *  //<BR />
 *  // last step is we get a property from the category &quot;JDBC&quot;<BR />
 *  //<BR />
 *  String driver = cfgmgr.getProperty(&quot;DRIVER&quot;,&quot;&quot;,&quot;JDBC&quot;);
 *  <BR />
 *  System.out.println(&quot;The driver is:&quot; + driver);<BR />
 *  </CODE>
 *
 *@author     Andreas Mecky andreas.mecky@xcom.de
 *@author     Terry Dye terry.dye@xcom.de
 *@created    27. Januar 2002
 *@version    $Id: ConfigurationManager.java,v 1.16 2002/08/06 15:08:59
 *      Andreas.Mecky Exp $
 */
public class ConfigurationManager implements FileListener {

    // separator for category and property for HashMap
    private final static char C_SEP = 127;
    // prefix for category names
    private final static char C_PFIX = 64;
    // all instances of the ConfigurationManager
    private static HashMap configurationManagers = null;
    // the parameters for one instance
    private HashMap parameter;
    // all categories for one instance
    private Vector categories;
    // flag if we should validate an XML input
    private boolean validate = false;
    // the name of the instance
    private String name;
    // file watcher
    private FileWatcher watcher;
    // the config file
    private File configFile;


    /**
     *  Constructor for the ConfigurationManager object
     *
     *@param  name  Description of the Parameter
     */
    private ConfigurationManager(String name) {
        parameter = new HashMap();
        categories = new Vector();
        addCategory("general");
        this.name = name;
    }


    /**
     *  This method returns the value for a particular key. The category is
     *  "general".
     *
     *@param  key  the name of the property
     *@return      the value as String or null
     */
    public String getProperty(String key) {
        return getProperty(key, null, null);
    }


    /**
     *  This method returns the value for a particular key. If this key does not
     *  exist then the method returns the defaultValue. The category is
     *  "general".
     *
     *@param  key           the name of the property
     *@param  defaultValue  the value that will be returned if the property
     *      cannot be found
     *@return               the value as String or the defaultValue
     */
    public String getProperty(String key, String defaultValue) {
        return getProperty(key, defaultValue, null);
    }


    /**
     *  This method returns the value for a particular key in the certain
     *  category. If this key does not exist then the method returns the
     *  defaultValue
     *
     *@param  key           the name of the property
     *@param  defaultValue  the value that will be returned if the property
     *      cannot be found
     *@param  category      the name of the category
     *@return               the value as String or the defaultValue
     */
    public String getProperty(String key, String defaultValue, String category) {
        if (key == null) {
            return defaultValue;
        }
        String mykey;        
        String generalKey = C_PFIX + "general" + C_SEP + key;
        if (category != null) {            
            mykey = C_PFIX + category + C_SEP + key;
        } else {
            mykey = generalKey;
        }
        if (parameter.containsKey((String) mykey)) {
            return (String) parameter.get((String) mykey);
        } else if (parameter.containsKey((String) generalKey)) {
            return (String) parameter.get((String) generalKey);
        } else {
            return defaultValue;
        }
    }


    /**
     *  This method returns the default instance of the ConfigurationManager. If
     *  no instance is found it will create a new instance and will try to load
     *  the config.xml. This file must be in the classpath.
     *
     *@return
     */
    public static ConfigurationManager getInstance() {
        return getInstance("default", true);
    }


    /**
     *  This method returns an instance of the ConfigurationManager with the
     *  given name<BR />
     *  If no instance is found it will create a new instance and will try to
     *  load the config.xml. This file must be in the classpath.
     *
     *@param  instanceName
     *@return
     */
    public static ConfigurationManager getInstance(String instanceName) {
        return getInstance(instanceName, true);
    }


    /**
     *  This method returns an instance of the ConfigurationManager with this
     *  name. If there is no instance then it will create a new one. If the
     *  autoLoad flag is set to true then it will try to load the config.xml. In
     *  order to use the ConfigurationManager do the following: <BR />
     *  private ConfigurationManager<BR />
     *  cfgmgr = ConfigurationManager.getInstance("myConfiguration",true);
     *
     *@param  instanceName
     *@param  autoLoad      if true then the method wil try to load the
     *      config.xml if it can find it in the classpath
     *@return               an instance of the ConfigurationManager
     */
    public static ConfigurationManager getInstance(String instanceName, boolean autoLoad) {
        if (configurationManagers == null) {
            configurationManagers = new HashMap();
        }
        if (configurationManagers.containsKey(instanceName)) {
            return (ConfigurationManager) configurationManagers.get(instanceName);
        }
        ConfigurationManager cfgmgr = new ConfigurationManager(instanceName);
        InputStream is = null;
        try {
            /*
             *  get a default config.xml, means no need to call method
             *  load ( filename )
             */
            if (autoLoad) {
                ClassLoader cl = cfgmgr.getClass().getClassLoader();
                is = cl.getResourceAsStream("config.xml");
                if (is != null) {
                    cfgmgr.load(is);
                    cfgmgr.setAutoReload(true);
                }
                /*
                 *  we now try register the watchers
                 */
                java.net.URL url = cl.getResource("config.xml");
                if (url != null) {
                    File file = new File(url.getFile());
                    cfgmgr.configFile = file;
                    cfgmgr.setWatcher(new FileWatcher(file));
                    cfgmgr.watcher = cfgmgr.getWatcher();
                    cfgmgr.watcher.addFileListener(cfgmgr);
                    cfgmgr.watcher.start();
                }
            }
            configurationManagers.put(instanceName, cfgmgr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return cfgmgr;
    }


    /**
     *  This method returns an Enumeration of all categories. The names are
     *  stored as Strings inside the Enumeration.
     *
     *@return        an enumeratiuon containing all names of all categories
     *@deprecated    use getAllCategoryNames instead
     */
    public Enumeration getCategoryNames() {
        return categories.elements();
    }


    /**
     *  This method returns an <code>String</code> array of the names of all
     *  categories.
     *
     *@return    an <code>String</code> containing all names of all categories
     */
    public String[] getAllCategoryNames() {
        return (String[]) categories.toArray(new String[0]);
    }


    /**
     *  This method will return a Hashtable that contains all properties for the
     *  category "general". The properties are stored as key and value both as
     *  strings.
     *
     *@return    a Hashtable that contains all properties for the category
     *      "general"
     */
    public Hashtable getProperties() {
        return getProperties(null);
    }


    /**
     *  This method will return a Hashtable that contains all properties for the
     *  selected category. The properties are stored as key and value both as
     *  strings.
     *
     *@param  category  the name of the category
     *@return           a Hashtable that contains all properties for this
     *      category
     */
    public Hashtable getProperties(String category) {
        Hashtable retValues = new Hashtable();
        // if no category was passed to us then we take "general"
        if (category == null) {
            category = C_PFIX + "general";
        } else {
            category = C_PFIX + category;
        }
        String currentKey = null;
        // get all the keys from our Hashtable
        Iterator it = parameter.keySet().iterator();
        while (it.hasNext()) {
            currentKey = (String) it.next();
            // let's see if the category name is inside the key-string            
            if (currentKey.indexOf(category + C_SEP) != -1) {
                // now extract the category name from the key and
                // store the name and the value                
                String name = currentKey.substring(currentKey.indexOf(C_SEP) + 1);
                String value = (String) parameter.get((String) currentKey);
                retValues.put(name, value);
            }
        }
        return retValues;
    }


    /**
     *  This method will set the value for a specific property in the category
     *  "general". If this property does not exist then it will be created
     *
     *@param  name   the name of the property
     *@param  value  the value for this property
     */
    public void setProperty(String name, String value) {
        setProperty(name, value, null);
    }


    /**
     *  This method will set the value for a specific property in the selected
     *  category. If this property does not exist then it will be created. If
     *  the category does not exist it will be created.
     *
     *@param  name      the name of the property
     *@param  value     the value for this property
     *@param  category  the name of the category
     */
    public void setProperty(String name, String value, String category) {
        // we use the default one if it is not specified
        String tmpCategory = null;
        if (category == null) {
            tmpCategory = C_PFIX + "general";
        } else {
            tmpCategory = C_PFIX + category;
        }
        // it is safe to use addCategory since it will check
        // if this category already exists and return quitely
        addCategory(category);
        // now we save the property
        String mykey = tmpCategory + C_SEP + name;
        parameter.put(mykey, value);
    }


    /**
     *  This method removes the property with the given name from the category
     *  general. It calls <code>removeProperty(name,"general")</code>.
     *
     *@param  name  the name of the property that will be removed
     */
    public void removeProperty(String name) {
        if (name != null) {
            removeProperty(name, "general");
        }
    }


    /**
     *  This method will remove a property with the given name from the specific
     *  category.
     *
     *@param  name      the name of the property
     *@param  category  the name of the category
     */
    public void removeProperty(String name, String category) {
        if (name != null && category != null) {            
            String mykey = C_PFIX + category + C_SEP + name; 
            if( parameter.get(mykey) != null ) {
                parameter.remove(mykey);
            }
        }
    }


    /**
     *  Adds a feature to the Category attribute of the ConfigurationManager
     *  object
     *
     *@param  category  The feature to be added to the Category attribute
     */
    public void addCategory(String category) {
        if (categories.indexOf(category) == -1) {
            categories.add(category);
        }
    }


    /**
     *  This method removes the specific category.
     *
     *@param  category  the name of the category that will be removed
     */
    public void removeCategory(String category) {
        String tmpCategory = C_PFIX + category;
        if (categories.indexOf(tmpCategory) != -1) {
            String[] propertyNames = getAllPropertyNamesFromCategory(category);
            for (int i = 0; i < propertyNames.length; i++) {
                removeProperty(propertyNames[i], category);
            }
            categories.remove(tmpCategory);
        }
    }

    /**
     *  Gets all property names for a given category. If the category
     * is <code>null</code> then the category is "general".     
     *
     *@param  category  the name of the category
     *@return           a string array with the names
     */
    public String[] getAllPropertyNamesFromCategory(String category) {
        Hashtable allProps = getProperties(category);
        Set myKeys = allProps.keySet();
        return (String[]) myKeys.toArray(new String[0]);
    }


    /**
     *  This method will save the current categories and their properties to a
     *  file
     *
     *@param  file                               the file that will be generated
     *@exception  ConfigurationManagerException  Description of the Exception
     */
    public void store(File file) throws ConfigurationManagerException {
        ConfigurationFileHandler cph = new ConfigurationFileHandler();
        HashMap props = new HashMap();
        for (int i = 0; i < categories.size(); i++) {
            String currentCategory = (String) categories.get(i);
            Hashtable myProperties = getProperties(currentCategory);
            props.put(currentCategory, myProperties);
        }
        HashMap params = new HashMap();
        params.put("File", file);
        params.put("Data", props);
        cph.initialize(params);
        cph.store();
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
        ConfigurationPropertiesHandler cph = new ConfigurationPropertiesHandler();
        HashMap params = new HashMap();
        params.put("File", file);
        cph.initialize(params);
        cph.load();
        categories = cph.getCategories();
        parameter = cph.getProperties();
    }


    /**
     *  This method will generate a Java-properties file. All categories will be
     *  saved as comments and only the properties are written to the file. The
     *  output is category.property=value
     *
     *@param  file                            the file
     *@throws  ConfigurationManagerException  if the file cannot be processed
     */
    public void storeProperties(File file) throws ConfigurationManagerException {
        ConfigurationPropertiesHandler cph = new ConfigurationPropertiesHandler();
        HashMap props = new HashMap();
        for (int i = 0; i < categories.size(); i++) {
            String currentCategory = (String) categories.get(i);
            Hashtable myProperties = getProperties(currentCategory);
            props.put(currentCategory, myProperties);
        }
        HashMap params = new HashMap();
        params.put("File", file);
        params.put("Data", props);
        // flag to show that only a single category will be saved
        params.put("Type", "Multi");
        cph.initialize(params);
        cph.store();
    }


    /**
     *  This method will write a Java-properties file and fill in the properties
     *  for the particular category. The category will be saved as comment and
     *  only the properties are written to the file. The output is
     *  property=value. The category is not included in the property name.
     *
     *@param  file                            the file
     *@param  category                        Description of the Parameter
     *@throws  ConfigurationManagerException  if the file cannot be processed
     */
    public void storeProperties(File file, String category) throws ConfigurationManagerException {
        ConfigurationPropertiesHandler cph = new ConfigurationPropertiesHandler();
        HashMap params = new HashMap();
        params.put("File", file);
        params.put("Properties", getProperties(category));
        params.put("Category", category);
        // flag to show that only a single category will be saved
        params.put("Type", "Single");
        cph.initialize(params);
        cph.store();
    }


    /**
     *  This method reads the configuration from an URL. It must be a XML file.
     *
     *@param  theURL                          the URL
     *@throws  ConfigurationManagerException  if an error occurs
     */
    public synchronized void load(String theURL) throws ConfigurationManagerException {
        HashMap params = new HashMap();
        params.put("URL", theURL);
        load(params);
    }


    /**
     *  This method will read an <code>InputStream</code>.<BR />
     *
     *
     *@param  is                              the <code>InputStream</code>
     *@throws  ConfigurationManagerException  if an error occurs
     */
    public synchronized void load(InputStream is) throws ConfigurationManagerException {
        HashMap params = new HashMap();
        params.put("InputStream", is);
        load(params);
    }


    /**
     *  This method reads a XML file.
     *
     *@param  file                            the file that will be read
     *@throws  ConfigurationManagerException  if the file cannot be read
     */
    public synchronized void load(File file) throws ConfigurationManagerException {
        HashMap params = new HashMap();
        params.put("File", file);

        configFile = file;
        load(params);
    }


    /**
     *  Description of the Method
     *
     *@param  params                             Description of the Parameter
     *@exception  ConfigurationManagerException  Description of the Exception
     */
    private void load(HashMap params) throws ConfigurationManagerException {
        ConfigurationFileHandler cfh = new ConfigurationFileHandler();
        cfh.initialize(params);
        cfh.load();
        cfh.setValidation(validate);
        categories = cfh.getCategories();
        parameter = cfh.getProperties();
        reloadWatcher();
    }


    /**
     *  Description of the Method
     */
    private void reloadWatcher() {
        // how do I get the File information that was loaded?
    }


    /**
     *  This method creates a string representation of this configuration. It
     *  can be used for debugging.
     *
     *@return    the configuration as <code>String</code>
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Configuration\n");
        sb.append("Name:" + name + "\n");
        for (int i = 0; i < categories.size(); i++) {
            String currentCategory = (String) categories.get(i);
            sb.append("  Category:" + currentCategory + "\n");
            Hashtable myProperties = getProperties(currentCategory);
            Enumeration lprops = myProperties.keys();
            // here we walk through the properties
            while (lprops.hasMoreElements()) {
                String currentKey = (String) lprops.nextElement();
                sb.append("    " + currentKey + "=" + (String) myProperties.get(currentKey) + "\n");
            }
        }
        return sb.toString();
    }


    /**
     *  This method, once implemented, will be called when the File object
     *  itself changes.
     *
     *@param  evt  Description of the Parameter
     */
    public void fileChanged(FileListenerEvent evt) {
        try {
            load(evt.getFile());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     *  Sets the validation attribute of the ConfigurationManager object
     *
     *@param  validate  The new validation value
     */
    public void setValidation(boolean validate) {
        this.validate = validate;
    }


    /**
     *  Sets the reloadIntervall attribute of the ConfigurationManager object
     *
     *@param  seconds  The new reloadIntervall value
     */
    public void setReloadIntervall(int seconds) {
        if (getWatcher() != null) {
            getWatcher().setInterval(seconds);
        }
    }


    /**
     * Sets the reload flag. If it is set to true then jConfig will watch the
     * correspondig resource for this configuration and reload it if it has
     * changed.
     *
     *@param  reload  The new autoReload value
     */
    public void setAutoReload(boolean reload) {
        if (reload) {
            if (getWatcher() != null) {
                getWatcher().start();
            } else {
                if (configFile != null) {
                    setWatcher(new FileWatcher(configFile));
                    getWatcher().addFileListener(this);
                    getWatcher().start();
                }
            }
        } else {
            if (getWatcher() != null) {
                getWatcher().stopWatching();
            }
        }
    }


    /**
     *  Getter for property FileWatcher
     *
     *@return    The FileWatcher
     */
    public FileWatcher getWatcher() {
        return watcher;
    }


    /**
     *  Setter for FileWatcher .
     *
     *@param  watcher  New value of property watcher.
     */
    public void setWatcher(org.jconfig.FileWatcher watcher) {
        this.watcher = watcher;
    }


    /**
     *  Getter for property Configuration Name.
     *
     *@return    Configuration Name.
     */
    public java.lang.String getConfigurationName() {
        return name;
    }


    /**
     *  Adds a FileListener to the current configuration
     *
     *@param  fileListener  The FileListener
     */
    public void addFileListener(FileListener fileListener) {
        getWatcher().addFileListener(fileListener);
    }
    
    /**
     * Getter for the File Object of the configuration file used.
     *
     * @return The File object
     */
    public File getConfigFile() {
        return configFile;
    }
}
