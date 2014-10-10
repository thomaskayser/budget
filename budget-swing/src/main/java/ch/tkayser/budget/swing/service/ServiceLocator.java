package ch.tkayser.budget.swing.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.sun.appserv.security.ProgrammaticLogin;

import ch.tkayser.budget.exception.BudgetException;
import ch.tkayser.budget.service.BudgetService;

public class ServiceLocator {
	
    private static BudgetService service;
    
    private static InitialContext context;
    
    public static BudgetService getService() throws BudgetException {
    	if (service == null) {
    		try {
        		initService();    			
    		} catch (Exception e) {
    			throw new BudgetException("Error getting Service", e);
    		}
    	}
    	
    	return service;
    }

	private static void initService() throws Exception {
		if (context == null) {
			initContext();
		}
		
	       service = (BudgetService) context.lookup("java:global/budget-ear-2.0.1-SNAPSHOT/budget-server-2.0.1-SNAPSHOT/BudgetServiceImpl");
	       
	}

	private static void initContext() throws Exception {		
			// init Context
	        Properties p = new Properties();
	        InputStream stream = ServiceLocator.class.getClassLoader().getResourceAsStream("jndi.properties");
	        if (stream == null) {
	        	throw new BudgetException("jndi File not Found");
	        }
	        p.load(stream);
	        context = new InitialContext(p);
			
			
			// login
	        System.setProperty("java.security.auth.login.config", "auth.conf");
	        ProgrammaticLogin pl = new ProgrammaticLogin();
	        pl.login("tom", "arvika");
			

        
		
	}


}
