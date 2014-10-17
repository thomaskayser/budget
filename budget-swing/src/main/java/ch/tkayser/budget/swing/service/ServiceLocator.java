package ch.tkayser.budget.swing.service;

import java.io.InputStream;
import java.util.Properties;

import javax.naming.InitialContext;

import ch.tkayser.budget.exception.BudgetException;
import ch.tkayser.budget.service.BudgetService;

public class ServiceLocator {

    private static BudgetService  service;

    private static InitialContext context;

    private static final String   SERVICE_PROPERTY_FILE       = "service.properties";

    private static final String   BUDGETSERVICE_JNDI_PROPERTY = "budgetservice.jndi";

    public static void init(LoginInformation loginInformation) throws BudgetException {
        context = GlasfishLoginHandler.login(loginInformation);
        initService();
    }

    public static BudgetService getService() {
        return service;
    }

    private static void initService() throws BudgetException {
        try {
            // get jndi name from file
            // Basis properties aus jndi.properties file
            Properties p = new Properties();
            InputStream stream = ServiceLocator.class.getClassLoader().getResourceAsStream(SERVICE_PROPERTY_FILE);
            if (stream == null) {
                throw new BudgetException("jndi File not Found");
            }
            p.load(stream);

            service = (BudgetService) context.lookup(p.getProperty(BUDGETSERVICE_JNDI_PROPERTY));
        } catch (Exception e) {
            throw new BudgetException("Fehler beim Service lookup: " + e.getMessage(), e);
        }
    }

}
