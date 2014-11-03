package de.jfxstructo;

import java.util.Locale;
import java.util.ResourceBundle;

import de.jfxstructo.config.Configuration;
import de.jfxstructo.config.Language;

public class Globals {

	private static ResourceBundle rb;
	
	static {
//		try {
//			rb = ResourceBundle.getBundle("lang", Locale.getDefault());
//		} catch (MissingResourceException ex) {
//			rb = ResourceBundle.getBundle("lang", new Locale("en", "EN"));				
//		}
		
		Locale locale = ((Language) Configuration.getConfig("language")).getLocale();
		rb = ResourceBundle.getBundle("lang", locale);
	}
	
	public static ResourceBundle getResourceBundle() {
		return rb;
	}
	
	public static void setResourceBundle(Locale locale) {
		rb = ResourceBundle.getBundle("lang", locale);
	}
	
}
