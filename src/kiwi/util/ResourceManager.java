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
   $Log: ResourceManager.java,v $
   Revision 1.16  2003/01/19 09:42:04  markl
   Throw exception if null or empty string passed in for resource name.

   Revision 1.15  2002/08/11 09:57:21  markl
   Version number change.

   Revision 1.14  2002/08/11 09:48:54  markl
   Added setter methods for resource paths.

   Revision 1.13  2001/12/25 10:12:35  markl
   Javadoc typo fix.

   Revision 1.12  2001/08/28 20:28:21  markl
   Fixes to defer access to Toolkit for situations where no X Display is
   available.

   Revision 1.11  2001/06/26 06:12:23  markl
   Fixed javadoc.

   Revision 1.10  2001/06/26 06:11:36  markl
   Added getStream() method.

   Revision 1.9  2001/03/12 03:16:50  markl
   *** empty log message ***

   Revision 1.8  1999/05/10 08:54:11  markl
   Added color theme support.

   Revision 1.7  1999/05/03 09:34:17  markl
   Fixed javadoc comment.

   Revision 1.6  1999/04/19 05:32:41  markl
   Added LocaleData bundle support and bundle buffering.

   Revision 1.5  1999/01/18 08:12:38  markl
   fixed bugs in ResourceBundle methods

   Revision 1.4  1999/01/17 08:32:57  markl
   Added Locale awareness and new getResourceBundle() method.

   Revision 1.3  1999/01/17 00:58:33  markl
   Added ResourceBundle support

   Revision 1.2  1999/01/10 03:55:05  markl
   added GPL header & RCS tag
   ----------------------------------------------------------------------------
*/

package kiwi.util;

import java.awt.Image;
import java.io.*;
import java.util.*;
import java.net.URL;
import javax.swing.*;

import kiwi.ui.*;

/** This class provides base functionality for a resource manager; it includes
  * support for the caching of images and sounds, and provides convenience
  * methods for retrieving other types of resources. All resources are
  * retrieved relative to an <i>anchor class</i>. The resource manager assumes
  * that images will be within an "images" directory, textures within a
  * "textures" directory, sounds within a "sounds" directory, URL-based
  * references within an "html" directory, properties within a "properties"
  * directory, resource bundles within a "locale" directory, and color theme
  * definitions within a "themes" directory. All of these paths, however, are
  * configurable.
  * <p>
  * The Kiwi library includes a resource library of its own; the resources
  * within the library are accessible through the internal
  * <code>ResourceManager</code>, a reference to which may be obtained via
  * a call to <code>kiwi.util.KiwiUtils.getResourceManager()</code>. Links to
  * index files of some of the resources are listed below:
  * <p><ul>
  * <li><a href="../../images_index.html">images</a>
  * <li><a href="../../textures_index.html">textures</a>
  * <li><a href="../../locale_index.html">resource bundles</a>
  * <li><a href="../../sounds_index.html">sounds</a>
  * </ul>
  *
  * @see kiwi.util.ResourceLoader
  *
  * @author Mark Lindner
  */

public class ResourceManager
  {
  private Hashtable images, textures, sounds, icons, bundles;
  private ResourceLoader loader;
  private static ResourceManager kiwiResourceManager = null;
  private static final String respathSeparator = "/";
  /** The default base path for images. */
  public static final String IMAGE_PATH = "images";
  /** The default base path for audio clips. */
  public static final String SOUND_PATH = "sounds";
  /** The default base path for HTML documents. */
  public static final String HTML_PATH = "html";
  /** The default base path for color themes. */
  public static final String THEME_PATH = "themes";
  /** The default base path for textures. */
  public static final String TEXTURE_PATH = "textures";
  /** The default base path for property files. */
  public static final String PROPERTY_PATH = "properties";
  /** The default base path for message bundles. */
  public static final String RESBUNDLE_PATH = "locale";
  /** The file extension for resource bundles. */
  public static final String RESBUNDLE_EXT = ".msg";
  /** The file extension for color themes. */
  public static final String THEME_EXT = ".thm";

  /** The base path for images. */
  protected String imagePath;
  /** The base path for audio clips. */
  protected String soundPath;
  /** The base path for HTML documents. */
  protected String htmlPath;
  /** The base path for color themes. */
  protected String themePath;
  /** The base path for textures. */
  protected String texturePath;
  /** The base path for property files. */
  protected String propertyPath;
  /** The base path for message bundles. */
  protected String resbundlePath;
  
  /** Construct a new <code>ResourceManager</code>.
    *
    * @param clazz The resource anchor class.
    */

  public ResourceManager(Class clazz)
    {
    loader = new ResourceLoader(clazz);
    images = new Hashtable();
    icons = new Hashtable();
    textures = new Hashtable();
    sounds = new Hashtable();
    bundles = new Hashtable();

    setImagePath(IMAGE_PATH);
    setSoundPath(SOUND_PATH);
    setHTMLPath(HTML_PATH);
    setThemePath(THEME_PATH);
    setTexturePath(TEXTURE_PATH);
    setPropertyPath(PROPERTY_PATH);
    setResourceBundlePath(RESBUNDLE_PATH);
    }

  /*
   */

  private String _checkPath(String path)
    {
    if(path == null)
      return(respathSeparator);
    
    return(path.endsWith(respathSeparator) ? path
           : (path + respathSeparator));
    }
  
  /** Set an alternate path (relative to the anchor class) for image and icon
   * resources.
   *
   * @param path The new resource path.
   * @since Kiwi 1.3.4
   */

  public void setImagePath(String path)
    {
    imagePath = _checkPath(path);
    }

  /** Set an alternate path (relative to the anchor class) for audio clip
   * resources.
   *
   * @param path The new resource path.
   * @since Kiwi 1.3.4
   */

  public void setSoundPath(String path)
    {
    soundPath = _checkPath(path);
    }

  /** Set an alternate path (relative to the anchor class) for HTML document
   * resources.
   *
   * @param path The new resource path.
   * @since Kiwi 1.3.4
   */

  public void setHTMLPath(String path)
    {
    htmlPath = _checkPath(path);
    }

  /** Set an alternate path (relative to the anchor class) for theme
   * resources.
   *
   * @param path The new resource path.
   * @since Kiwi 1.3.4
   */

  public void setThemePath(String path)
    {
    themePath = _checkPath(path);
    }

  /** Set an alternate path (relative to the anchor class) for texture
   * resources.
   *
   * @param path The new resource path.
   * @since Kiwi 1.3.4
   */

  public void setTexturePath(String path)
    {
    texturePath = _checkPath(path);
    }

  /** Set an alternate path (relative to the anchor class) for property
   * resources.
   *
   * @param path The new resource path.
   * @since Kiwi 1.3.4
   */

  public void setPropertyPath(String path)
    {
    propertyPath = _checkPath(path);
    }

  /** Set an alternate path (relative to the anchor class) for message bundle
   * resources.
   *
   * @param path The new resource path.
   * @since Kiwi 1.3.4
   */

  public void setResourceBundlePath(String path)
    {
    resbundlePath = _checkPath(path);
    }
  
  /** Get a reference to the internal Kiwi resource manager.
    */

  public static ResourceManager getKiwiResourceManager()
    {
    if(kiwiResourceManager == null)
      kiwiResourceManager = new ResourceManager(kiwi.ResourceAnchor.class);

    return(kiwiResourceManager);
    }

  /** Clear the image resource cache. */
  
  public void clearImageCache()
    {
    images.clear();
    }

  /** Clear the icon resource cache. */
  
  public void clearIconCache()
    {
    icons.clear();
    }

  /** Clear the texture resource cache. */
  
  public void clearTextureCache()
    {
    textures.clear();
    }

  /** Clear the audio clip resource cache. */
  
  public void clearAudioClipCache()
    {
    sounds.clear();
    }
  
  /** Clear the resource bundle cache. */
  
  public void clearResourceBundleCache()
    {
    bundles.clear();
    }
  
  /** Retrieve an internal <code>Icon</code> resource. This is a convenience
    * method that makes a call to <code>getImage()</code> and then wraps the
    * result in a Swing <code>ImageIcon</code> object.
    *
    * @param name The name of the resource.
    * @return An <code>Icon</code> for the specified image. If an icon for this
    * image has previously been constructed, the cached copy is returned.
    *
    * @exception kiwi.util.ResourceNotFoundException If the resource was not
    * found.
    * @see javax.swing.ImageIcon
    */

  public Icon getIcon(String name)
    {
    Icon icon = (Icon)icons.get(name);
    if(icon != null) return(icon);

    icon = new ImageIcon(getImage(name));
    icons.put(name, icon);

    return(icon);
    }

  /** Retrieve an internal <code>Image</code> resource. If the named image has
    * previously been loaded, a cached copy is returned.
    *
    * @param name The name of the resource.
    * @return The <code>Image</code> object representing the resource.
    * @exception kiwi.util.ResourceNotFoundException If the resource was not
    * found.
    */
  
  public Image getImage(String name)
    {
    checkResourceName(name);
    
    Image image = (Image)images.get(name);
    if(image != null) return(image);

    String path = imagePath + name;
    image = loader.getResourceAsImage(path);
    if(image == null)
      throw(new ResourceNotFoundException(path));
    images.put(name, image);

    return(image);
    }
  
  /** Retrieve an internal texture resource. If the named texture has
    * previously been loaded, a cached copy is returned.
    *
    * @param name The name of the resource.
    * @return The <code>Image</code> object representing the resource.
    * @exception kiwi.util.ResourceNotFoundException If the resource was not
    * found.
    */

  public Image getTexture(String name)
    {
    checkResourceName(name);
    
    Image image = (Image)textures.get(name);
    if(image != null) return(image);

    String path = texturePath + name;
    image = loader.getResourceAsImage(path);
    if(image == null)
      throw(new ResourceNotFoundException(path));
    textures.put(name, image);

    return(image);
    }

  /** Retrieve an internal <code>URL</code> resource.
    *
    * @param name The name of the resource.
    * @return A URL for the resource.
    * @exception kiwi.util.ResourceNotFoundException If the resource was not
    * found.
    */

  public URL getURL(String name)
    {
    checkResourceName(name);
    
    String path = htmlPath + name;
    URL url = loader.getResourceAsURL(path);
    if(url == null)
      throw(new ResourceNotFoundException(path));
    
    return(url);
    }

  /** Retrieve an internal <code>AudioClip</code> resource. If the named sound
    * has previously been loaded, a cached copy is returned.
    *
    * @param name The name of the resource.
    * @return The <code>AudioClip</code> object representing the resource.
    * @exception kiwi.util.ResourceNotFoundException If the resource was not
    * found.
    */

  public AudioClip getSound(String name)
    {
    checkResourceName(name);
    
    String path = soundPath + name;
    AudioClip clip = loader.getResourceAsAudioClip(soundPath + name);
    if(clip == null)
      throw(new ResourceNotFoundException(path));
    
    return(clip);
    }

  /** Get a reference to a <code>Properties</code> resource.
    *
    * @param name The name of the resource.
    * @return The <code>Properties</code> object representing the resource.
    * @exception kiwi.util.ResourceNotFoundException If the resource was not
    * found.
    */

  public Properties getProperties(String name)
    {
    checkResourceName(name);
    
    Properties props = null;
    String path = propertyPath + name;
    
    try
      {
      props = loader.getResourceAsProperties(path);
      }
    catch(IOException ex) { }
    
    if(props == null)
      throw(new ResourceNotFoundException(path));

    return(props);
    }

  /** Get a reference to a <code>LocaleData</code> object for the default
   * locale. The locale naming convention
   * <i>basename_language_country_variant</i> is
   * supported; a search is performed starting with the most specific name
   * and ending with the most generic.
   *
   * @param name The name of the resource; this should be the base name of
   * the resource bundle; the appropriate locale country, language, and
   * variant codes and the ".msg" extension will be automatically
   * appended to the name.
   * @return The <code>LocaleData</code> object representing the resource.
   * If the resource bundle has been previously loaded, a cached copy is
   * returned.
   * @exception kiwi.util.ResourceNotFoundException If the resource was not
   * found.
   */

  public LocaleData getResourceBundle(String name)
    throws ResourceNotFoundException
    {    
    return(getResourceBundle(name, Locale.getDefault()));
    }
  
  /** Get a reference to a <code>LocaleData</code> object for a specified
   * locale. The locale naming convention
   * <i>basename_language_country_variant</i> is
   * supported; a search is performed starting with the most specific name
   * and ending with the most generic. If the resource bundle has been
   * previously loaded, a cached copy is returned.
   *
   * @param name The name of the resource; this should be the base name of
   * the resource bundle; the appropriate locale contry, language, and
   * variant codes and the ".msg" extension will be automatically
   * appended to the name.
   * @param locale The locale for the resource.
   * @return The <code>LocaleData</code> object representing the resource.
   * @exception kiwi.util.ResourceNotFoundException If the resource was not
   * found.
   */

  public LocaleData getResourceBundle(String name, Locale locale)
    throws ResourceNotFoundException
    {
    checkResourceName(name);
    
    String path = resbundlePath + name, cpath = null;
    LocaleData bundle = null;

    Stack paths = new Stack();
    paths.push(path + RESBUNDLE_EXT);
    path += "_" + locale.getLanguage();
    paths.push(path + RESBUNDLE_EXT);
    String country = locale.getCountry();
    if((country != null) && (country.length() > 0))
      {
      path += "_" + country;
      paths.push(path + RESBUNDLE_EXT);
      String variant = locale.getVariant();
      if((variant != null) && (variant.length() > 0))
        {
        path += "_" + variant;
        paths.push(variant + RESBUNDLE_EXT);
        }
      }

    while(!paths.empty())
      {
      cpath = (String)paths.pop();

      bundle = (LocaleData)bundles.get(cpath);
      if(bundle == null)
        {
        try
          {
          InputStream is = loader.getResourceAsStream(cpath);
          bundle = new LocaleData(is);
          bundles.put(cpath, bundle);
          }
        catch(IOException ex)
          {
          bundle = null;
          }
        }

      if(bundle != null) break;
      }
      
    if(bundle == null)
      throw(new ResourceNotFoundException(cpath));

    return(bundle);
    }

  /** Get a reference to a <code>ColorTheme</code> resource.
    *
    * @param name The name of the resource; this should be the base name of
    * the color theme; the ".thm" extension will be automatically
    * appended to the name.
    * @return The <code>ColorTheme</code> object representing the resource.
    * @exception kiwi.util.ResourceNotFoundException If the resource was not
    * found.
    */

  public ColorTheme getTheme(String name)
    {
    checkResourceName(name);
    
    String path = themePath + name + THEME_EXT;
    ColorTheme theme = null;
    
    try
      {
      InputStream is = loader.getResourceAsStream(path);
      Config cfg = new Config();
      cfg.load(is);
      theme = new ColorTheme(cfg);
      }
    catch(IOException ex)
      {
      throw(new ResourceNotFoundException(path));
      }

    return(theme);
    }

  /** Get a stream to a resource.
   *
   * @param name The name of the resource.
   * @return An <code>InputStream</code> from which the resource can be read.
   * @exception kiwi.util.ResourceNotFoundException If the resource was not
   * found.
   * @since Kiwi 1.3
   */

  public InputStream getStream(String name) throws ResourceNotFoundException
    {
    checkResourceName(name);
    
    InputStream is;

    try
      {
      is = loader.getResourceAsStream(name);
      }
    catch(IOException ex)
      {
      throw(new ResourceNotFoundException(name));
      }
    
    return(is);
    }

  /*
   */

  private void checkResourceName(String name) throws IllegalArgumentException
    {
    if((name == null) || name.equals(""))
      throw(new IllegalArgumentException("Null or empty resource name"));
    }
  
  }

/* end of source file */
