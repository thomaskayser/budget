/*
 * $HeadURL$
 * Copyright(c) ISC-EJPD - Alle Rechte vorbehalten
 *
 * Letzter Commit
 *   $LastChangedDate: 2011-04-12 11:51:47 +0200 (Di, 12 Apr 2011) $
 *   $Author: tom $
 *   $Revision: 2091 $ 
 */
package ch.tkayser.budget.swing.formating;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import ch.tkayser.budget.swing.Constants;

/**
 * Validation Messages for the usage outside of Hibernate validation
 */
public class ValidationMessages {
    
    // Attribute prefix
    private static final String ATTRIBUTE_PREFIX = "attr.";
    
    // msg prefix fuer format validierungen
    private static String MSG_INPUT_INVALID = "input.invalid";

    // bundle for validation messages
    private static ResourceBundle validationMessages = null;
    static {
        validationMessages = ResourceBundle.getBundle("ValidationMessages");
    }
    
    private static ResourceBundle applicationMessages = null;
    static {
        applicationMessages = ResourceBundle.getBundle(Constants.MAIN_RESOURCES);
    }
    
    /**
     * gibt eine Fehlermeldung zurueck fuer ein attribut
     *
     * @param attributeClass
     * @param attributeName
     * @return
     */
    public static String getMessage(Class<?> attributeClass, String attributeName) {
        // build error msgkey: base msg und classe
        String msgKey =  MSG_INPUT_INVALID;
        if (attributeClass != null) {
            msgKey += "."+attributeClass.getSimpleName().toLowerCase();
        }
        // text ubersetzen
        return getMessage(msgKey, attributeName);
    }
    
    
    /**
     * liefert eine Message ergäntz mit dem Attribut Namen zurueck
     *
     * @param msgKey
     * @param attributeName
     * @return
     */
    public static String getMessage(String msgKey, String attributeName) {
        // name des attributes holen
        String translatedAttributeName = getKey(applicationMessages, ATTRIBUTE_PREFIX+attributeName);        
        return  MessageFormat.format(getKey(validationMessages, msgKey), translatedAttributeName);
    }
    
    /**
     * liest den key aus einem Bundle. gibt den key zurüeck wenn er nicht gefunden wird
     *
     * @param bundle
     * @param key
     * @return
     */
    private static String getKey(ResourceBundle bundle, String key) {
        String result = "";
        try {
            result = bundle.getString(key);
        } catch (Exception e) {
            // doo nothing just return the key
            result = key;
        }
        return result;
    }
}
