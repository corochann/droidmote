package cz.mpelant.droidmotepc;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * To support UTF-8 resources.
 */
public class Utf8ResourceBundle {

	/**
	 * Gets the bundle.
	 *
	 * @param baseName the base name
	 * @return the bundle
	 */
	public static final ResourceBundle getBundle(String baseName) {
	  ResourceBundle bundle = ResourceBundle.getBundle(baseName);
	  return createUtf8PropertyResourceBundle(bundle);
	}

	/**
	 * Gets the bundle.
	 *
	 * @param baseName the base name
	 * @param locale the locale
	 * @return the bundle
	 */
	public static final ResourceBundle getBundle(String baseName, Locale locale) {
	  ResourceBundle bundle = ResourceBundle.getBundle(baseName, locale); 
	  return createUtf8PropertyResourceBundle(bundle);
	}

	/**
	 * Gets the bundle.
	 *
	 * @param baseName the base name
	 * @param locale the locale
	 * @param loader the loader
	 * @return the bundle
	 */
	public static ResourceBundle getBundle(String baseName, Locale locale, ClassLoader loader) {
	  ResourceBundle bundle = ResourceBundle.getBundle(baseName, locale); 
	  return createUtf8PropertyResourceBundle(bundle);
	}

	/**
	 * Creates the utf8 property resource bundle.
	 *
	 * @param bundle the bundle
	 * @return the resource bundle
	 */
	private static ResourceBundle createUtf8PropertyResourceBundle(ResourceBundle bundle) {
	  if (!(bundle instanceof PropertyResourceBundle)) return bundle;

	  return new Utf8PropertyResourceBundle((PropertyResourceBundle)bundle);
	}

	/**
	 * The Class Utf8PropertyResourceBundle.
	 */
	private static class Utf8PropertyResourceBundle extends ResourceBundle {
	  
  	/** The bundle. */
  	PropertyResourceBundle bundle;

	  /**
  	 * Instantiates a new utf8 property resource bundle.
  	 *
  	 * @param bundle the bundle
  	 */
  	private Utf8PropertyResourceBundle(PropertyResourceBundle bundle) {
	    this.bundle = bundle;
	  }
	  /* (non-Javadoc)
	   * @see java.util.ResourceBundle#getKeys()
	   */
	  @SuppressWarnings("unchecked")
	public Enumeration getKeys() {
	    return bundle.getKeys();
	  }
	  /* (non-Javadoc)
	   * @see java.util.ResourceBundle#handleGetObject(java.lang.String)
	   */
	  protected Object handleGetObject(String key) {
	    String value = (String)bundle.handleGetObject(key);
	    try {
	      return new String (value.getBytes("ISO-8859-1"),"UTF-8") ;
	    } catch (UnsupportedEncodingException e) {
	      // Shouldn't fail - but should we still add logging message?
	      return null;
	    } 
	  }

	}
	}