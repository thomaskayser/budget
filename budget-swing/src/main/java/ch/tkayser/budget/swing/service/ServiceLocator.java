package ch.tkayser.budget.swing.service;


import javax.naming.InitialContext;

import ch.tkayser.budget.exception.BudgetException;
import ch.tkayser.budget.service.BudgetService;

public class ServiceLocator {
	
    private static BudgetService service;
    
    private static InitialContext context;
    
    public static void init(LoginInformation loginInformation) throws BudgetException {
        context = GlasfishLoginHandler.login(loginInformation);  
        initService();
    }
    
    public static BudgetService getService() {
    	return service;
    }

	private static void initService() throws BudgetException {
		try {
		       service = (BudgetService) context.lookup("java:global/budget-ear-2.0.1-SNAPSHOT/budget-server-2.0.1-SNAPSHOT/BudgetServiceImpl");	        			
		} catch (Exception e) {
			throw new BudgetException("Fehler beim Service lookup: "+e.getMessage(), e);
		}
	}


}
