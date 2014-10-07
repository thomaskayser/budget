/*
 * $HeadURL$
 * Copyright(c) ISC-EJPD - Alle Rechte vorbehalten
 *
 * Letzter Commit
 *   $LastChangedDate: 2011-04-12 13:06:31 +0200 (Di, 12 Apr 2011) $
 *   $Author: tom $
 *   $Revision: 2093 $ 
 */
package ch.tkayser.budget.swing.formating;

import java.text.Format;
import java.text.ParseException;

import javax.swing.JFormattedTextField.AbstractFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Formatter fuer JTextFields
 */
public class BudgetFormater extends AbstractFormatter {

    private static final long serialVersionUID = 1L;

    // the format to user
    private Format            format;

    // a logger
    private static Logger     logger           = LoggerFactory.getLogger(BudgetFormater.class);

    /**
     * create a formater
     * 
     * @param valueClass
     *            the class to format from and to
     * @param format
     *            the display format
     */
    public BudgetFormater(Format format) {
        this.format = format;
    }

    /**
     * Ueberschreibt Methode von Superklasse {@see
     * javax.swing.JFormattedTextField.AbstractFormatter#stringToValue(java.lang
     * .String)}
     * 
     * @param text
     * @return
     * @throws ParseException
     */
    @Override
    public Object stringToValue(String text) throws ParseException {
        try {
            logger.debug("parsing " + text);
            if (text.equals("")) {
                logger.debug("returning null ");
                return null;
            } else {
                Object parsed = format.parseObject(text);
                logger.debug("returning "+parsed);
                return parsed;
            }
        } catch (Exception e) {
            throw new ParseException("Fehler beim parsen von " + text, 0);
        }
    }

    /**
     * Ueberschreibt Methode von Superklasse {@see
     * javax.swing.JFormattedTextField.AbstractFormatter#valueToString(java.lang
     * .Object)}
     * 
     * @param value
     * @return
     * @throws ParseException
     */
    @Override
    public String valueToString(Object value) throws ParseException {
        logger.debug("formating " + value);
        if (value != null) {
            return format.format(value);
        } else {
            return "";
        }
    }

}
