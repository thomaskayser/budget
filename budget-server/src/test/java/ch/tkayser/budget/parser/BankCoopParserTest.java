package ch.tkayser.budget.parser;

import static junit.framework.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import ch.tkayser.budget.dto.TransactionDTO;
import ch.tkayser.budget.parser.impl.mt940.BankCoopParser;

public class BankCoopParserTest extends ParserTestBase {

    private static final String UBS_TESTFILE = "/parser/bankcoop_mt940.sta";

    @Test
    public void testParser() throws Exception {

        // create parser and parse test file
        BankCoopParser parser = new BankCoopParser();
        List<TransactionDTO> parsedTransaction = parser.parserTransaction(getClass().getResource(UBS_TESTFILE)
                .openStream());
        assertNotNull(parsedTransaction);
        for (TransactionDTO tx: parsedTransaction) {
            System.out.println(tx.getValuta());            
            System.out.println(tx.getAmount());
        }
        

    }

}
