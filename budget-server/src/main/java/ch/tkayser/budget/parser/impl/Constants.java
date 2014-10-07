/*
 * Software is written by:
 *
 *   Thomas Kayser
 *   tomra@gmx.net
 *   Switzerland
 *
 * Copyright (c) 2009
 * 
 */
package ch.tkayser.budget.parser.impl;

public class Constants {

    // line separator for booking texts
    public static final String BOOKINGTEXT_LINE_SEPARATOR = " / ";

    // default delimiter
    public static final char COLUMM_DELIMITER = ';';

    // default format of date column
    public static final String DEFAULT_DATE_FORMAT = "dd.MM.yyyy";

    // amount format
    public static final String DEFAULT_AMOUNT_FORMAT = "###,###,###.##";

    // default characterset for encoded files
    public static final String DEFAULT_CHARSET = "ISO-8859-1";

    // total row title
    public static final String TOTAL_ROW_TITLE = "Totale";

    // system property for server file
	public static final String SERVER_FILE_SYSTEM_PROPERTY = "ch.tkayser.budget.serverconfigfile";

}
