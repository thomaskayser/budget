/*
 * $HeadURL$
 * Copyright(c) ISC-EJPD - Alle Rechte vorbehalten
 *
 * Letzter Commit
 *   $LastChangedDate: 2011-06-15 16:05:50 +0200 (Mi, 15 Jun 2011) $
 *   $Author: tom $
 *   $Revision: 2145 $ 
 */
package ch.tkayser.budget.swing.prefs;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jdesktop.application.LocalStorage;

import ch.tkayser.budget.swing.MainApplication;

/**
 * Preferences for the gui
 */
public class GUIPreferences {

    //@formatter:off
    
    // singleton instance
    private static GUIPreferences instance;    
    
    // file name
    private static final String PREF_FILE_NAME     = "Preferences.xml";
    
    // map with the preferences
    private Map<String, String> preferences = new HashMap<String, String>();

    // property names
    private static final String PROP_LOOK_AND_FEEL = "lookAndFeelName";
    

    //@formatter:on

    private GUIPreferences() {
    }

    /**
     * gibt die singleton instanz der Preferences zurueck
     * 
     * 
     * @return
     * @throws IOException
     */
    public static GUIPreferences getInstance() throws IOException {
        if (instance == null) {
            // prefs laden und Instanz erstellen
            instance = new GUIPreferences();
            instance.load();
        }
        return instance;
    }

    /**
     * get the look and feel TODO: Bitte durch Methoden Beschreibung ersetzen
     * 
     * @return
     */
    public String getLookAndFeelName() {
        return getProperty(PROP_LOOK_AND_FEEL);
    }

    /**
     * set the look and feel
     * 
     * @param lookAndFeelName
     */
    public void setLookAndFeelName(String lookAndFeelName) {
        setProperty(PROP_LOOK_AND_FEEL, lookAndFeelName);
    }

    /**
     * liefert ein Property
     */
    private String getProperty(String propertyName) {
        return preferences.get(propertyName);
    }

    /**
     * set a property
     * 
     * @param propertyName
     * @param value
     */
    private void setProperty(String propertyName, String value) {
        preferences.put(propertyName, value);
    }

    /**
     * save the preferences
     * 
     * @throws IOException
     * 
     */
    public void save() throws IOException {
        getStorage().save(preferences, PREF_FILE_NAME);
    }

    /**
     * load the preferences
     * 
     * @throws IOException
     * 
     */
    @SuppressWarnings("unchecked")
    private void load() throws IOException {
        preferences = (Map<String, String>)getStorage().load(PREF_FILE_NAME);
    }

    /**
     * gibt den local storage zum speichern zurueck
     * 
     * @return
     */
    private LocalStorage getStorage() {
        return MainApplication.getMainApplication().getContext().getLocalStorage();
    }

}
