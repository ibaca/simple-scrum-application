package org.inftel.ssa.mail;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 *
 * @author jsbaes
 */
public class MailProperties {

	private static ResourceBundle propertiesByDefault = null;

	/**
	 * Lectura de propiedades por defecto de la aplicación
	 *  
	 */
	static {
		try {
                    // Este bloque se ejecuta cuando se accede por primera vez a la clase
			propertiesByDefault = ResourceBundle
					.getBundle("org.inftel.ssa.mail.mail");
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	public static String getValor(String property) {
		String valor = null;
		try {
			valor = MailProperties.propertiesByDefault.getString(property);
		} catch (MissingResourceException mre) {
			mre.printStackTrace();
		} finally {
			return valor;
		}
	}

}

