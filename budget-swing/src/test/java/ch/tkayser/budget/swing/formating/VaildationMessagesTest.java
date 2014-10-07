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

import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;

import ch.tkayser.budget.swing.test.SwingBaseTest;

/**
 * Tests fuer die ValidationMessages Klasse
 */
public class VaildationMessagesTest extends SwingBaseTest  {

    @Test
    public void testGetMessage() {
        logger.debug(ValidationMessages.getMessage(BigDecimal.class, "amount"));
        logger.debug(ValidationMessages.getMessage(Date.class, "valuta"));
    }
}
