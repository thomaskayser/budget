package ch.tkayser.budget.parser.impl;

import ch.tkayser.budget.dto.TransactionDTO;
import ch.tkayser.budget.exception.BudgetException;
import ch.tkayser.budget.parser.TransactionParser;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Tom on 27.02.2015.
 */
public class VisecaCCParser implements TransactionParser {


    private static final String DATE_FORMAT = new String("dd.MM.yy");

    private static Logger logger = LoggerFactory.getLogger(VisecaCCParser.class);

    // name of the parser
    private static String PARSER_NAME = "Viseca Kreditkarten";

    // ignore patterns
    private static final List<String> IGNORE_PATTERNS;
    static {
        IGNORE_PATTERNS = new ArrayList<String>();
        IGNORE_PATTERNS.add("Zahlung");
    }

    @Override
    public List<TransactionDTO> parserTransaction(InputStream input) throws BudgetException {

        List<TransactionDTO> transactions = new ArrayList<TransactionDTO>();

        try {
            PdfReader pdfReader = new PdfReader(input);
            for (int i = 1; i <= pdfReader.getNumberOfPages(); i++) {
                if (pdfReader.getPageN(i) != null) {
                    String pdfText = PdfTextExtractor.getTextFromPage(pdfReader, i);
                    if (pdfText != null) {
                        BufferedReader reader = new BufferedReader(new StringReader(pdfText));
                        String line = reader.readLine();
                        while (line != null) {
                            logger.debug("Line {}", line);
                            TransactionDTO tx = parseTx(line);
                            if (tx != null) {
                                transactions.add(tx);
                            }
                            line = reader.readLine();
                        }
                    }
                }

            }

        } catch (Exception e) {
            throw new BudgetException("Fehler beim parsen des Files: " + e.getMessage(), e);
        }

        return transactions;
    }

    private TransactionDTO parseTx(String line) {

        // check min length
        line = line.trim();
        if (line == null || line.length() < 17) {
            return null;
        }

        // try to parse two dates at the start of the line
        String datePart = line.substring(0, 17);
        String[] parts = datePart.split(" ");
        if (parts == null || parts.length != 2) {
            return null;
        }
        Date txDate = ParserHelper.parseDate(DATE_FORMAT, datePart.substring(0, 8));
        Date valuta = ParserHelper.parseDate(DATE_FORMAT, datePart.substring(8, 17));
        if(txDate == null || valuta == null) {
            return null;
        }

        String rest = line.substring(18);
        if (ignoreLine(rest)) {
            return null;
        }

        int lastSpace = rest.lastIndexOf(" ");
        if (lastSpace == -1) {
            return null;
        }
        String text = rest.substring(0, lastSpace);
        String amountTxt = rest.substring(lastSpace+1);
        BigDecimal amount = ParserHelper.parseAmount(amountTxt);
        if (amount == null) {
            return null;
        }


        TransactionDTO dto = new TransactionDTO();
        dto.setValuta(valuta);
        dto.setBookingText(text);
        dto.setAmount(amount);
        return dto;
    }



    private boolean ignoreLine(String line) {
       for (String ignorePattern : IGNORE_PATTERNS) {
           if (line.startsWith(ignorePattern)) {
               return true;
           }
       }
       return false;
   }

    @Override
    public String getParserName() {
        return PARSER_NAME;
    }



}
